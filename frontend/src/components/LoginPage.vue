<template>
  <div class="ocean-container">
    <!-- 背景动画 -->
    <div class="ocean-bg">
      <!-- 天空渐变 -->
      <div class="sky-gradient"></div>
      
      <!-- 云朵 -->
      <div class="clouds">
        <div class="cloud cloud-1"></div>
        <div class="cloud cloud-2"></div>
        <div class="cloud cloud-3"></div>
      </div>
      
      <!-- 海鸥 -->
      <div class="seagulls">
        <div class="seagull seagull-1">🕊️</div>
        <div class="seagull seagull-2">🕊️</div>
      </div>
      
      <!-- 波浪 -->
      <div class="waves">
        <div class="wave wave-1"></div>
        <div class="wave wave-2"></div>
        <div class="wave wave-3"></div>
      </div>
      
      <!-- 小船 -->
      <div class="boat-container">
        <div class="boat">⛵</div>
      </div>
    </div>

    <!-- 登录表单 -->
    <div class="login-card-wrapper">
      <div class="login-card">
        <!-- 可爱的猫咪头像 -->
        <div class="cat-avatar">
          <div class="cat-face">
            <div class="cat-ears">
              <div class="ear ear-left"></div>
              <div class="ear ear-right"></div>
            </div>
            <div class="cat-head">
              <div class="cat-eyes">
                <div class="eye eye-left">
                  <div class="pupil"></div>
                </div>
                <div class="eye eye-right">
                  <div class="pupil"></div>
                </div>
              </div>
              <div class="cat-nose"></div>
              <div class="cat-mouth">
                <div class="mouth-left"></div>
                <div class="mouth-right"></div>
              </div>
              <div class="cat-whiskers">
                <div class="whisker whisker-left-1"></div>
                <div class="whisker whisker-left-2"></div>
                <div class="whisker whisker-right-1"></div>
                <div class="whisker whisker-right-2"></div>
              </div>
            </div>
          </div>
        </div>

        <h1 class="login-title">欢迎回到海洋之旅</h1>
        <p class="login-subtitle">简州猫陪你一起探索蓝色星球</p>

        <!-- 错误提示 -->
        <div v-if="error" class="error-message">
          <span class="error-icon">⚠️</span>
          {{ error }}
        </div>

        <!-- 表单 -->
        <div class="form-container">
          <div class="input-group">
            <label class="input-label">🧭 航海者昵称</label>
            <div class="input-wrapper">
              <span class="input-icon">👤</span>
              <input
                v-model="formData.nickname"
                type="text"
                class="ocean-input"
                placeholder="请输入你的昵称"
                @keyup.enter="handleSubmit"
              />
            </div>
          </div>

          <div class="input-group">
            <label class="input-label">🔐 航海密令</label>
            <div class="input-wrapper">
              <span class="input-icon">🔑</span>
              <input
                v-model="formData.password"
                type="password"
                class="ocean-input"
                placeholder="请输入密码"
                @keyup.enter="handleSubmit"
              />
            </div>
          </div>

          <button
            @click="handleSubmit"
            :disabled="loading"
            class="ocean-button"
          >
            <span v-if="loading" class="loading-spinner">🌊</span>
            <span v-else>🚢 启航登录</span>
          </button>
        </div>

        <!-- 底部链接 -->
        <div class="footer-links">
          <p class="register-hint">
            还没有航海证？
            <router-link to="/register" class="register-link">
              🐱 立即注册
            </router-link>
          </p>
        </div>

        <!-- 装饰小鱼 -->
        <div class="decorative-fish">
          <span class="fish fish-1">🐟</span>
          <span class="fish fish-2">🐠</span>
          <span class="fish fish-3">🐡</span>
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
  password: ''
})
const loading = ref(false)
const error = ref('')

