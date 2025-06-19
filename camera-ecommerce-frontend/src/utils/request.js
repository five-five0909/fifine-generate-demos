import axios from 'axios';
import { message as AntMessage } from 'ant-design-vue'; // Ant Design Vue的消息提示组件

// 创建 Axios 实例
const service = axios.create({
    // 从环境变量读取API基础路径，方便不同环境部署
    // 需要在 .env.development 或 .env.production 文件中定义 VITE_API_BASE_URL
    // 例如: VITE_API_BASE_URL=/api  (如果前端和后端部署在同一域名下，通过Nginx等代理)
    // 或 VITE_API_BASE_URL=http://localhost:8080/api (如果后端独立部署)
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api', // API的基础URL
    timeout: 10000, // 请求超时时间 (毫秒)
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
});

// 请求拦截器 (Request Interceptor)
service.interceptors.request.use(
    config => {
        // 在发送请求之前做些什么，例如加入Token
        const token = localStorage.getItem('userToken'); // 从localStorage获取Token
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        // 对请求错误做些什么
        console.error('Request Interceptor Error:', error); // 用于调试
        return Promise.reject(error);
    }
);

// 响应拦截器 (Response Interceptor)
service.interceptors.response.use(
    /**
     * 如果你想获取诸如header或status之类的http信息
     * 请返回 response => response
     */

    /**
     * 通过自定义代码确定请求状态
     * 你也可以通过HTTP状态代码来判断状态
     */
    response => {
        const res = response.data;

        // 如果自定义代码不是200 (或2xx范围)，则判断为错误。
        // 这里的判断逻辑可能需要根据后端API的实际响应结构来调整
        // 例如，有些后端会在成功时也返回一个包含code, message, data的对象
        if (response.status >= 200 && response.status < 300) {
            // 如果后端返回的 res 直接就是数据，则直接返回
            // 如果后端返回 { code: 0, message: 'success', data: ... } 结构，则判断 res.code
            // 假设后端成功时直接返回数据，或错误时返回特定结构
            return res;
        } else {
            // 这种方式更依赖HTTP状态码，如果后端总是返回200，但在body里用code表示业务错误，则需要修改逻辑
            AntMessage.error(res.message || `Error: ${response.status}`);
            return Promise.reject(new Error(res.message || `Error: ${response.status}`));
        }
    },
    error => {
        console.error('Response Interceptor Error details:', error); // 用于调试
        let errorMessage = '网络错误，请稍后再试';

        if (error.response) {
            // 请求已发出，但服务器响应的状态码不在 2xx 范围内
            const status = error.response.status;
            const data = error.response.data;

            console.error('Error response status:', status);
            console.error('Error response data:', data);


            switch (status) {
                case 400:
                    // 通常是参数校验错误，后端GlobalExceptionHandler会返回详细信息
                    if (typeof data === 'string') {
                        errorMessage = data;
                    } else if (data && typeof data.message === 'string') {
                        errorMessage = data.message;
                    } else if (data && typeof data.error === 'string') { // Backend might return {error: "...", message: "..."}
                        errorMessage = data.error + (data.message ? `: ${data.message}` : '');
                    }
                     else if (data && typeof data === 'object' && !data.message) {
                        // If data is an object (like validation errors map)
                        const firstErrorKey = Object.keys(data)[0];
                        const firstErrorValue = data[firstErrorKey];
                        if (firstErrorKey && firstErrorValue) {
                             errorMessage = `${firstErrorKey}: ${firstErrorValue}`;
                        } else {
                            errorMessage = '请求参数错误';
                        }
                    }
                    else {
                        errorMessage = '请求参数错误';
                    }
                    break;
                case 401:
                    errorMessage = (data && data.message) ? data.message : '认证失败或Token已过期，请重新登录';
                    // TODO: Add logic to redirect to login page
                    // Example: router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } });
                    // This requires router instance. A common pattern is to use an event bus or a navigation module.
                    localStorage.removeItem('userToken'); // Clear invalid token
                    // Forcing a reload to login page if router is not available here.
                    // Consider a more elegant solution like a navigation utility or event.
                    if (window.location.pathname !== '/login') {
                        // window.location.href = '/login';
                    }
                    break;
                case 403:
                    errorMessage = (data && data.message) ? data.message : '您没有权限执行此操作';
                    break;
                case 404:
                    errorMessage = (data && data.message) ? data.message : '请求的资源未找到';
                    break;
                case 500:
                    errorMessage = (data && data.message) ? data.message : '服务器内部错误，请联系管理员';
                    break;
                default:
                    errorMessage = (data && data.message) ? data.message : `请求错误 (${status})`;
            }
        } else if (error.request) {
            // 请求已发出，但没有收到响应 (例如网络问题)
            errorMessage = '无法连接到服务器，请检查网络';
        } else {
            // 其他错误 (例如在设置请求时发生错误)
             errorMessage = error.message || '请求发起失败';
        }

        AntMessage.error(errorMessage);
        return Promise.reject(error); // 返回原始错误，以便调用方可以进一步处理
    }
);

export default service;
