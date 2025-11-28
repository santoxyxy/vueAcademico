package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import taller1.grupo.vueadmin.system.entity.Materia;
import taller1.grupo.vueadmin.system.entity.dto.MateriaDto;

/**
 * Mapper de Materias
 * Incluye JOIN con Niveles para obtener nombre del nivel
 * @author Tu nombre
 * @date 2025-01-01
 */
@Mapper
public interface MateriaMapper extends BaseMapper<Materia> {

    /**
     * Consulta paginada de materias con JOIN a niveles
     * @param page Objeto de paginación
     * @param blurry Texto de búsqueda (nombre de materia o código)
     * @return Página con resultados incluyendo nombre del nivel
     */
    IPage<MateriaDto> queryMateriaTable(Page<?> page, @Param("blurry") String blurry);

    /**
     * Obtener una materia con el nombre del nivel
     * @param codmat Código de la materia
     * @return MateriaDto con datos completos
     */
    MateriaDto getMateriaWithNivel(@Param("codmat") String codmat);
}
