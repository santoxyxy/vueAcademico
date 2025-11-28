package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Paralelo;
import taller1.grupo.vueadmin.system.entity.dto.ParaleloDto;

/**
 * Mapper de Paralelos
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface ParaleloMapper extends BaseMapper<Paralelo> {

    /**
     * Consulta paginada de paralelos con filtro
     * @param page Objeto de paginación
     * @param blurry Texto de búsqueda (nombre)
     * @return Página con resultados
     */
    IPage<ParaleloDto> queryParaleloTable(Page<?> page, @Param("blurry") String blurry);
}