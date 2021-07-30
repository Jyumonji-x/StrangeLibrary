<template>

  <div>
    <h2>现场还书</h2>
    <divider/>
    <div class="content-padding">
      <Input v-model="add_duplicate" placeholder="ISBN-XXX" style="width: 180px"/>
      <Select v-model="returnMode" style="width:200px">
        <Option v-for="i in returnModeList" :value="i.value" :key="i.value">{{ i.value }}</Option>
      </Select>
      <Button @click.native="push()">添加</Button>
      <br/> <br/>
      <Table :columns="duplicatesColumns" :data="duplicatesData"></Table>
      <br/><br/>
      <Button type="primary" @click.native="submit()">提交</Button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      returnModeList: [
        {
          value: '完好'
        },
        {
          value: '遗失'
        },
        {
          value: '损坏'
        },
      ],
      returnMode: '完好',
      borrow_id: '',
      duplicatesColumns: [
        {
          title: '所还书编号',
          key: 'duplicate_key'
        },
        {
          title: '状态',
          key: 'status'
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
                    this.duplicatesData.splice(params.index, 1);
                  }
                }
              }, '删除')
            ]);
          }
        }
      ],
      duplicatesData: [],
      add_duplicate: ''
    }
  },
  methods: {
    async submit() {
      let n = 0;
      for (let i = 0; i < this.duplicatesData.length; i++) {
        await this.$axios.post("/apiBorrow/borrow/onsiteReturn", {
          copyId: this.duplicatesData[i].duplicate_key,
          session: this.$store.state.session,
          branch: this.$store.state.branch,
          status: this.duplicatesData[i].status
        }).then((res) => {
          if (res.status === 200) {
            if (res.data.rtn === 1) {
              this.$message({
                message: res.data.message,
                type: 'success'
              })
              console.log(this.duplicatesData);
              console.log("splice");
              this.duplicatesData.splice(i - n, 1);
              n++;
              console.log(this.duplicatesData);
              i--;
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
      this.duplicatesData.push({duplicate_key: this.add_duplicate, status: this.returnMode});
    }
  }
}
</script>
