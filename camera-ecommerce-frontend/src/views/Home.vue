<template>
  <div class="home-page">
    <!-- 1. Hero Section/Carousel -->
    <a-carousel autoplay class="hero-carousel" effect="fade">
      <div v-for="slide in heroSlides" :key="slide.id" class="carousel-slide">
        <img :src="slide.imageUrl" :alt="slide.altText" />
        <div class="carousel-caption">
          <h2>{{ slide.title }}</h2>
          <p>{{ slide.subtitle }}</p>
          <router-link :to="slide.link" v-if="slide.link">
            <a-button type="primary" size="large">{{ slide.buttonText || '了解更多' }}</a-button>
          </router-link>
        </div>
      </div>
    </a-carousel>

    <!-- 2. Featured Categories -->
    <section class="categories-section container mt-5 mb-5">
      <h2 class="section-title"><span>探索热门分类</span></h2>
      <a-row :gutter="[16, 24]">
        <a-col v-for="category in featuredCategories" :key="category.id" :xs="12" :sm="8" :md="6" :lg="4">
          <router-link :to="`/products?categoryId=${category.id}`">
            <a-card hoverable class="category-card">
              <template #cover>
                <img :alt="category.name" :src="category.iconUrl || `https://via.placeholder.com/200x150/ECECEC/AAAAAA?text=${category.name}`" class="category-img"/>
              </template>
              <a-card-meta>
                <template #title>
                  <div class="category-title">{{ category.name }}</div>
                </template>
              </a-card-meta>
            </a-card>
          </router-link>
        </a-col>
         <a-col v-if="featuredCategories.length === 0 && !categoriesLoading" :span="24" class="text-center">
            <a-empty description="暂无分类信息" />
        </a-col>
        <a-col v-if="categoriesLoading" :span="24" class="text-center">
            <a-spin />
        </a-col>
      </a-row>
    </section>

    <!-- 3. Recommended Products -->
    <section class="featured-products-section container mt-5 mb-5">
      <h2 class="section-title"><span>编辑推荐</span></h2>
      <a-row :gutter="[16, 24]">
        <a-col v-for="product in recommendedProducts" :key="product.id" :xs="24" :sm="12" :md="8" :lg="6">
          <ProductCard :product="product" />
        </a-col>
      </a-row>
       <div class="text-center mt-4" v-if="recommendedProducts.length === 0 && !productsLoading">
         <a-empty description="暂无推荐商品" />
      </div>
       <div class="text-center mt-4" v-if="productsLoading">
         <a-spin />
      </div>
      <div class="text-center mt-4" v-if="recommendedProducts.length > 3">
        <router-link to="/products">
          <a-button type="default" size="large">查看所有商品 <RightOutlined /></a-button>
        </router-link>
      </div>
    </section>

    <!-- 4. Promotional Banner/Call to Action (Optional) -->
    <section class="promo-banner-section mt-5 mb-5" v-if="promoBanner.visible">
      <div class="container">
        <a-alert
          :message="promoBanner.title"
          :description="promoBanner.description"
          :type="promoBanner.type || 'info'"
          show-icon
          closable
          @close="() => promoBanner.visible = false"
        >
          <template #icon><NotificationOutlined /></template>
        </a-alert>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { Carousel, Row, Col, Card, Button, Alert, Empty, Spin, message as AntMessage } from 'ant-design-vue';
import { RightOutlined, NotificationOutlined } from '@ant-design/icons-vue';
import ProductCard from '@/components/product/ProductCard.vue';
// Mock services until they are created
const mockCategoryService = {
    getCategories: async (params) => {
        console.log('Mock getCategories called with:', params);
        await new Promise(resolve => setTimeout(resolve, 500)); // Simulate network delay
        // Return mock data structure similar to what backend might send
        if (params && params.parentId === 0) {
             return { records: [
                { id: 1, name: '单反相机', iconUrl: 'https://via.placeholder.com/200x150/87d068/FFF?text=DSLR' },
                { id: 2, name: '微单相机', iconUrl: 'https://via.placeholder.com/200x150/108ee9/FFF?text=Mirrorless' },
                { id: 3, name: '运动相机', iconUrl: 'https://via.placeholder.com/200x150/f50/FFF?text=ActionCam' },
                { id: 4, name: '镜头配件', iconUrl: 'https://via.placeholder.com/200x150/ffbf00/FFF?text=Lenses' },
                { id: 5, name: '三脚架', iconUrl: 'https://via.placeholder.com/200x150/2db7f5/FFF?text=Tripods' },
                { id: 6, name: '无人机', iconUrl: 'https://via.placeholder.com/200x150/eb2f96/FFF?text=Drones' },
            ]};
        }
        return { records: [] };
    }
};
const mockProductService = {
    getProducts: async (params) => {
        console.log('Mock getProducts called with:', params);
        await new Promise(resolve => setTimeout(resolve, 800));
        return { records: [
            { id: 101, name: '尼康 Z7 II 全画幅微单', price: 18999, mainImageUrl: 'https://via.placeholder.com/300x200/A3D1FF/000?text=Nikon+Z7+II', discountPrice: 18500, brand: '尼康', stock: 15 },
            { id: 102, name: '索尼 Alpha 7 IV 全画幅微单', price: 16999, mainImageUrl: 'https://via.placeholder.com/300x200/FFC8A3/000?text=Sony+A7+IV', brand: '索尼', stock: 8 },
            { id: 103, name: '佳能 EOS R6 全画幅微单', price: 15999, mainImageUrl: 'https://via.placeholder.com/300x200/FFACA3/000?text=Canon+EOS+R6', brand: '佳能', stock: 0 }, // Out of stock example
            { id: 104, name: 'GoPro HERO12 Black 运动相机', price: 3298, mainImageUrl: 'https://via.placeholder.com/300x200/A3FFAE/000?text=GoPro+HERO12', brand: 'GoPro', stock: 22 },
        ], total: 4 };
    }
};
// import { getCategories } from '@/services/category.service'; // TODO: Uncomment when service exists
// import { getProducts } from '@/services/product.service';   // TODO: Uncomment when service exists
const getCategories = mockCategoryService.getCategories; // Using mock
const getProducts = mockProductService.getProducts;     // Using mock


