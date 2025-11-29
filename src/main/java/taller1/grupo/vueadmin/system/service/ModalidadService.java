package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Modalidad;
import taller1.grupo.vueadmin.system.entity.dto.ModalidadDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de Modalidad
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface ModalidadService {

    /**
     * Obtener lista de modalidades con filtro
     * @param blurry Texto de búsqueda
     * @return Lista de modalidades
     */
    List<Modalidad> getModalidadList(String blurry);

    /**
     * Consulta paginada de modalidades
     * @param queryDto Objeto con parámetros de paginación y filtro
     * @return Página con resultados
     */
    IPage<ModalidadDto> queryModalidadTable(QueryDto queryDto);

    /**
     * Editar o crear modalidad
     * @param modalidadDto Datos de la modalidad
     */
    void editModalidad(ModalidadDto modalidadDto);

    /**
     * Eliminar modalidad
     * @param codm ID de la modalidad
     */
    void delModalidad(Integer codm);
}
