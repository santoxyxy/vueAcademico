import request from "@/utils/request";

/**
 * Obtener tabla de mapas con paginación
 * @param params
 * @returns {AxiosPromise}
 */
export function getMapaTable(params) {
    return request({
        url: '/sys/mapa/table',
        method: 'get',
        params
    });
}

/**
 * Obtener paralelos asignados a una materia
 * @param codmat Código de materia
 * @param gestion Año académico
 * @returns {AxiosPromise}
 */
export function getParalelosByMateria(codmat, gestion) {
    return request({
        url: `/sys/mapa/paralelos/${codmat}`,
        method: 'get',
        params: { gestion }
    });
}

/**
 * Crear o actualizar asignación materia-paralelo
 * @param data
 * @returns {AxiosPromise}
 */
export function editMapa(data) {
    return request({
        url: '/sys/mapa/edit',
        method: 'post',
        data
    });
}

/**
 * Eliminar asignación materia-paralelo
 * @param codmat
 * @param codpar
 * @param gestion
 * @returns {AxiosPromise}
 */
export function delMapa(codmat, codpar, gestion) {
    return request({
        url: '/sys/mapa/del',
        method: 'delete',
        params: { codmat, codpar, gestion }
    });
}