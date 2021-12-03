<template>
  <div>
    <!--        顶部操作栏，用行内表单这样就不用自己再调样式和间距了-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchParams.keyWords"

                  placeholder="请输入书籍名...">
        </el-input>
      </el-form-item>

      <!--      级联选择器选择书籍类别，价格区间-->
      <div class="block" style="display: inline;margin-right: 10px">
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
        <el-button @click="getBooks">搜索</el-button>
      </el-form-item>

    </el-form>

    <!--    书籍展示-->
    <el-row v-for="(bookRow,index) in rowArr" :key="index">
      <el-col :span="3" v-for="(book, index2) in bookRow" :key="index2" :offset="index2 > 0 ? 2 : 0">
        <div>
          <el-card :body-style="{ padding: '0px'}"
                   shadow="hover"
                   class="box-card">
            <img :src="book.imageUrl"
                 class="image">
            <div style="padding: 14px;">
              <span style="font-size: 10px">{{ book.name }}</span>
              <span style="float:right"><el-tag type="info">{{
                  getSortNameBySortId(book.sortId)
                }}</el-tag></span><br>
              <span style="color: palevioletred">￥{{ book.price }}</span>
              <div class="bottom clearfix">
                <el-button type="text" class="button">加入购物车</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!--    分页-->
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
  </div>

</template>

<script>

import request from "@/network/request";
import qs from "qs";

export default {
  name: "Index",
  data() {
    return {
      bookData: [],
      sorts: null,
      rowArr: [],
      rowSize: 5,
      total: 0,
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
      //查询书籍的筛选条件
      searchParams: {
        keyWords: "",
        sortIds: [],
        //价格区间，price[k,k+1]为一个区间，k为偶数
        price: [],
        pageNumber: 1,
        pageSize: 10
      },
    }
  },
  computed: {},
  methods: {
    getBooks() {
      request({
        method: 'get',
        url: 'http://localhost:8081/book/bookList',
        params: this.searchParams,
        paramsSerializer: params => {
          return qs.stringify(params, {indices: false})
        }
      }).then(resp => {
        this.bookData = resp.data.data.records
        this.total = resp.data.data.total
        this.bookRowsArr()
      }, error => {
        this.$message.error(error)
      })
    },
    getSorts() {
      request({
        method: 'get',
        url: 'http://localhost:8081/sort/listSort',
        params: {usful: 1}
      }).then(resp => {
        this.sorts = resp.data.data
        this.transSortToCascader()
      }, error => {
        this.$message.error(error)
      })
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
    bookRowsArr() {
      this.rowArr = []
      let currentIndex = 0
      for (let i = 0; i < (this.bookData.length / 5) + 1; i++) {
        let arr = []
        for (let j = 0; j < this.rowSize; j++) {
          if (currentIndex < this.bookData.length) {
            arr.push(this.bookData[currentIndex++])
          }
        }
        this.rowArr.push(arr);
      }
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
    getSortNameBySortId(sortId) {
      let targetSort
      this.sorts.forEach(sort => {
        if (sort.id == sortId) {
          targetSort = sort
        }
      })
      return targetSort.sortName
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
  },
  mounted() {
    this.getSorts()
    this.getBooks()
  }
}
</script>

<style scoped>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  height: 27vh;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 103%;
}
</style>