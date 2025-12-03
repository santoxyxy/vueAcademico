import request from '@/utils/request'

/**
 * Obtener lista simple (sin paginación)
 */
export function getDmodalidadList(blurry) {
  return request({
    url: '/sys/dmodalidad/list',
    method: 'get',
    params: { blurry }
  })
}

/**
 * Obtener tabla paginada
 */
export function queryDmodalidadTable(params) {
  return request({
    url: '/sys/dmodalidad/table',
    method: 'get',
    params // { page, size, blurry }
  })
}

/**
 * Obtener detalles por modalidad padre
 */
export function getDetallesByModalidad(codm) {
  return request({
    url: `/sys/dmodalidad/modalidad/${codm}`,
    method: 'get'
  })
}

/**
 * Crear o actualizar detalle
 */
export function editDmodalidad(data) {
  return request({
    url: '/sys/dmodalidad/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar detalle
 */
export function delDmodalidad(id) {
  return request({
    url: '/sys/dmodalidad/del',
    method: 'delete',
    params: { id }
  })
}