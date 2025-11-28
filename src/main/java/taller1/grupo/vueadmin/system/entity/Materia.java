package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taller1.grupo.vueadmin.common.utils.BaseEntity;

/**
 * Entidad Materia - Representa las asignaturas del sistema
 * Relacionada con Niveles (codn) y Paralelos (a través de MAPA)
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "materia", schema = "academico")
public class Materia extends BaseEntity {

    @TableId(value = "codmat", type = IdType.INPUT)
    private String codmat;

    @TableField("nombre")
    private String nombre;

    @TableField("codn")
    private Integer codn;

    @TableField("estado")
    private Integer estado;
}