import request from "../../utils/request";

/**
 * Obtener lista de registro
 * @param params
 * @returns {AxiosPromise}
 */
export function getClientesList(params){
    return request({
        //url: '/api/sys/clientes/list',
        url: '/sys/clientes/table',
        method: 'get',
        params
    })
}

/**
 * Eliminar registro de cliente
 * @param data
 * @returns {AxiosPromise}
 */
export function delCliente(data) {
    return request({
        url: '/sys/clientes/delete', // <--- Asegúrate de que esta URL sea la correcta para tu backend
        method: 'post', // Método común para eliminación
        data
    });
}