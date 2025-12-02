import request from '@/utils/request'

/**
 * Obtener lista simple de items (para selects)
 */
export function getItemsList(params) {
  return request({
    url: '/sys/items/list',
    method: 'get',
    params: { blurry: params }
  })
}

/**
 * Obtener tabla paginada de items
 */
export function queryItemsTable(params) {
  return request({
    url: '/sys/items/table',
    method: 'get',
    params
  })
}

/**
 * Crear o editar item
 */
export function editItems(data) {
  return request({
    url: '/sys/items/edit',
    method: 'post',
    data
  })
}

/**
 * Eliminar item
 */
export function delItems(id) {
  return request({
    url: '/sys/items/del',
    method: 'delete',
    params: { id }
  })
}