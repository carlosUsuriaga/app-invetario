// Toggle Sidebar
const toggleBtn = document.getElementById('toggleBtn');
const sidebar = document.getElementById('sidebar');

toggleBtn.addEventListener('click', () => {
    sidebar.classList.toggle('collapsed');
});

// Dropdown de Notificaciones
const bellBtn = document.getElementById('bellBtn');
const dropdown = document.getElementById('dropdown');
const markAll = document.getElementById('markAll');
const badge = document.getElementById('badge');

bellBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    dropdown.classList.toggle('show');
});

// Cerrar dropdown al hacer click fuera
document.addEventListener('click', (e) => {
    if (!dropdown.contains(e.target) && !bellBtn.contains(e.target)) {
        dropdown.classList.remove('show');
    }
});

// Marcar todas las notificaciones como leídas
markAll.addEventListener('click', () => {
    badge.textContent = '';
    // Aquí puedes agregar una llamada AJAX para marcar como leídas en el servidor
    console.log('Todas las notificaciones marcadas como leídas');
});

// Highlight del menú activo según la URL actual
const currentPath = window.location.pathname;
const navItems = document.querySelectorAll('.nav-item');

navItems.forEach(item => {
    const href = item.getAttribute('href');
    if (href && currentPath.includes(href.split('?')[0])) {
        item.classList.add('active');
    }
});

// Sidebar responsive para móviles
if (window.innerWidth <= 768) {
    const menuToggle = document.createElement('button');
    menuToggle.className = 'mobile-menu-toggle';
    menuToggle.innerHTML = `
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
    `;

    document.querySelector('.navbar-left').prepend(menuToggle);

    menuToggle.addEventListener('click', () => {
        sidebar.classList.toggle('open');
    });

    // Cerrar sidebar al hacer click en un item (móvil)
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            if (window.innerWidth <= 768) {
                sidebar.classList.remove('open');
            }
        });
    });
}

// Ejemplo: Cargar notificaciones dinámicamente
function loadNotifications() {
    // Aquí puedes hacer una llamada AJAX para obtener notificaciones del servidor
    // fetch('/api/notificaciones')
    //     .then(response => response.json())
    //     .then(data => {
    //         renderNotifications(data);
    //     });

    // Ejemplo de notificación estática
    const notifications = [
        {
            id: 1,
            title: 'Stock bajo',
            message: 'El producto "Laptop HP" tiene stock bajo',
            time: 'Hace 2 horas',
            read: false
        }
    ];

    if (notifications.length > 0) {
        renderNotifications(notifications);
    }
}

function renderNotifications(notifications) {
    const dropdownBody = document.getElementById('dropdownBody');
    const badge = document.getElementById('badge');

    if (notifications.length === 0) {
        dropdownBody.innerHTML = '<p class="empty">No hay notificaciones</p>';
        badge.textContent = '';
        return;
    }

    const unreadCount = notifications.filter(n => !n.read).length;
    badge.textContent = unreadCount > 0 ? unreadCount : '';

    dropdownBody.innerHTML = notifications.map(notif => `
        <div class="notification-item ${notif.read ? 'read' : ''}">
            <div class="notification-header">
                <strong>${notif.title}</strong>
                <span class="notification-time">${notif.time}</span>
            </div>
            <p class="notification-message">${notif.message}</p>
        </div>
    `).join('');
}

// Llamar función para cargar notificaciones al iniciar
// loadNotifications();

// Animación de entrada para las stat cards
const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry, index) => {
        if (entry.isIntersecting) {
            setTimeout(() => {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }, index * 100);
        }
    });
}, { threshold: 0.1 });

document.querySelectorAll('.stat-card').forEach(card => {
    card.style.opacity = '0';
    card.style.transform = 'translateY(20px)';
    card.style.transition = 'all 0.5s ease';
    observer.observe(card);
});