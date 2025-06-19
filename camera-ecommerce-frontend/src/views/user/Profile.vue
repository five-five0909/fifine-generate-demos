<template>
  <div class="profile-container container mt-4 mb-4">
    <a-row :gutter="[24, 24]">
      <!-- 左侧菜单 -->
      <a-col :xs="24" :sm="24" :md="6">
        <a-card title="个人中心" class="profile-menu-card">
          <a-menu
            :selectedKeys="selectedMenuKeys"
            mode="inline"
            @click="handleMenuClick"
          >
            <a-menu-item key="info">
              <UserOutlined /> 基本信息
            </a-menu-item>
            <a-menu-item key="orders">
              <SolutionOutlined /> 我的订单
            </a-menu-item>
            <a-menu-item key="password">
              <LockOutlined /> 修改密码
            </a-menu-item>
            <!-- 可以添加更多菜单项，如我的收藏、收货地址等 -->
          </a-menu>
        </a-card>
      </a-col>

      <!-- 右侧内容区域 -->
      <a-col :xs="24" :sm="24" :md="18">
        <a-card :title="activeTabTitle" class="profile-content-card">
          <!-- 基本信息表单 -->
          <div v-if="activeTab === 'info'">
            <a-skeleton :loading="initialLoading" active avatar :paragraph="{ rows: 4 }">
              <a-form
                :model="profileFormState"
                layout="vertical"
                @finish="handleUpdateProfile"
                ref="profileFormRef"
              >
                <a-row :gutter="16">
                  <a-col :xs="24" :sm="12">
                    <a-form-item label="用户名">
                      <a-input :value="profileFormState.username" disabled />
                    </a-form-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-item label="邮箱" name="email" :rules="[{ type: 'email', message: '邮箱格式不正确'}]">
                      <a-input v-model:value="profileFormState.email" placeholder="请输入邮箱" />
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row :gutter="16">
                  <a-col :xs="24" :sm="12">
                    <a-form-item label="昵称" name="nickname">
                      <a-input v-model:value="profileFormState.nickname" placeholder="请输入昵称" />
                    </a-form-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-item label="联系电话" name="phoneNumber">
                      <a-input v-model:value="profileFormState.phoneNumber" placeholder="请输入联系电话" />
                    </a-form-item>
                  </a-col>
                </a-row>
                 <a-form-item label="头像URL (可选)" name="avatarUrl">
                    <a-input v-model:value="profileFormState.avatarUrl" placeholder="请输入头像链接" />
                    <!-- TODO: Replace with a file upload component for actual avatar upload -->
                    <a-avatar :src="profileFormState.avatarUrl" :size="64" class="mt-2" v-if="profileFormState.avatarUrl">
                         <template #icon><UserOutlined /></template>
                    </a-avatar>
                </a-form-item>
                <a-form-item>
                  <a-button type="primary" html-type="submit" :loading="profileLoading">保存更改</a-button>
                </a-form-item>
              </a-form>
            </a-skeleton>
          </div>

          <!-- 修改密码表单 -->
          <div v-if="activeTab === 'password'">
            <a-form
              :model="passwordFormState"
              layout="vertical"
              @finish="handleUpdatePassword"
              ref="passwordFormRef"
              :style="{ maxWidth: '450px' }"
            >
              <a-form-item
                label="旧密码"
                name="oldPassword"
                :rules="[{ required: true, message: '请输入旧密码' }]"
              >
                <a-input-password v-model:value="passwordFormState.oldPassword" placeholder="您当前的密码"/>
              </a-form-item>
              <a-form-item
                label="新密码"
                name="newPassword"
                :rules="[{ required: true, message: '请输入新密码' }, { min: 6, message: '新密码至少6位字符' }]"
                has-feedback
              >
                <a-input-password v-model:value="passwordFormState.newPassword" placeholder="设置您的新密码 (至少6位)"/>
              </a-form-item>
              <a-form-item
                label="确认新密码"
                name="confirmNewPassword"
                :dependencies="['newPassword']"
                :rules="[
                  { required: true, message: '请再次输入新密码' },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue('newPassword') === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject(new Error('两次输入的新密码不一致'));
                    },
                  }),
                ]"
                has-feedback
              >
                <a-input-password v-model:value="passwordFormState.confirmNewPassword" placeholder="确认您的新密码"/>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" html-type="submit" :loading="passwordLoading">确认修改密码</a-button>
              </a-form-item>
            </a-form>
          </div>

           <div v-if="activeTab === 'orders'" class="orders-tab-content">
            <p>您可以在这里查看您的订单历史、跟踪订单状态以及管理您的购买记录。</p>
            <a-button type="primary" @click="() => router.push('/orders')">
              <SolutionOutlined class="mr-1"/> 前往我的订单页面
            </a-button>
          </div>

        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Menu, Card, Form, Input, Button, Row, Col, Avatar, Skeleton, message as AntMessage } from 'ant-design-vue';
