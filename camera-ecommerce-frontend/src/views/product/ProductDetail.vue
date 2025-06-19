<template>
  <div class="product-detail-page container mt-4 mb-4">
    <a-skeleton :loading="loading" active :paragraph="{ rows: 8 }">
      <a-card v-if="product" :bordered="false" class="product-detail-card">
        <a-row :gutter="[32, 48]">
          <!-- Image Gallery -->
          <a-col :xs="24" :md="10" :lg="12" class="image-gallery-col">
            <div class="main-image-container">
              <img
                :src="activeImage || product.mainImageUrl || 'https://via.placeholder.com/600x600/ECECEC/AAAAAA?text=No+Image'"
                alt="Product Main Image"
                class="main-image"
              />
            </div>
            <a-row :gutter="[8,8]" class="thumbnail-row mt-2" v-if="allImages.length > 1">
              <a-col v-for="(imgUrl, index) in allImages" :key="index" :span="6" :xs="4">
                <div
                  class="thumbnail-wrapper"
                  :class="{ active: activeImage === imgUrl }"
                  @click="setActiveImage(imgUrl)"
                  @keypress.enter="setActiveImage(imgUrl)"
                  tabindex="0"
                  role="button"
                  :aria-label="'View image ' + (index + 1)"
                >
                  <img
                    :src="imgUrl || 'https://via.placeholder.com/100x100/ECECEC/AAAAAA?text=Thumb'"
                    :alt="'Thumbnail ' + (index + 1)"
                    class="thumbnail-image"
                  />
                </div>
              </a-col>
            </a-row>
          </a-col>

          <!-- Product Information & Actions -->
          <a-col :xs="24" :md="14" :lg="12" class="product-info-col">
            <h1 class="product-title">{{ product.name }}</h1>
            <p class="product-subtitle text-muted" v-if="product.subtitle">{{ product.subtitle }}</p>

            <div class="price-section mt-3 mb-3">
              <span class="current-price" v-if="product.discountPrice && product.discountPrice < product.price">
                ¥{{ product.discountPrice.toFixed(2) }}
              </span>
              <span class="original-price" :class="{ 'has-discount': product.discountPrice && product.discountPrice < product.price }">
                ¥{{ product.price.toFixed(2) }}
              </span>
              <a-tag color="volcano" v-if="product.discountPrice && product.discountPrice < product.price" class="ml-2">
                促销价
              </a-tag>
            </div>

            <div class="info-grid">
              <span><strong>品牌:</strong> {{ product.brand || '未知品牌' }}</span>
              <span><strong>型号:</strong> {{ product.modelNumber || 'N/A' }}</span>
              <span><strong>销量:</strong> {{ product.saleCount || 0 }} 件已售</span>
              <span><strong>库存:</strong>
                <span :class="{'low-stock': product.stock < 10 && product.stock > 0, 'no-stock': product.stock === 0}">
                  {{ product.stock > 0 ? `${product.stock} 件` : '暂无库存' }}
                </span>
              </span>
            </div>

            <a-divider />

            <div class="action-section mt-4">
              <a-form-item label="选择数量" v-if="product.stock > 0" class="quantity-form-item">
                <a-input-number v-model:value="quantity" :min="1" :max="Math.min(product.stock, 99)" size="large" />
              </a-form-item>

              <div class="buttons-group">
                <a-button
                  type="primary"
                  size="large"
                  @click="handleAddToCart"
                  :disabled="product.stock === 0 || quantity <= 0"
                  :loading="addingToCart"
                  class="action-button"
                >
                  <ShoppingCartOutlined /> 加入购物车
                </a-button>
                <a-button
                  type="danger"
                  size="large"
                  :disabled="product.stock === 0 || quantity <= 0"
                  @click="handleBuyNow"
                  class="action-button"
                >
                  <ThunderboltOutlined /> 立即购买
                </a-button>
              </div>
            </div>

            <div class="tags-section mt-4" v-if="product.tags && product.tags.length > 0">
              <strong>商品标签:</strong>
              <a-tag v-for="tag in product.tags.split(',').map(t => t.trim()).filter(t => t)" :key="tag" color="blue" class="ml-1">{{ tag }}</a-tag>
            </div>
          </a-col>
        </a-row>

        <!-- Product Description & Details Tabs -->
        <a-tabs v-model:activeKey="activeTabKey" class="mt-5 product-details-tabs">
          <a-tab-pane key="description" tab="商品详情">
            <div v-if="product.description" v-html="product.description" class="product-description-content"></div>
            <a-empty v-else description="暂无详细描述" />
          </a-tab-pane>
          <a-tab-pane key="specs" tab="规格参数">
            <a-descriptions bordered :column="{ xxl: 2, xl: 2, lg: 1, md: 1, sm: 1, xs: 1 }">
              <a-descriptions-item label="品牌">{{ product.brand || 'N/A' }}</a-descriptions-item>
              <a-descriptions-item label="型号">{{ product.modelNumber || 'N/A' }}</a-descriptions-item>
              <a-descriptions-item label="SKU">{{ product.sku || 'N/A' }}</a-descriptions-item>
              <a-descriptions-item label="库存状态">{{ product.stock > 0 ? '有货' : '缺货' }}</a-descriptions-item>
              <!-- Add more specs as needed -->
            </a-descriptions>
            <a-empty v-if="!product.brand && !product.modelNumber && !product.sku" description="暂无规格参数" class="mt-3"/>
          </a-tab-pane>
          <a-tab-pane key="reviews" tab="用户评价 (0)">
            <a-empty description="暂无用户评价" />
            <!-- TODO: Implement reviews list and submission form -->
          </a-tab-pane>
        </a-tabs>
      </a-card>
      <a-result status="404" title="商品信息加载失败" sub-title="抱歉，您访问的商品不存在或已下架。" v-else-if="!loading && !product" class="mt-5">
        <template #extra>
          <router-link to="/products"><a-button type="primary" size="large">返回商品列表</a-button></router-link>
        </template>
      </a-result>
    </a-skeleton>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Row, Col, Card, Button, InputNumber, Tag, Tabs, TabPane, Skeleton, Empty, Result, Descriptions, DescriptionsItem, message as AntMessage } from 'ant-design-vue';
