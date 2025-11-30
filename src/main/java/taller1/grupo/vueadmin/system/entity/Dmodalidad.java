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
 * Entidad DMODALIDAD - Detalle de modalidades (ordinaria/extraordinaria)
 * Asociado a MODALIDAD
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "dmodalidad", schema = "academico")
public class Dmodalidad extends BaseEntity {

    @TableId(value = "coddm", type = IdType.AUTO)
    private Integer coddm;

    @TableField("nombre")
    private String nombre;

    @TableField("estado")
    private Integer estado;

    @TableField("codm")
    private Integer codm;  // FK a modalidad
}