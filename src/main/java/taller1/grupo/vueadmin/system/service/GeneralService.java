package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.General;
import taller1.grupo.vueadmin.system.entity.dto.GeneralDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de GENERAL
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface GeneralService {

    /**
     * Obtener lista completa
     * @param gestion Filtro por gestión
     * @return Lista de registros
     */
    List<General> getGeneralList(Integer gestion);

    /**
     * Consulta paginada
     * @param queryDto Parámetros de consulta
     * @param gestion Filtro por gestión
     * @return Página con resultados
     */
    IPage<GeneralDto> queryGeneralTable(QueryDto queryDto, Integer gestion);

    /**
     * Editar o crear registro
     * @param generalDto Datos del registro
     */
    void editGeneral(GeneralDto generalDto);

    /**
     * Eliminar registro
     * @param ids ID del usuario
     * @param gestion Gestión (opcional, si es null elimina todas)
     */
    void delGeneral(Integer ids, Integer gestion);

    /**
     * Obtener información de un usuario en una gestión
     * @param ids ID del usuario
     * @param gestion Gestión
     * @return GeneralDto
     */
    GeneralDto getGeneralByUserAndGestion(Integer ids, Integer gestion);

    /**
     * Obtener gestiones de un usuario
     * @param ids ID del usuario
     * @return Lista de años
     */
    List<Integer> getGestionesByUser(Integer ids);

    /**
     * Registrar usuario en gestión actual
     * Si no existe, lo crea automáticamente
     * @param ids ID del usuario
     * @param gestion Gestión
     */
    void registrarEnGestion(Integer ids, Integer gestion);
}
