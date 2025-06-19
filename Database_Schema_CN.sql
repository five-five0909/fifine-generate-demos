-- 数据库名: camera_ecommerce_db (示例，您可以根据实际情况修改)
-- CREATE DATABASE IF NOT EXISTS camera_ecommerce_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE camera_ecommerce_db;

-- 1. 用户表 (t_user)
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` BIGINT AUTO_INCREMENT COMMENT '用户ID，主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名，唯一',
  `password` VARCHAR(100) NOT NULL COMMENT '密码，加密存储',
  `email` VARCHAR(100) COMMENT '邮箱，唯一',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `phone_number` VARCHAR(20) COMMENT '联系电话',
  `avatar_url` VARCHAR(255) COMMENT '头像URL',
  `status` TINYINT DEFAULT 1 COMMENT '账户状态: 0-禁用, 1-启用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 商品分类表 (t_product_category)
CREATE TABLE IF NOT EXISTS `t_product_category` (
  `id` BIGINT AUTO_INCREMENT COMMENT '分类ID，主键',
  `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
  `level` INT DEFAULT 1 COMMENT '分类层级',
  `sort_order` INT DEFAULT 0 COMMENT '排序字段',
  `icon_url` VARCHAR(255) COMMENT '分类图标URL',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- 3. 商品表 (t_product)
CREATE TABLE IF NOT EXISTS `t_product` (
  `id` BIGINT AUTO_INCREMENT COMMENT '商品ID，主键',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `subtitle` VARCHAR(200) COMMENT '副标题/简短描述',
  `description` TEXT COMMENT '商品详细描述',
  `sku` VARCHAR(50) COMMENT '商品SKU，唯一（可选）',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '商品原价',
  `discount_price` DECIMAL(10, 2) COMMENT '商品折扣价 (促销应用后)',
  `stock` INT DEFAULT 0 COMMENT '库存数量',
  `main_image_url` VARCHAR(255) COMMENT '主图片URL',
  `image_urls` TEXT COMMENT '其他图片URL列表 (JSON格式或逗号分隔)',
  `status` TINYINT DEFAULT 1 COMMENT '商品状态: 0-下架, 1-上架, 2-预售等',
  `tags` VARCHAR(255) COMMENT '商品标签 (逗号分隔)',
  `brand` VARCHAR(100) COMMENT '品牌',
  `model_number` VARCHAR(100) COMMENT '型号',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `sale_count` INT DEFAULT 0 COMMENT '销量',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku` (`sku`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_name` (`name`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `t_product_category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 4. 购物车项表 (t_cart_item)
-- 注意：购物车通常是与用户关联的，且可以有多种实现方式。
-- 方式一：持久化购物车 (如下表) - 用户无论是否登录，关闭浏览器后购物车内容仍在 (如果用户登录)。
-- 方式二：临时购物车 (例如基于Redis或前端localStorage) - 这种方式下可能不需要这个表，或者这个表只在用户登录时用于同步。
-- 这里我们设计一个持久化的购物车项表。
CREATE TABLE IF NOT EXISTS `t_cart_item` (
  `id` BIGINT AUTO_INCREMENT COMMENT '购物车项ID，主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `quantity` INT NOT NULL COMMENT '商品数量',
  `price_at_addition` DECIMAL(10, 2) NOT NULL COMMENT '加入购物车时的商品单价 (冗余字段，可选)',
  `checked_status` TINYINT DEFAULT 1 COMMENT '是否选中用于结算: 0-未选中, 1-已选中',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`), -- 一个用户对一个商品在购物车中只有一条记录
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `t_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车项表';

-- 5. 订单表 (t_order)
CREATE TABLE IF NOT EXISTS `t_order` (
  `id` BIGINT AUTO_INCREMENT COMMENT '订单ID，主键',
  `order_sn` VARCHAR(64) NOT NULL COMMENT '订单编号，唯一',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
  `payable_amount` DECIMAL(10, 2) NOT NULL COMMENT '应付金额 (可能经过促销折扣)',
  `payment_method` TINYINT COMMENT '支付方式: 1-模拟支付宝, 2-模拟微信支付等',
  `payment_status` TINYINT DEFAULT 0 COMMENT '支付状态: 0-未支付, 1-已支付, 2-支付失败, 3-已退款',
  `payment_time` DATETIME COMMENT '支付时间',
  `shipping_status` TINYINT DEFAULT 0 COMMENT '发货状态: 0-未发货, 1-已发货, 2-已签收',
  `shipping_time` DATETIME COMMENT '发货时间',
  `receive_time` DATETIME COMMENT '确认收货时间',
  `order_status` TINYINT DEFAULT 0 COMMENT '订单状态: 0-待付款, 1-待发货, 2-已发货, 3-已完成, 4-已关闭(取消/超时), 5-售后中',
  `receiver_name` VARCHAR(100) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
  `receiver_address` VARCHAR(255) NOT NULL COMMENT '收货详细地址',
  `shipping_fee` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '运费',
  `promotion_info` VARCHAR(255) COMMENT '促销活动信息',
  `invoice_header` VARCHAR(255) COMMENT '发票抬头',
  `remarks` VARCHAR(500) COMMENT '订单备注',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_sn` (`order_sn`),
  KEY `idx_user_id_status` (`user_id`, `order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 6. 订单项表 (t_order_item)
CREATE TABLE IF NOT EXISTS `t_order_item` (
  `id` BIGINT AUTO_INCREMENT COMMENT '订单项ID，主键',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `order_sn` VARCHAR(64) NOT NULL COMMENT '订单编号 (冗余)',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称 (冗余)',
  `product_sku` VARCHAR(50) COMMENT '商品SKU (冗余, 可选)',
  `product_image_url` VARCHAR(255) COMMENT '商品图片URL (冗余)',
  `quantity` INT NOT NULL COMMENT '购买数量',
  `unit_price` DECIMAL(10, 2) NOT NULL COMMENT '商品单价 (成交时价格)',
  `total_price` DECIMAL(10, 2) NOT NULL COMMENT '该项总价',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
  -- 注意：这里不直接关联 t_product，因为订单生成后商品信息可能变化，订单项应保留成交快照。
  -- 如果需要追踪原始商品，可以保留 product_id，但不设置外键约束或设置为 ON DELETE SET NULL。
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

-- 索引建议：
-- t_user: username, email (已作为UNIQUE KEY)
-- t_product_category: parent_id
-- t_product: category_id, name (已创建), status, price
-- t_cart_item: user_id (已创建)
-- t_order: user_id, order_status (已创建联合索引), payment_status, shipping_status
-- t_order_item: order_id (已创建), product_id (已创建)

-- 预设商品分类数据 (示例)
-- INSERT INTO `t_product_category` (`name`, `parent_id`, `level`, `sort_order`) VALUES
-- ('数码相机', 0, 1, 1),
-- ('单反相机', 1, 2, 1),
-- ('微单相机', 1, 2, 2),
-- ('卡片相机', 1, 2, 3),
-- ('运动相机', 1, 2, 4),
-- ('相机配件', 0, 1, 2),
-- ('镜头', 6, 2, 1),
-- ('存储卡', 6, 2, 2),
-- ('三脚架', 6, 2, 3),
-- ('电池与充电器', 6, 2, 4);

```
