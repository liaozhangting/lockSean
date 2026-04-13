<template>
  <div class="ocean-community">
    <!-- 顶部导航栏 -->
    <NavBar :user-info="userInfo" @logout="handleLogout" />

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 左侧侧边栏 -->
      <SideBar :active-menu="activeMenu" @menu-change="handleMenuChange" />

      <!-- 中间内容区 -->
      <div class="content-area">
        <!-- 欢迎横幅 -->
        <WelcomeBanner :user-info="userInfo" />

        <!-- 内容发布区 -->
        <PublishArea @publish="handlePublish" />

        <!-- 内容列表 -->
        <ContentList :contents="contentList" :loading="contentLoading" />
      </div>

      <!-- 右侧信息栏 -->
      <RightPanel :user-info="userInfo" :stats="userStats" />
    </div>

    <!-- 海洋装饰背景 -->
    <OceanDecoration />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

// 导入子组件（实际使用时需要创建这些组件）
import NavBar from './home/NavBar.vue'
import SideBar from './home/SideBar.vue'
import WelcomeBanner from './home/WelcomeBanner.vue'
import PublishArea from './home/PublishArea.vue'
import ContentList from './home/ContentList.vue'
import RightPanel from './home/RightPanel.vue'
import OceanDecoration from './home/OceanDecoration.vue'

const router = useRouter()
const userInfo = ref(null)
const activeMenu = ref('recommend')
const contentList = ref([])
const contentLoading = ref(false)

// 用户统计数据
const userStats = ref({
  posts: 42,
  followers: 128,
  likes: 567,
  views: 1234
})

// 初始化
onMounted(() => {
  loadUserInfo()
  loadContentList()
})

// 加载用户信息
const loadUserInfo = () => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
  }
}

// 加载内容列表
const loadContentList = async () => {
  contentLoading.value = true
  try {
    const res = await request.get('/content/home/recommend', {
      params: { page: 1, pageSize: 10 }
    })
    if (res.data && res.data.data) {
      contentList.value = res.data.data.map(item => ({
        id: item.id,
        author: item.authorName,
        avatar: item.authorAvatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${item.authorName}`,
        content: item.content,
        images: typeof item.images === 'string' ? item.images.split(',').filter(Boolean) : (item.images || []),
        likes: item.likes || 0,
        comments: item.comments || 0,
        shares: item.shares || 0,
        time: formatTime(item.createTime),
        tags: typeof item.tags === 'string' ? item.tags.split(',').filter(Boolean) : (item.tags || []),
        isLiked: item.hasLiked || false,
        isCollected: false
      }))
    }
  } catch (err) {
    console.error('加载内容失败', err)
    contentList.value = []
  } finally {
    contentLoading.value = false
  }
}

const formatTime = (dateStr) => {
  if (!dateStr) return '未知时间'
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}

// 生成模拟内容
const generateMockContent = () => {
  return [
    {
      id: 1,
      author: '海洋探索者',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=ocean',
      content: '今天在海边发现了一只超可爱的简州猫！🐱 它正在沙滩上追逐小螃蟹，太有趣了！',
      images: [],
      likes: 234,
      comments: 45,
      shares: 12,
      time: '2小时前',
      tags: ['猫咪', '海边', '日常']
    },
    {
      id: 2,
      author: '蓝色水手',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=sailor',
      content: '分享一个高并发优化的小技巧：使用Redis分布式锁 + Lua脚本可以有效防止超卖问题 💡',
      images: [],
      likes: 589,
      comments: 123,
      shares: 67,
      time: '5小时前',
      tags: ['技术分享', 'Redis', '高并发']
    },
    {
      id: 3,
      author: '猫咪船长',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=captain',
      content: '新项目上线了！基于Spring Boot + Vue3的内容社区，欢迎大家来体验 🚀',
      images: [],
      likes: 421,
      comments: 89,
      shares: 34,
      time: '1天前',
      tags: ['项目分享', 'Spring Boot', 'Vue3']
    }
  ]
}

// 菜单切换
const handleMenuChange = (menu) => {
  activeMenu.value = menu
  loadContentList()
}

// 发布内容
const handlePublish = (content) => {
  console.log('发布内容:', content)
  // 实际应该调用后端API
  contentList.value.unshift({
    id: Date.now(),
    author: userInfo.value.nickname,
    avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${userInfo.value.nickname}`,
    content: content,
    images: [],
    likes: 0,
    comments: 0,
    shares: 0,
    time: '刚刚',
    tags: []
  })
}

// 退出登录
const handleLogout = async () => {
  try {
    await request.post('/user/logout')
  } catch (err) {
    console.error('退出失败', err)
  } finally {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }
}
</script>

<style scoped>
.ocean-community {
  min-height: 100vh;
  background: linear-gradient(to bottom, #E3F2FD 0%, #BBDEFB 100%);
  position: relative;
  overflow-x: hidden;
}

.main-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 20px;
  display: grid;
  grid-template-columns: 240px 1fr 300px;
  gap: 20px;
  position: relative;
  z-index: 2;
}

.content-area {
  min-height: calc(100vh - 100px);
}

/* 响应式 */
@media (max-width: 1200px) {
  .main-container {
    grid-template-columns: 200px 1fr;
  }
}

@media (max-width: 768px) {
  .main-container {
    grid-template-columns: 1fr;
    padding: 10px;
  }
}
</style>