
    /**
     * Script para gestionar notificaciones en el navbar
     * Carga dinámicamente y actualiza el dropdown
     */

    const bellBtn = document.getElementById('bellBtn');
    const dropdown = document.getElementById('dropdown');
    const badge = document.getElementById('badge');
    const dropdownBody = document.getElementById('dropdownBody');
    const markAll = document.getElementById('markAll');

    let actualizandose = false;

    /**
     * Inicializa cuando carga la página
     */
    document.addEventListener('DOMContentLoaded', () => {
        iniciarActualizacionAutomatica();
    });
    function iniciarActualizacionAutomatica() {
        cargarConteo();

        setInterval(() => {
            if (!dropdown.classList.contains('show')) {
                cargarConteo();
            }
        }, 5000);
    }

    /**
     * Toggle del dropdown
     */
    bellBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        dropdown.classList.toggle('show');
        if (dropdown.classList.contains('show')) {
            cargarNotificaciones();
        }
    });

    /**
     * Cerrar dropdown cuando se hace clic afuera
     */
    document.addEventListener('click', () => {
        dropdown.classList.remove('show');
    });

    /**
     * No cerrar dropdown cuando se hace clic dentro
     */
    dropdown.addEventListener('click', (e) => {
        e.stopPropagation();
    });

    /**
     * Carga el conteo de notificaciones no leídas
     */
    async function cargarConteo() {
        try {
            const res = await fetch('/notificacion/count');

            if (!res.ok) {
                console.error('Error al obtener conteo:', res.status);
                return;
            }

            const count = await res.json();

            if (count > 0) {
                badge.textContent = count > 99 ? '99+' : count;
                badge.classList.add('show');
            } else {
                badge.classList.remove('show');
            }
        } catch (err) {
            console.error('Error en cargarConteo:', err);
        }
    }

    /**
     * Carga las notificaciones no leídas al abrir el dropdown
     */
    async function cargarNotificaciones() {
        if (actualizandose) return;

        actualizandose = true;

        try {
            const res = await fetch('/notificacion/lista');

            if (!res.ok) {
                console.error('Error al obtener notificaciones:', res.status);
                dropdownBody.innerHTML = '<p class="empty">Error al cargar notificaciones</p>';
                actualizandose = false;
                return;
            }

            const notifs = await res.json();

            if (notifs.length === 0) {
                dropdownBody.innerHTML = '<p class="empty">No hay notificaciones pendientes</p>';
                actualizandose = false;
                return;
            }

            // Construir HTML de las notificaciones
            dropdownBody.innerHTML = notifs.map(n => {
                const icono = obtenerIconoTipo(n.tipo);
                const fecha = formatFecha(n.fechaRegistro);
                const clase = !n.leido ? 'unread' : '';

                return `
                    <div class="notif-item ${clase}" onclick="marcarNotificacionLeida(${n.idNotificacion})">
                        <div class="notif-item-header">
                            <span class="notif-icono">${icono}</span>
                            <span class="notif-tipo-badge tipo-${n.tipo.toLowerCase()}">${n.tipo}</span>
                        </div>
                        <div class="notif-item-content">
                            <p class="notif-mensaje">${n.mensaje}</p>
                            <small class="notif-fecha">${fecha}</small>
                        </div>
                    </div>
                `;
            }).join('');

            actualizandose = false;
        } catch (err) {
            console.error('Error en cargarNotificaciones:', err);
            dropdownBody.innerHTML = '<p class="empty">Error al cargar notificaciones</p>';
            actualizandose = false;
        }
    }

    /**
     * Marca una notificación como leída cuando se hace clic
     */
    async function marcarNotificacionLeida(id) {
        try {
            const res = await fetch(`/notificacion/marcar/${id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            if (res.ok) {
                // Recargar conteo y notificaciones
                await cargarConteo();
                await cargarNotificaciones();
            } else {
                console.error('Error al marcar como leído:', res.status);
            }
        } catch (err) {
            console.error('Error en marcarNotificacionLeida:', err);
        }
    }

    /**
     * Marca todas las notificaciones como leídas
     */
    markAll.addEventListener('click', async () => {
        try {
            const res = await fetch('/notificacion/marcar-todas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            if (res.ok) {
                await cargarConteo();
                await cargarNotificaciones();
            } else {
                console.error('Error al marcar todas:', res.status);
            }
        } catch (err) {
            console.error('Error en markAll:', err);
        }
    });

    /**
     * Formatea la fecha relativa (Hace X tiempo)
     */
    function formatFecha(fecha) {
        try {
            const diff = Math.floor((new Date() - new Date(fecha)) / 1000);

            if (diff < 60) return 'Hace un momento';
            if (diff < 3600) return `Hace ${Math.floor(diff / 60)} min`;
            if (diff < 86400) return `Hace ${Math.floor(diff / 3600)} hrs`;
            if (diff < 604800) return `Hace ${Math.floor(diff / 86400)} días`;

            return new Date(fecha).toLocaleDateString('es-PE');
        } catch (err) {
            return 'Fecha desconocida';
        }
    }

    /**
     * Obtiene el icono según el tipo de notificación
     */
    function obtenerIconoTipo(tipo) {
        const iconos = {
            'INFO': 'ℹ️',
            'ADVERTENCIA': '⚠️',
            'ALERTA': '🔔',
            'CRITICO': '🔴'
        };
        return iconos[tipo] || '📬';
    }