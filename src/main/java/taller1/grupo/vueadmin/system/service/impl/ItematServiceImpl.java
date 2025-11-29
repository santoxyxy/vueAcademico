package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Itemat;
import taller1.grupo.vueadmin.system.entity.dto.ItematDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.ItematMapper;
import taller1.grupo.vueadmin.system.service.ItematService;

import java.util.List;

/**
 * Implementación del servicio ITEMAT
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class ItematServiceImpl implements ItematService {

    private final ItematMapper itematMapper;

    @Override
    public List<Itemat> getItematList(Integer gestion, String codmat) {
        LambdaQueryWrapper<Itemat> wrapper = new LambdaQueryWrapper<>();
        
        if (gestion != null) {
            wrapper.eq(Itemat::getGestion, gestion);
        }
        if (StringUtils.isNotBlank(codmat)) {
            wrapper.eq(Itemat::getCodmat, codmat);
        }
        
        wrapper.orderByAsc(Itemat::getCodmat, Itemat::getCodi);
        return itematMapper.selectList(wrapper);
    }

    @Override
    public IPage<ItematDto> queryItematTable(QueryDto queryDto, Integer gestion, String codmat) {
        Page<ItematDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return itematMapper.queryItematTable(page, queryDto.getBlurry(), gestion, codmat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editItemat(ItematDto itematDto) {
        // Validaciones básicas
        if (StringUtils.isBlank(itematDto.getCodmat())) {
            throw new BadRequestException("Debe seleccionar una materia");
        }
        if (itematDto.getCodi() == null) {
            throw new BadRequestException("Debe seleccionar un ítem de evaluación");
        }
        if (itematDto.getGestion() == null) {
            throw new BadRequestException("Debe especificar la gestión académica");
        }
        if (itematDto.getPonderacion() == null || itematDto.getPonderacion() < 0 || itematDto.getPonderacion() > 100) {
            throw new BadRequestException("La ponderación debe estar entre 0 y 100");
        }

        // Validar que la suma de ponderaciones no exceda 100%
        ItematDto existe = itematMapper.getItematByKeys(
            itematDto.getCodmat(),
            itematDto.getCodi(),
            itematDto.getGestion()
        );

        Integer sumaActual;
        if (existe != null) {
            // Es edición: excluir el ítem actual del cálculo
            sumaActual = itematMapper.getSumPonderacionesExcluding(
                itematDto.getCodmat(),
                itematDto.getGestion(),
                itematDto.getCodi()
            );
        } else {
            // Es nuevo: calcular suma total actual
            sumaActual = itematMapper.getSumPonderaciones(
                itematDto.getCodmat(),
                itematDto.getGestion()
            );
        }

        Integer nuevaSuma = sumaActual + itematDto.getPonderacion();
        if (nuevaSuma > 100) {
            throw new BadRequestException(
                String.format("La suma de ponderaciones excedería el 100%% (actual: %d%%, nueva: %d%%)", 
                    sumaActual, nuevaSuma)
            );
        }

        // Crear entidad
        Itemat itemat = new Itemat();
        itemat.setCodmat(itematDto.getCodmat());
        itemat.setCodi(itematDto.getCodi());
        itemat.setGestion(itematDto.getGestion());
        itemat.setPonderacion(itematDto.getPonderacion());
        itemat.setEstado(itematDto.getEstado() != null ? itematDto.getEstado() : 1);

        if (existe != null) {
            // Actualizar: delete + insert (MyBatis-Plus con PK compuesta)
            itematMapper.delete(new LambdaQueryWrapper<Itemat>()
                .eq(Itemat::getCodmat, itemat.getCodmat())
                .eq(Itemat::getCodi, itemat.getCodi())
                .eq(Itemat::getGestion, itemat.getGestion()));
            itematMapper.insert(itemat);
        } else {
            // Insertar nuevo
            itematMapper.insert(itemat);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delItemat(String codmat, Integer codi, Integer gestion) {
        ItematDto existe = itematMapper.getItematByKeys(codmat, codi, gestion);
        if (existe == null) {
            throw new BadRequestException("La configuración no existe");
        }

        // TODO: Verificar si tiene relaciones en NOTAS antes de eliminar
        
        LambdaQueryWrapper<Itemat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Itemat::getCodmat, codmat)
               .eq(Itemat::getCodi, codi)
               .eq(Itemat::getGestion, gestion);
        
        itematMapper.delete(wrapper);
    }

    @Override
    public List<ItematDto> getItemsByMateria(String codmat, Integer gestion) {
        return itematMapper.getItemsByMateria(codmat, gestion);
    }

    @Override
    public boolean validatePonderaciones(String codmat, Integer gestion) {
        Integer suma = itematMapper.getSumPonderaciones(codmat, gestion);
        return suma != null && suma == 100;
    }
}
