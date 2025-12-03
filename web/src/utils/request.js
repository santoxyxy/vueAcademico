import axios from "axios";
import routers from '../router/routers';
import { useStore } from "../store";
import { errorMsg } from "./message";

// Crear instancia de axios
const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL,
    timeout: 60000
});

// Request interceptor
instance.interceptors.request.use(
    config => {
        const store = useStore();
        if (store.token) {
            config.headers['Authorization'] = 'Bearer ' + store.token;
        }
        config.headers['Content-Type'] = 'application/json';
        return config;
    },
    error => Promise.reject(error)
);

// Response interceptor
instance.interceptors.response.use(
    response => response.data,
    error => {
        const store = useStore();

        if (!error.response) {
            errorMsg(error.message || 'Error de red');
        } else {
            const code = error.response.status;
            const data = error.response.data;

            switch (code) {
                case 401: {
                    // Token expirado o no autorizado
                    const refresh = store.refreshToken;
                    if (refresh) {
                        return againRequest(refresh, error);
                    } else {
                        store.tokenAction(null);
                        routers.push({
                            path: '/login',
                            query: { backto: routers.currentRoute.fullPath }
                        });
                    }
                    break;
                }

                case 403: {
                    // Mostrar mensaje real del backend
                    if (data && (data.msg || data.detail)) {
                        errorMsg(data.msg || data.detail);
                    } else {
                        errorMsg('No tiene permiso para realizar esta acción (403)');
                    }
                    break;
                }

                default: {
                    if (data && (data.msg || data.detail)) {
                        errorMsg(data.msg || data.detail);
                    } else {
                        errorMsg('Error en la solicitud al servidor');
                    }
                    break;
                }
            }
        }

        return Promise.reject(error);
    }
);

// Reintentar request con refreshToken
async function againRequest(refresh, error) {
    const store = useStore();
    await refreshToken(refresh);
    const config = error.response.config;
    config.headers['Authorization'] = 'Bearer ' + store.token;
    const res = await axios.request(config);
    return res.data;
}

// Refrescar token
export function refreshToken(refresh) {
    const store = useStore();
    return axios({
        url: '/auth/refresh',
        method: 'put',
        headers: { Authorization: `Bearer ${refresh}` }
    }).then(res => {
        if (res.data.success) {
            store.tokenAction(res.data.data);
        } else {
            errorMsg(res.data.msg || 'Error al refrescar token');
            store.tokenAction(null);
        }
    });
}

export default instance;
