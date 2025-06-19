<template>
  <div class="product-list-page container mt-4 mb-4">
    <a-row :gutter="[24, 24]">
      <!-- Filters Column -->
      <a-col :xs="24" :sm="24" :md="6">
        <a-card title="商品分类" class="filter-card mb-3" :bordered="false">
          <a-menu
            mode="inline"
            @click="handleCategoryChange"
            :selectedKeys="selectedCategoryKeys"
            class="category-menu"
            >
            <a-menu-item key="all">
                <AppstoreOutlined /> 所有分类
            </a-menu-item>
            <template v-for="category in categories" :key="category.id">
                <a-sub-menu v-if="category.children && category.children.length > 0" :key="category.id" :title="category.name">
                    <template #icon><FolderOutlined /></template>
                    <a-menu-item v-for="subCategory in category.children" :key="subCategory.id">
                        {{ subCategory.name }}
                    </a-menu-item>
                </a-sub-menu>
                <a-menu-item v-else :key="category.id">
                    <template #icon><FolderOutlined /></template>
                    {{ category.name }}
                </a-menu-item>
            </template>
             <a-menu-item v-if="categoriesLoading">
                <a-spin size="small" /> 加载分类中...
            </a-menu-item>
          </a-menu>
        </a-card>

        <a-card title="价格范围" class="filter-card mb-3" :bordered="false">
            <a-slider
                range
                :min="0"
                :max="priceFilterMax"
                v-model:value="priceRange"
                @afterChange="handlePriceChange"
                :step="50"
                :marks="{ 0: '¥0', [priceFilterMax/2]: `¥${priceFilterMax/2}`, [priceFilterMax]: `¥${priceFilterMax}` }"
            />
            <div class="price-range-display mt-2">
                价格: ¥{{ priceRange[0] }} - ¥{{ priceRange[1] }}
            </div>
        </a-card>
         <!-- Placeholder for Brand Filter -->
        <a-card title="品牌筛选 (示例)" class="filter-card" :bordered="false">
            <a-checkbox-group v-model:value="selectedBrands" style="width: 100%;">
                <a-row>
                    <a-col :span="24" v-for="brand in sampleBrands" :key="brand.value">
                        <a-checkbox :value="brand.value">{{ brand.label }}</a-checkbox>
                    </a-col>
                </a-row>
            </a-checkbox-group>
             <a-button type="link" size="small" @click="applyBrandFilter" class="mt-2">应用品牌筛选</a-button>
        </a-card>
      </a-col>

      <!-- Products Column -->
      <a-col :xs="24" :sm="24" :md="18">
        <a-card class="products-card" :bordered="false">
          <div class="toolbar mb-4">
            <a-input-search
              v-model:value="searchKeyword"
              placeholder="搜索商品名称、标签..."
              style="max-width: 350px; min-width:200px; flex-grow: 1;"
              @search="onSearch"
              enter-button="搜索"
              size="large"
            />
            <a-select v-model:value="sortOrder" style="width: 180px;" @change="handleSortChange" size="large" class="sort-select">
              <a-select-option value="default">默认排序</a-select-option>
              <a-select-option value="price_asc">价格升序</a-select-option>
              <a-select-option value="price_desc">价格降序</a-select-option>
              <a-select-option value="sales_desc">销量优先</a-select-option>
              <a-select-option value="newest_desc">新品优先</a-select-option>
            </a-select>
          </div>

          <div v-if="loading" class="loading-state">
            <a-spin size="large" tip="正在努力加载商品..." />
          </div>
          <div v-else-if="products.length === 0" class="empty-state">
            <a-empty>
                <template #description>
                    <span>暂无符合条件的商品。尝试调整筛选条件或 <a @click="resetFilters">重置筛选</a>。</span>
                </template>
            </a-empty>
          </div>
          <div v-else>
            <a-row :gutter="[16, 24]">
              <a-col v-for="product in products" :key="product.id" :xs="24" :sm="12" :md="8" :lg="8">
                <ProductCard :product="product" />
              </a-col>
            </a-row>
            <div class="pagination-container mt-4">
              <a-pagination
                v-model:current="pagination.current"
                :total="pagination.total"
                :page-size="pagination.pageSize"
                @change="handlePageChange"
                show-less-items
                show-quick-jumper
                show-size-changer
                :page-size-options="['12', '24', '36', '48']"
                @showSizeChange="onPageSizeChange"
              />
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Row, Col, Card, Menu, Input, Select, Pagination, Spin, Empty, Slider, Checkbox, Button, message as AntMessage } from 'ant-design-vue';
import { AppstoreOutlined, FolderOutlined } from '@ant-design/icons-vue';
import ProductCard from '@/components/product/ProductCard.vue'; // Assume this will be created
// Mock services until actual ones are ready
const mockProductService = {
    getProducts: async (params) => {
        console.log('Mock getProducts called with:', params);
        await new Promise(resolve => setTimeout(resolve, 700));
        const MOCK_PRODUCTS = [ // Sample products
            { id: 1, name: '超高清4K运动相机', price: 1299, mainImageUrl: 'https://via.placeholder.com/300x200/FFA07A/000?text=ActionCam+4K', categoryId:3, brand: 'GoAction', stock: 20, salesCount: 150, createdAt: '2023-10-01T10:00:00Z' },
            { id: 2, name: '专业级全画幅单反', price: 22500, mainImageUrl: 'https://via.placeholder.com/300x200/98FB98/000?text=Pro+DSLR', categoryId:1, brand: 'CamPro', stock: 5, salesCount: 80, createdAt: '2023-09-15T10:00:00Z' },
            { id: 3, name: '轻巧便携微单套机', price: 5880, mainImageUrl: 'https://via.placeholder.com/300x200/ADD8E6/000?text=Mirrorless+Kit', categoryId:2, brand: 'LiteShot', stock: 0, salesCount: 220, createdAt: '2023-11-01T10:00:00Z' },
            { id: 4, name: '长焦距变焦镜头', price: 7500, mainImageUrl: 'https://via.placeholder.com/300x200/FFD700/000?text=Zoom+Lens', categoryId:4, brand: 'LensMaster', stock: 12, salesCount: 95, createdAt: '2023-08-20T10:00:00Z' },
            { id: 5, name: '碳纤维旅行三脚架', price: 899, mainImageUrl: 'https://via.placeholder.com/300x200/E6E6FA/000?text=Carbon+Tripod', categoryId:5, brand: 'SturdyPod', stock: 30, salesCount: 180, createdAt: '2023-10-25T10:00:00Z' },
            { id: 6, name: '智能航拍无人机', price: 4999, mainImageUrl: 'https://via.placeholder.com/300x200/DDA0DD/000?text=Drone+4K', categoryId:6, brand: 'SkyHigh', stock: 10, salesCount: 60, createdAt: '2023-07-10T10:00:00Z' },
        ];
        let filtered = MOCK_PRODUCTS;
        if (params.categoryId) filtered = filtered.filter(p => p.categoryId === parseInt(params.categoryId));
        if (params.keyword) filtered = filtered.filter(p => p.name.toLowerCase().includes(params.keyword.toLowerCase()));
        if (params.minPrice) filtered = filtered.filter(p => p.price >= params.minPrice);
        if (params.maxPrice) filtered = filtered.filter(p => p.price <= params.maxPrice);

        // Sorting
        if (params.sort) {
            const [field, order] = params.sort.split('_');
            filtered.sort((a, b) => {
                let comparison = 0;
                if (a[field] > b[field]) comparison = 1;
                else if (a[field] < b[field]) comparison = -1;
                return order === 'desc' ? comparison * -1 : comparison;
            });
        }

        const start = (params.current - 1) * params.size;
        const end = start + params.size;
        return { records: filtered.slice(start, end), total: filtered.length };
    }
};
const mockCategoryService = {
    getCategories: async (params) => {
        await new Promise(resolve => setTimeout(resolve, 300));
        if (params && params.parentId === 0) {
            return { records: [
                { id: 1, name: '单反相机', children: [] }, { id: 2, name: '微单相机', children: [] }, { id: 3, name: '运动相机', children: [] },
                { id: 4, name: '镜头配件', children: [ {id: 401, name: '定焦镜头'}, {id: 402, name: '变焦镜头'} ] },
                { id: 5, name: '三脚架', children: [] }, { id: 6, name: '无人机', children: [] }
            ]};
        } else if (params && params.parentId) { // Mock sub-categories if any
             const parentCat = (await mockCategoryService.getCategories({parentId:0})).records.find(c => c.id === params.parentId);
             return { records: parentCat ? parentCat.children : [] };
        }
        return { records: [] };
    }
};
// import { getProducts } from '@/services/product.service';
// import { getCategories as fetchProductCategories } from '@/services/category.service';
const getProducts = mockProductService.getProducts;
const fetchProductCategories = mockCategoryService.getCategories;


