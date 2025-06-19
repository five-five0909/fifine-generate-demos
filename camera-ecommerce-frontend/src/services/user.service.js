import request from '../utils/request';

const API_PREFIX = '/users';

export function getCurrentUserProfile() {
    return request({
        url: `${API_PREFIX}/me`,
        method: 'get'
    });
}

export function updateUserProfile(data) {
    return request({
        url: `${API_PREFIX}/me`,
        method: 'put',
        data
    });
}

export function updateUserPassword(data) {
    return request({
        url: `${API_PREFIX}/password`,
        method: 'put',
        data
    });
}