import { ShoppingCartOutlined, ThunderboltOutlined } from '@ant-design/icons-vue';
// Mock service until actual one is ready
const mockProductService = {
    getProductById: async (id) => {
        console.log('Mock getProductById called with id:', id);
        await new Promise(resolve => setTimeout(resolve, 500));
        const MOCK_PRODUCT_DETAIL = {
            id: parseInt(id), name: `高端单反相机 ${id}`, price: 12999.00, discountPrice: 12500.00,
            subtitle: '捕捉精彩瞬间，专业摄影师的选择。2400万像素，4K视频录制。',
            mainImageUrl: 'https://via.placeholder.com/600x600/E8E8E8/AAAAAA?text=Main+Product+Image',
            imageUrls: JSON.stringify([ // Store as JSON string or comma-separated
                'https://via.placeholder.com/100x100/E8E8E8/AAAAAA?text=Thumb+1',
                'https://via.placeholder.com/100x100/DADADA/AAAAAA?text=Thumb+2',
                'https://via.placeholder.com/100x100/CECECE/AAAAAA?text=Thumb+3',
                'https://via.placeholder.com/100x100/C2C2C2/AAAAAA?text=Thumb+4',
            ]),
            brand: '顶级相机', modelNumber: `CAM-PRO-${id}`, sku: `SKU00${id}PRO`,
            stock: id % 2 === 0 ? 5 : 0, // Alternate stock for testing
            saleCount: 120 + parseInt(id),
            tags: '全画幅,专业,4K,高速连拍',
            description: `<p>这是一款<strong>旗舰级</strong>数码单反相机，专为追求极致画质和卓越性能的摄影爱好者及专业人士打造。</p><ul><li>2420万有效像素全画幅CMOS传感器</li><li>高性能EXPEED 6图像处理器</li><li>4K超高清视频录制能力</li><li>高达10帧/秒的连拍速度</li><li>坚固耐用的镁合金机身</li></ul><p>无论是风光、人像还是体育摄影，它都能助您轻松捕捉每一个决定性瞬间。</p><img src="https://via.placeholder.com/800x400/CCCCCC/FFFFFF?text=Sample+Detail+Image" alt="Detail" style="max-width:100%; margin-top:15px; border-radius:4px;">`
        };
        if (id === "999") return null; // Simulate not found
        return MOCK_PRODUCT_DETAIL;
    }
};
const mockCartService = {
    addItemToCart: async (item) => {
        console.log('Mock addItemToCart called with:', item);
        await new Promise(resolve => setTimeout(resolve, 300));
        // Simulate success or failure
        if (item.productId === "999") throw new Error("模拟添加到购物车失败：商品无效");
        return { success: true, cartItemId: Date.now() };
    }
};
// import { getProductById } from '@/services/product.service';
// import { addItemToCart } from '@/services/cart.service';
const getProductById = mockProductService.getProductById;
const addItemToCart = mockCartService.addItemToCart;

