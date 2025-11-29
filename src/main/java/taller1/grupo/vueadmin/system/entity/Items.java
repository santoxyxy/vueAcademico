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
 * Entidad Items - Representa los ítems de evaluación (Parcial, Proyecto, Final, etc.)
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "items", schema = "academico")
public class Items extends BaseEntity {

    @TableId(value = "codi", type = IdType.AUTO)
    private Integer codi;

    @TableField("nombre")
    private String nombre;

    @TableField("estado")
    private Integer estado;
}
