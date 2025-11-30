package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para GENERAL
 * Incluye información descriptiva del usuario
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class GeneralDto {

    private Integer ids;
    private Integer gestion;

    // Información del usuario (JOINs)
    private String username;
    private String email;
    private String nickName;
    private Boolean enabled;
    
    // Información del personal (si existe)
    private String nombreCompleto;  // nombre + ap + am
    private String tipo;            // DOCENTE/ESTUDIANTE
    private String telf;
    
    // Estadísticas útiles
    private Integer totalMaterias;   // Materias en las que está inscrito/dicta
    private Integer totalNotas;      // Cantidad de notas registradas
}
