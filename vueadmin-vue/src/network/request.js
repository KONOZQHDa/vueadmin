import axios from "axios"
import router from "../router/index"

export default function getrequest(config) {
	const request = axios.create({
		timeout: 5000,
		baseURL: 'http://localhost:8080',
		headers: {'Content-Type': "application/json;charset=utf-8"}
	})
	//sa-token认证信息
	request.interceptors.request.use(config => {
		config.headers[sessionStorage.getItem('tokenName')] = sessionStorage.getItem('tokenValue')
		return config
	})

	request.interceptors.response.use(
			response => {
				if (response.data.code == 200) {
					return response
				}
				//code=401说明未登录
				else if (response.data.code == 401){
					/*//因为import router from "../router/index"导致的bug未解决、
					// 所以跳转到登录页面的功能暂时按下面实现
					window.location.href="http://localhost:8080/#/login";*/
					router.push("/login").then()
					return Promise.reject(response.data.message)
				} else {
					return Promise.reject(response.data.message)
				}
			}, error => {
				return Promise.reject(error.message)
			})

	return request(config)
}