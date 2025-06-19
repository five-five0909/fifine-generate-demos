<template>
  <div class="register-page-wrapper">
    <a-card class="register-card" title="创建您的账户">
      <a-form
        :model="formState"
        name="registerForm"
        layout="vertical"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
        ref="registerFormRef"
      >
        <a-form-item
          label="用户名"
          name="username"
          :rules="[
            { required: true, message: '请输入您的用户名!' },
            { min: 3, max: 50, message: '用户名长度应为3至50个字符' }
          ]"
        >
          <a-input v-model:value="formState.username" placeholder="设置您的用户名" size="large"/>
        </a-form-item>

        <a-form-item
          label="邮箱地址"
          name="email"
          :rules="[
            { required: true, message: '请输入您的邮箱地址!' },
            { type: 'email', message: '请输入一个有效的邮箱地址!' }
          ]"
        >
          <a-input v-model:value="formState.email" placeholder="例如: user@example.com" size="large"/>
        </a-form-item>

        <a-form-item
          label="密码"
          name="password"
          :rules="[
            { required: true, message: '请输入您的密码!' },
            { min: 6, message: '密码长度至少为6个字符' }
          ]"
          has-feedback
        >
          <a-input-password v-model:value="formState.password" placeholder="设置您的密码 (至少6位)" size="large"/>
        </a-form-item>

        <a-form-item
          label="确认密码"
          name="confirmPassword"
          :dependencies="['password']"
          :rules="[
            { required: true, message: '请再次输入您的密码!' },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('password') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error('两次输入的密码不匹配!'));
              },
            }),
          ]"
          has-feedback
        >
          <a-input-password v-model:value="formState.confirmPassword" placeholder="确认您的密码" size="large"/>
        </a-form-item>

        <a-form-item
          label="昵称 (可选)"
          name="nickname"
        >
          <a-input v-model:value="formState.nickname" placeholder="设置您的昵称" size="large"/>
        </a-form-item>

        <a-form-item
            name="agreement"
            :value-prop-name="'checked'"
            :rules="[{
                required: true,
                message: '请阅读并同意用户协议和服务条款',
                transform: value => (value || undefined), // Ensure validation triggers if not checked
                validator: (_, value) => value ? Promise.resolve() : Promise.reject()
            }]">
          <a-checkbox v-model:checked="formState.agreement">
            我已阅读并同意 <a href="/user-agreement" target="_blank" rel="noopener noreferrer">用户协议</a> 和 <a href="/privacy-policy" target="_blank" rel="noopener noreferrer">隐私政策</a>.
          </a-checkbox>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading" block size="large">
            立即注册
          </a-button>
        </a-form-item>
        <div class="login-link-section">
          已有账户? <router-link to="/login">前往登录</router-link>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Form, Input, Button, Checkbox, Card, message as AntMessage } from 'ant-design-vue';
import { register } from '@/services/auth.service';

// Ant Design component aliases
const AForm = Form;
const AFormItem = Form.Item;
const AInput = Input;
const AInputPassword = Input.Password;
const AButton = Button;
const ACheckbox = Checkbox;
const ACard = Card;

const router = useRouter();
const loading = ref(false);
const registerFormRef = ref(); // For potential form instance methods access

const formState = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  agreement: false,
});

const onFinish = async () => { // `values` argument is implicitly passed from form state
  loading.value = true;
  try {
    const { username, email, password, nickname } = formState; // Destructure from reactive state
    await register({ username, email, password, nickname });
    AntMessage.success('注册成功! 即将跳转到登录页面...');
    setTimeout(() => {
      router.push({ name: 'Login' });
    }, 1500);
  } catch (error) {
    console.error('Registration failed:', error.response?.data?.message || error.message);
    // Error messages are typically handled by the request interceptor.
  } finally {
    loading.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('Form validation failed:', errorInfo);
  // AntMessage.error('请检查所有必填项和输入格式!'); // General message, or rely on field-level errors
};
</script>

<style scoped>
.register-page-wrapper {
  display: flex;
  justify-content: center;
  align-items: center; /* Vertically center */
  min-height: calc(100vh - 64px); /* Full viewport height minus navbar */
  background-color: #f0f2f5;
  padding: 20px 0; /* Vertical padding for the page */
}

.register-card {
  width: 480px; /* Slightly wider for more fields */
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  margin: 20px; /* Margin for smaller screens */
}

@media (max-width: 576px) {
  .register-card {
    width: 100%;
    max-width: 400px; /* Max width on very small screens */
    margin: 20px;
    box-shadow: none;
  }
}

.login-link-section {
  text-align: center;
  margin-top: 16px;
}
</style>
