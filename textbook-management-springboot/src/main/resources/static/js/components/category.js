// 分类管理组件
window.CategoryList = {
    template: `
        <div>
            <h2 class="text-white mb-4">分类管理</h2>
            
            <!-- 操作按钮 -->
            <div class="mb-3">
                <button class="btn btn-success" @click="showAddModal">
                    <i class="fas fa-plus"></i> 添加分类
                </button>
            </div>

            <!-- 分类列表 -->
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>分类名称</th>
                            <th>描述</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="category in categories" :key="category.id">
                            <td>{{ category.id }}</td>
                            <td>{{ category.name }}</td>
                            <td>{{ category.description }}</td>
                            <td>{{ formatDate(category.createTime) }}</td>
                            <td>
                                <button class="btn btn-sm btn-primary me-2" @click="showEditModal(category)">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" @click="deleteCategory(category.id)">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 添加/编辑分类模态框 -->
            <div class="modal fade" id="categoryModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? '编辑分类' : '添加分类' }}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveCategory">
                                <div class="mb-3">
                                    <label class="form-label">分类名称</label>
                                    <input type="text" class="form-control" v-model="form.name" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">描述</label>
                                    <textarea class="form-control" v-model="form.description" rows="3"></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" @click="saveCategory">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            categories: [],
            form: {
                id: null,
                name: '',
                description: ''
            },
            isEditing: false,
            modal: null
        }
    },
    created() {
        this.fetchCategories();
    },
    mounted() {
        this.modal = new bootstrap.Modal(document.getElementById('categoryModal'));
    },
    methods: {
        async fetchCategories() {
            try {
                const response = await axios.get('/api/category/all');
                this.categories = response.data;
            } catch (error) {
                console.error('获取分类列表失败:', error);
                alert('获取分类列表失败，请重试');
            }
        },
        showAddModal() {
            this.isEditing = false;
            this.form = {
                id: null,
                name: '',
                description: ''
            };
            this.modal.show();
        },
        showEditModal(category) {
            this.isEditing = true;
            this.form = { ...category };
            this.modal.show();
        },
        async saveCategory() {
            try {
                if (this.isEditing) {
                    await axios.post('/api/category/update', this.form);
                } else {
                    await axios.post('/api/category/add', this.form);
                }
                this.modal.hide();
                this.fetchCategories();
            } catch (error) {
                console.error('保存分类失败:', error);
                alert('保存失败，请重试');
            }
        },
        async deleteCategory(id) {
            if (confirm('确定要删除这个分类吗？')) {
                try {
                    await axios.delete(`/api/category/delete/${id}`);
                    this.fetchCategories();
                } catch (error) {
                    console.error('删除分类失败:', error);
                    alert('删除失败，请重试');
                }
            }
        },
        async getCategoryDetail(id) {
            try {
                const response = await axios.get(`/api/category/detail/${id}`);
                return response.data;
            } catch (error) {
                console.error('获取分类详情失败:', error);
            }
        },
        formatDate(date) {
            if (!date) return '';
            return new Date(date).toLocaleString();
        }
    }
}; 