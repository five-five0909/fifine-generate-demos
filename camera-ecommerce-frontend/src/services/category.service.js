import request from '../utils/request';

const API_PREFIX = '/categories'; // 后端CategoryController的基础路径

/**
 * 获取商品分类列表
 * @param {object} params - 查询参数, 例如 { parentId: 0, pageSize: 10, pageNum: 1 }
 * @returns {Promise}
 */
export function getCategories(params) {
    return request({
        url: API_PREFIX,
        method: 'get',
        params // GET请求的参数放在params中
    });
}

/**
 * 获取单个商品分类详情
 * @param {number|string} id - 分类ID
 * @returns {Promise}
 */
export function getCategoryById(id) {
    return request({
        url: `${API_PREFIX}/${id}`,
        method: 'get'
    });
}
