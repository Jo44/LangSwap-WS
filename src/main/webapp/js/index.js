/*
 * Index - JS
 *
 * @author Jo44
 * @version 1.2 (26/04/2026)
 * @since 21/04/2026
 */

/* On loading */
document.addEventListener("DOMContentLoaded", async () => {

    // Elements
    const statusElement = document.getElementById("status");
    const tableBodyElement = document.getElementById("translations-body");
    const downloadButton = document.getElementById("download-csv");
    const sortableHeaders = Array.from(document.querySelectorAll("th.sortable"));

    // API URL
    const translationsUrl = new URL("/LangSwap-WS/api/home/", window.location.href);

    // Create text cell
    const createTextCell = (value) => {
        const cell = document.createElement("td");
        cell.textContent = value ?? "-";
        return cell;
    };

    // Format timestamp -> "dd/MM/yyyy - HH:mm"
    const formatTimestamp = (timestamp) => {
        const date = new Date(timestamp * 1000);
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year} - ${hours}:${minutes}`;
    };

    // Create language cell
    const createLanguageCell = (languageId) => {
        const cell = document.createElement("td");
        const flag = getLanguageFlag(languageId);
        if (flag == null) {
            cell.textContent = "-";
            return cell;
        }
        const img = document.createElement("img");
        img.src = flag.src;
        img.alt = flag.alt;
        img.title = flag.alt;
        img.className = "language-flag";
        cell.appendChild(img);
        return cell;
    };

    // Get language flag
    const getLanguageFlag = (languageId) => {
        const languageFlags = {
            0: { src: "./img/flags/jp.svg", alt: "Japan" },
            1: { src: "./img/flags/us.svg", alt: "USA" },
            2: { src: "./img/flags/de.svg", alt: "Germany" },
            3: { src: "./img/flags/fr.svg", alt: "France" }
        };
        return languageFlags[languageId] ?? null;
    };

    // Sorting state
    let currentSortKey = null;
    let currentSortDirection = "asc";

    // Sort values by supported property type
    const compareValues = (leftValue, rightValue, sortKey) => {
        if (sortKey === "actionId" || sortKey === "languageId" || sortKey === "creationDate") {
            const leftNumber = Number(leftValue ?? 0);
            const rightNumber = Number(rightValue ?? 0);
            return leftNumber - rightNumber;
        }

        const leftText = String(leftValue ?? "").toLowerCase();
        const rightText = String(rightValue ?? "").toLowerCase();
        return leftText.localeCompare(rightText, "fr");
    };

    // Return a sorted copy of translations depending on current state
    const getSortedTranslations = (translations) => {
        if (!currentSortKey) {
            return [...translations];
        }

        const sortedTranslations = [...translations].sort((leftTranslation, rightTranslation) => {
            const comparison = compareValues(leftTranslation[currentSortKey], rightTranslation[currentSortKey], currentSortKey);
            if (comparison !== 0) {
                return currentSortDirection === "asc" ? comparison : -comparison;
            }
            return Number(leftTranslation.actionId ?? 0) - Number(rightTranslation.actionId ?? 0);
        });

        return sortedTranslations;
    };

    // Render table body from an array
    const renderTable = (translations) => {
        tableBodyElement.innerHTML = "";

        translations.forEach((translation) => {
            const row = document.createElement("tr");

            row.appendChild(createTextCell(translation.actionId));
            row.appendChild(createTextCell(translation.obfuscatedName));
            row.appendChild(createLanguageCell(translation.languageId));
            row.appendChild(createTextCell(translation.deobfuscatedName));
            row.appendChild(createTextCell(formatTimestamp(translation.creationDate)));
            row.appendChild(createTextCell(translation.characterName));

            tableBodyElement.appendChild(row);
        });
    };

    // Update arrows and ARIA state on headers
    const updateSortHeaders = () => {
        sortableHeaders.forEach((headerElement) => {
            const sortKey = headerElement.dataset.sortKey;
            headerElement.classList.remove("sorted-asc", "sorted-desc");
            headerElement.setAttribute("aria-sort", "none");

            if (sortKey === currentSortKey) {
                const ariaSort = currentSortDirection === "asc" ? "ascending" : "descending";
                headerElement.classList.add(currentSortDirection === "asc" ? "sorted-asc" : "sorted-desc");
                headerElement.setAttribute("aria-sort", ariaSort);
            }
        });
    };

    // Main logic
    try {
        // Fetch translations from API
        const response = await fetch(translationsUrl);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        const translations = await response.json();
        
        // Check if there are translations to display
        if (!Array.isArray(translations) || translations.length === 0) {
            statusElement.textContent = "No translations found";
            return;
        }

        // Default sort (latest uploads first)
        currentSortKey = "creationDate";
        currentSortDirection = "desc";
        renderTable(getSortedTranslations(translations));
        updateSortHeaders();

        // Click on headers to sort by column
        sortableHeaders.forEach((headerElement) => {
            headerElement.addEventListener("click", () => {
                const sortKey = headerElement.dataset.sortKey;

                if (currentSortKey === sortKey) {
                    currentSortDirection = currentSortDirection === "asc" ? "desc" : "asc";
                } else {
                    currentSortKey = sortKey;
                    currentSortDirection = "asc";
                }

                renderTable(getSortedTranslations(translations));
                updateSortHeaders();
            });
        });

        // Update status
        statusElement.textContent = `${translations.length} translations loaded`;

        // Download CSV button
        downloadButton.disabled = false;
        downloadButton.addEventListener("click", () => {
            const headers = ["actionId", "obfuscatedName", "deobfuscatedName", "languageId"];
            const rows = translations.map((t) =>
                headers.map((h) => t[h] ?? "").join(";")
            );
            const csv = [...rows].join("\r\n");
            const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
            const url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "extracted_translations.csv";
            a.click();
            URL.revokeObjectURL(url);
        });

    } catch (error) {
        // Handle errors
        statusElement.textContent = "Unable to load translations";
        console.error(error);
    }
});
