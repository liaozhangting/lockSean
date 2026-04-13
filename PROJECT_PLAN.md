# LockSeanSocialMedia 项目要点与开发计划

## 技术栈
Spring Boot、MySQL、Redis、RabbitMQ、ElasticSearch、Vue

## 项目背景
一个支持图文/视频发布的 UGC 社区平台，涵盖内容生产、互动到搜索的全链路功能

---

## 功能要点

### (1) 评论热点分片 ✅
针对高热帖子评论查询压力，以评论增速作为热点识别阈值，触发后异步预热缓存；采用 Redis INCR 生成评论序列号，ZSet 存储分片元数据以分片起始序列号为 score，查询时定位分片键后取对应数据，实现评论数据的分片管理

**实现计划：**
- [x] 评论实体设计 (Comment Entity) ✅
- [x] Redis INCR 评论序列号生成 ✅
- [x] ZSet 分片元数据存储 ✅
- [x] 热点识别阈值触发机制 ✅
- [x] 分片查询接口 ✅

**已创建文件：**
- `entity/Comment.java` - 评论实体
- `mapper/CommentMapper.java` - 评论Mapper
- `dto/CommentDTO.java` - 评论DTO
- `service/CommentService.java` - 评论服务接口
- `service/impl/CommentServiceImpl.java` - 评论服务实现
- `controller/CommentController.java` - 评论控制器
- `utils/CommentShardUtils.java` - 评论分片工具类
- `common/contstant/CommentConstants.java` - 评论常量
- `resources/comment.sql` - 评论表SQL

### (2) 搜索功能优化 ✅
针对模糊查询性能较差的问题（LIKE 查询 1~2 s），引入 ES 实现全文检索，基于 IK 分词器支持中文分词，搜索响应时间优化至 200-300ms；数据写入时通过 MQ 异步同步至 ES，保证 MySQL 与 ES 的数据一致性

**实现计划：**
- [x] Elasticsearch 配置 ✅
- [x] 内容索引映射设计 ✅
- [x] IK 分词器集成 ✅
- [x] MQ 异步同步 ES ✅
- [x] 搜索接口实现 ✅

**已创建文件：**
- `entity/ContentDocument.java` - ES 文档实体
- `repository/ContentSearchRepository.java` - ES Repository
- `service/ContentSearchService.java` - 搜索服务接口
- `service/impl/ContentSearchServiceImpl.java` - 搜索服务实现
- `dto/SearchDTO.java` - 搜索 DTO
- `controller/SearchController.java` - 搜索控制器
- `mq/EsSyncProducer.java` - ES 同步消息生产者
- `mq/EsSyncConsumer.java` - ES 同步消息消费者
- `common/contstant/MqConstants.java` - MQ 常量

### (3) 数据库性能优化 ✅
通过 Explain 分析定位慢查询，针对帖子列表的分页查询，从传统的 offset 分页改为基于自增 ID 的游标查询，避免深分页的全表扫描；针对多条件筛选场景，设计复合索引并使用覆盖索引优化，查询耗时降低约 70%

**实现计划：**
- [x] 游标分页改造 (Cursor Pagination) ✅
- [x] 复合索引设计 ✅
- [x] 覆盖索引优化 ✅
- [x] 分页接口改造 ✅

**已创建/修改文件：**
- `dto/PageCursorDTO.java` - 游标分页 DTO
- `entity/Content.java` - 更新内容实体
- `mapper/ContentMapper.java` - 添加游标分页查询
- `service/ContentService.java` - 更新服务接口
- `service/impl/ContentServiceImpl.java` - 实现游标分页
- `controller/ContentController.java` - 更新控制器
- `resources/optimization.sql` - 索引优化 SQL

### (4) 缓存与数据库一致性 ✅
评论优先写入缓存，MQ 实时触发落库作为主路径，定时任务定期扫描序列号断层作为兜底补录，结合数据库唯一索引保证幂等写入；MQ 消费失败消息进入死信队列，定期扫描死信队列恢复丢失数据

**实现计划：**
- [x] 评论缓存写入逻辑 ✅
- [x] MQ 落库机制 ✅
- [x] 定时任务扫描断层 ✅
- [x] 死信队列处理 ✅
- [x] 幂等写入保证 ✅

