<template>
    <div id="loginTopDiv">
        <el-row type="flex" class="el-row" justify="center">
            <el-col :span=5.5>
                <el-card style="padding-right: 35px" id="loginCard">

                    <el-form :model="logForm" :rules="rules" ref="logForm" label-width="100px" class="demo-logForm">
                        <el-form-item label="用户名" prop="username">
                            <el-input v-model="logForm.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input v-model="logForm.password" show-password></el-input>
                        </el-form-item>
                        <el-form-item label="验证码" prop="captcha">
                            <el-input v-model="logForm.captcha"
                                      style="width: 120px;float: left"

                            ></el-input>
                            <img :src="captchaImg" style="float: left;margin-left: 8px; border-radius: 3px"
                                 @click="changeCaptcha">
                        </el-form-item>
                        <el-form-item>
                            <el-button style="margin-left: 0" type="primary" @click="submitForm('logForm')">立即登录
                            </el-button>
                            <el-button @click="resetForm('logForm')">重置</el-button>
                        </el-form-item>
                    </el-form>

                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import AxiosInstance from "../network/request"

export default {
    name: "Login",
    data() {
        return {
            logForm: {
                username: '',
                password: '',
                captcha: '',
                captchaKey: null
            },
            rules: {
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
                ],
                captcha: [
                    {required: true, message: '请输入验证码', trigger: 'blur'},
                ],
            },
            captchaImg: null,
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                /*this.$router.push('/index')*/
                AxiosInstance({
                    method: 'post',
                    params: {
                        captchaKey: this.logForm.captchaKey,
                        captcha: this.logForm.captcha
                    },
                    data: this.logForm,
                    url: "http://localhost:8081/login",
                }).then(Response => {
                    let tokenName = Response.data.data.tokenName;
                    let tokenValue = Response.data.data.tokenValue;
                    let token = {
                        tokenName,
                        tokenValue
                    }
                    this.$store.commit('SET_TOKEN', token)
                    this.$router.push('/index')
                }, error => {
                    this.$message.error(error)
                })
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        getCaptcha() {
            AxiosInstance({
                method: "get",
                url: "http://localhost:8081/captcha"
            }).then(resp => {
                    this.logForm.captchaKey = resp.data.data.captchaKey;
                    this.captchaImg = resp.data.data.captchaImg
                },
                error => {
                    this.$message.error(error)
                })
        },
        changeCaptcha() {
            AxiosInstance({
                method: "get",
                url: "http://localhost:8081/captcha"
            }).then(resp => {
                    this.logForm.captchaKey = resp.data.data.captchaKey;
                    this.captchaImg = resp.data.data.captchaImg
                },
                error => {

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
.el-row {
    height: 100vh;
    display: flex;
    align-items: center;
    text-align: center;
}

.el-divider {
    height: 200px;
}

#loginTopDiv {
    background-image: url("../assets/bg.png");
}

#loginCard {
    padding-right: 35px;
    background-color: rgb(228, 226, 222);
}
</style>