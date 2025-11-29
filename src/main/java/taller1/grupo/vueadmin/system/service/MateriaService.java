package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Materia;
import taller1.grupo.vueadmin.system.entity.dto.MateriaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de Materias
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface MateriaService {

    /**
     * Obtener lista de materias con filtro
     * @param blurry Texto de búsqueda
     * @return Lista de materias
     */
    List<Materia> getMateriaList(String blurry);

    /**
     * Consulta paginada de materias
     * @param queryDto Objeto con parámetros de paginación y filtro
     * @return Página con resultados incluyendo nombre del nivel
     */
    IPage<MateriaDto> queryMateriaTable(QueryDto queryDto);

    /**
     * Editar o crear materia
     * @param materiaDto Datos de la materia
     */
    void editMateria(MateriaDto materiaDto);

    /**
     * Eliminar materia
     * @param codmat Código de la materia
     */
    void delMateria(String codmat);

    /**
     * Obtener materia por código con nombre del nivel
     * @param codmat Código de la materia
     * @return MateriaDto con datos completos
     */
    MateriaDto getMateriaWithNivel(String codmat);
}