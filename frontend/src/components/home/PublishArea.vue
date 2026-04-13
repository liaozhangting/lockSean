<template>
  <div class="publish-area">
    <div class="publish-header">
      <span class="header-icon">✍️</span>
      <span class="header-text">分享你的航海故事</span>
    </div>

    <div class="publish-input-area">
      <textarea
        v-model="content"
        class="publish-textarea"
        :placeholder="placeholder"
        rows="4"
        @focus="isExpanded = true"
      ></textarea>

      <!-- 展开后的功能区 -->
      <transition name="expand">
        <div v-if="isExpanded" class="publish-toolbar">
          <div class="toolbar-left">
            <button class="tool-btn" @click="handleAddImage" title="添加图片" :disabled="uploading">
              <span class="tool-icon">🖼️</span>
              <span class="tool-text">{{ uploading ? '上传中...' : '图片' }}</span>
            </button>
            <button class="tool-btn" @click="handleAddVideo" title="添加视频" :disabled="uploading">
              <span class="tool-icon">🎥</span>
              <span class="tool-text">{{ uploading ? '上传中...' : '视频' }}</span>
            </button>
            <button class="tool-btn" @click="handleAddEmoji" title="添加表情">
              <span class="tool-icon">😊</span>
              <span class="tool-text">表情</span>
            </button>
            <button class="tool-btn" @click="handleAddTag" title="添加话题">
              <span class="tool-icon">#️⃣</span>
              <span class="tool-text">话题</span>
            </button>
          </div>

          <div class="toolbar-right">
            <div class="char-count" :class="{ warning: charCount > maxChars }">
              {{ charCount }}/{{ maxChars }}
            </div>
            <button 
              class="cancel-btn" 
              @click="handleCancel"
            >
              取消
            </button>
            <button 
              class="publish-btn" 
              :disabled="!canPublish || uploading"
              @click="handlePublish"
            >
              <span v-if="publishing || uploading" class="loading">🌊</span>
              <span v-else>🚢 发布</span>
            </button>
          </div>
        </div>
      </transition>

      <!-- 媒体预览区域 -->
      <transition name="expand">
        <div v-if="isExpanded && (imageUrls.length > 0 || videoUrls.length > 0)" class="media-preview">
          <!-- 图片预览 -->
          <div v-if="imageUrls.length > 0" class="preview-section">
            <div class="preview-title">📷 图片 ({{ imageUrls.length }})</div>
            <div class="preview-grid">
              <div v-for="(url, index) in imageUrls" :key="url" class="preview-item">
                <img :src="url" alt="预览图片" class="preview-image" />
                <button class="remove-btn" @click="removeImage(index)" title="删除">✕</button>
              </div>
            </div>
          </div>

          <!-- 视频预览 -->
          <div v-if="videoUrls.length > 0" class="preview-section">
            <div class="preview-title">🎬 视频 ({{ videoUrls.length }})</div>
            <div class="preview-grid">
              <div v-for="(url, index) in videoUrls" :key="url" class="preview-item">
                <video :src="url" class="preview-video" controls></video>
                <button class="remove-btn" @click="removeVideo(index)" title="删除">✕</button>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <!-- 表情选择器 -->
    <transition name="fade">
      <div v-if="showEmojiPicker" class="emoji-picker">
        <div class="emoji-header">
          <span>选择表情</span>
          <button class="close-btn" @click="showEmojiPicker = false">✕</button>
        </div>
        <div class="emoji-grid">
          <span 
            v-for="emoji in emojiList" 
            :key="emoji"
            class="emoji-item"
            @click="insertEmoji(emoji)"
          >
            {{ emoji }}
          </span>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import request from '@/utils/request.js' // 路径根据你的项目结构调整
const emit = defineEmits(['publish'])

const content = ref('')
const isExpanded = ref(false)
const publishing = ref(false)
const showEmojiPicker = ref(false)
const maxChars = 500
const imageUrls = ref([]) // 存储图片URL
const videoUrls = ref([]) // 存储视频URL
const uploading = ref(false)

// 占位符
const placeholders = [
  '今天在海边发现了什么有趣的事情？🌊',
  '分享一下你的技术心得吧~ 💻',
  '简州猫今天又做了什么可爱的事？🐱',
  '记录下这美好的一刻...',
  '有什么想和大家分享的吗？'
]

