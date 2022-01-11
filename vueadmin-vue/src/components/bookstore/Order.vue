<template>
  <div>
    <el-tabs type="border-card">
      <el-tab-pane label="已支付订单">
        <el-table
            :data="payedOrders"
            style="width: 100%">
          <el-table-column
              label="创建时间"
              width="200">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span style="margin-left: 10px">{{ scope.row.created }}</span>
            </template>
          </el-table-column>
          <el-table-column
              label="订单编号"
              prop="orderNumber"
              width="320">
          </el-table-column>
          <el-table-column
              label="总价"
              width="190">
            <template slot-scope="scope">
              <span style="color: palevioletred;">￥{{ scope.row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  @click="orderDetails(scope.row.orderNumber)">查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="待支付订单">
        <el-table
            :data="notPayedOrders"
            style="width: 100%">
          <el-table-column
              label="创建时间"
              width="200">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span style="margin-left: 10px">{{ scope.row.created }}</span>
            </template>
          </el-table-column>
          <el-table-column
              label="订单编号"
              prop="orderNumber"
              width="320">
          </el-table-column>
          <el-table-column
              label="总价"
              width="190">
            <template slot-scope="scope">
              <span style="color: palevioletred;">￥{{ scope.row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  @click="orderDetails(scope.row.orderNumber)">查看详情
              </el-button>
              <el-button
                  size="mini"
                  @click="payOrder(scope.row.orderNumber)">立即支付
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="已过期订单">
        <el-table
            :data="expiredOrders"
            style="width: 100%">
          <el-table-column
              label="创建时间"
              width="200">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span style="margin-left: 10px">{{ scope.row.created }}</span>
            </template>
          </el-table-column>
          <el-table-column
              label="订单编号"
              prop="orderNumber"
              width="320">
          </el-table-column>
          <el-table-column
              label="总价"
              width="190">
            <template slot-scope="scope">
              <span style="color: palevioletred;">￥{{ scope.row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  @click="orderDetails(scope.row.orderNumber)">查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="取消订单">
        <el-table
            :data="canceledOrders"
            style="width: 100%">
          <el-table-column
              label="创建时间"
              width="200">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span style="margin-left: 10px">{{ scope.row.created }}</span>
            </template>
          </el-table-column>
          <el-table-column
              label="订单编号"
              prop="orderNumber"
              width="320">
          </el-table-column>
          <el-table-column
              label="总价"
              width="190">
            <template slot-scope="scope">
              <span style="color: palevioletred;">￥{{ scope.row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  @click="orderDetails(scope.row.orderNumber)">查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {getUserOrders, payOrder} from "@/api/moudles/bookstore/order";

export default {
  name: "Order",
  data() {
    return {
      orders: []
    }
  },
  computed: {
    payedOrders() {
      return this.orders.filter(order => {
        return order.payStatus == 1
      })
    },
    notPayedOrders() {
      return this.orders.filter(order => {
        return order.payStatus == 0
      })
    },
    expiredOrders() {
      return this.orders.filter(order => {
        return order.payStatus == 2
      })
    },
    canceledOrders() {
      return this.orders.filter(order => {
        return order.payStatus == 3
      })
    }
  },
  methods: {
    getOrders() {
      getUserOrders().then(response => {
        this.orders = response.data.data
      }, error => {
        this.$message.error(error)
      })
    },
    payOrder(orderNumber) {
      payOrder({orderNumber}).then(resp => {
        this.$message.success("假装支付成功")
        this.getOrders()
      }, error => {
        this.$message.error(error)
      })
    },
    orderDetails(orderNumber) {
      this.$router.push({
        name: 'OrderDetails',
        query: {
          orderNumber,
          isNewOrder: 0
        }
      })
    }

  },
  mounted() {
    this.getOrders()
  }
}
</script>

<style scoped>

</style>