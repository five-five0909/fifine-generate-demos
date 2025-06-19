# 项目目录结构设计

## 一、后端项目结构 (Java - Spring Boot - Maven)

我们将采用标准的Maven项目结构，并结合Spring Boot的最佳实践。

```
camera-ecommerce-backend/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml                 # Maven项目配置文件，包含依赖和插件
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/        # 根包名，例如 com.example.cameraecommerce
    │   │       └── cameraecommerce/
    │   │           ├── CameraEcommerceApplication.java # Spring Boot启动类
    │   │           ├── annotation/    # 自定义注解 (如果需要)
    │   │           ├── aspect/        # AOP切面 (如果需要)
    │   │           ├── common/        # 通用工具类、常量、枚举等
    │   │           │   ├── constant/  # 常量定义
    │   │           │   ├── enums/     # 枚举定义
    │   │           │   └── utils/     # 工具类 (如日期、字符串处理)
    │   │           ├── config/        # 配置类
    │   │           │   ├── MyBatisPlusConfig.java
    │   │           │   ├── SecurityConfig.java
    │   │           │   ├── SwaggerConfig.java  # SpringDoc/Swagger配置
    │   │           │   └── WebMvcConfig.java   # Web MVC相关配置 (如果需要)
    │   │           ├── controller/    # API接口层 (Spring MVC Controllers)
    │   │           │   ├── AuthController.java
    │   │           │   ├── UserController.java
    │   │           │   ├── ProductController.java
    │   │           │   ├── CategoryController.java
    │   │           │   ├── CartController.java
    │   │           │   └── OrderController.java
    │   │           ├── dto/           # 数据传输对象 (Data Transfer Objects)
    │   │           │   ├── request/   # 请求DTO
    │   │           │   │   ├── LoginRequest.java
    │   │           │   │   └── RegisterRequest.java
    │   │           │   └── response/  # 响应DTO
    │   │           │       └── JwtResponse.java
    │   │           ├── entity/        # 数据库实体类 (JPA Entities / MyBatis POJOs)
    │   │           │   ├── User.java
    │   │           │   ├── ProductCategory.java
    │   │           │   ├── Product.java
    │   │           │   ├── CartItem.java  // 购物车项逻辑实体，可能不直接映射表或作为VO
    │   │           │   ├── Order.java
    │   │           │   └── OrderItem.java
    │   │           ├── exception/     # 自定义异常类和全局异常处理
    │   │           │   ├── GlobalExceptionHandler.java
    │   │           │   └── ResourceNotFoundException.java
    │   │           ├── filter/        # Servlet过滤器 (如JwtRequestFilter已在config包下)
    │   │           ├── interceptor/   # Spring MVC拦截器 (如果需要)
    │   │           ├── mapper/        # MyBatis-Plus Mapper接口
    │   │           │   ├── UserMapper.java
    │   │           │   ├── ProductCategoryMapper.java
    │   │           │   ├── ProductMapper.java
    │   │           │   ├── OrderMapper.java
    │   │           │   └── OrderItemMapper.java
    │   │           ├── scheduled/     # 定时任务 (如果需要)
    │   │           ├── security/      # 安全相关，如 UserDetailsService, JwtTokenUtil
    │   │           │   ├── JwtTokenUtil.java
    │   │           │   ├── JwtUserDetailsService.java
    │   │           │   └── JwtAuthenticationEntryPoint.java
    │   │           └── service/       # 业务逻辑层接口
    │   │               ├── impl/      # 业务逻辑层实现
    │   │               │   ├── AuthServiceImpl.java
    │   │               │   ├── UserServiceImpl.java
    │   │               │   ├── ProductServiceImpl.java
    │   │               │   ├── CartServiceImpl.java
    │   │               │   └── OrderServiceImpl.java
    │   │               ├── AuthService.java
    │   │               ├── UserService.java
    │   │               ├── ProductCategoryService.java
    │   │               ├── ProductService.java
    │   │               ├── CartService.java
    │   │               └── OrderService.java
    │   └── resources/
    │       ├── application.yml     # Spring Boot主配置文件 (或 application.properties)
    │       ├── application-dev.yml # 开发环境配置
    │       ├── application-prod.yml# 生产环境配置
    │       ├── static/             # 静态资源 (通常不由后端API项目提供，除非有管理后台静态页)
    │       ├── templates/          # 服务端模板 (如果使用Thymeleaf等，但前后端分离项目通常不需要)
    │       └── mapper/             # MyBatis XML映射文件 (如果使用XML配置SQL)
    │           ├── UserMapper.xml
    │           └── ProductMapper.xml
    └── test/
        └── java/
            └── com/
                └── example/
                    └── cameraecommerce/
                        ├── controller/
                        │   └── AuthControllerTest.java
                        └── service/
                            └── UserServiceImplTest.java
```

