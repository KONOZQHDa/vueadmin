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
        <el-button @click="">搜索</el-button>
      </el-form-item>

    </el-form>

    <!--    收藏书籍展示-->
    <el-row v-for="(bookRow,index) in rowArr" :key="index">
      <el-col :span="3" v-for="(book, index2) in bookRow" :key="index2" :offset="index2 > 0 ? 2 : 0">
        <div>
          <el-card :body-style="{ padding: '0px'}"
                   shadow="hover"
                   class="box-card">
            <img :src="book.imageUrl"
                 class="image">
            <div style="padding: 14px;">
              <div class="bookTitleDiv">
                <span style="font-size: 10px">{{ book.name }}</span>
                <span style="float:right"><el-tag type="info">{{
                    getSortNameBySortId(book.sortId)
                  }}</el-tag></span>
              </div>
              <br>
              <span style="color: palevioletred">￥{{ isDiscountBook(book.id) ? book.discountPrice : book.price }}</span><br>
              <span style="color: darkgray; font-size: 1px">收藏时价格:￥{{ book.addedPrice }}</span><br>
              <el-button type="text" @click="removeFavorite(book.id)">取消收藏</el-button>

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
          :total="favoritesData.length">
      </el-pagination>
    </div>


  </div>

</template>

<script>


import {removeFavorite} from "@/api/moudles/bookstore/favorites";

export default {
  name: "Favorites",
  data() {
    return {
      cartDialogVisible: false,
      cartEditForm: {
        bookId: '',
        num: null
      },
      rowArr: [],
      rowSize: 5,
      favoritesData: [],
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
  methods: {

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
    isDiscountBook(bookId) {
      return this.$store.state.book.discountBooks.some(discountBook => {
        return discountBook.id == bookId
      })
    },
    getSortNameBySortId(sortId) {
      var targetSort = {}
      this.$store.state.book.bookTypes.forEach(sort => {
        if (sort.id == sortId) {
          targetSort = sort
        }
      })
      return targetSort.sortName != null ? targetSort.sortName : '书籍类型被禁用中'
    },
    removeFavorite(bookId) {
      let params = {bookId}
      removeFavorite(params).then(resp => {
        this.$store.dispatch('favorites/GET_FAVORITES')
        location.reload()
      }, error => {
        this.$message.error(error)
      })
    },
    getFavorites() {
      this.favoritesData = this.$store.state.favorites.favorites
      this.bookRowsArr()
    },
    bookRowsArr() {
      this.rowArr = []
      let currentIndex = 0
      for (let i = 0; i < (this.favoritesData.length / 5) + 1; i++) {
        let arr = []
        for (let j = 0; j < this.rowSize; j++) {
          if (currentIndex < this.favoritesData.length) {
            arr.push(this.favoritesData[currentIndex++])
          }
        }
        this.rowArr.push(arr);
      }
    },
    //将请求获取到的Sort转换为级联选择器中的类型的数据
    transSortToCascader() {
      let arr = []
      this.$store.state.book.bookTypes.forEach(item => {
        let obj = {}
        obj.value = item.id
        obj.label = item.sortName
        arr.unshift(obj)
      })
      this.options[0].children = arr
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
  beforeMount() {
    // 在这里才请求收藏夹中的数据收藏页面会出错，因为请求数据还没回来，页面就开始渲染收藏夹等的内容，
    // 且一开始登录后进入首页，首页也需要用到该收藏夹数据
    // 在main.js里面请求又会因为没有登录导致请求出错出现报错信息
    // 最后通过在main.js中执行该action且在该dispat的回调中对请求失败情况不处理，投机感觉不靠谱，暂时没想到更好的办法
    // this.$store.dispatch('favorites/GET_FAVORITES'),
    this.getFavorites()
    this.transSortToCascader()
  }
}
</script>

<style scoped>


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
  width: 110%;
}

.bookTitleDiv {
  width: 100%;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
}

</style>