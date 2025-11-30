package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para DMODALIDAD
 * Incluye nombre de la modalidad padre para visualización
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class DmodalidadDto {

    private Integer coddm;
    private String nombre;
    private Integer estado;
    private Integer codm;
    
    // Campo adicional del JOIN
    private String nombreModalidad;  // modalidad.nombre
}
