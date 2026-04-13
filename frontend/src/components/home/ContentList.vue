<template>
  <div class="content-list">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner">🌊</div>
      <p>加载中...</p>
    </div>

    <!-- 内容列表 -->
    <div v-else class="content-items">
      <div 
        v-for="item in contents" 
        :key="item.id"
        class="content-card"
      >
        <!-- 用户信息 -->
        <div class="card-header">
          <div class="user-info">
            <img 
              :src="item.avatar" 
              alt="avatar"
              class="user-avatar"
            />
            <div class="user-details">
              <div class="user-name">{{ item.author }}</div>
              <div class="post-time">{{ item.time }}</div>
            </div>
          </div>
          <button class="more-btn">⋯</button>
        </div>

        <!-- 内容正文 -->
        <div class="card-content">
          <p class="content-text">{{ item.content }}</p>
          
          <!-- 话题标签 -->
          <div v-if="item.tags && item.tags.length" class="tags-row">
            <span 
              v-for="tag in item.tags" 
              :key="tag"
              class="tag-item"
            >
              #{{ tag }}
            </span>
          </div>
        </div>

        <!-- 图片/视频 -->
        <div v-if="item.images && item.images.length" class="media-grid">
          <img 
            v-for="(img, index) in item.images" 
            :key="index"
            :src="img"
            class="media-item"
            alt="content image"
          />
        </div>

        <!-- 互动栏 -->
        <div class="card-actions">
          <button 
            class="action-btn like-btn"
            :class="{ liked: item.isLiked }"
            @click="handleLike(item)"
          >
            <span class="action-icon">{{ item.isLiked ? '❤️' : '🤍' }}</span>
            <span class="action-text">{{ item.likes }}</span>
          </button>

          <button 
            class="action-btn comment-btn"
            @click="handleComment(item)"
          >
            <span class="action-icon">💬</span>
            <span class="action-text">{{ item.comments }}</span>
          </button>

          <button 
            class="action-btn share-btn"
            @click="handleShare(item)"
          >
            <span class="action-icon">🔗</span>
            <span class="action-text">{{ item.shares }}</span>
          </button>

          <button 
            class="action-btn collect-btn"
            :class="{ collected: item.isCollected }"
            @click="handleCollect(item)"
          >
            <span class="action-icon">{{ item.isCollected ? '⭐' : '☆' }}</span>
          </button>
        </div>

        <!-- 波浪分割线 -->
        <div class="wave-divider"></div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && contents.length === 0" class="empty-state">
      <div class="empty-icon">🌊</div>
      <p class="empty-text">暂时还没有内容哦~</p>
      <p class="empty-hint">快来发布第一条内容吧！</p>
    </div>

    <!-- 加载更多 -->
    <div v-if="!loading && contents.length > 0" class="load-more">
      <button class="load-more-btn" @click="handleLoadMore">
        <span class="boat-icon">⛵</span>
        <span>继续探索</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '../../utils/request'

