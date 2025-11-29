package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Itemat;
import taller1.grupo.vueadmin.system.entity.dto.ItematDto;

import java.util.List;

/**
 * Mapper de ITEMAT
 * Incluye JOINs con materia e items
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface ItematMapper extends BaseMapper<Itemat> {

    /**
     * Consulta paginada con JOINs para mostrar nombres descriptivos
     * @param page Objeto de paginación
     * @param blurry Filtro de búsqueda (nombre materia o ítem)
     * @param gestion Filtro por gestión (año académico)
     * @param codmat Filtro opcional por materia específica
     * @return Página con resultados
     */
    IPage<ItematDto> queryItematTable(Page<?> page,
                                       @Param("blurry") String blurry,
                                       @Param("gestion") Integer gestion,
                                       @Param("codmat") String codmat);

    /**
     * Obtener ítems configurados para una materia en una gestión
     * @param codmat Código de materia
     * @param gestion Año académico
     * @return Lista de ItematDto
     */
    List<ItematDto> getItemsByMateria(@Param("codmat") String codmat,
                                       @Param("gestion") Integer gestion);

    /**
     * Verificar si ya existe una configuración
     * @param codmat Código materia
     * @param codi Código ítem
     * @param gestion Gestión
     * @return ItematDto si existe, null si no
     */
    ItematDto getItematByKeys(@Param("codmat") String codmat,
                               @Param("codi") Integer codi,
                               @Param("gestion") Integer gestion);

    /**
     * Obtener suma de ponderaciones de una materia en una gestión
     * Útil para validar que la suma sea 100%
     * @param codmat Código materia
     * @param gestion Gestión
     * @return Suma total de ponderaciones
     */
    Integer getSumPonderaciones(@Param("codmat") String codmat,
                                @Param("gestion") Integer gestion);

    /**
     * Obtener suma de ponderaciones excluyendo un ítem específico
     * Útil para validaciones al editar
     * @param codmat Código materia
     * @param gestion Gestión
     * @param excludeCodi Ítem a excluir del cálculo
     * @return Suma parcial
     */
    Integer getSumPonderacionesExcluding(@Param("codmat") String codmat,
                                         @Param("gestion") Integer gestion,
                                         @Param("excludeCodi") Integer excludeCodi);
}
