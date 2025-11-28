package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferencia de datos de Paralelos
 * Usado en formularios y respuestas de API
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class ParaleloDto {

    private Integer codp;

    private String nombre;

    private Integer estado;
}