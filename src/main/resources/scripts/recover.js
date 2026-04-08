document.addEventListener("DOMContentLoaded", async () => {
    const msg = document.getElementById("recoverMsg");
    const list = document.getElementById("accountsList");
    const accountsBox = document.getElementById("accountsBox");


    loadAccounts();




    async function loadAccounts() {
        msg.textContent = "";
        msg.style.color = "";

        try {
            const accountsResponse = await fetch("/api/accounts/list", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({})
            });

            const data = await accountsResponse.json();

            if (data.message !== "ok" || !Array.isArray(data.users)) {
                msg.textContent = "Failed to load accounts.";
                msg.style.color = "red";
                return;
            }

            renderAccounts(data.users);

        } catch (error) {
            console.error("Error loading accounts:", error);
            msg.textContent = "An error occurred while loading accounts.";
            msg.style.color = "red";
        }
    }

    function renderAccounts(accounts) {
        list.innerHTML = "";

        accounts.forEach((acct) => {
            const row = document.createElement("div");
            row.className = "accounts-row twocol";

            row.innerHTML = `
      <div>${escapeHtml(acct.username)}</div>
      <div class="pw-reset-cell">
        <input class="pw-input" type="password" placeholder="New password" />
        <button type="button" class="pw-toggle-btn">Show</button>
        <button type="button" class="pw-btn">Set</button>
      </div>`;

            const input = row.querySelector(".pw-input");
            const btn = row.querySelector(".pw-btn");
            const toggleBtn = row.querySelector(".pw-toggle-btn");


            //show/hide toggle button
            toggleBtn.addEventListener("click", () => {
                if (input.type === "password") {
                    input.type = "text";
                    toggleBtn.textContent = "Hide";
                } else {
                    input.type = "password";
                    toggleBtn.textContent = "Show";
                }
            });


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

                //confirmation message
                const confirmed = window.confirm(`Are you sure you want to set a new password for ${acct.username}?`);
                if (!confirmed) {
                    return;
                }

                // updating passwords functionality 
                try {
                    const response = await fetch("/api/recovery/reset", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        credentials: "include",
                        body: JSON.stringify({
                            username:acct.username,
                            password: newPass
                        })
                    });
                    const data = await response.json();

                    if(data.status !== "ok") {
                        msg.textContent= `Failed to update password for ${acct.username}.`;
                        msg.style.color = "red";
                        return;
                    }

                    msg.textContent = `Password updated for ${acct.username}.`;
                    msg.style.color = "green";
                    input.value = "";
                    toggleBtn.textContent = "Show";
                    btn.diabled = true;
                } catch (error) {
                    console.error("Error updating password:", error);
                    msg.textcContent = "An error occurred while updating password.";
                    msg.style.color = "red";
                }
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