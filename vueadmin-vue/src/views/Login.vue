<template>
  <div id="loginTopDiv">
    <el-row type="flex" class="el-row1" justify="center">
      <el-col :span=5.5>
        <el-card style="padding-right: 35px" id="loginCard">

          <el-form :model="logForm" :rules="rules" ref="logForm" label-width="100px" class="demo-logForm">
            <el-form-item>
              <el-image fit="cover"
                        :src="avatarSrc"
                        style="width: 80px; height: 80px; border: 1px solid transparent;border-radius: 60px"
              >
                <div slot="error" class="image-slot">
                  <span></span>
                </div>
              </el-image>
            </el-form-item>

            <el-form-item label=" 用户名" prop="userName" class="label_item">
              <el-input v-model="logForm.userName"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password" class="label_item">
              <el-input v-model="logForm.password" show-password></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="captcha" class="label_item">
              <el-input v-model="logForm.captcha"
                        style="width: 120px;float: left"

              ></el-input>
              <img :src="captchaImg" style="float: left;margin-left: 8px; border-radius: 3px"
                   @click="changeCaptcha">
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('logForm')">登录
              </el-button>
              <el-button @click="resetForm('logForm')">重置</el-button>
              <el-button type="text" @click="regist">立即注册</el-button>

            </el-form-item>
          </el-form>

        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getCaptcha, getUserAvatarSrc, login} from "@/api";

export default {
  name: "Login",
  data() {
    return {
      logForm: {
        userName: '',
        password: '',
        captcha: '',
        captchaKey: null
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
        captcha: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
        ],
      },
      captchaImg: null,
      showAvatar: false,
      avatarSrc: 'https://img1.baidu.com/it/u=2173543220,220911097&fm=26&fmt=auto'
    };
  },
  watch: {
    'logForm.userName': {
      handler() {
        let params = {userName: this.logForm.userName}
        getUserAvatarSrc(params).then(resp => {
          this.avatarSrc = resp.data.data
        }, error => {
          this.$message.error(error)
        })
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
            if (valid) {
              let params = {
                captchaKey: this.logForm.captchaKey,
                captcha: this.logForm.captcha
              }
              login(params, this.logForm).then(Response => {
                let tokenName = Response.data.data.tokenName;
                let tokenValue = Response.data.data.tokenValue;
                let token = {
                  tokenName,
                  tokenValue
                }
                this.$store.commit('SET_TOKEN', token)
                this.$store.commit('SET_ISSYSUSER', 0)
                this.$router.push('/index')
              }, error => {
                this.$message.error(error)
                this.getCaptcha()
              })
            } else {
              this.$message({
                message: '内容不符合要求，请重新输入！',
                type: "warning"
              })
              return false;
            }
          }
      )

    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
    ,
    regist() {
      this.$router.push('/register')
    }
    ,
    getCaptcha() {
      getCaptcha().then(resp => {
            this.logForm.captchaKey = resp.data.data.captchaKey;
            this.captchaImg = resp.data.data.captchaImg
          },
          error => {
            this.$message.error(error)
          })
    }
    ,
    changeCaptcha() {
      getCaptcha().then(resp => {
            this.logForm.captchaKey = resp.data.data.captchaKey;
            this.captchaImg = resp.data.data.captchaImg
          },
          error => {
            this.$message.error(error)
          })
      this.logForm.captcha = ''
    }
  },
  created() {
    this.getCaptcha();
  }
}
</script>

<style>
.el-row1 {
  height: 100vh;
  display: flex;
  align-items: center;
  text-align: center;
}


#loginTopDiv {
  background-image: url("../assets/bg7.jpg");
}

#loginCard {
  padding-right: 35px;
  background-color: transparent;
}

.label_item .el-form-item__label {
  color: white;
}

</style>