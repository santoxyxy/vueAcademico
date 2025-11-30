package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taller1.grupo.vueadmin.common.utils.BaseEntity;
import java.io.Serializable;

/**
 * Entidad PROGRA - Clases ofertadas
 * Define qué materias-paralelos se dictan en una gestión específica
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "progra", schema = "academico")
public class Progra extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("codpar")
    private Integer codpar;   // PK1: Código paralelo (sección)

    @TableField("codp")
    private Integer codp;     // PK2: Código periodo (siempre 1 en este diseño)

    @TableField("codmat")
    private String codmat;    // PK3: Código materia

    @TableField("gestion")
    private Integer gestion;  // PK4: Año académico

    @TableField("ids")
    private Integer ids;      // FK: ID del docente responsable (sys_user.id)
}
