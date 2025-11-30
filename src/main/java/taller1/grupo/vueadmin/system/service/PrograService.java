package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Progra;
import taller1.grupo.vueadmin.system.entity.dto.PrograDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de PROGRA - Clases ofertadas
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface PrograService {

    /**
     * Consulta paginada con filtros
     * @param queryDto Parámetros de consulta
     * @param gestion Año académico
     * @param codmat Filtro opcional por materia
     * @param codn Filtro opcional por nivel
     * @return Página con resultados
     */
    IPage<PrograDto> queryPrograTable(QueryDto queryDto, 
                                       Integer gestion, 
                                       String codmat,
                                       Integer codn);

    /**
     * Crear o actualizar clase ofertada
     * @param prograDto Datos de la clase
     */
    void editProgra(PrograDto prograDto);

    /**
     * Eliminar clase ofertada
     * @param codpar Código paralelo
     * @param codp Código periodo
     * @param codmat Código materia
     * @param gestion Gestión
     */
    void delProgra(Integer codpar, Integer codp, String codmat, Integer gestion);

    /**
     * Obtener clases que dicta un docente
     * @param ids ID del docente
     * @param gestion Gestión
     * @return Lista de clases
     */
    List<PrograDto> getClasesByDocente(Integer ids, Integer gestion);

    /**
     * Obtener paralelos ofertados de una materia
     * @param codmat Código materia
     * @param gestion Gestión
     * @return Lista de paralelos
     */
    List<PrograDto> getParalelosByMateria(String codmat, Integer gestion);
}
