<template>
  <Form :model="registerForm" status-icon :rules="rules" ref="registerForm"
           class="demo-registerForm">
    <FormItem label="用户名" prop="username">
      <Input type="text" v-model="registerForm.username" autocomplete="off"></Input>
    </FormItem>
    <FormItem label="密码" prop="password">
      <Input type="password" v-model="registerForm.password" autocomplete="off"></Input>
    </FormItem>
    <FormItem label="重复密码" prop="passwordConfirm">
      <Input type="password" v-model="registerForm.passwordConfirm" autocomplete="off"></Input>
    </FormItem>
    <FormItem class="mb-0">
      <Button type="primary" @click.native="submitForm('registerForm')">提交</Button>
      <Button @click.native="resetForm('registerForm')">重置</Button>
    </FormItem>
  </Form>
</template>
<script>

export default {
  data() {
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
        } else if (value.indexOf(this.registerForm.username) !== -1) {
          callback(new Error('密码不能包含用户名'))
        } else {
          callback();
        }
      }
    };
    const validatePasswordConfirm = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        passwordConfirm: ''
      },
      rules: {
        username: [{
          // validator: validateUsername,
          required: true,
          trigger: 'blur'
        }],
        password: [{
          validator: validatePassword,
          required: true,
          trigger: 'blur'
        }],
        passwordConfirm: [{
          validator: validatePasswordConfirm,
          required: true,
          trigger: 'blur'
        }]
      }
    };
  },
  methods: {
    submitForm(formName) {
      console.log(this.registerForm.username + " " + this.registerForm.password);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/apiUser/authority/create_admin", {
            username: this.registerForm.username,
            password: this.registerForm.password,
            session: this.$store.state.session
          })
              .then(resp => {
                if (resp.status === 200) {
                  if (resp.data.rtn === 1) {
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
            message: '请填写后再注册',
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

