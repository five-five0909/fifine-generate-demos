<template>
  <div class="checkout-page container mt-4 mb-4">
    <a-page-header title="确认订单信息" @back="goBack" class="page-header-custom">
        <template #subTitle>请仔细核对您的订单与收货信息</template>
    </a-page-header>

    <a-row :gutter="[24, 32]">
      <!-- Left Column: Shipping and Payment -->
      <a-col :xs="24" :lg="14">
        <a-card title="收货信息" class="card-custom mb-4" :bordered="false">
          <a-form :model="shippingForm" layout="vertical" ref="shippingFormRef">
            <a-row :gutter="16">
                <a-col :xs="24" :sm="12">
                    <a-form-item label="收货人姓名" name="receiverName" :rules="[{ required: true, message: '请输入收货人姓名' }]">
                    <a-input v-model:value="shippingForm.receiverName" placeholder="请输入姓名" size="large"/>
                    </a-form-item>
                </a-col>
                <a-col :xs="24" :sm="12">
                    <a-form-item label="联系电话" name="receiverPhone" :rules="[{ required: true, message: '请输入联系电话' }, { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确'}]">
                    <a-input v-model:value="shippingForm.receiverPhone" placeholder="请输入手机号" size="large"/>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item label="详细地址" name="receiverAddress" :rules="[{ required: true, message: '请输入详细地址' }]">
              <a-textarea v-model:value="shippingForm.receiverAddress" placeholder="请输入省市区、街道、门牌号等" :rows="3" size="large"/>
            </a-form-item>
             <a-form-item label="订单备注 (可选)" name="remarks">
              <a-textarea v-model:value="shippingForm.remarks" placeholder="有什么想对卖家说的吗？（选填）" :rows="2" size="large"/>
            </a-form-item>
          </a-form>
        </a-card>

        <a-card title="选择支付方式" class="card-custom" :bordered="false">
          <a-radio-group v-model:value="paymentMethod" style="width: 100%;">
            <a-radio value="alipay" class="payment-option">
                <AlipayCircleOutlined /> 模拟支付宝
            </a-radio>
            <a-radio value="wechatpay" class="payment-option">
                <WechatPayOutlined /> 模拟微信支付 (暂不可用)
            </a-radio>
            <!-- Add more payment options here -->
          </a-radio-group>
        </a-card>
      </a-col>

      <!-- Right Column: Order Summary -->
      <a-col :xs="24" :lg="10">
        <a-card title="订单摘要" class="card-custom order-summary-card" :bordered="false">
          <div v-if="checkoutItems.length === 0 && !initialLoading" class="text-center">
            <a-empty description="没有待结算的商品。" />
            <a-button type="link" @click="() => router.push('/cart')" class="mt-2">返回购物车</a-button>
          </div>
          <div v-else>
            <a-list item-layout="horizontal" :data-source="checkoutItems" class="checkout-item-list">
                <template #renderItem="{ item }">
                <a-list-item>
                    <a-list-item-meta>
                    <template #avatar>
                        <img :src="item.mainImage || item.productMainImage || 'https://via.placeholder.com/60x60/ECECEC/AAAAAA?text=Img'" alt="商品图片" class="checkout-item-image" />
                    </template>
                    <template #title><span class="checkout-item-name">{{ item.productName }}</span></template>
                    <template #description>
                        <span class="checkout-item-details">单价: ¥{{ (item.price || 0).toFixed(2) }} x {{ item.quantity }}</span>
                    </template>
                    </a-list-item-meta>
                    <div class="checkout-item-total">¥{{ ((item.price || 0) * item.quantity).toFixed(2) }}</div>
                </a-list-item>
                </template>
            </a-list>
            <a-divider />
            <div class="summary-row">
                <span>商品总计 ({{ totalItemsQuantity }}件):</span>
                <span>¥{{ itemsTotalAmount.toFixed(2) }}</span>
            </div>
            <div class="summary-row">
                <span>运费:</span>
                <span>{{ shippingFee > 0 ? '¥' + shippingFee.toFixed(2) : '包邮' }}</span>
            </div>
            <a-divider />
            <div class="summary-row total">
                <span>应付总额:</span>
                <span class="amount">¥{{ finalTotalAmount.toFixed(2) }}</span>
            </div>
            <a-button type="danger" size="large" block @click="handleSubmitOrder" :loading="submitting" class="mt-4 submit-order-button">
                提交订单并支付
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
import { PageHeader, Row, Col, Card, List, ListItem, ListItemMeta, Divider, Form, Input, Textarea, Radio, Button, Empty, message as AntMessage } from 'ant-design-vue';
import { AlipayCircleOutlined, WechatPayOutlined } from '@ant-design/icons-vue';
import { createOrder } from '@/services/order.service';
import { getCurrentUserProfile } from '@/services/user.service';

const APageHeader = PageHeader; const ARow = Row; const ACol = Col; const ACard = Card;
const AList = List; const AListItem = List.Item; const AListItemMeta = List.Item.Meta;
const ADivider = Divider; const AForm = Form; const AFormItem = Form.Item;
const AInput = Input; const ATextarea = Textarea; const ARadioGroup = Radio.Group;
const ARadio = Radio; const AButton = Button; const AEmpty = Empty;

