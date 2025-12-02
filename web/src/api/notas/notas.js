import request from '@/utils/request'
// Si no tienes alias @, cambia a: import request from '../../utils/request'

/**
 * Obtener tabla de notas con filtros
 */
export function queryNotasTable(params) {
  return request({
    url: '/sys/notas/table',
    method: 'get',
    params // { page, size, gestion, codmat, idusuario }
  })
}

/**
 * Obtener notas de un estudiante en una materia
 */
export function getNotasEstudianteMateria(idusuario, codmat, gestion) {
  return request({
    url: `/sys/notas/estudiante/${idusuario}/materia/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Obtener notas de un paralelo para un ítem específico
 */
export function getNotasParaleloItem(coda, codi, codmat, gestion) {
  return request({
    url: `/sys/notas/paralelo/${coda}/item/${codi}`,
    method: 'get',
    params: { codmat, gestion }
  })
}

/**
 * Calcular nota final de un estudiante
 */
export function calcularNotaFinal(idusuario, codmat, gestion) {
  return request({
    url: `/sys/notas/final/estudiante/${idusuario}/materia/${codmat}`,
    method: 'get',
    params: { gestion }
  })
}

/**
 * Registrar o actualizar una nota
 */
export function editNota(data) {
  return request({
    url: '/sys/notas/edit',
    method: 'post',
    data
  })
}

/**
 * Registrar notas masivas (lista de notas)
 */
export function registrarNotasMasivas(notasList) {
  return request({
    url: '/sys/notas/masivas',
    method: 'post',
    data: notasList
  })
}

/**
 * Eliminar nota
 */
export function delNota(codmat, codi, coda, codp, gestion, idusuario) {
  return request({
    url: '/sys/notas/del',
    method: 'delete',
    params: { codmat, codi, coda, codp, gestion, idusuario }
  })
}