// Ant Design Component Aliases
const ARow = Row; const ACol = Col; const ACard = Card; const AButton = Button; const AInputNumber = InputNumber;
const ATag = Tag; const ATabs = Tabs; const ATabPane = TabPane; const ASkeleton = Skeleton; const AEmpty = Empty;
const AResult = Result; const ADescriptions = Descriptions; const ADescriptionsItem = DescriptionsItem;
const AFormItem = Form.Item;


const route = useRoute();
const router = useRouter();

const product = ref(null);
const loading = ref(true);
const addingToCart = ref(false);
const quantity = ref(1);
const activeImage = ref('');
const activeTabKey = ref('description');

const productId = computed(() => route.params.id);

const allImages = computed(() => {
  if (!product.value) return [];
  const images = [];
  if (product.value.mainImageUrl) images.push(product.value.mainImageUrl);
  if (product.value.imageUrls) {
    try {
      const parsed = JSON.parse(product.value.imageUrls);
      if (Array.isArray(parsed)) images.push(...parsed);
    } catch (e) {
      images.push(...product.value.imageUrls.split(',').map(url => url.trim()).filter(url => url));
    }
  }
  return [...new Set(images)].filter(Boolean); // Unique and non-empty URLs
});

const setActiveImage = (url) => { activeImage.value = url; };

const fetchProductDetailData = async () => {
  if (!productId.value) return;
  loading.value = true;
  product.value = null; // Reset product before fetching new one
  try {
    const data = await getProductById(productId.value);
    if (data) {
      product.value = data;
      activeImage.value = data.mainImageUrl || (allImages.value.length > 0 ? allImages.value[0] : '');
      quantity.value = data.stock > 0 ? 1 : 0; // Default to 1 if in stock
    } else {
        AntMessage.error('商品未找到或已下架。');
    }
  } catch (error) {
    console.error('获取商品详情失败:', error);
    AntMessage.error(error.response?.data?.message || '加载商品详情出错。');
  } finally {
    loading.value = false;
  }
};

onMounted(() => { fetchProductDetailData(); });

watch(productId, (newId, oldId) => {
  if (newId && newId !== oldId) {
    fetchProductDetailData();
    activeTabKey.value = 'description';
  }
});

const handleAddToCart = async () => {
  if (!localStorage.getItem('userToken')) {
    AntMessage.warning('请先登录后再将商品加入购物车。');
    router.push({ name: 'Login', query: { redirect: route.fullPath } });
    return;
  }
  if (!product.value || quantity.value <= 0) {
    AntMessage.error('请选择有效的商品数量。');
    return;
  }
  addingToCart.value = true;
  try {
    await addItemToCart({ productId: product.value.id, quantity: quantity.value });
    AntMessage.success(`${product.value.name} 已成功加入购物车!`);
    window.dispatchEvent(new CustomEvent('cart-updated')); // For Navbar cart icon update
  } catch (error) {
    AntMessage.error(error.response?.data?.message || '加入购物车失败，请稍后再试。');
  } finally {
    addingToCart.value = false;
  }
};

