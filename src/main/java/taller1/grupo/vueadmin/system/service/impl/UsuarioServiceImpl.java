package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import taller1.grupo.vueadmin.system.entity.Personal;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.UsuarioMapper;
import taller1.grupo.vueadmin.system.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioMapper usuarioMapper;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Override
    public IPage<Personal> queryUsuarioTable(QueryDto queryDto) {
        Page<Personal> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return usuarioMapper.queryUsuarioTable(page, queryDto.getBlurry());
    }

 
}
