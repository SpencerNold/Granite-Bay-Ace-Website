document.addEventListener("DOMContentLoaded", () => {
    console.log("footer.js DOMContentLoaded");

    document.addEventListener("click", (e) => {
        console.log("CLICK:", e.target);

        if (e.target.id === "openModal") {
            modal.classList.add("show");
        }

        if (
            e.target.id === "closeModal" ||
            e.target.id === "modal"
        ) {
            modal.classList.remove("show");
        }
    });
});