const handleBuyNow = () => {
  if (!localStorage.getItem('userToken')) {
    AntMessage.warning('请先登录后再进行购买操作。');
    router.push({ name: 'Login', query: { redirect: route.fullPath } });
    return;
  }
   if (product.value && product.value.id && quantity.value > 0) {
    const itemToCheckout = [{
        productId: product.value.id,
        productName: product.value.name,
        price: product.value.discountPrice !== null && product.value.discountPrice < product.value.price ? product.value.discountPrice : product.value.price,
        quantity: quantity.value,
        mainImage: product.value.mainImageUrl,
        stock: product.value.stock // Pass stock to checkout for final validation
    }];
    sessionStorage.setItem('checkoutItems', JSON.stringify(itemToCheckout)); // Use sessionStorage for temporary checkout data
    router.push({ name: 'Checkout' });
  } else {
    AntMessage.error('无法立即购买，请检查商品状态或选择数量。');
  }
};

</script>

<style scoped>
.product-detail-page .ant-card {
  border-radius: 8px;
}
.image-gallery-col .main-image-container {
    border: 1px solid #f0f0f0;
    border-radius: 6px;
    overflow: hidden;
    background-color: #fff; /* Ensure background for transparent images if any */
    display: flex;
    justify-content: center;
    align-items: center;
    height: clamp(300px, 60vh, 500px); /* Responsive height */
}
.image-gallery-col .main-image {
  max-width: 100%;
  max-height: 100%; /* Fill container */
  object-fit: contain; /* Show entire image */
}
.thumbnail-row {
  margin-top: 12px;
}
.thumbnail-wrapper {
  border: 2px solid transparent;
  border-radius: 4px;
  padding: 2px;
  cursor: pointer;
  transition: border-color 0.3s, transform 0.2s;
  background-color: #fff;
}
.thumbnail-wrapper:hover {
  border-color: #E0E0E0; /* Lighter Ant Design primary color */
  transform: scale(1.05);
}
.thumbnail-wrapper.active {
  border-color: #00b96b; /* Primary color from App.vue */
}
.thumbnail-image {
  width: 100%;
  height: 80px; /* Fixed height for thumbnails */
  object-fit: cover;
  border-radius: 2px; /* Inner image radius */
}

.product-title {
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 8px;
  line-height: 1.3;
}
.product-subtitle {
  font-size: 16px;
  color: #757575;
  margin-bottom: 20px;
}
.price-section .current-price {
  font-size: 30px;
  color: #ff4d4f;
  font-weight: bold;
  margin-right: 12px;
}
.price-section .original-price {
  font-size: 18px;
  color: #aaa;
  text-decoration: line-through;
}
.price-section .original-price.has-discount {
  /* Style for original price when there's a discount */
}
.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 10px;
    margin-top: 16px;
    color: #555;
    font-size: 0.95em;
}
.info-grid span {
    padding: 5px 0;
}
.low-stock { color: #faad14; /* Ant Design warning color */ font-weight: bold; }
.no-stock { color: #f5222d; /* Ant Design error color */ font-weight: bold; }

.action-section .quantity-form-item {
    margin-bottom: 20px;
}
.action-section .buttons-group {
    display: flex;
    gap: 12px; /* Space between buttons */
    flex-wrap: wrap; /* Allow buttons to wrap on small screens */
}
.action-section .action-button {
    flex-grow: 1; /* Allow buttons to share space */
    min-width: 150px; /* Minimum width for buttons */
}

.tags-section .ant-tag {
  margin-top: 8px;
  margin-right: 8px;
}
.product-details-tabs {
    border-top: 1px solid #f0f0f0;
    padding-top: 20px;
}
.product-description-content :deep(img) { /* Style images inside v-html */
    max-width: 100%;
    height: auto;
    margin-top: 10px;
    margin-bottom: 10px;
    border-radius: 4px;
}
.ml-1 { margin-left: 4px !important; }
.ml-2 { margin-left: 8px !important; }
.mt-2 { margin-top: 8px !important; }
.mt-3 { margin-top: 16px !important; }
.mt-4 { margin-top: 24px !important; }
.mt-5 { margin-top: 32px !important; }
.mb-3 { margin-bottom: 16px !important; }
.mb-4 { margin-bottom: 24px !important; }
</style>
