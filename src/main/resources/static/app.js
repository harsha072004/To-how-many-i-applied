const API_URL = 'http://localhost:8080/api/applications';

// Runs when the page loads
document.addEventListener('DOMContentLoaded', () => {
    loadStatistics();
    loadApplications();

    // Set today's date as default in the form
    document.getElementById('appliedDate').valueAsDate = new Date();
});

// Fetch and display statistics
async function loadStatistics() {
    try {
        const response = await fetch(`${API_URL}/statistics`);
        const stats = await response.json();
        
        document.getElementById('stat-total').innerText = stats.totalApplications;
        document.getElementById('stat-applied').innerText = stats.statusCounts['APPLIED'] || 0;
        document.getElementById('stat-interviews').innerText = stats.statusCounts['INTERVIEW'] || 0;
        document.getElementById('stat-rejected').innerText = stats.statusCounts['REJECTED'] || 0;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

// Fetch and display the table (Updated for Search)
async function loadApplications(searchQuery = '') {
    try {
        // If there's a search query, use the /search endpoint. Otherwise, use the base URL.
        const url = searchQuery ? `${API_URL}/search?${searchQuery}` : API_URL;
        
        const response = await fetch(url);
        const applications = await response.json();
        const tbody = document.getElementById('table-body');
        tbody.innerHTML = ''; // Clear table

        applications.forEach(app => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${app.appliedDate}</td>
                <td><strong>${app.companyName}</strong></td>
                <td>${app.role}</td>
                <td><span class="badge badge-${app.status}">${app.status.replace('_', ' ')}</span></td>
                <td>${app.heardBack ? '✅ Yes' : '⏳ No'}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error loading applications:', error);
    }
}

// Handle Form Submission
document.getElementById('add-form').addEventListener('submit', async (e) => {
    e.preventDefault(); // Prevent page reload

    // Build the request object
    const newApp = {
        companyName: document.getElementById('companyName').value,
        role: document.getElementById('role').value,
        appliedDate: document.getElementById('appliedDate').value,
        status: document.getElementById('status').value,
        notes: document.getElementById('notes').value
    };

    const msgElement = document.getElementById('form-message');
    
    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newApp)
        });

        if (response.ok) {
            msgElement.style.color = 'green';
            msgElement.innerText = 'Application added successfully!';
            document.getElementById('add-form').reset();
            document.getElementById('appliedDate').valueAsDate = new Date(); // reset date
            
            // Refresh dashboard data
            loadStatistics();
            loadApplications();
        } else {
            const err = await response.json();
            msgElement.style.color = 'red';
            msgElement.innerText = err.message || 'Validation failed';
        }
    } catch (error) {
        msgElement.style.color = 'red';
        msgElement.innerText = 'Server error';
    }
    
    setTimeout(() => msgElement.innerText = '', 3000); // Clear message after 3 seconds
});
// Execute search based on filters
function executeSearch() {
    const company = document.getElementById('search-company').value.trim();
    const role = document.getElementById('search-role').value.trim();
    const status = document.getElementById('search-status').value;

    // Build the query string using URLSearchParams (a clean way to handle URLs in JS)
    const params = new URLSearchParams();
    if (company) params.append('company', company);
    if (role) params.append('role', role);
    if (status) params.append('status', status);

    // Call loadApplications with the constructed query string
    loadApplications(params.toString());
}

// Clear search filters and reload all data
function resetSearch() {
    document.getElementById('search-company').value = '';
    document.getElementById('search-role').value = '';
    document.getElementById('search-status').value = '';
    loadApplications(); // Loads everything
}