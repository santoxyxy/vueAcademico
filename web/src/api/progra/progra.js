import request from '@/utils/request'

/**
 * API de PROGRA - Clases ofertadas
 */

// Consultar tabla de clases con paginación
export function getPrograTable(params) {
  return request({
    url: '/sys/progra/table',
    method: 'get',
    params: params
  })
}

// Obtener clases que dicta un docente
export function getClasesByDocente(ids, gestion) {
  return request({
    url: `/sys/progra/docente/${ids}`,
    method: 'get',
    params: { gestion }
  })
}

// Obtener paralelos ofertados de una materia
export function getParalelosByMateria(codmat, gestion) {
  return request({
    url: `/sys/progra/materia/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

// Crear o actualizar clase
export function editProgra(data) {
  return request({
    url: '/sys/progra/edit',
    method: 'post',
    data: data
  })
}

// Eliminar clase
export function delProgra(params) {
  return request({
    url: '/sys/progra/del',
    method: 'delete',
    params: params
  })
}