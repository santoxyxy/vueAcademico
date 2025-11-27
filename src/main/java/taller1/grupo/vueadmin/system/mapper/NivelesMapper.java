package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Niveles;
import taller1.grupo.vueadmin.system.entity.dto.NivelesDto;

/**
 * Mapper de Niveles
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface NivelesMapper extends BaseMapper<Niveles> {

    /**
     * Consulta paginada de niveles con filtro
     * @param page Objeto de paginación
     * @param blurry Texto de búsqueda (nombre)
     * @return Página con resultados
     */
    IPage<NivelesDto> queryNivelesTable(Page<?> page, @Param("blurry") String blurry);
}