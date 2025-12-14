/**
 * Vista Completa de Reportes Script
 */

const ENDPOINTS = {
    resumen: '/reporte/api/resumen',
    inventario: '/reporte/api/inventario',
    'stock-bajo': '/reporte/api/stock-bajo',
    'proximos-vencer': '/reporte/api/proximos-vencer',
    vencidos: '/reporte/api/vencidos',
    'sin-movimiento': '/reporte/api/sin-movimiento',
    movimientos: '/reporte/api/movimientos',
    alertas: '/reporte/api/alertas'
};

const TITULOS = {
    resumen: '📈 Resumen General del Sistema',
    inventario: '📦 Reporte de Inventario Completo',
    'stock-bajo': '⚠️ Productos con Stock Bajo',
    'proximos-vencer': '⏰ Productos Próximos a Vencer',
    vencidos: '🔴 Productos Vencidos',
    'sin-movimiento': '📦 Productos Sin Movimiento',
    movimientos: '🔄 Movimientos de Inventario',
    alertas: '🔔 Reporte de Alertas'
};

let reporteActual = 'resumen';

document.addEventListener('DOMContentLoaded', () => {
    cargarReporte('resumen');
});

function cambiarReporte() {
    const tipo = document.getElementById('filtroTipo').value;
    cargarReporte(tipo);
}

async function cargarReporte(tipo) {
    reporteActual = tipo;
    const container = document.getElementById('resumenReporte');
    const endpoint = ENDPOINTS[tipo];

    if (!endpoint) {
        container.innerHTML = '<div class="empty-reporte"><p>Reporte no encontrado</p></div>';
        return;
    }

    container.innerHTML = '<div class="loading">Cargando reporte...</div>';

    try {
        const response = await fetch(endpoint);
        if (!response.ok) {
            container.innerHTML = '<div class="empty-reporte"><div class="empty-reporte-icon">❌</div><p>Error al cargar</p></div>';
            return;
        }

        const datos = await response.json();
        if (datos.error) {
            container.innerHTML = `<div class="empty-reporte"><div class="empty-reporte-icon">❌</div><p>${datos.error}</p></div>`;
            return;
        }

        let html = '';
        switch(tipo) {
            case 'resumen': html = renderResumen(datos); break;
            case 'inventario': html = renderInventario(datos); break;
            case 'stock-bajo': html = renderStockBajo(datos); break;
            case 'proximos-vencer': html = renderProximosVencer(datos); break;
            case 'vencidos': html = renderVencidos(datos); break;
            case 'sin-movimiento': html = renderSinMovimiento(datos); break;
            case 'movimientos': html = renderMovimientos(datos); break;
            case 'alertas': html = renderAlertas(datos); break;
            default: html = '<p>Reporte desconocido</p>';
        }

        container.innerHTML = html;
    } catch (err) {
        console.error('Error:', err);
        container.innerHTML = '<div class="empty-reporte"><div class="empty-reporte-icon">❌</div><p>Error al cargar</p></div>';
    }
}

