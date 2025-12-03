import request from '@/utils/request'

export function pagePersonas(params) {
  return request({
    url: '/sys/personal/table', // ✅ CORREGIDO: removido /api
    method: 'get',
    params
  })
}

export function getPersona(idusuario) {
  return request({ 
    url: `/sys/personal/${idusuario}`, // ✅ CORREGIDO
    method: 'get' 
  })
}

export function createPersona(data) {
  return request({ 
    url: '/sys/personal', // ✅ CORREGIDO
    method: 'post', 
    data 
  })
}

export function updatePersona(idusuario, data) {
  return request({ 
    url: `/sys/personal/${idusuario}`, // ✅ CORREGIDO
    method: 'put', 
    data 
  })
}

export function deletePersona(idusuario) {
  return request({ 
    url: `/sys/personal/${idusuario}`, // ✅ CORREGIDO
    method: 'delete' 
  })
}