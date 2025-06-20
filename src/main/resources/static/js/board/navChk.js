document.addEventListener("DOMContentLoaded", function () {
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll(".nav-link");

  navLinks.forEach((link) => {
    const href = link.getAttribute("href");
    const absoluteHref = new URL(href, window.location.origin).pathname;

    if (absoluteHref === currentPath) {
      link.classList.add("active");
    } else {
      link.classList.remove("active");
    }
  });
});
