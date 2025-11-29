package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Materia;
import taller1.grupo.vueadmin.system.entity.dto.MateriaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.MateriaMapper;
import taller1.grupo.vueadmin.system.service.MateriaService;

import java.util.List;

/**
 * Implementación del servicio de Materias
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class MateriaServiceImpl implements MateriaService {

    private final MateriaMapper materiaMapper;

    /**
     * Obtener lista de materias con filtro
     */
    @Override
    public List<Materia> getMateriaList(String blurry) {
        LambdaQueryWrapper<Materia> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Materia::getNombre, blurry)
                   .or()
                   .like(Materia::getCodmat, blurry);
        }
        
        wrapper.orderByAsc(Materia::getCodmat);
        return materiaMapper.selectList(wrapper);
    }

    /**
     * Consulta paginada de materias
     */
    @Override
    public IPage<MateriaDto> queryMateriaTable(QueryDto queryDto) {
        Page<MateriaDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return materiaMapper.queryMateriaTable(page, queryDto.getBlurry());
    }

    /**
     * Editar o crear materia
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editMateria(MateriaDto materiaDto) {
        // Validaciones
        if (StringUtils.isBlank(materiaDto.getCodmat())) {
            throw new BadRequestException("El código de materia es obligatorio");
        }
        if (StringUtils.isBlank(materiaDto.getNombre())) {
            throw new BadRequestException("El nombre de la materia es obligatorio");
        }
        if (materiaDto.getCodn() == null) {
            throw new BadRequestException("Debe seleccionar un nivel académico");
        }

        // Verificar duplicados
        checkMateria(materiaDto);

        Materia materia = new Materia();
        materia.setCodmat(materiaDto.getCodmat().toUpperCase().trim());
        materia.setNombre(materiaDto.getNombre());
        materia.setCodn(materiaDto.getCodn());
        materia.setEstado(materiaDto.getEstado() != null ? materiaDto.getEstado() : 1);

        // Verificar si es actualización o inserción
        Materia existe = materiaMapper.selectById(materiaDto.getCodmat());
        
        if (existe != null) {
            // Actualizar
            materiaMapper.updateById(materia);
        } else {
            // Insertar
            materiaMapper.insert(materia);
        }
    }

    /**
     * Eliminar materia
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMateria(String codmat) {
        Materia materia = materiaMapper.selectById(codmat);
        if (materia == null) {
            throw new BadRequestException("La materia no existe");
        }

        // TODO: Verificar si tiene relaciones con MAPA, DICTA, PROGRA antes de eliminar
        // Por ahora solo eliminamos
        materiaMapper.deleteById(codmat);
    }

    /**
     * Obtener materia con nombre del nivel
     */
    @Override
    public MateriaDto getMateriaWithNivel(String codmat) {
        MateriaDto materiaDto = materiaMapper.getMateriaWithNivel(codmat);
        if (materiaDto == null) {
            throw new BadRequestException("La materia no existe");
        }
        return materiaDto;
    }

    /**
     * Verificar que no exista una materia con el mismo código o nombre
     */
    private void checkMateria(MateriaDto materiaDto) {
        LambdaQueryWrapper<Materia> wrapper = new LambdaQueryWrapper<>();

        // Si es actualización, excluir el registro actual
        Materia existe = materiaMapper.selectById(materiaDto.getCodmat());
        
        // Solo verificar nombre si es inserción o si cambió el nombre
        if (StringUtils.isNotBlank(materiaDto.getNombre())) {
            wrapper.eq(Materia::getNombre, materiaDto.getNombre());
            
            if (existe != null) {
                wrapper.ne(Materia::getCodmat, materiaDto.getCodmat());
            }
            
            long count = materiaMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BadRequestException("Ya existe una materia con ese nombre");
            }
        }
    }
}
