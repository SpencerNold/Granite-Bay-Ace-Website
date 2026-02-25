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

//check if admin is logged in to display admin navbar
async function chooseNavbarName() {
    //for easy persistence across pages
    const role = localStorage.getItem("role");
    if(role === "admin") {
        return "AdminNavbar"
    }
    if (role && role !== "admin") {
        return "navbar";
    }

    //server verified persistent behavior
    const key = localStorage.getItem("sessionKey");
    if(!key) {
        return "navbar";
    }

    try {
        const res = await fetch("/api/session", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ key })
        });
        if (!res.ok) {
            return "navbar";
        }
            const data = await res.json();

            //field names; expects { ok: true, role: admin }
            if (data && data.ok === true && data.role === "admin") {
                localStorage.setItem("role", "admin");
                return "AdminNavbar";
            }
        } catch (e) {
            console.warn("Session check failed: ", e);

        }
        return "navbar";


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