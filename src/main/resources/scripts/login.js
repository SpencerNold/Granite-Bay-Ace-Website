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

            const data = await response.json();

             if (data.key === "error") {
                alert(data.message || "Invalid username or password");
                return;
            }

            localStorage.setItem("sessionKey", data.key);
            localStorage.setItem("username", data.username);
            window.location.href = "/admin";

        } catch (error) {
            console.error("Error sending login request:", error);
            alert("An error occurred. Please try again later.");
        }
    });
});
