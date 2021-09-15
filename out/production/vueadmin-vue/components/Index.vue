<template>
    <div>index
        <el-button @click="tryTest">try</el-button>
    </div>

</template>

<script>
import request from '../network/request'
export default {
    name: "Index",
    data() {
        return {}
    },
    methods:{
        tryTest(){
            request({
                methods: 'get',
                url: 'http://localhost:8081/test?null='
            }).then(response =>{
                this.$message.success(response.data.message)
            })
        }
    }
}
</script>

<style scoped>

</style>