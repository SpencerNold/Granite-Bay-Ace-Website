document.addEventListener("DOMContentLoaded", importAll);

function importAll() {
    importItem("navbar")
    importItem("footer")
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
            const container = document.createElement("div")
            container.innerHTML = html

            if (name === "navbar") {
                document.body.prepend(container);
            }
            else if (name === "footer") {
                document.body.appendChild(container);
            }
            else {
                document.body.appendChild(container);
            }
        });
}