const handleSubmit = async () => {
  if (!formData.value.nickname || !formData.value.password) {
    error.value = '请填写完整信息'
    return
  }

  error.value = ''
  loading.value = true

  try {
    const response = await request.post('/user/login', formData.value)
    
    console.log('登录响应:', response.data)
    
    // 修复：兼容后端返回的字符串 "true"
    const isSuccess = response.data.code === "true" || 
                     response.data.code === 200 || 
                     response.data.code === true
    
    if (isSuccess && response.data.data) {
      const { token, userInfo } = response.data.data
      
      // 保存 token 和用户信息
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
      
      console.log('登录成功，token 已保存')
      console.log('用户信息:', userInfo)
      
      // 跳转到首页
      router.push('/home')
    } else {
      error.value = response.data.message || '登录失败，请检查账号密码'
    }
  } catch (err) {
    console.error('登录错误:', err)
    error.value = err.response?.data?.message || '登录失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ===== 主容器 ===== */
.ocean-container {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* ===== 背景 ===== */
.ocean-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

/* 天空渐变 */
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

/* 云朵 */
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
  animation-delay: 0s;
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
  left: 30%;
  animation-delay: -10s;
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

.cloud-3 {
  width: 90px;
  height: 35px;
  top: 15%;
  right: -10%;
  animation-delay: -20s;
}

.cloud-3::before {
  width: 45px;
  height: 45px;
  top: -20px;
  left: 10px;
}

.cloud-3::after {
  width: 55px;
  height: 30px;
  top: -12px;
  right: 10px;
}

@keyframes float-cloud {
  0%, 100% { transform: translateX(0) translateY(0); }
  25% { transform: translateX(30vw) translateY(-10px); }
  50% { transform: translateX(60vw) translateY(5px); }
  75% { transform: translateX(90vw) translateY(-5px); }
}

/* 海鸥 */
.seagulls {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 40%;
}

.seagull {
  position: absolute;
  font-size: 24px;
  animation: fly 20s infinite ease-in-out;
}

.seagull-1 {
  top: 15%;
  left: -5%;
  animation-delay: 0s;
}

.seagull-2 {
  top: 25%;
  left: 50%;
  animation-delay: -10s;
}

@keyframes fly {
  0% { transform: translateX(0) translateY(0) rotate(0deg); }
  25% { transform: translateX(30vw) translateY(-20px) rotate(5deg); }
  50% { transform: translateX(60vw) translateY(10px) rotate(-5deg); }
  75% { transform: translateX(90vw) translateY(-10px) rotate(3deg); }
  100% { transform: translateX(120vw) translateY(0) rotate(0deg); }
}

/* 波浪 */
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
  0%, 100% { transform: translateX(0) translateY(0); }
  50% { transform: translateX(-25%) translateY(-10px); }
}

/* 小船 */
.boat-container {
  position: absolute;
  bottom: 150px;
  left: 0;
  width: 100%;
  height: 100px;
}

.boat {
  position: absolute;
  font-size: 48px;
  left: 10%;
  animation: boat-float 6s infinite ease-in-out;
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.2));
}

@keyframes boat-float {
  0%, 100% { transform: translateY(0) rotate(-2deg); }
  50% { transform: translateY(-15px) rotate(2deg); }
}

/* ===== 登录卡片 ===== */
.login-card-wrapper {
  position: relative;
  z-index: 10;
  padding: 20px;
}

.login-card {
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

/* ===== 简州猫头像 ===== */
.cat-avatar {
  text-align: center;
  margin-bottom: 30px;
}

.cat-face {
  display: inline-block;
  position: relative;
  animation: cat-bounce 2s infinite ease-in-out;
}

@keyframes cat-bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.cat-ears {
  position: relative;
  width: 100px;
  height: 40px;
  margin: 0 auto;
}

.ear {
  position: absolute;
  width: 0;
  height: 0;
  border-left: 25px solid transparent;
  border-right: 25px solid transparent;
  border-bottom: 40px solid #87CEEB;
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.1));
}

.ear::after {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  border-bottom: 25px solid #FFB6C1;
  left: -15px;
  top: 10px;
}

.ear-left {
  left: 0;
  transform: rotate(-15deg);
}

.ear-right {
  right: 0;
  transform: rotate(15deg);
}

