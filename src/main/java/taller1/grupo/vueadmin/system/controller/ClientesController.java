package taller1.grupo.vueadmin.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.logs.annotation.Log;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.dto.ClientesDto;
import taller1.grupo.vueadmin.system.service.ClientesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class ClientesController extends ResultUtil {

    private final ClientesService clientesService;

    @Log("Obtener lista de clientes")
    @GetMapping("/clientes/list")
    public ResponseEntity<Object> getClientesList(String blurry) {
        try {
            return success(true, clientesService.getClientesList(blurry));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

    @Log("Consultar lista de clientes dto")
    @GetMapping("/clientes/table")
    public ResponseEntity<Object> queryClientesTable(QueryDto queryDto) {
        try {
            return success(true, clientesService.queryClientesTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

     @Log("Editar clientes")
    @PostMapping("/clientes/edit")
    public ResponseEntity<Object> editClientes(@RequestBody ClientesDto clientesDto) {
        try {
            System.out.println("id cliente" + clientesDto.getId());

            String str = StringUtil.getEditType(clientesDto.getId());
            /*
             * if (userDto.getEnabled() == null) {
             * userDto.setEnabled(false);
             * } else {
             * userDto.setEnabled(true);
             * }
             */
            // clientesService.editClientes(clientesDto);
            return success(true, str);
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }


    @Log("Eliminar cliente")
    @DeleteMapping("/clientes/del")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delClientes(@RequestParam Long id) {
        try {
            clientesService.delClientes(id);
            return success(true, "Eliminar exitosamente");
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }
}