document.addEventListener("DOMContentLoaded", importAll);

function importAll() {
    importItem("navbar", (element) => {
        document.body.prepend(element)
    })
    importItem("footer", (element) => {
        document.body.append(element)
    })
}

function importItem(name, func) {
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
        });
}