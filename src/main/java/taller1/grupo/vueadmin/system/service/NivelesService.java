package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.Niveles;
import taller1.grupo.vueadmin.system.entity.dto.NivelesDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de Niveles
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface NivelesService {

    /**
     * Obtener lista de niveles con filtro
     * @param blurry Texto de búsqueda
     * @return Lista de niveles
     */
    List<Niveles> getNivelesList(String blurry);

    /**
     * Consulta paginada de niveles
     * @param queryDto Objeto con parámetros de paginación y filtro
     * @return Página con resultados
     */
    IPage<NivelesDto> queryNivelesTable(QueryDto queryDto);

    /**
     * Editar o crear nivel
     * @param nivelesDto Datos del nivel
     */
    void editNiveles(NivelesDto nivelesDto);

    /**
     * Eliminar nivel
     * @param codn ID del nivel
     */
    void delNiveles(Integer codn);
}
