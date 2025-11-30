package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taller1.grupo.vueadmin.common.utils.BaseEntity;

/**
 * Entidad GENERAL - Metadatos por usuario y gestión
 * Almacena información general del usuario en cada gestión académica
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "general", schema = "academico")
public class General extends BaseEntity {

    @TableField("ids")
    private Integer ids;  // PK: ID del usuario (sys_user.id)

    @TableField("gestion")
    private Integer gestion;  // Año académico
}
