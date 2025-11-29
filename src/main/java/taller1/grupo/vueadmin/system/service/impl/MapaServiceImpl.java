package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.Mapa;
import taller1.grupo.vueadmin.system.entity.dto.MapaDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.mapper.MapaMapper;
import taller1.grupo.vueadmin.system.service.MapaService;

import java.util.List;

/**
 * Implementación del servicio MAPA
 * @author Tu nombre
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class MapaServiceImpl implements MapaService {

    private final MapaMapper mapaMapper;

    @Override
    public List<Mapa> getMapaList(Integer gestion) {
        LambdaQueryWrapper<Mapa> wrapper = new LambdaQueryWrapper<>();
        
        if (gestion != null) {
            wrapper.eq(Mapa::getGestion, gestion);
        }
        
        wrapper.orderByAsc(Mapa::getCodmat, Mapa::getCodpar);
        return mapaMapper.selectList(wrapper);
    }

    @Override
    public IPage<MapaDto> queryMapaTable(QueryDto queryDto, Integer gestion) {
        Page<MapaDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return mapaMapper.queryMapaTable(page, queryDto.getBlurry(), gestion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editMapa(MapaDto mapaDto) {
        // Validaciones
        if (mapaDto.getCodmat() == null || mapaDto.getCodmat().trim().isEmpty()) {
            throw new BadRequestException("Debe seleccionar una materia");
        }
        if (mapaDto.getCodpar() == null) {
            throw new BadRequestException("Debe seleccionar un paralelo");
        }
        if (mapaDto.getGestion() == null) {
            throw new BadRequestException("Debe especificar la gestión académica");
        }

        // Verificar si ya existe
        MapaDto existe = mapaMapper.getMapaByKeys(
            mapaDto.getCodmat(), 
            mapaDto.getCodpar(), 
            mapaDto.getGestion()
        );

        Mapa mapa = new Mapa();
        mapa.setCodmat(mapaDto.getCodmat());
        mapa.setCodpar(mapaDto.getCodpar());
        mapa.setGestion(mapaDto.getGestion());
        mapa.setEstado(mapaDto.getEstado() != null ? mapaDto.getEstado() : 1);

        if (existe != null) {
            // Actualizar (MyBatis-Plus no soporta PK compuesta para update)
            // Usamos delete + insert
            mapaMapper.delete(new LambdaQueryWrapper<Mapa>()
                .eq(Mapa::getCodmat, mapa.getCodmat())
                .eq(Mapa::getCodpar, mapa.getCodpar())
                .eq(Mapa::getGestion, mapa.getGestion()));
            mapaMapper.insert(mapa);
        } else {
            // Insertar nuevo
            mapaMapper.insert(mapa);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMapa(String codmat, Integer codpar, Integer gestion) {
        MapaDto existe = mapaMapper.getMapaByKeys(codmat, codpar, gestion);
        if (existe == null) {
            throw new BadRequestException("La asignación no existe");
        }

        // TODO: Verificar si tiene relaciones en PROGRA o DICTA antes de eliminar
        
        LambdaQueryWrapper<Mapa> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mapa::getCodmat, codmat)
               .eq(Mapa::getCodpar, codpar)
               .eq(Mapa::getGestion, gestion);
        
        mapaMapper.delete(wrapper);
    }

    @Override
    public List<MapaDto> getParalelosByMateria(String codmat, Integer gestion) {
        return mapaMapper.getParalelosByMateria(codmat, gestion);
    }
}
