<template>
  <div>
    <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchParams.keyWords"
                  v-if="hasAuthorization('sys:book:list')"
                  placeholder="请输入书籍名...">
        </el-input>
      </el-form-item>

      <!--      级联选择器选择书籍类别，价格区间-->
      <div class="block" style="display: inline;margin-right: 10px" v-if="hasAuthorization('sys:book:list')">
        <el-cascader
            :options="options"
            v-model="cascaderValue"
            :props="props"
            collapse-tags
            placeholder="请选择筛选条件..."
            @change="updateSearchParams"
            clearable></el-cascader>
      </div>

      <el-form-item>
        <el-button v-if="hasAuthorization('sys:book:list')" @click="getBooks">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true"
                   v-if="hasAuthorization('sys:book:save')">新增
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm
            title="确人删除吗？"
            @confirm="deleteBooks"
        >
          <el-button type="danger" slot="reference" :disabled="deleteButtonDisabled"
                     v-if="hasAuthorization('sys:book:delete')">批量删除
          </el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <!--    书籍表格-->
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
          label="书名"
          align="center"
          prop="name"
          width="210">
      </el-table-column>
      <el-table-column
          align="center"
          label="封面"
          width="150">
        <template slot-scope="scope">
          <el-image
              style="width: 80px; height: 80px"
              :src="scope.row.imageUrl"
              fit="cover"></el-image>
        </template>
      </el-table-column>
      <el-table-column
          prop="price"
          align="center"
          width="90px"
          label="价格">
      </el-table-column>
      <el-table-column
          prop="discountPrice"
          align="center"
          width="90px"
          label="折扣价">
      </el-table-column>
      <el-table-column
          prop="stock"
          align="center"
          width="90px"
          label="库存">
      </el-table-column>
      <el-table-column
          prop="sort"
          align="center"
          width="180px"
          label="书籍类型">
        <template slot-scope="scope">
          <el-tag type="info">{{ getSortNameBySortId(scope.row.sortId) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="createrName"
          align="center"
          width="110px"
          label="创建者">
      </el-table-column>
      <el-table-column
          align="center"
          width="320px"
          prop="specialTime"
          label="特价时间">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.specialStartDateTime">{{ scope.row.specialStartDateTime }}</el-tag>
          -
          <el-tag type="info" v-if="scope.row.specialStartDateTime">{{ scope.row.specialStartDateTime }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          width="90px"
          label="状态">
        <template slot-scope="scope">
          <el-tag :type="formatByDiction(bookStatusTagType,scope.row.goodStatus)"
          >{{ formatByDiction(bookStatus, scope.row.goodStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          width="260px"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editBook(scope.row.id)"
                     v-if="hasAuthorization('sys:book:update')">编辑
          </el-button>

          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:book:update')"></el-divider>

          <el-button type="text" @click="changeBookImgHandler(scope.row.id)"
                     v-if="hasAuthorization('sys:book:update')">更换封面
          </el-button>

          <el-divider direction="vertical"
                      v-if="hasAuthorization('sys:book:delete')"></el-divider>

          <el-popconfirm
              title="确人删除吗？"
              @confirm="deleteBooks(scope.row.id)"
          >
            <el-button type="text" slot="reference"
                       v-if="hasAuthorization('sys:book:delete')">删除
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
          :current-page="searchParams.pageNumber"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="searchParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>


    <!--    新增书籍或编辑按钮对话框-->
    <el-dialog
        title="新增书籍"
        :visible.sync="dialogVisible"
        @close="resetForm('editForm')"
        width="30%">
      <el-form :model="editForm" :rules="rules" ref="editForm" label-width="100px" class="demo-editForm">

        <el-form-item label="书名" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="editForm.price"></el-input>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="editForm.stock"></el-input>
        </el-form-item>
        <el-form-item label="类别" prop="sortId">
          <el-select v-model="editForm.sortId" placeholder="请选择类别...">
            <el-option
                v-for="item in sorts"
                :key="item.id"
                :label="item.sortName"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="goodStatus">
          <el-radio-group v-model="editForm.goodStatus">
            <el-radio v-for="(item,index) in $store.state.diction.dictypes[bookStatus]" :label="item.dicKey"
                      :key="index">{{ item.dicValue }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="特价时间">
          <el-date-picker
              value-format="yyyy-MM-dd HH:mm:ss"
              v-model="specialDateTime"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="特价" prop="discountPrice">
          <el-input v-model="editForm.discountPrice"></el-input>
        </el-form-item>
        <el-form-item label="图片" v-if="!editForm.id">
          <el-upload
              ref="upload"
              class="upload-demo"
              name="imgs"
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
          <el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--    点击更换封面的对话框-->

    <el-dialog
        title="更换封面"
        :visible.sync="dialogVisible2"
        @close="resetForm('editForm')"
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

  </div>
</template>

<script>
import {addBook, deleteBooks, getBook, getSorts, updateBook, upImg} from "@/api/moudles/system/book";

export default {
  name: "BookManage",
  data() {
    return {
      //查询书籍的筛选条件
      searchParams: {
        keyWords: '',
        sortIds: [],
        //价格区间，price[k,k+1]为一个区间，k为偶数
        price: [],
        pageNumber: 1,
        pageSize: 10
      },
      //书籍类别
      sorts: [],
      //新增或编辑书籍对话框可见性控制
      dialogVisible: false,
      //更换封面对话框可见性控制
      dialogVisible2: false,
      formNameToReset: '',
      multipleSelection: [],
      tableData: [],
      total: 0,
      specialDateTime: [],
      editForm: {
        id: '',
        name: '',
        price: '',
        discountPrice: '',
        stock: '',
        sortId: '',
        imageUrl: '',
        specialStartDateTime: '',
        specialEndDateTime: '',
        goodStatus: null
      },
      rules: {
        name: [
          {required: true, message: '请输入书籍名称', trigger: 'blur'},
        ],
        price: [
          {required: true, message: '请输入书籍价格', trigger: 'blur'},
        ],
        stock: [
          {required: true, message: '请输入书籍库存', trigger: 'blur'},
        ],
        sortId: [
          {required: true, message: '请选择书籍类别', trigger: 'blur'},
        ],
        goodStatus: [
          {required: true, message: '请选择书籍状态', trigger: 'blur'},
        ]
      },
      //书籍封面
      fileList: [],
      //级联选择器数据
      cascaderValue: [],
      props: {multiple: true},
      //级联选择器的值
      options: [{
        value: 'sort',
        label: '类型',
        children: []
      }, {
        value: 'price',
        label: '价格区间',
        children: [{
          value: [0, 50],
          label: '0到50',
        }, {
          value: [50, 100],
          label: '50到100',
        }, {
          value: [100, 150],
          label: '100到150'
        }, {
          value: [150, 200],
          label: '150到200'
        }, {
          value: [200, 100000000],
          label: '200以上'
        }]
      }],
      picFormData: ''
    }
  },
  computed: {
    deleteButtonDisabled() {
      return this.multipleSelection.length == 0 ? true : false
    },
  },
  methods: {
    getSorts() {
      let params = {usful: 1}
      getSorts(params).then(resp => {
        this.sorts = resp.data.data
        this.transSortToCascader()
      }, error => {
        this.$message.error(error)
      })
    },
    //将请求获取到的Sort转换为级联选择器中的类型的数据
    transSortToCascader() {
      let arr = []
      this.sorts.forEach(item => {
        let obj = {}
        obj.value = item.id
        obj.label = item.sortName
        arr.unshift(obj)
      })
      this.options[0].children = arr
    },
    //将级联选择器中选中的条件转换到查询条件对象searchParams对象
    updateSearchParams(checkedNodes) {
      this.searchParams.sortIds = []
      this.searchParams.price = []
      checkedNodes.forEach(checkedNode => {
        if (checkedNode[0] == 'sort') {
          this.searchParams.sortIds.unshift(checkedNode[1])
        }
        if (checkedNode[0] == 'price') {
          this.searchParams.price = this.searchParams.price.concat(checkedNode[1])
        }
      })
    },
    getSortNameBySortId(sortId) {
      let targetSort
      this.sorts.forEach(sort => {
        if (sort.id == sortId) {
          targetSort = sort
        }
      })
      return targetSort.sortName
    },
    deleteBooks(id) {
      let ids = []
      if (id) {
        ids.unshift(id)
      }
      this.multipleSelection.forEach(item => {
        ids.unshift(item.id)
      })
      let params = {ids: ids}
      deleteBooks(params).then(resp => {
        this.$message.success(resp.data.message)
        this.getBooks()
      }, error => {
        this.$message.error(error)
      })
    },
    //完成点击编辑图书时的数据回显
    editBook(id) {
      this.tableData.forEach(book => {
        if (book.id == id) {
          this.editForm = book
          this.specialDateTime[0] = book.specialStartDateTime
          this.specialDateTime[1] = book.specialEndDateTime
        }
      })
      this.dialogVisible = true
    },
    getBooks() {
      getBook(this.searchParams).then(resp => {
        this.tableData = resp.data.data.records
        this.total = resp.data.data.total
      }, error => {
        this.$message.error(error)
      })
    },
    //将elementui时间选择器选中的时间转换为表单数据中需要的格式
    formatSpecialDataTime() {
      this.editForm.specialStartDateTime = this.specialDateTime[0]
      this.editForm.specialEndDateTime = this.specialDateTime[1]
    },
    addBook() {
      this.formatSpecialDataTime()
      addBook(this.editForm).then(resp => {
        this.$message.success(resp.data.message)
        this.getBooks()
        this.dialogVisible = false
        this.resetForm(this.formNameToReset)
        this.specialDateTime = []
        this.fileList = []
      }, error => {
        this.$message.error(error)
      })
    },
    updateBook() {
      updateBook(this.editForm).then(resp => {
        this.$message.success(resp.data.message)
        this.getBooks()
        this.resetForm(this.formNameToReset)
        this.dialogVisible = false
        this.dialogVisible2 = false
        this.specialDateTime = []
      }, error => {
        this.$message.error(error)
      })
    },
    changeBookImgHandler(id) {
      this.dialogVisible2 = true
      this.editBook(id)
      this.dialogVisible = false
    },
    //调用elementui上传组件的submit方法会后，其会遍历文件数组并调用该方法
    upFile(params) {
      this.picFormData.append('imgs', params.file)
    },
    subPicForm() {
      this.picFormData = new FormData()
      this.$refs.upload.submit()
      upImg(this.picFormData).then(resp => {
        //成功上次图片后保存返回的图片资源路径
        this.editForm.imageUrl = resp.data.data;
        if (this.editForm.id) {
          this.updateBook()
        } else {
          this.addBook()
        }
      }, error => {
        this.$message.error(error.message);
      })

      //上传完成后重置存放图片的数据表单
      this.picFormData = null
    },
    //以下为组件自带函数

    //封面上传成功后，将返回的图片源保存
    handleUploadSuccess(response) {
      this.editForm.imageUrl = response.data.data;
      this.addBook()
    },
    //封面上传失败
    handleUploadError(err) {
      this.$message.error(err.message);
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.editForm = {}
    },
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.formNameToReset = formName
          //更新书籍信息
          if (this.editForm.id) {
            this.updateBook()
            //新增书籍
          } else {
            //先完成书籍封面上传，上传成功后将返回图片的资源路径保存到editForm中,并在回调函数中完成新增书籍或更新书籍的请求
            this.subPicForm()
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
    //表格自带方法，勾选内容改变后回调
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //分页组件自带函数
    handleSizeChange(val) {
      this.searchParams.pageSize = val
      this.getBooks()
    },
    handleCurrentChange(val) {
      this.searchParams.pageNumber = val
      this.getBooks()
    },
    handleRemove(file, fileList) {
      console.log(file);
      console.log("fileList: ", fileList)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },

  },
  mounted() {
    this.getBooks();
    this.getSorts()
  }
}
</script>

<style scoped>

</style>