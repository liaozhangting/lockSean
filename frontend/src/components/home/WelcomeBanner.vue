<template>
  <div class="welcome-banner">
    <!-- 背景装饰 -->
    <div class="banner-bg">
      <div class="cloud cloud-1">☁️</div>
      <div class="cloud cloud-2">☁️</div>
      <div class="seagull">🕊️</div>
      <div class="sun">☀️</div>
    </div>

    <!-- 主内容 -->
    <div class="banner-content">
      <div class="welcome-text">
        <h2 class="greeting">
          {{ greeting }}，{{ userInfo?.nickname }} 船长！
          <span class="wave-hand">👋</span>
        </h2>
        <p class="subtitle">
          今天也要开心地创作内容呀~ 🌊
        </p>
      </div>

      <div class="cat-mascot">
        <div class="cat-container">
          <div class="cat">🐱</div>
          <div class="speech-bubble">
            {{ randomTip }}
          </div>
        </div>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-row">
      <div class="stat-item">
        <div class="stat-icon">📝</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.posts }}</div>
          <div class="stat-label">我的创作</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon">❤️</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.likes }}</div>
          <div class="stat-label">获得点赞</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon">👥</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.followers }}</div>
          <div class="stat-label">粉丝数量</div>
        </div>
      </div>
      <div class="stat-item">
        <div class="stat-icon">📊</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.views }}</div>
          <div class="stat-label">浏览量</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  userInfo: Object
})

// 统计数据（模拟）
const stats = ref({
  posts: 42,
  likes: 567,
  followers: 128,
  views: 1234
})

// 简州猫提示语
const tips = [
  '记得多喝水哦~ 💧',
  '今天的代码写得怎么样？',
  '休息一下，看看海边的风景',
  '分享让快乐加倍！',
  '你的创作很棒！继续加油~',
  '喵~ 又是美好的一天！'
]

const randomTip = ref('')

// 问候语
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '夜深了'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 18) return '下午好'
  if (hour < 22) return '晚上好'
  return '夜深了'
})

onMounted(() => {
  // 随机选择一条提示
  randomTip.value = tips[Math.floor(Math.random() * tips.length)]
})
</script>

<style scoped>
.welcome-banner {
  background: linear-gradient(135deg, 
    #42A5F5 0%,
    #2196F3 50%,
    #1E88E5 100%
  );
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(33, 150, 243, 0.3);
  border: 3px solid rgba(255, 255, 255, 0.3);
}

/* 背景装饰 */
.banner-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.cloud {
  position: absolute;
  font-size: 32px;
  opacity: 0.5;
  animation: float-cloud 20s infinite ease-in-out;
}

.cloud-1 {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.cloud-2 {
  top: 40%;
  right: 15%;
  animation-delay: -10s;
}

@keyframes float-cloud {
  0%, 100% { transform: translateX(0) translateY(0); }
  50% { transform: translateX(30px) translateY(-10px); }
}

.seagull {
  position: absolute;
  top: 15%;
  right: 20%;
  font-size: 24px;
  animation: fly-seagull 15s infinite ease-in-out;
}

@keyframes fly-seagull {
  0%, 100% { transform: translateX(0) translateY(0) rotate(0deg); }
  50% { transform: translateX(-100px) translateY(-20px) rotate(-5deg); }
}

.sun {
  position: absolute;
  top: 10%;
  right: 10%;
  font-size: 40px;
  animation: rotate-sun 20s infinite linear;
}

@keyframes rotate-sun {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 主内容 */
.banner-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 25px;
  position: relative;
  z-index: 2;
}

.welcome-text {
  flex: 1;
}

.greeting {
  font-size: 32px;
  font-weight: bold;
  color: white;
  margin: 0 0 10px 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  gap: 10px;
}

.wave-hand {
  display: inline-block;
  animation: wave 2s infinite ease-in-out;
}

@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(20deg); }
  75% { transform: rotate(-20deg); }
}

.subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

/* 猫咪吉祥物 */
.cat-mascot {
  position: relative;
}

.cat-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.cat {
  font-size: 64px;
  animation: cat-bounce 2s infinite ease-in-out;
  filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.2));
}

@keyframes cat-bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.speech-bubble {
  position: relative;
  background: white;
  color: #1976D2;
  padding: 10px 15px;
  border-radius: 15px;
  font-size: 13px;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  max-width: 200px;
  text-align: center;
  animation: bubble-pop 0.5s ease-out;
}

@keyframes bubble-pop {
  0% { transform: scale(0); opacity: 0; }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); opacity: 1; }
}

.speech-bubble::before {
  content: '';
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-bottom: 8px solid white;
}

/* 统计信息 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  position: relative;
  z-index: 2;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  padding: 15px;
  border-radius: 12px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: white;
  line-height: 1;
  margin-bottom: 4px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
}

/* 响应式 */
@media (max-width: 768px) {
  .welcome-banner {
    padding: 20px;
  }

  .banner-content {
    flex-direction: column;
    gap: 20px;
  }

  .greeting {
    font-size: 24px;
  }

  .cat {
    font-size: 48px;
  }

  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-value {
    font-size: 20px;
  }

  .stat-icon {
    font-size: 24px;
  }
}
</style>