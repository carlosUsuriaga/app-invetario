document.getElementById("buscar").addEventListener("input", function () {
    fetch("/producto/buscar?nombre=" + encodeURIComponent(this.value))
        .then(r => r.json())
        .then(data => {
            let tbody = document.getElementById("tabla-body");
            tbody.innerHTML = "";

            data.forEach(c => {
                const estado = c.estado ? 'Activo' : 'Inactivo';
                const estadoClass = c.estado ? 'badge badge-active' : 'badge badge-inactive';
                const stockClass = c.stockActual <= c.stockMinimo ? 'stock-badge stock-low' : 'stock-badge stock-ok';

                tbody.innerHTML += `
                    <tr data-categoria-id="${c.categoria.idCategoria}"
                        data-unidad-id="${c.unidadMedida.id}"
                        data-fecha-vencimiento="${c.fechaVencimiento || ''}"
                        data-precio="${c.precioUnitario}"
                        data-estado="${c.estado}">
                        <td class="td-id">${c.idProducto}</td>
                        <td class="td-nombre">${c.nombre}</td>
                        <td class="td-descripcion">${c.descripcion || ''}</td>
                        <td>
                            <span class="badge-categoria">${c.categoria.nombre}</span>
                        </td>
                        <td>
                            <span class="badge-unidad">${c.unidadMedida.nombre}</span>
                        </td>
                        <td>
                            <span class="${stockClass}">${c.stockActual}</span>
                        </td>
                        <td>
                            <span class="stock-min">${c.stockMinimo}</span>
                        </td>
                        <td>
                            <span class="precio">S/ ${c.precioUnitario}</span>
                        </td>
                        <td>${c.fechaVencimiento || ''}</td>
                        <td>
                            <span class="${estadoClass}">${estado}</span>
                        </td>
                        <td>${c.fechaRegistro}</td>
                        <td>${c.fechaActualizacion}</td>
                        <td>
                            <div class="action-buttons">
                                <button class="btn-editar">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                                    </svg>
                                    Editar
                                </button>
                                <a href="/producto/anular/${c.idProducto}"
                                   class="btn-anular"
                                   onclick="return confirm('¿Anular este producto?')">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <polyline points="3 6 5 6 21 6"></polyline>
                                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                                    </svg>
                                    Anular
                                </a>
                            </div>
                        </td>
                    </tr>
                `;
            });

            asignarEventos();
        });
});


function asignarEventos() {
    document.querySelectorAll(".btn-editar").forEach(button => {
        button.addEventListener("click", function() {
            const row = this.closest('tr');

            // Recupero datos de la fila
            const idProducto = row.cells[0].innerText;
            const nombre = row.cells[1].innerText;
            const descripcion = row.cells[2].innerText;
            const categoriaId = row.getAttribute('data-categoria-id');
            const unidadMedidaId = row.getAttribute('data-unidad-id');
            const stockActual = row.cells[5].innerText;
            const stockMinimo = row.cells[6].innerText;
            const precioUnitario = row.cells[7].innerText.replace('S/ ', '');
            const fechaVencimiento = row.getAttribute('data-fecha-vencimiento');
            const estado = row.getAttribute('data-estado') === 'true';
            // Lleno los campos del modal
            document.getElementById('editId').value = idProducto;
            document.getElementById('editNombre').value = nombre;
            document.getElementById('editDescripcion').value = descripcion;
            document.getElementById('editCategoria').value = categoriaId;
            document.getElementById('editUnidadMedida').value = unidadMedidaId;
            document.getElementById('editStockActual').value = stockActual;
            document.getElementById('editStockMinimo').value = stockMinimo;
            document.getElementById('editPrecioUnitario').value = precioUnitario;
            document.getElementById('editFechaVencimiento').value = fechaVencimiento;
            document.getElementById('editEstado').value = estado ? 'true' : 'false';

            // Muestro el modal
            document.getElementById('modal').style.display = 'block';
        });
    });
}

asignarEventos();

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}