// Ant Design Component Aliases
const ARow = Row; const ACol = Col; const ACard = Card; const AMenu = Menu; const AMenuItem = Menu.Item;
const ASubMenu = Menu.SubMenu; const AInputSearch = Input.Search; const ASelect = Select;
const ASelectOption = Select.Option; const APagination = Pagination; const ASpin = Spin;
const AEmpty = Empty; const ASlider = Slider; const ACheckboxGroup = Checkbox.Group; const ACheckbox = Checkbox;
const AButton = Button;


const route = useRoute();
const router = useRouter();

const products = ref([]);
const categories = ref([]);
const categoriesLoading = ref(false);
const loading = ref(false);
const priceFilterMax = ref(50000); // Max price for slider

const searchKeyword = ref(route.query.keyword || '');
const selectedCategoryKeys = ref(route.query.categoryId ? [route.query.categoryId.toString()] : ['all']);
const sortOrder = ref(route.query.sort || 'default');
const priceRange = ref([
    parseInt(route.query.minPrice) || 0,
    parseInt(route.query.maxPrice) || priceFilterMax.value
]);
const selectedBrands = ref(route.query.brands ? route.query.brands.split(',') : []);
const sampleBrands = ref([ // Mock brands
    { label: '尼康', value: 'Nikon' }, { label: '索尼', value: 'Sony' }, { label: '佳能', value: 'Canon' },
    { label: 'GoPro', value: 'GoPro' }, { label: '大疆', value: 'DJI' }, { label: '其他', value: 'OtherBrand' }
]);


