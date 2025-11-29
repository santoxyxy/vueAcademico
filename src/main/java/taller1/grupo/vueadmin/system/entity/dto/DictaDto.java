package taller1.grupo.vueadmin.system.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para transferencia de datos de DICTA
 * Incluye información completa para mostrar en UI
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
public class DictaDto {

    // PKs
    private Integer codpar;
    private Integer codp;
    private String codmat;
    private Integer gestion;

    // FK Docente
    private Integer ids;

    // Datos descriptivos (JOINs)
    private String nombreMateria;     // materia.nombre
    private String nombreParalelo;    // paralelo.nombre
    private String nombreDocente;     // personal.nombre + ap + am
    private String emailDocente;      // sys_user.email
    private String tipoDocente;       // personal.tipo
    
    // Datos adicionales útiles
    private Integer codn;             // materia.codn
    private String nombreNivel;       // niveles.nombre
}
