<template>
  <div class="order-detail-page container mt-4 mb-4">
    <a-page-header title="订单详情" @back="() => router.go(-1)" class="page-header-custom">
      <template #subTitle>订单号: {{ order?.orderSn || '加载中...' }}</template>
      <template #extra>
        <a-button v-if="order && order.orderStatus === 0" type="primary" danger @click="() => goToPayment(order.id)" :loading="actionLoading">立即支付</a-button>
        <a-popconfirm
            v-if="order && order.orderStatus === 0"
            title="确定要取消这个订单吗?"
            ok-text="确定取消"
            cancel-text="再想想"
            @confirm="() => handleCancelOrder(order.id)"
        >
            <a-button class="ml-2" :loading="actionLoading">取消订单</a-button>
        </a-popconfirm>
        <a-button v-if="order && order.orderStatus === 2" type="primary" @click="() => confirmReceipt(order.id)" :loading="actionLoading">确认收货</a-button>
      </template>
    </a-page-header>

    <a-skeleton :loading="loading" active :paragraph="{ rows: 10 }">
      <a-card v-if="order" :bordered="false" class="order-detail-card">
        <a-descriptions title="订单信息" bordered :column="{ xxl: 2, xl: 2, lg: 2, md: 1, sm: 1, xs: 1 }" class="mb-4">
          <a-descriptions-item label="订单号">{{ order.orderSn }}</a-descriptions-item>
          <a-descriptions-item label="订单状态">
            <a-tag :color="getOrderStatusColor(order.orderStatus)">{{ getOrderStatusText(order.orderStatus) }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="下单时间">{{ formatDateTime(order.createdAt) }}</a-descriptions-item>
          <a-descriptions-item label="支付方式">{{ getPaymentMethodText(order.paymentMethod) }}</a-descriptions-item>
          <a-descriptions-item label="支付状态">{{ getPaymentStatusText(order.paymentStatus) }}</a-descriptions-item>
          <a-descriptions-item label="支付时间">{{ order.paymentTime ? formatDateTime(order.paymentTime) : '未支付' }}</a-descriptions-item>
           <a-descriptions-item label="发货状态">{{ getShippingStatusText(order.shippingStatus) }}</a-descriptions-item>
          <a-descriptions-item label="发货时间">{{ order.shippingTime ? formatDateTime(order.shippingTime) : '未发货' }}</a-descriptions-item>
          <a-descriptions-item label="完成时间">{{ order.receiveTime ? formatDateTime(order.receiveTime) : '未完成' }}</a-descriptions-item>
        </a-descriptions>

        <a-descriptions title="收货信息" bordered :column="1" class="mb-4">
          <a-descriptions-item label="收货人">{{ order.receiverName }}</a-descriptions-item>
          <a-descriptions-item label="联系电话">{{ order.receiverPhone }}</a-descriptions-item>
          <a-descriptions-item label="收货地址" :span="2">{{ order.receiverAddress }}</a-descriptions-item>
          <a-descriptions-item label="订单备注">{{ order.remarks || '无' }}</a-descriptions-item>
        </a-descriptions>

        <h3 class="section-title">商品列表</h3>
        <a-list item-layout="horizontal" :data-source="orderItems" class="order-item-list">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-list-item-meta>
                <template #avatar>
                   <router-link :to="`/products/${item.productId}`">
                    <img :src="item.productImageUrl || 'https://via.placeholder.com/80x80/ECECEC/AAAAAA?text=No+Image'" alt="商品图片" class="product-image" />
                   </router-link>
                </template>
                <template #title>
                    <router-link :to="`/products/${item.productId}`" class="product-name-link">{{ item.productName }}</router-link>
                </template>
                <template #description>
                  <span class="product-sku">SKU: {{ item.productSku || 'N/A' }}</span>
                </template>
              </a-list-item-meta>
              <div class="item-price-quantity">
                <div>¥{{ (item.unitPrice || 0).toFixed(2) }}</div>
                <div>x {{ item.quantity }}</div>
              </div>
              <div class="item-total-price">
                <strong>¥{{ (item.totalPrice || 0).toFixed(2) }}</strong>
              </div>
            </a-list-item>
          </template>
        </a-list>

        <a-divider />
        <div class="order-totals-summary">
            <a-row justify="end">
                <a-col :xs="24" :sm="10" :md="8">
                    <div class="summary-item"><span>商品总额:</span> <span>¥{{ (order.totalAmount || 0).toFixed(2) }}</span></div>
                    <div class="summary-item"><span>运费:</span> <span>¥{{ (order.shippingFee || 0).toFixed(2) }}</span></div>
                    <div class="summary-item promotion" v-if="order.promotionInfo"><span>促销活动:</span> <span>- ¥{{ calculatePromotionDiscount(order.promotionInfo) }}</span></div>
                    <a-divider style="margin: 8px 0;" />
                    <div class="summary-item total">
                        <strong>应付总额:</strong>
                        <strong class="final-amount-value">¥{{ (order.payableAmount || 0).toFixed(2) }}</strong>
                    </div>
                </a-col>
            </a-row>
        </div>

      </a-card>
      <a-result status="404" title="订单详情加载失败" sub-title="抱歉，无法找到该订单的详细信息或您没有权限查看。" v-else-if="!loading && !order" class="mt-5">
        <template #extra>
          <router-link to="/orders"><a-button type="primary" size="large">返回我的订单列表</a-button></router-link>
        </template>
      </a-result>
    </a-skeleton>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { PageHeader, Card, Descriptions, DescriptionsItem, Divider, List, ListItem, ListItemMeta, Tag, Skeleton, Result, Button, Popconfirm, message as AntMessage } from 'ant-design-vue';
import { getOrderDetails, cancelOrder, simulatePayment } from '@/services/order.service'; // Assuming confirmReceipt needs a service

const APageHeader = PageHeader; const ACard = Card; const ADescriptions = Descriptions;
const ADescriptionsItem = Descriptions.Item; const ADivider = Divider; const AList = List;
const AListItem = List.Item; const AListItemMeta = List.Item.Meta; const ATag = Tag;
const ASkeleton = Skeleton; const AResult = Result; const AButton = Button; const APopconfirm = Popconfirm;

const route = useRoute();
const router = useRouter();

const order = ref(null);
const orderItems = ref([]);
const loading = ref(true);
const actionLoading = ref(false); // For buttons like pay, cancel, confirm receipt

const orderId = computed(() => route.params.id);

const fetchOrderDetailsData = async () => {
  if (!orderId.value) {
      AntMessage.error('无效的订单ID');
      router.push('/orders'); // Redirect if no ID
      return;
  }
  loading.value = true;
  try {
    const response = await getOrderDetails(orderId.value);
    if (response && response.order) { // Assuming backend returns { order: {}, orderItems: [] }
      order.value = response.order;
      orderItems.value = response.orderItems || [];
    } else {
      order.value = null; // Explicitly set to null if order not found
      AntMessage.error('未能获取订单详情。');
    }
  } catch (error) {
    console.error('获取订单详情失败:', error.response?.data?.message || error.message);
    order.value = null;
  } finally {
    loading.value = false;
  }
};

onMounted(fetchOrderDetailsData);

const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return '';
  return new Date(dateTimeStr).toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false });
};

