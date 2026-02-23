document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".login-form");

    //Checks if user presses login button
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        //Gets username and password from login form
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        try {
            // NOTE: Double check endpoint is accurate after it is written
            const response = await fetch("/api/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            //invalid login
             if (data.key === "error") {
                alert(data.message || "Invalid username or password");
                return;
            }

             //stores session key and username for use
            localStorage.setItem("sessionKey", data.key);
            localStorage.setItem("username", data.username);
            window.location.href = "/admin";

        } catch (error) {
            console.error("Error sending login request:", error);
            alert("An error occurred. Please try again later.");
        }
    });
});