const placeholder = computed(() => {
  return placeholders[Math.floor(Math.random() * placeholders.length)]
})

// 字符计数
const charCount = computed(() => content.value.length)

// 是否可以发布
const canPublish = computed(() => {
  return content.value.trim().length > 0 && 
         content.value.length <= maxChars && 
         !publishing.value
})

// 表情列表
const emojiList = [
  '😊', '😂', '🥰', '😍', '🤗', '😎', '🤔', '😏',
  '🐱', '🐶', '🐰', '🦊', '🐯', '🐼', '🐨', '🐻',
  '🌊', '⛵', '🚢', '⚓', '🏖️', '🌅', '🌴', '🐚',
  '💙', '💚', '💛', '🧡', '❤️', '💜', '🖤', '🤍',
  '👍', '👏', '🙌', '💪', '✨', '🌟', '⭐', '🔥',
  '🎉', '🎊', '🎈', '🎁', '🏆', '🥇', '🎯', '🎪'
]



// 上传图片
const handleAddImage = async () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  input.multiple = true

  input.onchange = async (e) => {
    const files = e.target.files
    if (!files || files.length === 0) return

    uploading.value = true

    try {
      for (let i = 0; i < files.length; i++) {
        const file = files[i]
        const formData = new FormData()
        formData.append('file', file)

        const response = await request.post('/api/cos/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })

        const result = response.data
        if (result.code === 'true' && result.data?.url) {
          imageUrls.value.push(result.data.url)
		  console.log("imageUrls：",imageUrls)
        }
      }

      alert(`成功上传 ${files.length} 张图片！🖼️`)
    } catch (error) {
      console.error('图片上传失败:', error)
      alert('图片上传失败，请重试')
    } finally {
      uploading.value = false
    }
  }

  input.click()
}


// 添加视频
const handleAddVideo = () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'video/*'
  input.multiple = true
  
  input.onchange = async (e) => {
    const files = e.target.files
    if (!files || files.length === 0) return
    
    uploading.value = true
    
    try {
      for (let i = 0; i < files.length; i++) {
        const file = files[i]
        const formData = new FormData()
        formData.append('file', file)
        
        const response = await fetch('/api/cos/upload', {
          method: 'POST',
          body: formData
        })
        
        const result = await response.json()
        
        if (result.code === 'true' && result.data && result.data.url) {
          videoUrls.value.push(result.data.url)
        }
      }
      
      alert(`成功上传 ${files.length} 个视频！🎥`)
    } catch (error) {
      console.error('视频上传失败:', error)
      alert('视频上传失败，请重试')
    } finally {
      uploading.value = false
    }
  }
  
  input.click()
}

// 删除图片
const removeImage = (index) => {
  imageUrls.value.splice(index, 1)
}

// 删除视频
const removeVideo = (index) => {
  videoUrls.value.splice(index, 1)
}

// 添加表情
const handleAddEmoji = () => {
  showEmojiPicker.value = !showEmojiPicker.value
}

// 插入表情
const insertEmoji = (emoji) => {
  content.value += emoji
  showEmojiPicker.value = false
}

// 添加话题
const handleAddTag = () => {
  const tag = prompt('输入话题名称（不含#）：')
  if (tag) {
    content.value += ` #${tag} `
  }
}

// 取消
const handleCancel = () => {
  if (content.value && !confirm('确定要取消吗？内容将不会保存。')) {
    return
  }
  content.value = ''
  imageUrls.value = []
  videoUrls.value = []
  isExpanded.value = false
  showEmojiPicker.value = false
}

// 发布
const handlePublish = async () => {
  if (!canPublish.value) return
  publishing.value = true
  
  try {
    // 构造发布数据，字段名对应后端 ContentDTO
    const publishData = {
      content: content.value,
      images: imageUrls.value.join(','),  // 改为 images，对应后端字段名
      tags: ''  // 如果有标签功能，可以添加；没有就传空字符串
    }
	console.log(publishData.images);
    
    // 发送请求到后端
    const response = await request.post('/content/publish', publishData, {
      headers: { 'Content-Type': 'application/json' }
    })
    
    const result = response.data
    
    if (result.code === 'true' || result.code === 200) {
      alert('发布成功！✨')
      
      // 通知父组件发布成功
      emit('publish', publishData)
      
      // 重置所有状态
      content.value = ''
      imageUrls.value = []
      videoUrls.value = []
      isExpanded.value = false
      showEmojiPicker.value = false
    } else {
      alert(result.message || '发布失败，请重试')
    }
  } catch (error) {
    console.error('发布失败:', error)
    alert('发布失败：' + (error.response?.data?.message || error.message))
  } finally {
    publishing.value = false
  }
}
</script>

