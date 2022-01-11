import request from "@/network/request";


export function getUserOrders() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/order/userOrders'
	})
}

export function addOrder(data, params) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/order/addOrder',
		data,
		params
	})
}

export function payOrder(params) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/order/payOrder',
		params,
	})
}

//获取订单的所有订单项
export function getOrderDetails(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/order/orderDetails',
		params
	})
}

//获取订单的信息
export function getOrderInfo(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/order/orderInfo',
		params
	})
}

export function cancelOrder(params) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/order/cancelOrder',
		params
	})
}