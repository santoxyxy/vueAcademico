package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Dmodalidad;
import taller1.grupo.vueadmin.system.entity.dto.DmodalidadDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.DmodalidadMapper;
import taller1.grupo.vueadmin.system.mapper.ModalidadMapper;
import taller1.grupo.vueadmin.system.service.DmodalidadService;

import java.util.List;

/**
 * Implementación del servicio DMODALIDAD
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class DmodalidadServiceImpl implements DmodalidadService {

    private final DmodalidadMapper dmodalidadMapper;
    private final ModalidadMapper modalidadMapper;

    @Override
    public List<Dmodalidad> getDmodalidadList(String blurry) {
        LambdaQueryWrapper<Dmodalidad> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Dmodalidad::getNombre, blurry);
        }
        
        wrapper.orderByAsc(Dmodalidad::getCoddm);
        return dmodalidadMapper.selectList(wrapper);
    }

    @Override
    public IPage<DmodalidadDto> queryDmodalidadTable(QueryDto queryDto) {
        Page<DmodalidadDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return dmodalidadMapper.queryDmodalidadTable(page, queryDto.getBlurry());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDmodalidad(DmodalidadDto dmodalidadDto) {
        // Validaciones
        if (StringUtils.isBlank(dmodalidadDto.getNombre())) {
            throw new BadRequestException("El nombre del detalle es obligatorio");
        }
        if (dmodalidadDto.getCodm() == null) {
            throw new BadRequestException("Debe seleccionar una modalidad padre");
        }

        // Verificar que la modalidad padre exista
        var modalidadPadre = modalidadMapper.selectById(dmodalidadDto.getCodm());
        if (modalidadPadre == null) {
            throw new BadRequestException("La modalidad padre no existe");
        }

        // Verificar duplicados
        checkDmodalidad(dmodalidadDto);

        Dmodalidad dmodalidad = new Dmodalidad();
        dmodalidad.setCoddm(dmodalidadDto.getCoddm());
        dmodalidad.setNombre(dmodalidadDto.getNombre());
        dmodalidad.setEstado(dmodalidadDto.getEstado() != null ? dmodalidadDto.getEstado() : 1);
        dmodalidad.setCodm(dmodalidadDto.getCodm());

        if (dmodalidad.getCoddm() != null) {
            // Actualizar
            dmodalidadMapper.updateById(dmodalidad);
        } else {
            // Insertar
            dmodalidadMapper.insert(dmodalidad);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDmodalidad(Integer coddm) {
        Dmodalidad dmodalidad = dmodalidadMapper.selectById(coddm);
        if (dmodalidad == null) {
            throw new BadRequestException("El detalle de modalidad no existe");
        }

        // TODO: Verificar si tiene relaciones con NOTAS antes de eliminar
        dmodalidadMapper.deleteById(coddm);
    }

    @Override
    public List<DmodalidadDto> getDetallesByModalidad(Integer codm) {
        return dmodalidadMapper.getDetallesByModalidad(codm);
    }

    /**
     * Verificar que no exista un detalle con el mismo nombre en la misma modalidad
     */
    private void checkDmodalidad(DmodalidadDto dmodalidadDto) {
        LambdaQueryWrapper<Dmodalidad> wrapper = new LambdaQueryWrapper<>();

        if (dmodalidadDto.getCoddm() != null) {
            wrapper.ne(Dmodalidad::getCoddm, dmodalidadDto.getCoddm());
        }

        if (StringUtils.isNotBlank(dmodalidadDto.getNombre()) && dmodalidadDto.getCodm() != null) {
            wrapper.eq(Dmodalidad::getNombre, dmodalidadDto.getNombre())
                   .eq(Dmodalidad::getCodm, dmodalidadDto.getCodm());
        }

        long count = dmodalidadMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("Ya existe un detalle con ese nombre en esta modalidad");
        }
    }
}
