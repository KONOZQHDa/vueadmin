<template>
    <el-tabs v-model="editableTabsValue" type="card" closable @tab-remove="removeTab" @tab-click="changeRoute ">
        <el-tab-pane
            v-for="(item, index) in editableTabs"
            :key="item.name"
            :label="item.title"
            :name="item.name"
        >
        </el-tab-pane>
    </el-tabs>
</template>

<script>
export default {
    data() {
        return {
        }
    },
    methods: {
        //删除标签
        removeTab(targetName){
            this.$store.commit('menu/REMOVE_TAB',targetName)
            let tabs = this.$store.state.menu.editableTabs
            let editableTabValue = this.$store.state.menu.editableTabsValue
            //删除后修改路由，使之跳转到新的对应页面
            tabs.forEach(tab =>{
                if (tab.name == editableTabValue) {
                    this.$router.push(tab.path)
                    return;
                }
            })
        },
        //点击标签后修改路由跳转到对应的页面
        changeRoute(target){
            let tabs = this.$store.state.menu.editableTabs
            tabs.forEach(tab =>{
                if (tab.name == target.name) {
                    this.$router.push(tab.path)
                    return;
                }
            })

        }
    },
    computed:{
        editableTabsValue:{
            get(){
                return this.$store.state.menu.editableTabsValue
            },
            set(value){
                this.$store.state.menu.editableTabsValue = value
            }
        },
        editableTabs(){
            return this.$store.state.menu.editableTabs
        },

    }
}
</script>

<style scoped>

</style>