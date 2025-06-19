import request from '../utils/request';

const API_PREFIX = '/products'; // 后端ProductController的基础路径

/**
 * 分页查询商品列表
 * @param {object} params - 查询参数, 例如 { categoryId, keyword, current, size, sort, minPrice, maxPrice }
 * @returns {Promise}
 */
export function getProducts(params) {
    return request({
        url: API_PREFIX,
        method: 'get',
        params
    });
}

/**
 * 获取单个商品详情
 * @param {number|string} id - 商品ID
 * @returns {Promise}
 */
export function getProductById(id) {
    return request({
        url: `${API_PREFIX}/${id}`,
        method: 'get'
    });
}
