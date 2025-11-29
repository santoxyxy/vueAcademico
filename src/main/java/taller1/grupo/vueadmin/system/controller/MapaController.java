package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.MapaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.MapaService;

/**
 * Controlador de MAPA - Gestión de oferta académica
 * Endpoints para asignar paralelos a materias por gestión
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class MapaController extends ResultUtil {

    private final MapaService mapaService;

    /**
     * Obtener tabla de mapas con paginación
     * GET /sys/mapa/table?gestion=2025
     */
    @Log("Consultar tabla de MAPA")
    @GetMapping("/mapa/table")
    public ResponseEntity<Object> queryMapaTable(QueryDto queryDto, 
                                                   @RequestParam(required = false) Integer gestion) {
        try {
            return success(true, mapaService.queryMapaTable(queryDto, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener paralelos asignados a una materia
     * GET /sys/mapa/paralelos/{codmat}?gestion=2025
     */
    @Log("Obtener paralelos por materia")
    @GetMapping("/mapa/paralelos/{codmat}")
    public ResponseEntity<Object> getParalelosByMateria(@PathVariable String codmat,
                                                          @RequestParam Integer gestion) {
        try {
            return success(true, mapaService.getParalelosByMateria(codmat, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Crear o actualizar asignación materia-paralelo
     * POST /sys/mapa/edit
     */
    @Log("Editar MAPA")
    @PostMapping("/mapa/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editMapa(@RequestBody MapaDto mapaDto) {
        try {
            mapaService.editMapa(mapaDto);
            return success(true, "Asignación guardada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar asignación materia-paralelo
     * DELETE /sys/mapa/del?codmat=MAT101&codpar=1&gestion=2025
     */
    @Log("Eliminar MAPA")
    @DeleteMapping("/mapa/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delMapa(@RequestParam String codmat,
                                           @RequestParam Integer codpar,
                                           @RequestParam Integer gestion) {
        try {
            mapaService.delMapa(codmat, codpar, gestion);
            return success(true, "Asignación eliminada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
