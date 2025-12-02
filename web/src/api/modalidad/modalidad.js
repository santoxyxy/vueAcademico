import request from '@/utils/request'

/**
 * Obtener lista simple de modalidades
 */
export function getModalidadList(blurry) {
  return request({
    url: '/api/sys/modalidad/list',
    method: 'get',
    params: { blurry }
  })
}

/**
 * Obtener tabla paginada
 */
export function queryModalidadTable(params) {
  return request({
    url: '/api/sys/modalidad/table',
    method: 'get',
    params // { page, size, blurry }
  })
}

/**
 * Crear o actualizar modalidad
 */
export function editModalidad(data) {
  return request({
    url: '/api/sys/modalidad/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar modalidad
 */
export function delModalidad(id) {
  return request({
    url: '/api/sys/modalidad/del',
    method: 'delete',
    params: { id }
  })
}