<template>
  <div class="not-found-page">
    <a-result
      status="404"
      title="404 - 页面飞走了"
      sub-title="抱歉，您寻找的页面似乎不在这个宇宙中。"
    >
      <template #icon>
        <RocketOutlined :style="{ fontSize: '72px', color: '#1890ff' }" />
      </template>
      <template #extra>
        <router-link to="/">
          <a-button type="primary" size="large">返回首页探索</a-button>
        </router-link>
        <a-button @click="goBack" size="large" class="ml-2">返回上一页</a-button>
      </template>
      <div class="suggestion-box">
        <p>您可能输错了网址，或者该页面已被移除或从未存在。</p>
        <p>试试检查您的网址拼写，或从我们的 <router-link to="/products">商品列表</router-link> 开始新的探索。</p>
      </div>
    </a-result>
  </div>
</template>

<script setup>
import { Result, Button } from 'ant-design-vue';
import { RocketOutlined } from '@ant-design/icons-vue';
import { useRouter } from 'vue-router';

const AResult = Result;
const AButton = Button;

const router = useRouter();
const goBack = () => {
  router.go(-1);
};
</script>

<style scoped>
.not-found-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px - 70px); /* Adjust based on actual Navbar/Footer height */
  padding: 20px;
  background-color: #f0f2f5; /* Optional: background color */
}
.ant-result-title {
  font-size: 28px !important;
  color: #333;
}
.ant-result-subtitle {
  font-size: 16px;
  color: #555;
}
.suggestion-box {
  margin-top: 30px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  max-width: 500px;
  text-align: left;
  line-height: 1.8;
}
.suggestion-box p {
  margin-bottom: 10px;
  color: #777;
}
.suggestion-box p:last-child {
  margin-bottom: 0;
}
.ml-2 {
    margin-left: 8px;
}
</style>
