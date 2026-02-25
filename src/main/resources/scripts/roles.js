const SESSION_KEY = localStorage.getItem('key');
const urlParams = new URLSearchParams(window.location.search);
const targetUser = urlParams.get('user');

async function init() {
    /* uncomment when testing
    if (!targetUser) {
        alert("No user specified.");
        window.location.href = "/manage-account.html";
        return;
    }
   */

    // verifies permissions
    const res = await fetch('/api/accounts/list', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ sessionKey: SESSION_KEY })
    });

    const { users, callerRole, message } = await res.json();
    if (message !== "ok") { return; }

    const isAdmin = (callerRole === 0);
    const target = users.find(u => u.username === targetUser);
    const adminBox = document.getElementById('roleAdmin');
    const managerBox = document.getElementById('roleManager');

    if (target) {
        // document.getElementById('displayUsername').innerText = targetUser;
        // document.getElementById('targetInfo').innerText = `Assign roles for ${targetUser}`;
        if (target.roleId === 0) {
            adminBox.checked = true;
        } else {
            managerBox.checked = true;
        }
    }

    // only admin can make changes
    if (!isAdmin) {
        document.querySelector('.save-button').style.display = 'none';
        adminBox.disabled = true;
        managerBox.disabled = true;
    }
}

async function saveChanges() {
    // determines which role is selected
    const selectedRole = document.getElementById('roleAdmin').checked ? 0 : 1;

    const res = await fetch('/api/accounts/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            sessionKey: SESSION_KEY,
            username: targetUser,
            roleId: selectedRole
        })
    });

    const result = await res.json();
    if (result.message === "ok") {
        alert("Role updated!");
        window.location.href = "/manage-account.html";
    } else {
        alert("Failed to update: " + result.message);
    }
}
document.querySelector('.save-button').addEventListener('click', saveChanges);
document.addEventListener('DOMContentLoaded', init);