package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Notas;
import taller1.grupo.vueadmin.system.entity.dto.NotasDto;

import java.util.List;

/**
 * Mapper de NOTAS
 * Incluye JOINs complejos con múltiples tablas
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface NotasMapper extends BaseMapper<Notas> {

    /**
     * Consulta paginada con JOINs completos
     * @param page Objeto de paginación
     * @param blurry Filtro de búsqueda
     * @param gestion Filtro por gestión
     * @param codmat Filtro opcional por materia
     * @param idusuario Filtro opcional por estudiante
     * @return Página con resultados
     */
    IPage<NotasDto> queryNotasTable(Page<?> page,
                                     @Param("blurry") String blurry,
                                     @Param("gestion") Integer gestion,
                                     @Param("codmat") String codmat,
                                     @Param("idusuario") Integer idusuario);

    /**
     * Obtener nota específica
     */
    NotasDto getNotaByKeys(@Param("codmat") String codmat,
                           @Param("codi") Integer codi,
                           @Param("coda") Integer coda,
                           @Param("codp") Integer codp,
                           @Param("gestion") Integer gestion,
                           @Param("idusuario") Integer idusuario);

    /**
     * Obtener notas de un estudiante en una materia
     */
    List<NotasDto> getNotasEstudianteMateria(@Param("codmat") String codmat,
                                              @Param("idusuario") Integer idusuario,
                                              @Param("gestion") Integer gestion);

    /**
     * Obtener notas de un paralelo para un ítem específico
     */
    List<NotasDto> getNotasParaleloItem(@Param("codmat") String codmat,
                                        @Param("coda") Integer coda,
                                        @Param("codi") Integer codi,
                                        @Param("gestion") Integer gestion);

    /**
     * Calcular nota final de un estudiante en una materia
     */
    Double calcularNotaFinal(@Param("codmat") String codmat,
                             @Param("idusuario") Integer idusuario,
                             @Param("gestion") Integer gestion);
}
