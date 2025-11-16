/*
This is an import for the admin page to edit admin page
 */

document.addEventListener("DOMContentLoaded", importAdmin);

async function importAdmin() {
    await importItem("AdminNavbar", (element) => {
        document.body.prepend(element)
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

