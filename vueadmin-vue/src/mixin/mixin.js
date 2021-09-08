import Vue from "vue";
Vue.mixin({
	methods:{
		hasAuthorization(authorizationName){
				return this.$store.state.menu.authorizations.includes(authorizationName)
		}
	}

})