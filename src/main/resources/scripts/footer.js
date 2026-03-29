document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modal");

    document.addEventListener("click", (e) => {

        if (e.target.id === "openModal") {
            modal.classList.add("show");
        }

        if (e.target.id === "modal") {
            modal.classList.remove("show");
        }
    });
});