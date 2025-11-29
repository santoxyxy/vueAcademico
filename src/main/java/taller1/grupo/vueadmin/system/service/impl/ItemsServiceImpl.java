package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Items;
import taller1.grupo.vueadmin.system.entity.dto.ItemsDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.ItemsMapper;
import taller1.grupo.vueadmin.system.service.ItemsService;

import java.util.List;

/**
 * Implementación del servicio de Items
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class ItemsServiceImpl implements ItemsService {

    private final ItemsMapper itemsMapper;

    /**
     * Obtener lista de ítems con filtro
     */
    @Override
    public List<Items> getItemsList(String blurry) {
        LambdaQueryWrapper<Items> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Items::getNombre, blurry);
        }
        
        wrapper.orderByAsc(Items::getCodi);
        return itemsMapper.selectList(wrapper);
    }

    /**
     * Consulta paginada de ítems
     */
    @Override
    public IPage<ItemsDto> queryItemsTable(QueryDto queryDto) {
        Page<ItemsDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return itemsMapper.queryItemsTable(page, queryDto.getBlurry());
    }

    /**
     * Editar o crear ítem
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editItems(ItemsDto itemsDto) {
        // Validar que el nombre no esté vacío
        if (StringUtils.isBlank(itemsDto.getNombre())) {
            throw new BadRequestException("El nombre del ítem es obligatorio");
        }

        // Verificar duplicados
        checkItems(itemsDto);

        Items items = new Items();
        items.setCodi(itemsDto.getCodi());
        items.setNombre(itemsDto.getNombre());
        items.setEstado(itemsDto.getEstado() != null ? itemsDto.getEstado() : 1);

        if (items.getCodi() != null) {
            // Actualizar
            itemsMapper.updateById(items);
        } else {
            // Insertar
            itemsMapper.insert(items);
        }
    }

    /**
     * Eliminar ítem
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delItems(Integer codi) {
        Items items = itemsMapper.selectById(codi);
        if (items == null) {
            throw new BadRequestException("El ítem no existe");
        }

        // TODO: Verificar si tiene relaciones con ITEMAT antes de eliminar
        // Por ahora solo eliminamos
        itemsMapper.deleteById(codi);
    }

    /**
     * Verificar que no exista un ítem con el mismo nombre
     */
    private void checkItems(ItemsDto itemsDto) {
        LambdaQueryWrapper<Items> wrapper = new LambdaQueryWrapper<>();

        if (itemsDto.getCodi() != null) {
            wrapper.ne(Items::getCodi, itemsDto.getCodi());
        }

        if (StringUtils.isNotBlank(itemsDto.getNombre())) {
            wrapper.eq(Items::getNombre, itemsDto.getNombre());
        }

        long count = itemsMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("Ya existe un ítem con ese nombre");
        }
    }
}