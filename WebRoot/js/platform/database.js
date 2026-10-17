(function () {
  "use strict";

  var chart = document.getElementById("db-bars");
  var tooltip = document.getElementById("db-bar-tooltip");
  var exportButton = document.getElementById("db-export");

  if (chart && tooltip) {
    chart.addEventListener("pointerover", function (event) {
      var bar = event.target.closest(".db-bar-item");
      if (!bar) return;
      tooltip.textContent = bar.dataset.month + " · " + bar.dataset.count + " 次访问";
      tooltip.style.display = "block";
      var chartBounds = chart.getBoundingClientRect();
      var barBounds = bar.getBoundingClientRect();
      tooltip.style.left = Math.max(12, Math.min(barBounds.left - chartBounds.left, chartBounds.width - 150)) + "px";
      tooltip.style.top = Math.max(0, barBounds.top - chartBounds.top - 30) + "px";
    });
    chart.addEventListener("pointerout", function (event) {
      if (event.target.closest(".db-bar-item")) tooltip.style.display = "none";
    });
  }

  if (exportButton && chart) {
    exportButton.addEventListener("click", function () {
      var rows = [["月份", "访问量"]];
      chart.querySelectorAll(".db-bar-item").forEach(function (bar) {
        rows.push([bar.dataset.month, bar.dataset.count]);
      });
      var csv = "\uFEFF" + rows.map(function (row) {
        return row.map(function (cell) { return '"' + String(cell).replace(/"/g, '""') + '"'; }).join(",");
      }).join("\r\n");
      var link = document.createElement("a");
      link.href = URL.createObjectURL(new Blob([csv], { type: "text/csv;charset=utf-8" }));
      link.download = "敦煌数据库月度访问量.csv";
      link.click();
      URL.revokeObjectURL(link.href);
    });
  }
})();
