package taller1.grupo.vueadmin.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.service.UsuarioService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class UsuarioController extends ResultUtil {
private final UsuarioService usuarioService;

@Log("Consultar lista de Usuarios dto")
    @GetMapping("/usuario/table")
    public ResponseEntity<Object> queryUsuarioTable(QueryDto queryDto) {
        try {
            return success(true, usuarioService.queryUsuarioTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

}
