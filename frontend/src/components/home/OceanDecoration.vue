<template>
  <div class="ocean-decoration">
    <!-- 天空背景 -->
    <div class="sky-layer">
      <!-- 云朵 -->
      <div class="cloud-group">
        <div class="cloud cloud-1">☁️</div>
        <div class="cloud cloud-2">☁️</div>
        <div class="cloud cloud-3">☁️</div>
        <div class="cloud cloud-4">☁️</div>
      </div>

      <!-- 太阳/月亮 -->
      <div class="celestial" :class="{ night: isNight }">
        <span v-if="!isNight" class="sun">☀️</span>
        <span v-else class="moon">🌙</span>
      </div>

      <!-- 海鸥 -->
      <div class="seagull-group">
        <div class="seagull seagull-1">🕊️</div>
        <div class="seagull seagull-2">🕊️</div>
        <div class="seagull seagull-3">🕊️</div>
      </div>

      <!-- 星星（夜晚） -->
      <div v-if="isNight" class="stars">
        <span class="star star-1">⭐</span>
        <span class="star star-2">✨</span>
        <span class="star star-3">⭐</span>
        <span class="star star-4">✨</span>
        <span class="star star-5">⭐</span>
      </div>
    </div>

    <!-- 海洋层 -->
    <div class="ocean-layer">
      <!-- 波浪 -->
      <div class="waves">
        <div class="wave wave-1"></div>
        <div class="wave wave-2"></div>
        <div class="wave wave-3"></div>
        <div class="wave wave-4"></div>
      </div>

      <!-- 小船 -->
      <div class="boats">
        <div class="boat boat-1">⛵</div>
        <div class="boat boat-2">🚢</div>
      </div>

      <!-- 海洋生物 -->
      <div class="sea-creatures">
        <div class="fish fish-1">🐟</div>
        <div class="fish fish-2">🐠</div>
        <div class="fish fish-3">🐡</div>
        <div class="dolphin">🐬</div>
        <div class="whale">🐋</div>
      </div>

      <!-- 海底装饰 -->
      <div class="sea-bottom">
        <div class="coral coral-1">🪸</div>
        <div class="coral coral-2">🪸</div>
        <div class="shell shell-1">🐚</div>
        <div class="shell shell-2">🐚</div>
        <div class="starfish">⭐</div>
      </div>
    </div>

    <!-- 简州猫漂浮 -->
    <div class="floating-cats">
      <div class="cat-bubble cat-1">
        <div class="bubble">
          <span class="cat">🐱</span>
        </div>
      </div>
      <div class="cat-bubble cat-2">
        <div class="bubble">
          <span class="cat">😺</span>
        </div>
      </div>
    </div>

    <!-- 泡泡效果 -->
    <div class="bubbles">
      <div class="bubble-item bubble-1"></div>
      <div class="bubble-item bubble-2"></div>
      <div class="bubble-item bubble-3"></div>
      <div class="bubble-item bubble-4"></div>
      <div class="bubble-item bubble-5"></div>
      <div class="bubble-item bubble-6"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const isNight = ref(false)

onMounted(() => {
  // 根据时间判断是白天还是夜晚
  const hour = new Date().getHours()
  isNight.value = hour < 6 || hour >= 20
})
</script>

<style scoped>
.ocean-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

/* ===== 天空层 ===== */
.sky-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50%;
}

/* 云朵 */
.cloud-group {
  position: relative;
  width: 100%;
  height: 100%;
}

.cloud {
  position: absolute;
  font-size: 48px;
  opacity: 0.6;
  animation: float-cloud 30s infinite ease-in-out;
}

.cloud-1 {
  top: 10%;
  left: 5%;
  animation-delay: 0s;
}

.cloud-2 {
  top: 25%;
  left: 60%;
  animation-delay: -10s;
  font-size: 56px;
}

.cloud-3 {
  top: 15%;
  right: 15%;
  animation-delay: -20s;
  font-size: 40px;
}

.cloud-4 {
  top: 35%;
  left: 30%;
  animation-delay: -15s;
  font-size: 52px;
}

@keyframes float-cloud {
  0%, 100% { transform: translateX(0) translateY(0); }
  25% { transform: translateX(30px) translateY(-10px); }
  50% { transform: translateX(60px) translateY(5px); }
  75% { transform: translateX(30px) translateY(-5px); }
}

/* 太阳/月亮 */
.celestial {
  position: absolute;
  top: 8%;
  right: 10%;
  font-size: 64px;
  animation: celestial-glow 4s infinite ease-in-out;
}

.sun {
  animation: rotate-sun 20s infinite linear;
  display: inline-block;
}

