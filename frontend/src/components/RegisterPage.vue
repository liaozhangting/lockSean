<template>
  <div class="ocean-container">
    <!-- 背景动画 -->
    <div class="ocean-bg">
      <div class="sky-gradient"></div>
      <div class="clouds">
        <div class="cloud cloud-1"></div>
        <div class="cloud cloud-2"></div>
      </div>
      <div class="waves">
        <div class="wave wave-1"></div>
        <div class="wave wave-2"></div>
        <div class="wave wave-3"></div>
      </div>
      <div class="boat-container">
        <div class="boat">⛵</div>
      </div>
    </div>

    <!-- 注册表单 -->
    <div class="register-card-wrapper">
      <div class="register-card">
        <!-- 简州猫欢迎 -->
        <div class="cat-welcome">
          <div class="cat-icon">🐱</div>
          <div class="sparkles">
            <span class="sparkle">✨</span>
            <span class="sparkle">✨</span>
            <span class="sparkle">✨</span>
          </div>
        </div>

        <h1 class="register-title">加入我们的航海之旅</h1>
        <p class="register-subtitle">简州猫邀请你成为内容创作者</p>

        <!-- 成功提示 -->
        <div v-if="success" class="success-message">
          <span class="success-icon">🎉</span>
          注册成功！即将跳转到登录页...
        </div>

        <!-- 错误提示 -->
        <div v-if="error" class="error-message">
          <span class="error-icon">⚠️</span>
          {{ error }}
        </div>

        <!-- 表单 -->
        <div class="form-container">
          <div class="input-group">
            <label class="input-label">🧭 选择你的航海昵称</label>
            <div class="input-wrapper">
              <span class="input-icon">👤</span>
              <input
                v-model="formData.nickname"
                type="text"
                class="ocean-input"
                placeholder="请输入昵称"
              />
            </div>
          </div>

          <div class="input-group">
            <label class="input-label">🔐 设置航海密令</label>
            <div class="input-wrapper">
              <span class="input-icon">🔑</span>
              <input
                v-model="formData.password"
                type="password"
                class="ocean-input"
                placeholder="至少6位密码"
              />
            </div>
          </div>

          <div class="input-group">
            <label class="input-label">🔒 确认航海密令</label>
            <div class="input-wrapper">
              <span class="input-icon">🔑</span>
              <input
                v-model="formData.confirmPassword"
                type="password"
                class="ocean-input"
                placeholder="再次输入密码"
                @keyup.enter="handleSubmit"
              />
            </div>
          </div>

          <button
            @click="handleSubmit"
            :disabled="loading || success"
            class="ocean-button register-btn"
          >
            <span v-if="loading" class="loading-spinner">🌊</span>
            <span v-else>🚢 开启创作之旅</span>
          </button>
        </div>

        <!-- 底部链接 -->
        <div class="footer-links">
          <p class="login-hint">
            已有航海证？
            <router-link to="/login" class="login-link">
              🐱 立即登录
            </router-link>
          </p>
        </div>

        <!-- 装饰元素 -->
        <div class="decorative-elements">
          <span class="deco-fish">🐟</span>
          <span class="deco-shell">🐚</span>
          <span class="deco-star">⭐</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const formData = ref({
  nickname: '',
  password: '',
  confirmPassword: ''
})
const loading = ref(false)
const error = ref('')
const success = ref(false)

