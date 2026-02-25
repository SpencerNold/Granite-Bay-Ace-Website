/*
This is an import for the admin page to edit admin page
 */

document.addEventListener("DOMContentLoaded", importAdmin);

async function importAdmin() {
    const existingContent = Array.from(document.body.childNodes);

    const mainWrapper = document.createElement("main");
    existingContent.forEach(node => mainWrapper.appendChild(node));
    document.body.appendChild(mainWrapper);

    await importItem("AdminNavbar", (element) => {
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

