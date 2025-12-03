import request from '@/utils/request'

/**
 * Obtener tabla de clases paginada
 */
export function queryPrograTable(params) {
  return request({
    url: '/sys/progra/table',
    method: 'get',
    params // { page, size, blurry, gestion, codmat, codn }
  })
}

/**
 * Obtener clases de un docente
 */
export function getClasesByDocente(ids, gestion) {
  return request({
    url: `/sys/progra/docente/${ids}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Obtener paralelos de una materia
 */
export function getParalelosByMateria(codmat, gestion) {
  return request({
    url: `/sys/progra/materia/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Crear o actualizar clase ofertada
 */
export function editProgra(data) {
  return request({
    url: '/sys/progra/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar clase ofertada
 */
export function delProgra(params) {
  return request({
    url: '/sys/progra/del',
    method: 'delete',
    params // { codpar, codp, codmat, gestion }
  })
}