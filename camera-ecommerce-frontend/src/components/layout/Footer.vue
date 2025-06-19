<template>
  <a-layout-footer class="footer">
    <div class="container footer-content">
      <div class="footer-links">
        <a href="/about-us" target="_blank" rel="noopener noreferrer">关于我们</a>
        <a href="/contact" target="_blank" rel="noopener noreferrer">联系我们</a>
        <a href="/privacy-policy" target="_blank" rel="noopener noreferrer">隐私政策</a>
        <a href="/terms-of-service" target="_blank" rel="noopener noreferrer">服务条款</a>
      </div>
      <div class="copyright">
        &copy; {{ currentYear }} 相机商城购物系统. 保留所有权利.
      </div>
      <div class="footer-social" v-if="false"> <!-- Hidden for now, can be enabled later -->
        <!-- Placeholder for social media icons -->
        <a href="#" aria-label="Facebook"><FacebookOutlined /></a>
        <a href="#" aria-label="Twitter"><TwitterOutlined /></a>
        <a href="#" aria-label="Instagram"><InstagramOutlined /></a>
      </div>
    </div>
  </a-layout-footer>
</template>

<script setup>
import { ref } from 'vue';
import { LayoutFooter } from 'ant-design-vue';
// import { FacebookOutlined, TwitterOutlined, InstagramOutlined } from '@ant-design/icons-vue'; // If social icons are needed

const currentYear = ref(new Date().getFullYear());

// Expose Ant Design component for template usage (though <script setup> makes it available automatically)
const ALayoutFooter = LayoutFooter;
</script>

<style scoped>
.footer {
  background-color: #001529; /* Dark background to match navbar */
  color: rgba(255, 255, 255, 0.65); /* Lighter text color for dark background */
  text-align: center;
  padding: 30px 0; /* Increased padding */
  /* border-top: 1px solid #303030; */ /* Darker border for dark theme */
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-links {
  margin-bottom: 15px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
  margin: 0 12px; /* Slightly more margin */
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #1890ff; /* Ant Design primary blue on hover */
}

.copyright {
  font-size: 0.9em;
  margin-bottom: 10px;
}

.footer-social {
  margin-top: 15px;
}
.footer-social a {
  color: rgba(255, 255, 255, 0.65);
  margin: 0 10px;
  font-size: 1.2em; /* Larger icons */
  transition: color 0.3s;
}
.footer-social a:hover {
  color: #1890ff;
}
</style>
