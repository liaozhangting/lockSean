<template>
  <nav class="ocean-navbar">
    <div class="navbar-container">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo-icon">
          <span class="cat-emoji">🐱</span>
          <span class="boat-emoji">⛵</span>
        </div>
        <div class="logo-text">
          <h1 class="site-name">海洋猫社区</h1>
          <p class="site-slogan">分享你的航海故事</p>
        </div>
      </div>

      <!-- 搜索框 -->
      <div class="search-section">
        <div class="search-box">
          <span class="search-icon">🔍</span>
          <input 
            type="text" 
            placeholder="搜索内容、用户、话题..." 
            class="search-input"
            v-model="searchQuery"
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">搜索</button>
        </div>
      </div>

      <!-- 右侧功能区 -->
      <div class="actions-section">
        <!-- 消息通知 -->
        <div class="action-item notification">
          <span class="icon">🔔</span>
          <span class="badge" v-if="notificationCount > 0">{{ notificationCount }}</span>
        </div>

        <!-- 用户信息 -->
        <div class="user-profile" @click="toggleUserMenu">
          <img 
            :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${userInfo?.nickname}`" 
            alt="avatar"
            class="user-avatar"
          />
          <span class="user-name">{{ userInfo?.nickname }}</span>
          <span class="dropdown-arrow">▼</span>
        </div>

        <!-- 下拉菜单 -->
        <transition name="dropdown">
          <div v-if="showUserMenu" class="user-menu">
            <div class="menu-item" @click="goToProfile">
              <span class="menu-icon">👤</span>
              <span>个人主页</span>
            </div>
            <div class="menu-item" @click="goToSettings">
              <span class="menu-icon">⚙️</span>
              <span>设置</span>
            </div>
            <div class="menu-divider"></div>
            <div class="menu-item logout" @click="handleLogout">
              <span class="menu-icon">🚪</span>
              <span>退出登录</span>
            </div>
          </div>
        </transition>
      </div>
    </div>

    <!-- 波浪装饰 -->
    <div class="wave-decoration"></div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  userInfo: Object
})

const emit = defineEmits(['logout'])

const searchQuery = ref('')
const notificationCount = ref(3)
const showUserMenu = ref(false)

// 搜索
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    console.log('搜索:', searchQuery.value)
    // 实际应该跳转到搜索页面或调用搜索API
  }
}

// 切换用户菜单
const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
}

// 跳转到个人主页
const goToProfile = () => {
  showUserMenu.value = false
  console.log('跳转到个人主页')
}

// 跳转到设置
const goToSettings = () => {
  showUserMenu.value = false
  console.log('跳转到设置')
}

// 退出登录
const handleLogout = () => {
  showUserMenu.value = false
  emit('logout')
}

// 点击外部关闭菜单
document.addEventListener('click', (e) => {
  if (!e.target.closest('.user-profile') && !e.target.closest('.user-menu')) {
    showUserMenu.value = false
  }
})
</script>

<style scoped>
.ocean-navbar {
  background: linear-gradient(135deg, #1E88E5 0%, #1976D2 100%);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  border-bottom: 3px solid rgba(255, 255, 255, 0.2);
}

.navbar-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 12px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 30px;
}

/* Logo区域 */
.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo-section:hover {
  transform: translateY(-2px);
}

.logo-icon {
  position: relative;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cat-emoji {
  font-size: 32px;
  animation: cat-float 3s infinite ease-in-out;
}

.boat-emoji {
  position: absolute;
  font-size: 16px;
  bottom: 0;
  right: 0;
  animation: boat-rock 2s infinite ease-in-out;
}

@keyframes cat-float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

@keyframes boat-rock {
  0%, 100% { transform: rotate(-5deg); }
  50% { transform: rotate(5deg); }
}

.logo-text {
  color: white;
}

.site-name {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.site-slogan {
  font-size: 11px;
  margin: 0;
  opacity: 0.9;
}

/* 搜索区域 */
.search-section {
  flex: 1;
  max-width: 600px;
}

.search-box {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 25px;
  padding: 8px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.search-box:focus-within {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.search-icon {
  font-size: 18px;
  margin-right: 10px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
  color: #333;
}

.search-input::placeholder {
  color: #999;
}

.search-btn {
  background: linear-gradient(135deg, #42A5F5 0%, #2196F3 100%);
  color: white;
  border: none;
  padding: 6px 20px;
  border-radius: 15px;
  cursor: pointer;
  font-size: 13px;
  font-weight: bold;
  transition: all 0.3s ease;
  margin-left: 10px;
}

.search-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.4);
}

/* 右侧功能区 */
.actions-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-item {
  position: relative;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-item:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.action-item .icon {
  font-size: 20px;
}

.badge {
  position: absolute;
  top: -2px;
  right: -2px;
  background: #FF5252;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 用户信息 */
.user-profile {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.2);
  padding: 6px 16px 6px 6px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.user-profile:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-name {
  color: white;
  font-weight: 600;
  font-size: 14px;
}

.dropdown-arrow {
  color: white;
  font-size: 10px;
  transition: transform 0.3s ease;
}

.user-profile:hover .dropdown-arrow {
  transform: translateY(2px);
}

/* 下拉菜单 */
.user-menu {
  position: absolute;
  top: 60px;
  right: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  padding: 8px 0;
  min-width: 180px;
  z-index: 1000;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.menu-item:hover {
  background: #F5F5F5;
}

.menu-item.logout:hover {
  background: #FFEBEE;
  color: #F44336;
}

.menu-icon {
  font-size: 18px;
}

.menu-divider {
  height: 1px;
  background: #E0E0E0;
  margin: 8px 0;
}

/* 下拉动画 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 波浪装饰 */
.wave-decoration {
  height: 4px;
  background: linear-gradient(90deg, 
    rgba(255, 255, 255, 0.3) 0%,
    rgba(255, 255, 255, 0.5) 50%,
    rgba(255, 255, 255, 0.3) 100%
  );
  background-size: 200% 100%;
  animation: wave-flow 3s linear infinite;
}

@keyframes wave-flow {
  0% { background-position: 0% 0%; }
  100% { background-position: 200% 0%; }
}

/* 响应式 */
@media (max-width: 768px) {
  .navbar-container {
    gap: 15px;
  }

  .logo-text {
    display: none;
  }

  .search-section {
    max-width: none;
  }

  .user-name {
    display: none;
  }

  .search-btn {
    padding: 6px 12px;
  }
}
</style>