<style scoped>
.publish-area {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid rgba(135, 206, 250, 0.3);
  position: relative;
}

.publish-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 2px dashed rgba(135, 206, 250, 0.3);
}

.header-icon {
  font-size: 24px;
}

.header-text {
  font-size: 16px;
  font-weight: 600;
  color: #1976D2;
}

/* 输入区域 */
.publish-input-area {
  position: relative;
}

.publish-textarea {
  width: 100%;
  border: 2px solid #E3F2FD;
  border-radius: 12px;
  padding: 15px;
  font-size: 15px;
  line-height: 1.6;
  resize: none;
  outline: none;
  transition: all 0.3s ease;
  font-family: inherit;
}

.publish-textarea:focus {
  border-color: #2196F3;
  box-shadow: 0 0 0 4px rgba(33, 150, 243, 0.1);
}

.publish-textarea::placeholder {
  color: #90CAF9;
}

/* 工具栏 */
.publish-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 2px solid #F5F5F5;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.tool-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: #E3F2FD;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 13px;
  color: #1976D2;
  font-weight: 500;
}

.tool-btn:hover:not(:disabled) {
  background: #BBDEFB;
  transform: translateY(-2px);
}

.tool-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.tool-icon {
  font-size: 18px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.char-count {
  font-size: 13px;
  color: #666;
  font-weight: 500;
}

.char-count.warning {
  color: #F44336;
  font-weight: bold;
}

.cancel-btn {
  padding: 8px 20px;
  background: #F5F5F5;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.cancel-btn:hover {
  background: #E0E0E0;
}

.publish-btn {
  padding: 10px 24px;
  background: linear-gradient(135deg, #42A5F5 0%, #2196F3 100%);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: bold;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.publish-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(33, 150, 243, 0.4);
}

.publish-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.loading {
  display: inline-block;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 媒体预览区域 */
.media-preview {
  margin-top: 15px;
  padding: 15px;
  background: #F5F9FF;
  border-radius: 12px;
  border: 2px dashed #BBDEFB;
}

.preview-section {
  margin-bottom: 15px;
}

.preview-section:last-child {
  margin-bottom: 0;
}

.preview-title {
  font-size: 13px;
  font-weight: 600;
  color: #1976D2;
  margin-bottom: 10px;
}

.preview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 10px;
}

.preview-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  background: white;
  aspect-ratio: 1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.preview-image,
.preview-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 24px;
  height: 24px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.remove-btn:hover {
  background: #F44336;
  transform: scale(1.1);
}

/* 表情选择器 */
.emoji-picker {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 10px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  padding: 15px;
  width: 100%;
  max-width: 400px;
  z-index: 10;
  border: 2px solid #E3F2FD;
}

.emoji-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #F5F5F5;
  font-size: 14px;
  font-weight: 600;
  color: #1976D2;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #999;
  transition: color 0.3s ease;
}

.close-btn:hover {
  color: #F44336;
}

.emoji-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 8px;
  max-height: 200px;
  overflow-y: auto;
}

.emoji-item {
  font-size: 24px;
  padding: 8px;
  text-align: center;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.emoji-item:hover {
  background: #E3F2FD;
  transform: scale(1.2);
}

/* 动画 */
.expand-enter-active,
.expand-leave-active {
  transition: all 0.3s ease;
}

.expand-enter-from,
.expand-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* 响应式 */
@media (max-width: 768px) {
  .publish-area {
    padding: 15px;
  }

  .toolbar-left {
    flex-wrap: wrap;
  }

  .tool-text {
    display: none;
  }

  .tool-btn {
    padding: 8px;
  }

  .emoji-grid {
    grid-template-columns: repeat(6, 1fr);
  }

  .preview-grid {
    grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  }
}
</style>