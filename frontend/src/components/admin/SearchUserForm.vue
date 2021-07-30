<template>
  <div>
    <div>
      <Input v-model="search_name" placeholder="学号/工号" class="width-180"/>
      <Button type="primary" @click.native="submit()">提交</Button>
    </div>
    <div>
      <br/>
      <br/>
      <h3>用户预约情况</h3>
      <br/>
      <Table :columns="personSubscribeColumns" :data="personSubscribeData"
             @on-selection-change="selectList"></Table>
      <br/>
      <Button type="primary" @click.native="borrow()">借书</Button>
    </div>
  </div>
</template>
<script>
export default {
  data: function () {
    return {
      disabled: true,
      borrowBooks: [],
      search_name: '',
      personSubscribeColumns: [
        {
          title: '书籍名称',
          key: 'title',
        },
        {
          title: '副本编号',
          key: 'copyId'
        },
        {
          title: '副本所属图书馆',
          key: 'branch'
        },
        {
          type: 'selection',
          width: 100,
          align: 'center'
        },
      ],
      personSubscribeData: [],
      thisBorrow_name: "",
      present_name: ''
    }
  },
  methods: {
    selectList(selection) {
      this.borrowBooks = [];
      if (selection.length > 0) {
        this.disabled = false;
        for (let i = 0; i < selection.length; i++) {
          this.borrowBooks.push(selection[i].copyId);
        }
      } else {
        this.disabled = true;
      }
    },
    submit() {
      this.present_name = this.search_name;
      this.getBooks(this.present_name);
    },
    getBooks(presentName) {
      this.$axios.post('/apiBook/book/copy/userReserved/', {
        "username": presentName,
        "branch": this.$store.state.branch
      }).then((res) => {
        console.log("查询预约副本返回数据：")
        console.log(res.data)
        if (res.status === 200) {
          if (res.data.rtn === 1) {
            let borrows = res.data.copies;
            this.personSubscribeData = [];
            for (let i = 0; i < borrows.length; i++) {
              if (borrows[i].status === "预约") {
                this.personSubscribeData.push(borrows[i])
              }
            }
            this.$message({
              message: res.data.message,
              type: 'success'
            })
          } else {
            this.$message({
              message: res.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    async borrow() {
      for (let i = 0; i < this.borrowBooks.length; i++) {
        await this.$axios.post("/apiBorrow/borrow/getReserve", {
          'borrower': this.search_name,
          'session': this.$store.state.session,
          'copyId': this.borrowBooks[i],
          'branch': this.$store.state.branch
        }).then((res) => {
          console.log(this.borrowBooks[i])
          if (res.status === 200) {
            if (res.data.rtn === 1) {
              this.$message({
                message: res.data.message,
                type: 'success'
              })
              this.getBooks(this.present_name)
            } else {
              this.$message({
                message: res.data.message,
                type: 'warning'
              })
            }
          }
        })
      }

    }
  },
  watch: {
    borrowBooks: {
      handler() {
        console.log("handler函数中查看borrowBooks")
        console.log(this.borrowBooks)
      }
    }
  }
}
</script>

<style scoped>
html, body {
  height: auto;
}

.width-80 {
  width: 80px;
}

.width-180 {
  width: 180px;
}
</style>
