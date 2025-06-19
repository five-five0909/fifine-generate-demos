<template>
  <div class="login-page-wrapper">
    <a-card class="login-card" title="用户登录">
      <a-form
        :model="formState"
        name="loginForm"
        layout="vertical"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          label="用户名或邮箱"
          name="username"
          :rules="[{ required: true, message: '请输入您的用户名或邮箱!' }]"
        >
          <a-input v-model:value="formState.username" placeholder="用户名或邮箱" size="large">
            <template #prefix><UserOutlined /></template>
          </a-input>
        </a-form-item>

        <a-form-item
          label="密码"
          name="password"
          :rules="[{ required: true, message: '请输入您的密码!' }]"
        >
          <a-input-password v.model:value="formState.password" placeholder="密码" size="large">
            <template #prefix><LockOutlined /></template>
          </a-input-password>
        </a-form-item>

        <a-form-item class="login-form-actions">
          <a-checkbox v-model:checked="formState.remember">记住我</a-checkbox>
          <router-link to="/forgot-password" class="forgot-password-link">忘记密码?</router-link>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" block size="large">
            登录
          </a-button>
        </a-form-item>
         <div class="register-link-section">
          没有账户? <router-link to="/register">立即注册</router-link>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Form, Input, Button, Checkbox, Card, message as AntMessage } from 'ant-design-vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { login } from '@/services/auth.service';

// Ant Design component aliases for clarity in template if preferred, though not strictly necessary with <script setup>
const AForm = Form;
const AFormItem = Form.Item;
const AInput = Input;
const AInputPassword = Input.Password;
const AButton = Button;
const ACheckbox = Checkbox;
const ACard = Card;

const router = useRouter();
const route = useRoute();
const loading = ref(false);

const formState = reactive({
  username: '',
  password: '',
  remember: true,
});

const onFinish = async (values) => {
  loading.value = true;
  try {
    const response = await login(values);

    localStorage.setItem('userToken', response.token);
    // Storing more user info from response for immediate use in Navbar, etc.
    const userInfo = {
        id: response.id,
        username: response.username,
        email: response.email,
        nickname: response.nickname, // Assuming backend provides these
        avatarUrl: response.avatarUrl
    };
    localStorage.setItem('userInfo', JSON.stringify(userInfo));

    AntMessage.success('登录成功!');

    // Dispatch custom event to notify other components (like Navbar) about auth change
    window.dispatchEvent(new CustomEvent('auth-change'));

    const redirectPath = route.query.redirect || '/';
    router.push(redirectPath);

  } catch (error) {
    // Error messages are handled by the request interceptor, but logging here can be useful.
    console.error('Login failed:', error.response?.data?.message || error.message);
    // AntMessage.error(error.response?.data?.message || '登录失败，请检查您的凭据'); // Usually handled by interceptor
  } finally {
    loading.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('Form validation failed:', errorInfo);
  // AntMessage.error('请检查输入项!'); // This could be redundant if fields show their own errors
};
</script>

<style scoped>
.login-page-wrapper {
  display: flex;
  justify-content: center;
  align-items: center; /* Vertically center the card */
  min-height: calc(100vh - 64px); /* Full viewport height minus navbar */
  background-color: #f0f2f5;
  padding: 20px; /* Add some padding for smaller screens */
}

.login-card {
  width: 400px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15); /* Softer shadow */
}

@media (max-width: 480px) {
  .login-card {
    width: 100%;
    margin: 20px;
    box-shadow: none;
  }
}

.login-form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-password-link {
  line-height: 1; /* Align with checkbox */
}

.register-link-section {
  text-align: center;
  margin-top: 16px; /* Space above the link */
}
</style>
