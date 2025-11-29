package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Dicta;
import taller1.grupo.vueadmin.system.entity.dto.DictaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.DictaMapper;
import taller1.grupo.vueadmin.system.mapper.MapaMapper;
import taller1.grupo.vueadmin.system.mapper.PersonalMapper;
import taller1.grupo.vueadmin.system.service.DictaService;

import java.util.List;

/**
 * Implementación del servicio DICTA
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class DictaServiceImpl implements DictaService {

    private final DictaMapper dictaMapper;
    private final MapaMapper mapaMapper;
    private final PersonalMapper personalMapper;

    @Override
    public IPage<DictaDto> queryDictaTable(QueryDto queryDto, 
                                           Integer gestion, 
                                           String codmat,
                                           Integer codn) {
        Page<DictaDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return dictaMapper.queryDictaTable(page, queryDto.getBlurry(), gestion, codmat, codn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDicta(DictaDto dictaDto) {
        // Validaciones básicas
        if (StringUtils.isBlank(dictaDto.getCodmat())) {
            throw new BadRequestException("Debe seleccionar una materia");
        }
        if (dictaDto.getCodpar() == null) {
            throw new BadRequestException("Debe seleccionar un paralelo");
        }
        if (dictaDto.getCodp() == null) {
            dictaDto.setCodp(1); // Valor por defecto según diseño
        }
        if (dictaDto.getGestion() == null) {
            throw new BadRequestException("Debe especificar la gestión académica");
        }
        if (dictaDto.getIds() == null) {
            throw new BadRequestException("Debe seleccionar un docente");
        }

        // Validar que el paralelo esté ofertado en MAPA
        var mapaExiste = mapaMapper.getMapaByKeys(
            dictaDto.getCodmat(),
            dictaDto.getCodpar(),
            dictaDto.getGestion()
        );
        if (mapaExiste == null) {
            throw new BadRequestException(
                "El paralelo no está ofertado para esta materia en la gestión seleccionada. " +
                "Debe configurarlo primero en MAPA"
            );
        }

        // Validar que el docente exista en PERSONAL
        var docente = personalMapper.selectById(dictaDto.getIds());
        if (docente == null) {
            throw new BadRequestException("El docente seleccionado no existe en el sistema");
        }

        // Validar que sea docente
        if (!"DOCENTE".equalsIgnoreCase(docente.getTipo())) {
            throw new BadRequestException("El usuario seleccionado no es un docente");
        }

        // Verificar si ya existe asignación
        DictaDto existe = dictaMapper.getDictaByKeys(
            dictaDto.getCodpar(),
            dictaDto.getCodp(),
            dictaDto.getCodmat(),
            dictaDto.getGestion()
        );

        Dicta dicta = new Dicta();
        dicta.setCodpar(dictaDto.getCodpar());
        dicta.setCodp(dictaDto.getCodp());
        dicta.setCodmat(dictaDto.getCodmat());
        dicta.setGestion(dictaDto.getGestion());
        dicta.setIds(dictaDto.getIds());

        if (existe != null) {
            // Actualizar: delete + insert
            dictaMapper.delete(new LambdaQueryWrapper<Dicta>()
                .eq(Dicta::getCodpar, dicta.getCodpar())
                .eq(Dicta::getCodp, dicta.getCodp())
                .eq(Dicta::getCodmat, dicta.getCodmat())
                .eq(Dicta::getGestion, dicta.getGestion()));
            dictaMapper.insert(dicta);
        } else {
            // Insertar nuevo
            dictaMapper.insert(dicta);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDicta(Integer codpar, Integer codp, String codmat, Integer gestion) {
        DictaDto existe = dictaMapper.getDictaByKeys(codpar, codp, codmat, gestion);
        if (existe == null) {
            throw new BadRequestException("La asignación no existe");
        }

        // TODO: Verificar si tiene alumnos inscritos en PROGRA antes de eliminar
        // Si hay alumnos, no permitir eliminación o dar advertencia

        LambdaQueryWrapper<Dicta> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dicta::getCodpar, codpar)
               .eq(Dicta::getCodp, codp)
               .eq(Dicta::getCodmat, codmat)
               .eq(Dicta::getGestion, gestion);
        
        dictaMapper.delete(wrapper);
    }

    @Override
    public List<DictaDto> getMateriasByDocente(Integer ids, Integer gestion) {
        if (ids == null) {
            throw new BadRequestException("Debe especificar el docente");
        }
        return dictaMapper.getMateriasByDocente(ids, gestion);
    }

    @Override
    public List<DictaDto> getDocentesByMateria(String codmat, Integer gestion) {
        if (StringUtils.isBlank(codmat)) {
            throw new BadRequestException("Debe especificar la materia");
        }
        return dictaMapper.getDocentesByMateria(codmat, gestion);
    }
}
