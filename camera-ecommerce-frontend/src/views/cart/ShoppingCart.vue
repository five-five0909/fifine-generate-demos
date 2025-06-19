<template>
  <div class="shopping-cart-page container mt-4 mb-4">
    <a-card title="我的购物车" :bordered="false" class="cart-card">
      <div v-if="loading" class="loading-placeholder">
        <a-spin size="large" tip="正在加载购物车..." />
      </div>
      <a-empty v-else-if="!cartItems || cartItems.length === 0" description="您的购物车还是空的，快去挑选喜爱的商品吧！" class="empty-cart">
        <router-link to="/products"><a-button type="primary" size="large">立即去逛逛</a-button></router-link>
      </a-empty>
      <div v-else class="cart-content">
        <a-list item-layout="horizontal" :data-source="cartItems" class="cart-item-list">
          <template #header>
            <a-row class="cart-list-header" :gutter="16">
              <a-col :span="2" class="text-center"><a-checkbox :checked="isAllSelected" :indeterminate="isIndeterminate" @change="handleSelectAllChange" /></a-col>
              <a-col :span="10">商品信息</a-col>
              <a-col :span="4" class="text-center">单价</a-col>
              <a-col :span="4" class="text-center">数量</a-col>
              <a-col :span="3" class="text-right">小计</a-col>
              <a-col :span="1" class="text-center">操作</a-col>
            </a-row>
          </template>
          <template #renderItem="{ item }">
            <a-list-item class="cart-item">
              <a-row :gutter="16" align="middle" style="width:100%">
                <a-col :span="2" class="text-center item-checkbox-col">
                    <a-checkbox
                        :checked="item.checkedStatus === 1"
                        @change="(e) => handleCheckedChange(item, e.target.checked)"
                        :disabled="updatingItemId === item.id"
                    />
                </a-col>
                <a-col :span="10" class="item-meta-col">
                    <a-list-item-meta>
                        <template #avatar>
                        <router-link :to="`/products/${item.productId}`">
                            <img :src="item.productMainImage || 'https://via.placeholder.com/80x80/ECECEC/AAAAAA?text=No+Image'" alt="商品图片" class="cart-item-image" />
                        </router-link>
                        </template>
                        <template #title>
                        <router-link :to="`/products/${item.productId}`" class="product-name">{{ item.productName || '商品名称加载中...' }}</router-link>
                        </template>
                        <template #description>
                        <span class="item-stock">库存: {{ item.currentStock }} 件</span>
                        </template>
                    </a-list-item-meta>
                </a-col>
                <a-col :span="4" class="text-center item-price-col">
                    ¥{{ (item.currentPrice || item.priceAtAddition || 0).toFixed(2) }}
                </a-col>
                <a-col :span="4" class="text-center item-quantity-col">
                    <a-input-number
                    :value="item.quantity"
                    :min="1"
                    :max="item.currentStock || 1"
                    @change="(value) => handleQuantityChange(item, value)"
                    :disabled="updatingItemId === item.id || item.currentStock === 0"
                    size="small"
                    />
                </a-col>
                <a-col :span="3" class="text-right item-subtotal-col">
                    <strong>¥{{ ((item.currentPrice || item.priceAtAddition || 0) * item.quantity).toFixed(2) }}</strong>
                </a-col>
                <a-col :span="1" class="text-center item-actions-col">
                    <a-popconfirm
                    title="确定要删除这个商品吗?"
                    ok-text="确定"
                    cancel-text="取消"
                    @confirm="() => handleRemoveItem(item.id)"
                    >
                    <DeleteOutlined class="delete-icon" />
                    </a-popconfirm>
                </a-col>
              </a-row>
            </a-list-item>
          </template>
        </a-list>

        <a-divider />

        <div class="cart-summary-bar">
          <a-row justify="space-between" align="middle">
            <a-col :xs="24" :sm="12" :md="10" class="summary-actions">
              <a-checkbox
                :checked="isAllSelected"
                :indeterminate="isIndeterminate"
                @change="handleSelectAllChange"
              >
                全选 ({{ selectedItemsCount }} / {{ cartItems.length }})
              </a-checkbox>
               <a-popconfirm
                title="确定删除所有选中的商品吗?"
                ok-text="确定删除"
                cancel-text="取消"
                @confirm="handleClearSelected"
                :disabled="selectedItemsCount === 0"
               >
                <a-button type="link" danger :disabled="selectedItemsCount === 0">删除选中</a-button>
               </a-popconfirm>
            </a-col>
            <a-col :xs="24" :sm="12" :md="14" class="summary-totals">
              <span class="total-price-label">合计 (不含运费):</span>
              <span class="total-price-value">¥{{ selectedTotalPrice.toFixed(2) }}</span>
              <a-button
                type="danger"
                size="large"
                class="checkout-button"
                @click="handleCheckout"
                :disabled="selectedItemsCount === 0 || submittingCheckout"
                :loading="submittingCheckout"
              >
                去结算 ({{ selectedItemsCount }}件)
              </a-button>
            </a-col>
          </a-row>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { List, ListItem, ListItemMeta, InputNumber, Button, Checkbox, Divider, Row, Col, Popconfirm, Empty, Spin, message as AntMessage } from 'ant-design-vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getCartItems, updateCartItem, deleteCartItem } from '@/services/cart.service'; // Assuming clearCart is not used for selected

