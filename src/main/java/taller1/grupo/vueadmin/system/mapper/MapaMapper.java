package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Mapa;
import taller1.grupo.vueadmin.system.entity.dto.MapaDto;

import java.util.List;

/**
 * Mapper de MAPA
 * Incluye JOINs con materia, paralelo y niveles
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface MapaMapper extends BaseMapper<Mapa> {

    /**
     * Consulta paginada con JOINs para mostrar nombres descriptivos
     * @param page Objeto de paginación
     * @param blurry Filtro de búsqueda (nombre materia o paralelo)
     * @param gestion Filtro por gestión (año académico)
     * @return Página con resultados
     */
    IPage<MapaDto> queryMapaTable(Page<?> page, 
                                   @Param("blurry") String blurry,
                                   @Param("gestion") Integer gestion);

    /**
     * Obtener paralelos disponibles para una materia en una gestión
     * @param codmat Código de materia
     * @param gestion Año académico
     * @return Lista de MapaDto
     */
    List<MapaDto> getParalelosByMateria(@Param("codmat") String codmat,
                                         @Param("gestion") Integer gestion);

    /**
     * Verificar si ya existe un registro MAPA
     * @param codmat Código materia
     * @param codpar Código paralelo
     * @param gestion Gestión
     * @return MapaDto si existe, null si no
     */
    MapaDto getMapaByKeys(@Param("codmat") String codmat,
                          @Param("codpar") Integer codpar,
                          @Param("gestion") Integer gestion);
}