import { UserOutlined, LockOutlined, SolutionOutlined } from '@ant-design/icons-vue';
import { getCurrentUserProfile, updateUserProfile, updateUserPassword } from '@/services/user.service';

// Ant Design Component Aliases
const AMenu = Menu;
const AMenuItem = Menu.Item;
const ACard = Card;
const AForm = Form;
const AFormItem = Form.Item;
const AInput = Input;
const AInputPassword = Input.Password;
const AButton = Button;
const ARow = Row;
const ACol = Col;
const AAvatar = Avatar;
const ASkeleton = Skeleton;

const router = useRouter();

const activeTab = ref('info');
const initialLoading = ref(true); // For initial profile fetch
const profileLoading = ref(false); // For profile update submission
const passwordLoading = ref(false); // For password update submission

const profileFormRef = ref();
const passwordFormRef = ref();

const profileFormState = reactive({
  id: null,
  username: '',
  email: '',
  nickname: '',
  phoneNumber: '',
  avatarUrl: '',
});

const passwordFormState = reactive({
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: '',
});

const selectedMenuKeys = computed(() => [activeTab.value]);
const activeTabTitle = computed(() => {
  switch (activeTab.value) {
    case 'info': return '编辑基本信息';
    case 'password': return '修改账户密码';
    case 'orders': return '我的订单概览';
    default: return '个人中心';
  }
});

const handleMenuClick = (e) => {
  if (e.key === 'orders') {
    // Option 1: Navigate directly
    // router.push('/orders');
    // Option 2: Switch tab and show a button to navigate (current implementation)
    activeTab.value = e.key;
  } else {
    activeTab.value = e.key;
  }
};

const fetchProfile = async () => {
  initialLoading.value = true;
  try {
    const data = await getCurrentUserProfile();
    if (data) {
      Object.assign(profileFormState, data);
    } else {
      AntMessage.error('未能加载用户信息，请尝试重新登录。');
      router.push('/login');
    }
  } catch (error) {
    AntMessage.error('获取用户信息失败: ' + (error.response?.data?.message || error.message || '请稍后重试'));
    if(error.response?.status === 401) router.push('/login');
  } finally {
    initialLoading.value = false;
  }
};

onMounted(() => {
  fetchProfile();
});

const handleUpdateProfile = async () => {
  profileLoading.value = true;
  try {
    const { email, nickname, phoneNumber, avatarUrl } = profileFormState;
    const updatedData = await updateUserProfile({ email, nickname, phoneNumber, avatarUrl });
    if (updatedData) {
      Object.assign(profileFormState, updatedData);
      const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
      localStorage.setItem('userInfo', JSON.stringify({
        ...storedUserInfo,
        nickname: updatedData.nickname || profileFormState.nickname,
        email: updatedData.email || profileFormState.email,
        avatarUrl: updatedData.avatarUrl || profileFormState.avatarUrl,
      }));
      window.dispatchEvent(new CustomEvent('auth-change')); // Notify Navbar
      AntMessage.success('用户信息更新成功!');
    }
  } catch (error) {
    AntMessage.error('更新失败: ' + (error.response?.data?.message || error.message || '请稍后重试'));
  } finally {
    profileLoading.value = false;
  }
};

const handleUpdatePassword = async () => {
  passwordLoading.value = true;
  try {
    await updateUserPassword({
      oldPassword: passwordFormState.oldPassword,
      newPassword: passwordFormState.newPassword,
    });
    AntMessage.success('密码修改成功! 为安全起见，请重新登录。');
    passwordFormRef.value.resetFields();

    localStorage.removeItem('userToken');
    localStorage.removeItem('userInfo');
    window.dispatchEvent(new CustomEvent('auth-change')); // Notify Navbar
    router.push('/login');

  } catch (error) {
     AntMessage.error('密码修改失败: ' + (error.response?.data?.message || error.message || '请检查您的输入'));
  } finally {
    passwordLoading.value = false;
  }
};

</script>

<style scoped>
.profile-container {
  /* background-color: #f0f2f5; */ /* Moved to page wrapper if needed */
}
.profile-menu-card .ant-menu-item {
  margin-bottom: 8px !important; /* Add space between menu items */
}
.profile-menu-card .ant-menu-item:last-child {
  margin-bottom: 0 !important;
}
.profile-menu-card .ant-menu-item .anticon {
  margin-right: 8px;
}
.profile-content-card .ant-card-head {
  font-size: 18px; /* Larger title for content card */
}
.orders-tab-content {
    padding: 20px;
    text-align: center;
}
.orders-tab-content p {
    margin-bottom: 20px;
    font-size: 16px;
    color: #555;
}
.mr-1 {
    margin-right: 4px;
}
.mt-2 {
    margin-top: 8px !important;
}
</style>