const handleSubmit = async () => {
  // 前端校验
  if (!formData.value.nickname || !formData.value.password) {
    error.value = '请填写完整信息'
    return
  }

  if (formData.value.password !== formData.value.confirmPassword) {
    error.value = '两次密码输入不一致'
    return
  }

  if (formData.value.password.length < 6) {
    error.value = '密码长度至少6位'
    return
  }

  error.value = ''
  loading.value = true

  try {
    const response = await request.post('/user/register', {
      nickname: formData.value.nickname,
      password: formData.value.password
    })

    if (response.data.code === "true") {
      success.value = true
      setTimeout(() => {
        router.push('/user/login')
      }, 2000)
    } else {
      error.value = response.data.message || '注册失败'
    }
  } catch (err) {
    error.value = err.response?.data?.message || '注册失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 复用登录页的基础样式 */
.ocean-container {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Microsoft YaHei', sans-serif;
}

.ocean-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.sky-gradient {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, 
    #87CEEB 0%,
    #6FB7E8 30%,
    #4A9FD8 60%,
    #2B7FB8 100%
  );
}

.clouds {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50%;
}

.cloud {
  position: absolute;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 100px;
  animation: float-cloud 30s infinite ease-in-out;
}

.cloud::before,
.cloud::after {
  content: '';
  position: absolute;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 100px;
}

.cloud-1 {
  width: 100px;
  height: 40px;
  top: 10%;
  left: -10%;
}

.cloud-1::before {
  width: 50px;
  height: 50px;
  top: -25px;
  left: 10px;
}

.cloud-1::after {
  width: 60px;
  height: 35px;
  top: -15px;
  right: 10px;
}

.cloud-2 {
  width: 120px;
  height: 45px;
  top: 20%;
  right: -10%;
  animation-delay: -15s;
}

.cloud-2::before {
  width: 60px;
  height: 60px;
  top: -30px;
  left: 15px;
}

.cloud-2::after {
  width: 70px;
  height: 40px;
  top: -20px;
  right: 15px;
}

@keyframes float-cloud {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(100vw); }
}

.waves {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 200px;
}

.wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 200%;
  height: 100%;
  background-repeat: repeat-x;
}

.wave-1 {
  background: linear-gradient(to bottom, 
    rgba(26, 117, 186, 0.3) 0%,
    rgba(26, 117, 186, 0.5) 100%
  );
  border-radius: 40% 60% 50% 50%;
  animation: wave-animation 8s infinite ease-in-out;
}

.wave-2 {
  background: linear-gradient(to bottom, 
    rgba(30, 144, 255, 0.3) 0%,
    rgba(30, 144, 255, 0.5) 100%
  );
  border-radius: 50% 40% 60% 50%;
  animation: wave-animation 10s infinite ease-in-out;
  animation-delay: -2s;
}

.wave-3 {
  background: linear-gradient(to bottom, 
    rgba(65, 105, 225, 0.3) 0%,
    rgba(65, 105, 225, 0.6) 100%
  );
  border-radius: 60% 50% 40% 60%;
  animation: wave-animation 12s infinite ease-in-out;
  animation-delay: -4s;
}

@keyframes wave-animation {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(-25%); }
}

.boat-container {
  position: absolute;
  bottom: 150px;
  width: 100%;
}

.boat {
  position: absolute;
  font-size: 48px;
  right: 15%;
  animation: boat-float 6s infinite ease-in-out;
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.2));
}

@keyframes boat-float {
  0%, 100% { transform: translateY(0) rotate(-2deg); }
  50% { transform: translateY(-15px) rotate(2deg); }
}

/* 注册卡片 */
.register-card-wrapper {
  position: relative;
  z-index: 10;
  padding: 20px;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 30px;
  padding: 50px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 450px;
  position: relative;
  border: 3px solid rgba(135, 206, 250, 0.5);
  animation: card-entrance 0.8s ease-out;
}

