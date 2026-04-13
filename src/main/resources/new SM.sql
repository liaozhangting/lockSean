-- 1. 用户表
CREATE TABLE tb_user (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                         password VARCHAR(128) DEFAULT '' COMMENT '密码，加密存储',
                         nick_name VARCHAR(32) DEFAULT '' COMMENT '昵称，默认是用户id',
                         icon VARCHAR(255) DEFAULT '' COMMENT '人物头像',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表';

-- 2. 内容表
CREATE TABLE tb_content(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    author_id BIGINT NOT NULL COMMENT '创作者id',
    title VARCHAR(255) DEFAULT NULL COMMENT '标题',
    images VARCHAR(2048) NOT NULL COMMENT '内容图片，多张以,隔开',
    content TEXT NOT NULL COMMENT '内容',
    likes INT DEFAULT 0 COMMENT '点赞数量',
    comments INT DEFAULT 0 COMMENT '评论数量',
    shares INT DEFAULT 0 COMMENT '分享数量',
    tags VARCHAR(255) DEFAULT '' COMMENT '标签，多个以,隔开',
    views BIGINT DEFAULT 0 COMMENT '浏览数量',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'

)