const pagination = reactive({
  current: parseInt(route.query.page) || 1,
  pageSize: 12,
  total: 0,
});

const fetchProductsData = async () => {
  loading.value = true;
  try {
    const params = {
      current: pagination.current,
      size: pagination.pageSize,
      keyword: searchKeyword.value || undefined,
      categoryId: selectedCategoryKeys.value[0] === 'all' ? undefined : selectedCategoryKeys.value[0],
      sort: sortOrder.value === 'default' ? undefined : sortOrder.value,
      minPrice: priceRange.value[0] > 0 ? priceRange.value[0] : undefined,
      maxPrice: priceRange.value[1] < priceFilterMax.value ? priceRange.value[1] : undefined,
      brands: selectedBrands.value.length > 0 ? selectedBrands.value.join(',') : undefined,
    };
    Object.keys(params).forEach(key => params[key] === undefined && delete params[key]);

    const response = await getProducts(params);
    products.value = response.records || [];
    pagination.total = response.total || 0;
  } catch (error) {
    console.error('获取商品列表失败:', error);
    AntMessage.error('加载商品失败，请稍后重试。');
  } finally {
    loading.value = false;
  }
};

const loadCategoriesData = async () => {
    categoriesLoading.value = true;
    try {
        const topCategories = await fetchProductCategories({ parentId: 0 });
        if (topCategories && topCategories.records) {
             for (const cat of topCategories.records) {
                const subResponse = await fetchProductCategories({ parentId: cat.id });
                cat.children = subResponse.records || [];
            }
            categories.value = topCategories.records;
        } else {
            categories.value = [];
        }
    } catch (error) {
        console.error('获取分类失败:', error);
        AntMessage.error('加载分类信息失败。');
    } finally {
        categoriesLoading.value = false;
    }
};

