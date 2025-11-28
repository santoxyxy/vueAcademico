package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.ParaleloDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.ParaleloService;

/**
 * Controlador de Paralelos
 * Endpoints para CRUD de paralelos
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class ParaleloController extends ResultUtil {

    private final ParaleloService paraleloService;

    /**
     * Obtener lista de paralelos
     * GET /sys/paralelo/list
     */
    @Log("Obtener lista de paralelos")
    @GetMapping("/paralelo/list")
    public ResponseEntity<Object> getParaleloList(String blurry) {
        try {
            return success(true, paraleloService.getParaleloList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Consultar tabla de paralelos con paginación
     * GET /sys/paralelo/table
     */
    @Log("Consultar tabla de paralelos")
    @GetMapping("/paralelo/table")
    public ResponseEntity<Object> queryParaleloTable(QueryDto queryDto) {
        try {
            return success(true, paraleloService.queryParaleloTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Editar o crear paralelo
     * POST /sys/paralelo/edit
     */
    @Log("Editar paralelo")
    @PostMapping("/paralelo/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editParalelo(@RequestBody ParaleloDto paraleloDto) {
        try {
            String str = StringUtil.getEditType(paraleloDto.getCodp());
            paraleloService.editParalelo(paraleloDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar paralelo
     * DELETE /sys/paralelo/del
     */
    @Log("Eliminar paralelo")
    @DeleteMapping("/paralelo/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delParalelo(Integer id) {
        try {
            paraleloService.delParalelo(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}