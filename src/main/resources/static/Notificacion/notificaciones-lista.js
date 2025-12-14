/**
 * Script para la página completa de notificaciones
 * Las funciones de marcar, eliminar y verificar ya están en el HTML inline
 */

document.addEventListener('DOMContentLoaded', () => {
    // Inicializar tooltips si es necesario
    inicializarTooltips();
});

/**
 * Inicializa tooltips (opcional)
 */
function inicializarTooltips() {
    const elementos = document.querySelectorAll('[title]');
    elementos.forEach(el => {
        el.addEventListener('mouseenter', mostrarTooltip);
        el.addEventListener('mouseleave', ocultarTooltip);
    });
}

function mostrarTooltip(e) {
    // Aquí puedes agregar lógica para mostrar tooltips personalizados
}

function ocultarTooltip(e) {
    // Aquí puedes agregar lógica para ocultar tooltips personalizados
}