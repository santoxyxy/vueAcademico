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
 * Entidad Niveles - Representa los niveles académicos (1er año, 2do año, etc.)
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "niveles", schema = "academico")
public class Niveles extends BaseEntity {

    @TableId(value = "codn", type = IdType.AUTO)
    private Integer codn;

    @TableField("nombre")
    private String nombre;

    @TableField("estado")
    private Integer estado;
}