const AList = List; const AListItem = List.Item; const AListItemMeta = List.Item.Meta;
const AInputNumber = InputNumber; const AButton = Button; const ACheckbox = Checkbox;
const ADivider = Divider; const ARow = Row; const ACol = Col; const APopconfirm = Popconfirm;
const AEmpty = Empty; const ASpin = Spin;

const router = useRouter();
const cartItems = ref([]);
const loading = ref(true);
const updatingItemId = ref(null);
const submittingCheckout = ref(false);


const fetchCartData = async () => {
  loading.value = true;
  try {
    const response = await getCartItems();
    cartItems.value = response || [];
  } catch (error) {
    console.error('获取购物车数据失败:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchCartData);

const handleQuantityChange = async (item, newQuantity) => {
  if (newQuantity === null || newQuantity === undefined || newQuantity < 1) {
      if (newQuantity === 0) handleRemoveItem(item.id); // If quantity becomes 0, remove item
      return;
  }
  if (newQuantity > item.currentStock) {
      AntMessage.warning(`最多可购买 ${item.currentStock} 件`);
      // Revert UI or keep it, backend will validate. For now, let it be, API call will fail or adjust.
      // item.quantity = item.currentStock; // Revert visually - this might not be ideal if API call fails
      return;
  }

  updatingItemId.value = item.id;
  try {
    const updatedItemBackend = await updateCartItem(item.id, { quantity: newQuantity });
    const index = cartItems.value.findIndex(i => i.id === item.id);
    if (index !== -1) {
      // Merge backend response with existing item to preserve any frontend-only properties if needed
      cartItems.value[index] = { ...cartItems.value[index], ...updatedItemBackend };
    }
    AntMessage.success('数量更新成功');
  } catch (error) {
    fetchCartData(); // Re-fetch to ensure data consistency on error
  } finally {
    updatingItemId.value = null;
  }
};

const handleCheckedChange = async (item, checked) => {
  updatingItemId.value = item.id;
  try {
    const updatedItemBackend = await updateCartItem(item.id, { checkedStatus: checked ? 1 : 0 });
    const index = cartItems.value.findIndex(i => i.id === item.id);
    if (index !== -1) {
      cartItems.value[index] = { ...cartItems.value[index], ...updatedItemBackend };
    }
  } catch (error) {
    fetchCartData();
  } finally {
    updatingItemId.value = null;
  }
};

const handleRemoveItem = async (itemId) => {
  try {
    await deleteCartItem(itemId);
    cartItems.value = cartItems.value.filter(item => item.id !== itemId);
    AntMessage.success('商品已成功从购物车移除');
  } catch (error) {
    // Error message handled by interceptor
  }
};

const selectedItems = computed(() => cartItems.value.filter(item => item.checkedStatus === 1));
const selectedItemsCount = computed(() => selectedItems.value.length);
const selectedTotalPrice = computed(() => {
  return selectedItems.value.reduce((total, item) => {
    return total + (item.currentPrice || item.priceAtAddition || 0) * item.quantity;
  }, 0);
});

const isAllSelected = computed({
  get: () => cartItems.value.length > 0 && selectedItemsCount.value === cartItems.value.length,
  set: async (value) => {
    const newCheckedStatus = value ? 1 : 0;
    // To avoid multiple re-renders and ensure atomicity, ideally backend supports batch update.
    // Frontend optimistic update, then batch API call.
    const itemsToUpdate = cartItems.value.filter(item => item.checkedStatus !== newCheckedStatus);
    if (itemsToUpdate.length === 0) return;

    // Optimistic UI update
    itemsToUpdate.forEach(item => item.checkedStatus = newCheckedStatus);

    try {
        // Example: one by one, replace with batch if available
        for (const item of itemsToUpdate) {
            await updateCartItem(item.id, { checkedStatus: newCheckedStatus });
        }
    } catch (error) {
        AntMessage.error('更新全选状态失败，正在刷新购物车...');
        fetchCartData(); // Revert on error
    }
  }
});

const isIndeterminate = computed(() => {
  return selectedItemsCount.value > 0 && selectedItemsCount.value < cartItems.value.length;
});

const handleClearSelected = async () => {
    const itemsToDelete = selectedItems.value.map(item => item.id);
    if (itemsToDelete.length === 0) return;

    // Optimistic UI update
    const originalItems = [...cartItems.value];
    cartItems.value = cartItems.value.filter(item => !itemsToDelete.includes(item.id));

    try {
        // Example: one by one, replace with batch if available
        for (const itemId of itemsToDelete) {
            await deleteCartItem(itemId);
        }
        AntMessage.success('选中的商品已删除');
    } catch (error) {
        AntMessage.error('删除选中商品失败，正在恢复...');
        cartItems.value = originalItems; // Revert on error
    }
};

const handleCheckout = () => {
  if (selectedItemsCount.value === 0) {
    AntMessage.warning('请至少选择一件商品进行结算。');
    return;
  }
  submittingCheckout.value = true;
  // Pass selected items to checkout page via session storage or state management
  sessionStorage.setItem('checkoutItemsFromCart', JSON.stringify(selectedItems.value));
  router.push({ name: 'Checkout' }).finally(() => {
      submittingCheckout.value = false;
  });
};

</script>

<style scoped>
.cart-card {
  border-radius: 8px;
}
.loading-placeholder, .empty-cart {
  padding: 40px 0;
  text-align: center;
}
.cart-item-list .ant-list-item {
  padding: 16px 0; /* Adjust item padding */
}
.cart-list-header {
  font-weight: bold;
  color: #555;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 8px;
}
.cart-item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}
.product-name {
  font-size: 15px;
  color: #333;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}
.item-stock { font-size: 12px; color: #888; margin-top: 4px; display: block;}
.item-price-col, .item-quantity-col, .item-subtotal-col, .item-actions-col, .item-checkbox-col {
  display: flex;
  align-items: center;
  justify-content: center;
}
.item-subtotal-col { justify-content: flex-end; }
.delete-icon { color: #ff4d4f; cursor: pointer; font-size: 16px; }
.delete-icon:hover { color: #d9363e; }

.cart-summary-bar {
  margin-top: 24px;
  padding: 20px;
  background-color: #f7f7f7;
  border-radius: 6px;
}
.summary-actions .ant-checkbox-wrapper {
    margin-right: 16px;
}
.summary-totals {
  text-align: right;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.total-price-label {
  font-size: 16px;
  margin-right: 8px;
  color: #555;
}
.total-price-value {
  font-size: 22px;
  font-weight: bold;
  color: #ff4d4f;
  margin-right: 16px;
}
.checkout-button {
    min-width: 150px;
}

/* Responsive adjustments */
@media (max-width: 767px) {
    .cart-list-header { display: none; } /* Hide header on small screens, rely on item layout */
    .cart-item .ant-col { margin-bottom: 8px; }
    .item-quantity-col, .item-subtotal-col, .item-actions-col { justify-content: flex-start; text-align: left; }
    .item-subtotal-col { margin-top: 8px; }
    .summary-totals { flex-direction: column; align-items: flex-end; gap: 10px; }
    .checkout-button { width: 100%; }
}
.text-center { text-align: center; }
.text-right { text-align: right; }
.mt-4 { margin-top: 24px !important; }
.mb-4 { margin-bottom: 24px !important; }
.p-5 { padding: 40px !important; }
</style>
