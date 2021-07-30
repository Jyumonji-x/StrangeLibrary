<template>
    <el-form :model="passModifyForm" status-icon :rules="rules" ref="passModifyForm" label-width="120px"
             class="demo-passModify">
        <el-form-item label="修改密码" prop="modifyPass">
            <el-input type="password" v-model="passModifyForm.modifyPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="请再输入一次" prop="modifyPassConfirm">
            <el-input type="password" v-model="passModifyForm.modifyPassConfirm" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item class="mb-0">
            <el-button type="primary" @click="submitForm('passModifyForm')">提交</el-button>
            <el-button @click="resetForm('passModifyForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>
<script>
    export default {
        data() {
            let saveUsername = this.$store.state.user.username
            let checkContain = /^[\w-_]*$/ //检测账号密码是否只包含数字、字母、横杠、下划线
            let checkLength = /^[\w-_]{6,32}$/ //检测长度
            let checkContainNum = /[0-9]+/     //检测是否包含数字
            let checkContainLetter = /[A-Za-z]+/ //检测是否包含字母
            let checkContainSpecial = /[-_]+/  //检测是否包含横杠
            const validatePassword = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'))
                } else if (!checkContain.test(value)) {
                    callback(new Error('密码需要由字母、数字、-、_构成'))
                } else if (!checkLength.test(value)) {
                    callback(new Error('密码长度需在6-32个字符'))
                } else {
                    let count = 0
                    if (checkContainNum.test(value)) {
                        count++
                        console.log('contain num')
                    }
                    if (checkContainLetter.test(value)) {
                        count++
                        console.log('contain letter')
                    }
                    if (checkContainSpecial.test(value)) {
                        count++
                        console.log('contain special')
                    }
                    if (count < 2) {
                        callback(new Error('密码需要包含字母、数字、(-、_)中的至少两种'))
                    } else if (value.indexOf(saveUsername) !== -1) {
                        callback(new Error('密码不能包含用户名'))
                    } else {
                        callback();
                    }
                }
            };
            const validatePassConfirm = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再确认一次修改后的密码'))
                } else if (value !== this.passModifyForm.modifyPass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                passModifyForm: {
                    modifyPass: '',
                    modifyPassConfirm: ''
                },
                rules: {
                    modifyPass: [{
                        required: true,
                        validator: validatePassword,
                        trigger: 'blur'
                    }],
                    modifyPassConfirm: [{
                        required: true,
                        validator: validatePassConfirm,
                        trigger: 'blur'
                    }]
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let session = this.$store.state.session;
                        this.$axios.post("/apiUser/user/modify", {
                            'session': session,
                            'password': this.passModifyForm.modifyPass,
                        })
                            .then(resp => {
                                if (resp.status === 200) {
                                    if (resp.data.rtn === 1) {
                                        this.$store.commit('logout')
                                        this.$router.replace({path: '/login'})
                                        this.$message({
                                            message: resp.data.message,
                                            type: 'success'
                                        })
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
                            message: '请先填写内容',
                            type: 'warning'
                        })
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>
