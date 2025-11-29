package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferencia de datos de ITEMAT
 * Incluye nombres descriptivos para mostrar en UI
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class ItematDto {

    private String codmat;
    private String nombreMateria;  // JOIN con materia.nombre

    private Integer codi;
    private String nombreItem;     // JOIN con items.nombre

    private Integer gestion;
    private Integer ponderacion;   // Porcentaje (0-100)
    private Integer estado;

    // Campos adicionales útiles para validación
    private Integer codn;          // Nivel de la materia
    private String nombreNivel;    // Nombre del nivel
}
