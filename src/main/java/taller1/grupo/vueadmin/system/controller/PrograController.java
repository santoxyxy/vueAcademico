package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.PrograDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.PrograService;

/**
 * Controlador de PROGRA - Clases ofertadas
 * Endpoints para gestionar qué clases se dictan efectivamente
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class PrograController extends ResultUtil {

    private final PrograService prograService;

    /**
     * Obtener tabla de clases con paginación
     * GET /sys/progra/table?gestion=2025&codmat=MAT101&codn=1
     */
    @Log("Consultar tabla de PROGRA")
    @GetMapping("/progra/table")
    public ResponseEntity<Object> queryPrograTable(QueryDto queryDto,
                                                    @RequestParam(required = false) Integer gestion,
                                                    @RequestParam(required = false) String codmat,
                                                    @RequestParam(required = false) Integer codn) {
        try {
            return success(true, prograService.queryPrograTable(queryDto, gestion, codmat, codn));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener clases que dicta un docente
     * GET /sys/progra/docente/{ids}?gestion=2025
     */
    @Log("Obtener clases por docente")
    @GetMapping("/progra/docente/{ids}")
    public ResponseEntity<Object> getClasesByDocente(@PathVariable Integer ids,
                                                      @RequestParam Integer gestion) {
        try {
            return success(true, prograService.getClasesByDocente(ids, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener paralelos ofertados de una materia
     * GET /sys/progra/materia/{codmat}?gestion=2025
     */
    @Log("Obtener paralelos por materia")
    @GetMapping("/progra/materia/{codmat}")
    public ResponseEntity<Object> getParalelosByMateria(@PathVariable String codmat,
                                                         @RequestParam Integer gestion) {
        try {
            return success(true, prograService.getParalelosByMateria(codmat, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Crear o actualizar clase ofertada
     * POST /sys/progra/edit
     */
    @Log("Editar PROGRA")
    @PostMapping("/progra/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editProgra(@RequestBody PrograDto prograDto) {
        try {
            prograService.editProgra(prograDto);
            return success(true, "Clase guardada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar clase ofertada
     * DELETE /sys/progra/del?codpar=1&codp=1&codmat=MAT101&gestion=2025
     */
    @Log("Eliminar PROGRA")
    @DeleteMapping("/progra/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delProgra(@RequestParam Integer codpar,
                                            @RequestParam Integer codp,
                                            @RequestParam String codmat,
                                            @RequestParam Integer gestion) {
        try {
            prograService.delProgra(codpar, codp, codmat, gestion);
            return success(true, "Clase eliminada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
