document.getElementById("weather-form").addEventListener("submit", function(event) {
    event.preventDefault();

    const a = document.getElementById("a").value;
    const b = document.getElementById("b").value;
    const c = document.getElementById("c").value;
    const tRange = document.getElementById("t-range").value;

    fetch("http://localhost:8080/backend/weather", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `a=${a}&b=${b}&c=${c}&t-range=${tRange}`
    })
    .then(response => response.json())
    .then(data => drawGraph(data))
    .catch(error => console.error("Error:", error));
});

function drawGraph(data) {
    const canvas = document.getElementById("graph");
    const ctx = canvas.getContext("2d");

    ctx.clearRect(0, 0, canvas.width, canvas.height); // Clear the previous drawing
    ctx.strokeStyle = "blue";
    ctx.lineWidth = 2;

    ctx.beginPath();

    let firstPoint = true;
    let previousX = 0;
    let previousY = 0;

    for (const [time, temp] of Object.entries(data)) {
        const x = 50 + parseInt(time) * 30; // Adjust scaling for time
        const y = 350 - temp * 2; // Adjust scaling for temperature

        if (firstPoint) {
            ctx.moveTo(x, y);
            firstPoint = false;
        } else {
            ctx.lineTo(x, y);
        }

        previousX = x;
        previousY = y;
    }

    ctx.stroke();

    // Draw X and Y Axis
    ctx.strokeStyle = "black";
    ctx.beginPath();
    ctx.moveTo(50, 0);
    ctx.lineTo(50, 400);
    ctx.moveTo(0, 350);
    ctx.lineTo(600, 350);
    ctx.stroke();

    // Labels
    ctx.font = "16px Arial";
    ctx.fillText("Time (minutes)", 300, 380);
    ctx.save();
    ctx.translate(10, 200);
    ctx.rotate(-Math.PI / 2);
    ctx.fillText("Temperature (°C)", 0, 0);
    ctx.restore();
}
