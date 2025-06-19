import request from '../utils/request';

const API_PREFIX = '/orders'; // 后端OrderController的基础路径

/**
 * 创建订单
 * @param {object} data - 订单创建请求体, 例如 { receiverName, receiverPhone, receiverAddress, remarks }
 * @returns {Promise}
 */
export function createOrder(data) {
    return request({
        url: API_PREFIX,
        method: 'post',
        data
    });
}

/**
 * 查询用户订单列表 (分页)
 * @param {object} params - 分页参数, 例如 { current, size }
 * @returns {Promise}
 */
export function getUserOrders(params) {
    return request({
        url: API_PREFIX,
        method: 'get',
        params
    });
}

/**
 * 查询单个订单详情
 * @param {number|string} orderId - 订单ID
 * @returns {Promise}
 */
export function getOrderDetails(orderId) {
    return request({
        url: `${API_PREFIX}/${orderId}`,
        method: 'get'
    });
}

/**
 * 模拟支付接口
 * @param {number|string} orderId - 订单ID
 * @returns {Promise}
 */
export function simulatePayment(orderId) {
    return request({
        url: `${API_PREFIX}/${orderId}/pay`,
        method: 'post'
    });
}

/**
 * 用户取消订单
 * @param {number|string} orderId - 订单ID
 * @returns {Promise}
 */
export function cancelOrder(orderId) {
    return request({
        url: `${API_PREFIX}/${orderId}/cancel`,
        method: 'put' // 后端是PUT
    });
}
