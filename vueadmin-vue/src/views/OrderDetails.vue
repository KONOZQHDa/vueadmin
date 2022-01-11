<template>
  <div v-loading="loading"
       element-loading-text="拼命加载中"
       element-loading-spinner="el-icon-loading"
       element-loading-background="rgba(0, 0, 0, 0.8)">
    <div v-for="(orderItem,index) in orderItems" :key="index"
         style="border: #999999 1px solid;margin: 0 10%;">

      <img :src='orderItem.imageUrl'
           class="order-item-img">
      <!--        订单项的书籍信息-->
      <div style="display: inline-block;">
        书籍名称：<span>{{ orderItem.name }}</span><br>
        数量：<span>{{ orderItem.num }}</span><br>
        价格：<span style="color: palevioletred">￥{{ orderItem.price }}</span><br>
        总价：<span style="color: palevioletred">￥{{ orderItem.price * orderItem.num }}</span>
      </div>
    </div>
    <br>
    <div style="margin-left: 10%">
      地址：<span>{{ order.address }}</span><br><br>
      电话：<span>{{ order.telephone }}</span><br><br>
      订单总价：<span style="color: palevioletred">￥{{ order.totalPrice }}</span><br><br>
    </div>
    <div style="margin-left: 10%" v-if="isNewOrder">
      <el-button type="primary" round @click="payOrder">立即支付</el-button>
      <el-button type="primary" round @click="cancelOrder">取消订单</el-button>
    </div>
  </div>
</template>
<script>
import {cancelOrder, getOrderDetails, getOrderInfo, payOrder} from "@/api/moudles/bookstore/order";

export default {
  name: "OrderDetails",
  data() {
    return {
      orderItems: [],
      order: null,
      loading: true,
      orderNumber: this.$route.query.orderNumber,
      isNewOrder: this.$route.query.isNewOrder
    }
  },
  methods: {
    getOrderItems(orderNumber) {
      getOrderDetails({orderNumber}).then(resp => {
        this.orderItems = resp.data.data
        this.loading = false
      }, err => {
        this.$message.error(err)
      })
    },
    getOrderInfo(orderNumber) {
      getOrderInfo({orderNumber}).then(resp => {
        this.order = resp.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    payOrder() {
      payOrder({orderNumber: this.orderNumber}).then(resp => {
        this.$message.success("假装支付成功")
        setTimeout(() => {
          this.$router.replace('/order')
        }, 1500)
      }, error => {
        this.$message.error(error)
      })
    },
    cancelOrder() {
      cancelOrder({orderNumber: this.orderNumber}).then(resp => {
        this.$message.success(resp.data.message)
        setTimeout(() => {
          this.$router.push('/order')
        }, 1500)
      })
    }
  },
  mounted() {
    this.getOrderItems(this.orderNumber)
    this.getOrderInfo(this.orderNumber)
  }
}
</script>

<style scoped>
.order-item-img {
  vertical-align: middle;
  display: inline-block;
  height: 130px;
  width: 105px;
  margin: 1% 3% 1% 1%
}

</style>