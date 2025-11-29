package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Itemat;
import taller1.grupo.vueadmin.system.entity.dto.ItematDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de ITEMAT - Configuración de ponderaciones
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface ItematService {

    /**
     * Obtener lista completa de configuraciones (sin paginación)
     * @param gestion Filtro por gestión
     * @param codmat Filtro opcional por materia
     * @return Lista de Itemat
     */
    List<Itemat> getItematList(Integer gestion, String codmat);

    /**
     * Consulta paginada con filtros
     * @param queryDto Parámetros de consulta
     * @param gestion Año académico
     * @param codmat Filtro opcional por materia
     * @return Página con resultados
     */
    IPage<ItematDto> queryItematTable(QueryDto queryDto, Integer gestion, String codmat);

    /**
     * Crear o actualizar configuración de ponderación
     * @param itematDto Datos de la configuración
     */
    void editItemat(ItematDto itematDto);

    /**
     * Eliminar configuración de ponderación
     * @param codmat Código materia
     * @param codi Código ítem
     * @param gestion Gestión
     */
    void delItemat(String codmat, Integer codi, Integer gestion);

    /**
     * Obtener ítems configurados para una materia
     * @param codmat Código de materia
     * @param gestion Año académico
     * @return Lista de ítems con ponderaciones
     */
    List<ItematDto> getItemsByMateria(String codmat, Integer gestion);

    /**
     * Validar que la suma de ponderaciones sea 100%
     * @param codmat Código materia
     * @param gestion Gestión
     * @return true si suma 100%, false si no
     */
    boolean validatePonderaciones(String codmat, Integer gestion);
}
