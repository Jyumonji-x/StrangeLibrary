<template>
  <Form ref="registerForm" :model="registerForm" :rules="rules" :label-width="120">
    <FormItem label="学号/工号" prop="username">
      <Input type="text" v-model="registerForm.username" autocomplete="off"></Input>
    </FormItem>
    <FormItem label="密码" prop="password">
      <Input type="password" v-model="registerForm.password" autocomplete="off"></Input>
    </FormItem>
    <FormItem label="重复密码" prop="passwordConfirm">
      <Input type="password" v-model="registerForm.passwordConfirm" autocomplete="off"></Input>
    </FormItem>
    <FormItem label="邮箱验证码" prop="emailCode">
      <Input type="text" v-model="registerForm.emailCode" autocomplete="off"></Input>
    </FormItem>
    <FormItem class="mb-0">
      <Button type="primary" @click.native="submitForm('registerForm')">提交</Button>
      <Button @click.native="resetForm('registerForm')">重置</Button>
      <Button v-if="!this.canSendCaptcha" type="primary" disabled>重新发送（{{ this.time }}）</Button>
      <Button v-if="this.canSendCaptcha" type="primary" @click.native="sendCode()">发送验证码</Button>
      <br/>
      <span class="ml-2">已经有账号？<router-link to="/login">去登录</router-link></span>
    </FormItem>

  </Form>

</template>
<script>
export default {
  data() {
    let saveUsername = ''
    let checkContain = /^[\w-_]*$/ //检测账号密码是否只包含数字、字母、横杠、下划线
    let checkLength = /^[\w-_]{6,32}$/ //检测长度
    let checkContainNum = /[0-9]+/     //检测是否包含数字
    let checkContainLetter = /[A-Za-z]+/ //检测是否包含字母
    let checkContainSpecial = /[-_]+/  //检测是否包含横杠
    let checkNumber = /^[0-9]+$/

    function isValidSchoolNumber(number) {
      if (!checkNumber.test(number)) {
        return false
      } else {
        let checkNumberLength6 = /^[0-9]{6}$/
        let checkNumberLength11 = /^[0-9]{11}$/
        if (checkNumberLength11.test(number)) {
          //是学号，进一步检测第三位是否为1,2,3
          let charAtNumber = number.toString().charAt(2)
          return !(charAtNumber !== '1' && charAtNumber !== '2' && charAtNumber !== '3')
        } else {
          // 不是学号，检测是否为6位教师工号
          return checkNumberLength6.test(number)
        }
      }
    }

    // let checkEmail = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/ //email检测
    // const validateEmail = (rule, value, callback) => {
    //   if (value === '') {
    //     callback(new Error('请输入邮箱'));
    //   } else if (!checkEmail.test(value)) {
    //     callback('邮箱格式非法')
    //   } else {
    //     callback();
    //   }
    // };
    const validateUsername = (rule, value, callback) => {
      saveUsername = value
      if (value === '') {
        callback(new Error('请输入学号/工号'))
      } else if (!isValidSchoolNumber(value)) {
        callback(new Error('请输入正确的学号/工号'))
      } else {
        callback();
      }
    };
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
    const validatePasswordConfirm = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    const validateEmailCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入邮箱验证码'))
      } else {
        callback();
      }
    }
    return {
      time: 60,
      canSendCaptcha: true,
      registerForm: {
        username: '',
        password: '',
        passwordConfirm: '',
        emailCode: ''
      },
      rules: {
        // email: [{
        //   validator: validateEmail,
        //   required: true,
        //   trigger: 'blur'
        // }],
        username: [{
          validator: validateUsername,
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
        }],
        emailCode: [{
          validator: validateEmailCode,
          required: true,
          trigger: 'blur'
        }]
      }
    };
  },
  methods: {
    clock() {
      let i = 60;
      this.time = i;
      this.canSendCaptcha = false;
      let interval = setInterval(() => {
        this.time = --i;
        if (i === 0) {
          this.canSendCaptcha = true;
          clearInterval(interval);
        }
      }, 1000);
    },
    isValidSchoolNumber(number) {
      let checkNumber = /^[0-9]+$/
      if (!checkNumber.test(number)) {
        return false
      } else {
        let checkNumberLength6 = /^[0-9]{6}$/
        let checkNumberLength11 = /^[0-9]{11}$/
        if (checkNumberLength11.test(number)) {
          //是学号，进一步检测第三位是否为1,2,3
          let charAtNumber = number.toString().charAt(2)
          return !(charAtNumber !== '1' && charAtNumber !== '2' && charAtNumber !== '3')
        } else {
          // 不是学号，检测是否为6位教师工号
          return checkNumberLength6.test(number)
        }
      }
    },
    submitForm(formName) {
      console.log(this.registerForm.username + " " + this.registerForm.password);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/apiUser/user/register/", {
            username: this.registerForm.username,
            password: this.registerForm.password,
            captcha: this.registerForm.emailCode
          })
              .then(resp => {
                if (resp.status === 200) {
                  if (resp.data.rtn === 1) {
                    console.log("register result:")
                    console.log(resp.data)
                    this.$store.commit('login', resp.data)
                    this.$router.replace({path: '/browse'})
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
    },
    sendCode() {
      console.log(this.registerForm.username);
      if (this.registerForm.username === '') {
        this.$message({
          message: "请先输入学号/工号！",
          type: 'warning'
        })
        return;
      } else if (!this.isValidSchoolNumber(this.registerForm.username)) {
        this.$message({
          message: "请先输入正确的学号/工号！",
          type: 'warning'
        })
        return;
      }
      this.$axios.get("/apiUser/user/captcha/" + this.registerForm.username, {}).then((resp) => {
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            this.clock();
            this.$message({
              message: "发送成功，请注意查收！",
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
    }
  },
  watch: {
    canSendCaptcha: {
      handler() {
        console.log("canSendCaptcha" + this.canSendCaptcha)
      }
    }
  }
}
</script>

