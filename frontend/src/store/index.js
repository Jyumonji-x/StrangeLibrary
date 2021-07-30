import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        //免登陆测试模式
        // userDetails: {
        //     username: 19302010020,
        //     permission:2,
        // },
        // token: 100,
        user: null,
        session: null,
        branch:null,
    },
    mutations: {
        setBranch(state, branch) {
            state.branch = branch
        },
        login(state, data) {
            state.session = data.session
            state.user = data.user
        },
        logout(state) {
            state.session = null
            state.user = null
        }
    },
    actions: {},
    modules: {}
})
