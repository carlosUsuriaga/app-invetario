/**
 * Sidebar Toggle Script
 */

document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.getElementById('toggleBtn');
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    // Recuperar preferencia guardada
    const sidebarCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';

    if (sidebarCollapsed) {
        collapseSidebar();
    }

    // Event listener para el botón
    if (toggleBtn) {
        toggleBtn.addEventListener('click', toggleSidebar);
    }
});

/**
 * Alterna entre sidebar expandido y colapsado
 */
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    if (sidebar.classList.contains('collapsed')) {
        expandSidebar();
    } else {
        collapseSidebar();
    }
}

/**
 * Colapsa el sidebar
 */
function collapseSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    sidebar.classList.add('collapsed');
    mainContent.classList.add('collapsed');
    localStorage.setItem('sidebarCollapsed', 'true');
}

/**
 * Expande el sidebar
 */
function expandSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    sidebar.classList.remove('collapsed');
    mainContent.classList.remove('collapsed');
    localStorage.setItem('sidebarCollapsed', 'false');
}