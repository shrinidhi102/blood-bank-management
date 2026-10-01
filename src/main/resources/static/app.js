
const api = "/api";

async function request(url, options = {}) {
    const response = await fetch(api + url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        }
    });

    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        throw new Error(error.detail || error.message ||
            `Request failed (${response.status})`);
    }

    return response.status === 204 ? null : response.json();
}

function showMessage(id, message, error = false) {
    const element = document.getElementById(id);
    element.textContent = message;
    element.style.color = error ? "#b42318" : "#187443";
}

function addCell(row, value) {
    const cell = document.createElement("td");
    cell.textContent = value ?? "";
    row.appendChild(cell);
}

function renderRows(tableId, rows, fields) {
    const table = document.getElementById(tableId);
    table.replaceChildren();

    if (!rows.length) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.colSpan = fields.length;
        cell.textContent = "No records found";
        row.appendChild(cell);
        table.appendChild(row);
        return;
    }

    rows.forEach(item => {
        const row = document.createElement("tr");
        fields.forEach(field => addCell(row, item[field]));
        table.appendChild(row);
    });
}

async function loadBloodGroups() {
    const groups = await request("/blood-groups");
    const select = document.getElementById("donorBloodGroup");

    select.replaceChildren(new Option("Select blood group", ""));

    groups.forEach(group => {
        select.add(new Option(
            group.groupName,
            group.bloodGroupId
        ));
    });
}

async function loadDonors() {
    const donors = await request("/donors");

    renderRows("donorTable", donors, [
        "donorId", "name", "age", "phone", "bloodGroup"
    ]);

    document.getElementById("donorCount").textContent =
        donors.length;

    const select = document.getElementById("donationDonor");
    const previous = select.value;

    select.replaceChildren(new Option("Select donor", ""));

    donors.forEach(donor => {
        select.add(new Option(
            `${donor.name} (${donor.bloodGroup})`,
            donor.donorId
        ));
    });

    if (previous) select.value = previous;
}

async function loadStock() {
    const stock = await request("/blood-units");

    renderRows("stockTable", stock, [
        "group_name", "available_units"
    ]);

    document.getElementById("unitCount").textContent =
        stock.reduce((sum, item) => sum + item.available_units, 0);
}

async function loadDonations() {
    const donations = await request("/donations");

    renderRows("donationTable", donations, [
        "donation_id", "donor_name", "group_name",
        "quantity_ml", "units_donated", "donation_date"
    ]);

    document.getElementById("donationCount").textContent =
        donations.length;
}

async function loadReport() {
    const report = await request("/reports/below-average");

    renderRows("reportTable", report, [
        "group_name", "available_units"
    ]);
}

async function refreshDashboard() {
    try {
        await Promise.all([
            loadBloodGroups(),
            loadDonors(),
            loadStock(),
            loadDonations(),
            loadReport()
        ]);
    } catch (error) {
        console.error(error);
        alert("Could not load dashboard: " + error.message);
    }
}

document.getElementById("donorForm").addEventListener(
    "submit", async event => {
        event.preventDefault();

        const form = new FormData(event.target);

        const donor = {
            name: form.get("name").trim(),
            age: Number(form.get("age")),
            gender: form.get("gender"),
            phone: form.get("phone"),
            bloodGroupId: Number(form.get("bloodGroupId"))
        };

        try {
            await request("/donors", {
                method: "POST",
                body: JSON.stringify(donor)
            });

            event.target.reset();
            showMessage("donorMessage", "Donor registered.");
            await refreshDashboard();
        } catch (error) {
            showMessage("donorMessage", error.message, true);
        }
    }
);

document.getElementById("donationForm").addEventListener(
    "submit", async event => {
        event.preventDefault();

        const form = new FormData(event.target);

        const donation = {
            donorId: Number(form.get("donorId")),
            quantityMl: Number(form.get("quantityMl")),
            unitsDonated: Number(form.get("unitsDonated")),
            donationDate: form.get("donationDate")
        };

        try {
            await request("/donations", {
                method: "POST",
                body: JSON.stringify(donation)
            });

            showMessage(
                "donationMessage",
                "Donation registered and stock updated."
            );

            event.target.reset();
            document.querySelector(
                '#donationForm [name="quantityMl"]'
            ).value = 450;
            document.querySelector(
                '#donationForm [name="unitsDonated"]'
            ).value = 1;

            await refreshDashboard();
        } catch (error) {
            showMessage("donationMessage", error.message, true);
        }
    }
);

document.getElementById("refreshButton").addEventListener(
    "click", refreshDashboard
);

document.querySelector(
    '#donationForm [name="donationDate"]'
).value = new Date().toLocaleDateString("en-CA");

refreshDashboard();