import axios from "axios"
import router from "../router/index"

export default function getrequest(config) {
	const request = axios.create({
		timeout: 10000,
		// headers: {'Content-Type': "application/json;charset=utf-8"}
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
				else if (response.data.code == 401) {
					router.push("/login").then()
					return Promise.reject(response.data.message)
				} else {
					return Promise.reject(response.data.message)
				}
			}, error => {
				console.log("reject");
				return Promise.reject(error.message)
			})

	return request(config)
}