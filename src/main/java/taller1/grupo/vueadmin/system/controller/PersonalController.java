package taller1.grupo.vueadmin.system.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.ResultUtil;
import taller1.grupo.vueadmin.system.entity.dto.PersonaUpsertDTO;
import taller1.grupo.vueadmin.system.entity.vo.PersonaVO;
import taller1.grupo.vueadmin.system.service.PersonalService;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import org.springframework.http.ResponseEntity; 

@RestController
@RequestMapping("/sys")
public class PersonalController extends ResultUtil {

  @Resource 
  private PersonalService personalService;

  // Página de Personas (JOIN personal + datos)
  @GetMapping("/personal/table")
  public ResponseEntity<Object> queryPersonalTable(QueryDto queryDto) {
        try {
            return success(true, personalService.queryPersonalTable(queryDto));
        } catch (BadRequestException e) {
            return fail(false, e.getMsg());
        }
    }

  @GetMapping("/personal/{idusuario}")
  public ResponseEntity<Object> get(@PathVariable Long idusuario) {
    try {
      return success(true, personalService.getByUserId(idusuario));
    } catch (BadRequestException e) {
      return fail(false, e.getMsg());
    }
  }

  @PostMapping("/personal")
  public ResponseEntity<Object> create(@Valid @RequestBody PersonaUpsertDTO dto) {
    try {
      return success(true, personalService.create(dto));
    } catch (BadRequestException e) {
      return fail(false, e.getMsg());
    }
  }

  @PutMapping("/personal/{idusuario}")
  public ResponseEntity<Object> update(@PathVariable Long idusuario,
                          @Valid @RequestBody PersonaUpsertDTO dto) {
    try {
      dto.setIdusuario(idusuario);
      return success(true, personalService.update(idusuario, dto));
    } catch (BadRequestException e) {
      return fail(false, e.getMsg());
    }
  }

  @DeleteMapping("/personal/{idusuario}")
  public ResponseEntity<Object> delete(@PathVariable Long idusuario) {
    try {
      personalService.delete(idusuario);
      return success(true, "Eliminar exitosamente");
    } catch (BadRequestException e) {
      return fail(false, e.getMsg());
    }
  }
}