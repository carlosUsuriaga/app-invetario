package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.UnidadMedida;
import ida.pe.Invetario.Repository.UnidadMedidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    public UnidadMedidaService(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    public List<UnidadMedida> listarUnidaMedida(){
        return unidadMedidaRepository.findAll();
    }
    public UnidadMedida guardarUnidadMedida(UnidadMedida unidadMedida){
        return unidadMedidaRepository.save(unidadMedida);
    }

    public void eliminarUnidaMedida(Long id){
        unidadMedidaRepository.deleteById(id);
    }

    public List<UnidadMedida> buscarPorNombreUnidadMedida(String nombre){
        return unidadMedidaRepository.findByNombre(nombre);
    }
    public boolean existsByNombre(String nombre ){
        return unidadMedidaRepository.existsByNombre(nombre);
    }
    public boolean existsByAbreviatura(String abreviatura){
        return unidadMedidaRepository.existsByAbreviatura(abreviatura);
    }
    public boolean existsByNombreAndIdNot(String nombre, Long id){
        return  unidadMedidaRepository.existsByNombreAndIdNot(nombre,id);
    }
    public boolean existsByAbreviaturaAndIdNot(String abreviatura,Long id){
        return unidadMedidaRepository.existsByAbreviaturaAndIdNot(abreviatura,id);
    }

    //servicio para editar

    public UnidadMedida editarUnidaMedida(Long id,UnidadMedida medidaActulizada){

        Optional<UnidadMedida>unidadMedidaEncontrada=unidadMedidaRepository.findById(id);
        if(!unidadMedidaEncontrada.isPresent()){
            throw new RuntimeException("Unidad de medida no encontrada");
        }

        UnidadMedida unidadMedida=unidadMedidaEncontrada.get();

        if(unidadMedida.getNombre()!=null){
            unidadMedida.setNombre(medidaActulizada.getNombre());
            unidadMedida.setAbreviatura(medidaActulizada.getAbreviatura());
            unidadMedida.setEstado(medidaActulizada.getEstado());
        }
        return unidadMedidaRepository.save(unidadMedida);
    }

    //servicio para buscar{
    public List<UnidadMedida> buscarPorContenidoUnidadMedida(String nombre){
        return unidadMedidaRepository.findByNombreContaining(nombre);
    }
    //servicio para anular unidad de medida
    public UnidadMedida anularUnidaMedida(Long id){
        Optional<UnidadMedida>unidadMedidaexistente=unidadMedidaRepository.findById(id);
        if(!unidadMedidaexistente.isPresent()){
            throw new RuntimeException("Unidad de medida no enontrada para anular");
        }
        UnidadMedida unidadMedida=unidadMedidaexistente.get();
        unidadMedida.setEstado(false);
        return  unidadMedidaRepository.save(unidadMedida);
    }






}
