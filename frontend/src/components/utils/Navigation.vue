<template>
    <div id="navigation">
        <Sider :style="{position: 'fixed', height: '100vh', left: 0, overflow: 'auto'}">
            <Menu :active-name="$router.currentRoute.name" id="menu" theme="dark" width="auto" :open-names="['1']">
                <!--公共区域-->
                <Submenu name="1">
                    <template slot="title">
                        <Icon type="ios-navigate"></Icon>
                        图书中心
                    </template>
                    <MenuItem name="browse" to="/browse">浏览书库</MenuItem>
                </Submenu>
                <!--注册登录-->
                <Submenu name="2" v-if="!$store.state.session">
                    <template slot="title">
                        <Icon type="ios-keypad"></Icon>
                        请登录
                    </template>
                    <MenuItem name="login" to="/login">登录</MenuItem>
                    <MenuItem name="register" to="/register">注册</MenuItem>
                </Submenu>
                <!--超级管理员-->
                <Submenu name="3" v-if="$store.state.session && ($store.state.user.permission==='超级管理员'||$store.state.user.permission==='管理员')">
                    <template slot="title">
                        <Icon type="ios-keypad"></Icon>
                        管理员功能
                    </template>
                    <MenuItem name="upload" to="/admin/upload">上传书籍</MenuItem>
                    <menuItem name="createAdmin" to="/admin/createAdmin"
                              v-if="$store.state.session && $store.state.user.permission==='超级管理员'">创建管理员
                    </menuItem>
                    <menuItem name="getSubscribe" to="/admin/getSubscribe">取预约图书</menuItem>
                    <menuItem name="borrowOnSite" to="/admin/borrowOnSite">现场借书</menuItem>
                    <menuItem name="returnOnSite" to="/admin/returnOnSite">现场还书</menuItem>
                    <menuItem name="creditReason" to="/admin/creditReason">信用分恢复审批</menuItem>
                    <menuItem name="history" to="/admin/history">系统记录</menuItem>
                    <menuItem name="permissionRules" to="/admin/permissionRules"
                    v-if="$store.state.session && $store.state.user.permission==='超级管理员'">用户规则</menuItem>
                </Submenu>
                <!--个人中心-->
                <Submenu name="4" v-if="$store.state.session">
                    <template slot="title">
                        <Icon type="ios-keypad"></Icon>
                        个人中心
                    </template>
                    <menuItem name="personal" to="/user/personal">我的信息</menuItem>
                    <menuItem v-if="$store.state.user.permission==='普通用户'" name="finePaying" to="/user/finePaying">支付罚款</menuItem>
                    <menuItem v-if="$store.state.user.permission==='普通用户'" name="myComment" to="/user/myComment">我的评论</menuItem>
                    <menuItem name="passModify" to="/user/passModify">修改密码</menuItem>
                    <menuItem name="logout" @click.native="logout">登出</menuItem>
                </Submenu>
            </Menu>
        </Sider>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                isCollapsed: false,
                current: this.$router.currentRoute.name
            };
        },
        computed: {
            menuitemClasses: function () {
                return [
                    'menu-item',
                    this.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
        },
        methods: {
            logout() {
                this.$store.commit('logout')
                this.$router.replace({path: '/browse'})
            },
        },
        mounted() {
            console.log(this.current)
        }
    }
</script>
<style>
    .menu-item span {
        display: inline-block;
        overflow: hidden;
        width: 69px;
        text-overflow: ellipsis;
        white-space: nowrap;
        vertical-align: bottom;
        transition: width .2s ease .2s;
    }

    .menu-item i {
        transform: translateX(0px);
        transition: font-size .2s ease, transform .2s ease;
        vertical-align: middle;
        font-size: 16px;
    }
</style>
