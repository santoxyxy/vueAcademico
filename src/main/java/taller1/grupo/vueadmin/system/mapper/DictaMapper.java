package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Dicta;
import taller1.grupo.vueadmin.system.entity.dto.DictaDto;

import java.util.List;

/**
 * Mapper de DICTA - Asignación docente-materia
 * Incluye JOINs complejos con múltiples tablas
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface DictaMapper extends BaseMapper<Dicta> {

    /**
     * Consulta paginada con JOINs completos
     * @param page Objeto de paginación
     * @param blurry Filtro (nombre materia, paralelo o docente)
     * @param gestion Filtro por gestión
     * @param codmat Filtro opcional por materia
     * @param codn Filtro opcional por nivel
     * @return Página con resultados
     */
    IPage<DictaDto> queryDictaTable(Page<?> page,
                                     @Param("blurry") String blurry,
                                     @Param("gestion") Integer gestion,
                                     @Param("codmat") String codmat,
                                     @Param("codn") Integer codn);

    /**
     * Obtener asignación específica
     * @param codpar Código paralelo
     * @param codp Código periodo
     * @param codmat Código materia
     * @param gestion Gestión
     * @return DictaDto o null
     */
    DictaDto getDictaByKeys(@Param("codpar") Integer codpar,
                             @Param("codp") Integer codp,
                             @Param("codmat") String codmat,
                             @Param("gestion") Integer gestion);

    /**
     * Obtener materias asignadas a un docente
     * @param ids ID del docente
     * @param gestion Gestión
     * @return Lista de materias que dicta
     */
    List<DictaDto> getMateriasByDocente(@Param("ids") Integer ids,
                                         @Param("gestion") Integer gestion);

    /**
     * Obtener docentes que dictan una materia
     * @param codmat Código materia
     * @param gestion Gestión
     * @return Lista de docentes
     */
    List<DictaDto> getDocentesByMateria(@Param("codmat") String codmat,
                                         @Param("gestion") Integer gestion);

    /**
     * Verificar si un paralelo ya tiene docente asignado
     * @param codmat Código materia
     * @param codpar Código paralelo
     * @param gestion Gestión
     * @return true si ya tiene docente, false si no
     */
    Integer countAsignaciones(@Param("codmat") String codmat,
                              @Param("codpar") Integer codpar,
                              @Param("gestion") Integer gestion);
}
