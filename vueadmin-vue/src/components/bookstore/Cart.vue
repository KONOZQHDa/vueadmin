<template>
  <div>
    <div style="display: inline" v-if="checkedCartItemIds.length">已选<span
        style="color: palevioletred;margin: 0 3px;">{{ checkedCartItemIds.length }}</span>件
    </div>
    <div style="display: inline; margin-right:11px" v-if="checkedCartItemIds.length">共计<span
        style="color: palevioletred">￥{{ orderTotalPrice }}</span></div>
    <el-button type="primary" :disabled="checkedCartItemIds.length == 0" @click="makeOrderHandler" round size="small">
      立即购买
    </el-button>
    <br><br>
    <el-row v-for="(cartItemRow,index) in rowArr" :key="index">
      <el-col :span="3" v-for="(cartItem, index2) in cartItemRow" :key="index2" :offset="index2 > 0 ? 2 : 0">
        <div>
          <el-card :body-style="{ padding: '0px'}"
                   shadow="hover"
                   class="box-card">
            <div slot="header" class="el-cart_header">
              <el-button style="float: right; padding: 0 0" type="text" @click="removeCartItem(cartItem.id)">x
              </el-button>
            </div>
            <img :src="cartItem.imageUrl"
                 class="image">
            <div style="padding: 14px;">
              <div class="bookTitleDiv">
                <span style="font-size: 10px">{{ cartItem.name }}</span>
                <span style="float:right"><el-tag type="info">{{
                    getSortNameBySortId(cartItem.sortId)
                  }}</el-tag></span>
              </div>
              <div style="float: left">
                <span style="color: palevioletred">￥{{
                    cartItem.presentPrice
                  }}</span><br>
                <span style="color: darkgray; font-size: 1px">加购时价格:￥{{ cartItem.addedPrice }}</span>

                <el-input-number class="my-el-input" v-model="cartItem.num" @change="handleNumChange(cartItem)" :min="1"
                                 :max="cartItem.stock"
                                 size="small"></el-input-number>
              </div>
              <br>
              <span style="color: palevioletred">总价:￥{{
                  cartItem.presentPrice * cartItem.num
                }}</span><br>
              <input type="checkbox" name="checkedCartItemIds" v-model="checkedCartItemIds"
                     :value="cartItem.id"></input>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <el-dialog
        title="收货信息"
        :visible.sync="courierInfoDialogVisible"
        width="25%"
    >
      <el-form :model="courierInfoEditForm" :rules="courierInfoRules" ref="courierInfoEditForm">
        <el-form-item label="收货地址" prop="address" label-width="90px">
          <el-input v-model="courierInfoEditForm.address"></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="telephone" label-width="90px">
          <el-input v-model="courierInfoEditForm.telephone"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button @click="submitForm('courierInfoEditForm')">提交订单</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {getCart, removeCartItems, updateCart} from "@/api/moudles/bookstore/cart";
import {addOrder} from "@/api/moudles/bookstore/order";

export default {
  name: "Cart",
  data() {
    return {
      cartItems: [],
      rowArr: [],
      checkedCartItemIds: [],
      courierInfoDialogVisible: false,
      courierInfoEditForm: {
        address: '',
        telephone: ''
      },
      courierInfoRules: {
        address: [
          {required: true, message: '请输入收货地址', trigger: 'blur'},
        ],
        telephone: [
          {required: true, message: '请输入电话', trigger: 'blur'},
        ],
      }
    }
  },
  computed: {
    orderTotalPrice() {
      let totalPrice = 0
      this.checkedCartItems.forEach(checkedCartItem => {
        totalPrice += checkedCartItem.presentPrice * checkedCartItem.num
      })
      return totalPrice
    },
    checkedCartItems() {
      let checkedCartItems = []
      this.checkedCartItemIds.forEach(checkCartItemId => {
        this.cartItems.forEach(cartItem => {
          if (cartItem.id == checkCartItemId) {
            checkedCartItems.unshift(cartItem)
          }
        })
      })
      return checkedCartItems
    }
  },
  methods: {
    getCart() {
      getCart().then(resp => {
        this.cartItems = resp.data.data
        this.getCartItemsPresentPrice()
        this.cartItemRowsArr()
      }, error => {
        this.$message.error(error)
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
    makeOrderHandler() {
      this.courierInfoDialogVisible = true
    },
    isDiscountBook(bookId) {
      return this.$store.state.book.discountBooks.some(discountBook => {
        return discountBook.id == bookId
      })
    },
    removeCartItems() {
      removeCartItems({ids: this.checkedCartItemIds}).then(resp => {
        this.checkedCartItemIds = []
        this.checkedCartItems = []
      }, error => {
        this.$message.error(error)
      })
    },
    addOrder() {
      let params = {
        totalPrice: this.orderTotalPrice,
        address: this.courierInfoEditForm.address,
        telephone: this.courierInfoEditForm.telephone
      }
      addOrder(this.checkedCartItems, params).then(resp => {
        this.removeCartItems()
        this.$router.push({
          name: 'OrderDetails',
          //同样，如果要传params只能通过name
          query: {
            orderNumber: resp.data.data,
            isNewOrder: 1
          }
        })
      }, error => {
        this.$message.error(error)
      })
    },
    //判断购物车中的书是否当前为特价书后设置其当前价格
    getCartItemsPresentPrice() {
      this.cartItems.forEach(cartItem => {
        cartItem.presentPrice = this.isDiscountBook(cartItem.bookId) ? cartItem.discountPrice : cartItem.price
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.editForm = {}
    },
    submitForm(formName) {
      //验证所以表单项是否符合所定义规则
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addOrder()
        } else {
          this.$message({
            message: '内容不符合要求，请重新输入！',
            type: "warning"
          })
          return false;
        }
      });
    },
    cartItemRowsArr() {
      this.rowArr = []
      let currentIndex = 0
      for (let i = 0; i < (this.cartItems.length / 5) + 1; i++) {
        let arr = []
        for (let j = 0; j < 5; j++) {
          if (currentIndex < this.cartItems.length) {
            arr.push(this.cartItems[currentIndex++])
          }
        }
        this.rowArr.push(arr);
      }
    },
    removeCartItem(cartItemId) {
      let ids = []
      ids.unshift(cartItemId)
      removeCartItems({ids}).then(resp => {
        this.getCart()
      }, error => {
        this.$message.error(error)
      })
    },
    //每次修改购物车中的数量时都应发请求修改
    handleNumChange(cartItem) {
      let params = {id: cartItem.id, num: cartItem.num}
      updateCart(params).then(resp => {
      }, error => {
        this.$message.error(error)
      })
    }
  },
  mounted() {
    this.getCart()
  }
}
</script>

<style scoped>
.box-card {
  width: 110%;
}

.bookTitleDiv {
  width: 100%;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
}

.image {
  width: 100%;
  height: 27vh;
  display: block;
}

.my-el-input {
}

.el-cart_header {
  padding: 0;
}


.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>