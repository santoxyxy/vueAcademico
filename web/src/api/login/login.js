import request from "../../utils/request";

//  Acceso
export function login(data){
    return request({
        url: '/auth/login',
        method: 'post',
        data
    })
}

// Obtener código de verificación
export function getVerifyCode(params){
    return request({
        url: '/auth/code',
        method: 'get',
        params
    })
}