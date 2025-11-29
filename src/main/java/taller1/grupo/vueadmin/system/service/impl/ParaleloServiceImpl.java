package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Paralelo;
import taller1.grupo.vueadmin.system.entity.dto.ParaleloDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.ParaleloMapper;
import taller1.grupo.vueadmin.system.service.ParaleloService;

import java.util.List;

/**
 * Implementación del servicio de Paralelos
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class ParaleloServiceImpl implements ParaleloService {

    private final ParaleloMapper paraleloMapper;

    /**
     * Obtener lista de paralelos con filtro
     */
    @Override
    public List<Paralelo> getParaleloList(String blurry) {
        LambdaQueryWrapper<Paralelo> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Paralelo::getNombre, blurry);
        }
        
        wrapper.orderByAsc(Paralelo::getCodp);
        return paraleloMapper.selectList(wrapper);
    }

    /**
     * Consulta paginada de paralelos
     */
    @Override
    public IPage<ParaleloDto> queryParaleloTable(QueryDto queryDto) {
        Page<ParaleloDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return paraleloMapper.queryParaleloTable(page, queryDto.getBlurry());
    }

    /**
     * Editar o crear paralelo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editParalelo(ParaleloDto paraleloDto) {
        // Validar que el nombre no esté vacío
        if (StringUtils.isBlank(paraleloDto.getNombre())) {
            throw new BadRequestException("El nombre del paralelo es obligatorio");
        }

        // Verificar duplicados
        checkParalelo(paraleloDto);

        Paralelo paralelo = new Paralelo();
        paralelo.setCodp(paraleloDto.getCodp());
        paralelo.setNombre(paraleloDto.getNombre());
        paralelo.setEstado(paraleloDto.getEstado() != null ? paraleloDto.getEstado() : 1);

        if (paralelo.getCodp() != null) {
            // Actualizar
            paraleloMapper.updateById(paralelo);
        } else {
            // Insertar
            paraleloMapper.insert(paralelo);
        }
    }

    /**
     * Eliminar paralelo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delParalelo(Long id) {
        Paralelo paralelo = paraleloMapper.selectById(id);
        if (paralelo == null) {
            throw new BadRequestException("El paralelo no existe");
        }

        // TODO: Verificar si tiene relaciones con MAPA antes de eliminar
        // Por ahora solo eliminamos
        paraleloMapper.deleteById(id);
    }

    /**
     * Verificar que no exista un paralelo con el mismo nombre
     */
    private void checkParalelo(ParaleloDto paraleloDto) {
        LambdaQueryWrapper<Paralelo> wrapper = new LambdaQueryWrapper<>();

        if (paraleloDto.getCodp() != null) {
            wrapper.ne(Paralelo::getCodp, paraleloDto.getCodp());
        }

        if (StringUtils.isNotBlank(paraleloDto.getNombre())) {
            wrapper.eq(Paralelo::getNombre, paraleloDto.getNombre());
        }

        long count = paraleloMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("Ya existe un paralelo con ese nombre");
        }
    }
}