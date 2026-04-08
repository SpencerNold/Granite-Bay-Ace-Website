document.addEventListener("DOMContentLoaded", async () => {
    const backBtn = document.getElementById("backToLoginBtn");
    const form = document.getElementById("adminRecoverForm");
    const msg = document.getElementById("recoverMsg");
    const list = document.getElementById("accountsList");
    const accountsBox = document.getElementById("accountsBox");


    if(accountsBox) {
        accountsBox.classList.add("hidden");
    }
    backBtn.addEventListener("click", () => {
        window.location.href = "/login";
    });

    const response = await fetch('/api/accounts/list', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({})
    });
    if (!response.ok) {
        console.log(response.status)
        return
    }
    let body = await response.json()
    let accounts = body.users
    console.log(JSON.stringify(accounts))


    renderAccounts()

    function renderAccounts() {
        // show the box now that admin is authenticated
        accountsBox.classList.remove("hidden");

        list.innerHTML = "";

        accounts.forEach((acct) => {
            const row = document.createElement("div");
            row.className = "accounts-row twocol";

            row.innerHTML = `
      <div>${escapeHtml(acct.username)}</div>
      <div class="pw-reset-cell">
        <input class="pw-input" type="password" placeholder="New password" />
        <button type="button" class="pw-btn">Set</button>
      </div>`;

            const input = row.querySelector(".pw-input");
            const btn = row.querySelector(".pw-btn");

            // Disable button initially
            btn.disabled = true;

            // Enable only when user types something
            input.addEventListener("input", () => {
                btn.disabled = input.value.trim().length === 0;
            });

            btn.addEventListener("click", async () => {
                const newPass = input.value.trim();

                if (newPass.length < 6) {
                    msg.textContent = "Password must be at least 6 characters.";
                    msg.style.color = "red";
                    return;
                }

                input.value = "";

                let uname = acct,username
                let pword = newPass
                let result = await fetch("/api/recovery/reset", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        username: uname,
                        password: pword
                    })
                })
                if (!result.ok) {
                    msg.textContent = "Failed to update password."
                    msg.style.color = "red"
                    return
                }
                let body = await result.json()
                console.log(JSON.stringify(body))
                msg.textContent = `Password updated for ${acct.username}.`
                msg.style.color = "green"
            });

            list.appendChild(row);
        });
    }

    function escapeHtml(str) {
        return String(str)
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }
});