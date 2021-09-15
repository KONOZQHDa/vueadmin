<template>
    <el-container>
<!--        左边导航栏-->
        <el-aside width="200px">
            <SideMenu/>
        </el-aside>
        <el-container>
            <el-header>
                <em>KONO曾淇杭哒！后台管理系统</em>
                <div class="header-avatar">

                    <el-avatar :src="userInfo.avatar"></el-avatar>
                    <el-dropdown>
                    <span class="el-dropdown">
                    {{userInfo.username}}<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item>
                                <router-link to="/userCenter">个人中心</router-link>
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="logOut">退出</el-dropdown-item>

                        </el-dropdown-menu>
                    </el-dropdown>

                    <span><el-link href="http://localhost:8080/#/login" target="_self">登录</el-link></span>
                    <span><el-link href="https://www.bilibili.com/" target="_blank">B站</el-link></span>
                </div>

            </el-header>
            <el-main>
                <Tag></Tag>
                <div style="margin: 0 15px">
                    <router-view></router-view>
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import SideMenu from "./SideMenu"
import Tag from "./Tag"
import request from '../network/request'
export default {
    name: "Home",
    data() {
        return {
            logForm: {
                userName: '',
                password: '',
                code: ''
            },
            rules: {
                userName: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
                ],
                code: [
                    {required: true, message: '请输入验证码', trigger: 'blur'},
                ],
            },
            userInfo:{
                username:'',
                avatar:''
            },
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    alert('submit!');
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        getUserInfo(){
            request({
                methods: 'get',
                url: 'http://localhost:8081/sysUser/userInfo',
            }).then(resp => {
                this.userInfo = resp.data.data
                this.$store.state.menu.authorizations = resp.data.data.authorizations
            }, error => {
            })
        },
        logOut(){
            request({
                methods: 'get',
                url: 'http://localhost:8081/logout'
            }).then(resp =>{
                this.$store.commit("CLEAR_TOKEN")
                localStorage.clear()
                sessionStorage.clear()
                this.$store.state.menu.editableTabs = []
                this.$store.state.menu.editableTabsValue = ''
                this.$store.state.menu.authorizations = []
                this.$router.push('/login')
            },error =>{
                this.$message.error(error)
            })
        },
        getSessionStorage(){
            //获取sessionStorage中的tabs和activeName，从而实现刷新页面标签不会消失
            let activeName = sessionStorage.getItem('activeName')
            if (activeName) {
                this.$store.state.menu.editableTabsValue = sessionStorage.getItem('activeName')
            }
            let tabs = sessionStorage.getItem('tabs')
            if (tabs) {
                this.$store.state.menu.editableTabs = JSON.parse(tabs)
            }
        }
    },
    components:{
        SideMenu,
        Tag
    },
    created() {
        this.getUserInfo()
        this.getSessionStorage()
    },
}
</script>

<style scoped>
.el-container {
    padding: 0;
    margin: 0;
    height: 100vh;
}

.el-row {
    height: 100vh;
    display: flex;
    align-items: center;
    text-align: center;
}

.el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
}

.el-aside {
    background-color: #D3DCE6;
    color: #333;
    line-height: 200px;
    padding: 0;
    margin: 0;
    height: 100%;
}

.el-main {
    background-color: white;
    color: #333;
    padding: 0;
}

body > .el-container {
    margin-bottom: 40px;
}

.el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
}

.el-icon-arrow-down {
    font-size: 12px;
}

.header-avatar {
    float: right;
    width: 250px;
    display: flex;
    justify-content: space-around;
    align-items: center;
}

.el-header{
    background-color: #17B3A3;
}

</style>