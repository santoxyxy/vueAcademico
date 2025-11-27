package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import taller1.grupo.vueadmin.system.entity.Personal;

@Repository
public interface UsuarioMapper extends BaseMapper<Personal> {
/* idusuario integer NOT NULL,
    nombre character varying(100) COLLATE pg_catalog."default" NOT NULL,
    ap character varying(100) COLLATE pg_catalog."default",
    am character varying(100) COLLATE pg_catalog."default",
    estado smallint,
    fnac date,
    ecivil character varying(30) COLLATE pg_catalog."default",
    genero character varying(10) COLLATE pg_catalog."default",
    dir character varying(200) COLLATE pg_catalog."default",
    telf character varying(30) COLLATE pg_catalog."default",
    tipo character varying(30) COLLATE pg_catalog."default",
    foto character varying(255) COLLATE pg_catalog."default",*/
    @Select("select idusuario,nombre,ap,am,estado, to_date(fnac::text, 'YYYY-MM-DD') AS fnac,ecivil,genero,dir,telf,tipo,foto from academico.personal")
    /*@Select("select idusuario, cedula ,nombre,ap,am,estado, to_date(fnac::text, 'YYYY-MM-DD') AS fnac,ecivil,genero,dir,telf,tipo,foto \n" + //
                "from academico.personal p, academico.datos d\n" + //
                "where p.idusuario = d.cod")*/
    IPage<Personal> queryUsuarioTable(Page<?> page, @Param("blurry") String blurry);

   
}