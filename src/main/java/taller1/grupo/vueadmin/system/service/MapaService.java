package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Mapa;
import taller1.grupo.vueadmin.system.entity.dto.MapaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de MAPA - Gestión de oferta académica
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface MapaService {

    /**
     * Obtener lista completa de mapas (sin paginación)
     * @param gestion Filtro por gestión
     * @return Lista de Mapa
     */
    List<Mapa> getMapaList(Integer gestion);

    /**
     * Consulta paginada de mapas con filtros
     * @param queryDto Parámetros de consulta
     * @param gestion Año académico
     * @return Página con resultados
     */
    IPage<MapaDto> queryMapaTable(QueryDto queryDto, Integer gestion);

    /**
     * Crear o actualizar asignación materia-paralelo
     * @param mapaDto Datos del mapa
     */
    void editMapa(MapaDto mapaDto);

    /**
     * Eliminar asignación materia-paralelo
     * @param codmat Código materia
     * @param codpar Código paralelo
     * @param gestion Gestión
     */
    void delMapa(String codmat, Integer codpar, Integer gestion);

    /**
     * Obtener paralelos asignados a una materia en una gestión
     * @param codmat Código de materia
     * @param gestion Año académico
     * @return Lista de paralelos asignados
     */
    List<MapaDto> getParalelosByMateria(String codmat, Integer gestion);
}