package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Dmodalidad;
import taller1.grupo.vueadmin.system.entity.dto.DmodalidadDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de DMODALIDAD
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface DmodalidadService {

    /**
     * Obtener lista completa
     * @param blurry Filtro de búsqueda
     * @return Lista de detalles
     */
    List<Dmodalidad> getDmodalidadList(String blurry);

    /**
     * Consulta paginada
     * @param queryDto Parámetros de consulta
     * @return Página con resultados
     */
    IPage<DmodalidadDto> queryDmodalidadTable(QueryDto queryDto);

    /**
     * Editar o crear detalle de modalidad
     * @param dmodalidadDto Datos del detalle
     */
    void editDmodalidad(DmodalidadDto dmodalidadDto);

    /**
     * Eliminar detalle de modalidad
     * @param coddm ID del detalle
     */
    void delDmodalidad(Integer coddm);

    /**
     * Obtener detalles por modalidad padre
     * @param codm Código de modalidad
     * @return Lista de detalles
     */
    List<DmodalidadDto> getDetallesByModalidad(Integer codm);
}
