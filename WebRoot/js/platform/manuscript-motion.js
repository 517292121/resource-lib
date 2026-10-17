(function () {
    "use strict";
    function init() {
        var scene = document.querySelector(".dh-manuscript");
        if (!scene || scene.classList.contains("dh-motion-ready")) return;
        var reduced = window.matchMedia("(prefers-reduced-motion: reduce)");
        var finePointer = window.matchMedia("(hover: hover) and (pointer: fine)");
        var frame = 0, pointer = null;
        var stage = scene.closest(".dh-stage");
        var counters = [];
        var counterFrame = 0;
        function finishCounters() {
            if (counterFrame) window.cancelAnimationFrame(counterFrame);
            counterFrame = 0;
            counters.forEach(function (counter) { counter.element.textContent = counter.original; });
            counters = [];
        }
        function tickCounters(time) {
            counterFrame = 0;
            counters = counters.filter(function (counter) {
                if (counter.start === null) counter.start = time;
                var progress = Math.min(1, (time - counter.start) / 1500);
                var value = Math.round(counter.target * (1 - Math.pow(1 - progress, 3)));
                counter.element.textContent = progress === 1 ? counter.original : (counter.grouped ? value.toLocaleString("en-US") : String(value));
                return progress < 1;
            });
            if (counters.length) counterFrame = window.requestAnimationFrame(tickCounters);
        }
        function reveal(element) {
            if (element.classList.contains("dh-is-revealed")) return;
            element.classList.add("dh-is-revealed");
            var number = element.querySelector(".dh-stat-value strong");
            if (!number || reduced.matches || document.hidden) return;
            var original = number.textContent.trim();
            if (!/^\d[\d,]*$/.test(original)) return;
            number.setAttribute("aria-label", original);
            counters.push({ element: number, original: original, target: Number(original.replace(/,/g, "")), grouped: original.indexOf(",") !== -1, start: null });
            if (!counterFrame) counterFrame = window.requestAnimationFrame(tickCounters);
        }
        if (stage) {
            var revealElements = stage.querySelectorAll(".dh-stat-card, .dh-introduction h2, .dh-introduction p");
            var revealObserver = window.IntersectionObserver ? new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (!entry.isIntersecting) return;
                    reveal(entry.target);
                    revealObserver.unobserve(entry.target);
                });
            }, { threshold: .15 }) : null;
            revealElements.forEach(function (element, index) {
                element.classList.add("dh-reveal-element");
                element.style.setProperty("--dh-reveal-delay", (index % 4 * .14) + "s");
                if (revealObserver) revealObserver.observe(element);
                else reveal(element);
            });
            stage.classList.add("dh-presentation-ready");
        }
        function artwork(className) {
            var element = document.createElement("div");
            element.className = "dh-motion-art " + className;
            element.setAttribute("aria-hidden", "true");
            scene.appendChild(element);
            return element;
        }
        // Separate the roll at the user-marked seam; retain both layers at rest.
        var originalArtwork = scene.querySelector(".dh-manuscript-image");
        var sheet = null;
        var roller = null;
        var cleanBoardReady = false;
        if (originalArtwork) {
            artwork("dh-scroll-board");
            sheet = artwork("dh-scroll-sheet");
            var tuck = artwork("dh-scroll-tuck");
            roller = artwork("dh-scroll-roller");
            [sheet, tuck, roller].forEach(function (layer) {
                var image = document.createElement("img");
                image.src = originalArtwork.currentSrc || originalArtwork.src;
                image.alt = "";
                image.className = "dh-scroll-source";
                image.draggable = false;
                layer.appendChild(image);
            });
            var cleanBoard = new Image();
            cleanBoard.addEventListener("load", function () {
                cleanBoardReady = true;
                scene.classList.add("dh-scroll-ready");
                if (originalArtwork.complete && originalArtwork.naturalWidth) unfold();
            }, { once: true });
            cleanBoard.src = (window.basePath || "./") + "images/platform/psb/home/manuscript-table-clean-v1.png";
            sheet.addEventListener("animationend", function (event) {
                if (event.animationName !== "dh-unroll-paper") return;
                scene.classList.remove("dh-scroll-opening");
                scene.classList.add("dh-scroll-open");
            });
        }
        artwork("dh-ambient-light");
        artwork("dh-light-rays");
        artwork("dh-paper-light");
        var trace = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        trace.setAttribute("viewBox", "0 0 1000 300");
        trace.setAttribute("preserveAspectRatio", "none");
        trace.setAttribute("class", "dh-motion-art dh-table-trace");
        trace.setAttribute("aria-hidden", "true");
        trace.setAttribute("focusable", "false");
        var path = document.createElementNS(trace.namespaceURI, "path");
        path.setAttribute("d", "M 125 12 L 870 12 L 973 284 L 27 284 Z");
        path.setAttribute("pathLength", "1000");
        trace.appendChild(path);
        scene.appendChild(trace);
        var dust = artwork("dh-gold-dust");
        for (var i = 0; i < 32; i++) {
            var mote = document.createElement("i");
            mote.style.setProperty("--x", (7 + (i * 37 % 87)) + "%");
            mote.style.setProperty("--y", (22 + (i * 23 % 73)) + "%");
            mote.style.setProperty("--size", (i % 6 === 0 ? 5 : 1.5 + i % 3 * .6) + "px");
            mote.style.setProperty("--duration", (7 + i % 7) + "s");
            mote.style.setProperty("--delay", (-i * 1.37) + "s");
            mote.style.setProperty("--drift", ((i % 2 ? 1 : -1) * (12 + i % 20)) + "px");
            dust.appendChild(mote);
        }
        function unfold() {
            if (!sheet || !cleanBoardReady || reduced.matches) return;
            if (scene.classList.contains("dh-scroll-opening") || scene.classList.contains("dh-scroll-open")) return;
            scene.classList.add("dh-scroll-opening");
            sync();
        }
        function resetPointer() {
            if (frame) window.cancelAnimationFrame(frame);
            frame = 0;
            pointer = null;
            scene.style.setProperty("--dh-rx", "0deg");
            scene.style.setProperty("--dh-ry", "0deg");
        }
        function sync() {
            var still = reduced.matches;
            scene.classList.toggle("dh-motion-paused", still || document.hidden);
            scene.classList.toggle("dh-motion-still", still);
            if (stage) {
                stage.classList.toggle("dh-presentation-paused", still || document.hidden);
                stage.classList.toggle("dh-presentation-still", still);
            }
            if (still || document.hidden) resetPointer();
            if (still || document.hidden) finishCounters();
        }
        scene.addEventListener("pointermove", function (event) {
            if (reduced.matches || !finePointer.matches || event.pointerType === "touch") return;
            pointer = { x: event.clientX, y: event.clientY };
            if (frame) return;
            frame = window.requestAnimationFrame(function () {
                frame = 0;
                if (!pointer) return;
                var bounds = scene.getBoundingClientRect();
                var x = Math.max(0, Math.min(1, (pointer.x - bounds.left) / bounds.width));
                var y = Math.max(0, Math.min(1, (pointer.y - bounds.top) / bounds.height));
                scene.style.setProperty("--dh-rx", ((.5 - y) * 1.5).toFixed(2) + "deg");
                scene.style.setProperty("--dh-ry", ((x - .5) * 2).toFixed(2) + "deg");
                scene.style.setProperty("--dh-light-x", (x * 100).toFixed(1) + "%");
                scene.style.setProperty("--dh-light-y", (y * 100).toFixed(1) + "%");
            });
        });
        scene.addEventListener("pointerleave", resetPointer);
        scene.addEventListener("pointercancel", resetPointer);
        document.addEventListener("visibilitychange", sync);
        if (reduced.addEventListener) reduced.addEventListener("change", sync);
        else reduced.addListener(sync);
        scene.classList.add("dh-motion-ready");
        sync();
        if (originalArtwork && !reduced.matches) {
            if (originalArtwork.complete && originalArtwork.naturalWidth) unfold();
            else originalArtwork.addEventListener("load", unfold, { once: true });
        }
    }
    if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
    else init();
})();
