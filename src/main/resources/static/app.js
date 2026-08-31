// Change this line:
const API_URL = 'http://localhost:8082/api/applications';

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
			// Build the timeline HTML
			            let timelineHtml = `<strong>${app.appliedDate}</strong><br><small style="color: #7f8c8d;">${app.daysSinceApplied} days ago</small>`;
			            
			            // If the backend suggested a follow-up date, show it!
			            if (app.followUpDate) {
			                timelineHtml += `<br><small style="color: #e67e22; font-weight: bold;">Follow-up: ${app.followUpDate}</small>`;
			            }

			            tr.innerHTML = `
			                <td>${timelineHtml}</td>
			                <td><strong>${app.companyName}</strong></td>
			                <td>${app.role}</td>
			                <td><span class="badge badge-${app.status}">${app.status.replace('_', ' ')}</span></td>
			                <td>${app.heardBack ? '✅ Yes' : '⏳ No'}</td>
			                <td>
			                    <button class="action-btn btn-edit" onclick="editApplication('${app.id}')">Edit</button>
			                    <button class="action-btn btn-delete" onclick="deleteApplication('${app.id}')">Delete</button>
			                </td>
			            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error loading applications:', error);
    }
}

// Execute search based on filters
function executeSearch() {
    const company = document.getElementById('search-company').value.trim();
    const role = document.getElementById('search-role').value.trim();
    const status = document.getElementById('search-status').value;

    const params = new URLSearchParams();
    if (company) params.append('company', company);
    if (role) params.append('role', role);
    if (status) params.append('status', status);

    loadApplications(params.toString());
}

// Clear search filters and reload all data
function resetSearch() {
    document.getElementById('search-company').value = '';
    document.getElementById('search-role').value = '';
    document.getElementById('search-status').value = '';
    loadApplications(); 
}

// Handle Form Submission (Create and Update)
document.getElementById('add-form').addEventListener('submit', async (e) => {
    e.preventDefault(); 

    const id = document.getElementById('app-id').value; // Check if we are editing
    const newApp = {
        companyName: document.getElementById('companyName').value,
        role: document.getElementById('role').value,
        appliedDate: document.getElementById('appliedDate').value,
        status: document.getElementById('status').value,
        notes: document.getElementById('notes').value
    };

    const msgElement = document.getElementById('form-message');
    
    // Determine if it's a POST or PUT
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;
    
    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newApp)
        });

        if (response.ok) {
            msgElement.style.color = 'green';
            msgElement.innerText = id ? 'Application updated successfully!' : 'Application added successfully!';
            
            cancelEdit(); // Clears the form and resets buttons
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
    
    setTimeout(() => msgElement.innerText = '', 3000); 
});

// Delete an application
async function deleteApplication(id) {
    if (!confirm("Are you sure you want to delete this application?")) return;
    
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (response.ok) {
            loadStatistics();
            loadApplications();
        } else {
            alert("Failed to delete application.");
        }
    } catch (error) {
        console.error('Error deleting:', error);
    }
}

// Fetch application data and populate the form for editing
async function editApplication(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const app = await response.json();
        
        // Populate form
        document.getElementById('app-id').value = app.id;
        document.getElementById('companyName').value = app.companyName;
        document.getElementById('role').value = app.role;
        document.getElementById('appliedDate').value = app.appliedDate;
        document.getElementById('status').value = app.status;
        document.getElementById('notes').value = app.notes || '';
        
        // Update UI for Edit Mode
        document.getElementById('form-title').innerText = "Edit Application";
        document.getElementById('submit-btn').innerText = "Update Application";
        document.getElementById('cancel-edit-btn').style.display = "block";
        
        window.scrollTo(0, 0); // Scroll to top so user sees the form
    } catch (error) {
        console.error('Error fetching application for edit:', error);
    }
}

// Reset form back to "Add Mode"
function cancelEdit() {
    document.getElementById('add-form').reset();
    document.getElementById('app-id').value = '';
    document.getElementById('appliedDate').valueAsDate = new Date(); // reset date to today
    
    // Reset UI
    document.getElementById('form-title').innerText = "Add New Application";
    document.getElementById('submit-btn').innerText = "Save Application";
    document.getElementById('cancel-edit-btn').style.display = "none";
}