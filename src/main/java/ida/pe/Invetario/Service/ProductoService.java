package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.Categoria;
import ida.pe.Invetario.Model.Producto;
import ida.pe.Invetario.Model.UnidadMedida;
import ida.pe.Invetario.Repository.CategoriaRepository;
import ida.pe.Invetario.Repository.ProductoRepository;
import ida.pe.Invetario.Repository.UnidadMedidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private  final UnidadMedidaRepository unidadMedidaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
                           UnidadMedidaRepository unidadMedidaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    public Producto guardarProducto(Producto producto){
        return productoRepository.save(producto);
    }
    public List<Producto> listarProducto(){
        return  productoRepository.findAll();
    }
    public void eliminarProducto(Long id){
        productoRepository.deleteById(id);
    }
    public List<Producto> buscarPorNombreProducto(String nombre){
        return productoRepository.findByNombre(nombre);
    }
    public Optional<Producto> buscarPorIdProducto(Long id){
        return productoRepository.findById(id);
    }
    public boolean existeNombre(String nombre) {
        return productoRepository.existsByNombre(nombre);
    }
    public boolean existeNombreExceptoId(String nombre, Long id) {
        return productoRepository.existsByNombreAndIdProductoNot(nombre, id);
    }

    //buscar producto
    public List<Producto> buscarProductoPorContenido(String nombre){
        return productoRepository.findByNombreContaining(nombre);
    }
    //editar Producto
    public Producto editarProducto(Long id, Producto productoActualizado) {
        Producto productoEncontrado = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no Encontrado"));

        // NOMBRE / DESC
        if (productoActualizado.getNombre() != null) {
            productoEncontrado.setNombre(productoActualizado.getNombre());
            productoEncontrado.setDescripcion(productoActualizado.getDescripcion());
        }
        // CATEGORÍA
        if (productoActualizado.getCategoria() != null &&
                productoActualizado.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(
                    productoActualizado.getCategoria().getIdCategoria()
            ).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            productoEncontrado.setCategoria(categoria);
        }
        // UNIDAD DE MEDIDA
        if (productoActualizado.getUnidadMedida() != null &&
                productoActualizado.getUnidadMedida().getId() != null) {
            UnidadMedida unidadMedida = unidadMedidaRepository.findById(
                    productoActualizado.getUnidadMedida().getId()
            ).orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada"));
            productoEncontrado.setUnidadMedida(unidadMedida);
        }
        // OTROS CAMPOS
        productoEncontrado.setStockActual(productoActualizado.getStockActual());
        productoEncontrado.setStockMinimo(productoActualizado.getStockMinimo());
        productoEncontrado.setPrecioUnitario(productoActualizado.getPrecioUnitario());
        productoEncontrado.setFechaVencimiento(productoActualizado.getFechaVencimiento());
        productoEncontrado.setEstado(productoActualizado.getEstado());
        return productoRepository.save(productoEncontrado);
    }
    public Producto anualarProducto(Long id){
        Optional<Producto>productoExistente=productoRepository.findById(id);
        if(!productoExistente.isPresent()){
            throw new RuntimeException("Producto no encotrado");
        }
        Producto productoEncontrado=productoExistente.get();
        productoEncontrado.setEstado(false);
        return productoRepository.save(productoEncontrado);
    }
}
