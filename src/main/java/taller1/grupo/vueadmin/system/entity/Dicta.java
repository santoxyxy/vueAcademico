package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taller1.grupo.vueadmin.common.utils.BaseEntity;
import java.io.Serializable;

/**
 * Entidad DICTA - Asignación de docentes a clases
 * Define quién dicta cada materia-paralelo en una gestión
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "dicta", schema = "academico")
public class Dicta extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("codpar")
    private Integer codpar;   // PK1: Código paralelo (sección)

    @TableId("codp")
    private Integer codp;     // PK2: Código periodo (siempre 1 en este diseño)

    @TableId("codmat")
    private String codmat;    // PK3: Código materia

    @TableId("gestion")
    private Integer gestion;  // PK4: Año académico

    @TableField("ids")
    private Integer ids;      // FK: ID del docente (sys_user.id)
}
