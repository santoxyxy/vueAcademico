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
 * Entidad Modalidad - Catálogo de modalidades académicas
 * Ejemplo: PRESENCIAL, VIRTUAL, SEMIPRESENCIAL
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "modalidad", schema = "academico")
public class Modalidad extends BaseEntity {

    @TableId(value = "codm", type = IdType.AUTO)
    private Integer codm;

    @TableField("nombre")
    private String nombre;

    @TableField("estado")
    private Integer estado;
}
