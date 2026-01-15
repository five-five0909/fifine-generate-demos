// 配置axios默认值
axios.defaults.baseURL = '';
axios.defaults.headers.common['Content-Type'] = 'application/json';

// 添加响应拦截器
axios.interceptors.response.use(
    response => response,
    error => {
        console.error('请求错误:', error);
        if (error.response) {
            console.error('错误状态码:', error.response.status);
            console.error('错误数据:', error.response.data);
        }
        return Promise.reject(error);
    }
);

// 创建路由实例
const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/', component: Home },
        { path: '/textbook', component: TextbookList },
        { path: '/category', component: CategoryList },
        { path: '/borrow', component: BorrowList }
    ]
});

// 创建Vue应用
const app = new Vue({
    el: '#app',
    router: router,
    data: {
        loading: false
    },
    created() {
        // 添加请求拦截器
        axios.interceptors.request.use(config => {
            this.loading = true;
            console.log('发送请求:', config.url);
            return config;
        });

        // 添加响应拦截器
        axios.interceptors.response.use(
            response => {
                this.loading = false;
                console.log('收到响应:', response.config.url, response.data);
                return response;
            },
            error => {
                this.loading = false;
                console.error('请求错误:', error);
                return Promise.reject(error);
            }
        );
    }
}); 