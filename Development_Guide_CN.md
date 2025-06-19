# 相机商城购物系统 - 从零开始开发指南

本指南将引导您完成“相机商城购物系统”从环境搭建到功能逐步实现的整个过程。

## 一、环境准备

在开始之前，请确保您的开发环境中已安装以下软件：

**后端：**
1.  **JDK (Java Development Kit):** 版本 17 或更高版本。
    *   验证安装：`java -version`
2.  **Maven:** 版本 3.6 或更高版本 (Spring Boot项目通常自带Maven Wrapper `mvnw`，可以不单独安装)。
    *   验证安装：`mvn -version`
3.  **MySQL:** 版本 8.0。
    *   确保MySQL服务已启动，并记住您的root用户密码或创建一个专门的数据库用户。
4.  **IDE (集成开发环境):**
    *   推荐 IntelliJ IDEA (Ultimate版对Spring Boot支持更佳，Community版也可)。
    *   或者 Eclipse IDE for Java EE Developers, VS Code with Java Extension Pack。

**前端：**
1.  **Node.js:** 版本 16.x, 18.x 或更高版本 (建议使用LTS版本)。
    *   验证安装：`node -v` 和 `npm -v`
2.  **Vue CLI (可选，如果选择使用Vue CLI创建项目):**
    *   安装：`npm install -g @vue/cli`
    *   验证安装：`vue --version`
3.  **Vite (推荐，本项目结构基于Vite):** Vite通常作为项目依赖安装，无需全局安装。
4.  **代码编辑器/IDE:**
    *   推荐 VS Code (Visual Studio Code) 并安装 Vue 相关插件 (如 Volar 或 VueDX)。
    *   WebStorm 也是一个很好的选择。

**通用：**
1.  **Git:** 版本控制系统。
    *   验证安装：`git --version`

## 二、项目搭建与初始化

### 1. 后端项目 (Spring Boot)

   a.  **创建数据库:**
      *   使用MySQL客户端 (如MySQL Workbench, Navicat, DBeaver或命令行) 连接到您的MySQL服务器。
      *   执行 `Database_Schema_CN.sql` 文件中的SQL语句来创建数据库 (例如 `camera_ecommerce_db`) 和所需的表。
      *   **重要:** 记下数据库名、用户名和密码，后续配置到后端项目中。

   b.  **获取后端代码:**
      *   将先前生成的后端代码文件（`pom.xml`, 以及 `src` 目录下的所有Java类和配置文件）放置在 `camera-ecommerce-backend` 文件夹中。

   c.  **配置数据库连接:**
      *   打开 `camera-ecommerce-backend/src/main/resources/application.yml` (或 `application.properties`)。
      *   修改以下数据库连接配置，替换为您的实际信息：
        ```yaml
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/camera_ecommerce_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
            username: your_mysql_username # 替换为您的MySQL用户名
            password: your_mysql_password # 替换为您的MySQL密码
            driver-class-name: com.mysql.cj.jdbc.Driver
          # ... 其他配置
          jpa: # 如果使用了JPA，这里是MyBatis-Plus，所以JPA配置可选
            hibernate:
              ddl-auto: none # 已手动创建表，不让Hibernate自动操作
            show-sql: true
          # ...
          # JWT Secret Key (非常重要，确保其安全且足够复杂)
          jwt:
            secret: YourVeryLongAndSecureSecretKeyForCameraEcommerceSystemAtLeast256BitsLong
            # 可以考虑使用环境变量注入，而不是硬编码

        # MyBatis Plus 配置
        mybatis-plus:
          mapper-locations: classpath*:/mapper/**/*.xml # 如果有XML映射文件
          global-config:
            db-config:
              id-type: auto # 全局主键策略
          configuration:
            map-underscore-to-camel-case: true # 开启驼峰命名转换
            log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 控制台打印SQL

        # SpringDoc OpenAPI (Swagger) 配置 (可选)
        springdoc:
          api-docs:
            path: /v3/api-docs # API文档路径
          swagger-ui:
            path: /swagger-ui.html # Swagger UI访问路径
            display-request-duration: true
            tags-sorter: alpha
            operations-sorter: alpha
        ```

   d.  **导入项目到IDE:**
      *   打开 IntelliJ IDEA 或其他IDE。
      *   选择 "Open" 或 "Import Project"，然后选择 `camera-ecommerce-backend` 文件夹。
      *   IDE会自动识别为Maven项目并下载依赖。

   e.  **运行后端项目:**
      *   找到 `CameraEcommerceApplication.java` (通常在 `src/main/java/com/example/cameraecommerce/` 下)。
      *   右键点击并选择 "Run 'CameraEcommerceApplication.main()'"。
      *   观察控制台输出，确保没有错误，并且Spring Boot应用成功启动 (通常在 `localhost:8080` 或您配置的端口)。
      *   访问 `http://localhost:8080/swagger-ui.html` (或您配置的Swagger路径) 来查看API文档。

