package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para NOTAS
 * Incluye información de JOINs para visualización completa
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class NotasDto {

    // PKs
    private String codmat;
    private Integer codi;
    private Integer coda;      // codpar
    private Integer codp;
    private Integer gestion;
    private Integer idusuario;

    // Datos principales
    private Integer nota;
    private Integer coddm;

    // Información descriptiva (JOINs)
    private String nombreMateria;      // materia.nombre
    private String nombreItem;         // items.nombre
    private String nombreParalelo;     // paralelo.nombre
    private String nombreEstudiante;   // personal.nombre + ap + am
    private String emailEstudiante;    // sys_user.email
    private String nombreModalidad;    // dmodalidad.nombre
    
    // Datos adicionales útiles
    private Integer ponderacion;       // itemat.ponderacion (%)
    private Integer codn;              // materia.codn
    private String nombreNivel;        // niveles.nombre
    
    // Para cálculos
    private Double notaPonderada;      // (nota * ponderacion) / 100
}
