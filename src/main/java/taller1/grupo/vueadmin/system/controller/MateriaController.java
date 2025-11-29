package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.MateriaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.MateriaService;

/**
 * Controlador de Materias
 * Endpoints para CRUD de materias académicas
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class MateriaController extends ResultUtil {

    private final MateriaService materiaService;

    /**
     * Obtener lista de materias
     * GET /sys/materia/list
     */
    @Log("Obtener lista de materias")
    @GetMapping("/materia/list")
    public ResponseEntity<Object> getMateriaList(String blurry) {
        try {
            return success(true, materiaService.getMateriaList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Consultar tabla de materias con paginación
     * GET /sys/materia/table
     */
    @Log("Consultar tabla de materias")
    @GetMapping("/materia/table")
    public ResponseEntity<Object> queryMateriaTable(QueryDto queryDto) {
        try {
            return success(true, materiaService.queryMateriaTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener materia por código
     * GET /sys/materia/{codmat}
     */
    @Log("Obtener materia por código")
    @GetMapping("/materia/{codmat}")
    public ResponseEntity<Object> getMateria(@PathVariable String codmat) {
        try {
            return success(true, materiaService.getMateriaWithNivel(codmat));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Editar o crear materia
     * POST /sys/materia/edit
     */
    @Log("Editar materia")
    @PostMapping("/materia/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editMateria(@RequestBody MateriaDto materiaDto) {
        try {
            String str = materiaDto.getCodmat() != null && 
                         materiaService.getMateriaList(null)
                            .stream()
                            .anyMatch(m -> m.getCodmat().equals(materiaDto.getCodmat())) 
                         ? "Editado con éxito" 
                         : "Agregado Exitosamente";
            materiaService.editMateria(materiaDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar materia
     * DELETE /sys/materia/del
     */
    @Log("Eliminar materia")
    @DeleteMapping("/materia/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delMateria(String codmat) {
        try {
            materiaService.delMateria(codmat);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
