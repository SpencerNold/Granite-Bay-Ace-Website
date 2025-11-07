document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".login-form");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        try {
            // NOTE: Double check endpoint is accurate after it is written
            const response = await fetch("/api/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password })
            });

            // Scrum-136 goes here (parse response (generate error/cookie))
        } catch (error) {
            console.error("Error sending login request:", error);
        }
    });
});
