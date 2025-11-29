package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.ModalidadDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.ModalidadService;

/**
 * Controlador de Modalidad
 * Endpoints para CRUD de modalidades académicas
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class ModalidadController extends ResultUtil {

    private final ModalidadService modalidadService;

    /**
     * Obtener lista de modalidades
     * GET /sys/modalidad/list
     */
    @Log("Obtener lista de modalidades")
    @GetMapping("/modalidad/list")
    public ResponseEntity<Object> getModalidadList(String blurry) {
        try {
            return success(true, modalidadService.getModalidadList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Consultar tabla de modalidades con paginación
     * GET /sys/modalidad/table
     */
    @Log("Consultar tabla de modalidades")
    @GetMapping("/modalidad/table")
    public ResponseEntity<Object> queryModalidadTable(QueryDto queryDto) {
        try {
            return success(true, modalidadService.queryModalidadTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Editar o crear modalidad
     * POST /sys/modalidad/edit
     */
    @Log("Editar modalidad")
    @PostMapping("/modalidad/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editModalidad(@RequestBody ModalidadDto modalidadDto) {
        try {
            String str = StringUtil.getEditType(modalidadDto.getCodm() != null ? 
                         Long.valueOf(modalidadDto.getCodm()) : null);
            modalidadService.editModalidad(modalidadDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar modalidad
     * DELETE /sys/modalidad/del
     */
    @Log("Eliminar modalidad")
    @DeleteMapping("/modalidad/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delModalidad(Integer id) {
        try {
            modalidadService.delModalidad(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}