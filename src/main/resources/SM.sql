-- 黑马点评项目数据库建表SQL

-- 1. 用户表
CREATE TABLE tb_user (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                         phone VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
                         password VARCHAR(128) DEFAULT '' COMMENT '密码，加密存储',
                         nick_name VARCHAR(32) DEFAULT '' COMMENT '昵称，默认是用户id',
                         icon VARCHAR(255) DEFAULT '' COMMENT '人物头像',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表';

-- 2. 用户详情表
CREATE TABLE tb_user_info (
                              user_id BIGINT PRIMARY KEY COMMENT '主键，用户id',
                              city VARCHAR(255) DEFAULT '' COMMENT '城市名称',
                              introduce VARCHAR(500) DEFAULT '' COMMENT '个人介绍，不要超过500字',
                              fans INT DEFAULT 0 COMMENT '粉丝数量',
                              followee INT DEFAULT 0 COMMENT '关注的人的数量',
                              gender TINYINT DEFAULT 0 COMMENT '性别，0：男，1：女',
                              birthday DATE DEFAULT NULL COMMENT '生日',
                              credits INT DEFAULT 0 COMMENT '积分',
                              level TINYINT DEFAULT 0 COMMENT '会员级别，0~9级,0代表未开通会员',
                              create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户详情表';

-- 3. 商户信息表
CREATE TABLE tb_shop (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                         name VARCHAR(255) NOT NULL COMMENT '商户名称',
                         type_id BIGINT NOT NULL COMMENT '商户类型的id',
                         images VARCHAR(2048) NOT NULL COMMENT '商户图片，多个图片以,隔开',
                         area VARCHAR(128) DEFAULT '' COMMENT '商区',
                         address VARCHAR(255) NOT NULL COMMENT '地址',
                         x DOUBLE NOT NULL COMMENT '经度',
                         y DOUBLE NOT NULL COMMENT '维度',
                         avg_price BIGINT DEFAULT 0 COMMENT '均价，取整数',
                         sold BIGINT DEFAULT 0 COMMENT '销量',
                         comments BIGINT DEFAULT 0 COMMENT '评论数量',
                         score INT DEFAULT 50 COMMENT '评分，1~50分，乘10后为5分制',
                         open_hours VARCHAR(32) DEFAULT '' COMMENT '营业时间，例如 10:00-22:00',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '商户信息表';

-- 4. 商户类型表
CREATE TABLE tb_shop_type (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                              name VARCHAR(32) NOT NULL COMMENT '类型名称',
                              icon VARCHAR(255) DEFAULT '' COMMENT '图标',
                              sort INT DEFAULT 0 COMMENT '顺序',
                              create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '商户类型表';

-- 5. 用户日记表（达人探店日记）
CREATE TABLE tb_blog (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                         shop_id BIGINT DEFAULT NULL COMMENT '商户id',
                         user_id BIGINT NOT NULL COMMENT '用户id',
                         title VARCHAR(255) NOT NULL COMMENT '标题',
                         images VARCHAR(2048) NOT NULL COMMENT '探店的照片，最多9张，多张以,隔开',
                         content TEXT NOT NULL COMMENT '探店的文字描述',
                         liked INT DEFAULT 0 COMMENT '点赞数量',
                         comments INT DEFAULT 0 COMMENT '评论数量',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户日记表（达人探店日记）';

-- 6. 用户关注表
CREATE TABLE tb_follow (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                           user_id BIGINT NOT NULL COMMENT '用户id',
                           follow_user_id BIGINT NOT NULL COMMENT '关联的用户id',
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           UNIQUE KEY uk_user_follow (user_id, follow_user_id) COMMENT '用户关注唯一索引'
) COMMENT '用户关注表';

-- 7. 优惠券表
CREATE TABLE tb_voucher (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                            shop_id BIGINT DEFAULT NULL COMMENT '商铺id',
                            title VARCHAR(255) NOT NULL COMMENT '代金券标题',
                            sub_title VARCHAR(255) DEFAULT '' COMMENT '副标题',
                            rules VARCHAR(1024) DEFAULT '' COMMENT '使用规则',
                            pay_value BIGINT NOT NULL COMMENT '支付金额，单位是分，例如200代表2元',
                            actual_value BIGINT NOT NULL COMMENT '抵扣金额，单位是分，例如200代表2元',
                            type TINYINT NOT NULL DEFAULT 0 COMMENT '0,普通券；1,秒杀券',
                            status TINYINT NOT NULL DEFAULT 1 COMMENT '1,上架; 2,下架; 3,过期',
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            INDEX idx_shop_id (shop_id),
                            INDEX idx_type (type),
                            INDEX idx_status (status)
) COMMENT '优惠券表';

-- 8. 优惠券订单表
CREATE TABLE tb_voucher_order (
                                  id BIGINT PRIMARY KEY COMMENT '主键',
                                  user_id BIGINT NOT NULL COMMENT '下单的用户id',
                                  voucher_id BIGINT NOT NULL COMMENT '购买的代金券id',
                                  pay_type TINYINT DEFAULT 1 COMMENT '支付方式 1：余额支付；2：支付宝；3：微信',
                                  status TINYINT DEFAULT 1 COMMENT '订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款',
                                  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                                  pay_time TIMESTAMP NULL COMMENT '支付时间',
                                  use_time TIMESTAMP NULL COMMENT '核销时间',
                                  refund_time TIMESTAMP NULL COMMENT '退款时间',
                                  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  INDEX idx_user_id (user_id),
                                  INDEX idx_voucher_id (voucher_id)
) COMMENT '优惠券订单表';


-- 添加外键约束
ALTER TABLE tb_user_info ADD CONSTRAINT fk_user_info_user_id
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE;

ALTER TABLE tb_shop ADD CONSTRAINT fk_shop_type_id
    FOREIGN KEY (type_id) REFERENCES tb_shop_type(id);

ALTER TABLE tb_blog ADD CONSTRAINT fk_blog_user_id
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE;

ALTER TABLE tb_blog ADD CONSTRAINT fk_blog_shop_id
    FOREIGN KEY (shop_id) REFERENCES tb_shop(id) ON DELETE SET NULL;

ALTER TABLE tb_follow ADD CONSTRAINT fk_follow_user_id
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE;

ALTER TABLE tb_follow ADD CONSTRAINT fk_follow_follow_user_id
    FOREIGN KEY (follow_user_id) REFERENCES tb_user(id) ON DELETE CASCADE;

ALTER TABLE tb_voucher ADD CONSTRAINT fk_voucher_shop_id
    FOREIGN KEY (shop_id) REFERENCES tb_shop(id) ON DELETE SET NULL;

ALTER TABLE tb_voucher_order ADD CONSTRAINT fk_voucher_order_user_id
    FOREIGN KEY (user_id) REFERENCES tb_user(id);

ALTER TABLE tb_voucher_order ADD CONSTRAINT fk_voucher_order_voucher_id
    FOREIGN KEY (voucher_id) REFERENCES tb_voucher(id);

-- 创建必要的索引
CREATE INDEX idx_user_phone ON tb_user(phone);
CREATE INDEX idx_shop_type_id ON tb_shop(type_id);
CREATE INDEX idx_shop_area ON tb_shop(area);
CREATE INDEX idx_blog_user_id ON tb_blog(user_id);
CREATE INDEX idx_blog_shop_id ON tb_blog(shop_id);
CREATE INDEX idx_follow_user_id ON tb_follow(user_id);
CREATE INDEX idx_follow_follow_user_id ON tb_follow(follow_user_id);