@keyframes rotate-sun {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.moon {
  animation: moon-glow 3s infinite ease-in-out;
  display: inline-block;
}

@keyframes moon-glow {
  0%, 100% { opacity: 0.8; }
  50% { opacity: 1; }
}

@keyframes celestial-glow {
  0%, 100% { filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5)); }
  50% { filter: drop-shadow(0 0 20px rgba(255, 255, 255, 0.8)); }
}

/* 海鸥 */
.seagull-group {
  position: relative;
  width: 100%;
  height: 100%;
}

.seagull {
  position: absolute;
  font-size: 24px;
  animation: fly-seagull 25s infinite ease-in-out;
  opacity: 0.7;
}

.seagull-1 {
  top: 15%;
  left: -5%;
  animation-delay: 0s;
}

.seagull-2 {
  top: 28%;
  left: 40%;
  animation-delay: -10s;
}

.seagull-3 {
  top: 20%;
  left: 70%;
  animation-delay: -15s;
}

@keyframes fly-seagull {
  0% { transform: translateX(0) translateY(0) rotate(0deg); }
  25% { transform: translateX(30vw) translateY(-20px) rotate(5deg); }
  50% { transform: translateX(60vw) translateY(10px) rotate(-5deg); }
  75% { transform: translateX(90vw) translateY(-15px) rotate(3deg); }
  100% { transform: translateX(120vw) translateY(0) rotate(0deg); }
}

/* 星星 */
.stars {
  position: absolute;
  width: 100%;
  height: 100%;
}

.star {
  position: absolute;
  animation: twinkle 2s infinite ease-in-out;
}

.star-1 {
  top: 10%;
  left: 15%;
  font-size: 20px;
  animation-delay: 0s;
}

.star-2 {
  top: 25%;
  left: 40%;
  font-size: 16px;
  animation-delay: 0.5s;
}

.star-3 {
  top: 15%;
  right: 25%;
  font-size: 18px;
  animation-delay: 1s;
}

.star-4 {
  top: 35%;
  left: 70%;
  font-size: 14px;
  animation-delay: 1.5s;
}

.star-5 {
  top: 20%;
  right: 10%;
  font-size: 22px;
  animation-delay: 2s;
}

@keyframes twinkle {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.2); }
}

/* ===== 海洋层 ===== */
.ocean-layer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
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
  opacity: 0.3;
}

.wave-1 {
  background: linear-gradient(to bottom, transparent, rgba(33, 150, 243, 0.2));
  border-radius: 50% 50% 0 0;
  animation: wave-motion 8s infinite ease-in-out;
}

.wave-2 {
  background: linear-gradient(to bottom, transparent, rgba(30, 144, 255, 0.3));
  border-radius: 60% 40% 0 0;
  animation: wave-motion 10s infinite ease-in-out;
  animation-delay: -2s;
}

.wave-3 {
  background: linear-gradient(to bottom, transparent, rgba(25, 118, 210, 0.4));
  border-radius: 40% 60% 0 0;
  animation: wave-motion 12s infinite ease-in-out;
  animation-delay: -4s;
}

.wave-4 {
  background: linear-gradient(to bottom, transparent, rgba(21, 101, 192, 0.5));
  border-radius: 55% 45% 0 0;
  animation: wave-motion 14s infinite ease-in-out;
  animation-delay: -6s;
}

@keyframes wave-motion {
  0%, 100% { transform: translateX(0) translateY(0); }
  50% { transform: translateX(-25%) translateY(-15px); }
}

/* 小船 */
.boats {
  position: absolute;
  width: 100%;
  height: 100%;
}

.boat {
  position: absolute;
  font-size: 40px;
  animation: boat-float 8s infinite ease-in-out;
  filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.2));
}

.boat-1 {
  bottom: 25%;
  left: 15%;
  animation-delay: 0s;
}

.boat-2 {
  bottom: 30%;
  right: 20%;
  animation-delay: -4s;
}

@keyframes boat-float {
  0%, 100% { transform: translateY(0) rotate(-3deg); }
  50% { transform: translateY(-20px) rotate(3deg); }
}

/* 海洋生物 */
.sea-creatures {
  position: absolute;
  width: 100%;
  height: 100%;
}

.fish {
  position: absolute;
  font-size: 24px;
  animation: swim-fish 20s infinite linear;
  opacity: 0.6;
}

.fish-1 {
  bottom: 40%;
  left: -10%;
  animation-delay: 0s;
}

.fish-2 {
  bottom: 55%;
  left: -10%;
  animation-delay: -7s;
}

.fish-3 {
  bottom: 48%;
  left: -10%;
  animation-delay: -14s;
}

@keyframes swim-fish {
  0% { transform: translateX(0) scaleX(1); }
  45% { transform: translateX(50vw) scaleX(1); }
  50% { transform: translateX(50vw) scaleX(-1); }
  95% { transform: translateX(0) scaleX(-1); }
  100% { transform: translateX(0) scaleX(1); }
}

