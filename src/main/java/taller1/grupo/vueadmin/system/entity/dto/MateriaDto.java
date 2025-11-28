package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferencia de datos de Materias
 * Incluye nombre del nivel para visualización
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class MateriaDto {

    private String codmat;

    private String nombre;

    private Integer codn;

    private String nombreNivel;  // para mostrar el nombre del nivel en lugar del código

    private Integer estado;
}