.cat-head {
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #87CEEB 0%, #6FB7E8 100%);
  border-radius: 50%;
  position: relative;
  margin: -10px auto 0;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

.cat-eyes {
  position: absolute;
  top: 35px;
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.eye {
  width: 16px;
  height: 20px;
  background: white;
  border-radius: 50%;
  position: relative;
  animation: blink 4s infinite;
}

@keyframes blink {
  0%, 98%, 100% { height: 20px; }
  99% { height: 2px; }
}

.pupil {
  width: 8px;
  height: 10px;
  background: #2c3e50;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.cat-nose {
  width: 12px;
  height: 10px;
  background: #FFB6C1;
  border-radius: 0 0 50% 50%;
  position: absolute;
  top: 55px;
  left: 50%;
  transform: translateX(-50%);
}

.cat-mouth {
  position: absolute;
  top: 62px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
}

.mouth-left,
.mouth-right {
  position: absolute;
  width: 15px;
  height: 8px;
  border: 2px solid #2c3e50;
  border-top: none;
  border-radius: 0 0 15px 15px;
}

.mouth-left {
  left: 0;
  border-right: none;
}

.mouth-right {
  right: 0;
  border-left: none;
}

.cat-whiskers {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.whisker {
  position: absolute;
  width: 35px;
  height: 2px;
  background: rgba(44, 62, 80, 0.5);
  border-radius: 1px;
  top: 50%;
}

.whisker-left-1 {
  left: -30px;
  transform: rotate(-10deg);
}

.whisker-left-2 {
  left: -30px;
  top: 55%;
  transform: rotate(10deg);
}

.whisker-right-1 {
  right: -30px;
  transform: rotate(10deg);
}

.whisker-right-2 {
  right: -30px;
  top: 55%;
  transform: rotate(-10deg);
}

/* ===== 标题 ===== */
.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #1E90FF;
  text-align: center;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.login-subtitle {
  font-size: 14px;
  color: #5DADE2;
  text-align: center;
  margin-bottom: 30px;
}

/* ===== 错误提示 ===== */
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

/* ===== 表单 ===== */
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
  background: linear-gradient(135deg, #1E90FF 0%, #4169E1 100%);
  color: white;
  border: none;
  border-radius: 15px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(30, 144, 255, 0.3);
  margin-top: 10px;
}

.ocean-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(30, 144, 255, 0.4);
}

.ocean-button:active:not(:disabled) {
  transform: translateY(0);
}

.ocean-button:disabled {
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

/* ===== 底部链接 ===== */
.footer-links {
  text-align: center;
  margin-top: 25px;
}

.register-hint {
  font-size: 14px;
  color: #5DADE2;
}

.register-link {
  color: #1E90FF;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s ease;
}

.register-link:hover {
  color: #4169E1;
  text-decoration: underline;
}

/* ===== 装饰小鱼 ===== */
.decorative-fish {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  overflow: hidden;
}

.fish {
  position: absolute;
  font-size: 24px;
  opacity: 0.3;
}

.fish-1 {
  top: 20%;
  right: -30px;
  animation: swim-1 15s infinite ease-in-out;
}

.fish-2 {
  bottom: 30%;
  left: -30px;
  animation: swim-2 12s infinite ease-in-out;
}

.fish-3 {
  top: 60%;
  right: -30px;
  animation: swim-3 18s infinite ease-in-out;
}

@keyframes swim-1 {
  0%, 100% { transform: translateX(0) translateY(0); }
  50% { transform: translateX(-400px) translateY(20px); }
}

@keyframes swim-2 {
  0%, 100% { transform: translateX(0) translateY(0) scaleX(-1); }
  50% { transform: translateX(400px) translateY(-20px) scaleX(-1); }
}

@keyframes swim-3 {
  0%, 100% { transform: translateX(0) translateY(0); }
  50% { transform: translateX(-400px) translateY(-30px); }
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .login-card {
    padding: 40px 30px;
    margin: 20px;
  }

  .login-title {
    font-size: 24px;
  }

  .cat-head {
    width: 80px;
    height: 80px;
  }

  .cat-ears {
    width: 80px;
  }
}
</style>