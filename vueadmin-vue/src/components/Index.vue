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
      <el-button type="primary" @click="showDiscountBook">特价书籍</el-button>

    </el-form>

    <!--    特价书籍展示抽屉-->
    <el-drawer
        :visible.sync="drawer"
        :with-header="false"
        direction="ttb"
        size="41%"
    >
      <!--      特价书籍轮播图-->
      <div class="block">
        <el-carousel type="card" height="280px">
          <el-carousel-item v-for="discountBook in pageDiscountBook" :key="discountBook.id">
            <div style="padding: 0;margin:0;height: 100%">
              <el-image
                  style="width: 34%; height: 100%;float: left"
                  :src="discountBook.imageUrl"
                  fit="contain"></el-image>
              <div style="float: left;">
                <div>特价时间：
                  <el-tag type="info">{{ discountBook.specialStartDateTime }}</el-tag>
                  -
                  <el-tag
                      type="info">{{ discountBook.specialEndDateTime }}
                  </el-tag>
                </div>
                <br>
                <div>
                  价格：
                  <span style="text-decoration:line-through;color: palevioletred">
                    ￥{{ discountBook.price }}
                  </span>&nbsp;
                  <span style="color: palevioletred">
                    {{ discountBook.discountPrice }}
                  </span>
                </div>

                <br>
                <!--                <div>-->
                <!--                  <el-tag type="warning">{{ getSortNameBySortId(discountBook.sortId) }}</el-tag>-->
                <!--                </div>-->
                <!--                收藏按钮-->
                <span style="float:left">
                  <!--                加入购物车按钮-->
                <el-button type="text" class="button" @click="addCartHandler(discountBook.id)">
                  <i class="el-icon-shopping-cart-2"></i>
                </el-button>
                </span>
                <span style="float: left">
                  <el-button v-if="!isFavorites(discountBook.id)" type="text" class="button"
                             @click="addCollection(discountBook,true)">
                    <i class="el-icon-star-off"></i>
                  </el-button>

                  <!--                取消收藏按钮-->
                  <el-button v-if="isFavorites(discountBook.id)" type="text" class="button"
                             @click="removeFavorite(discountBook.id)">
                    <i class="el-icon-star-on"></i>
                  </el-button>
                </span>
              </div>
              <el-col>
              </el-col>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div style="display: flex;flex-direction: row; margin: 0px auto;">
        <el-pagination
            :current-page="discountBookCurrentPage"
            @current-change="handleDiscountBookCurrentChange"
            :page-size="4"
            layout="total, prev, pager, next"
            :total="this.$store.state.book.discountBooks.length">
        </el-pagination>
      </div>
    </el-drawer>
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
                <!--                收藏按钮-->
                <el-button v-if="!isFavorites(book.id)" type="text" class="button" @click="addCollection(book,false)">
                  <i class="el-icon-star-off"></i>
                </el-button>

                <!--                取消收藏按钮-->
                <el-button v-if="isFavorites(book.id)" type="text" class="button" @click="removeFavorite(book.id)">
                  <i class="el-icon-star-on"></i>
                </el-button>

                <!--                加入购物车按钮-->
                <el-button type="text" class="button" @click="addCartHandler(book.id)">
                  <i class="el-icon-shopping-cart-2"></i>
                </el-button>

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

    <el-dialog
        title="加入购物车"
        :visible.sync="cartDialogVisible"
        @close="resetForm('cartEditForm')"
        width="30%">
      <el-form :model="cartEditForm" :rules="rules" ref="cartEditForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="购买数量" prop="num">
          <el-input v-model="cartEditForm.num"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('cartEditForm')">确认</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>
import {addFavorite, removeFavorite} from "@/api/moudles/bookstore/favorites";
import {getBook} from "@/api/moudles/system/book";
import {addCart} from "@/api/moudles/bookstore/cart";

export default {
  name: "Index",
  data() {
    return {
      bookData: [],
      cartDialogVisible: false,
      cartEditForm: {
        bookId: '',
        num: 1
      },
      rules: {
        num: [
          {required: true, message: '请输入加购数量', trigger: 'blur'},
        ],
      },
      rowArr: [],
      rowSize: 5,
      //选中待加入购物车的书
      addToCartBook: null,
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
      discountBookCurrentPage: 1,
      //查询书籍的筛选条件
      searchParams: {
        keyWords: "",
        sortIds: [],
        //价格区间，price[k,k+1]为一个区间，k为偶数
        price: [],
        pageNumber: 1,
        pageSize: 10,
      },
      //抽屉组件可见控制
      drawer: false,
    }
  },
  computed: {
    pageDiscountBook() {
      return this.$store.state.book.discountBooks.slice((this.discountBookCurrentPage - 1) * 4, (this.discountBookCurrentPage - 1) * 4 + 4)
    }
  },
  methods: {
    getBooks() {
      getBook(this.searchParams).then(resp => {
        this.bookData = resp.data.data.records
        this.total = resp.data.data.total
        this.bookRowsArr()
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
    removeFavorite(bookId) {
      let params = {bookId}
      removeFavorite(params).then(resp => {
        this.$store.dispatch('favorites/GET_FAVORITES')
      }, error => {
        this.$message.error(error)
      })
    },
    isFavorites(bookId) {
      let favorites = this.$store.state.favorites.favorites
      return favorites.some(favorite => {
        return favorite.id == bookId
      })
    },
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addCart()
        } else {
          this.$message({
            message: '内容不符合要求，请重新输入！',
            type: "warning"
          })
          return false;
        }
      });
    }
    ,
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.editForm = {}
    }
    ,
    showDiscountBook() {
      this.drawer = true
      this.$store.dispatch('book/GET_DISCOUNTBOOKS')
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
    }
    ,
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
    getSortNameBySortId(sortId) {
      let targetSort
      this.$store.state.book.bookTypes.forEach(sort => {
        if (sort.id == sortId) {
          targetSort = sort
        }
      })
      return targetSort.sortName
    }
    ,
    addCollection(book, isDiscount) {
      let data = {bookId: book.id, addedPrice: book.price}
      let params = {isDiscount}
      addFavorite(data, params).then(resp => {
        //重新获取收取的书籍到vuex
        this.$store.dispatch('favorites/GET_FAVORITES')
      }, error => {
        this.$message.error(error)
      })
    }
    ,
    addCartHandler(bookId) {
      this.cartEditForm.bookId = bookId
      this.cartDialogVisible = true
    },
    addCart() {
      let isDiscount = this.isDiscountBook(this.cartEditForm.bookId)

      addCart(this.cartEditForm, {isDiscount}).then(resp => {
        this.$message.success(resp.data.message)
        this.cartDialogVisible = false
      }, error => {
        this.$message.error(error)
      })
    }
    ,
    isDiscountBook(bookId) {
      return this.$store.state.book.discountBooks.some(discountBook => {
        return discountBook.id == bookId
      })
    },
    //分页组件自带函数
    handleSizeChange(val) {
      this.searchParams.pageSize = val
      this.getBooks()
    }
    ,
    handleCurrentChange(val) {
      this.searchParams.pageNumber = val
      this.getBooks()
    },
    handleDiscountBookCurrentChange(val) {
      this.discountBookCurrentPage = val
    }
  },
  mounted() {
    this.getBooks()
    this.transSortToCascader()
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
  line-height: 15px;
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

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: ghostwhite;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: ghostwhite;
}
</style>