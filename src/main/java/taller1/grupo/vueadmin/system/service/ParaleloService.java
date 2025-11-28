package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Paralelo;
import taller1.grupo.vueadmin.system.entity.dto.ParaleloDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de Paralelos
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface ParaleloService {

    /**
     * Obtener lista de paralelos con filtro
     * @param blurry Texto de búsqueda
     * @return Lista de paralelos
     */
    List<Paralelo> getParaleloList(String blurry);

    /**
     * Consulta paginada de paralelos
     * @param queryDto Objeto con parámetros de paginación y filtro
     * @return Página con resultados
     */
    IPage<ParaleloDto> queryParaleloTable(QueryDto queryDto);

    /**
     * Editar o crear paralelo
     * @param paraleloDto Datos del paralelo
     */
    void editParalelo(ParaleloDto paraleloDto);

    /**
     * Eliminar paralelo
     * @param id ID del paralelo
     */
    void delParalelo(Integer id);
}