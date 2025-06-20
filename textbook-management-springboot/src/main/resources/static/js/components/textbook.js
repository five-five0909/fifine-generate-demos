// 教材管理组件
window.TextbookList = {
    template: `
        <div>
            <h2 class="text-white mb-4">教材管理</h2>
            
            <!-- 搜索框 -->
            <div class="search-box">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control" v-model="search.title" placeholder="教材名称">
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control" v-model="search.author" placeholder="作者">
                    </div>
                    <div class="col-md-3">
                        <select class="form-control" v-model="search.categoryId">
                            <option value="">所有分类</option>
                            <option v-for="category in categories" :key="category.id" :value="category.id">
                                {{ category.name }}
                            </option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button class="btn btn-primary w-100" @click="searchTextbooks">
                            <i class="fas fa-search"></i> 搜索
                        </button>
                    </div>
                </div>
            </div>

            <!-- 操作按钮 -->
            <div class="mb-3">
                <button class="btn btn-success" @click="showAddModal">
                    <i class="fas fa-plus"></i> 添加教材
                </button>
            </div>

            <!-- 教材列表 -->
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ISBN</th>
                            <th>教材名称</th>
                            <th>作者</th>
                            <th>出版社</th>
                            <th>分类</th>
                            <th>价格</th>
                            <th>库存</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="textbook in textbooks" :key="textbook.id">
                            <td>{{ textbook.isbn }}</td>
                            <td>{{ textbook.title }}</td>
                            <td>{{ textbook.author }}</td>
                            <td>{{ textbook.publisher }}</td>
                            <td>{{ textbook.categoryName }}</td>
                            <td>¥{{ textbook.price }}</td>
                            <td>{{ textbook.stock }}</td>
                            <td>
                                <button class="btn btn-sm btn-primary me-2" @click="showEditModal(textbook)">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" @click="deleteTextbook(textbook.id)">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 添加/编辑教材模态框 -->
            <div class="modal fade" id="textbookModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? '编辑教材' : '添加教材' }}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveTextbook">
                                <div class="mb-3">
                                    <label class="form-label">ISBN</label>
                                    <input type="text" class="form-control" v-model="form.isbn" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">教材名称</label>
                                    <input type="text" class="form-control" v-model="form.title" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">作者</label>
                                    <input type="text" class="form-control" v-model="form.author" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">出版社</label>
                                    <input type="text" class="form-control" v-model="form.publisher" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">出版日期</label>
                                    <input type="date" class="form-control" v-model="form.publishDate" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">分类</label>
                                    <select class="form-control" v-model="form.categoryId" required>
                                        <option v-for="category in categories" :key="category.id" :value="category.id">
                                            {{ category.name }}
                                        </option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">价格</label>
                                    <input type="number" class="form-control" v-model="form.price" step="0.01" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">库存</label>
                                    <input type="number" class="form-control" v-model="form.stock" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">描述</label>
                                    <textarea class="form-control" v-model="form.description" rows="3"></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" @click="saveTextbook">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            textbooks: [],
            categories: [],
            search: {
                title: '',
                author: '',
                categoryId: ''
            },
            form: {
                id: null,
                isbn: '',
                title: '',
                author: '',
                publisher: '',
                publishDate: '',
                categoryId: '',
                price: '',
                stock: '',
                description: ''
            },
            isEditing: false,
            modal: null
        }
    },
    created() {
        this.fetchTextbooks();
        this.fetchCategories();
    },
    mounted() {
        this.modal = new bootstrap.Modal(document.getElementById('textbookModal'));
    },
    methods: {
        async fetchTextbooks() {
            try {
                const response = await axios.get('/api/textbook/all');
                this.textbooks = response.data;
            } catch (error) {
                console.error('获取教材列表失败:', error);
            }
        },
        async fetchCategories() {
            try {
                const response = await axios.get('/api/category/all');
                this.categories = response.data;
            } catch (error) {
                console.error('获取分类列表失败:', error);
            }
        },
        async searchTextbooks() {
            try {
                const response = await axios.get('/api/textbook/search', {
                    params: this.search
                });
                this.textbooks = response.data;
            } catch (error) {
                console.error('搜索教材失败:', error);
            }
        },
        showAddModal() {
            this.isEditing = false;
            this.form = {
                id: null,
                isbn: '',
                title: '',
                author: '',
                publisher: '',
                publishDate: '',
                categoryId: '',
                price: '',
                stock: '',
                description: ''
            };
            this.modal.show();
        },
        showEditModal(textbook) {
            this.isEditing = true;
            this.form = { ...textbook };
            this.modal.show();
        },
        async saveTextbook() {
            try {
                if (this.isEditing) {
                    await axios.post('/api/textbook/update', this.form);
                } else {
                    await axios.post('/api/textbook/add', this.form);
                }
                this.modal.hide();
                this.fetchTextbooks();
            } catch (error) {
                console.error('保存教材失败:', error);
                alert('保存失败，请重试');
            }
        },
        async deleteTextbook(id) {
            if (confirm('确定要删除这本教材吗？')) {
                try {
                    await axios.delete(`/api/textbook/delete/${id}`);
                    this.fetchTextbooks();
                } catch (error) {
                    console.error('删除教材失败:', error);
                    alert('删除失败，请重试');
                }
            }
        },
        async getTextbookDetail(id) {
            try {
                const response = await axios.get(`/api/textbook/detail/${id}`);
                return response.data;
            } catch (error) {
                console.error('获取教材详情失败:', error);
            }
        },
        async updateStock(id, stock) {
            try {
                await axios.post('/api/textbook/updateStock', { id, stock });
                this.fetchTextbooks();
            } catch (error) {
                console.error('更新库存失败:', error);
            }
        }
    }
}; 