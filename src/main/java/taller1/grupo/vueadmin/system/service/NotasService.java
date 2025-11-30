package taller1.grupo.vueadmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import taller1.grupo.vueadmin.system.entity.dto.NotasDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

import java.util.List;

/**
 * Servicio de NOTAS
 * @author Tu nombre
 * @date 2025-01-01
 */
public interface NotasService {

    /**
     * Consulta paginada con filtros
     */
    IPage<NotasDto> queryNotasTable(QueryDto queryDto, 
                                     Integer gestion, 
                                     String codmat,
                                     Integer idusuario);

    /**
     * Registrar o actualizar nota
     */
    void editNota(NotasDto notasDto);

    /**
     * Eliminar nota
     */
    void delNota(String codmat, Integer codi, Integer coda, 
                 Integer codp, Integer gestion, Integer idusuario);

    /**
     * Obtener notas de un estudiante en una materia
     */
    List<NotasDto> getNotasEstudianteMateria(String codmat, 
                                              Integer idusuario, 
                                              Integer gestion);

    /**
     * Obtener notas de un paralelo para un ítem
     */
    List<NotasDto> getNotasParaleloItem(String codmat, Integer coda, 
                                         Integer codi, Integer gestion);

    /**
     * Calcular nota final de un estudiante
     */
    Double calcularNotaFinal(String codmat, Integer idusuario, Integer gestion);

    /**
     * Registrar notas masivas para un paralelo
     */
    void registrarNotasMasivas(List<NotasDto> notasList);
}
