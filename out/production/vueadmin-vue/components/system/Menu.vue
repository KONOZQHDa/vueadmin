<template>
  <div>
    <!--        新增按钮，用这种行内表单中自带的按钮是因为使用它就不用自己再去调按钮的样式和间距之类了，本身独占一行-->
    <el-form :inline="true" class="demo-form-inline" v-if="hasAuthorization('sys:menu:save')">
      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true">新增</el-button>
      </el-form-item>
    </el-form>
    <!--        菜单管理的表格-->
    <el-table
        :data="tableData"
        style="width: 100%;margin-bottom: 20px;"
        row-key="id"
        border
        stripe
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column
          prop="name"
          label="名称"
          sortable
          align="center"
          width="170">
      </el-table-column>
      <el-table-column
          prop="perms"
          label="权限编码"
          align="center"
          width="160">
      </el-table-column>
      <el-table-column
          prop="type"
          width="90px"
          align="center"
          label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" type="info">目录</el-tag>
          <el-tag v-if="scope.row.type === 1">菜单</el-tag>
          <el-tag v-if="scope.row.type === 2" type="success">按钮</el-tag>

        </template>
      </el-table-column>
      <el-table-column
          prop="path"
          label="菜单URL"
          align="center"
          width="150px">
      </el-table-column>
      <el-table-column
          prop="component"
          label="菜单组件"
          align="center"
          width="160">
      </el-table-column>
      <el-table-column
          prop="routeName"
          label="路由名称"
          align="center"
          width="160">
      </el-table-column>
      <el-table-column
          prop="state"
          width="90px"
          align="center"
          label="状态">
        <template slot-scope="scope">
          <el-tag :type="formatByDiction(usfulStatusTagType,scope.row.state)"
          >{{ formatByDiction(glUsful, scope.row.state) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="created"
          width="160px"
          align="center"
          label="创建时间">
      </el-table-column>
      <el-table-column
          prop="updated"
          width="160px"
          align="center"
          label="更新时间">
      </el-table-column>
      <el-table-column
          prop="operation"
          width="150px"
          align="center"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editMenuHandle(scope.row.id)"
                     v-if="hasAuthorization('sys:menu:update')">编辑
          </el-button>

          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:menu:delete')"></el-divider>

          <template>
            <el-popconfirm
                title="确人删除吗？"
                @confirm="deleteMenu(scope.row.id)"
            >
              <el-button type="text" slot="reference"
                         v-if="hasAuthorization('sys:menu:delete')">删除
              </el-button>
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <!--        点击新增按钮显示表单内容的对话框-->
    <el-dialog
        title="菜单信息"
        :visible.sync="dialogVisible"
        @close="resetForm('editForm')"
        width="35%">
      <el-form :model="editForm" :rules="rules" ref="editForm" label-width="100px" class="demo-editForm">
        <el-form-item label="上级菜单" prop="parentMenuId">
          <el-select v-model="editForm.parentId" placeholder="请选择上级菜单">
            <template v-if="item.type==0" v-for="item in tableData">
              <el-option :label="item.name" :value="item.id"></el-option>
              <el-option v-for="i in item.children" v-if="i.type==1" :label="i.name" :value="i.id">
                <span>-{{ i.name }}</span>
              </el-option>
            </template>
          </el-select>
        </el-form-item>

        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="权限编码" prop="perms">
          <el-input v-model="editForm.perms"></el-input>
        </el-form-item>
        <el-form-item label="菜单URL" prop="path">
          <el-input v-model="editForm.path"></el-input>
        </el-form-item>
        <el-form-item label="路由名称" prop="routeName">
          <el-input v-model="editForm.routeName"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="editForm.icon"></el-input>
        </el-form-item>
        <el-form-item label="菜单组件" prop="component">
          <el-input v-model="editForm.component"></el-input>
        </el-form-item>


        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="editForm.type">
            <el-radio :label="0">目录</el-radio>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="editForm.state">
            <el-radio v-for="(item,index) in $store.state.diction.dictypes[glUsful]" :label="item.dicKey"
                      :key="index">{{ item.dicValue }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button v-if="!editForm.id" type="primary" @click="submitForm('editForm')">立即创建</el-button>
          <el-button v-if="editForm.id" type="primary" @click="submitForm('editForm')">更 新</el-button>
          <el-button @click="resetForm('editForm')">重 置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {addMenu, deleteMenu, getMunus, updateMenu} from "@/api/moudles/system/sys_menu";

export default {
  name: "Menu",
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      editForm: {
        id: '',
        parentId: '',
        name: '',
        path: '',
        component: '',
        state: '',
        perms: '',
        type: '',
        routeName: '',
        icon: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'},
          {min: 1, max: 8, message: '长度在 1 到 8 个字符', trigger: 'blur'}
        ],
        type: [
          {required: true, message: '请选择菜单类型', trigger: 'change'}
        ],
        state: [
          {required: true, message: '请选择菜单状态', trigger: 'change'}
        ],
        path: [],
        component: [],
        perms: []
      }

    }
  },
  watch: {
    //新增或编辑菜单时，根据所选的类型不同，则表单中对应的必填项不同，对应修改表单rules
    'editForm.type': {
      handler(newValue, oldValue) {
        if (newValue == 0 || newValue == '') {
          if (oldValue == 1) {
            this.rules.path = []
            this.rules.component = []
          } else if (oldValue == 2) {
            this.rules.perms = []
          }
        } else if (newValue == 1) {
          //如果原来选的是按钮，那么将验证规则中的权限编码项去掉
          if (oldValue == 2) {
            this.rules.perms = []
          }

          //新增的是菜单，则url和组件必填
          this.rules.path = [
            {required: true, message: '请输入菜单URL', trigger: 'blur'},
          ]

          this.rules.component = [
            {required: true, message: '请输入菜单组件', trigger: 'blur'},
          ]
          //新增的是按钮，则权限编码必填
        } else if (newValue == 2) {
          if (oldValue == 1) {
            this.rules.path = []
            this.rules.component = []
          }

          this.rules.perms = [
            {required: true, message: '请选择权限编码', trigger: 'blur'}
          ]
        }
      }
    }
  },
  methods: {
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.editForm.id) {
            this.addMenu()
          } else {
            this.editMenu()
          }
        } else {
          console.log('表单内容不符合规范，请重新输入！');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.editForm = {}
      this.rules.path = []
      this.rules.component = []
      this.rules.perms = []
    },
    getTableData() {
      getMunus().then(Response => {
        this.tableData = Response.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    //菜单编辑处理，只是显示编辑表单并实现数据回显，后调用
    editMenuHandle(id) {

      let targetMenu
      let parentMenuId
      this.tableData.forEach(item => {
        if (item.id == id) {
          targetMenu = item
        } else {
          if (item.hasOwnProperty('children')) {
            item.children.forEach(e => {
              if (e.id == id) {
                targetMenu = e
                parentMenuId = item.id
              } else {
                if (e.hasOwnProperty('children')) {
                  e.children.forEach(i => {
                    if (i.id == id) {
                      targetMenu = i
                      parentMenuId = e.id
                    }
                  })
                }
              }
            })
          }
        }
      })

      this.editForm = targetMenu
      this.dialogVisible = true
    },
    //完成菜单修改
    editMenu() {
      delete this.editForm.children

      updateMenu(this.editForm).then(resp => {
        this.$message.success(resp.data.message)
        this.getTableData()
        this.dialogVisible = false
      }, error => {
        this.$message.error(error)
      })

    },
    //实现新增菜单
    addMenu() {
      addMenu(this.editForm).then(resp => {
        this.$message.success(resp.data.message)
        this.getTableData()
        this.dialogVisible = false
      }, error => {
        this.$message.error(error)
      })
    },
    //删除菜单
    deleteMenu(id) {
      let params = {id}
      deleteMenu(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getTableData()
      }, error => {
        this.$message.error(error)
      })
    }
  },
  created() {
    this.getTableData()
  }
}
</script>

<style scoped>

</style>