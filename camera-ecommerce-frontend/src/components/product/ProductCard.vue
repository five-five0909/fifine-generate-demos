<template>
  <router-link :to="`/products/${product.id}`" class="product-card-link" :aria-label="`查看 ${product.name} 详情`">
    <a-card hoverable class="product-card" :tabindex="0" @keypress.enter="viewDetailHandler">
      <template #cover>
        <div class="product-image-wrapper">
          <img
            :alt="product.name"
            :src="product.mainImageUrl || 'https://via.placeholder.com/300x200/ECECEC/AAAAAA?text=No+Image'"
            class="product-image"
            @error="onImageError"
          />
          <a-tag v-if="product.stock === 0" class="out-of-stock-tag" color="grey">暂无库存</a-tag>
          <a-tag v-else-if="product.discountPrice && product.discountPrice < product.price" class="promo-tag" color="volcano">促销</a-tag>
        </div>
      </template>
      <a-card-meta>
        <template #title>
          <div class="product-name" :title="product.name">{{ product.name }}</div>
        </template>
        <template #description>
          <div class="product-info">
            <div class="product-price">
              <span class="current-price" v-if="product.discountPrice && product.discountPrice < product.price">
                ¥{{ product.discountPrice.toFixed(2) }}
              </span>
              <span class="original-price" :class="{ 'has-discount': product.discountPrice && product.discountPrice < product.price }">
                ¥{{ product.price.toFixed(2) }}
              </span>
            </div>
            <div class="product-sales">已售 {{ product.saleCount || 0 }} 件</div>
          </div>
        </template>
      </a-card-meta>
      <template #actions>
        <a-tooltip title="加入购物车">
          <div @click.prevent.stop="addToCartHandler" role="button" :aria-label="`将 ${product.name} 加入购物车`" tabindex="0" @keypress.enter="addToCartHandler">
            <ShoppingCartOutlined key="add-to-cart" />
          </div>
        </a-tooltip>
        <a-tooltip title="查看详情">
           <div @click.prevent.stop="viewDetailHandler" role="button" :aria-label="`查看 ${product.name} 详细信息`" tabindex="0" @keypress.enter="viewDetailHandler">
            <EyeOutlined key="view-detail" />
          </div>
        </a-tooltip>
        <a-tooltip title="收藏商品 (暂不可用)">
            <div role="button" aria-label="收藏商品" tabindex="0" style="cursor: not-allowed; color: #ccc;">
                <HeartOutlined key="favorite" />
            </div>
        </a-tooltip>
      </template>
    </a-card>
  </router-link>
</template>

<script setup>
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';
import { Card, Tooltip, Tag, message as AntMessage } from 'ant-design-vue';
import { ShoppingCartOutlined, EyeOutlined, HeartOutlined } from '@ant-design/icons-vue';
import { addItemToCart } from '@/services/cart.service'; // Assuming this service exists

const ACard = Card;
const ACardMeta = Card.Meta;
const ATooltip = Tooltip;
const ATag = Tag;

const props = defineProps({
  product: {
    type: Object,
    required: true,
    default: () => ({
        id:0,
        name: '商品加载中...',
        mainImageUrl: '',
        price: 0,
        discountPrice: null,
        saleCount: 0,
        stock: 0,
    })
  }
});

const router = useRouter();

const onImageError = (event) => {
  event.target.src = 'https://via.placeholder.com/300x200/ECECEC/AAAAAA?text=Image+Error'; // Fallback image
};

const addToCartHandler = async () => {
  if (!localStorage.getItem('userToken')) {
    AntMessage.warning('请先登录后再将商品加入购物车。');
    router.push({ name: 'Login', query: { redirect: `/products/${props.product.id}` } });
    return;
  }
  if (props.product.stock === 0) {
      AntMessage.warning('此商品当前暂无库存。');
      return;
  }
  try {
    // Assuming addItemToCart service handles loading state if it's long
    await addItemToCart({ productId: props.product.id, quantity: 1 });
    AntMessage.success(`"${props.product.name}" 已成功加入购物车!`);
    window.dispatchEvent(new CustomEvent('cart-updated')); // Notify navbar or other components
  } catch (error) {
    // Error message is handled by the request interceptor
    console.error('Failed to add to cart:', error);
  }
};

const viewDetailHandler = () => {
    if (props.product && props.product.id) {
        router.push(`/products/${props.product.id}`);
    } else {
        console.error("Product ID is missing, cannot navigate to detail.");
        AntMessage.error("无法查看商品详情，商品信息不完整。");
    }
};

</script>

<style scoped>
.product-card-link {
  text-decoration: none;
  color: inherit;
  display: block; /* Ensure the link wraps the card properly */
}
.product-card {
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
  height: 100%; /* Make card fill column height if in a grid */
  display: flex;
  flex-direction: column;
}
.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.12);
}
.product-image-wrapper {
    position: relative;
    width: 100%;
    padding-top: 75%; /* Aspect ratio 4:3 for the image (height will be 75% of width) */
    overflow: hidden;
}
.product-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.product-card:hover .product-image {
    transform: scale(1.05);
}
.promo-tag, .out-of-stock-tag {
    position: absolute;
    top: 10px;
    left: 10px;
    font-size: 12px;
    border-radius: 3px;
}

.product-card :deep(.ant-card-body) {
    padding: 16px; /* Adjust padding for content */
    flex-grow: 1; /* Allow body to grow and push actions down */
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.product-name {
  font-size: 1em; /* Relative to card font size */
  font-weight: 500;
  color: #333;
  margin-bottom: 6px;
  /* Multi-line ellipsis */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 2.8em; /* Approx 2 lines height */
  line-height: 1.4em;
}
.product-info {
    flex-grow: 1; /* Pushes sales to bottom if name is short */
}
.product-price {
  margin-bottom: 4px;
}
.product-price .current-price {
  font-size: 1.1em;
  color: #ff4d4f;
  font-weight: bold;
  margin-right: 6px;
}
.product-price .original-price {
  font-size: 0.85em;
  color: #aaa;
  text-decoration: line-through;
}
.product-sales {
  font-size: 0.8em;
  color: #888;
}

.ant-card-actions {
    background: #fcfcfc; /* Slightly different background for actions */
    border-top: 1px solid #f0f0f0;
}
.ant-card-actions > li {
    padding: 8px 0; /* Smaller padding for actions */
}
.ant-card-actions > li > span > .anticon {
  font-size: 18px; /* Icon size */
  color: #555;
  transition: color 0.3s;
}
.ant-card-actions > li > span:hover > .anticon {
  color: #00b96b; /* Primary color from App.vue */
}
</style>
