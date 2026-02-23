// retrieves sesison key from local storage
const SESSION_KEY = localStorage.getItem('key');

async function loadTable() {
    // uncomment when session keys can actually be created
    /*if (!SESSION_KEY) {
        window.location.href = "/login.html";
        return;
    } */

    try {
        const res = await fetch('/api/accounts/list', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ sessionKey: SESSION_KEY })
        });

        const { users, callerRole, message } = await res.json();
        if (message !== "ok") { return; }

        const isAdmin = callerRole === 0;

        const tbody = document.getElementById('userTableBody');
        tbody.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');

            // Logic: Admins see active delete checkboxes, Managers see disabled ones.
            row.innerHTML = `
                <td>${user.username}</td>
                <td><span class="role-badge">${user.roleId === 0 ? 'Admin' : 'Manager'}</span></td>
                <td>
                    <input type="checkbox" 
                           ${isAdmin ? '' : 'disabled'} 
                           onchange="if(this.checked) confirmDelete('${user.username}', this)">
                </td>
                <td>
                    <a href="/roles.html?user=${user.username}" class="edit-link">Edit Role →</a>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.error("Failed to load user table", e);
    }
}

async function confirmDelete(username, checkbox) {
    if (confirm(`Are you sure you want to delete ${username}?`)) {
        const res = await fetch('/api/accounts/delete', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ sessionKey: SESSION_KEY, username: username })
        });
        const result = await res.json();
        if (result.message === "ok") {
            await loadTable();
        } else {
            alert("Error: " + result.message);
            checkbox.checked = false;
        }
    } else {
        checkbox.checked = false;
    }
}

document.addEventListener('DOMContentLoaded', loadTable);