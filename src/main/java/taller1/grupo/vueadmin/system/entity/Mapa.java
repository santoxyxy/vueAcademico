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
 * Entidad Mapa - Oferta de materias por paralelo y gestión
 * Representa qué paralelos se ofertan para cada materia en una gestión específica
 * @author Tu nombre
 * @date 2025-01-01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "mapa", schema = "academico")
public class Mapa extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("codmat")
    private String codmat;  // PK1: Código de materia

    @TableField("codpar")
    private Integer codpar; // PK2: Código de paralelo

    @TableField("gestion")
    private Integer gestion; // PK3: Año académico

    @TableField("estado")
    private Integer estado;  // 1=activo, 0=inactivo
}
