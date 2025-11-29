package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.NivelesDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.NivelesService;

/**
 * Controlador de Niveles
 * Endpoints para CRUD de niveles académicos
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class NivelesController extends ResultUtil {

    private final NivelesService nivelesService;

    /**
     * Obtener lista de niveles
     * GET /sys/niveles/list
     */
    @Log("Obtener lista de niveles")
    @GetMapping("/niveles/list")
    public ResponseEntity<Object> getNivelesList(String blurry) {
        try {
            return success(true, nivelesService.getNivelesList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Consultar tabla de niveles con paginación
     * GET /sys/niveles/table
     */
    @Log("Consultar tabla de niveles")
    @GetMapping("/niveles/table")
    public ResponseEntity<Object> queryNivelesTable(QueryDto queryDto) {
        try {
            return success(true, nivelesService.queryNivelesTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Editar o crear nivel
     * POST /sys/niveles/edit
     */
    @Log("Editar nivel")
    @PostMapping("/niveles/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editNiveles(@RequestBody NivelesDto nivelesDto) {
        try {
            String str = StringUtil.getEditType(nivelesDto.getCodn() != null ? 
                         Long.valueOf(nivelesDto.getCodn()) : null);
            nivelesService.editNiveles(nivelesDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar nivel
     * DELETE /sys/niveles/del
     */
    @Log("Eliminar nivel")
    @DeleteMapping("/niveles/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delNiveles(Integer id) {
        try {
            nivelesService.delNiveles(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