@keyframes card-entrance {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 猫咪欢迎 */
.cat-welcome {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}

.cat-icon {
  font-size: 64px;
  display: inline-block;
  animation: cat-wave 2s infinite ease-in-out;
  filter: drop-shadow(2px 2px 8px rgba(0,0,0,0.1));
}

@keyframes cat-wave {
  0%, 100% { transform: rotate(-10deg); }
  50% { transform: rotate(10deg); }
}

.sparkles {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
}

.sparkle {
  position: absolute;
  font-size: 20px;
  animation: sparkle 1.5s infinite ease-in-out;
}

.sparkle:nth-child(1) {
  left: 0;
  animation-delay: 0s;
}

.sparkle:nth-child(2) {
  left: 50%;
  transform: translateX(-50%);
  animation-delay: 0.5s;
}

.sparkle:nth-child(3) {
  right: 0;
  animation-delay: 1s;
}

@keyframes sparkle {
  0%, 100% { opacity: 0; transform: translateY(0) scale(0.5); }
  50% { opacity: 1; transform: translateY(-20px) scale(1); }
}

/* 标题 */
.register-title {
  font-size: 28px;
  font-weight: bold;
  color: #1E90FF;
  text-align: center;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.register-subtitle {
  font-size: 14px;
  color: #5DADE2;
  text-align: center;
  margin-bottom: 30px;
}

/* 成功提示 */
.success-message {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
  border: 2px solid #66BB6A;
  border-radius: 15px;
  padding: 12px 15px;
  margin-bottom: 20px;
  color: #2E7D32;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  animation: success-bounce 0.5s ease-out;
}

@keyframes success-bounce {
  0% { transform: scale(0.8); opacity: 0; }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); opacity: 1; }
}

.success-icon {
  font-size: 18px;
  animation: rotate 0.8s ease-in-out;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 错误提示 */
.error-message {
  background: linear-gradient(135deg, #FFE5E5 0%, #FFD0D0 100%);
  border: 2px solid #FF6B6B;
  border-radius: 15px;
  padding: 12px 15px;
  margin-bottom: 20px;
  color: #D32F2F;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  animation: shake 0.5s;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-10px); }
  75% { transform: translateX(10px); }
}

.error-icon {
  font-size: 18px;
}

/* 表单样式 */
.form-container {
  margin-top: 25px;
}

.input-group {
  margin-bottom: 25px;
}

.input-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #2980B9;
  margin-bottom: 10px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 15px;
  font-size: 20px;
  z-index: 2;
}

.ocean-input {
  width: 100%;
  padding: 15px 15px 15px 50px;
  border: 2px solid #87CEEB;
  border-radius: 15px;
  font-size: 15px;
  background: rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
  outline: none;
}

.ocean-input:focus {
  border-color: #1E90FF;
  box-shadow: 0 0 0 4px rgba(30, 144, 255, 0.1);
  background: white;
}

.ocean-input::placeholder {
  color: #AED6F1;
}

.ocean-button {
  width: 100%;
  padding: 15px;
  color: white;
  border: none;
  border-radius: 15px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 10px;
}

.register-btn {
  background: linear-gradient(135deg, #66BB6A 0%, #4CAF50 100%);
  box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
}

.register-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
}

.register-btn:active:not(:disabled) {
  transform: translateY(0);
}

.register-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 底部链接 */
.footer-links {
  text-align: center;
  margin-top: 25px;
}

.login-hint {
  font-size: 14px;
  color: #5DADE2;
}

.login-link {
  color: #66BB6A;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s ease;
}

.login-link:hover {
  color: #4CAF50;
  text-decoration: underline;
}

/* 装饰元素 */
.decorative-elements {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  overflow: hidden;
}

.deco-fish,
.deco-shell,
.deco-star {
  position: absolute;
  font-size: 24px;
  opacity: 0.3;
}

.deco-fish {
  top: 15%;
  right: -20px;
  animation: swim 12s infinite ease-in-out;
}

.deco-shell {
  bottom: 20%;
  left: -20px;
  animation: float-deco 8s infinite ease-in-out;
}

.deco-star {
  top: 50%;
  right: -25px;
  animation: twinkle 3s infinite ease-in-out;
}

@keyframes swim {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(-350px); }
}

@keyframes float-deco {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(15deg); }
}

@keyframes twinkle {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.2); }
}

/* 响应式 */
@media (max-width: 768px) {
  .register-card {
    padding: 40px 30px;
    margin: 20px;
  }

  .register-title {
    font-size: 24px;
  }

  .cat-icon {
    font-size: 56px;
  }
}
</style>