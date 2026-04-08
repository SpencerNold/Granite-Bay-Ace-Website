// retrieves sesison key from local storage
async function loadTable() {
    try {
        const res = await fetch('/api/accounts/list', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({})
        });

        const { users, callerRole, message } = await res.json();

        if (message !== "ok") { return; }

        const isAdmin = callerRole === 0;

        const tbody = document.getElementById('userTableBody');
        tbody.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');
            row.setAttribute('data-username', user.username);

            // Admins see active delete checkboxes, Managers see disabled ones.
            row.innerHTML = `
                <td>${user.username}</td>
                <td>
                    <input type="checkbox" class="delete-checkbox" ${isAdmin ? '' : 'disabled'}>
                </td>
                <td>
                    <select class="role-select" ${isAdmin ? '' : 'disabled'}>
                        <option value="0" ${user.roleId === 0 ? 'selected' : ''}>Admin</option>
                        <option value="1" ${user.roleId === 1 ? 'selected' : ''}>Manager</option>
                    </select>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.error("Could not load user table", e);
    }
}

// Saves changes in table
async function saveAllChanges() {
    const rows = document.querySelectorAll('#userTableBody tr');

    for(const row of rows) {
        const username = row.getAttribute('data-username');
        const isMarkedDelete = row.querySelector('.delete-checkbox').checked;
        const selectedRole = row.querySelector('.role-select').value;

        //save delete account
        if (isMarkedDelete) {
            await fetch('/api/accounts/delete', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ username: username })
            });
            continue;
        }

        //save updated roles
        await fetch ('/api/accounts/add', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                username: username,
                roleId: parseInt(selectedRole)
            })
        });
    }

    loadTable();
}

// toggles "add account" form
function toggleAddForm() { document.getElementById('addAccountForm').classList.toggle('hidden'); }

// redirects to recover page when "recover password" button is pressed
function redirectRecoverPage() { window.location.href = '/recover'; }

// successful logout when "logout" button is pressed
function logout() {
    localStorage.removeItem("sessionKey")
    window.location.href = "/"
}

// Save account in add account form
async function saveNewAccount() {
    const username = document.getElementById('newUsername').value;
    const password = document.getElementById('newPassword').value;
    const roleId = document.getElementById('newRole').value;

    if(!username || !password) return alert("Please fill in all fields");

    const res = await fetch('/api/accounts/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            username,
            password,
            roleId: parseInt(roleId)
        })
    });

    if ((await res.json()).message === "ok") {
        toggleAddForm();
        loadTable()
        document.getElementById("newUsername").value = "";
        document.getElementById("newPassword").value = "";
    }
}

// removes session cookie to successfully logout
async function logout() {
    fetch('/api/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
    })
    .then(response => {
        if (!response.ok) {
            return Promise.reject('Logout failed: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Logout successful:', data);
        localStorage.removeItem('session');
        sessionStorage.removeItem('session');
        localStorage.removeItem('isAdmin');
        window.location.href = '/index.html';
    })
    .catch(error => {
        console.error('Error logging out:', error);
    });
}

//document.getElementById('btnSaveAll').addEventListener('click', saveAllChanges);
document.getElementById('btnSaveTable').addEventListener('click', saveAllChanges);

// Button functionality
document.addEventListener('DOMContentLoaded', () => {
  loadTable();

  safeOnClick('btnRefreshTable', loadTable);
  safeOnClick('btnCancelTable', loadTable);
  safeOnClick('btn-save', saveNewAccount);
  safeOnClick('logoutBtn', logout);
  safeOnClick('recoverPassBtn', redirectRecoverPage);

});