function renderResumen(datos) {
    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS.resumen}</h2>
            <div class="reporte-meta">
                <span>Generado: ${formatFecha(new Date())}</span>
                <span>Por: ${datos.generadoPor || 'Sistema'}</span>
            </div>
        </div>

        <div class="stats-grid-reporte">
            <div class="stat-card-reporte">
                <p class="stat-label-reporte">Total Productos</p>
                <p class="stat-value-reporte">${datos.totalProductos || 0}</p>
            </div>
            <div class="stat-card-reporte">
                <p class="stat-label-reporte">Stock Total</p>
                <p class="stat-value-reporte">${datos.stockTotal || 0}</p>
            </div>
            <div class="stat-card-reporte">
                <p class="stat-label-reporte">Valor Total</p>
                <p class="stat-value-reporte">S/ ${formatNumber(datos.valorTotal || 0)}</p>
            </div>
            <div class="stat-card-reporte">
                <p class="stat-label-reporte">Precio Promedio</p>
                <p class="stat-value-reporte">S/ ${formatNumber(datos.precioPromedio || 0)}</p>
            </div>
        </div>

        <div class="stats-grid-reporte">
            <div class="stat-card-reporte danger">
                <p class="stat-label-reporte">Vencidos</p>
                <p class="stat-value-reporte">${datos.productosVencidos || 0}</p>
            </div>
            <div class="stat-card-reporte warning">
                <p class="stat-label-reporte">Vencen Pronto</p>
                <p class="stat-value-reporte">${datos.productosProximosVencer || 0}</p>
            </div>
            <div class="stat-card-reporte warning">
                <p class="stat-label-reporte">Stock Bajo</p>
                <p class="stat-value-reporte">${datos.productosStockBajo || 0}</p>
            </div>
            <div class="stat-card-reporte">
                <p class="stat-label-reporte">Alertas Sin Leer</p>
                <p class="stat-value-reporte">${datos.alertasNoLeidas || 0}</p>
            </div>
        </div>

        <div class="recomendaciones-reporte">
            <h3>💡 Recomendaciones</h3>
            <div class="recomendaciones-lista">
                ${datos.productosVencidos > 0 ? `<div class="recom-item-reporte">Eliminar ${datos.productosVencidos} productos vencidos</div>` : ''}
                ${datos.productosProximosVencer > 0 ? `<div class="recom-item-reporte">Planificar salida de ${datos.productosProximosVencer} productos</div>` : ''}
                ${datos.productosStockBajo > 0 ? `<div class="recom-item-reporte">Reponer ${datos.productosStockBajo} productos</div>` : ''}
            </div>
        </div>
    `;
}

function renderInventario(datos) {
    if (!datos.productos || datos.productos.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">📭</div><p>Sin productos</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS.inventario}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalProductos}</span>
                <span>Activos: ${datos.productosActivos}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Stock</th>
                        <th>Precio</th>
                        <th>Valor</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.productos.map(p => `
                        <tr>
                            <td>${p.nombre}</td>
                            <td>${p.categoria.nombre}</td>
                            <td>${p.stockActual}/${p.stockMinimo}</td>
                            <td>S/ ${formatNumber(p.precioUnitario)}</td>
                            <td>S/ ${formatNumber(p.precioUnitario * p.stockActual)}</td>
                            <td><span class="badge-reporte ${p.estado ? 'badge-activo' : 'badge-inactivo'}">${p.estado ? 'Activo' : 'Inactivo'}</span></td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderStockBajo(datos) {
    if (!datos.productos || datos.productos.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">✅</div><p>Stock adecuado</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS['stock-bajo']}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalProductos}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Stock Actual</th>
                        <th>Stock Mínimo</th>
                        <th>Diferencia</th>
                        <th>Categoría</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.productos.map(p => `
                        <tr>
                            <td>${p.nombre}</td>
                            <td><span class="badge-reporte badge-alerta">${p.stockActual}</span></td>
                            <td>${p.stockMinimo}</td>
                            <td>${p.stockActual - p.stockMinimo}</td>
                            <td>${p.categoria.nombre}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderProximosVencer(datos) {
    if (!datos.productos || datos.productos.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">✅</div><p>Sin problemas</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS['proximos-vencer']}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalProductos}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Vencimiento</th>
                        <th>Días</th>
                        <th>Stock</th>
                        <th>Categoría</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.productos.map(p => `
                        <tr>
                            <td>${p.nombre}</td>
                            <td>${formatFecha(p.fechaVencimiento)}</td>
                            <td><span class="badge-reporte badge-alerta">${calcularDias(p.fechaVencimiento)}d</span></td>
                            <td>${p.stockActual}</td>
                            <td>${p.categoria.nombre}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderVencidos(datos) {
    if (!datos.productos || datos.productos.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">✅</div><p>Sin productos vencidos</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS.vencidos}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalProductos}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Vencimiento</th>
                        <th>Días Vencido</th>
                        <th>Stock</th>
                        <th>Categoría</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.productos.map(p => `
                        <tr>
                            <td>${p.nombre}</td>
                            <td>${formatFecha(p.fechaVencimiento)}</td>
                            <td><span class="badge-reporte badge-alerta">${Math.abs(calcularDias(p.fechaVencimiento))}d</span></td>
                            <td>${p.stockActual}</td>
                            <td>${p.categoria.nombre}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderSinMovimiento(datos) {
    if (!datos.productos || datos.productos.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">✅</div><p>Todos en movimiento</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS['sin-movimiento']}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalProductos}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Última Actualización</th>
                        <th>Semanas</th>
                        <th>Stock</th>
                        <th>Categoría</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.productos.map(p => `
                        <tr>
                            <td>${p.nombre}</td>
                            <td>${formatFecha(p.fechaActualizacion)}</td>
                            <td><span class="badge-reporte badge-alerta">${calcularSemanas(p.fechaActualizacion)}s</span></td>
                            <td>${p.stockActual}</td>
                            <td>${p.categoria.nombre}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderMovimientos(datos) {
    if (!datos.movimientos || datos.movimientos.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">📭</div><p>Sin movimientos</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS.movimientos}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalMovimientos}</span>
                <span>Entradas: ${datos.entradas}</span>
                <span>Salidas: ${datos.salidas}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Tipo</th>
                        <th>Cantidad</th>
                        <th>Fecha</th>
                        <th>Usuario</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.movimientos.slice(0, 50).map(m => `
                        <tr>
                            <td>${m.producto.nombre}</td>
                            <td><span class="badge-reporte ${m.tipo === 'ENTRADA' ? 'badge-ok' : 'badge-alerta'}">${m.tipo}</span></td>
                            <td>${m.cantidad}</td>
                            <td>${formatFecha(m.fechaRegistro)}</td>
                            <td>${m.usuario ? m.usuario.nombre : 'Sistema'}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderAlertas(datos) {
    if (!datos.notificaciones || datos.notificaciones.length === 0) {
        return `<div class="empty-reporte"><div class="empty-reporte-icon">✅</div><p>Sin alertas</p></div>`;
    }

    return `
        <div class="reporte-header-info">
            <h2 class="reporte-titulo">${TITULOS.alertas}</h2>
            <div class="reporte-meta">
                <span>Total: ${datos.totalAlertas}</span>
                <span>No Leídas: ${datos.alertasNoLeidas}</span>
            </div>
        </div>

        <div class="tabla-reporte-contenedor">
            <table class="tabla-reporte">
                <thead>
                    <tr>
                        <th>Tipo</th>
                        <th>Mensaje</th>
                        <th>Producto</th>
                        <th>Fecha</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    ${datos.notificaciones.slice(0, 50).map(n => `
                        <tr>
                            <td><span class="badge-reporte" style="background: ${getTipoColor(n.tipo)}; color: white;">${n.tipo}</span></td>
                            <td>${n.mensaje}</td>
                            <td>${n.producto ? n.producto.nombre : '-'}</td>
                            <td>${formatFecha(n.fechaRegistro)}</td>
                            <td><span class="badge-reporte ${n.leido ? 'badge-ok' : 'badge-alerta'}">${n.leido ? 'Leído' : 'No Leído'}</span></td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

// Funciones auxiliares
function formatFecha(fecha) {
    try {
        const date = new Date(fecha);
        return date.toLocaleDateString('es-PE', { year: 'numeric', month: '2-digit', day: '2-digit' });
    } catch {
        return fecha;
    }
}

function formatNumber(num) {
    return parseFloat(num).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function calcularDias(fecha) {
    try {
        const today = new Date();
        const target = new Date(fecha);
        const diff = target - today;
        return Math.ceil(diff / (1000 * 60 * 60 * 24));
    } catch {
        return 0;
    }
}

function calcularSemanas(fecha) {
    try {
        const today = new Date();
        const target = new Date(fecha);
        const diff = today - target;
        return Math.floor(diff / (1000 * 60 * 60 * 24 * 7));
    } catch {
        return 0;
    }
}

function getTipoColor(tipo) {
    const colores = {
        'INFO': '#2196F3',
        'ADVERTENCIA': '#FF9800',
        'ALERTA': '#FF9800',
        'CRITICO': '#F44336'
    };
    return colores[tipo] || '#667eea';
}

function imprimirReporte() {
    window.print();
}

function descargarReporte() {
    alert('Función en desarrollo');
}