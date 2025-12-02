import request from '@/utils/request'

/**
 * Obtener tabla de configuraciones con filtros
 * @param {Object} params - { page, size, gestion, codmat }
 */
export function queryItematTable(params) {
  return request({
    url: '/sys/itemat/table',
    method: 'get',
    params
  })
}

/**
 * Obtener items configurados para una materia
 * @param {String} codmat - Código de materia
 * @param {Number} gestion - Año de gestión
 */
export function getItemsByMateria(codmat, gestion) {
  return request({
    url: `/sys/itemat/items/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Validar que las ponderaciones sumen 100%
 * @param {String} codmat - Código de materia
 * @param {Number} gestion - Año de gestión
 */
export function validatePonderaciones(codmat, gestion) {
  return request({
    url: `/sys/itemat/validate/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Guardar configuración de ponderación
 * @param {Object} data - { codmat, codi, gestion, ponderacion, estado }
 */
export function editItemat(data) {
  return request({
    url: '/sys/itemat/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar configuración
 * @param {String} codmat - Código de materia
 * @param {Number} codi - Código de ítem
 * @param {Number} gestion - Año de gestión
 */
export function delItemat(codmat, codi, gestion) {
  return request({
    url: '/sys/itemat/del',
    method: 'delete',
    params: { codmat, codi, gestion }
  })
}