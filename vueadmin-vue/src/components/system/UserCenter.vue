<template>
  <div>
    <el-col :span="6">
      <el-form :model="editForm" :rules="rules" ref="editForm">
        <el-form-item label="用户名" prop="username" label-width="80px">
          <span>{{ editForm.username }}</span>
        </el-form-item>

        <el-form-item label="邮箱" prop="email" label-width="80px">
          <el-input v-model="editForm.email" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="头像" prop="avatar" label-width="80px">
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

        <el-form-item style="display: flex; flex-direction: row; justify-content: space-around">
          <el-button @click="resetForm('editForm')">重 置</el-button>
          <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
        </el-form-item>

      </el-form>
    </el-col>

  </div>
</template>

<script>
import request from "@/network/request";

export default {
  name: "UserCenter",
  data() {
    return {
      editForm: {
        email: '',
        avatar: '',
        username: ''
      },
      rules: [],
      fileList: [],
    }
  },
  methods: {
    getUserInfo() {
      request({
        method: 'get',
        url: 'http://localhost:8081/sysUser/userInfo',
      }).then(resp => {
        this.editForm = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    updateUser() {
      request({
        method: 'put',
        url: 'http://localhost:8081/sysUser/updateUser',
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
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.subPicForm()
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
    //设置上传封面时的请求头
    upHeaders() {
      let upHeaders = {}
      let tokenName = sessionStorage.getItem('tokenName')
      let tokenValue = sessionStorage.getItem('tokenValue')
      upHeaders[tokenName] = tokenValue
      upHeaders['Content-Type'] = 'multipart/form-data'
      return upHeaders
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
        this.editForm.avatar = resp.data.data
        this.updateUser()
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
  created() {
    this.getUserInfo()
  }
}
</script>

<style scoped>

</style>