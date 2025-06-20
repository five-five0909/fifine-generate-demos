// 首页组件
window.Home = {
    template: `
        <div>
            <!-- 加载动画 -->
            <div class="loading" v-if="loading">
                <div class="loading-spinner"></div>
            </div>

            <!-- 英雄区域 -->
            <div class="hero-section">
                <h1 class="hero-title">
                    <i class="fas fa-book"></i> 教材管理系统
                </h1>
                <p class="hero-subtitle">高效管理教材资源，便捷借阅服务</p>
            </div>

            <!-- 统计信息 -->
            <div class="row">
                <div class="col-lg-3 col-md-6">
                    <div class="card stats-card text-center">
                        <div class="card-body">
                            <div class="stats-icon icon-blue">
                                <i class="fas fa-book"></i>
                            </div>
                            <div class="stats-number">{{ animatedStatistics.textbookCount }}</div>
                            <div class="stats-label">教材总数</div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="card stats-card text-center">
                        <div class="card-body">
                            <div class="stats-icon icon-green">
                                <i class="fas fa-list"></i>
                            </div>
                            <div class="stats-number">{{ animatedStatistics.categoryCount }}</div>
                            <div class="stats-label">分类数量</div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="card stats-card text-center">
                        <div class="card-body">
                            <div class="stats-icon icon-orange">
                                <i class="fas fa-hand-holding"></i>
                            </div>
                            <div class="stats-number">{{ animatedStatistics.borrowRecordCount }}</div>
                            <div class="stats-label">借阅记录</div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="card stats-card text-center">
                        <div class="card-body">
                            <div class="stats-icon icon-red">
                                <i class="fas fa-exclamation-triangle"></i>
                            </div>
                            <div class="stats-number">{{ animatedStatistics.overdueCount }}</div>
                            <div class="stats-label">逾期记录</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 快捷操作 -->
            <div class="quick-actions text-center">
                <h3 style="color: white; margin-bottom: 30px;">快捷操作</h3>
                <router-link to="/textbook" class="btn btn-primary action-btn">
                    <i class="fas fa-book"></i> 教材管理
                </router-link>
                <router-link to="/category" class="btn btn-success action-btn">
                    <i class="fas fa-list"></i> 分类管理
                </router-link>
                <router-link to="/borrow" class="btn btn-warning action-btn">
                    <i class="fas fa-hand-holding"></i> 借阅管理
                </router-link>
            </div>

            <!-- 页脚 -->
            <div class="footer">
                <p>&copy; 2024 教材管理系统. 致力于提供优质的教材管理服务.</p>
            </div>
        </div>
    `,
    data() {
        return {
            loading: false,
            statistics: {
                textbookCount: 0,
                categoryCount: 0,
                borrowRecordCount: 0,
                overdueCount: 0
            },
            animatedStatistics: {
                textbookCount: 0,
                categoryCount: 0,
                borrowRecordCount: 0,
                overdueCount: 0
            }
        }
    },
    created() {
        this.fetchStatistics();
    },
    watch: {
        statistics: {
            handler() {
                this.animateNumbers();
            },
            deep: true
        }
    },
    methods: {
        async fetchStatistics() {
            this.loading = true;
            try {
                const response = await axios.get('/api/statistics');
                if (response.data) {
                    this.statistics = {
                        textbookCount: parseInt(response.data.textbookCount) || 0,
                        categoryCount: parseInt(response.data.categoryCount) || 0,
                        borrowRecordCount: parseInt(response.data.borrowRecordCount) || 0,
                        overdueCount: parseInt(response.data.overdueCount) || 0
                    };
                }
            } catch (error) {
                alert('获取统计数据失败，请检查网络连接或联系管理员');
            } finally {
                this.loading = false;
            }
        },
        animateNumbers() {
            // 对每个统计数字做动画
            Object.keys(this.animatedStatistics).forEach(key => {
                const start = this.animatedStatistics[key];
                const end = this.statistics[key];
                if (start === end) return;
                const step = (end - start) / 30;
                let current = start;
                let count = 0;
                const animate = () => {
                    if (count >= 29) {
                        this.animatedStatistics[key] = end;
                        return;
                    }
                    current += step;
                    this.animatedStatistics[key] = Math.floor(current);
                    count++;
                    requestAnimationFrame(animate);
                };
                animate();
            });
        }
    }
}; 