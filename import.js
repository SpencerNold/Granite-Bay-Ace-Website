document.addEventListener("DOMContentLoaded", importAll);

function importAll() {
    importItem("navbar")
}

function importItem(name) {
    if (!document.querySelector(`link[href="/items/${name}.css"]`)) {
        const link = document.createElement("link")
        link.rel = "stylesheet"
        link.href = `/items/${name}.css`
        document.head.appendChild(link)
    }
    fetch(`/items/${name}.html`)
        .then(response => response.text())
        .then(html => {
            const navbar = document.createElement("div")
            navbar.innerHTML = html
            console.log(html)
            document.body.prepend(navbar)
        });
}