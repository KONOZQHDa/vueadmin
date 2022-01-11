<template>
  <div>
    <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchKey" clearable placeholder="请输入书籍类型名..."></el-input>
      </el-form-item>

      <el-form-item>
        <el-button v-if="hasAuthorization('sys:type:list')" @click="getBookTypes">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true"
                   v-if="hasAuthorization('sys:type:save')">新增
        </el-button>
      </el-form-item>
    </el-form>
    <!--        字典展示表格-->
    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border>
      <el-table-column
          label="类型名称"
          align="center"
          prop="sortName"
          width="180">
      </el-table-column>
      <el-table-column
          align="center"
          width="100px"
          prop="usfulStatus"
          label="状态">
        <template slot-scope="scope">
          <el-tag :type="formatByDiction(usfulStatusTagType,scope.row.usful)">
            {{ formatByDiction(glUsful, scope.row.usful) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          width="260px"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editTypeHandle(scope.row.id)"
                     v-if="hasAuthorization('sys:type:update')">编辑
          </el-button>


          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:type:delete')"></el-divider>

          <el-popconfirm
              title="确人删除吗？"
              @confirm="deleteType(scope.row.id)"
          >
            <el-button type="text" slot="reference"
                       v-if="hasAuthorization('sys:type:delete')">删除
            </el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
    <!--        点击新增或编辑(dictype)弹出的对话框-->
    <el-dialog
        title="书籍类型"
        :visible.sync="dialogVisible"
        @close="resetForm('typeEditForm')"
        width="27%">
      <el-form :model="typeEditForm"
               ref="typeEditForm"
               :rules="rules"
               label-width="100px"
               class="demo-editForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="typeEditForm.sortName"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="typeEditForm.usful">
            <el-radio v-for="(item,index) in $store.state.diction.dictypes[glUsful]" :label="item.dicKey"
                      :key="index">{{ item.dicValue }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('typeEditForm')">确认</el-button>
          <el-button @click="resetForm('typeEditForm')">重 置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import {addType, deleteType, getType, updateType} from "@/api/moudles/system/type";

export default {
  name: "TypeManage",
  data() {
    return {
      searchKey: '',
      //新增或编辑字典对话框可见控制
      dialogVisible: false,
      currentPage: 1,
      total: 2,
      pageSize: 10,
      //字典类型表格数据
      tableData: [],
      selectOptions: [{value: 1}, {value: 2}, {value: 3}, {value: 4}, {value: 5}],
      // 表格中选中的对象数组
      multipleSelection: [],
      typeEditForm: {
        id: '',
        sortName: '',
        usful: ''
      },
      rules: {
        sortName: [
          {required: true, message: '请输入类型名称', trigger: 'blur'},
        ],
        usful: [
          {required: true, message: '请选择是否可用', trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    getBookTypes() {
      let params = {usful: 0, word: this.searchKey}
      getType(params).then(resp => {
        this.tableData = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    editTypeHandle(id) {
      this.tableData.forEach(type => {
        if (type.id == id) {
          this.typeEditForm = type
        }
      })
      this.dialogVisible = true
    },
    deleteType(id) {
      let params = {id}
      deleteType(params)
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dictypeEditForm = {}
      this.dicEditForm = {}
    },
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.typeEditForm.id) {
            updateType(this.typeEditForm).then(response => {
              this.$message.success(response.data.message)
              this.dialogVisible = false
            }, error => {
              this.$message.error(error)
            })
          } else {
            addType(this.typeEditForm).then(response => {
              this.$message.success(response.data.message)
              this.dialogVisible = false
            }, error => {
              this.$message.error(error)
            })
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
  },
  created() {
    this.getBookTypes()
  }
}
</script>

<style scoped>

</style>