(function () {
    "use strict";
    function ready() {
        // Keep the artwork effect separate from navigation and data dialogs.
        if (document.querySelector(".dh-manuscript")) {
            var motionStyle = document.createElement("link");
            motionStyle.rel = "stylesheet";
            motionStyle.href = (window.basePath || "./") + "css/platform/manuscript-motion.css?v=17";
            document.head.appendChild(motionStyle);
            var motionScript = document.createElement("script");
            motionScript.src = (window.basePath || "./") + "js/platform/manuscript-motion.js?v=17";
            document.head.appendChild(motionScript);
        }
        // Upgrade buttons in any already-compiled JSP to the same native links.
        var libraryEntry = document.querySelector(".dh-library-entry");
        if (libraryEntry) document.querySelectorAll("button.dh-stat-view").forEach(function (button) {
            var link = document.createElement("a");
            link.className = button.className;
            link.href = libraryEntry.href;
            link.setAttribute("aria-label", (button.getAttribute("aria-label") || "查看") + "，进入文献库");
            link.textContent = button.textContent;
            button.parentNode.replaceChild(link, button);
        });
        var locale = document.getElementById("dh-language");
        if (locale) locale.addEventListener("change", function () {
            if (typeof window.switch_lang === "function") window.switch_lang(locale.value);
            else if (window.basePath) window.location.href = window.basePath + "locale.htm?locale=" + encodeURIComponent(locale.value) + "&from=" + encodeURIComponent(window.basePath + "index");
        });
        var login = document.querySelector("[data-home-login]");
        if (login) login.addEventListener("click", function () {
            if (typeof window.openLogin === "function") window.openLogin();
            else window.location.href = (window.basePath || "./") + "member/login";
        });
    }
    if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", ready);
    else ready();
})();
