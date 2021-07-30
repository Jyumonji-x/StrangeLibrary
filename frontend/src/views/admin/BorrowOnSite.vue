<template>
  <div>
    <h2>现场借书</h2>
    <divider/>
    <div class="content-padding">
      <Input v-model="add_duplicate" placeholder="ISBN-XXX" style="width: 180px"/>
      <Button @click.native="push()">添加</Button>
      <br/> <br/>
      <Table :columns="duplicatesColumns" :data="duplicatesData"></Table>
      <br/>
      <br/>
      <Input v-model="borrow_id" placeholder="借书人学号/工号" style="width: 180px" required/>
      <Button type="primary" @click.native="submit()">提交</Button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      duplicatesColumns: [
        {
          title: '所借书编号',
          key: 'duplicate_key'
        },
        {
          title: 'Delete',
          key: 'delete',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {type: 'primary', size: 'small'},
                on: {
                  click: () => {
                    this.delete(params.index);
                  }
                }
              }, '删除')
            ]);
          }
        }
      ],
      duplicatesData: [],
      borrow_id: '',
      add_duplicate: ''
    }
  },
  methods: {
    async submit() {
      for (let i = 0; i < this.duplicatesData.length; i++) {
        await this.$axios.post("/apiBorrow/borrow/onsiteBorrow", {
          borrower: this.borrow_id,
          copyId: this.duplicatesData[i].duplicate_key,
          session: this.$store.state.session,
          branch: this.$store.state.branch
        }).then((res) => {
          if (res.status === 200) {
            if (res.data.rtn === 1) {
              this.$message({
                message: res.data.message,
                type: 'success'
              })
              this.duplicatesData.splice(i--, 1);
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
    delete(index) {
      this.duplicatesData.splice(index, 1);
    },
    push() {
      let checkDupKey = /^[0-9]+[-][0-9]+$/ //检测长度
      if (!checkDupKey.test(this.add_duplicate)) {
        this.$message({
          message: '编号格式不正确',
          type: 'warning'
        })
        return;
      }
      for (let i = 0; i < this.duplicatesData.length; i++) {
        if (this.add_duplicate === this.duplicatesData[i].duplicate_key) {
          this.$message({
            message: '副本编号重复',
            type: 'warning'
          })
          return;
        }
      }
      this.duplicatesData.push({duplicate_key: this.add_duplicate});
    }
  }
}
</script>
