<template>
  <div>
    <el-col :span="6">
      <el-form :model="editForm" :rules="rules" ref="editForm">
        <el-form-item label="用户名" prop="userName" label-width="80px">
          <span>{{ editForm.userName }}</span>
        </el-form-item>

        <el-form-item label="昵称" prop="nickName" label-width="80px">
          <el-input v-model="editForm.nickName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="手机号" prop="phone" label-width="80px">
          <el-input v-model="editForm.phone" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item style="display: flex; flex-direction: row; justify-content: space-around">
          <el-button @click="resetForm('editForm')">重 置</el-button>
          <el-button type="primary" @click="submitForm('editForm')">修 改</el-button>
        </el-form-item>
      </el-form>
      <el-form :inline="true" class="demo-form-inline" style="margin-left: 25%; margin-top: 15%">
        <el-form-item>
          <el-button type="primary" @click="changeAvatarDialogVisible = true">修改头像</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="changePasswordDialogVisible = true">修改密码</el-button>
        </el-form-item>
      </el-form>

      <!--      更换头像对话框-->
      <el-dialog
          title="更换头像"
          :visible.sync="changeAvatarDialogVisible"
          @close="fileList = []"
          width="25%">
        <el-form>
          <el-form-item>
            <el-upload
                ref="upload"
                class="upload-demo"
                name="imgs"
                :headers="upHeaders"
                :auto-upload="false"
                action=""
                :http-request="upFile"
                :on-remove="handleRemove"
                :file-list="fileList"
                :on-exceed="handleExceed"
                :on-error="handleUploadError"
                :on-success="handleUploadSuccess"
                list-type="picture">
              <el-button size="small" type="primary">点击上传</el-button>&nbsp;&nbsp;
              <span slot="tip" class="el-upload__tip">(只能上传一个jpg/png文件，且不超过15MB)</span>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button @click="subPicForm">确认</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>

      <!--      修改密码对话框-->
      <el-dialog
          title=""
          :visible.sync="changePasswordDialogVisible"
          @close="resetForm('changePasswordForm')"
          width="25%">
        <el-form :model="changePasswordForm.oldPassword" :rules="changePasswordFormRules" ref="changePasswordForm">
          <el-form-item label="原密码" prop="oldPassword" label-width="80px">
            <el-input v-model="changePasswordForm.oldPassword" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="新密码" prop="newPassword" label-width="80px">
            <el-input v-model="changePasswordForm.newPassword" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item style="display: flex; flex-direction: row; justify-content: space-around">
            <el-button @click="resetForm('changePasswordForm')">重 置</el-button>
            <el-button type="primary" @click="submitForm('changePasswordForm')">确 认</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>
    </el-col>

  </div>
</template>

<script>
import request from "@/network/request";

export default {
  name: "UserCenter",
  data() {
    return {
      //修改用户基本信息表格，修改密码，头像需要单独操作
      editForm: {
        phone: '',
        userName: '',
        nickName: ''
      },
      changePasswordForm: {
        oldPassword: '',
        newPassword: ''
      },
      rules: {},
      changePasswordFormRules: {},
      fileList: [],
      changeAvatarDialogVisible: false,
      changePasswordDialogVisible: false,
    }
  },
  methods: {
    getUserInfo() {
      request({
        method: 'get',
        url: 'http://localhost:8081/user/userInfo',
      }).then(resp => {
        this.editForm = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    updateUser() {
      request({
        method: 'put',
        url: 'http://localhost:8081/user/updateInfo',
        data: this.editForm
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.getUserInfo()
        this.$bus.$emit('updateUserInfo')
        this.fileList = []
      }, error => {
        this.$message.error(error)
      })
    },
    changePassword() {
      request({
        method: 'put',
        url: 'http://localhost:8081/user/changePassword',
        data: this.changePasswordForm
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.changePasswordDialogVisible = false
        this.$bus.$emit('logout')
        this.$router.push('/login')
      }, error => {
        this.$message.error(error)
      })
    },
    changeAvatar(data) {
      request({
        method: 'put',
        url: 'http://localhost:8081/user/updateInfo',
        data
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.fileList = []
        this.getUserInfo()
        this.$bus.$emit('updateUserInfo')
      }, error => {
        this.$message.error(error)
      })
    },
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == 'editForm') {
            this.updateUser()
          } else {
            this.changePassword()
          }
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
      this.editForm = {}
    },

    //调用elementui上传组件的submit方法会后，其会遍历文件数组并调用该方法
    upFile(params) {
      this.picFormData.append('imgs', params.file)
    },
    subPicForm() {
      this.picFormData = new FormData()
      this.$refs.upload.submit()
      request({
        method: 'post',
        url: 'http://localhost:8081/book/upImg',
        data: this.picFormData,
        headers: {'Content-Type': 'multipart/form-data'}
      }).then(resp => {
        let data = {avatar: resp.data.data}
        this.changeAvatar(data)
      }, error => {
        this.$message.error(error)
      })

      //上传完成后重置存放图片的数据表单
      this.picFormData = null
    },
    //封面上传成功后，将返回的图片源保存
    handleUploadSuccess(response) {
      this.editForm.avatar = response.data.data;
    },
    //封面上传失败
    handleUploadError(err) {
      this.$message.error(err.message);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    handleRemove(file, fileList) {
      console.log(file);
      console.log("fileList: ", fileList)
    },
  },
  computed: {
    upHeaders() {
      let upHeaders = {}
      let tokenName = sessionStorage.getItem('tokenName')
      let tokenValue = sessionStorage.getItem('tokenValue')
      upHeaders[tokenName] = tokenValue
      upHeaders['Content-Type'] = 'multipart/form-data'
      return upHeaders
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style scoped>

</style>