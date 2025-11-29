package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Modalidad;
import taller1.grupo.vueadmin.system.entity.dto.ModalidadDto;

/**
 * Mapper de Modalidad
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface ModalidadMapper extends BaseMapper<Modalidad> {

    /**
     * Consulta paginada de modalidades con filtro
     * @param page Objeto de paginación
     * @param blurry Texto de búsqueda (nombre)
     * @return Página con resultados
     */
    IPage<ModalidadDto> queryModalidadTable(Page<?> page, @Param("blurry") String blurry);
}
