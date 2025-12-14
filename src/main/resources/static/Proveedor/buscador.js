document.getElementById("buscar").addEventListener("input", function() {
    fetch("/proveedor/buscar?nombre=" + encodeURIComponent(this.value))
        .then(r => r.json())
        .then(data => {
            let tbody = document.getElementById("tabla-body");
            tbody.innerHTML = "";

            data.forEach(c => {
                tbody.innerHTML += `
                    <tr>
                        <td class="td-id">${c.idProveedor}</td>
                        <td class="td-nombre">${c.nombre}</td>
                        <td>
                            <span class="badge-ruc">${c.ruc}</span>
                        </td>
                        <td>
                            <a href="tel:${c.telefono}" class="contact-link">
                                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                                </svg>
                                <span>${c.telefono}</span>
                            </a>
                        </td>
                        <td>
                            <a href="mailto:${c.correo}" class="contact-link">
                                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                                    <polyline points="22,6 12,13 2,6"></polyline>
                                </svg>
                                <span>${c.correo}</span>
                            </a>
                        </td>
                        <td class="td-direccion">${c.direccion}</td>
                        <td>
                            <span class="${c.estado ? 'badge badge-active' : 'badge badge-inactive'}">
                                ${c.estado ? 'Activo' : 'Inactivo'}
                            </span>
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
                                <a href="/proveedor/anular/${c.idProveedor}"
                                   class="btn-anular"
                                   onclick="return confirm('¿Anular este proveedor?')">
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
function asignarEventos(){
    document.querySelectorAll(".btn-editar").forEach(button=>{
       button.addEventListener("click",function(){

       //obtengo la linea donde aplaste el boton
       const row=this.closest('tr');

       //recupero los datos de esa fila para llenarlos en el modal
       const id=row.cells[0].innerText;
       const nombre=row.cells[1].innerText;
       const ruc=row.cells[2].innerText;
       const telefono=row.cells[3].innerText;
       const correo=row.cells[4].innerText;
       const direccion=row.cells[5].innerText;
       const estado=row.getAttribute('data-estado')=== 'true'

       //lleno los datos de la fila a los imputs del modal
       document.getElementById('editId').value=id;
       document.getElementById('editNombre').value=nombre;
       document.getElementById('editRuc').value=ruc;
       document.getElementById('editTelefono').value=telefono;
       document.getElementById('editCorreo').value=correo;
       document.getElementById('editDireccion').value=direccion;
       document.getElementById('editEstado').value=estado ? 'true' : 'false';

       //mostramos el modal listo
       document.getElementById('modal').style.display='block';
       });
    });
}

asignarEventos();

//funcion cerra modal que le asignaremos al button cancelar

function cerrarModal(){
    document.getElementById('modal').style.display='none';
}


