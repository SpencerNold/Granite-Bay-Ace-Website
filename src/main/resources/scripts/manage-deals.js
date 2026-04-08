document.addEventListener("DOMContentLoaded", () => {
    const manageDealsButton = document.getElementById("manageDealsBtn");
    const dealsModal = document.getElementById("dealsModal");
    const closeDealsModal = document.getElementById("closeDealsModal");
    const saveDealsModal = document.getElementById("saveDealsModal");
    const fileInput = document.getElementById("dealPdf");

    if (manageDealsButton && dealsModal) {
        manageDealsButton.addEventListener("click", () => {
            dealsModal.classList.remove("is-hidden");
        });
    }

    if (closeDealsModal && dealsModal) {
        closeDealsModal.addEventListener("click", () => {
            dealsModal.classList.add("is-hidden");
        });
    }

    if (saveDealsModal && fileInput) {
        saveDealsModal.addEventListener("click", async () => {
            const file = fileInput.files[0];

            if (!file) {
                alert("Please select a PDF file to upload.");
                return;
            }

            if (file.type !== "application/pdf") {
                alert("Please upload a PDF file only.");
                return;
            }

            const reader = new FileReader();            // Read the file as a data URL (base64)

            reader.onload = async () => {
                try {
                    const base64 = reader.result.split(",")[1];

                    console.log("file size bytes:", file.size);
                    console.log("base64 length:", base64.length);

                    // Send the base64 string to the server
                    const response = await fetch("/api/admin/upload-advertisement", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            fileName: file.name,
                            fileData: base64
                        })
                    });

                    // Handle the server response
                    if (response.ok) {
                        const result = await response.json();
                        console.log(result);

                        if (result.message !== "ok") {
                            alert("Upload failed on server.");
                            return;
                        }

                        dealsModal.classList.add("is-hidden");

                        const freshUrl = "/uploads/advertisements.pdf?v=" + Date.now();
                        window.open(freshUrl, "_blank");
                    } else {
                        console.error("Upload failed with status:", response.status);
                        console.error(await response.text());
                        alert("Failed to upload the PDF.");
                    }
                } catch (err) {
                    console.error(err);
                    alert("Upload failed.");
                }
            };

            reader.readAsDataURL(file);
        });
    }

    window.addEventListener("click", (event) => {
        if (event.target === dealsModal) {
            dealsModal.classList.add("is-hidden");
        }
    });
});