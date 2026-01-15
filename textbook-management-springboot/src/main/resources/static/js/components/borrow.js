// 借阅管理组件
window.BorrowList = {
    template: `
        <div>
            <h2 class="text-white mb-4">借阅管理</h2>
            
            <!-- 搜索框 -->
            <div class="search-box">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control" v-model="search.studentName" placeholder="学生姓名">
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control" v-model="search.studentNumber" placeholder="学号">
                    </div>
                    <div class="col-md-3">
                        <select class="form-control" v-model="search.status">
                            <option value="">所有状态</option>
                            <option value="借阅中">借阅中</option>
                            <option value="已归还">已归还</option>
                            <option value="逾期">逾期</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button class="btn btn-primary w-100" @click="searchBorrowRecords">
                            <i class="fas fa-search"></i> 搜索
                        </button>
                    </div>
                </div>
            </div>

            <!-- 操作按钮 -->
            <div class="mb-3">
                <button class="btn btn-success" @click="showAddModal">
                    <i class="fas fa-plus"></i> 新增借阅
                </button>
                <button class="btn btn-warning ms-2" @click="showOverdueRecords">
                    <i class="fas fa-exclamation-triangle"></i> 查看逾期记录
                </button>
            </div>

            <!-- 借阅记录列表 -->
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>教材名称</th>
                            <th>学生姓名</th>
                            <th>学号</th>
                            <th>借阅日期</th>
                            <th>应还日期</th>
                            <th>实际归还日期</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="record in borrowRecords" :key="record.id">
                            <td>{{ record.id }}</td>
                            <td>{{ record.textbookTitle }}</td>
                            <td>{{ record.studentName }}</td>
                            <td>{{ record.studentNumber }}</td>
                            <td>{{ formatDate(record.borrowDate) }}</td>
                            <td>{{ formatDate(record.dueDate) }}</td>
                            <td>{{ formatDate(record.returnDate) }}</td>
                            <td>
                                <span :class="getStatusClass(record.status)">{{ record.status }}</span>
                            </td>
                            <td>
                                <button v-if="record.status === '借阅中'" 
                                        class="btn btn-sm btn-success me-2" 
                                        @click="returnTextbook(record.id)">
                                    <i class="fas fa-undo"></i> 归还
                                </button>
                                <button class="btn btn-sm btn-primary me-2" @click="showEditModal(record)">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" @click="deleteBorrowRecord(record.id)">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 添加/编辑借阅记录模态框 -->
            <div class="modal fade" id="borrowModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">{{ isEditing ? '编辑借阅记录' : '新增借阅' }}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form @submit.prevent="saveBorrowRecord">
                                <div class="mb-3">
                                    <label class="form-label">教材</label>
                                    <select class="form-control" v-model="form.textbookId" required>
                                        <option v-for="textbook in textbooks" :key="textbook.id" :value="textbook.id">
                                            {{ textbook.title }}
                                        </option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">学生姓名</label>
                                    <input type="text" class="form-control" v-model="form.studentName" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">学号</label>
                                    <input type="text" class="form-control" v-model="form.studentNumber" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">借阅日期</label>
                                    <input type="date" class="form-control" v-model="form.borrowDate" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">应还日期</label>
                                    <input type="date" class="form-control" v-model="form.dueDate" required>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" @click="saveBorrowRecord">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            borrowRecords: [],
            textbooks: [],
            search: {
                studentName: '',
                studentNumber: '',
                status: ''
            },
            form: {
                id: null,
                textbookId: '',
                studentName: '',
                studentNumber: '',
                borrowDate: '',
                dueDate: ''
            },
            isEditing: false,
            modal: null
        }
    },
    created() {
        this.fetchBorrowRecords();
        this.fetchTextbooks();
    },
    mounted() {
        this.modal = new bootstrap.Modal(document.getElementById('borrowModal'));
    },
    methods: {
        async fetchBorrowRecords() {
            try {
                const response = await axios.get('/api/borrow/all');
                this.borrowRecords = response.data;
            } catch (error) {
                console.error('获取借阅记录失败:', error);
                alert('获取借阅记录失败，请重试');
            }
        },
        async fetchTextbooks() {
            try {
                const response = await axios.get('/api/textbook/all');
                this.textbooks = response.data;
            } catch (error) {
                console.error('获取教材列表失败:', error);
                alert('获取教材列表失败，请重试');
            }
        },
        async searchBorrowRecords() {
            try {
                const response = await axios.get('/api/borrow/search', {
                    params: this.search
                });
                this.borrowRecords = response.data;
            } catch (error) {
                console.error('搜索借阅记录失败:', error);
                alert('搜索失败，请重试');
            }
        },
        showAddModal() {
            this.isEditing = false;
            this.form = {
                id: null,
                textbookId: '',
                studentName: '',
                studentNumber: '',
                borrowDate: new Date().toISOString().split('T')[0],
                dueDate: this.getDefaultDueDate()
            };
            this.modal.show();
        },
        showEditModal(record) {
            this.isEditing = true;
            this.form = { ...record };
            this.modal.show();
        },
        async saveBorrowRecord() {
            try {
                if (this.isEditing) {
                    await axios.post('/api/borrow/update', this.form);
                } else {
                    await axios.post('/api/borrow/add', this.form);
                }
                this.modal.hide();
                this.fetchBorrowRecords();
            } catch (error) {
                console.error('保存借阅记录失败:', error);
                alert('保存失败，请重试');
            }
        },
        async deleteBorrowRecord(id) {
            if (confirm('确定要删除这条借阅记录吗？')) {
                try {
                    await axios.delete(`/api/borrow/delete/${id}`);
                    this.fetchBorrowRecords();
                } catch (error) {
                    console.error('删除借阅记录失败:', error);
                    alert('删除失败，请重试');
                }
            }
        },
        async returnTextbook(id) {
            if (confirm('确定要归还这本教材吗？')) {
                try {
                    await axios.post(`/api/borrow/return/${id}`);
                    this.fetchBorrowRecords();
                } catch (error) {
                    console.error('归还教材失败:', error);
                    alert('归还失败，请重试');
                }
            }
        },
        async showOverdueRecords() {
            try {
                const response = await axios.get('/api/borrow/overdue/api');
                this.borrowRecords = response.data;
            } catch (error) {
                console.error('获取逾期记录失败:', error);
                alert('获取逾期记录失败，请重试');
            }
        },
        async getBorrowDetail(id) {
            try {
                const response = await axios.get(`/api/borrow/detail/${id}`);
                return response.data;
            } catch (error) {
                console.error('获取借阅详情失败:', error);
            }
        },
        async getBorrowByStudent(studentNumber) {
            try {
                const response = await axios.get(`/api/borrow/student/${studentNumber}`);
                return response.data;
            } catch (error) {
                console.error('根据学号获取借阅记录失败:', error);
            }
        },
        formatDate(date) {
            if (!date) return '';
            return new Date(date).toLocaleString();
        },
        getStatusClass(status) {
            switch (status) {
                case '借阅中':
                    return 'badge bg-primary';
                case '已归还':
                    return 'badge bg-success';
                case '逾期':
                    return 'badge bg-danger';
                default:
                    return 'badge bg-secondary';
            }
        },
        getDefaultDueDate() {
            const date = new Date();
            date.setDate(date.getDate() + 30); // 默认借阅30天
            return date.toISOString().split('T')[0];
        }
    }
}; 