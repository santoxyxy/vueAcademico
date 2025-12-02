import request from '@/utils/request'

/**
 * Obtener tabla de asignaciones paginada
 */
export function queryDictaTable(params) {
  return request({
    url: '/api/sys/dicta/table',
    method: 'get',
    params // { page, size, blurry, gestion, codmat, codn }
  })
}

/**
 * Obtener materias que dicta un docente
 */
export function getMateriasByDocente(ids, gestion) {
  return request({
    url: `/api/sys/dicta/docente/${ids}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Obtener docentes de una materia
 */
export function getDocentesByMateria(codmat, gestion) {
  return request({
    url: `/api/sys/dicta/materia/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Crear o actualizar asignación
 */
export function editDicta(data) {
  return request({
    url: '/api/sys/dicta/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar asignación
 */
export function delDicta(params) {
  return request({
    url: '/api/sys/dicta/del',
    method: 'delete',
    params // { codpar, codp, codmat, gestion }
  })
}