<template>
  <div id="registerTopDiv">
    <el-row type="flex" class="el-row" justify="center">
      <el-col :span=5.5>
        <el-card style="padding-right: 35px" id="registerCard">
          <el-form :model="logForm" :rules="rules" ref="logForm" label-width="100px" class="demo-logForm">
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="logForm.userName"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="logForm.nickName"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="logForm.password" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('logForm')">立即注册
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
import {register} from "@/api";

export default {
  name: "Login",
  data() {
    return {
      logForm: {
        userName: '',
        nickName: '',
        password: '',
      },
      rules: {
        userName: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        nickName: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ]
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          register(this.logForm).then(response => {
            this.$confirm('注册成功，是否立刻去登录...', '', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'success'
            }).then(() => {
              this.$router.push('/login')
            })
          }, error => {
            this.$message.error(error)
          })
        } else {
          this.$message({
            message: '内容不符合要求，请重新输入！',
            type: "warning"
          })
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
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


#registerTopDiv {
  background-image: url("../assets/bg8.jpg");
}

#registerCard {
  padding-right: 35px;
  background-color: white;
}
</style>