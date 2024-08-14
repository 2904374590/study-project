<script setup>


</script>

<template>
  <div style="width: 100vw;height: 100vh; overflow: hidden;display: flex">
    <div style="flex: 1;width: 100%;height: 100%;">
      <el-image style="width: 100%;height: 100%;" fit="cover"
                src="https://ts1.cn.mm.bing.net/th/id/R-C.6b5df1bfe0e4778a44dba0753cd169c8?rik=QRQIMqvjWRCO5Q&riu=http%3a%2f%2fpic39.nipic.com%2f20140321%2f8857347_232251363165_2.jpg&ehk=7oAaMo6LCHJc%2bqpQ0IPvcH7v69jGRQhb2vDz%2fOd5720%3d&risl=&pid=ImgRaw&r=0" />
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold ">欢迎来到我们的学习平台</div>
      <div style="margin-top: 10px">在这里你可以学习如何使用Java，如何搭建网站，并且与Java之父密切交流!</div>
      <div style="margin-top: 10px">在这里你可以同性交友，因为都是男的</div>
    </div>
    <div style="width: 400px;background: white;z-index: 1">
<!--    路由---  右面的页面-->
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear">
          <component :is="Component" />
        </transition>
      </router-view>

    </div>
  </div>
</template>
<script setup>

</script>

<style scoped>
  .welcome-title {
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
  }
</style>