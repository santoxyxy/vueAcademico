package taller1.grupo.vueadmin.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;

/**
 * Controlador de DATOS - Gestión de datos personales
 * @author Tu nombre
 * @date 2025-01-01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class DatosController extends ResultUtil {

    // Si necesitas implementar lógica aquí, agrega el servicio correspondiente
    // private final DatosService datosService;

    @Log("Endpoint de prueba - DATOS")
    @GetMapping("/datos/test")
    public ResponseEntity<Object> testDatos() {
        try {
            return success(true, "DatosController está funcionando correctamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}