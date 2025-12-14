/**
 * Dashboard Reportes Script
 * Carga reportes mini en el dashboard
 */

document.addEventListener('DOMContentLoaded', () => {
    cargarResumenGeneral();
    cargarStockBajo();
    cargarProximosVencer();
});

/**
 * Carga resumen general
 */
async function cargarResumenGeneral() {
    try {
        const response = await fetch('/reporte/api/resumen');
        if (!response.ok) {
            mostrarError('resumenGeneral', 'Error al cargar');
            return;
        }

        const datos = await response.json();
        const container = document.getElementById('resumenGeneral');

        if (datos.error) {
            mostrarError('resumenGeneral', datos.error);
            return;
        }

        const html = `
            <div class="stats-mini">
                <div class="stat-mini success">
                    <p class="stat-mini-label">Productos</p>
                    <p class="stat-mini-value">${datos.totalProductos || 0}</p>
                </div>
                <div class="stat-mini">
                    <p class="stat-mini-label">Valor Total</p>
                    <p class="stat-mini-value">S/ ${formatNumber(datos.valorTotal || 0)}</p>
                </div>
                <div class="stat-mini">
                    <p class="stat-mini-label">Stock Total</p>
                    <p class="stat-mini-value">${datos.stockTotal || 0}</p>
                </div>
                <div class="stat-mini">
                    <p class="stat-mini-label">Promedio</p>
                    <p class="stat-mini-value">S/ ${formatNumber(datos.precioPromedio || 0)}</p>
                </div>
            </div>

            <div class="recomendaciones-mini">
                ${datos.productosVencidos > 0 ? `
                    <div class="recom-item danger">
                        <span class="recom-icon">🔴</span>
                        <span class="recom-text"><strong>${datos.productosVencidos}</strong> vencidos</span>
                    </div>
                ` : ''}
                ${datos.productosProximosVencer > 0 ? `
                    <div class="recom-item warning">
                        <span class="recom-icon">⏰</span>
                        <span class="recom-text"><strong>${datos.productosProximosVencer}</strong> vencen pronto</span>
                    </div>
                ` : ''}
                ${datos.productosStockBajo > 0 ? `
                    <div class="recom-item warning">
                        <span class="recom-icon">⚠️</span>
                        <span class="recom-text"><strong>${datos.productosStockBajo}</strong> stock bajo</span>
                    </div>
                ` : ''}
            </div>
        `;

        container.innerHTML = html;
    } catch (err) {
        console.error('Error:', err);
        mostrarError('resumenGeneral', 'Error al cargar');
    }
}

/**
 * Carga stock bajo
 */
async function cargarStockBajo() {
    try {
        const response = await fetch('/reporte/api/stock-bajo');
        if (!response.ok) {
            mostrarError('stockBajo', 'Error al cargar');
            return;
        }

        const datos = await response.json();
        const container = document.getElementById('stockBajo');

        if (datos.error || !datos.productos || datos.productos.length === 0) {
            container.innerHTML = '<div class="empty-chart"><div class="empty-chart-icon">✅</div><p>Stock adecuado</p></div>';
            return;
        }

        const productosLimitados = datos.productos.slice(0, 5);
        const html = `
            <div class="productos-mini-list">
                ${productosLimitados.map(p => `
                    <div class="producto-mini-item">
                        <div>
                            <p class="producto-mini-nombre">${p.nombre}</p>
                        </div>
                        <div style="display: flex; gap: 0.5rem; align-items: center;">
                            <span class="producto-mini-valor">${p.stockActual}/${p.stockMinimo}</span>
                            <span class="producto-mini-stock">Bajo</span>
                        </div>
                    </div>
                `).join('')}
            </div>
            ${datos.totalProductos > 5 ? `<p style="text-align: center; color: #999; font-size: 0.9rem; margin-top: 1rem;">+${datos.totalProductos - 5} más</p>` : ''}
        `;

        container.innerHTML = html;
    } catch (err) {
        console.error('Error:', err);
        mostrarError('stockBajo', 'Error al cargar');
    }
}

/**
 * Carga próximos a vencer
 */
async function cargarProximosVencer() {
    try {
        const response = await fetch('/reporte/api/proximos-vencer');
        if (!response.ok) {
            mostrarError('proximosVencer', 'Error al cargar');
            return;
        }

        const datos = await response.json();
        const container = document.getElementById('proximosVencer');

        if (datos.error || !datos.productos || datos.productos.length === 0) {
            container.innerHTML = '<div class="empty-chart"><div class="empty-chart-icon">✅</div><p>Sin problemas</p></div>';
            return;
        }

        const productosLimitados = datos.productos.slice(0, 5);
        const html = `
            <div class="productos-mini-list">
                ${productosLimitados.map(p => {
                    const dias = calcularDias(p.fechaVencimiento);
                    return `
                        <div class="producto-mini-item">
                            <div>
                                <p class="producto-mini-nombre">${p.nombre}</p>
                                <small style="color: #999; font-size: 0.8rem;">Vence: ${formatFecha(p.fechaVencimiento)}</small>
                            </div>
                            <span class="producto-mini-stock warning">${dias}d</span>
                        </div>
                    `;
                }).join('')}
            </div>
            ${datos.totalProductos > 5 ? `<p style="text-align: center; color: #999; font-size: 0.9rem; margin-top: 1rem;">+${datos.totalProductos - 5} más</p>` : ''}
        `;

        container.innerHTML = html;
    } catch (err) {
        console.error('Error:', err);
        mostrarError('proximosVencer', 'Error al cargar');
    }
}

/**
 * Muestra error
 */
function mostrarError(containerId, mensaje) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = `
            <div class="empty-chart" style="color: #f44336;">
                <div class="empty-chart-icon">❌</div>
                <p>${mensaje}</p>
            </div>
        `;
    }
}

/**
 * Formatea números
 */
function formatNumber(num) {
    return parseFloat(num).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

/**
 * Formatea fecha
 */
function formatFecha(fecha) {
    try {
        const date = new Date(fecha);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    } catch {
        return fecha;
    }
}

/**
 * Calcula días restantes
 */
function calcularDias(fecha) {
    try {
        const today = new Date();
        const target = new Date(fecha);
        const difference = target - today;
        const dias = Math.ceil(difference / (1000 * 60 * 60 * 24));
        return Math.max(dias, 0);
    } catch {
        return 0;
    }
}