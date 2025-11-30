package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.NotasDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.NotasService;
import java.util.List;

/**
 * Controlador de NOTAS - Sistema de calificaciones
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class NotasController extends ResultUtil {
    private final NotasService notasService;

    /**
     * Consultar tabla de notas con paginación
     * GET /sys/notas/table?gestion=2025&codmat=MAT101&idusuario=3
     */
    @Log("Consultar tabla de NOTAS")
    @GetMapping("/notas/table")
    public ResponseEntity<Object> queryNotasTable(QueryDto queryDto,
                                                  @RequestParam(required = false) Integer gestion,
                                                  @RequestParam(required = false) String codmat,
                                                  @RequestParam(required = false) Integer idusuario) {
        try {
            return success(true, notasService.queryNotasTable(queryDto, gestion, codmat, idusuario));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener notas de un estudiante en una materia
     * GET /sys/notas/estudiante/{idusuario}/materia/{codmat}?gestion=2025
     */
    @Log("Obtener notas de estudiante en materia")
    @GetMapping("/notas/estudiante/{idusuario}/materia/{codmat}")
    public ResponseEntity<Object> getNotasEstudianteMateria(@PathVariable Integer idusuario,
                                                              @PathVariable String codmat,
                                                              @RequestParam Integer gestion) {
        try {
            return success(true, notasService.getNotasEstudianteMateria(codmat, idusuario, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Obtener notas de un paralelo para un ítem
     * GET /sys/notas/paralelo/{coda}/item/{codi}?codmat=MAT101&gestion=2025
     */
    @Log("Obtener notas de paralelo por ítem")
    @GetMapping("/notas/paralelo/{coda}/item/{codi}")
    public ResponseEntity<Object> getNotasParaleloItem(@PathVariable Integer coda,
                                                         @PathVariable Integer codi,
                                                         @RequestParam String codmat,
                                                         @RequestParam Integer gestion) {
        try {
            return success(true, notasService.getNotasParaleloItem(codmat, coda, codi, gestion));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Calcular nota final de un estudiante
     * GET /sys/notas/final/estudiante/{idusuario}/materia/{codmat}?gestion=2025
     */
    @Log("Calcular nota final")
    @GetMapping("/notas/final/estudiante/{idusuario}/materia/{codmat}")
    public ResponseEntity<Object> calcularNotaFinal(@PathVariable Integer idusuario,
                                                      @PathVariable String codmat,
                                                      @RequestParam Integer gestion) {
        try {
            Double notaFinal = notasService.calcularNotaFinal(codmat, idusuario, gestion);
            return success(true, notaFinal);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    } // Cierre del método calcularNotaFinal

    /**
     * Registrar o actualizar nota
     * POST /sys/notas/edit
     */
    @Log("Editar NOTA")
    @PostMapping("/notas/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> editNota(@RequestBody NotasDto notasDto) {
        try {
            notasService.editNota(notasDto);
            return success(true, "Nota guardada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Registrar notas masivas
     * POST /sys/notas/masivas
     */
    @Log("Registrar notas masivas")
    @PostMapping("/notas/masivas")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCENTE')")
    public ResponseEntity<Object> registrarNotasMasivas(@RequestBody List<NotasDto> notasList) {
        try {
            notasService.registrarNotasMasivas(notasList);
            return success(true, "Notas registradas exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    /**
     * Eliminar nota
     * DELETE /sys/notas/del?codmat=MAT101&codi=1&coda=1&codp=1&gestion=2025&idusuario=3
     */
    @Log("Eliminar NOTA")
    @DeleteMapping("/notas/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delNota(@RequestParam String codmat,
                                          @RequestParam Integer codi,
                                          @RequestParam Integer coda,
                                          @RequestParam Integer codp,
                                          @RequestParam Integer gestion,
                                          @RequestParam Integer idusuario) {
        try {
            notasService.delNota(codmat, codi, coda, codp, gestion, idusuario);
            return success(true, "Nota eliminada exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}
