package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Niveles;
import taller1.grupo.vueadmin.system.entity.dto.NivelesDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.NivelesMapper;
import taller1.grupo.vueadmin.system.service.NivelesService;

import java.util.List;

/**
 * Implementación del servicio de Niveles
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class NivelesServiceImpl implements NivelesService {

    private final NivelesMapper nivelesMapper;

    /**
     * Obtener lista de niveles con filtro
     */
    @Override
    public List<Niveles> getNivelesList(String blurry) {
        LambdaQueryWrapper<Niveles> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Niveles::getNombre, blurry);
        }
        
        wrapper.orderByAsc(Niveles::getCodn);
        return nivelesMapper.selectList(wrapper);
    }

    /**
     * Consulta paginada de niveles
     */
    @Override
    public IPage<NivelesDto> queryNivelesTable(QueryDto queryDto) {
        Page<NivelesDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return nivelesMapper.queryNivelesTable(page, queryDto.getBlurry());
    }

    /**
     * Editar o crear nivel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editNiveles(NivelesDto nivelesDto) {
        // Validar que el nombre no esté vacío
        if (StringUtils.isBlank(nivelesDto.getNombre())) {
            throw new BadRequestException("El nombre del nivel es obligatorio");
        }

        // Verificar duplicados
        checkNiveles(nivelesDto);

        Niveles niveles = new Niveles();
        niveles.setCodn(nivelesDto.getCodn());
        niveles.setNombre(nivelesDto.getNombre());
        niveles.setEstado(nivelesDto.getEstado() != null ? nivelesDto.getEstado() : 1);

        if (niveles.getCodn() != null) {
            // Actualizar
            nivelesMapper.updateById(niveles);
        } else {
            // Insertar
            nivelesMapper.insert(niveles);
        }
    }

    /**
     * Eliminar nivel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delNiveles(Integer codn) {
        Niveles niveles = nivelesMapper.selectById(codn);
        if (niveles == null) {
            throw new BadRequestException("El nivel no existe");
        }

        // TODO: Verificar si tiene materias asociadas antes de eliminar
        // Por ahora solo eliminamos
        nivelesMapper.deleteById(codn);
    }

    /**
     * Verificar que no exista un nivel con el mismo nombre
     */
    private void checkNiveles(NivelesDto nivelesDto) {
        LambdaQueryWrapper<Niveles> wrapper = new LambdaQueryWrapper<>();

        if (nivelesDto.getCodn() != null) {
            wrapper.ne(Niveles::getCodn, nivelesDto.getCodn());
        }

        if (StringUtils.isNotBlank(nivelesDto.getNombre())) {
            wrapper.eq(Niveles::getNombre, nivelesDto.getNombre());
        }

        long count = nivelesMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("Ya existe un nivel con ese nombre");
        }
    }
}