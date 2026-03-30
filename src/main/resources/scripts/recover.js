document.addEventListener("DOMContentLoaded", () => {
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

    const demoAccounts = [
        { username: "Username 1", status: "Ready" },
        { username: "Username 2", status: "Ready" },
        { username: "Username 3", status: "Ready" },
    ];

    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const u = document.getElementById("adminUsername").value.trim();
        const p = document.getElementById("adminPassword").value.trim();

        // DEMO ONLY: replace with real API call later
        if (u !== "admin" || p !== "admin") {
            msg.textContent = "Invalid admin credentials (demo). Try admin/admin.";
            return;
        }

        msg.textContent = "Admin authenticated (demo).";
        renderAccounts();
    });

    function renderAccounts() {
        // show the box now that admin is authenticated
        accountsBox.classList.remove("hidden");

        list.innerHTML = "";

        demoAccounts.forEach((acct) => {
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

                // DEMO ONLY: replace with real API call later
                msg.textContent = `Password updated for ${acct.username} (demo).`;
                msg.style.color = "green";
                input.value = "";
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