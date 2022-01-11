import request from "@/network/request";
import qs from "qs";

export function getSorts(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sort/listSort',
		params
	});
}

export function deleteBooks(params) {
	return request({
		method: 'delete',
		url: 'http://localhost:8081/book/deleteBooks',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	});
}

export function getBook(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/book/bookList',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	});
}

export function addBook(data) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/book/addBook',
		data
	});
}

export function updateBook(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/book/updateBook',
		data
	});
}

export function upImg(data) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/book/upImg',
		data,
		headers: {'Content-Type': 'multipart/form-data'}
	});
}

export function getDiscountBooks() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/book/discountBooks'
	})
}