**说明:**
- **`com.example.cameraecommerce`**: 这是示例的根包名，你需要替换成你自己的。
- **模块化**: 代码按照功能模块（如`user`, `product`, `order`）和分层（`controller`, `service`, `mapper`, `entity`）进行组织。
- **`dto`**: 用于封装API请求和响应的数据结构，有助于接口定义清晰和参数校验。
- **`common`**: 存放全局通用的工具类、常量、枚举等。
- **`security`**: 存放与Spring Security和JWT相关的类。
- **配置文件**: 使用`.yml`格式，并按环境分离配置。
- **MyBatis XML**: 如果选择使用XML编写复杂SQL，可以放在`resources/mapper/`目录下。

## 二、前端项目结构 (Vue 3 - Vite/Vue CLI)

我们将使用Vite或Vue CLI生成的标准项目结构，并进行一些约定。

```
camera-ecommerce-frontend/
├── node_modules/           # 项目依赖
├── public/                 # 静态资源，会被直接复制到打包输出的根目录
│   ├── favicon.ico
│   └── index.html          # SPA的HTML入口文件
├── src/
│   ├── App.vue             # 根Vue组件
│   ├── main.js             # 应用入口文件 (初始化Vue, Router, UI库等)
│   ├── assets/             # 静态资源 (会被Webpack/Vite处理)
│   │   ├── images/
│   │   │   └── logo.png
│   │   └── styles/
│   │       ├── base.css    # 基础样式重置
│   │       └── theme.css   # 主题样式 (如果需要)
│   ├── components/         # 全局可复用组件
│   │   ├── common/         # 通用基础组件 (如按钮, 输入框的封装 - 如果需要)
│   │   │   └── AppLogo.vue
│   │   ├── layout/         # 布局组件
│   │   │   ├── Navbar.vue
│   │   │   ├── Footer.vue
│   │   │   └── MainLayout.vue # 主要页面布局
│   │   └── product/        # 特定模块的组件 (如商品卡片)
│   │       └── ProductCard.vue
│   ├── router/             # Vue Router配置
│   │   └── index.js
│   ├── services/           # API服务层 (或称api, store)
│   │   ├── axiosInstance.js # Axios实例和拦截器配置
│   │   ├── auth.service.js
│   │   ├── user.service.js
│   │   ├── product.service.js
│   │   ├── category.service.js
│   │   ├── cart.service.js
│   │   └── order.service.js
│   ├── store/              # 状态管理 (Pinia/Vuex，按需使用)
│   │   ├── modules/
│   │   │   └── cart.js     # 购物车模块的状态
│   │   └── index.js        # 主store配置文件
│   ├── utils/              # 通用工具函数
│   │   ├── request.js      # 对axiosInstance的进一步封装 (可选，也可直接用service)
│   │   └── validators.js   # 表单校验规则 (可选)
│   └── views/              # 页面级组件 (路由对应的组件)
│       ├── auth/
│       │   ├── Login.vue
│       │   └── Register.vue
│       ├── user/
│       │   └── Profile.vue
│       ├── product/
│       │   ├── ProductList.vue
│       │   └── ProductDetail.vue
│       ├── cart/
│       │   └── ShoppingCart.vue
│       ├── order/
│       │   ├── Checkout.vue
│       │   ├── OrderList.vue
│       │   └── OrderDetail.vue
│       ├── Home.vue
│       └── NotFound.vue    # 404页面
├── .env                    # 环境变量 (通用)
├── .env.development        # 开发环境变量
├── .env.production         # 生产环境变量
├── .eslintrc.js            # ESLint配置文件 (如果使用)
├── .gitignore
├── index.html              # Vite项目的入口HTML (如果使用Vue CLI则在public下)
├── package.json            # NPM依赖和脚本配置
├── vite.config.js          # Vite配置文件 (或 vue.config.js for Vue CLI)
└── README.md
```

**说明:**
- **`src/assets`**: 存放会被构建工具处理的静态资源，如图片、CSS预处理器文件。
- **`src/components`**: 存放可复用的UI组件。可以按功能或通用性进一步分子目录。
    - `common/`: 非常通用的组件。
    - `layout/`: 页面布局相关的组件。
- **`src/router`**: 存放Vue Router的路由配置。
- **`src/services` (或 `src/api`)**: 存放与后端API交互的逻辑，通常会封装Axios。
- **`src/store`**: 存放状态管理逻辑（Pinia或Vuex）。按需使用，对于购物车等场景可能需要。
- **`src/utils`**: 存放通用的JavaScript工具函数。
- **`src/views` (或 `src/pages`)**: 存放页面级组件，即路由直接渲染的组件。
- **环境变量**: 使用`.env`系列文件管理不同环境的配置（如API基础路径）。
- **Vite/Vue CLI**: 结构基本一致，Vite会将`index.html`放在项目根目录，而Vue CLI通常放在`public`目录。

这些结构提供了良好的组织性和可扩展性，可以根据项目的具体复杂度和团队习惯进行微调。
