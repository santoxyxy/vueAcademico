package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.General;
import taller1.grupo.vueadmin.system.entity.dto.GeneralDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.GeneralMapper;
import taller1.grupo.vueadmin.system.mapper.SysUserMapper;
import taller1.grupo.vueadmin.system.service.GeneralService;

import java.util.List;

/**
 * Implementación del servicio GENERAL
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final GeneralMapper generalMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public List<General> getGeneralList(Integer gestion) {
        LambdaQueryWrapper<General> wrapper = new LambdaQueryWrapper<>();
        
        if (gestion != null) {
            wrapper.eq(General::getGestion, gestion);
        }
        
        wrapper.orderByDesc(General::getGestion);
        return generalMapper.selectList(wrapper);
    }

    @Override
    public IPage<GeneralDto> queryGeneralTable(QueryDto queryDto, Integer gestion) {
        Page<GeneralDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return generalMapper.queryGeneralTable(page, queryDto.getBlurry(), gestion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editGeneral(GeneralDto generalDto) {
        // Validaciones
        if (generalDto.getIds() == null) {
            throw new BadRequestException("Debe especificar el usuario");
        }
        if (generalDto.getGestion() == null) {
            throw new BadRequestException("Debe especificar la gestión");
        }

        // Verificar que el usuario existe
        var usuario = sysUserMapper.selectById(generalDto.getIds());
        if (usuario == null) {
            throw new BadRequestException("El usuario no existe");
        }

        // Verificar si ya existe el registro
        Integer existe = generalMapper.existeEnGestion(
            generalDto.getIds(),
            generalDto.getGestion()
        );

        General general = new General();
        general.setIds(generalDto.getIds());
        general.setGestion(generalDto.getGestion());

        if (existe > 0) {
            // Actualizar (aunque no hay mucho que actualizar en esta tabla)
            generalMapper.updateById(general);
        } else {
            // Insertar nuevo
            generalMapper.insert(general);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delGeneral(Integer ids, Integer gestion) {
        if (ids == null) {
            throw new BadRequestException("Debe especificar el usuario");
        }

        LambdaQueryWrapper<General> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(General::getIds, ids);
        
        if (gestion != null) {
            // Eliminar solo de esa gestión
            wrapper.eq(General::getGestion, gestion);
        }
        // Si gestion es null, elimina de todas las gestiones

        generalMapper.delete(wrapper);
    }

    @Override
    public GeneralDto getGeneralByUserAndGestion(Integer ids, Integer gestion) {
        if (ids == null || gestion == null) {
            throw new BadRequestException("Debe especificar usuario y gestión");
        }
        return generalMapper.getGeneralByUserAndGestion(ids, gestion);
    }

    @Override
    public List<Integer> getGestionesByUser(Integer ids) {
        if (ids == null) {
            throw new BadRequestException("Debe especificar el usuario");
        }
        return generalMapper.getGestionesByUser(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registrarEnGestion(Integer ids, Integer gestion) {
        if (ids == null || gestion == null) {
            throw new BadRequestException("Debe especificar usuario y gestión");
        }

        // Verificar que el usuario existe
        var usuario = sysUserMapper.selectById(ids);
        if (usuario == null) {
            throw new BadRequestException("El usuario no existe");
        }

        // Verificar si ya está registrado
        Integer existe = generalMapper.existeEnGestion(ids, gestion);
        
        if (existe == 0) {
            // Registrar automáticamente
            General general = new General();
            general.setIds(ids);
            general.setGestion(gestion);
            generalMapper.insert(general);
        }
        // Si ya existe, no hace nada
    }
}
