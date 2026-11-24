const CORE_API_BASE = window.CORE_API_BASE || "http://localhost:8080/api/v1";
const REALTIME_WS_URL = window.REALTIME_WS_URL || "ws://localhost:8081/ws";

async function loadSports() {
    const listEl = document.getElementById("sports-list");
    try {
        const res = await fetch(`${CORE_API_BASE}/sports`);
        const sports = await res.json();
        listEl.innerHTML = sports
            .map(s => `<li><strong>${s.name}</strong> (${s.code}) &mdash; ${s.playersOnField} pe teren / max ${s.maxRosterSize} în lot</li>`)
            .join("");
    } catch (err) {
        listEl.innerHTML = `<li>Core Service (:8080) nu este pornit încă.</li>`;
    }
}

document.addEventListener("DOMContentLoaded", loadSports);
