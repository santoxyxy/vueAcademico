package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Modalidad;
import taller1.grupo.vueadmin.system.entity.dto.ModalidadDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.ModalidadMapper;
import taller1.grupo.vueadmin.system.service.ModalidadService;

import java.util.List;

/**
 * Implementación del servicio de Modalidad
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class ModalidadServiceImpl implements ModalidadService {

    private final ModalidadMapper modalidadMapper;

    /**
     * Obtener lista de modalidades con filtro
     */
    @Override
    public List<Modalidad> getModalidadList(String blurry) {
        LambdaQueryWrapper<Modalidad> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Modalidad::getNombre, blurry);
        }
        
        wrapper.orderByAsc(Modalidad::getCodm);
        return modalidadMapper.selectList(wrapper);
    }

    /**
     * Consulta paginada de modalidades
     */
    @Override
    public IPage<ModalidadDto> queryModalidadTable(QueryDto queryDto) {
        Page<ModalidadDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return modalidadMapper.queryModalidadTable(page, queryDto.getBlurry());
    }

    /**
     * Editar o crear modalidad
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editModalidad(ModalidadDto modalidadDto) {
        // Validar que el nombre no esté vacío
        if (StringUtils.isBlank(modalidadDto.getNombre())) {
            throw new BadRequestException("El nombre de la modalidad es obligatorio");
        }

        // Verificar duplicados
        checkModalidad(modalidadDto);

        Modalidad modalidad = new Modalidad();
        modalidad.setCodm(modalidadDto.getCodm());
        modalidad.setNombre(modalidadDto.getNombre());
        modalidad.setEstado(modalidadDto.getEstado() != null ? modalidadDto.getEstado() : 1);

        if (modalidad.getCodm() != null) {
            // Actualizar
            modalidadMapper.updateById(modalidad);
        } else {
            // Insertar
            modalidadMapper.insert(modalidad);
        }
    }

    /**
     * Eliminar modalidad
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delModalidad(Integer codm) {
        Modalidad modalidad = modalidadMapper.selectById(codm);
        if (modalidad == null) {
            throw new BadRequestException("La modalidad no existe");
        }

        // TODO: Verificar si tiene relaciones con DMODALIDAD antes de eliminar
        // Por ahora solo eliminamos
        modalidadMapper.deleteById(codm);
    }

    /**
     * Verificar que no exista una modalidad con el mismo nombre
     */
    private void checkModalidad(ModalidadDto modalidadDto) {
        LambdaQueryWrapper<Modalidad> wrapper = new LambdaQueryWrapper<>();

        if (modalidadDto.getCodm() != null) {
            wrapper.ne(Modalidad::getCodm, modalidadDto.getCodm());
        }

        if (StringUtils.isNotBlank(modalidadDto.getNombre())) {
            wrapper.eq(Modalidad::getNombre, modalidadDto.getNombre());
        }

        long count = modalidadMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("Ya existe una modalidad con ese nombre");
        }
    }
}
