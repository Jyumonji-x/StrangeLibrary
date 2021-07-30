<template>
    <Form ref="loginForm" :model="loginForm" :rules="rules" :label-width="120">
        <FormItem label="学号/工号" prop="username">
            <Input type="text" v-model="loginForm.username" autocomplete="off"></Input>
        </FormItem>
        <FormItem label="密码" prop="password">
            <Input type="password" v-model="loginForm.password" autocomplete="off"></Input>
        </FormItem>
        <FormItem class="mb-0">
            <Button type="primary" @click.native="submitForm()">提交</Button>
            <Button @click.native="resetForm()">重置</Button>
            <span class="ml-2">还没有账号？<router-link to="/register">去注册</router-link></span>
        </FormItem>
    </Form>
</template>
<script>
    export default {
        data() {
            return {
                loginForm: {
                    username: '',
                    password: '',
                },
                rules: {
                    username: [{
                        required: true,
                        message: '请输入用户名',
                        trigger: 'blur'
                    }],
                    password: [{
                        required: true,
                        message: '请输入密码',
                        trigger: 'blur'
                    }],
                }
            };
        },
        methods: {
            submitForm() {
                this.$refs['loginForm'].validate((valid) => {
                    if (valid) {
                        this.$axios.post("/apiUser/user/login", {
                            username: this.loginForm.username,
                            password: this.loginForm.password,
                        })
                            .then(resp => {
                                if (resp.status === 200) {
                                    if (resp.data.rtn === 1) {
                                        console.log("login result:")
                                        console.log(resp.data)
                                        this.$store.commit("login", resp.data)
                                        console.log(this.$store);
                                        let permission = this.$store.state.user.permission;
                                        // let permission = resp.data.permission
                                        if (permission === "管理员"||permission === "超级管理员") {
                                            this.$router.replace('/branchConfirm');
                                        } else if (permission === "普通用户") {
                                            this.$router.replace({path: '/browse'});
                                        }
                                    } else {
                                        this.$message({
                                            message: resp.data.message,
                                            type: 'warning'
                                        })
                                    }
                                }
                            })
                    } else {
                        this.$message({
                            message: '请填写后再登录',
                            type: 'warning'
                        })
                        return false;
                    }
                });
            }
            ,
            resetForm() {
                this.$refs['loginForm'].resetFields();
            }
        }
    }
</script>
