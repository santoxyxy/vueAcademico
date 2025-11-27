package taller1.grupo.vueadmin.system.service;

import taller1.grupo.vueadmin.system.entity.Personal;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UsuarioService {
    public IPage<Personal> queryUsuarioTable(QueryDto queryDto);

}