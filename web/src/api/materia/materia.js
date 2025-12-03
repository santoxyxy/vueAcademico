import request from "@/utils/request";

/**
 * Obtener lista de materias (sin paginación)
 * @param {string} blurry - Texto de búsqueda
 */
export function getMateriaList(blurry) {
  return request({
    url: '/sys/materia/list',
    method: 'get',
    params: { blurry }
  });
}

/**
 * Consultar tabla de materias con paginación
 * @param {object} params - { blurry, size, currentPage, estado }
 */
export function queryMateriaTable(params) {
  return request({
    url: '/sys/materia/table',
    method: 'get',
    params
  });
}

/**
 * Obtener materia por código
 * @param {string} codmat - Código de la materia
 */
export function getMateria(codmat) {
  return request({
    url: `/sys/materia/${codmat}`,
    method: 'get'
  });
}

/**
 * Crear o editar materia
 * @param {object} data - MateriaDto
 */
export function editMateria(data) {
  return request({
    url: '/sys/materia/edit',
    method: 'post',
    data
  });
}

/**
 * Eliminar materia (lógico)
 * @param {string} codmat - Código de la materia
 */
export function delMateria(codmat) {
  return request({
    url: '/sys/materia/del',
    method: 'delete',
    params: { codmat }
  });
}