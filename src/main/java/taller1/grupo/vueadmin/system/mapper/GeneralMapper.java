package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.General;
import taller1.grupo.vueadmin.system.entity.dto.GeneralDto;

import java.util.List;

/**
 * Mapper de GENERAL
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface GeneralMapper extends BaseMapper<General> {

    /**
     * Consulta paginada con JOINs
     * @param page Objeto de paginación
     * @param blurry Filtro de búsqueda
     * @param gestion Filtro por gestión
     * @return Página con resultados
     */
    IPage<GeneralDto> queryGeneralTable(Page<?> page,
                                         @Param("blurry") String blurry,
                                         @Param("gestion") Integer gestion);

    /**
     * Obtener información general de un usuario
     * @param ids ID del usuario
     * @param gestion Gestión
     * @return GeneralDto
     */
    GeneralDto getGeneralByUserAndGestion(@Param("ids") Integer ids,
                                           @Param("gestion") Integer gestion);

    /**
     * Obtener todas las gestiones de un usuario
     * @param ids ID del usuario
     * @return Lista de gestiones
     */
    List<Integer> getGestionesByUser(@Param("ids") Integer ids);

    /**
     * Verificar si un usuario existe en una gestión
     * @param ids ID del usuario
     * @param gestion Gestión
     * @return Cantidad (0 o 1)
     */
    Integer existeEnGestion(@Param("ids") Integer ids,
                            @Param("gestion") Integer gestion);
}