const props = defineProps({
  contents: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

// 点赞
const handleLike = async (item) => {
  try {
    await request.put(`/content/like/${item.id}`)
    item.isLiked = !item.isLiked
    item.likes = item.isLiked ? item.likes + 1 : item.likes - 1
  } catch (err) {
    console.error('点赞失败', err)
  }
}

// 评论
const handleComment = (item) => {
  console.log('评论:', item.id)
  alert('评论功能开发中... 💬')
}

// 分享
const handleShare = (item) => {
  console.log('分享:', item.id)
  alert('分享功能开发中... 🔗')
}

// 收藏
const handleCollect = (item) => {
  item.isCollected = !item.isCollected
  console.log('收藏:', item.id)
}

// 加载更多
const handleLoadMore = () => {
  console.log('加载更多')
  alert('加载更多内容... ⛵')
}
</script>

<style scoped>
.content-list {
  margin-bottom: 20px;
}

/* 加载状态 */
.loading-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.loading-spinner {
  font-size: 48px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 内容卡片 */
.content-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.content-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(135, 206, 250, 0.3);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.content-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: 2px solid #E3F2FD;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-name {
  font-size: 15px;
  font-weight: 600;
  color: #1976D2;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.more-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #999;
  padding: 5px 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.more-btn:hover {
  background: #F5F5F5;
  color: #666;
}

/* 内容区域 */
.card-content {
  margin-bottom: 15px;
}

.content-text {
  font-size: 15px;
  line-height: 1.6;
  color: #333;
  margin: 0 0 12px 0;
  word-break: break-word;
}

.tags-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.tag-item {
  display: inline-block;
  padding: 4px 12px;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
  color: #1976D2;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tag-item:hover {
  background: linear-gradient(135deg, #BBDEFB 0%, #90CAF9 100%);
  transform: translateY(-2px);
}

/* 媒体网格 */
.media-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 15px;
}

.media-item {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.media-item:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 互动栏 */
.card-actions {
  display: flex;
  align-items: center;
  gap: 20px;
  padding-top: 15px;
  border-top: 2px solid #F5F5F5;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: none;
  border: none;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  color: #666;
}

.action-btn:hover {
  background: #F5F5F5;
}

.action-icon {
  font-size: 18px;
}

.action-text {
  font-weight: 500;
}

/* 点赞按钮 */
.like-btn:hover {
  background: #FFEBEE;
  color: #F44336;
}

.like-btn.liked {
  color: #F44336;
}

.like-btn.liked .action-icon {
  animation: heart-beat 0.5s ease;
}

@keyframes heart-beat {
  0%, 100% { transform: scale(1); }
  25% { transform: scale(1.3); }
  50% { transform: scale(1.1); }
}

/* 评论按钮 */
.comment-btn:hover {
  background: #E3F2FD;
  color: #2196F3;
}

/* 分享按钮 */
.share-btn:hover {
  background: #FFF3E0;
  color: #FF9800;
}

/* 收藏按钮 */
.collect-btn:hover {
  background: #FFF9C4;
  color: #FFC107;
}

.collect-btn.collected {
  color: #FFC107;
}

.collect-btn.collected .action-icon {
  animation: star-shine 0.5s ease;
}

@keyframes star-shine {
  0%, 100% { transform: scale(1) rotate(0deg); }
  50% { transform: scale(1.3) rotate(180deg); }
}

/* 波浪分割线 */
.wave-divider {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg,
    transparent 0%,
    rgba(135, 206, 250, 0.3) 25%,
    rgba(135, 206, 250, 0.5) 50%,
    rgba(135, 206, 250, 0.3) 75%,
    transparent 100%
  );
  background-size: 200% 100%;
  animation: wave-flow 3s linear infinite;
}

@keyframes wave-flow {
  0% { background-position: 0% 0%; }
  100% { background-position: 200% 0%; }
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
  animation: float 3s infinite ease-in-out;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.empty-text {
  font-size: 18px;
  font-weight: 600;
  color: #666;
  margin: 0 0 10px 0;
}

.empty-hint {
  font-size: 14px;
  color: #999;
  margin: 0;
}

/* 加载更多 */
.load-more {
  text-align: center;
  padding: 30px 20px;
}

.load-more-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 12px 30px;
  background: linear-gradient(135deg, #42A5F5 0%, #2196F3 100%);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.load-more-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(33, 150, 243, 0.4);
}

.boat-icon {
  font-size: 20px;
  animation: boat-sail 2s infinite ease-in-out;
}

@keyframes boat-sail {
  0%, 100% { transform: translateX(0) rotate(0deg); }
  50% { transform: translateX(5px) rotate(5deg); }
}

/* 响应式 */
@media (max-width: 768px) {
  .content-card {
    padding: 15px;
  }

  .card-actions {
    gap: 10px;
    overflow-x: auto;
  }

  .action-text {
    display: none;
  }

  .media-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>