package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.DictaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.DictaService;

/**
 * Controlador de DICTA - Asignación de docentes a materias
 * Endpoints para gestionar quién enseña qué
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class DictaController extends ResultUtil {

    private final DictaService dictaService;

    /**
     * Obtener tabla de asignaciones con paginación
     * GET /sys/dicta/table?gestion=2025&codmat=MAT101&codn=1
     */
    @Log("Consultar tabla de DICTA")
    @GetMapping("/dicta/table")
    public ResponseEntity<Object> queryDictaTable(QueryDto queryDto,
                                                   @RequestParam(required = false) Integer gestion,
                                                   @RequestParam(required = false) String codmat,
                                                   @RequestParam(required = false) Integer codn) {
        try {
            return success(true, dictaService.queryDictaTable(queryDto, gestion, codmat, codn));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener materias que dicta un docente
     * GET /sys/dicta/docente/{ids}?gestion=2025
     */
    @Log("Obtener materias por docente")
    @GetMapping("/dicta/docente/{ids}")
    public ResponseEntity<Object> getMateriasByDocente(@PathVariable Integer ids,
                                                        @RequestParam Integer gestion) {
        try {
            return success(true, dictaService.getMateriasByDocente(ids, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener docentes de una materia
     * GET /sys/dicta/materia/{codmat}?gestion=2025
     */
    @Log("Obtener docentes por materia")
    @GetMapping("/dicta/materia/{codmat}")
    public ResponseEntity<Object> getDocentesByMateria(@PathVariable String codmat,
                                                        @RequestParam Integer gestion) {
        try {
            return success(true, dictaService.getDocentesByMateria(codmat, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Crear o actualizar asignación docente-materia
     * POST /sys/dicta/edit
     */
    @Log("Editar DICTA")
    @PostMapping("/dicta/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editDicta(@RequestBody DictaDto dictaDto) {
        try {
            dictaService.editDicta(dictaDto);
            return success(true, "Asignación guardada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar asignación docente-materia
     * DELETE /sys/dicta/del?codpar=1&codp=1&codmat=MAT101&gestion=2025
     */
    @Log("Eliminar DICTA")
    @DeleteMapping("/dicta/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delDicta(@RequestParam Integer codpar,
                                           @RequestParam Integer codp,
                                           @RequestParam String codmat,
                                           @RequestParam Integer gestion) {
        try {
            dictaService.delDicta(codpar, codp, codmat, gestion);
            return success(true, "Asignación eliminada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
