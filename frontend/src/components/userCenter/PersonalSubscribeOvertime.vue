<template>
  <div v-if="this.subscribeOvertimeData.length!==0">
    <h3>预约逾期书籍</h3>
    <br/>
    <Table :columns="subscribeOvertimeColumns" :data="subscribeOvertimeData"></Table>
    <br/><br/>
    <p style="color: red">以上书本预约超期而为借出，预约已自动取消，确认后清除废弃预约记录</p>
    <Button type="primary" @click.native="removeSubscribe()">确认逾期</Button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      subscribeOvertimeColumns: [
        {
          title: '书籍名称',
          key: 'bookName'
        },
        {
          title: '副本编号',
          key: 'copyId'
        },
      ],
      subscribeOvertimeData: [],
    }
  },
  methods: {
    removeSubscribe() {
      console.log("删除逾期预约记录")
      this.$axios.post('/apiViolation/overdue/' + this.$store.state.user.username).then((resp) => {
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            this.$message({
              message: resp.data.message,
              type: 'success'
            })
            this.getSubscribeOvertime();
          } else {
            this.$message({
              message: resp.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    getSubscribeOvertime() {
      this.$axios.get('/apiViolation/overdue/' + this.$store.state.user.username).then((res) => {
        if (res.status === 200) {
          if (res.data.rtn === 1) {
            console.log(res.data);
            this.subscribeOvertimeData = res.data.overdueList
            console.log(this.subscribeOvertimeData)
            // if (this.personBorrowData.length===0){this.personBorrowData.push("暂时没有借书记录")}
            // if (this.personSubscribeData.length===0){this.personSubscribeData.push("暂时没有借书记录")}
          } else {
            this.$message({
              message: res.data.message,
              type: 'warning'
            })
          }
        }

      })
    }
  },
  mounted() {
    this.getSubscribeOvertime();
  }
}
</script>
