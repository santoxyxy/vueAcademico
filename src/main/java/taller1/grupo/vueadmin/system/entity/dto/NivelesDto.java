package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferencia de datos de Niveles
 * Usado en formularios y respuestas de API
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class NivelesDto {

    private Integer codn;

    private String nombre;

    private Integer estado;
}