package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Progra;
import taller1.grupo.vueadmin.system.entity.dto.PrograDto;

import java.util.List;

/**
 * Mapper de PROGRA - Clases ofertadas
 * Incluye JOINs complejos con múltiples tablas
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface PrograMapper extends BaseMapper<Progra> {

    /**
     * Consulta paginada con JOINs completos
     * @param page Objeto de paginación
     * @param blurry Filtro (nombre materia, paralelo o docente)
     * @param gestion Filtro por gestión
     * @param codmat Filtro opcional por materia
     * @param codn Filtro opcional por nivel
     * @return Página con resultados
     */
    IPage<PrograDto> queryPrograTable(Page<?> page,
                                       @Param("blurry") String blurry,
                                       @Param("gestion") Integer gestion,
                                       @Param("codmat") String codmat,
                                       @Param("codn") Integer codn);

    /**
     * Obtener clase específica
     * @param codpar Código paralelo
     * @param codp Código periodo
     * @param codmat Código materia
     * @param gestion Gestión
     * @return PrograDto o null
     */
    PrograDto getPrograByKeys(@Param("codpar") Integer codpar,
                               @Param("codp") Integer codp,
                               @Param("codmat") String codmat,
                               @Param("gestion") Integer gestion);

    /**
     * Obtener clases que dicta un docente
     * @param ids ID del docente
     * @param gestion Gestión
     * @return Lista de clases
     */
    List<PrograDto> getClasesByDocente(@Param("ids") Integer ids,
                                        @Param("gestion") Integer gestion);

    /**
     * Obtener paralelos ofertados de una materia
     * @param codmat Código materia
     * @param gestion Gestión
     * @return Lista de paralelos
     */
    List<PrograDto> getParalelosByMateria(@Param("codmat") String codmat,
                                           @Param("gestion") Integer gestion);

    /**
     * Verificar si un paralelo ya tiene clase asignada
     * @param codmat Código materia
     * @param codpar Código paralelo
     * @param gestion Gestión
     * @return cantidad de registros
     */
    Integer countClasesExistentes(@Param("codmat") String codmat,
                                   @Param("codpar") Integer codpar,
                                   @Param("gestion") Integer gestion);
}
