package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.Categoria;
import ida.pe.Invetario.Repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    public List<Categoria> obtenerTodasLasCategorias() {
        return (List<Categoria>) categoriaRepository.findAll();
    }
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
    public boolean existsByNombre(String nombre){
        return categoriaRepository.existsByNombre(nombre);
    }
    public boolean existsByNombreAndIdCategoriaNot(String nombre,Long idCategoria){
        return categoriaRepository.existsByNombreAndIdCategoriaNot(nombre,idCategoria);
    }



    public List<Categoria> buscarPorNombreConteniendo(String nombre) {
        return categoriaRepository.findByNombreContaining(nombre);
    }
    public Categoria EditarCategoria(Long id, Categoria categoriaActualizada){
        Optional<Categoria> categoriaExistente=categoriaRepository.findById(id);
        if(!categoriaExistente.isPresent()){
            throw new RuntimeException("Usuario no encontrado");
        }
        Categoria categoria=categoriaExistente.get();
        if(categoriaActualizada.getNombre()!=null){
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            categoria.setEstado(categoriaActualizada.getEstado());
        }
        return categoriaRepository.save(categoria);
    };
    public Categoria AnularCategoria(Long id){
        Optional<Categoria>categoriaExistente=categoriaRepository.findById(id);

        if(!categoriaExistente.isPresent()){
            throw new RuntimeException("Categoria no encontrada");
        }
        Categoria categoriaEncotrada=categoriaExistente.get();
        categoriaEncotrada.setEstado(false);
        return categoriaRepository.save(categoriaEncotrada);
    }

}
