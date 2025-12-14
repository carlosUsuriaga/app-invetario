package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.Categoria;
import ida.pe.Invetario.Model.Proveedor;
import ida.pe.Invetario.Repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public Proveedor guardarProveedor(Proveedor proveedor){
        return proveedorRepository.save(proveedor);
    }
    public List<Proveedor> listarProveedor(){
        return proveedorRepository.findAll();
    }
    public Optional<Proveedor> buscarPorId(Long id){
        return proveedorRepository.findById(id);
    }
    public List<Proveedor> buscarPorNombre(String nombre){
        return proveedorRepository.findByNombre(nombre);
    }
    public void eliminarProveedor(Long id){
        proveedorRepository.deleteById(id);
    }
    public boolean existsByNombre(String nombre){
        return proveedorRepository.existsByNombre(nombre);
    };
    public boolean existsByRuc(String ruc){
        return  proveedorRepository.existsByRuc(ruc);
    };
    public boolean existsByTelefono(String telefono){
        return proveedorRepository.existsByTelefono(telefono);
    };
    public boolean existsByCorreo(String correo){
        return proveedorRepository.existsByCorreo(correo);
    };

    public boolean existsByNombreAndIdProveedorNot(String nombre, Long idProveedor){
        return proveedorRepository.existsByNombreAndIdProveedorNot(nombre,idProveedor);
    };
    public boolean existsByRucAndIdProveedorNot(String ruc,Long idProveedor){
        return proveedorRepository.existsByRucAndIdProveedorNot(ruc,idProveedor);
    };
    public boolean existsByTelefonoAndIdProveedorNot(String telefono,Long idProveedor){
        return proveedorRepository.existsByTelefonoAndIdProveedorNot(telefono,idProveedor);
    };
    public boolean existsByCorreoAndIdProveedorNot(String correo,Long idProveedor){
        return proveedorRepository.existsByCorreoAndIdProveedorNot(correo,idProveedor);
    };





    public List<Proveedor> buscarPorNombreContenido(String nombre){
        return proveedorRepository.findByNombreContaining(nombre);
    }
    public Proveedor editarProveedor(Long id, Proveedor proveedorActuazlizado){
        Optional<Proveedor> proveedorExitente = proveedorRepository.findById(id);

        if(!proveedorExitente.isPresent()){
            throw new RuntimeException("Proveedor no encontrado");
        }
        Proveedor proveedor= proveedorExitente.get();

        if(proveedorActuazlizado.getNombre()!=null){
            proveedor.setNombre(proveedorActuazlizado.getNombre());
            proveedor.setRuc(proveedorActuazlizado.getRuc());
            proveedor.setTelefono(proveedorActuazlizado.getTelefono());
            proveedor.setCorreo(proveedorActuazlizado.getCorreo());
            proveedor.setDireccion(proveedorActuazlizado.getDireccion());
            proveedor.setEstado(proveedorActuazlizado.getEstado());
        }

        return proveedorRepository.save(proveedor);
    }

    public Proveedor anularCategoria(Long id){
        Optional<Proveedor> proveedorExitente=proveedorRepository.findById(id);

        if(!proveedorExitente.isPresent()){
            throw new RuntimeException("Proveedor no encontrada");
        }
        Proveedor proveedorEncotrado=proveedorExitente.get();
        proveedorEncotrado.setEstado(false);
        return proveedorRepository.save(proveedorEncotrado);

    }

    public Boolean validarProveedor(String ruc){
        return proveedorRepository.existsByRuc(ruc);
    }





}
