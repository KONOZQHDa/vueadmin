<template>
  <div>
    <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchKey" clearable placeholder="请输入用户名..."></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="search()">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true"
                   v-if="hasAuthorization('sys:user:save')">新增
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm
            title="确人删除吗？"
            @confirm="deleteUsers"
        >
          <el-button type="danger" slot="reference" :disabled="deleteButtonDisabled"
                     v-if="hasAuthorization('sys:user:delete')">批量删除
          </el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <!--        用户信息展示表格-->
    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange">
      <el-table-column
          align="center"
          type="selection"
          width="50">
      </el-table-column>
      <el-table-column
          label="头像"
          align="center"
          prop="avatar"
          width="80">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column
          prop="username"
          align="center"
          label="用户名"
          width="120">
      </el-table-column>
      <el-table-column
          prop="users"
          align="center"
          width="210px"
          label="角色名称">
        <template slot-scope="scope">
          <el-tag type="info"
                  style="margin-right: 7px"
                  v-for="role in scope.row.roles">
            {{ role }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="email"
          align="center"
          width="210px"
          label="邮箱">
      </el-table-column>
      <el-table-column
          align="center"
          width="90px"
          label="状态">
        <template slot-scope="scope">
          <el-tag :type="formatByDiction(usfulStatusTagType,scope.row.state)"
          >{{ formatByDiction(glUsful, scope.row.state) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="created"
          align="center"
          width="245px"
          label="创建时间">
      </el-table-column>
      <el-table-column
          align="center"
          width="300px"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="assignRolesHandle(scope.row.id)"
                     v-if="hasAuthorization('sys:user:role')">分配角色
          </el-button>
          <el-divider direction="vertical" v-if="hasAuthorization('sys:user:role')"></el-divider>
          <el-popconfirm
              title="确认重置密码吗？"
              @confirm="resetPassword(scope.row.id)"
          >
            <el-button type="text" slot="reference"
                       v-if="hasAuthorization('sys:user:repass')">重置密码
            </el-button>
          </el-popconfirm>

          <el-divider direction="vertical" v-if="hasAuthorization('sys:user:repass')"></el-divider>

          <el-button type="text" @click="handleEditUser(scope.row.id)"
                     v-if="hasAuthorization('sys:user:update')">编辑
          </el-button>
          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:user:delete')"></el-divider>

          <el-popconfirm
              title="确人删除吗？"
              @confirm="deleteUsers(scope.row.id)"
          >
            <el-button type="text" slot="reference"
                       v-if="hasAuthorization('sys:user:delete')">删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--        分页-->
    <div style="display: flex;flex-direction: row; margin-top: 18px;">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <!--        点击新增或编辑后弹出的对话框-->
    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="35%"
        :before-close="close">

      <el-form :model="editForm" :rules="rules" ref="editForm">
        <el-form-item label="用户名" prop="userName" label-width="100px">
          <el-input v-model="editForm.username" autocomplete="off"></el-input>
          <el-alert v-if="!editForm.id"
                    title="初始密码为123456"
                    :closable="false"
                    type="info"
                    style="line-height: 12px;"
          ></el-alert>
        </el-form-item>

        <el-form-item label="密码" prop="password" label-width="100px" v-if="editForm.id">
          <el-input v-model="editForm.password" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="email" label-width="100px">
          <el-input v-model="editForm.email" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="state" label-width="100px">
          <el-radio-group v-model="editForm.state">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item style="display: flex; flex-direction: row; justify-content: space-around">
          <el-button @click="resetForm('editForm')">重 置</el-button>
          <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!--        分配角色弹出对话框-->
    <el-dialog
        title="分配角色"
        :visible.sync="rolesDialogVisible"
        width="25%"
    >
      <div style="margin-bottom: 13px">
        <el-tree
            :data="roles"
            show-checkbox
            node-key="id"
            :props="{label:'name'}"
            ref="roleTree"
        >
        </el-tree>
      </div>

      <el-button type="primary" @click="assignRoles" size="mini">分 配</el-button>
      <el-button type="danger" @click="removeRoles" size="mini">删 除</el-button>
    </el-dialog>

  </div>
</template>

<script>
import {
  addUser,
  assignRoles,
  deleteUser,
  getUsers,
  removeRole,
  resetPassword,
  searchUsers,
  updateUser
} from "@/api/moudles/system/sys_user";
import {getRoles} from "@/api/moudles/system/sys_role";

export default {
  name: "user",
  data() {
    return {
      searchKey: '',
      dialogVisible: false,
      rolesDialogVisible: false,
      currentPage: 1,
      total: null,
      pageSize: 10,
      tableData: [],
      // 表格中选中的对象数组
      multipleSelection: [],
      editForm: {
        username: '',
        state: '',
        id: '',
        email: '',
        password: ''
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名称', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入用户邮箱', trigger: 'blur'},
        ],
        state: [
          {required: true, message: '请选择用户状态', trigger: 'blur'},
        ]
      },
      roles: [],
      userIdToAssignRole: null
    }
  },
  methods: {
    // 搜索功能
    search() {
      if (this.searchKey) {
        let params = {
          word: this.searchKey
        }
        searchUsers(params).then(resp => {
          this.tableData = resp.data.data.users
        }, error => {
          this.$message.error(error)
        })
      }
    },
    //点击编辑用户按钮，完成编辑用户提交的前置工作
    handleEditUser(id) {
      this.dialogVisible = true
      //实现编辑用户时的数据回显
      this.tableData.forEach(item => {
        if (item.id == id) {
          this.editForm = item
          this.editForm.password = ''
        }
      })
    },
    //删除或批量删除
    deleteUsers(id) {
      let params = {
        ids: id
      }
      //删除一个
      if (id) {
        deleteUser(params).then(resp => {
          this.$message.success(resp.data.message)
          this.getUsers()
        }, error => {
          this.$message.error(error)
        })
      }
      //批量删除
      else {
        let ids = []
        this.multipleSelection.forEach(item => {
          ids.unshift(item.id)
        })
        let params = {
          ids: ids
        }
        deleteUser(params).then(resp => {
          this.$message.success(resp.data.message)
          this.getUsers()
        }, error => {
          this.$message.error(error)
        })
      }
    },
    //重置密码
    resetPassword(id) {
      let params = {
        id: id
      }
      resetPassword(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getUsers()
      }, error => {
        this.$message.error(error)
      })
    },
    //点击分配角色按钮，完成分配角色提交的前置工作
    assignRolesHandle(userId) {
      this.getRoles()
      this.rolesDialogVisible = true
      this.userIdToAssignRole = userId
    },
    getRoles() {
      getRoles().then(resp => {
        this.roles = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    //发送请求完成分配角色
    assignRoles() {
      let params = {
        userId: this.userIdToAssignRole,
        rolesId: this.$refs.roleTree.getCheckedKeys()
      }
      assignRoles(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getUsers()
      }, error => {
        this.$message.error(error)
      })
      this.rolesDialogVisible = false
    },
    removeRoles() {
      let params = {
        userId: this.userIdToAssignRole,
        rolesId: this.$refs.roleTree.getCheckedKeys()
      }
      removeRole(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getUsers()
      }, error => {
        this.$message.error(error)
      })
      this.rolesDialogVisible = false
    },
    //以下为组件自带函数：
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    //表格自带方法，勾选内容改变后回调
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //分页组件自带函数
    handleSizeChange(val) {
    },
    handleCurrentChange(val) {
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.editForm = {}
    },
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //表单中id有值则为编辑用户操作，若id没有值则为添加用户操作
          if (this.editForm.id) {
            updateUser(this.editForm).then(resp => {
              this.$message.success(resp.data.message)
              this.getUsers()
              this.dialogVisible = false
              this.resetForm(formName)
            }, error => {
              this.$message.error(error)
            })
          } else {
            addUser(this.editForm).then(resp => {
              this.$message.success(resp.data.message)
              this.getUsers()
              this.resetForm(formName)
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
    getUsers() {
      getUsers().then(resp => {
        this.tableData = resp.data.data.users
        this.total = resp.data.data.totalCount
      }, error => {
        this.$message.error(error)
      })
    },
    close() {
      this.resetForm("editForm")
      this.dialogVisible = false
    }
  },
  computed: {
    deleteButtonDisabled() {
      return this.multipleSelection.length == 0 ? true : false
    }
  },
  created() {
    this.getUsers("editForm")
  }
}
</script>

<style scoped>

</style>