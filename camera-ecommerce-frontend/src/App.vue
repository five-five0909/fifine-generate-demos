<template>
  <a-config-provider
    :locale="zhCN"
    :theme="{
      token: {
        colorPrimary: '#00b96b', // Custom theme color example
        // borderRadius: '2px', // Example: More squared corners
      },
      // components: { // Example: Component specific theme
      //   Button: {
      //     colorPrimary: '#00b96b',
      //     // algorithm: true, // Enable algorithm for component if needed
      //   }
      // }
    }"
  >
    <div id="app-wrapper">
      <Navbar />
      <main class="app-main-content">
        <router-view v-slot="{ Component, route }">
          <transition name="fade" mode="out-in">
            <component :is="Component" :key="route.path" />
          </transition>
        </router-view>
      </main>
      <Footer />
    </div>
  </a-config-provider>
</template>

<script setup>
import Navbar from './components/layout/Navbar.vue'; // Placeholder, will be created later
import Footer from './components/layout/Footer.vue'; // Placeholder, will be created later
import { ConfigProvider as AConfigProvider } from 'ant-design-vue';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
// import dayjs from 'dayjs'; // If using dayjs for dates with AntD
// import 'dayjs/locale/zh-cn'; // If using dayjs for dates with AntD
// dayjs.locale('zh-cn'); // If using dayjs for dates with AntD

// Global initialization logic (e.g., fetching user profile on load if token exists)
// import { useUserStore } from './store/user'; // Example with Pinia
// import { onMounted } from 'vue';

// const userStore = useUserStore();

// onMounted(() => {
//   const token = localStorage.getItem('userToken');
//   if (token && !userStore.isLoggedIn) { // Check if not already logged in/profile fetched
//     userStore.fetchUserProfile().catch(error => {
//       console.error("Failed to fetch user profile on app mount:", error);
//       // Potentially clear token if profile fetch fails due to invalid token
//       if (error.response && (error.response.status === 401 || error.response.status === 403)) {
//         localStorage.removeItem('userToken');
//         // Optionally redirect to login or show a message
//       }
//     });
//   }
// });

</script>

<style> /* Changed from scoped to global for app-level styles if needed, or keep scoped */
#app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-main-content { /* Renamed from main to avoid conflict with HTML5 main tag styles if any */
  flex: 1;
  /* padding-top: 64px; /* This should be handled by Navbar's height if it's fixed */
                       /* Or dynamically calculated if navbar height can change */
}

/* Route transition animation (optional) */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease; /* Slightly faster transition */
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
