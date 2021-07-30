import Vue from 'vue'
import VueRouter from 'vue-router'
import Router from 'vue-router'
import Login from '@/views/userCenter/Login'
import Register from '@/views/userCenter/Register'
import Browse from '@/views/bookCenter/Browse'
import store from '@/store'
import Upload from '@/views/admin/Upload'
import Personal from '@/views/userCenter/Personal'
import PassModify from '@/views/userCenter/PassModify'
import CreateAdmin from '@/views/admin/CreateAdmin'
import BookDetails from '@/views/BookDetails'
import BranchConfirm from "@/views/admin/BranchConfirm";
import BorrowOnSite from "@/views/admin/BorrowOnSite";
import GetSubscribe from "@/views/admin/GetSubscribe";
import ReturnOnSite from "@/views/admin/ReturnOnSite";
import PermissionRules from "@/views/admin/PermissionRules"
import History from "@/views/admin/History"
import FinePaying from "@/views/userCenter/FinePaying"
import BlankCrumb from "@/components/utils/BlankCrumbWithNav";
import MyComment from "@/views/userCenter/MyComment"
import CreditReasonGetter from "@/views/admin/CreditReasonGetter"
import ElementUI from 'element-ui'
Vue.use(VueRouter)

let needLogin = function (to, from, next) {
    if (store.state.session) {
        next()
    } else {
        ElementUI.Message({
            message: '请在登录后查看',
            type: 'warning'
        })
        next({
            path: '/login'
        })
    }
}

let needLogout = function (to, from, next) {
    if (store.state.session) {
        ElementUI.Message({
            message: '重新登录/注册请现在个人中心中登出',
            type: 'warning'
        })
        next(from)
    } else {
        next()
    }
}

export const router = new Router({
    mode: "hash",
    routes: [
        {
            path: '/',
            name: '',
            component: BlankCrumb,
            children: [
                {
                    path: 'browse',
                    name: 'browse',
                    component: Browse,
                },
                {
                    path: 'login',
                    name: 'login',
                    component: Login,
                    meta: {needLogOut: true},
                    beforeEnter: needLogout
                },
                {
                    path: 'register',
                    name: 'register',
                    component: Register,
                    meta: {needLogOut: true},
                    beforeEnter: needLogout
                },
                {
                    path: 'bookDetails',
                    name: 'bookDetails',
                    component: BookDetails,
                },
            ]
        },
        {
            path: '/user',
            name: 'user',
            component: BlankCrumb,
            children: [
                {
                    path: 'personal',
                    name: 'personal',
                    component: Personal,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'passModify',
                    name: 'passModify',
                    component: PassModify,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'finePaying',
                    name: 'finePaying',
                    component: FinePaying,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'myComment',
                    name: 'myComment',
                    component: MyComment,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
            ]
        },
        {
            path: '/admin',
            name: 'admin',
            component: BlankCrumb,
            children: [
                {
                    path: 'upload',
                    name: 'upload',
                    component: Upload,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'createAdmin',
                    name: 'createAdmin',
                    component: CreateAdmin,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'borrowOnSite',
                    name: 'borrowOnSite',
                    component: BorrowOnSite,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'creditReason',
                    name: 'creditReason',
                    component: CreditReasonGetter,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'returnOnSite',
                    name: 'returnOnSite',
                    component: ReturnOnSite,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'getSubscribe',
                    name: 'getSubscribe',
                    component: GetSubscribe,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'permissionRules',
                    name: 'permissionRules',
                    component: PermissionRules,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                },
                {
                    path: 'history',
                    name: 'history',
                    component: History,
                    meta: {requireAuth: true},
                    beforeEnter: needLogin
                }
            ]
        },
        {
            path: '/branchConfirm',
            component: BranchConfirm,
            meta: {requireAuth: true},
        }
    ]
})

export default router
