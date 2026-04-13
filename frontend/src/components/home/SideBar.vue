<template>
  <aside class="ocean-sidebar">
    <!-- 导航菜单 -->
    <div class="menu-section">
      <div 
        v-for="item in menuItems" 
        :key="item.id"
        :class="['menu-item', { active: activeMenu === item.id }]"
        @click="handleMenuClick(item.id)"
      >
        <span class="menu-icon">{{ item.icon }}</span>
        <span class="menu-text">{{ item.text }}</span>
        <span v-if="item.badge" class="menu-badge">{{ item.badge }}</span>
      </div>
    </div>

    <!-- 热门话题 -->
    <div class="topics-section">
      <div class="section-title">
        <span class="title-icon">🔥</span>
        <span>热门话题</span>
      </div>
      <div class="topic-list">
        <div 
          v-for="topic in hotTopics" 
          :key="topic.id"
          class="topic-item"
          @click="handleTopicClick(topic)"
        >
          <div class="topic-info">
            <div class="topic-name">#{{ topic.name }}</div>
            <div class="topic-count">{{ topic.count }} 讨论</div>
          </div>
          <div class="topic-trend">
            <span class="trend-icon">📈</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷链接 -->
    <div class="links-section">
      <div class="section-title">
        <span class="title-icon">⚓</span>
        <span>快捷导航</span>
      </div>
      <div class="link-list">
        <a 
          v-for="link in quickLinks" 
          :key="link.id"
          :href="link.url"
          class="link-item"
          target="_blank"
        >
          <span class="link-icon">{{ link.icon }}</span>
          <span>{{ link.text }}</span>
        </a>
      </div>
    </div>

    <!-- 装饰猫爪 -->
    <div class="paw-decoration">
      <span class="paw">🐾</span>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  activeMenu: {
    type: String,
    default: 'recommend'
  }
})

const emit = defineEmits(['menu-change'])

// 菜单项
const menuItems = ref([
  { id: 'recommend', icon: '🌊', text: '推荐', badge: null },
  { id: 'following', icon: '💙', text: '关注', badge: 5 },
  { id: 'hot', icon: '🔥', text: '热门', badge: null },
  { id: 'tech', icon: '💻', text: '技术分享', badge: null },
  { id: 'life', icon: '🌈', text: '生活日常', badge: null },
  { id: 'cat', icon: '🐱', text: '猫咪世界', badge: 'HOT' },
  { id: 'ocean', icon: '🌊', text: '海洋探索', badge: null }
])

// 热门话题
const hotTopics = ref([
  { id: 1, name: 'Spring Boot实战', count: '1.2k' },
  { id: 2, name: 'Redis高并发', count: '856' },
  { id: 3, name: '简州猫日常', count: '632' },
  { id: 4, name: 'Vue3开发', count: '521' },
  { id: 5, name: '海边旅行', count: '445' }
])

// 快捷链接
const quickLinks = ref([
  { id: 1, icon: '📝', text: '我的草稿', url: '#' },
  { id: 2, icon: '⭐', text: '我的收藏', url: '#' },
  { id: 3, icon: '📊', text: '数据统计', url: '#' },
  { id: 4, icon: '⚙️', text: '账号设置', url: '#' }
])

// 菜单点击
const handleMenuClick = (menuId) => {
  emit('menu-change', menuId)
}

// 话题点击
const handleTopicClick = (topic) => {
  console.log('点击话题:', topic.name)
  // 实际应该跳转到话题页面
}
</script>

<style scoped>
.ocean-sidebar {
  background: linear-gradient(180deg, 
    rgba(255, 255, 255, 0.95) 0%,
    rgba(240, 248, 255, 0.95) 100%
  );
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 20px;
  height: fit-content;
  position: sticky;
  top: 100px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(135, 206, 250, 0.3);
  overflow: hidden;
}

/* 菜单部分 */
.menu-section {
  margin-bottom: 25px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  margin-bottom: 6px;
}

.menu-item:hover {
  background: rgba(33, 150, 243, 0.1);
  transform: translateX(5px);
}

.menu-item.active {
  background: linear-gradient(135deg, #42A5F5 0%, #2196F3 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.menu-icon {
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
}

.menu-text {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
}

.menu-badge {
  background: #FF5252;
  color: white;
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: bold;
}

.menu-item.active .menu-badge {
  background: rgba(255, 255, 255, 0.3);
}

/* 热门话题 */
.topics-section {
  margin-bottom: 25px;
  padding-top: 20px;
  border-top: 2px dashed rgba(135, 206, 250, 0.3);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #1976D2;
  margin-bottom: 15px;
}

.title-icon {
  font-size: 18px;
}

.topic-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.topic-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  background: rgba(227, 242, 253, 0.5);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(135, 206, 250, 0.2);
}

.topic-item:hover {
  background: rgba(187, 222, 251, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

.topic-info {
  flex: 1;
}

.topic-name {
  font-size: 13px;
  font-weight: 600;
  color: #1976D2;
  margin-bottom: 2px;
}

.topic-count {
  font-size: 11px;
  color: #666;
}

.topic-trend {
  display: flex;
  align-items: center;
  justify-content: center;
}

.trend-icon {
  font-size: 16px;
  animation: trend-pulse 2s infinite;
}

@keyframes trend-pulse {
  0%, 100% { opacity: 0.6; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.1); }
}

/* 快捷链接 */
.links-section {
  padding-top: 20px;
  border-top: 2px dashed rgba(135, 206, 250, 0.3);
}

.link-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.link-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 13px;
  color: #666;
  text-decoration: none;
  transition: all 0.3s ease;
}

.link-item:hover {
  background: rgba(33, 150, 243, 0.1);
  color: #1976D2;
  transform: translateX(5px);
}

.link-icon {
  font-size: 16px;
}

/* 装饰猫爪 */
.paw-decoration {
  position: absolute;
  bottom: 15px;
  right: 15px;
  opacity: 0.2;
  animation: paw-float 3s infinite ease-in-out;
}

.paw {
  font-size: 32px;
  display: inline-block;
}

@keyframes paw-float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-10px) rotate(10deg); }
}

/* 响应式 */
@media (max-width: 1200px) {
  .ocean-sidebar {
    padding: 15px;
  }
  
  .topics-section,
  .links-section {
    display: none;
  }
}

@media (max-width: 768px) {
  .ocean-sidebar {
    display: none;
  }
}
</style>