.dolphin {
  position: absolute;
  bottom: 35%;
  right: -5%;
  font-size: 32px;
  animation: dolphin-jump 15s infinite ease-in-out;
  opacity: 0.7;
}

@keyframes dolphin-jump {
  0%, 100% { transform: translateX(0) translateY(0) rotate(0deg); }
  25% { transform: translateX(-30vw) translateY(-50px) rotate(-15deg); }
  50% { transform: translateX(-60vw) translateY(0) rotate(0deg); }
  75% { transform: translateX(-90vw) translateY(-40px) rotate(-10deg); }
}

.whale {
  position: absolute;
  bottom: 20%;
  left: -10%;
  font-size: 48px;
  animation: whale-swim 40s infinite linear;
  opacity: 0.5;
}

@keyframes whale-swim {
  0% { transform: translateX(0); }
  100% { transform: translateX(120vw); }
}

/* 海底装饰 */
.sea-bottom {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 100px;
}

.coral,
.shell,
.starfish {
  position: absolute;
  bottom: 10px;
  opacity: 0.4;
  animation: sway 4s infinite ease-in-out;
}

.coral-1 {
  left: 20%;
  font-size: 32px;
  animation-delay: 0s;
}

.coral-2 {
  right: 25%;
  font-size: 28px;
  animation-delay: -2s;
}

.shell-1 {
  left: 40%;
  font-size: 24px;
  animation-delay: -1s;
}

.shell-2 {
  right: 45%;
  font-size: 20px;
  animation-delay: -3s;
}

.starfish {
  left: 60%;
  font-size: 28px;
  animation-delay: -1.5s;
}

@keyframes sway {
  0%, 100% { transform: rotate(-5deg); }
  50% { transform: rotate(5deg); }
}

/* ===== 漂浮的猫 ===== */
.floating-cats {
  position: absolute;
  width: 100%;
  height: 100%;
}

.cat-bubble {
  position: absolute;
  animation: float-cat 12s infinite ease-in-out;
}

.cat-1 {
  bottom: 35%;
  left: 10%;
  animation-delay: 0s;
}

.cat-2 {
  bottom: 50%;
  right: 15%;
  animation-delay: -6s;
}

.bubble {
  position: relative;
  width: 80px;
  height: 80px;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.8), rgba(135, 206, 250, 0.4));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 
    inset 0 -10px 20px rgba(255, 255, 255, 0.3),
    0 4px 15px rgba(0, 0, 0, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.5);
}

.cat {
  font-size: 32px;
  animation: cat-rotate 3s infinite ease-in-out;
}

@keyframes float-cat {
  0%, 100% { transform: translateY(0) translateX(0); }
  25% { transform: translateY(-30px) translateX(20px); }
  50% { transform: translateY(-10px) translateX(0); }
  75% { transform: translateY(-25px) translateX(-15px); }
}

@keyframes cat-rotate {
  0%, 100% { transform: rotate(0deg); }
  50% { transform: rotate(360deg); }
}

/* ===== 泡泡效果 ===== */
.bubbles {
  position: absolute;
  width: 100%;
  height: 100%;
}

.bubble-item {
  position: absolute;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.8), rgba(135, 206, 250, 0.3));
  border-radius: 50%;
  opacity: 0.6;
  animation: rise-bubble 10s infinite ease-in-out;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.bubble-1 {
  width: 20px;
  height: 20px;
  bottom: -20px;
  left: 15%;
  animation-delay: 0s;
}

.bubble-2 {
  width: 30px;
  height: 30px;
  bottom: -30px;
  left: 35%;
  animation-delay: -2s;
}

.bubble-3 {
  width: 15px;
  height: 15px;
  bottom: -15px;
  left: 55%;
  animation-delay: -4s;
}

.bubble-4 {
  width: 25px;
  height: 25px;
  bottom: -25px;
  left: 70%;
  animation-delay: -6s;
}

.bubble-5 {
  width: 18px;
  height: 18px;
  bottom: -18px;
  left: 85%;
  animation-delay: -8s;
}

.bubble-6 {
  width: 22px;
  height: 22px;
  bottom: -22px;
  right: 10%;
  animation-delay: -3s;
}

@keyframes rise-bubble {
  0% {
    transform: translateY(0) translateX(0);
    opacity: 0;
  }
  10% {
    opacity: 0.6;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    transform: translateY(-100vh) translateX(50px);
    opacity: 0;
  }
}

/* 响应式 */
@media (max-width: 768px) {
  .cloud,
  .seagull,
  .boat,
  .fish,
  .dolphin,
  .whale {
    font-size: 50%;
  }

  .celestial {
    font-size: 48px;
  }

  .cat-bubble .bubble {
    width: 60px;
    height: 60px;
  }

  .cat {
    font-size: 24px;
  }
}
</style>