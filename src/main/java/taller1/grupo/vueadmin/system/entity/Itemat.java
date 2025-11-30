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
 * Entidad ITEMAT - Ponderaciones de ítems por materia y gestión
 * Define qué porcentaje vale cada ítem (Parcial1, Proyecto, etc.) en cada materia
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "itemat", schema = "academico")
public class Itemat extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("codmat")
    private String codmat;    // PK1: Código de materia

    @TableField("codi")
    private Integer codi;     // PK2: Código de ítem

    @TableField("gestion")
    private Integer gestion;  // PK3: Año académico

    @TableField("ponderacion")
    private Integer ponderacion; // Porcentaje que vale (0-100)

    @TableField("estado")
    private Integer estado;   // 1=activo, 0=inactivo
}
