import request from "../../utils/request";

export function queryUsuarioTable(params){
    return request({
        //url: '/api/sys/clientes/list',
        url: '/sys/usuario/table',
        method: 'get',
        params
    })
}

// cambiar estado del usuario 
export function estadoUsuario(data){
    return request({
        url: '/sys/usuario/estado/edit',
        method: 'post',
        data
    })
}

export function editUsuario(data){
// editar y adicionar 
    return request({
        url: '/sys/usuario/edit',
        method: 'post',
        data
    })
}



