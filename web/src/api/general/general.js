import request from "@/utils/request";

/**
 * Obtener lista de registros generales
 * @param {number} gestion - Año académico (opcional)
 */
export function getGeneralList(gestion) {
  return request({
    url: '/sys/general/list',
    method: 'get',
    params: { gestion }
  });
}

/**
 * Consultar tabla general con paginación
 * @param {object} params - { blurry, size, currentPage }
 * @param {number} gestion - Año académico (opcional)
 */
export function queryGeneralTable(params, gestion) {
  return request({
    url: '/sys/general/table',
    method: 'get',
    params: { ...params, gestion }
  });
}

/**
 * Obtener información de usuario en una gestión
 * @param {number} ids - ID del usuario
 * @param {number} gestion - Año académico
 */
export function getGeneralByUserAndGestion(ids, gestion) {
  return request({
    url: `/sys/general/usuario/${ids}/gestion/${gestion}`,
    method: 'get'
  });
}

/**
 * Obtener gestiones de un usuario
 * @param {number} ids - ID del usuario
 */
export function getGestionesByUser(ids) {
  return request({
    url: `/sys/general/usuario/${ids}/gestiones`,
    method: 'get'
  });
}

/**
 * Registrar usuario en gestión
 * @param {object} data - { ids, gestion }
 */
export function registrarEnGestion(data) {
  return request({
    url: '/sys/general/registrar',
    method: 'post',
    data
  });
}

/**
 * Editar o crear registro general
 * @param {object} data - GeneralDto
 */
export function editGeneral(data) {
  return request({
    url: '/sys/general/edit',
    method: 'post',
    data
  });
}

/**
 * Eliminar registro general
 * @param {number} ids - ID del usuario
 * @param {number} gestion - Año académico (opcional)
 */
export function delGeneral(ids, gestion) {
  return request({
    url: '/sys/general/del',
    method: 'delete',
    params: { ids, gestion }
  });
}