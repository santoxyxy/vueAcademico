import request from '@/utils/request'

/**
 * Obtener lista simple de paralelos (para selects)
 */
export function getParaleloList(params) {
  return request({
    url: '/sys/paralelo/list',
    method: 'get',
    params
  })
}

/**
 * Obtener tabla paginada de paralelos
 */
export function queryParaleloTable(params) {
  return request({
    url: '/sys/paralelo/table',
    method: 'get',
    params
  })
}

/**
 * Crear o editar paralelo
 */
export function editParalelo(data) {
  return request({
    url: '/sys/paralelo/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar paralelo
 */
export function delParalelo(id) {
  return request({
    url: '/sys/paralelo/del',
    method: 'delete',
    params: { id }
  })
}