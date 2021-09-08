<template>
    <div>
        <el-row type="flex" class="el-row" justify="center" >
            <el-col :span=6>
                <h3>KONO曾淇杭哒！的管理系统</h3>
                <img src="../assets/wechat.png" width="80%">
                <P>扫描二维码来免费帮你拼多多砍一刀</P>
            </el-col>

            <el-col :span=1>
                <el-divider direction="vertical"></el-divider>
            </el-col>
            <el-col :span=6>
                <el-form :model="logForm" :rules="rules" ref="logForm" label-width="100px" class="demo-logForm">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="logForm.username" ></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input v-model="logForm.password" show-password ></el-input>
                    </el-form-item>
                    <el-form-item label="验证码" prop="captcha">
                        <el-input v-model="logForm.captcha"
                                  style="width: 150px;float: left"

                        ></el-input>
                        <img :src="captchaImg" style="float: left;margin-left: 8px; border-radius: 3px" @click="changeCaptcha">
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('logForm')">立即登录</el-button>
                        <el-button @click="resetForm('logForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>

        </el-row>
    </div>
</template>

<script>
import AxiosInstance from "../network/request"
import Mock from "mockjs"
import element from "element-ui";
export default {
    name: "Login",
    data() {
        return {
            logForm: {
                username: '',
                password: '',
                captcha: '',
                captchaKey:null
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                password:[
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
                ],
                captcha:[
                    { required: true, message: '请输入验证码', trigger: 'blur' },
                ],
            },
            captchaImg:null,
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                /*this.$router.push('/index')*/
                AxiosInstance({
                    method: 'post',
                    params:{
                        captchaKey: this.logForm.captchaKey,
                        captcha: this.logForm.captcha
                    },
                    data: this.logForm,
                    url:"http://localhost:8081/login",
                }).then(Response =>{
                    let tokenName = Response.data.data.tokenName;
                    let tokenValue = Response.data.data.tokenValue;
                    let token = {
                        tokenName,
                        tokenValue
                    }
                    this.$store.commit('SET_TOKEN',token)
                    this.$router.push('/index')
                },error =>{
                    this.$message.error(error)
                })
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        getCaptcha(){
            AxiosInstance({
                method:"get",
                url:"http://localhost:8081/captcha"
            }).then(resp =>{
                    this.logForm.captchaKey = resp.data.data.captchaKey;
                    this.captchaImg = resp.data.data.captchaImg
            },
                error =>{
                this.$message.error(error)
            })
        },
        changeCaptcha(){
            AxiosInstance({
                method:"get",
                url:"http://localhost:8081/captcha"
            }).then(resp =>{
                    this.logForm.captchaKey = resp.data.data.captchaKey;
                    this.captchaImg = resp.data.data.captchaImg
                },
                error =>{

                })
            this.logForm.captcha = ''
        }
    },
    created() {
        this.getCaptcha();
    }
}
</script>

<style scoped>
.el-row{
    height: 100vh;
    display: flex;
    align-items: center;
    text-align: center;
}

.el-divider{
    height: 200px;
}
</style>