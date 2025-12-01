import request from '@/utils/request'

/**
 * API de GENERAL
 * Metadatos por usuario y gestión
 */

// Obtener lista filtrada por gestión
export function getGeneralList(gestion) {
  return request({
    url: '/sys/general/list',
    method: 'get',
    params: { gestion }
  })
}

// Consulta paginada
export function queryGeneralTable(params) {
  return request({
    url: '/sys/general/table',
    method: 'get',
    params
  })
}

// Obtener info de usuario en gestión específica
export function getGeneralByUserAndGestion(ids, gestion) {
  return request({
    url: `/sys/general/usuario/${ids}/gestion/${gestion}`,
    method: 'get'
  })
}

// Obtener gestiones de un usuario
export function getGestionesByUser(ids) {
  return request({
    url: `/sys/general/usuario/${ids}/gestiones`,
    method: 'get'
  })
}

// Registrar usuario en gestión
export function registrarEnGestion(data) {
  return request({
    url: '/sys/general/registrar',
    method: 'post',
    data
  })
}

// Editar/crear registro
export function editGeneral(data) {
  return request({
    url: '/sys/general/edit',
    method: 'post',
    data
  })
}

// Eliminar registro
export function delGeneral(ids, gestion) {
  return request({
    url: '/sys/general/del',
    method: 'delete',
    params: { ids, gestion }
  })
}