### 2. 前端项目 (Vue 3)

   a.  **获取前端代码:**
      *   将先前生成的前端代码文件（`package.json`, `vite.config.js` 或 `vue.config.js`, `index.html` (Vite) 以及 `public` 和 `src` 目录下的所有文件）放置在 `camera-ecommerce-frontend` 文件夹中。

   b.  **配置API基础路径:**
      *   在 `camera-ecommerce-frontend` 目录下创建或修改 `.env.development` 文件 (用于开发环境)。
      *   添加以下行，指向您的后端API地址：
        ```
        VITE_API_BASE_URL=http://localhost:8080/api
        ```
        (如果您的后端API前缀不是 `/api`，或者端口不同，请相应修改。)

   c.  **安装依赖:**
      *   打开终端或命令提示符。
      *   导航到 `camera-ecommerce-frontend` 目录: `cd path/to/camera-ecommerce-frontend`
      *   运行命令安装项目依赖：
        ```bash
        npm install
        # 或者 yarn install
        ```

   d.  **运行前端开发服务器:**
      *   在 `camera-ecommerce-frontend` 目录下，运行：
        ```bash
        npm run dev
        # 或者 yarn dev
        ```
      *   控制台会显示前端应用的访问地址 (通常是 `http://localhost:5173` 或类似端口)。
      *   在浏览器中打开此地址。

## 三、功能实现顺序建议

建议按照以下模块顺序逐步开发和测试，先后端再前端，或者并行开发（如果多人协作）。

### 1. 用户认证与管理 (核心)

   a.  **后端:**
      *   **实体与Mapper:** 确认 `User.java`, `UserMapper.java` 无误。
      *   **安全配置:** 仔细检查 `SecurityConfig.java`, `JwtTokenUtil.java`, `JwtUserDetailsService.java`, `JwtRequestFilter.java`, `JwtAuthenticationEntryPoint.java`。确保密码加密、JWT生成与验证逻辑正确。
      *   **DTO:** 确认 `RegisterRequest.java`, `LoginRequest.java`, `JwtResponse.java`, `UserUpdateRequest.java`, `PasswordUpdateRequest.java`。
      *   **Service:** 实现 `AuthService` (注册、登录逻辑)，`UserService` (获取用户信息、更新信息、修改密码)。
      *   **Controller:** 实现 `AuthController` (处理 `/api/auth/register`, `/api/auth/login` 路由)，`UserController` (处理 `/api/users/me`, `/api/users/password` 路由)。
      *   **测试:** 使用Postman或Swagger UI测试注册和登录接口，确保能获取JWT。测试需要认证的接口是否能正确验证Token并返回数据或拒绝访问。

   b.  **前端:**
      *   **API服务:** 确认 `services/auth.service.js`, `services/user.service.js` 中的API调用函数正确。
      *   **路由:** 确认 `router/index.js` 中认证相关路由 (`/login`, `/register`, `/profile`) 和导航守卫逻辑正确。
      *   **页面组件:**
          *   `views/auth/Login.vue`: 实现登录表单、API调用、Token存储、导航逻辑。
          *   `views/auth/Register.vue`: 实现注册表单、API调用、导航逻辑。
          *   `components/layout/Navbar.vue`: 实现根据登录状态显示不同内容 (用户名/退出 vs 登录/注册链接)。
          *   `views/user/Profile.vue`: 实现获取和展示当前用户信息，以及更新信息和修改密码的表单和逻辑。
      *   **状态管理 (可选):** 如果使用Pinia/Vuex，此时可以设置用户状态模块。

