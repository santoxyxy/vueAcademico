import request from "../../utils/request";

/**
 * Obtener lista de registro
 * @param params
 * @returns {AxiosPromise}
 */
export function getClientesList(params){
    return request({
        url: '/sys/clientes/table',
        method: 'get',
        params
    })
}

/**
 * Eliminar registro de cliente
 * @param id
 * @returns {AxiosPromise}
 */
export function delCliente(id) {
    return request({
        url: '/sys/clientes/del',
        method: 'delete',
        params: { id }
    });
}

/**
 * Editar cliente
 * @param data
 * @returns {AxiosPromise}
 */
export function editCliente(data) {
    return request({
        url: '/sys/clientes/edit',
        method: 'post',
        data
    });
}