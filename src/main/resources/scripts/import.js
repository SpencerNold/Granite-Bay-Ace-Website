document.addEventListener("DOMContentLoaded", importAll);

async function importAll() {
    const existingContent = Array.from(document.body.childNodes);

    const mainWrapper = document.createElement("main");
    existingContent.forEach(node => mainWrapper.appendChild(node));
    document.body.appendChild(mainWrapper);

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