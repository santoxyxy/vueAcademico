package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Items;
import taller1.grupo.vueadmin.system.entity.dto.ItemsDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de Items
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface ItemsService {

    /**
     * Obtener lista de ítems con filtro
     * @param blurry Texto de búsqueda
     * @return Lista de ítems
     */
    List<Items> getItemsList(String blurry);

    /**
     * Consulta paginada de ítems
     * @param queryDto Objeto con parámetros de paginación y filtro
     * @return Página con resultados
     */
    IPage<ItemsDto> queryItemsTable(QueryDto queryDto);

    /**
     * Editar o crear ítem
     * @param itemsDto Datos del ítem
     */
    void editItems(ItemsDto itemsDto);

    /**
     * Eliminar ítem
     * @param codi ID del ítem
     */
    void delItems(Integer codi);
}