const router = useRouter();
const checkoutItems = ref([]);
const shippingFormRef = ref();
const initialLoading = ref(true);

const shippingForm = reactive({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remarks: '',
});
const paymentMethod = ref('alipay');
const submitting = ref(false);
const shippingFee = ref(0.00); // Example: Free shipping

const loadCheckoutItemsData = () => {
  const itemsStr = sessionStorage.getItem('checkoutItemsFromCart') || sessionStorage.getItem('checkoutItems');
  if (itemsStr) {
    checkoutItems.value = JSON.parse(itemsStr);
    if(checkoutItems.value.length === 0) {
        AntMessage.warning('没有商品需要结算，将返回购物车。');
        setTimeout(() => router.push('/cart'), 1500);
    }
  } else {
    AntMessage.warning('没有商品需要结算，将返回购物车。');
    setTimeout(() => router.push('/cart'), 1500);
  }
  initialLoading.value = false;
};

const loadDefaultShippingInfoData = async () => {
    try {
        const userProfile = await getCurrentUserProfile();
        if(userProfile) {
            shippingForm.receiverName = userProfile.nickname || userProfile.username || '';
            shippingForm.receiverPhone = userProfile.phoneNumber || '';
            // shippingForm.receiverAddress = userProfile.defaultAddress || ''; // TODO: Implement address book feature
        }
    } catch (error) {
        console.warn("获取用户默认收货信息失败:", error.response?.data?.message || error.message);
    }
};

onMounted(() => {
  loadCheckoutItemsData();
  loadDefaultShippingInfoData();
});

const itemsTotalAmount = computed(() => {
  return checkoutItems.value.reduce((sum, item) => sum + (item.price || 0) * item.quantity, 0);
});
const totalItemsQuantity = computed(() => {
  return checkoutItems.value.reduce((sum, item) => sum + item.quantity, 0);
});

const finalTotalAmount = computed(() => {
  return itemsTotalAmount.value + shippingFee.value;
});

const handleSubmitOrder = async () => {
  if (checkoutItems.value.length === 0) {
    AntMessage.error('请先选择商品再提交订单。');
    return;
  }
  try {
    await shippingFormRef.value.validate();
    submitting.value = true;

    const orderRequest = {
      ...shippingForm,
      // paymentMethod: paymentMethod.value, // Backend may not need this if payment is separate step
      // items: checkoutItems.value.map(item => ({ productId: item.productId, quantity: item.quantity, price: item.price })) // Backend will get items from cart or this payload
    };

    const createdOrder = await createOrder(orderRequest);
    AntMessage.success('订单已成功创建！');

    sessionStorage.removeItem('checkoutItemsFromCart');
    sessionStorage.removeItem('checkoutItems');

    // Redirect to payment page or order detail page
    router.replace({ name: 'OrderDetail', params: { id: createdOrder.id }, query: { from: 'checkout_success' } });

  } catch (formError) {
    if (formError && formError.errorFields) {
        AntMessage.error('请检查并填写所有必填的收货信息。');
    } else {
        console.error("订单提交失败:", formError.response?.data?.message || formError.message);
        // Error message is handled by the request interceptor
    }
  } finally {
    submitting.value = false;
  }
};

const goBack = () => {
    // If items came from cart, go to cart. Otherwise, go to previous page or home.
    if (sessionStorage.getItem('checkoutItemsFromCart')) {
        router.push('/cart');
    } else {
        router.go(-1);
    }
}

</script>

<style scoped>
.checkout-page {
  background-color: #f7f8fa; /* Light grey background for the page */
  padding-bottom: 40px;
}
.page-header-custom {
    background-color: #fff;
    border-radius: 8px 8px 0 0;
    padding: 16px 24px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.card-custom {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.order-summary-card {
    position: sticky; /* Make summary card sticky on large screens */
    top: 80px; /* Adjust based on navbar height + some padding */
}

.checkout-item-list .ant-list-item {
    padding: 12px 0;
}
.checkout-item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}
.checkout-item-name {
    font-size: 14px;
    color: #333;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
}
.checkout-item-details {
    font-size: 12px;
    color: #888;
}
.checkout-item-total {
  font-weight: 500;
  font-size: 14px;
  text-align: right;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  font-size: 14px;
  color: #555;
}
.summary-row.total {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  border-top: 1px dashed #e0e0e0;
  margin-top: 10px;
  padding-top: 15px;
}
.summary-row.total .amount {
  font-size: 22px;
  color: #ff4d4f;
}
.payment-option {
    display: block;
    height: 40px;
    line-height: 40px;
    font-size: 15px;
}
.payment-option .anticon {
    margin-right: 8px;
    font-size: 18px;
    vertical-align: middle;
}
.submit-order-button {
    font-weight: 500;
}
.mt-2 { margin-top: 8px !important; }
.mt-3 { margin-top: 16px !important; }
.mt-4 { margin-top: 24px !important; }
.mb-3 { margin-bottom: 16px !important; }
.mb-4 { margin-bottom: 24px !important; }
</style>
