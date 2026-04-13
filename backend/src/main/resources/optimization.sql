-- 数据库性能优化 SQL

-- 1. 游标分页 vs 传统 OFFSET 分页
-- 传统 OFFSET 分页（深分页性能差）：
-- SELECT * FROM tb_content WHERE deleted = 0 ORDER BY id DESC LIMIT 1000000, 10;
-- 原因：需要扫描 1000000+10 条数据，然后丢弃前 1000000 条

-- 游标分页（性能稳定）：
-- SELECT * FROM tb_content WHERE deleted = 0 AND id < 1000000 ORDER BY id DESC LIMIT 10;
-- 原因：直接通过主键索引定位，只需要扫描 10 条数据

-- 2. 复合索引设计
-- 针对多条件筛选场景，创建复合索引

-- 内容表复合索引：(type, deleted, id) 或 (type, deleted, create_time)
-- 适用于：按类型筛选 + 分页查询
CREATE INDEX idx_type_deleted_id ON tb_content(type, deleted, id DESC);

-- 用户内容列表索引：(author_id, deleted, id)
-- 适用于：查询某用户的内容列表
CREATE INDEX idx_author_deleted_id ON tb_content(author_id, deleted, id DESC);

-- 创建时间索引：(deleted, create_time)
-- 适用于：按时间排序的分页查询
CREATE INDEX idx_deleted_create_time ON tb_content(deleted, create_time DESC);

-- 3. 覆盖索引
-- 如果查询只需要 id, title, create_time 等字段，可以利用覆盖索引避免回表
-- 例如：SELECT id, title, create_time FROM tb_content WHERE id < ? ORDER BY id DESC LIMIT 10;
-- 确保 idx_id_title_create_time 覆盖了这些字段

-- 4. 慢查询分析示例
-- 执行计划分析：
EXPLAIN SELECT * FROM tb_content WHERE deleted = 0 ORDER BY id DESC LIMIT 10 OFFSET 100000;

-- 使用游标分页优化：
EXPLAIN SELECT * FROM tb_content WHERE deleted = 0 AND id < ? ORDER BY id DESC LIMIT 10;

-- 5. 缓存与数据库一致性保证
-- 使用数据库唯一索引保证幂等写入
ALTER TABLE comment ADD UNIQUE KEY uk_content_sequence (content_id, sequence_no);

-- 6. 分片元数据表（如果使用手动分片）
-- CREATE TABLE IF NOT EXISTS comment_shard_meta (
--     content_id BIGINT NOT NULL COMMENT '内容ID',
--     shard_index INT NOT NULL COMMENT '分片序号',
--     shard_start_seq BIGINT NOT NULL COMMENT '分片起始序列号',
--     shard_end_seq BIGINT NOT NULL COMMENT '分片结束序列号',
--     PRIMARY KEY (content_id, shard_index)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
