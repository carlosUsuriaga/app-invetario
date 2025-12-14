document.getElementById("buscar").addEventListener("input", function () {
    fetch("/unidadmedida/buscar?nombre=" + encodeURIComponent(this.value))
        .then(r => r.json())
        .then(data => {
            let tbody = document.getElementById("tabla-body");
            tbody.innerHTML = "";

            data.forEach(u => {
                tbody.innerHTML += `
                    <tr>
                        <td class="td-id">${u.id}</td>
                        <td class="td-nombre">${u.nombre}</td>
                        <td>
                            <span class="badge-abrev">${u.abreviatura}</span>
                        </td>
                        <td>
                            <span class="${u.estado ? 'badge badge-active' : 'badge badge-inactive'}">
                                ${u.estado ? 'Activo' : 'Inactivo'}
                            </span>
                        </td>
                        <td>${u.fechaRegistro}</td>
                        <td>${u.fechaActualizacion}</td>
                        <td>
                            <div class="action-buttons">
                                <button class="btn-editar">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                                    </svg>
                                    Editar
                                </button>
                                <a href="/unidadmedida/anular/${u.id}"
                                   class="btn-anular"
                                   onclick="return confirm('¿Anular esta unidad de medida?')">
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
        button.addEventListener("click", function () {
            const row = this.closest('tr');

            const id = row.cells[0].innerText;
            const nombre = row.cells[1].innerText;
            const abreviatura = row.cells[2].innerText;
            const estado = row.getAttribute('data-estado') === 'true';

            document.getElementById('editId').value = id;
            document.getElementById('editNombre').value = nombre;
            document.getElementById('editAbreviatura').value = abreviatura;
            document.getElementById('editEstado').value = estado ? 'true' : 'false';

            document.getElementById('modal').style.display = 'block';
        });
    });
}
asignarEventos();

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
    window.location.href = '/unidadmedida/lista';
}