### 2. 商品展示

   a.  **后端:**
      *   **实体与Mapper:** `ProductCategory.java`, `ProductCategoryMapper.java`, `Product.java`, `ProductMapper.java`。
      *   **Service:** `ProductCategoryService` (获取分类列表)，`ProductService` (分页查询商品、获取商品详情)。
      *   **Controller:** `CategoryController`, `ProductController`。
      *   **测试:** 测试分类查询和商品列表/详情查询接口。

   b.  **前端:**
      *   **API服务:** `services/category.service.js`, `services/product.service.js`。
      *   **组件:**
          *   `components/product/ProductCard.vue`: 商品卡片组件。
      *   **页面组件:**
          *   `views/Home.vue`: 展示热门分类和推荐商品。
          *   `views/product/ProductList.vue`: 实现分类筛选、关键词搜索、排序、分页加载商品列表。
          *   `views/product/ProductDetail.vue`: 展示商品详细信息，包括图片、价格、描述、库存等。
      *   **路由:** 确认商品列表和详情页路由。

### 3. 购物车功能

   a.  **后端:**
      *   **实体与Mapper:** `CartItem.java` (如果持久化), `CartItemMapper.java`。
      *   **DTO:** `CartItemAddRequest.java`, `CartItemUpdateRequest.java`。
      *   **Service:** `CartService` (添加、查看、修改数量、删除购物车项)。
      *   **Controller:** `CartController`。
      *   **测试:** 测试购物车相关接口，确保用户认证有效。

   b.  **前端:**
      *   **API服务:** `services/cart.service.js`。
      *   **页面组件:** `views/cart/ShoppingCart.vue`:
          *   展示购物车商品列表。
          *   实现修改数量、删除商品、选中/取消选中商品。
          *   计算选中商品总价。
          *   “去结算”按钮。
      *   **集成:** 在商品详情页和列表页的 `ProductCard.vue` 中添加“加入购物车”功能。
      *   **Navbar:** (可选) 更新购物车图标以显示商品数量。

### 4. 订单与支付 (模拟)

   a.  **后端:**
      *   **实体与Mapper:** `Order.java`, `OrderMapper.java`, `OrderItem.java`, `OrderItemMapper.java`。
      *   **DTO:** `OrderCreateRequest.java`。
      *   **Service:** `OrderService` (创建订单、查询订单列表/详情、模拟支付、取消订单、更新订单状态 - 后台)。
          *   **库存管理:** 创建订单时扣减库存，取消订单时归还库存。
          *   **促销逻辑:** (后端体现) 计算订单价格时应用促销规则。
      *   **Controller:** `OrderController`。
      *   **测试:** 测试订单创建、查询、支付模拟接口。

   b.  **前端:**
      *   **API服务:** `services/order.service.js`。
      *   **页面组件:**
          *   `views/order/Checkout.vue`: 确认订单信息（商品、收货地址）、提交订单。
          *   `views/order/OrderList.vue`: 展示用户订单列表、分页、按状态筛选。
          *   `views/order/OrderDetail.vue`: 展示订单详细信息，包括订单状态、商品项、收货信息等。
          *   实现“去支付”、“取消订单”、“确认收货”（前端按钮，后端逻辑）等操作。

## 四、进一步完善与部署

1.  **完善前端UI/UX:** 根据需要调整样式，优化用户体验。
2.  **表单校验:** 前后端都需要进行严格的表单校验。
3.  **错误处理:** 完善全局异常处理和特定业务异常处理。
4.  **日志记录:** 在后端关键业务点添加日志记录。
5.  **安全性增强:**
    *   防止XSS, CSRF (虽然JWT一定程度上避免了传统CSRF，但仍需注意API设计)。
    *   输入参数合法性检查。
    *   权限细化 (例如后台管理功能)。
6.  **测试:**
    *   单元测试 (后端Service层，前端工具函数)。
    *   集成测试 (测试API接口)。
    *   端到端测试 (模拟用户操作流程)。
7.  **代码优化与重构:** 提高代码可读性和性能。
8.  **构建与部署:**
    *   **后端:** 使用 `mvn package` 打包成JAR文件，然后部署到服务器 (如使用 `java -jar app.jar`)。考虑使用Docker容器化部署。
    *   **前端:** 使用 `npm run build` (或 `yarn build`) 打包静态文件，然后部署到Web服务器 (如Nginx, Apache) 或静态站点托管服务。配置Nginx反向代理API请求到后端服务。

## 五、总结

这是一个相对复杂的项目，请务必耐心，一步一个脚印。遇到问题时，善用搜索引擎、查阅官方文档、利用IDE的调试功能。祝您开发顺利！
