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

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class MateriaController extends ResultUtil {

    private final MateriaService materiaService;

    @Log("Obtener lista de materias")
    @GetMapping("/materia/list")
    public ResponseEntity<Object> getMateriaList(String blurry) {
        try {
            return success(true, materiaService.getMateriaList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Consultar tabla de materias")
    @GetMapping("/materia/table")
    public ResponseEntity<Object> queryMateriaTable(QueryDto queryDto) {
        try {
            return success(true, materiaService.queryMateriaTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Obtener materia por código")
    @GetMapping("/materia/{codmat}")
    public ResponseEntity<Object> getMateria(@PathVariable String codmat) {
        try {
            return success(true, materiaService.getMateriaWithNivel(codmat));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Editar materia")
    @PostMapping("/materia/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editMateria(@RequestBody MateriaDto materiaDto) {
        try {
            // Usa el servicio para verificar si existe, no hagas la llamada aquí
            String str = StringUtil.getEditType(materiaDto.getCodmat() != null ? 1L : null);
            materiaService.editMateria(materiaDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Eliminar materia")
    @DeleteMapping("/materia/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delMateria(@RequestParam String codmat) {
        try {
            materiaService.delMateria(codmat);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}