package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Progra;
import taller1.grupo.vueadmin.system.entity.dto.PrograDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.PrograMapper;
import taller1.grupo.vueadmin.system.mapper.MapaMapper;
import taller1.grupo.vueadmin.system.mapper.PersonalMapper;
import taller1.grupo.vueadmin.system.service.PrograService;

import java.util.List;

/**
 * Implementación del servicio PROGRA
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class PrograServiceImpl implements PrograService {

    private final PrograMapper prograMapper;
    private final MapaMapper mapaMapper;
    private final PersonalMapper personalMapper;

    @Override
    public IPage<PrograDto> queryPrograTable(QueryDto queryDto, 
                                              Integer gestion, 
                                              String codmat,
                                              Integer codn) {
        Page<PrograDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return prograMapper.queryPrograTable(page, queryDto.getBlurry(), gestion, codmat, codn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editProgra(PrograDto prograDto) {
        // Validaciones básicas
        if (StringUtils.isBlank(prograDto.getCodmat())) {
            throw new BadRequestException("Debe seleccionar una materia");
        }
        if (prograDto.getCodpar() == null) {
            throw new BadRequestException("Debe seleccionar un paralelo");
        }
        if (prograDto.getCodp() == null) {
            prograDto.setCodp(1); // Valor por defecto según diseño
        }
        if (prograDto.getGestion() == null) {
            throw new BadRequestException("Debe especificar la gestión académica");
        }
        if (prograDto.getIds() == null) {
            throw new BadRequestException("Debe seleccionar un docente responsable");
        }

        // Validar que el paralelo esté ofertado en MAPA
        var mapaExiste = mapaMapper.getMapaByKeys(
            prograDto.getCodmat(),
            prograDto.getCodpar(),
            prograDto.getGestion()
        );
        if (mapaExiste == null) {
            throw new BadRequestException(
                "El paralelo no está ofertado para esta materia en la gestión seleccionada. " +
                "Debe configurarlo primero en MAPA"
            );
        }

        // Validar que el docente exista en PERSONAL
        var docente = personalMapper.selectById(prograDto.getIds());
        if (docente == null) {
            throw new BadRequestException("El docente seleccionado no existe en el sistema");
        }

        // Validar que sea docente
        if (!"DOCENTE".equalsIgnoreCase(docente.getTipo())) {
            throw new BadRequestException("El usuario seleccionado no es un docente");
        }

        // Verificar si ya existe clase
        PrograDto existe = prograMapper.getPrograByKeys(
            prograDto.getCodpar(),
            prograDto.getCodp(),
            prograDto.getCodmat(),
            prograDto.getGestion()
        );

        Progra progra = new Progra();
        progra.setCodpar(prograDto.getCodpar());
        progra.setCodp(prograDto.getCodp());
        progra.setCodmat(prograDto.getCodmat());
        progra.setGestion(prograDto.getGestion());
        progra.setIds(prograDto.getIds());

        if (existe != null) {
            // Actualizar: delete + insert (PK compuesta)
            prograMapper.delete(new LambdaQueryWrapper<Progra>()
                .eq(Progra::getCodpar, progra.getCodpar())
                .eq(Progra::getCodp, progra.getCodp())
                .eq(Progra::getCodmat, progra.getCodmat())
                .eq(Progra::getGestion, progra.getGestion()));
            prograMapper.insert(progra);
        } else {
            // Insertar nuevo
            prograMapper.insert(progra);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delProgra(Integer codpar, Integer codp, String codmat, Integer gestion) {
        PrograDto existe = prograMapper.getPrograByKeys(codpar, codp, codmat, gestion);
        if (existe == null) {
            throw new BadRequestException("La clase no existe");
        }

        // TODO: Verificar si tiene estudiantes inscritos en NOTAS antes de eliminar
        // Si hay estudiantes, no permitir eliminación o dar advertencia

        LambdaQueryWrapper<Progra> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Progra::getCodpar, codpar)
               .eq(Progra::getCodp, codp)
               .eq(Progra::getCodmat, codmat)
               .eq(Progra::getGestion, gestion);
        
        prograMapper.delete(wrapper);
    }

    @Override
    public List<PrograDto> getClasesByDocente(Integer ids, Integer gestion) {
        if (ids == null) {
            throw new BadRequestException("Debe especificar el docente");
        }
        return prograMapper.getClasesByDocente(ids, gestion);
    }

    @Override
    public List<PrograDto> getParalelosByMateria(String codmat, Integer gestion) {
        if (StringUtils.isBlank(codmat)) {
            throw new BadRequestException("Debe especificar la materia");
        }
        return prograMapper.getParalelosByMateria(codmat, gestion);
    }
}
