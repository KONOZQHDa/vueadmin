<template>
  <div>
    <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchKey" clearable placeholder="请输入字典名..."></el-input>
      </el-form-item>

      <el-form-item>
        <el-button v-if="hasAuthorization('sys:dic:list')" @click="searchDictype">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true"
                   v-if="hasAuthorization('sys:dic:save')">新增
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
          label="字典名称"
          align="center"
          prop="dictypeKey"
          width="180">
      </el-table-column>
      <el-table-column
          align="center"
          width="210px"
          prop="dictypeRemark"
          label="字典描述">
      </el-table-column>
      <el-table-column
          align="center"
          width="100px"
          prop="usfulStatus"
          label="状态">
        <template slot-scope="scope">
          <el-tag :type="formatByDiction(usfulStatusTagType,scope.row.usfulStatus)">
            {{ formatByDiction(glUsful, scope.row.usfulStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          width="260px"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editDictype(scope.row.id)"
                     v-if="hasAuthorization('sys:dic:save')">编辑
          </el-button>
          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:dic:manage')"></el-divider>

          <el-button type="text"
                     @click="manageDic(scope.row.dictypeKey,scope.row.id)"
                     v-if="hasAuthorization('sys:dic:manage')">管理字典
          </el-button>

          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:dic:delete')"></el-divider>

          <el-popconfirm
              title="确人删除吗？"
              @confirm="deleteDictype(scope.row.id)"
          >
            <el-button type="text" slot="reference"
                       v-if="hasAuthorization('sys:dic:delete')">删除
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
    <!--        点击新增或编辑(dictype)弹出的对话框-->
    <el-dialog
        title="字典类型信息"
        :visible.sync="dialogVisible"
        @close="resetForm('dictypeEditForm')"
        width="27%">
      <el-form :model="dictypeEditForm"
               ref="dictypeEditForm"
               :rules="rules"
               label-width="100px"
               class="demo-editForm">
        <el-form-item label="key" prop="dictypeKey">
          <el-input v-model="dictypeEditForm.dictypeKey"></el-input>
        </el-form-item>
        <el-form-item label="remark" prop="dictypeRemark">
          <el-input v-model="dictypeEditForm.dictypeRemark"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="usfulStatus">
          <el-radio-group v-model="dictypeEditForm.usfulStatus">
            <el-radio v-for="(item,index) in $store.state.diction.dictypes[glUsful]" :label="item.dicKey"
                      :key="index">{{ item.dicValue }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('dictypeEditForm')">确定</el-button>
          <el-button @click="resetForm('dictypeEditForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!--    编辑或新增字典某一键值对数据时弹出的对话框-->
    <el-dialog
        title="字典信息"
        :visible.sync="dialogVisible2"
        @close="resetForm('dicEditForm')"
        width="27%">
      <el-form :model="dicEditForm"
               :rules="dicRules"
               ref="dicEditForm"
               label-width="100px"
               class="demo-editForm">
        <el-form-item label="字典key" prop="dicKey">
          <el-input v-model="dicEditForm.dicKey"></el-input>
        </el-form-item>
        <el-form-item label="字典value" prop="dicValue">
          <el-input v-model="dicEditForm.dicValue"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('dicEditForm')">确定</el-button>
          <el-button @click="resetForm('dicEditForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!--    点击管理字典按钮弹出的对话框-->
    <el-dialog
        title="字典信息"
        :visible.sync="dialogVisible3"
        width="37%">
      <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="searchKey" clearable placeholder="请输入key..."></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="searchDiction">搜索</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="dialogVisible2 = true"
                     v-if="hasAuthorization('sys:role:save')">新增
          </el-button>
        </el-form-item>
      </el-form>
      <!--      字典键值对数据展示表格-->
      <el-table style="width: 100%"
                :data="dicTableData">
        <el-table-column
            align="center"
            width="150px"
            prop="dicKey"
            label="key">
        </el-table-column>
        <el-table-column
            align="center"
            width="150px"
            prop="dicValue"
            label="value">
        </el-table-column>
        <el-table-column
            align="center"
            width="220px"
            label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="editDiction(scope.row.id)"
                       v-if="hasAuthorization('sys:role:update')">编辑
            </el-button>

            <el-divider direction="vertical"
                        v-if="hasAuthorization('sys:role:delete')"></el-divider>

            <el-popconfirm
                title="确人删除吗？"
                @confirm="deleteDic(scope.row.id)"
            >
              <el-button type="text" slot="reference"
                         v-if="hasAuthorization('sys:role:delete')">删除
              </el-button>
            </el-popconfirm>

          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/network/request";

export default {
  name: "Dictionary",
  data() {
    return {
      searchKey: '',
      selectedRoleId: '',
      //新增或编辑字典对话框可见控制
      dialogVisible: false,
      //编辑或新增字典某一键值对数据时弹出的对话框可见控制
      dialogVisible2: false,
      //管理字典弹出对话框可见控制
      dialogVisible3: false,
      currentPage: 1,
      total: 2,
      pageSize: 10,
      //字典类型表格数据
      tableData: [],
      //字典数据类型表格数据
      dicTableData: [],
      selectOptions: [{value: 1}, {value: 2}, {value: 3}, {value: 4}, {value: 5}],
      // 表格中选中的对象数组
      multipleSelection: [],
      dictypeEditForm: {
        id: '',
        dictypeKey: '',
        dictypeRemark: '',
        usfulStatus: null
      },
      dicEditForm: {
        id: '',
        dicKey: '',
        dicValue: '',
        dictypeId: ''
      },
      rules: {
        dictypeKey: [
          {required: true, message: '请输入dictypeKey', trigger: 'blur'},
        ],
        dictypeRemark: [
          {required: true, message: '请输入dictypeRemark', trigger: 'blur'},
        ],
        usfulStatus: [
          {required: true, message: '请选择状态', trigger: 'blur'},
        ]
      },
      dicRules: {
        dicKey: [
          {required: true, message: '请输入dicKey', trigger: 'blur'},
        ],
        dicValue: [
          {required: true, message: '请输入dicValue', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    //编辑按钮click事件回调函数,只完成显示对话框及其数据回显
    editDictype(id) {
      this.tableData.forEach(item => {
        if (item.id === id) {
          this.dictypeEditForm = item
        }
      })
      this.dialogVisible = true
    },

    searchDictype() {
      if (this.searchKey) {
        request({
          method: 'get',
          url: 'http://localhost:8081/dictype/search',
          params: {
            word: this.searchKey
          }
        }).then(resp => {
          this.tableData = resp.data.data
        }, error => {
          this.$message.error(error)
        })
      }
    },
    searchDiction() {
      if (this.searchKey) {
        request({
          method: 'get',
          url: 'http://localhost:8081/diction/search',
          params: {
            word: this.searchKey
          }
        }).then(resp => {
          this.dicTableData = resp.data.data
        }, error => {
          this.$message.error(error)
        })
      }
    },
    manageDic(dictypeKey, dictypeId) {
      this.dicEditForm.dictypeId = dictypeId
      this.getDictions(dictypeKey)
      this.dialogVisible3 = true
    },
    editDiction(id) {
      this.dicTableData.forEach(item => {
        if (item.id == id) {
          this.dicEditForm = item
        }
      })
      this.dialogVisible2 = true
    },
    getDictypes() {
      request({
        method: 'get',
        url: 'http://localhost:8081/dictype/listDicType',
      }).then(resp => {
        this.tableData = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    getDictions(dictypeKey) {
      this.dicTableData = this.$store.state.diction.dictypes[dictypeKey]
    },
    deleteDictype(id) {
      if (id) {
        request({
          method: 'delete',
          url: 'http://localhost:8081/dictype/deleteDicType',
          params: {
            id
          }
        }).then(resp => {
          this.$message.success(resp.data.message)
          this.getDictypes()
        }, error => {
          this.$message.error(error)
        })
      }
    },
    deleteDic(id) {
      request({
        method: 'delete',
        url: 'http://localhost:8081/diction/deleteDic',
        params: {
          id
        }
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.$store.dispatch('diction/getDicTypes')
        this.getDictions()
        this.dialogVisible2 = false
        this.dialogVisible3 = false
      }, error => {
        this.$message.error(error)
      })
    },
    //分页组件自带函数
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
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
          //判断是字典类型表单还是字典表单
          if (formName === 'dictypeEditForm') {
            //表单中id有值则为编辑操作，若id没有值则为添加操作
            if (this.dictypeEditForm.id) {
              request({
                method: 'put',
                url: 'http://localhost:8081/dictype/updateDicType',
                data: this.dictypeEditForm
              }).then(resp => {
                this.$message.success(resp.data.message)
                this.getDictypes()
                this.dialogVisible = false
                this.resetForm(formName)
              }, error => {
                this.$message.error(error)
              })
            } else {
              request({
                method: 'post',
                url: 'http://localhost:8081/dictype/addDicType',
                data: this.dictypeEditForm
              }).then(resp => {
                this.$message.success(resp.data.message)
                this.getDictypes()
                this.resetForm(formName)
                this.dialogVisible = false
              }, error => {
                this.$message.error(error)
              })
            }
            //字典键值对表单
          } else {
            //表单中id有值则为编辑操作，若id没有值则为添加操作
            if (this.dicEditForm.id) {
              request({
                method: 'put',
                url: 'http://localhost:8081/diction/updateDic',
                data: this.dicEditForm
              }).then(resp => {
                this.$message.success(resp.data.message)
                this.$store.dispatch('diction/getDicTypes')
                this.getDictions()
                this.dialogVisible2 = false
                this.dialogVisible3 = false
                this.resetForm(formName)
              }, error => {
                this.$message.error(error)
              })
            } else {
              request({
                method: 'post',
                url: 'http://localhost:8081/diction/addDic',
                data: this.dicEditForm
              }).then(resp => {
                this.$message.success(resp.data.message)
                this.$store.dispatch('diction/getDicTypes')
                this.getDictions()
                this.resetForm(formName)
                this.dialogVisible2 = false
                this.dialogVisible3 = false
              }, error => {
                this.$message.error(error)
              })
            }
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
    this.getDictypes()
  }
}
</script>

<style scoped>

</style>