<template>
  <div>
    <h2>用户规则</h2>
    <divider/>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
      <h3>教师规则</h3>
      <FormItem label="最⼤借书数⽬" prop="maxAmountTeacher">
        <Input v-model="formValidate.maxAmountTeacher" placeholder="请输入最⼤借书数⽬"></Input>
      </FormItem>
      <FormItem label="借书有效期/秒" prop="borrowTimeTeacher">
        <Input v-model="formValidate.borrowTimeTeacher" placeholder="请输入借书有效期/秒"></Input>
      </FormItem>
      <FormItem label="预约有效期/秒" prop="subscribeTimeTeacher">
        <Input v-model="formValidate.subscribeTimeTeacher" placeholder="请输入预约有效期/秒"></Input>
      </FormItem>
      <h3>本科生规则</h3>
      <FormItem label="最⼤借书数⽬" prop="maxAmountUndergra">
        <Input v-model="formValidate.maxAmountUndergra" placeholder="请输入最⼤借书数⽬"></Input>
      </FormItem>
      <FormItem label="借书有效期/秒" prop="borrowTimeUndergra">
        <Input v-model="formValidate.borrowTimeUndergra" placeholder="请输入借书有效期/秒"></Input>
      </FormItem>
      <FormItem label="预约有效期/秒" prop="subscribeTimeUndergra">
        <Input v-model="formValidate.subscribeTimeUndergra" placeholder="请输入预约有效期/秒"></Input>
      </FormItem>
      <h3>研究生规则</h3>
      <FormItem label="最⼤借书数⽬" prop="maxAmountPostgra">
        <Input v-model="formValidate.maxAmountPostgra" placeholder="请输入最⼤借书数⽬"></Input>
      </FormItem>
      <FormItem label="借书有效期/秒" prop="borrowTimePostgra">
        <Input v-model="formValidate.borrowTimePostgra" placeholder="请输入借书有效期/秒"></Input>
      </FormItem>
      <FormItem label="预约有效期/秒" prop="subscribeTimePostgra">
        <Input v-model="formValidate.subscribeTimePostgra" placeholder="请输入预约有效期/秒"></Input>
      </FormItem>
      <FormItem>
        <Button type="primary" @click="handleSubmit('formValidate')">提交修改</Button>
      </FormItem>
    </Form>
  </div>

</template>
<script>
export default {
  data() {
    let checkNumber = /^[0-9]{1,8}$/
    const validateMaxAmount = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入最⼤借书数⽬'))
      } else if (!checkNumber.test(value)) {
        callback(new Error('请输入正确的最⼤借书数⽬值（1-99999999）'))
      } else {
        callback();
      }
    };
    const validateBorrowTime = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入借书有效期'))
      } else if (!checkNumber.test(value)) {
        callback(new Error('请输入正确的借书有效期值（1-99999999）'))
      } else {
        callback();
      }
    };
    const validateSubscribeTime = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入预约有效期'))
      } else if (!checkNumber.test(value)) {
        callback(new Error('请输入正确的预约有效期值（1-99999999）'))
      } else {
        callback();
      }
    };
    return {
      formValidate: {
        maxAmountUndergra: '',
        borrowTimeUndergra: '',
        subscribeTimeUndergra: '',
        maxAmountPostgra: '',
        borrowTimePostgra: '',
        subscribeTimePostgra: '',
        maxAmountTeacher: '',
        borrowTimeTeacher: '',
        subscribeTimeTeacher: '',
      },
      ruleValidate: {
        maxAmountTeacher: [
          {
            required: true,
            validator: validateMaxAmount,
            trigger: 'blur'
          }
        ],
        borrowTimeTeacher: [
          {
            required: true,
            validator: validateBorrowTime,
            trigger: 'blur'
          }
        ],
        subscribeTimeTeacher: [
          {
            required: true,
            validator: validateSubscribeTime,
            trigger: 'blur'
          }
        ],
        maxAmountUndergra: [
          {
            required: true,
            validator: validateMaxAmount,
            trigger: 'blur'
          }
        ],
        borrowTimeUndergra: [
          {
            required: true,
            validator: validateBorrowTime,
            trigger: 'blur'
          }
        ],
        subscribeTimeUndergra: [
          {
            required: true,
            validator: validateSubscribeTime,
            trigger: 'blur'
          }
        ],
        maxAmountPostgra: [
          {
            required: true,
            validator: validateMaxAmount,
            trigger: 'blur'
          }
        ],
        borrowTimePostgra: [
          {
            required: true,
            validator: validateBorrowTime,
            trigger: 'blur'
          }
        ],
        subscribeTimePostgra: [
          {
            required: true,
            validator: validateSubscribeTime,
            trigger: 'blur'
          }
        ],
      }
    }
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.$axios.post("/apiBorrow/borrowRules", {
            maxAmountTeacher: this.formValidate.maxAmountTeacher,
            maxAmountUndergra: this.formValidate.maxAmountUndergra,
            maxAmountPostgra: this.formValidate.maxAmountPostgra,
            borrowTimeTeacher: this.formValidate.borrowTimeTeacher,
            borrowTimeUndergra: this.formValidate.borrowTimeUndergra,
            borrowTimePostgra: this.formValidate.borrowTimePostgra,
            subscribeTimeTeacher: this.formValidate.subscribeTimeTeacher,
            subscribeTimeUndergra: this.formValidate.subscribeTimeUndergra,
            subscribeTimePostgra: this.formValidate.subscribeTimePostgra,
          }).then(resp => {
            if (resp.data.rtn === 1) {
              this.$message({
                message: resp.data.message,
                type: 'success'
              })
              this.initial();
            } else {
              this.$message({
                message: resp.data.message,
                type: 'warning'
              })
            }
          })
        } else {
          this.$message({
            message: '请填写完成后再注册',
            type: 'warning'
          })
          return false;
        }
      })
    },
    initial() {
      this.$axios.get("/apiBorrow/borrowRules").then(resp => {
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            console.log("config get!")
            console.log(resp.data)
            this.formValidate = resp.data;
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
  mounted() {
    this.initial();
  }
}
</script>
i
