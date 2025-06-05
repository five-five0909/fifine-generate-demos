// src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import { createPinia } from 'pinia'; // Import Pinia

const app = createApp(App);
const pinia = createPinia(); // Create Pinia instance

app.use(Antd);
app.use(pinia); // Use Pinia
app.mount('#app');
