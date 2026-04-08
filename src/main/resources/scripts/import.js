document.addEventListener("DOMContentLoaded", importAll);

async function importAll() {
    const existingContent = Array.from(document.body.childNodes);

    const mainWrapper = document.createElement("main");
    existingContent.forEach(node => mainWrapper.appendChild(node));
    document.body.appendChild(mainWrapper);

    //choose navbar
    const navbarName = await chooseNavbarName();

    await importItem(navbarName, (element) => {
        document.body.prepend(element)
    })

    await importItem("footer", (element) => {
        document.body.append(element)
    })
}

//check if admin/manager is logged in to display admin navbar
async function chooseNavbarName() {
    try {
        const res = await fetch("/get-role-value", {
            method: "GET",
            credentials: "include",
        });
        if (!res.ok) {
            return "navbar";
        }
        const data = await res.json();
        const level = data.level;
        if (level === 0 || level === 1) {   // checks for Admin or Manager role. Displays AdminNavbar if role is found.
            localStorage.setItem('isAdmin', 'true');
            return "AdminNavbar";
        }
    } catch (e) {
        console.error("Error fetching role", e);
    }

    localStorage.setItem('isAdmin', 'false');
    return "navbar";
}

function importItem(name, func) {
    return new Promise((resolve) => {
    if (!document.querySelector(`link[href="/${name}.css"]`)) {
        const link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = `/${name}.css`;
        document.head.appendChild(link);
    }
    fetch(`/${name}.html`)
        .then(response => response.text())
        .then(html => {
            const element = document.createElement("div");
            element.innerHTML = html;
            func(element);
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