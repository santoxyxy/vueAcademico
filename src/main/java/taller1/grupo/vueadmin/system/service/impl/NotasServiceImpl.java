package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Notas;
import taller1.grupo.vueadmin.system.entity.Progra;
import taller1.grupo.vueadmin.system.entity.dto.NotasDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.*;
import taller1.grupo.vueadmin.system.service.NotasService;

import java.util.List;

/**
 * Implementación del servicio NOTAS
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class NotasServiceImpl implements NotasService {

    private final NotasMapper notasMapper;
    private final PrograMapper prograMapper;
    private final ItematMapper itematMapper;

    @Override
    public IPage<NotasDto> queryNotasTable(QueryDto queryDto, 
                                           Integer gestion, 
                                           String codmat,
                                           Integer idusuario) {
        Page<NotasDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return notasMapper.queryNotasTable(page, queryDto.getBlurry(), 
                                           gestion, codmat, idusuario);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editNota(NotasDto notasDto) {
        // Validaciones básicas
        if (StringUtils.isBlank(notasDto.getCodmat())) {
            throw new BadRequestException("Debe especificar la materia");
        }
        if (notasDto.getCodi() == null) {
            throw new BadRequestException("Debe especificar el ítem de evaluación");
        }
        if (notasDto.getCoda() == null) {
            throw new BadRequestException("Debe especificar el paralelo");
        }
        if (notasDto.getCodp() == null) {
            notasDto.setCodp(1); // Valor por defecto
        }
        if (notasDto.getGestion() == null) {
            throw new BadRequestException("Debe especificar la gestión");
        }
        if (notasDto.getIdusuario() == null) {
            throw new BadRequestException("Debe especificar el estudiante");
        }
        if (notasDto.getNota() == null) {
            throw new BadRequestException("Debe ingresar una calificación");
        }

        // Validar rango de nota (0-100)
        if (notasDto.getNota() < 0 || notasDto.getNota() > 100) {
            throw new BadRequestException("La nota debe estar entre 0 y 100");
        }

        // Validar que el estudiante esté inscrito en PROGRA
        var inscripcion = prograMapper.selectOne(new LambdaQueryWrapper<Progra>()
    .eq(Progra::getCodmat, notasDto.getCodmat())
    .eq(Progra::getCodpar, notasDto.getCoda())
    .eq(Progra::getCodp, notasDto.getCodp())
    .eq(Progra::getGestion, notasDto.getGestion()));
        
        if (inscripcion == null) {
            throw new BadRequestException(
                "El estudiante no está inscrito en esta materia/paralelo");
        }

        // Validar que el ítem esté configurado en ITEMAT
        var itemConfig = itematMapper.getItematByKeys(
            notasDto.getCodmat(),
            notasDto.getCodi(),
            notasDto.getGestion()
        );
        
        if (itemConfig == null) {
            throw new BadRequestException(
                "El ítem de evaluación no está configurado para esta materia");
        }

        // Verificar si ya existe la nota
        NotasDto existe = notasMapper.getNotaByKeys(
            notasDto.getCodmat(),
            notasDto.getCodi(),
            notasDto.getCoda(),
            notasDto.getCodp(),
            notasDto.getGestion(),
            notasDto.getIdusuario()
        );

        Notas notas = new Notas();
        notas.setCodmat(notasDto.getCodmat());
        notas.setCodi(notasDto.getCodi());
        notas.setCoda(notasDto.getCoda());
        notas.setCodp(notasDto.getCodp());
        notas.setGestion(notasDto.getGestion());
        notas.setIdusuario(notasDto.getIdusuario());
        notas.setNota(notasDto.getNota());
        notas.setCoddm(notasDto.getCoddm());

        if (existe != null) {
            // Actualizar: delete + insert (PK compuesta)
            notasMapper.delete(new LambdaQueryWrapper<Notas>()
                .eq(Notas::getCodmat, notas.getCodmat())
                .eq(Notas::getCodi, notas.getCodi())
                .eq(Notas::getCoda, notas.getCoda())
                .eq(Notas::getCodp, notas.getCodp())
                .eq(Notas::getGestion, notas.getGestion())
                .eq(Notas::getIdusuario, notas.getIdusuario()));
            notasMapper.insert(notas);
        } else {
            // Insertar nuevo
            notasMapper.insert(notas);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delNota(String codmat, Integer codi, Integer coda, 
                        Integer codp, Integer gestion, Integer idusuario) {
        NotasDto existe = notasMapper.getNotaByKeys(
            codmat, codi, coda, codp, gestion, idusuario
        );
        
        if (existe == null) {
            throw new BadRequestException("La nota no existe");
        }

        LambdaQueryWrapper<Notas> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notas::getCodmat, codmat)
               .eq(Notas::getCodi, codi)
               .eq(Notas::getCoda, coda)
               .eq(Notas::getCodp, codp)
               .eq(Notas::getGestion, gestion)
               .eq(Notas::getIdusuario, idusuario);
        
        notasMapper.delete(wrapper);
    }

    @Override
    public List<NotasDto> getNotasEstudianteMateria(String codmat, 
                                                      Integer idusuario, 
                                                      Integer gestion) {
        return notasMapper.getNotasEstudianteMateria(codmat, idusuario, gestion);
    }

    @Override
    public List<NotasDto> getNotasParaleloItem(String codmat, Integer coda, 
                                                Integer codi, Integer gestion) {
        return notasMapper.getNotasParaleloItem(codmat, coda, codi, gestion);
    }

    @Override
    public Double calcularNotaFinal(String codmat, Integer idusuario, Integer gestion) {
        return notasMapper.calcularNotaFinal(codmat, idusuario, gestion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registrarNotasMasivas(List<NotasDto> notasList) {
        if (notasList == null || notasList.isEmpty()) {
            throw new BadRequestException("La lista de notas está vacía");
        }

        for (NotasDto notasDto : notasList) {
            editNota(notasDto);
        }
    }
}
