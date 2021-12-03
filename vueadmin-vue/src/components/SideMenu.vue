<template>
  <el-menu
      :default-active="editableTabsValue"
      class="el-menu-vertical-demo"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b">
    <!--    <router-link to="/index">-->
    <!--      <el-menu-item index="Index" @click="addTab({name:'Index',title:'首页'})">-->
    <!--        <template slot="title">-->
    <!--          <i class="el-icon-s-home"></i>-->
    <!--          <span slot="title">首页</span>-->
    <!--        </template>-->
    <!--      </el-menu-item>-->
    <!--    </router-link>-->

    <el-submenu v-for="menu in menuList" :index="menu.name">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span>{{ menu.title }}</span>
      </template>

      <router-link v-for="menuitem in menu.children" :to="menuitem.path">
        <!--                点击导航后添加对应标签-->
        <el-menu-item :index="menuitem.name" @click="addTab(menuitem)">
          <template slot="title">
            <i :class="menuitem.icon"></i>
            {{ menuitem.title }}
          </template>
        </el-menu-item>
      </router-link>
    </el-submenu>

  </el-menu>
</template>

<script>
export default {
  name: "SideMenu",
  data() {
    return {}
  },
  methods: {
    //点击导航项后添加对应标签
    addTab(tab) {
      this.$store.commit('menu/ADD_TAB', tab)
    }
  },
  computed: {
    menuList() {
      return this.$store.state.menu.menuList
    },
    editableTabsValue() {
      return this.$store.state.menu.editableTabsValue
    }
  }
}
</script>

<style scoped>
.el-menu {
  height: 100%;
  text-align: left;
}

</style>