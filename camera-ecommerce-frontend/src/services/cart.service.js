import request from '../utils/request';

const API_PREFIX = '/cart'; // 后端CartController的基础路径

/**
 * 添加商品到购物车
 * @param {object} data - { productId, quantity }
 * @returns {Promise}
 */
export function addItemToCart(data) {
    return request({
        url: `${API_PREFIX}/items`,
        method: 'post',
        data
    });
}

/**
 * 查看购物车
 * @returns {Promise}
 */
export function getCartItems() {
    return request({
        url: API_PREFIX,
        method: 'get'
    });
}

/**
 * 修改购物车中商品数量或选中状态
 * @param {number|string} itemId - 购物车项ID
 * @param {object} data - { quantity, checkedStatus }
 * @returns {Promise}
 */
export function updateCartItem(itemId, data) {
    return request({
        url: `${API_PREFIX}/items/${itemId}`,
        method: 'put',
        data
    });
}

/**
 * 从购物车删除商品
 * @param {number|string} itemId - 购物车项ID
 * @returns {Promise}
 */
export function deleteCartItem(itemId) {
    return request({
        url: `${API_PREFIX}/items/${itemId}`,
        method: 'delete'
    });
}

/**
 * 清空当前用户购物车
 * @returns {Promise}
 */
export function clearCart() {
    return request({
        url: `${API_PREFIX}/clear`,
        method: 'delete'
    });
}
