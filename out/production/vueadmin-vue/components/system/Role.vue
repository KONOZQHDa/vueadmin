<template>
  <div>
    <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchKey" clearable placeholder="请输入角色名..."></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="search">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true"
                   v-if="hasAuthorization('sys:role:save')">新增
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm
            title="确人删除吗？"
            @confirm="deleteRoles"
        >
          <el-button type="danger"
                     slot="reference"
                     :disabled="deleteButtonDisabled"
                     v-if="hasAuthorization('sys:role:delete')">
            批量删除
          </el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <!--        角色信息展示表格-->
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
          label="名称"
          align="center"
          prop="name"
          width="120">
      </el-table-column>
      <el-table-column
          prop="code"
          align="center"
          label="唯一编码"
          width="150">
      </el-table-column>
      <el-table-column
          prop="remark"
          align="center"
          width="300px"
          label="描述">
      </el-table-column>
      <el-table-column
          align="center"
          width="90px"
          prop="state"
          label="状态">
        <template slot-scope="scope">
          <el-tag :type="formatByDiction(usfulStatusTagType,scope.row.state)"
          >{{ formatByDiction(glUsful, scope.row.state) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          width="260px"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="assignPermissions(scope.row.id)"
                     v-if="hasAuthorization('sys:role:perm')">管理权限
          </el-button>
          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:role:update')"></el-divider>

          <el-button type="text" @click="editRole(scope.row.id)"
                     v-if="hasAuthorization('sys:role:update')">编辑
          </el-button>
          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:role:delete')"></el-divider>

          <el-popconfirm
              title="确人删除吗？"
              @confirm="deleteRoles(scope.row.id)"
          >
            <el-button type="text" slot="reference"
                       v-if="hasAuthorization('sys:role:delete')">删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--        点击新增或编辑后弹出的对话框-->
    <el-dialog
        title="角色信息"
        :visible.sync="dialogVisible"
        @close="resetForm('editForm')"
        width="30%">
      <el-form :model="editForm" :rules="rules" ref="editForm" label-width="100px" class="demo-editForm">

        <el-form-item label="角色名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="唯一编码" prop="code">
          <el-input v-model="editForm.code"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input v-model="editForm.remark"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="editForm.state">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!--        分配权限按钮点击后显示的对话框-->
    <el-dialog
        title="分配权限"
        :visible.sync="dialogVisible2"
        @close=""
        width="30%">
      <el-row>
        <el-col :span="11">
          <el-tree
              :data="treeData"
              show-checkbox
              node-key="id"
              ref="tree"
              check-strictly
              :default-expand-all="true"
              :props="defaultProps">
          </el-tree>
        </el-col>

        <el-col :span="10" :offset="2">
          当前拥有的权限：<br><br>
          <el-tag type="info" v-for="(temp,index) in permissions" :key="index">{{ temp }}</el-tag>
        </el-col>
      </el-row>

      <span slot="footer" class="dialog-footer" style="display: flex;flex-direction: row">
                <el-button type="primary" @click="" size="mini" @click="assignPermissionsHandle">分 配</el-button>
                <el-button type="danger" @click="" size="mini" @click="removePermissionHandle">删 除</el-button>
            </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  addRole,
  assignPermmission,
  deletePermmission,
  deleteRole,
  getPermissions,
  getRoles,
  searchRole,
  updateRole
} from "@/api/moudles/sys_role";
import {getMunus} from "@/api/moudles/sys_menu";

export default {
  name: "Role",
  data() {
    return {
      searchKey: '',
      selectedRoleId: '',
      dialogVisible: false,
      dialogVisible2: false,
      currentPage: 1,
      total: 2,
      pageSize: 10,
      tableData: [],
      permissions: [],
      // 表格中选中的对象数组
      multipleSelection: [],
      editForm: {
        id: '',
        name: '',
        code: '',
        remark: '',
        state: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入角色名称', trigger: 'blur'},
        ],
        code: [
          {required: true, message: '请输入角色唯一编码', trigger: 'blur'},
        ],
        state: [
          {required: true, message: '请输入角色状态', trigger: 'blur'},
        ],
        remark: [
          {required: true, message: '请输入角色描述', trigger: 'blur'},
        ],
      },
      // 树形控件的数据
      treeData: [],
      //树形控件配置对象中作为子树的属性的名字，还有哪个属性作为标签显示
      defaultProps: {
        children: 'children',
        label: 'name'
      },
    }
  },
  methods: {
    //编辑按钮click事件回调函数,只完成显示对话框及其数据回显
    editRole(id) {
      this.tableData.forEach(item => {
        if (item.id === id) {
          this.editForm = item
        }
      })
      this.dialogVisible = true
      this.editForm.id = id
    },
    search() {
      if (this.searchKey) {
        let params = {
          word: this.searchKey
        }
        searchRole(params).then(resp => {
          this.tableData = resp.data.data
        }, error => {
          this.$message.error(error)
        })
      }
    },

    deleteRoles(id) {
      //删除一个
      if (id) {
        let params = {
          ids: id
        }
        deleteRole(params).then(resp => {
          this.$message.success(resp.data.message)
          this.getRoles()
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
        deleteRole(params).then(resp => {
          this.$message.success(resp.data.message)
          this.getRoles()
        }, error => {
          this.$message.error(error)
        })
      }
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
            updateRole(this.editForm).then(resp => {
              this.$message.success(resp.data.message)
              this.getRoles()
              this.dialogVisible = false
              this.resetForm(formName)
            }, error => {
              this.$message.error(error)
            })
          } else {
            addRole(this.editForm).then(resp => {
              this.$message.success(resp.data.message)
              this.getRoles()
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
    getRoles() {
      getRoles().then(resp => {
        this.tableData = resp.data.data
        this.total = this.tableData.length
      }, error => {
        this.$message.error(error)
      })
    },
    getTreeData() {
      getMunus().then(resp => {
        this.treeData = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    //分配权限click事件回调函数,只完成显示对话框及其数据回显，真正的是表单提交assignPermissionsHandle方法响应到后端
    assignPermissions(roleId) {
      this.dialogVisible2 = true
      this.$nextTick(() => {
        this.getRolePermissions(roleId)
      })
      //将改行的角色设置为被选中
      this.selectedRoleId = roleId
    },
    getRolePermissions(roleId) {
      let params = {
        roleId
      }
      getPermissions(params).then(resp => {
        this.permissions = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    //发送请求进行给角色分配权限
    assignPermissionsHandle() {
      //拿到树形控件中选中项的id值数组
      let menuIds = this.$refs.tree.getCheckedKeys();
      let params = {
        roleId: this.selectedRoleId,
        menuIds
      }
      assignPermmission(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getRolePermissions(this.selectedRoleId)
        this.$refs.tree.setCheckedKeys([])
        
      }, error => {
        this.$message.error(error)
      })
    },
    removePermissionHandle() {
      //拿到树形控件中选中项的id值数组
      let menuIds = this.$refs.tree.getCheckedKeys();
      let params = {
        roleId: this.selectedRoleId,
        menuIds
      }
      deletePermmission(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getRolePermissions(this.selectedRoleId)
        this.$refs.tree.setCheckedKeys([])
      }, error => {
        this.$message.error(error)
      })
    }
  },
  computed: {
    deleteButtonDisabled() {
      return this.multipleSelection.length == 0 ? true : false
    }
  },
  created() {
    this.getRoles()
    this.getTreeData()
  }
}
</script>

<style scoped>

</style>