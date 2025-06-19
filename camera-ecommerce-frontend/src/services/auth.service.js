import request from '../utils/request'; // 引入封装好的axios实例

const API_PREFIX = '/auth'; // 后端AuthController的基础路径

/**
 * 用户登录
 * @param {object} data - 包含 username 和 password 的对象
 * @returns {Promise}
 */
export function login(data) {
    return request({
        url: `${API_PREFIX}/login`,
        method: 'post',
        data
    });
}

/**
 * 用户注册
 * @param {object} data - 包含 username, password, email 的对象
 * @returns {Promise}
 */
export function register(data) {
    return request({
        url: `${API_PREFIX}/register`,
        method: 'post',
        data
    });
}

// 可以根据需要添加其他认证相关的API调用，例如获取验证码、忘记密码等

// 示例：获取当前用户信息 (通常放在user.service.js)
// export function getProfile() {
//   return request({
//     url: '/users/me', // 假设用户信息的API路径
//     method: 'get'
//   });
// }
