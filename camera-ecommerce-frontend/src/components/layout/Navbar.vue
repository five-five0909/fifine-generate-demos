<template>
  <a-layout-header class="navbar">
    <div class="container navbar-content">
      <div class="logo">
        <router-link to="/">相机商城</router-link>
      </div>
      <a-menu
        theme="dark"
        mode="horizontal"
        :selectedKeys="selectedKeys"
        :items="filteredMenuItems"
        @select="handleMenuSelect"
        class="nav-menu"
      />
      <div class="user-actions">
        <template v-if="isLoggedIn">
          <a-dropdown :trigger="['hover']">
            <a class="ant-dropdown-link user-avatar-link" @click.prevent>
              <a-avatar :style="{ backgroundColor: avatarColor, verticalAlign: 'middle' }">
                {{ usernameFirstChar }}
              </a-avatar>
              <span class="username-display">{{ currentUser?.nickname || currentUser?.username }}</span>
              <DownOutlined />
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item key="profile">
                  <router-link to="/profile">个人中心</router-link>
                </a-menu-item>
                <a-menu-item key="orders">
                  <router-link to="/orders">我的订单</router-link>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" @click="handleLogout">
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">登录</router-link>
          <router-link to="/register" class="nav-link">注册</router-link>
        </template>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { LayoutHeader, Menu, Dropdown, Avatar, message as AntMessage } from 'ant-design-vue';
import { DownOutlined } from '@ant-design/icons-vue';

// Component imports
const ALayoutHeader = LayoutHeader;
const AMenu = Menu;
const AMenuItem = Menu.Item;
const AMenuDivider = Menu.Divider;
const ADropdown = Dropdown;
const AAvatar = Avatar;

const router = useRouter();
const route = useRoute();

const isLoggedIn = ref(false);
const currentUser = ref({});

const defaultMenuItems = [
  { key: '/', label: '首页', path: '/' },
  { key: '/products', label: '全部商品', path: '/products' },
  // Add more public menu items here if needed
];

const authMenuItems = [
    { key: '/cart', label: '购物车', path: '/cart', requiresAuth: true },
    // { key: '/orders', label: '我的订单', path: '/orders', requiresAuth: true }, // Now in dropdown
];

const menuItems = computed(() => {
    let items = [...defaultMenuItems];
    if (isLoggedIn.value) {
        items = items.concat(authMenuItems.filter(item => !menuItemsForUserDropdown.some(d => d.key === item.key.replace('/', ''))));
    }
    return items;
});

const filteredMenuItems = computed(() => menuItems.value);


const menuItemsForUserDropdown = [
    { key: 'profile', label: '个人中心', path: '/profile'},
    { key: 'orders', label: '我的订单', path: '/orders'},
];


const updateAuthState = () => {
  isLoggedIn.value = !!localStorage.getItem('userToken');
  currentUser.value = JSON.parse(localStorage.getItem('userInfo') || '{}');
};

const usernameFirstChar = computed(() => {
  const name = currentUser.value?.nickname || currentUser.value?.username;
  return name ? name.charAt(0).toUpperCase() : 'U';
});

// Simple avatar color generation based on username
const avatarColor = computed(() => {
  const name = currentUser.value?.username || 'User';
  let hash = 0;
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash);
  }
  const c = (hash & 0x00FFFFFF).toString(16).toUpperCase();
  return "#" + "00000".substring(0, 6 - c.length) + c;
});


const selectedKeys = ref([]);

watch(
  () => route.path,
  (newPath) => {
    updateAuthState(); // Keep auth state fresh on route change
    const baseItem = menuItems.value.find(item => newPath === item.path);
    if (baseItem) {
        selectedKeys.value = [baseItem.key];
    } else {
        const parentItem = menuItems.value.find(item => item.path !== '/' && newPath.startsWith(item.path));
        selectedKeys.value = parentItem ? [parentItem.key] : [];
    }
  },
  { immediate: true }
);

// Handle auth changes from other tabs/windows via localStorage events
const handleStorageChange = (event) => {
  if (event.key === 'userToken' || event.key === 'userInfo') {
    updateAuthState();
  }
};

onMounted(() => {
  updateAuthState(); // Initial check
  window.addEventListener('storage', handleStorageChange);
  // Custom event for login/logout within the same tab if needed
  window.addEventListener('auth-change', updateAuthState);
});

onUnmounted(() => {
  window.removeEventListener('storage', handleStorageChange);
  window.removeEventListener('auth-change', updateAuthState);
});


const handleMenuSelect = ({ key }) => {
  router.push(key);
};

const handleLogout = () => {
  localStorage.removeItem('userToken');
  localStorage.removeItem('userInfo');
  updateAuthState(); // Update local state
  AntMessage.success('已成功退出登录');
  router.push({ name: 'Login' });
  // Dispatch a custom event if other components need to react immediately
  window.dispatchEvent(new CustomEvent('auth-change'));
};

</script>

<style scoped>
.navbar {
  background-color: #001529;
  padding: 0; /* Remove default padding if container handles it */
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 64px;
}

.navbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  /* Using Ant Design's container class if preferred, or custom padding */
   padding: 0 20px; /* Default padding */
}

.logo {
  font-size: 22px; /* Slightly larger logo */
  font-weight: bold;
}
.logo a {
  color: white;
  text-decoration: none;
  transition: opacity 0.3s;
}
.logo a:hover {
    opacity: 0.8;
}

.nav-menu {
  line-height: 62px; /* Align text vertically */
  border-bottom: none;
  /* flex-grow: 1; */ /* Removed to allow user-actions to not be pushed too far */
  margin-left: 25px; /* Space between logo and menu */
}

/* For Ant Design Vue v4+, menu items are configured via JS. */
/* If specific styling overrides are needed, target .ant-menu-item or .ant-menu-submenu-title */
:deep(.ant-menu-item a) { /* Styling router-link within menu items */
  color: rgba(255, 255, 255, 0.85);
}
:deep(.ant-menu-item-selected a),
:deep(.ant-menu-item a:hover) {
  color: white;
}
:deep(.ant-menu-item-selected) {
  background-color: #1890ff !important; /* Ant Design primary blue for selected */
}


.user-actions {
  display: flex;
  align-items: center;
  color: white;
  margin-left: auto; /* Push user actions to the right */
}

.user-actions .nav-link {
  color: rgba(255, 255, 255, 0.85);
  margin-left: 18px; /* Spacing for login/register links */
  text-decoration: none;
  transition: color 0.3s;
  padding: 8px 12px;
  border-radius: 4px;
}
.user-actions .nav-link:hover {
  color: white;
  background-color: rgba(255,255,255,0.1);
}

.user-avatar-link {
  color: rgba(255, 255, 255, 0.85);
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}
.user-avatar-link:hover {
  color: white;
  background-color: rgba(255,255,255,0.1);
}
.username-display {
  margin-left: 8px;
  margin-right: 4px; /* Space before dropdown icon */
  max-width: 100px; /* Prevent long names from breaking layout */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Ensure dropdown menu items with router-link also get styled */
:deep(.ant-dropdown-menu-item a),
:deep(.ant-dropdown-menu-item > span > a) {
  color: inherit !important; /* Inherit color from menu item */
  display: block; /* Make link fill the item */
}
:deep(.ant-dropdown-menu-item a:hover) {
  color: #1890ff !important;
}
</style>
