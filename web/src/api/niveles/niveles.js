import request from '@/utils/request'
// Si no tienes alias @, cambia a: import request from '../../utils/request'

/**
 * Obtener lista simple de niveles (para selects)
 */
export function getNivelesList(blurry) {
  return request({
    url: '/sys/niveles/list',
    method: 'get',
    params: { blurry }
  })
}

/**
 * Obtener tabla paginada de niveles
 */
export function queryNivelesTable(params) {
  return request({
    url: '/sys/niveles/table',
    method: 'get',
    params
  })
}

/**
 * Crear o editar nivel
 */
export function editNiveles(data) {
  return request({
    url: '/sys/niveles/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar nivel
 */
export function delNiveles(id) {
  return request({
    url: '/sys/niveles/del',
    method: 'delete',
    params: { id }
  })
}