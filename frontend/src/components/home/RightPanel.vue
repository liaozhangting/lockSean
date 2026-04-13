<template>
  <aside class="right-panel">
    <!-- 用户卡片 -->
    <div class="user-card">
      <div class="user-card-bg">
        <div class="bg-wave"></div>
      </div>
      <div class="user-card-content">
        <img 
          :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${userInfo?.nickname}`"
          alt="avatar"
          class="profile-avatar"
        />
        <h3 class="profile-name">{{ userInfo?.nickname }}</h3>
        <p class="profile-bio">🌊 热爱分享的航海者</p>
        
        <div class="profile-stats">
          <div class="stat-box">
            <div class="stat-num">{{ stats?.posts || 0 }}</div>
            <div class="stat-label">创作</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-box">
            <div class="stat-num">{{ stats?.followers || 0 }}</div>
            <div class="stat-label">粉丝</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-box">
            <div class="stat-num">{{ stats?.likes || 0 }}</div>
            <div class="stat-label">获赞</div>
          </div>
        </div>

        <button class="edit-profile-btn" @click="handleEditProfile">
          <span class="btn-icon">✏️</span>
          <span>编辑资料</span>
        </button>
      </div>
    </div>

    <!-- 推荐用户 -->
    <div class="recommend-section">
      <div class="section-header">
        <span class="header-icon">👥</span>
        <span class="header-title">推荐关注</span>
      </div>
      <div class="recommend-list">
        <div 
          v-for="user in recommendUsers"
          :key="user.id"
          class="recommend-item"
        >
          <img :src="user.avatar" alt="avatar" class="recommend-avatar" />
          <div class="recommend-info">
            <div class="recommend-name">{{ user.name }}</div>
            <div class="recommend-desc">{{ user.desc }}</div>
          </div>
          <button 
            class="follow-btn"
            :class="{ followed: user.isFollowed }"
            @click="toggleFollow(user)"
          >
            {{ user.isFollowed ? '✓' : '+' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 今日数据 -->
    <div class="daily-stats">
      <div class="section-header">
        <span class="header-icon">📊</span>
        <span class="header-title">今日数据</span>
      </div>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-icon views">👁️</div>
          <div class="stat-content">
            <div class="stat-value">{{ dailyStats.views }}</div>
            <div class="stat-name">浏览</div>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon likes">❤️</div>
          <div class="stat-content">
            <div class="stat-value">{{ dailyStats.likes }}</div>
            <div class="stat-name">点赞</div>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon comments">💬</div>
          <div class="stat-content">
            <div class="stat-value">{{ dailyStats.comments }}</div>
            <div class="stat-name">评论</div>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon followers">➕</div>
          <div class="stat-content">
            <div class="stat-value">{{ dailyStats.newFollowers }}</div>
            <div class="stat-name">新粉丝</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 简州猫每日一言 -->
    <div class="daily-quote">
      <div class="quote-cat">🐱</div>
      <div class="quote-content">
        <div class="quote-text">"{{ dailyQuote }}"</div>
        <div class="quote-author">—— 简州猫</div>
      </div>
    </div>

    <!-- 装饰元素 -->
    <div class="decoration-elements">
      <span class="deco-fish">🐟</span>
      <span class="deco-shell">🐚</span>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  userInfo: Object,
  stats: Object
})

// 推荐用户
const recommendUsers = ref([
  {
    id: 1,
    name: '海洋探险家',
    desc: '分享海洋知识',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=explorer',
    isFollowed: false
  },
  {
    id: 2,
    name: '技术船长',
    desc: 'Java开发者',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=captain',
    isFollowed: false
  },
  {
    id: 3,
    name: '猫咪水手',
    desc: '猫奴一枚',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=sailor',
    isFollowed: false
  }
])

// 今日数据
const dailyStats = ref({
  views: 234,
  likes: 67,
  comments: 23,
  newFollowers: 8
})

// 每日一言
const quotes = [
  '保持热爱，奔赴山海',
  '代码改变世界，创意点亮生活',
  '每一次分享，都是一次成长',
  '技术让世界更美好',
  '简单生活，快乐编程',
  '今天也要开心地写代码呀~'
]

const dailyQuote = ref('')

// 编辑资料
const handleEditProfile = () => {
  console.log('编辑资料')
  alert('编辑资料功能开发中... ✏️')
}

// 关注/取消关注
const toggleFollow = (user) => {
  user.isFollowed = !user.isFollowed
  console.log(user.isFollowed ? '已关注' : '已取消关注', user.name)
}

onMounted(() => {
  // 随机选择一条每日一言
  dailyQuote.value = quotes[Math.floor(Math.random() * quotes.length)]
})
</script>

<style scoped>
.right-panel {
  position: sticky;
  top: 100px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: fit-content;
}

/* 用户卡片 */
.user-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(135, 206, 250, 0.3);
  position: relative;
}

.user-card-bg {
  height: 80px;
  background: linear-gradient(135deg, #42A5F5 0%, #2196F3 100%);
  position: relative;
  overflow: hidden;
}

.bg-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 200%;
  height: 30px;
  background: radial-gradient(circle at 50% 0%, rgba(255,255,255,0.3) 0%, transparent 50%);
  background-size: 40px 30px;
  animation: wave-move 3s linear infinite;
}

@keyframes wave-move {
  0% { transform: translateX(0); }
  100% { transform: translateX(-40px); }
}

.user-card-content {
  padding: 0 20px 20px;
  text-align: center;
  position: relative;
  margin-top: -30px;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 4px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background: white;
}

.profile-name {
  font-size: 18px;
  font-weight: bold;
  color: #1976D2;
  margin: 12px 0 5px;
}

.profile-bio {
  font-size: 13px;
  color: #666;
  margin: 0 0 15px;
}

.profile-stats {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 15px 0;
  margin-bottom: 15px;
  border-top: 2px solid #F5F5F5;
  border-bottom: 2px solid #F5F5F5;
}

.stat-box {
  text-align: center;
}

.stat-num {
  font-size: 20px;
  font-weight: bold;
  color: #1976D2;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.stat-divider {
  width: 1px;
  height: 30px;
  background: #E0E0E0;
}

.edit-profile-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
  color: #1976D2;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-profile-btn:hover {
  background: linear-gradient(135deg, #BBDEFB 0%, #90CAF9 100%);
  transform: translateY(-2px);
}

.btn-icon {
  font-size: 16px;
}

/* 推荐用户 */
.recommend-section {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(135, 206, 250, 0.3);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 15px;
  padding-bottom: 12px;
  border-bottom: 2px solid #F5F5F5;
}

.header-icon {
  font-size: 18px;
}

.header-title {
  font-size: 15px;
  font-weight: 600;
  color: #1976D2;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recommend-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.recommend-item:hover {
  background: #F5F5F5;
}

.recommend-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #E3F2FD;
}

.recommend-info {
  flex: 1;
  min-width: 0;
}

.recommend-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recommend-desc {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.follow-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #42A5F5 0%, #2196F3 100%);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.follow-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.4);
}

.follow-btn.followed {
  background: linear-gradient(135deg, #66BB6A 0%, #4CAF50 100%);
}

/* 今日数据 */
.daily-stats {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(135, 206, 250, 0.3);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  background: #F5F5F5;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: #E3F2FD;
  transform: translateY(-2px);
}

.stat-icon {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  font-size: 18px;
}

.stat-icon.views {
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.stat-icon.likes {
  background: linear-gradient(135deg, #FCE4EC 0%, #F8BBD0 100%);
}

.stat-icon.comments {
  background: linear-gradient(135deg, #FFF9C4 0%, #FFF59D 100%);
}

.stat-icon.followers {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 18px;
  font-weight: bold;
  color: #1976D2;
  line-height: 1;
}

.stat-name {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}

/* 每日一言 */
.daily-quote {
  background: linear-gradient(135deg, #FFF9C4 0%, #FFF59D 100%);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(255, 235, 59, 0.5);
  position: relative;
  overflow: hidden;
}

.quote-cat {
  font-size: 48px;
  text-align: center;
  margin-bottom: 15px;
  animation: cat-shake 3s infinite ease-in-out;
}

@keyframes cat-shake {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(-5deg); }
  75% { transform: rotate(5deg); }
}

.quote-content {
  text-align: center;
}

.quote-text {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin-bottom: 10px;
  font-style: italic;
}

.quote-author {
  font-size: 12px;
  color: #999;
  font-weight: 600;
}

/* 装饰元素 */
.decoration-elements {
  position: fixed;
  pointer-events: none;
  z-index: 1;
}

.deco-fish,
.deco-shell {
  position: absolute;
  font-size: 20px;
  opacity: 0.2;
}

.deco-fish {
  top: 100px;
  right: 20px;
  animation: swim-deco 15s infinite ease-in-out;
}

.deco-shell {
  bottom: 50px;
  right: 30px;
  animation: float-deco 6s infinite ease-in-out;
}

@keyframes swim-deco {
  0%, 100% { transform: translateX(0) translateY(0); }
  50% { transform: translateX(-30px) translateY(20px); }
}

@keyframes float-deco {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-15px) rotate(10deg); }
}

/* 响应式 */
@media (max-width: 1200px) {
  .right-panel {
    display: none;
  }
}
</style>