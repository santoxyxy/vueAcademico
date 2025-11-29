package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.ItemsDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.ItemsService;

/**
 * Controlador de Items
 * Endpoints para CRUD de ítems de evaluación
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class ItemsController extends ResultUtil {

    private final ItemsService itemsService;

    /**
     * Obtener lista de ítems
     * GET /sys/items/list
     */
    @Log("Obtener lista de ítems")
    @GetMapping("/items/list")
    public ResponseEntity<Object> getItemsList(String blurry) {
        try {
            return success(true, itemsService.getItemsList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Consultar tabla de ítems con paginación
     * GET /sys/items/table
     */
    @Log("Consultar tabla de ítems")
    @GetMapping("/items/table")
    public ResponseEntity<Object> queryItemsTable(QueryDto queryDto) {
        try {
            return success(true, itemsService.queryItemsTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Editar o crear ítem
     * POST /sys/items/edit
     */
    @Log("Editar ítem")
    @PostMapping("/items/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editItems(@RequestBody ItemsDto itemsDto) {
        try {
            String str = StringUtil.getEditType(itemsDto.getCodi() != null ? 
                         Long.valueOf(itemsDto.getCodi()) : null);
            itemsService.editItems(itemsDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar ítem
     * DELETE /sys/items/del
     */
    @Log("Eliminar ítem")
    @DeleteMapping("/items/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delItems(Integer id) {
        try {
            itemsService.delItems(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
