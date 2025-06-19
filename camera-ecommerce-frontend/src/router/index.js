import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue'; // 首页组件
import Login from '../views/auth/Login.vue';
import Register from '../views/auth/Register.vue';
import Profile from '../views/user/Profile.vue';
import ProductList from '../views/product/ProductList.vue';
import ProductDetail from '../views/product/ProductDetail.vue';
import ShoppingCart from '../views/cart/ShoppingCart.vue';
import Checkout from '../views/order/Checkout.vue';
import OrderList from '../views/order/OrderList.vue';
import OrderDetail from '../views/order/OrderDetail.vue';
import NotFound from '../views/NotFound.vue'; // 404页面

// 定义路由规则
const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: { title: '首页' }
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { title: '登录', guestOnly: true } // guestOnly表示已登录用户不应访问
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: { title: '注册', guestOnly: true }
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile,
        meta: { title: '个人中心', requiresAuth: true } // requiresAuth表示需要登录
    },
    {
        path: '/products',
        name: 'ProductList',
        component: ProductList,
        meta: { title: '商品列表' }
    },
    {
        path: '/products/:id', // 商品详情页，使用动态路由参数:id
        name: 'ProductDetail',
        component: ProductDetail,
        props: true, // 将路由参数作为props传递给组件
        meta: { title: '商品详情' }
    },
    {
        path: '/cart',
        name: 'ShoppingCart',
        component: ShoppingCart,
        meta: { title: '购物车', requiresAuth: true }
    },
    {
        path: '/checkout',
        name: 'Checkout',
        component: Checkout,
        meta: { title: '结算', requiresAuth: true }
    },
    {
        path: '/orders',
        name: 'OrderList',
        component: OrderList,
        meta: { title: '我的订单', requiresAuth: true }
    },
    {
        path: '/orders/:id', // 订单详情页
        name: 'OrderDetail',
        component: OrderDetail,
        props: true,
        meta: { title: '订单详情', requiresAuth: true }
    },
    {
        path: '/:pathMatch(.*)*', // 捕获所有未匹配的路由
        name: 'NotFound',
        component: NotFound,
        meta: { title: '404 - 页面未找到' }
    }
];

// 创建路由实例
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL), // 使用HTML5 History模式
    routes, // 路由规则数组
    scrollBehavior(to, from, savedPosition) {
        // 页面切换时，滚动到顶部
        if (savedPosition) {
            return savedPosition;
        } else {
            return { top: 0 };
        }
    }
});

// 全局前置守卫 (Navigation Guard)
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('userToken'); // 假设token存储在localStorage

    // 更新页面标题
    if (to.meta.title) {
        document.title = `${to.meta.title} - 相机商城`;
    } else {
        document.title = '相机商城';
    }

    if (to.matched.some(record => record.meta.requiresAuth)) {
        // 此路由需要认证，检查是否已登录
        // 如果没有，则重定向到登录页面
        if (!token) {
            next({
                name: 'Login',
                query: { redirect: to.fullPath } // 保存用户尝试访问的原始路径，登录后可以跳回
            });
        } else {
            next(); // 已登录，继续导航
        }
    } else if (to.matched.some(record => record.meta.guestOnly)) {
        // 此路由只允许未登录用户访问（如登录、注册页）
        if (token) {
            next({ name: 'Home' }); // 如果已登录，重定向到首页
        } else {
            next();
        }
    }
    else {
        next(); // 确保一定要调用 next()
    }
});

export default router;
