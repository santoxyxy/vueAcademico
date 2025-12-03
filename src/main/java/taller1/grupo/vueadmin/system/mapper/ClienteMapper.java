
package taller1.grupo.vueadmin.system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import taller1.grupo.vueadmin.system.entity.dto.Clientes2Dto;
import taller1.grupo.vueadmin.system.entity.dto.ClientesDto;

@Mapper
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);  // Instancia del Mapper

    // Método que convierte ClientesDto en Clientes2Dto
    Clientes2Dto clientesDtoToClientes2Dto(ClientesDto clientesDto);
}

