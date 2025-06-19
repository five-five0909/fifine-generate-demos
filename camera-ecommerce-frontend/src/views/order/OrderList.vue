<template>
  <div class="order-list-page container mt-4 mb-4">
    <a-card title="我的订单" :bordered="false" class="order-list-card">
      <a-tabs v-model:activeKey="activeTabKey" @change="handleTabChange" class="order-tabs">
        <a-tab-pane key="all" tab="全部订单"></a-tab-pane>
        <a-tab-pane key="0" tab="待付款"></a-tab-pane> <!-- Status 0 -->
        <a-tab-pane key="1" tab="待发货"></a-tab-pane> <!-- Status 1 -->
        <a-tab-pane key="2" tab="已发货"></a-tab-pane> <!-- Status 2 -->
        <a-tab-pane key="3" tab="已完成"></a-tab-pane> <!-- Status 3 -->
        <a-tab-pane key="4" tab="已取消/关闭"></a-tab-pane> <!-- Status 4 -->
      </a-tabs>

      <div v-if="loading" class="loading-container">
        <a-spin size="large" tip="正在加载订单..." />
      </div>
      <a-empty v-else-if="orders.length === 0" :description="`您还没有${activeTabKey === 'all' ? '' : getOrderStatusText(parseInt(activeTabKey))}的订单哦~`" class="empty-orders">
         <router-link to="/products"><a-button type="primary" size="large">去挑选商品</a-button></router-link>
      </a-empty>
      <div v-else class="orders-container">
        <a-list item-layout="vertical" :data-source="orders" class="order-list-items">
          <template #renderItem="{ item: order }">
            <a-list-item class="order-item-wrapper">
              <a-card :bodyStyle="{padding: 0}" class="order-item-card-inner">
                <div class="order-header">
                  <span class="order-time">下单时间: {{ formatDateTime(order.createdAt) }}</span>
                  <span class="order-sn">订单号: {{ order.orderSn }}</span>
                  <span class="order-status" :style="{color: getOrderStatusColor(order.orderStatus)}">
                    {{ getOrderStatusText(order.orderStatus) }}
                  </span>
                </div>

                <!-- Simplified Product Preview: Assuming orderItems are part of the order object for list view (can be fetched on demand too) -->
                <div v-if="order.orderItems && order.orderItems.length > 0" class="order-products-preview">
                     <div v-for="pItem in order.orderItems.slice(0, 2)" :key="pItem.productId || pItem.id" class="product-preview-item">
                         <img :src="pItem.productImageUrl || 'https://via.placeholder.com/60x60?text=Item'" :alt="pItem.productName" class="preview-image"/>
                         <div class="preview-info">
                            <div class="preview-name">{{pItem.productName}}</div>
                            <div class="preview-qty">x {{pItem.quantity}}</div>
                         </div>
                     </div>
                     <a-button type="link" size="small" @click="viewOrderDetail(order.id)" v-if="order.orderItems.length > 2" class="preview-more">
                        ...等{{order.orderItems.length}}件商品
                     </a-button>
                </div>
                 <div v-else class="order-products-preview-empty">
                    <a-button type="link" size="small" @click="viewOrderDetail(order.id)">查看订单内商品</a-button>
                </div>


                <div class="order-footer">
                  <span class="order-total">订单总额: <strong class="amount-value">¥{{ (order.payableAmount || 0).toFixed(2) }}</strong></span>
                  <div class="order-actions">
                    <a-button type="primary" ghost size="small" @click="viewOrderDetail(order.id)" class="action-btn">订单详情</a-button>
                    <a-button v-if="order.orderStatus === 0" type="danger" size="small" @click="goToPayment(order.id)" class="action-btn">立即支付</a-button>
                    <a-popconfirm
                        v-if="order.orderStatus === 0"
                        title="确定要取消这个订单吗?"
                        ok-text="确定取消"
                        cancel-text="再想想"
                        @confirm="() => handleCancelOrder(order.id)"
                    >
                        <a-button size="small" class="action-btn">取消订单</a-button>
                    </a-popconfirm>
                     <a-button v-if="order.orderStatus === 2" type="primary" size="small" @click="() => confirmReceipt(order.id)" class="action-btn">确认收货</a-button>
                     <!-- Add more actions like "Apply for Refund", "Track Shipment" etc. -->
                  </div>
                </div>
              </a-card>
            </a-list-item>
          </template>
        </a-list>
         <div class="pagination-container mt-4" v-if="orders.length > 0 && pagination.total > pagination.pageSize">
            <a-pagination
                v-model:current="pagination.current"
                :total="pagination.total"
                :page-size="pagination.pageSize"
                @change="pagination.onChange"
                show-less-items
            />
        </div>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Card, Tabs, TabPane, List, ListItem, Button, Empty, Spin, Popconfirm, Pagination, message as AntMessage } from 'ant-design-vue';
import { getUserOrders, cancelOrder, simulatePayment } from '@/services/order.service';

const ACard = Card; const ATabs = Tabs; const ATabPane = TabPane; const AList = List;
const AListItem = List.Item; const AButton = Button; const AEmpty = Empty;
const ASpin = Spin; const APopconfirm = Popconfirm; const APagination = Pagination;

const router = useRouter();
const route = useRoute();

const orders = ref([]);
const loading = ref(true);
const activeTabKey = ref(route.query.status?.toString() || 'all');


const pagination = reactive({
  current: parseInt(route.query.page) || 1,
  pageSize: 5,
  total: 0,
  onChange: (page) => {
    pagination.current = page;
    updateRouteQuery();
  }
});

