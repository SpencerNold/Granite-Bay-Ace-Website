// ======================
// Gallery logic
// ======================

// Cache DOM elements
const mainImg = document.getElementById("mainAd");
const thumbs = Array.from(document.querySelectorAll(".thumb"));
const rail = document.getElementById("thumbRail");
const storeName = document.getElementById("storeName");
const storeAddr = document.getElementById("storeAddr");
const storeHour = document.getElementById("storeHour");
const storePhone = document.getElementById("storePhone");
const storeEmail = document.getElementById("storeEmail");
let map = document.getElementById("map");

let activeIndex = 0;

let autoTimer = null;
const AUTO_INTERVAL = 7000; // 7 seconds

// Set active thumb + main image
function setActive(index, { scrollIntoView = true } = {}) {
    activeIndex = (index + thumbs.length) % thumbs.length;

    // Update thumb states
    thumbs.forEach((t, i) => {
        const isActive = i === activeIndex;
        t.classList.toggle("active", isActive);
        t.setAttribute("aria-selected", isActive ? "true" : "false");
    });

    // Update main image
    const active = thumbs[activeIndex];

    // Start fade-out
    mainImg.style.opacity = 0;
    storeName.style.opacity = 0;
    storeAddr.style.opacity = 0;
    storeHour.style.opacity = 0;
    storePhone.style.opacity = 0;
    storeEmail.style.opacity = 0;

    // After fade-out, change image and fade back in
    setTimeout(() => {
        mainImg.src = active.dataset.src;
        mainImg.alt = active.dataset.alt;
        mainImg.style.opacity = 1; // Fade back in

        // Update store info box
        storeName.textContent = active.dataset.store || "Store Name";
        storeAddr.textContent = active.dataset.addr || "Store description goes here.";
        storeHour.innerHTML = active.dataset.hour || "Hour goes here.";
        storePhone.textContent = active.dataset.phone || "Phone goes here.";
        storeEmail.textContent = active.dataset.email || "Email goes here.";

        // Google Maps frame changes with the store location
        const query = `${active.dataset.store} ${active.dataset.addr}`;
        const encodedAddr = encodeURIComponent(query);
        if (map) { map.src = `https://www.google.com/maps?q=${encodedAddr}&output=embed`; }

        storeName.style.opacity = 1;
        storeAddr.style.opacity = 1;
        storeHour.style.opacity = 1;
        storePhone.style.opacity = 1;
        storeEmail.style.opacity = 1;
    }, 300); // Match CSS transition duration

    if (scrollIntoView) {
        // Works for vertical rail too
        active.scrollIntoView({ behavior: "smooth", block: "nearest", inline: "center" });
    }
}

// Hover/click/focus on thumbs
thumbs.forEach((btn, i) => {
    btn.addEventListener("mouseenter", () => setActive(i));
    btn.addEventListener("click", () => setActive(i));
    btn.addEventListener("focus", () => setActive(i));
});
// Init
setActive(0, { scrollIntoView: false });

// Helper functions for autoplay
function startAutoPlay() {
    stopAutoPlay(); // Clear any existing timer
    autoTimer = setInterval(() => {
        setActive(activeIndex + 1);
    }, AUTO_INTERVAL);
}

function stopAutoPlay() {
    if (autoTimer) {
        clearInterval(autoTimer);
        autoTimer = null;
    }
}

// Start autoplay on page load
startAutoPlay();
const viewer = document.querySelector(".viewer");
viewer.addEventListener("mouseenter", stopAutoPlay);
viewer.addEventListener("mouseleave", startAutoPlay);

//force fresh download of the advertisement file
document.addEventListener("DOMContentLoaded", () => {
    const link = document.querySelector(".deals-button");

    if (!link) return;
    // Override click to append timestamp for cache busting
    link.addEventListener("click", (event) => {
        event.preventDefault();

        const freshUrl = "/uploads/advertisements.pdf?v=" + Date.now();
        window.open(freshUrl, "_blank");
    });
});