**已创建/修改文件：**
- `mq/CommentConsumer.java` - 评论 MQ 消费者
- `common/task/SequenceGapScanTask.java` - 序列号断层扫描定时任务
- `mapper/CommentMapper.java` - 添加 selectMaxSequenceNo
- `LockSeanSocialMediaApplication.java` - 启用 @EnableScheduling

### (5) 接口安全与鉴权 ✅
基于 JWT 实现无状态登录鉴权，通过拦截器统一校验 Token 有效性；针对高频接口设计限流防刷机制，自定义注解结合 AOP 切面实现，以 Redis 计数器控制请求频率，对业务代码零侵入

**实现计划：**
- [x] JWT 鉴权拦截器完善 ✅
- [x] 限流注解定义 ✅
- [x] AOP 切面实现 ✅
- [x] Redis 计数器限流 ✅
- [x] 接口集成测试 ✅

**已创建/修改文件：**
- `common/annotation/RateLimit.java` - 限流注解
- `common/aspect/RateLimitAspect.java` - 限流 AOP 切面
- `controller/TestController.java` - 限流测试控制器
- `pom.xml` - 添加 AOP 依赖

---

## 开发进度

| 日期 | 内容 | 状态 |
|------|------|------|
| 2026-04-08 | 项目要点文档创建 | ✅ |
| 2026-04-08 | 评论热点分片 | ✅ |
| 2026-04-08 | 搜索功能优化 | ✅ |
| 2026-04-08 | 数据库性能优化 | ✅ |
| 2026-04-08 | 缓存与数据库一致性 | ✅ |
| 2026-04-08 | 接口安全与鉴权 | ✅ |

**所有功能模块已完成！**

---

## 项目文件清单

### Entity
- `Content.java` - 内容实体
- `Comment.java` - 评论实体
- `User.java` - 用户实体
- `ContentDocument.java` - ES 文档实体
- `vo/ContentVO.java` - 内容视图对象

### Mapper
- `ContentMapper.java` - 内容 Mapper
- `CommentMapper.java` - 评论 Mapper
- `UserMapper.java` - 用户 Mapper

### Service
- `ContentService.java` - 内容服务接口
- `ContentServiceImpl.java` - 内容服务实现
- `CommentService.java` - 评论服务接口
- `CommentServiceImpl.java` - 评论服务实现
- `ContentSearchService.java` - 搜索服务接口
- `ContentSearchServiceImpl.java` - 搜索服务实现
- `UserService.java` - 用户服务接口
- `UserServiceImpl.java` - 用户服务实现

### Controller
- `ContentController.java` - 内容控制器
- `CommentController.java` - 评论控制器
- `SearchController.java` - 搜索控制器
- `UserController.java` - 用户控制器
- `TestController.java` - 测试控制器

### MQ
- `EsSyncProducer.java` - ES 同步消息生产者
- `EsSyncConsumer.java` - ES 同步消息消费者
- `CommentConsumer.java` - 评论消息消费者

### Utils
- `CommentShardUtils.java` - 评论分片工具类
- `CosUtils.java` - 腾讯云 COS 工具类
- `IPUtils.java` - IP 工具类
- `JWUtil.java` - JWT 工具类
- `Result.java` - 统一响应结果

### Config
- `ElasticsearchConfig.java` - ES 配置
- `RedisConfig.java` - Redis 配置
- `RabbitMQConfig.java` - MQ 配置
- `CorsConfig.java` - 跨域配置
- `WebMvcConfig.java` - Web MVC 配置

### Common
- `RateLimit.java` - 限流注解
- `RateLimitAspect.java` - 限流切面
- `CommentConstants.java` - 评论常量
- `MqConstants.java` - MQ 常量
- `TokenInterceptor.java` - Token 拦截器
- `BaseContext.java` - 上下文
- `SequenceGapScanTask.java` - 序列号断层扫描定时任务

### SQL
- `comment.sql` - 评论表
- `optimization.sql` - 索引优化 SQL

---

*最后更新：2026-04-08 08:50*