// Ant Design Component Aliases
const ACarousel = Carousel;
const ARow = Row;
const ACol = Col;
const ACard = Card;
const ACardMeta = Card.Meta;
const AButton = Button;
const AAlert = Alert;
const AEmpty = Empty;
const ASpin = Spin;

const heroSlides = ref([
  { id: 1, imageUrl: 'https://via.placeholder.com/1600x500/0052cc/ffffff?text=探索摄影新境界', altText: '相机促销1', title: '探索摄影新境界', subtitle: '新款旗舰相机，限时优惠进行中！', link: '/products/101', buttonText: '立即抢购' },
  { id: 2, imageUrl: 'https://via.placeholder.com/1600x500/003366/ffffff?text=捕捉生活每一刻', altText: '相机促销2', title: '捕捉生活每一刻', subtitle: '高品质镜头与配件，满足您的所有需求。', link: '/products?categoryId=4', buttonText: '选购配件' },
]);

const featuredCategories = ref([]);
const categoriesLoading = ref(false);
const recommendedProducts = ref([]);
const productsLoading = ref(false);

const promoBanner = reactive({
  visible: true,
  title: '迎新春摄影大赛火热进行中！',
  description: '上传您的得意之作，赢取万元大奖与最新款相机。活动截止日期：2024年2月28日。',
  type: 'info', // 'success', 'info', 'warning', 'error'
});

const fetchFeaturedCategories = async () => {
  categoriesLoading.value = true;
  try {
    const response = await getCategories({ parentId: 0, pageSize: 6 }); // pageSize might be 'size'
    featuredCategories.value = response.records || response;
  } catch (error) {
    console.error('获取热门分类失败:', error.response?.data?.message || error.message);
    // AntMessage.error('获取热门分类失败'); // Usually handled by request interceptor
  } finally {
    categoriesLoading.value = false;
  }
};

const fetchRecommendedProducts = async () => {
  productsLoading.value = true;
  try {
    const response = await getProducts({ current: 1, size: 4, /* isRecommended: true */ }); // Assuming 4 recommendations
    recommendedProducts.value = response.records || [];
  } catch (error) {
    console.error('获取推荐商品失败:', error.response?.data?.message || error.message);
    // AntMessage.error('获取推荐商品失败');
  } finally {
    productsLoading.value = false;
  }
};

onMounted(() => {
  fetchFeaturedCategories();
  fetchRecommendedProducts();
});
</script>

<style scoped>
.home-page {
  /* background-color: #f0f2f5; */ /* Optional: if you want a global bg color for sections */
}

.hero-carousel .carousel-slide {
  position: relative;
  width: 100%;
  height: 500px; /* Adjust height as needed */
}
.hero-carousel .carousel-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.hero-carousel .carousel-caption {
  position: absolute;
  bottom: 50px;
  left: 50%;
  transform: translateX(-50%);
  color: white;
  text-align: center;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 20px 30px;
  border-radius: 8px;
}
.hero-carousel .carousel-caption h2 {
  font-size: 2.5em;
  color: white;
  margin-bottom: 10px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.7);
}
.hero-carousel .carousel-caption p {
  font-size: 1.2em;
  margin-bottom: 20px;
}

.section-title {
  font-size: 28px;
  font-weight: 600; /* Semibold */
  margin-bottom: 30px;
  text-align: center;
  position: relative; /* For pseudo-element line */
  padding-bottom: 10px;
}
.section-title span {
  position: relative;
  z-index: 1;
}
.section-title::after { /* Underline effect */
  content: '';
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  width: 80px;
  height: 4px;
  background-color: #00b96b; /* Primary color from App.vue */
  border-radius: 2px;
}


.category-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border-radius: 8px;
  overflow: hidden; /* Ensure cover image respects border radius */
}
.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}
.category-card .category-img {
  height: 150px;
  object-fit: cover;
  /* padding: 10px; */ /* Removed padding for cover effect */
}
.category-card .category-title {
  text-align: center;
  font-size: 1.1em;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 8px 0; /* Add padding inside meta if needed */
}
.category-card .ant-card-body {
    padding: 12px; /* Adjust padding for card body */
}

.promo-banner-section .ant-alert {
  border-radius: 8px;
  padding: 15px 20px; /* Custom padding */
}
.promo-banner-section .ant-alert-message {
    font-size: 1.1em;
    font-weight: 500;
}

.mt-5 { margin-top: 3rem !important; }
.mb-5 { margin-bottom: 3rem !important; }
</style>
