package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.GeneralDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.GeneralService;

/**
 * Controlador de GENERAL - Metadatos por usuario/gestión
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class GeneralController extends ResultUtil {

    private final GeneralService generalService;

    /**
     * Obtener lista de registros generales
     * GET /sys/general/list?gestion=2025
     */
    @Log("Obtener lista de registros GENERAL")
    @GetMapping("/general/list")
    public ResponseEntity<Object> getGeneralList(@RequestParam(required = false) Integer gestion) {
        try {
            return success(true, generalService.getGeneralList(gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Consultar tabla con paginación
     * GET /sys/general/table?gestion=2025
     */
    @Log("Consultar tabla de GENERAL")
    @GetMapping("/general/table")
    public ResponseEntity<Object> queryGeneralTable(QueryDto queryDto,
                                                     @RequestParam(required = false) Integer gestion) {
        try {
            return success(true, generalService.queryGeneralTable(queryDto, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener información de un usuario en una gestión
     * GET /sys/general/usuario/{ids}/gestion/{gestion}
     */
    @Log("Obtener información general de usuario")
    @GetMapping("/general/usuario/{ids}/gestion/{gestion}")
    public ResponseEntity<Object> getGeneralByUserAndGestion(@PathVariable Integer ids,
                                                               @PathVariable Integer gestion) {
        try {
            return success(true, generalService.getGeneralByUserAndGestion(ids, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener gestiones de un usuario
     * GET /sys/general/usuario/{ids}/gestiones
     */
    @Log("Obtener gestiones de usuario")
    @GetMapping("/general/usuario/{ids}/gestiones")
    public ResponseEntity<Object> getGestionesByUser(@PathVariable Integer ids) {
        try {
            return success(true, generalService.getGestionesByUser(ids));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Registrar usuario en gestión
     * POST /sys/general/registrar
     */
    @Log("Registrar usuario en gestión")
    @PostMapping("/general/registrar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> registrarEnGestion(@RequestBody GeneralDto generalDto) {
        try {
            generalService.registrarEnGestion(generalDto.getIds(), generalDto.getGestion());
            return success(true, "Usuario registrado en gestión exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Editar o crear registro
     * POST /sys/general/edit
     */
    @Log("Editar GENERAL")
    @PostMapping("/general/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> editGeneral(@RequestBody GeneralDto generalDto) {
        try {
            generalService.editGeneral(generalDto);
            return success(true, "Registro guardado exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar registro
     * DELETE /sys/general/del?ids=1&gestion=2025
     */
    @Log("Eliminar GENERAL")
    @DeleteMapping("/general/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delGeneral(@RequestParam Integer ids,
                                              @RequestParam(required = false) Integer gestion) {
        try {
            generalService.delGeneral(ids, gestion);
            return success(true, "Registro eliminado exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
