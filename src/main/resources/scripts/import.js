document.addEventListener("DOMContentLoaded", importAll);

async function importAll() {
    await importItem("navbar", (element) => {
        document.body.prepend(element)
    })
    await importItem("footer", (element) => {
        document.body.append(element)
    })
}

function importItem(name, func) {
    return new Promise((resolve) => {
    if (!document.querySelector(`link[href="/${name}.css"]`)) {
        const link = document.createElement("link")
        link.rel = "stylesheet"
        link.href = `/${name}.css`
        document.head.appendChild(link)
    }
    fetch(`/${name}.html`)
        .then(response => response.text())
        .then(html => {
            const element = document.createElement("div")
            element.innerHTML = html
            func(element)
            resolve();
        });
    });
}

document.addEventListener("click", function(event) {
    if (event.target.classList.contains("remove-btn")) {
        const card = event.target.closest(".card");
        card.remove();
    }
});