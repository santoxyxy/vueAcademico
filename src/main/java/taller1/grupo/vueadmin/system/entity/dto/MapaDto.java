package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferencia de datos de MAPA
 * Incluye nombres descriptivos para mostrar en UI
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class MapaDto {

    private String codmat;
    private String nombreMateria;  // JOIN con materia.nombre

    private Integer codpar;
    private String nombreParalelo; // JOIN con paralelo.nombre

    private Integer gestion;
    private Integer estado;
    
    // Campos adicionales útiles para UI
    private Integer codn;          // Nivel de la materia
    private String nombreNivel;    // Nombre del nivel
}