const getOrderStatusText = (status) => { /* ... (same as OrderList) ... */
  const map = { 0: '待付款', 1: '待发货', 2: '已发货', 3: '已完成', 4: '已关闭', 5: '售后中' };
  return map[status] || '未知';
};
const getOrderStatusColor = (status) => { /* ... (same as OrderList) ... */
  const map = { 0: 'orange', 1: 'blue', 2: 'purple', 3: 'green', 4: 'gray', 5: 'red' };
  return map[status] || 'default';
};
const getPaymentMethodText = (method) => { /* ... (same as OrderList) ... */
    const map = { 1: '模拟支付宝', 2: '模拟微信支付' };
    return map[method] || (order.value?.paymentStatus === 1 ? '已支付' : '未指定/未支付');
};
const getPaymentStatusText = (status) => { /* ... (same as OrderList) ... */
    const map = { 0: '未支付', 1: '已支付', 2: '支付失败', 3: '已退款' };
    return map[status] || '未知';
};
const getShippingStatusText = (status) => {
    const map = { 0: '未发货', 1: '已发货', 2: '已签收', 3: '发货遇到问题' };
    return map[status] || '未知';
};
const calculatePromotionDiscount = (promoInfo) => {
    // This is a placeholder. Real logic would parse promoInfo or get discount from order object.
    if (promoInfo && typeof promoInfo === 'string' && promoInfo.includes('满100减10')) return '10.00';
    return '0.00';
};


const goToPayment = async (id) => {
  actionLoading.value = true;
  try {
    const response = await simulatePayment(id);
    AntMessage.success(response.message || '模拟支付成功！订单状态已更新。');
    fetchOrderDetailsData();
  } catch (error) { /* Handled by interceptor */ }
  finally { actionLoading.value = false; }
};

const handleCancelOrder = async (id) => {
  actionLoading.value = true;
    try {
        await cancelOrder(id);
        AntMessage.success('订单已成功取消');
        fetchOrderDetailsData();
    } catch (error) { /* Handled by interceptor */ }
    finally { actionLoading.value = false; }
};

const confirmReceipt = async (id) => {
    actionLoading.value = true;
    AntMessage.loading('正在确认收货...', 0);
    // TODO: Replace with actual API call: await orderService.confirmOrderReceived(id);
    await new Promise(resolve => setTimeout(resolve, 1500)); // Simulate API call
    AntMessage.destroy();
    AntMessage.success('已确认收货！感谢您的购买。');
    fetchOrderDetailsData(); // Refresh details
    actionLoading.value = false;
};

</script>

<style scoped>
.order-detail-page .ant-page-header-heading-extra .ant-btn {
    margin-left: 8px;
}
.page-header-custom {
    background-color: #fff;
    border-radius: 8px;
    margin-bottom: 24px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.order-detail-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.section-title {
  font-size: 18px;
  font-weight: 500;
  margin-top: 24px;
  margin-bottom: 16px;
}
.order-item-list .ant-list-item {
    padding: 12px 0;
}
.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}
.product-name-link {
    color: #333;
    font-weight: 500;
}
.product-name-link:hover {
    color: #00b96b; /* primary color */
}
.product-sku {
    font-size: 12px;
    color: #888;
}
.item-price-quantity {
    text-align: right;
    color: #555;
    min-width: 100px; /* Ensure consistent width */
}
.item-total-price {
    text-align: right;
    font-weight: bold;
    color: #333;
    min-width: 100px; /* Ensure consistent width */
}

.order-totals-summary {
    /* text-align: right; */
    margin-top: 20px;
    font-size: 14px;
    line-height: 1.8;
}
.summary-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    color: #555;
}
.summary-item span:first-child {
    /* color: #888; */
}
.summary-item.promotion span:last-child {
    color: #ff4d4f; /* Discount color */
}
.summary-item.total strong {
    font-size: 16px;
    color: #333;
}
.summary-item.total .final-amount-value {
    font-size: 20px;
    font-weight: bold;
    color: #ff4d4f;
}

.mb-4 { margin-bottom: 24px !important; }
.mt-4 { margin-top: 24px !important; }
.mt-5 { margin-top: 32px !important; }
.ml-2 { margin-left: 8px !important; }
</style>
