package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Dmodalidad;
import taller1.grupo.vueadmin.system.entity.dto.DmodalidadDto;

import java.util.List;

/**
 * Mapper de DMODALIDAD
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface DmodalidadMapper extends BaseMapper<Dmodalidad> {

    /**
     * Consulta paginada con JOIN a modalidad
     * @param page Objeto de paginación
     * @param blurry Texto de búsqueda
     * @return Página con resultados
     */
    IPage<DmodalidadDto> queryDmodalidadTable(Page<?> page, @Param("blurry") String blurry);

    /**
     * Obtener detalles por modalidad padre
     * @param codm Código de modalidad
     * @return Lista de detalles
     */
    List<DmodalidadDto> getDetallesByModalidad(@Param("codm") Integer codm);
}
