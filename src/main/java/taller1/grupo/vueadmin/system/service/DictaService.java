package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Dicta;
import taller1.grupo.vueadmin.system.entity.dto.DictaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de DICTA - Asignación docente-materia
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface DictaService {

    /**
     * Consulta paginada con filtros
     * @param queryDto Parámetros de consulta
     * @param gestion Año académico
     * @param codmat Filtro opcional por materia
     * @param codn Filtro opcional por nivel
     * @return Página con resultados
     */
    IPage<DictaDto> queryDictaTable(QueryDto queryDto, 
                                     Integer gestion, 
                                     String codmat,
                                     Integer codn);

    /**
     * Crear o actualizar asignación docente-materia
     * @param dictaDto Datos de la asignación
     */
    void editDicta(DictaDto dictaDto);

    /**
     * Eliminar asignación docente-materia
     * @param codpar Código paralelo
     * @param codp Código periodo
     * @param codmat Código materia
     * @param gestion Gestión
     */
    void delDicta(Integer codpar, Integer codp, String codmat, Integer gestion);

    /**
     * Obtener materias asignadas a un docente
     * @param ids ID del docente
     * @param gestion Gestión
     * @return Lista de materias
     */
    List<DictaDto> getMateriasByDocente(Integer ids, Integer gestion);

    /**
     * Obtener docentes que dictan una materia
     * @param codmat Código materia
     * @param gestion Gestión
     * @return Lista de docentes
     */
    List<DictaDto> getDocentesByMateria(String codmat, Integer gestion);
}
