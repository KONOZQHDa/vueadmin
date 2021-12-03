import Vue from "vue";

Vue.mixin({
	data() {
		return {
			glUsful: 'usful_status',
			bookStatus: 'book_status',
			bookStatusTagType: 'book_status_tagType',
			usfulStatusTagType: 'usful_status_tagType'
		}
	},
	methods: {
		hasAuthorization(authorizationName) {
			return this.$store.state.menu.authorizations.includes(authorizationName)
		},
		formatByDiction(dictypeKey, dicKey) {
			//targetDictype 前几次会为空，可能是main.js中请求字典数据还未成功返回，不知道咋破
			let targetDictype = this.$store.state.diction.dictypes[dictypeKey]
			let dic_Value = ''
			targetDictype.forEach(dic => {
				if (dic.dicKey == dicKey) {
					dic_Value = dic.dicValue
				}
			})
			return dic_Value == '' ? 'Unknown' : dic_Value
		}
	}

})