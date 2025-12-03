package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.DmodalidadDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.DmodalidadService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class DmodalidadController extends ResultUtil {

    private final DmodalidadService dmodalidadService;

    @Log("Obtener lista de detalles de modalidades")
    @GetMapping("/dmodalidad/list")
    public ResponseEntity<Object> getDmodalidadList(String blurry) {
        try {
            return success(true, dmodalidadService.getDmodalidadList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Consultar tabla de detalles de modalidades")
    @GetMapping("/dmodalidad/table")
    public ResponseEntity<Object> queryDmodalidadTable(QueryDto queryDto) {
        try {
            return success(true, dmodalidadService.queryDmodalidadTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Obtener detalles por modalidad")
    @GetMapping("/dmodalidad/modalidad/{codm}")
    public ResponseEntity<Object> getDetallesByModalidad(@PathVariable Integer codm) {
        try {
            return success(true, dmodalidadService.getDetallesByModalidad(codm));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Editar detalle de modalidad")
    @PostMapping("/dmodalidad/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editDmodalidad(@RequestBody DmodalidadDto dmodalidadDto) {
        try {
            String str = StringUtil.getEditType(dmodalidadDto.getCoddm() != null ? 
                         Long.valueOf(dmodalidadDto.getCoddm()) : null);
            dmodalidadService.editDmodalidad(dmodalidadDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Eliminar detalle de modalidad")
    @DeleteMapping("/dmodalidad/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delDmodalidad(@RequestParam Integer id) {
        try {
            dmodalidadService.delDmodalidad(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}