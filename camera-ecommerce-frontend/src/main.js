import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 路由配置
import Antd from 'ant-design-vue'; // 引入Ant Design Vue
import 'ant-design-vue/dist/reset.css'; // Ant Design Vue的重置样式 (v4.x+) 或 'ant-design-vue/dist/antd.css' (v3.x)

// 如果需要，可以在这里引入Bootstrap的栅格系统或部分CSS
// import 'bootstrap/dist/css/bootstrap-grid.min.css'; // 示例：只引入栅格系统
// import 'bootstrap/dist/css/bootstrap-reboot.min.css'; // 示例：只引入Reboot

import './assets/styles/base.css'; // 引入自定义的基础样式

const app = createApp(App);

app.use(router); // 使用Vue Router
app.use(Antd);   // 使用Ant Design Vue

app.mount('#app');
