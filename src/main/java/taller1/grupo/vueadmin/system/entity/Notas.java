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
 * Entidad NOTAS - Sistema de calificaciones
 * PK compuesta: (codmat, codi, codp, gestion, idusuario)
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "notas", schema = "academico")
public class Notas extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("codmat")
    private String codmat;    // PK1: Código materia

    @TableField("codi")
    private Integer codi;     // PK2: Código ítem (Parcial1, Proyecto, etc.)

    @TableField("coda")
    private Integer coda;     // PK3: Código paralelo (codpar)

    @TableField("codp")
    private Integer codp;     // PK4: Código periodo (siempre 1)

    @TableField("gestion")
    private Integer gestion;  // PK5: Año académico

    @TableField("idusuario")
    private Integer idusuario; // PK6: ID del estudiante

    @TableField("nota")
    private Integer nota;     // Calificación (0-100)

    @TableField("coddm")
    private Integer coddm;    // FK: Detalle modalidad (ordinaria/extraordinaria)
}