package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.ItematDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.ItematService;

/**
 * Controlador de ITEMAT - Configuración de ponderaciones
 * Endpoints para gestionar qué ítems tiene cada materia y su porcentaje
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class ItematController extends ResultUtil {

    private final ItematService itematService;

    /**
     * Obtener tabla de configuraciones con paginación
     * GET /sys/itemat/table?gestion=2025&codmat=MAT101
     */
    @Log("Consultar tabla de ITEMAT")
    @GetMapping("/itemat/table")
    public ResponseEntity<Object> queryItematTable(QueryDto queryDto,
                                                     @RequestParam(required = false) Integer gestion,
                                                     @RequestParam(required = false) String codmat) {
        try {
            return success(true, itematService.queryItematTable(queryDto, gestion, codmat));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener ítems configurados para una materia
     * GET /sys/itemat/items/{codmat}?gestion=2025
     */
    @Log("Obtener ítems por materia")
    @GetMapping("/itemat/items/{codmat}")
    public ResponseEntity<Object> getItemsByMateria(@PathVariable String codmat,
                                                      @RequestParam Integer gestion) {
        try {
            return success(true, itematService.getItemsByMateria(codmat, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Validar que las ponderaciones sumen 100%
     * GET /sys/itemat/validate/{codmat}?gestion=2025
     */
    @Log("Validar ponderaciones")
    @GetMapping("/itemat/validate/{codmat}")
    public ResponseEntity<Object> validatePonderaciones(@PathVariable String codmat,
                                                          @RequestParam Integer gestion) {
        try {
            boolean isValid = itematService.validatePonderaciones(codmat, gestion);
            if (isValid) {
                return success(true, "Las ponderaciones suman 100% correctamente");
            } else {
                return fail(false, "Las ponderaciones NO suman 100%");
            }
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Crear o actualizar configuración de ponderación
     * POST /sys/itemat/edit
     */
    @Log("Editar ITEMAT")
    @PostMapping("/itemat/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editItemat(@RequestBody ItematDto itematDto) {
        try {
            itematService.editItemat(itematDto);
            return success(true, "Configuración guardada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar configuración de ponderación
     * DELETE /sys/itemat/del?codmat=MAT101&codi=1&gestion=2025
     */
    @Log("Eliminar ITEMAT")
    @DeleteMapping("/itemat/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delItemat(@RequestParam String codmat,
                                             @RequestParam Integer codi,
                                             @RequestParam Integer gestion) {
        try {
            itematService.delItemat(codmat, codi, gestion);
            return success(true, "Configuración eliminada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
} 
