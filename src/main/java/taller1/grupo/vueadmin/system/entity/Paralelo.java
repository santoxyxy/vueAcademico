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
 * Entidad Paralelo - Representa las secciones (A, B, C) de las materias
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "paralelo", schema = "academico")
public class Paralelo extends BaseEntity {

    @TableId(value = "codp", type = IdType.AUTO)
    private Integer codp;

    @TableField("nombre")
    private String nombre;

    @TableField("estado")
    private Integer estado;
}