const updateRouterQuery = () => {
  const query = {};
  if (searchKeyword.value) query.keyword = searchKeyword.value;
  if (selectedCategoryKeys.value[0] && selectedCategoryKeys.value[0] !== 'all') query.categoryId = selectedCategoryKeys.value[0];
  if (sortOrder.value !== 'default') query.sort = sortOrder.value;
  if (pagination.current > 1) query.page = pagination.current;
  if (pagination.pageSize !== 12) query.pageSize = pagination.pageSize;
  if (priceRange.value[0] > 0) query.minPrice = priceRange.value[0];
  if (priceRange.value[1] < priceFilterMax.value) query.maxPrice = priceRange.value[1];
  if (selectedBrands.value.length > 0) query.brands = selectedBrands.value.join(',');

  router.push({ query });
};

const onSearch = () => { pagination.current = 1; updateRouterQuery(); };
const handleCategoryChange = (e) => { selectedCategoryKeys.value = [e.key]; pagination.current = 1; updateRouterQuery(); };
const handleSortChange = () => { pagination.current = 1; updateRouterQuery(); };
const handlePriceChange = () => { pagination.current = 1; updateRouterQuery(); };
const applyBrandFilter = () => { pagination.current = 1; updateRouterQuery(); };
const handlePageChange = (page, pageSize) => {
  pagination.current = page;
  pagination.pageSize = pageSize; // AntD pagination component might pass pageSize here too
  updateRouterQuery();
};
const onPageSizeChange = (current, size) => {
    pagination.current = 1; // Reset to first page when page size changes
    pagination.pageSize = size;
    updateRouterQuery();
};

const resetFilters = () => {
    searchKeyword.value = '';
    selectedCategoryKeys.value = ['all'];
    sortOrder.value = 'default';
    priceRange.value = [0, priceFilterMax.value];
    selectedBrands.value = [];
    pagination.current = 1;
    pagination.pageSize = 12;
    updateRouterQuery();
};

watch(() => route.query, (newQuery, oldQuery) => {
    searchKeyword.value = newQuery.keyword || '';
    selectedCategoryKeys.value = newQuery.categoryId ? [newQuery.categoryId.toString()] : ['all'];
    sortOrder.value = newQuery.sort || 'default';
    pagination.current = parseInt(newQuery.page) || 1;
    pagination.pageSize = parseInt(newQuery.pageSize) || 12;
    priceRange.value = [parseInt(newQuery.minPrice) || 0, parseInt(newQuery.maxPrice) || priceFilterMax.value];
    selectedBrands.value = newQuery.brands ? newQuery.brands.split(',') : [];
    fetchProductsData();
  },
  { immediate: true, deep: true }
);

onMounted(() => {
  loadCategoriesData();
  // fetchProductsData is called by the route query watch initially
});
</script>

<style scoped>
.filter-card .ant-menu-inline, .filter-card .ant-menu-vertical {
    border-right: none;
}
.filter-card .ant-card-body {
    padding: 16px; /* Smaller padding for filter cards */
}
.products-card .ant-card-body {
    padding: 20px;
}
.toolbar {
  display: flex;
  justify-content: space-between; /* Pushes search and sort apart */
  align-items: center;
  flex-wrap: wrap;
  gap: 16px; /* Adds space between items if they wrap */
}
.sort-select {
    /* margin-left: auto; Push sort to the right if search is not taking full width */
}

.pagination-container {
  display: flex;
  justify-content: center; /* Center pagination */
  margin-top: 24px;
}
.loading-state, .empty-state {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 300px; /* Ensure it takes some space */
  padding: 20px;
}
.price-range-display {
    text-align: center;
    color: #555;
}
.category-menu .anticon {
    margin-right: 8px;
}
</style>