const paginationConfig = computed(() => ({
    current: pagination.current,
    pageSize: pagination.pageSize,
    total: pagination.total,
    onChange: pagination.onChange,
    hideOnSinglePage: true,
}));

const fetchOrdersData = async () => {
  loading.value = true;
  try {
    const params = {
      current: pagination.current,
      size: pagination.pageSize,
      orderStatus: activeTabKey.value === 'all' ? undefined : parseInt(activeTabKey.value),
    };

    const response = await getUserOrders(params);
    // Assuming backend returns orderItems directly with each order in the list for preview
    // If not, this needs adjustment or items won't show in list view.
    orders.value = response.records || [];
    pagination.total = response.total || 0;
  } catch (error) {
    console.error('获取订单列表失败:', error.response?.data?.message || error.message);
  } finally {
    loading.value = false;
  }
};

const updateRouteQuery = () => {
  const query = { ...route.query };
  if (activeTabKey.value && activeTabKey.value !== 'all') {
    query.status = activeTabKey.value;
  } else {
    delete query.status;
  }
  if (pagination.current > 1) {
    query.page = pagination.current.toString();
  } else {
    delete query.page;
  }
  router.push({ query });
};

const handleTabChange = (key) => {
  activeTabKey.value = key;
  pagination.current = 1;
  updateRouteQuery();
};

watch(() => route.query, (newQuery) => {
    activeTabKey.value = newQuery.status?.toString() || 'all';
    pagination.current = parseInt(newQuery.page) || 1;
    fetchOrdersData();
}, { immediate: true, deep: true });


// onMounted(() => { /* fetchOrdersData is called by watch */ });

const viewOrderDetail = (orderId) => {
  router.push({ name: 'OrderDetail', params: { id: orderId } });
};

const goToPayment = async (orderId) => {
  try {
    // In a real app, this might redirect to a payment page or open a modal
    const response = await simulatePayment(orderId);
    AntMessage.success(response.message || '模拟支付成功！订单状态已更新。');
    fetchOrdersData();
  } catch (error) {
    // Error handled by interceptor
  }
};

const handleCancelOrder = async (orderId) => {
    try {
        await cancelOrder(orderId);
        AntMessage.success('订单已成功取消');
        fetchOrdersData();
    } catch (error) {
        // Error handled by interceptor
    }
};

const confirmReceipt = async (orderId) => {
    AntMessage.loading('正在确认收货...', 0);
    // TODO: Replace with actual API call to backend
    // For now, simulate success and update UI
    await new Promise(resolve => setTimeout(resolve, 1000));
    // Example: orderService.confirmOrderReceived(orderId);
    AntMessage.destroy();
    AntMessage.success('已确认收货！感谢您的购买。');
    // Optimistically update status or re-fetch
    const order = orders.value.find(o => o.id === orderId);
    if (order) order.orderStatus = 3; // 3 for 'completed'
    // Or better: fetchOrdersData();
};

const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return 'N/A';
  const date = new Date(dateTimeStr);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

const getOrderStatusText = (status) => {
  const map = { 0: '待付款', 1: '待发货', 2: '已发货', 3: '已完成', 4: '已取消', 5: '售后中' };
  return map[status] || '未知';
};
const getOrderStatusColor = (status) => {
  const map = { 0: '#faad14', 1: '#1890ff', 2: '#87d068', 3: '#52c41a', 4: '#bfbfbf', 5: '#ff4d4f' };
  return map[status] || '#333';
};

</script>

<style scoped>
.order-list-card {
  border-radius: 8px;
}
.order-tabs {
  margin-bottom: 20px;
}
.loading-container, .empty-orders {
  padding: 40px 0;
  text-align: center;
}
.order-item-wrapper {
  margin-bottom: 16px;
  padding: 0 !important; /* Override Ant List default padding */
}
.order-item-card-inner {
    border: 1px solid #f0f0f0;
    border-radius: 6px;
    transition: box-shadow 0.3s;
}
.order-item-card-inner:hover {
    box-shadow: 0 2px 8px rgba(0,0,0,0.09);
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa;
  padding: 10px 16px;
  font-size: 13px;
  color: #555;
  border-bottom: 1px solid #f0f0f0;
}
.order-header .order-time { color: #888; }
.order-header .order-sn { font-weight: 500; }
.order-header .order-status { font-weight: bold; }

.order-products-preview {
    padding: 12px 16px;
    display: flex;
    align-items: center;
    gap: 10px; /* Space between product items */
    border-bottom: 1px solid #f0f0f0;
}
.product-preview-item {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #555;
}
.preview-image {
    width: 50px;
    height: 50px;
    object-fit: cover;
    margin-right: 8px;
    border-radius: 3px;
    border: 1px solid #eee;
}
.preview-info {
    display: flex;
    flex-direction: column;
}
.preview-name {
    color: #333;
    margin-bottom: 2px;
    /* Ellipsis for long names if needed */
}
.preview-qty { color: #888; }
.preview-more { font-size: 12px; }
.order-products-preview-empty {
    padding: 12px 16px;
    border-bottom: 1px solid #f0f0f0;
}


.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  font-size: 14px;
}
.order-total .amount-value {
  color: #ff4d4f;
  font-size: 16px;
  font-weight: bold;
}
.order-actions .action-btn {
  margin-left: 8px;
}
.pagination-container {
  display: flex;
  justify-content: center;
}
.mt-4 { margin-top: 24px !important; }
</style>
