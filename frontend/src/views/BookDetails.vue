<template>
  <div>
    <h2>书籍详情</h2>
    <List item-layout="vertical">
      <ListItem>
        <h5 style="font-size: 20px">{{ this.book.title }}</h5>
        <p style="font-size: 15px"><span>{{ this.book.author }}</span>|<span>{{ this.book.time_publish }}</span></p>
        <p style="font-size: 15px" class="m-0">馆藏副本：共<span class="text-muted">{{
            book.copy_number
          }}</span>本，<span
            class="text-muted">{{ book.available_number }}</span>本可借</p>
        <div v-if="currentRater===0">
          暂无评分
        </div>
        <div v-if="currentRater!==0">
        <Rate v-model="currentRate" disabled show-text>
          <span style="color: #f5a623">{{ currentRate}}(共{{currentRater}}人评分)</span>
        </Rate>
        </div>
        <p style="font-size: 15px">{{ this.book.intro }}</p>
        <divider />
        <template slot="extra">
          <img :src="'http://121.37.153.119:9091/img/'+book.cover" class="p-2" style="width: 280px" alt="cover">
        </template>
      </ListItem>
    </List>
    <Table v-if="($store.state.session && $store.state.user.permission==='普通用户')" :columns="duplicatesColumns"
           :data="duplicatesData">
    </Table>
    <Table v-if="(!$store.state.session)||($store.state.session && ($store.state.user.permission==='管理员'||$store.state.user.permission==='超级管理员'))"
           :columns="duplicatesColumnsWithoutSubscribe" :data="duplicatesData">
    </Table>
    <br/>
    <br/>
    <div v-if="$store.state.session&&($store.state.user.permission==='管理员'||$store.state.user.permission==='超级管理员')">
      <Select v-model="branch" style="width:200px">
        <Option v-for="item in branchList" :value="item.value" :key="item.value">{{ item.value }}</Option>
      </Select>
      <InputNumber :max="20" :min="1" v-model="duplicateNum"></InputNumber>
      <Button type="primary" @click.native="addDuplicates()">添加副本</Button>
    </div>
    <br />
    <divider />
    <Comments></Comments>
  </div>
</template>
<script>
import Comments from "../components/bookCenter/Comments";
  export default {
  components:{
    Comments
  },
  data() {
    return {
      currentRate:0,
      currentRater:1,
      book: [],
      duplicatesColumns: [
        {
          title: '副本编号',
          key: 'copyId'
        },
        {
          title: 'ISBN版号',
          key: 'isbn'
        },
        {
          title: '副本所属图书馆',
          key: 'branch'
        },
        {
          title: '借阅情况',
          key: 'status'
        },
        {
          title: '借阅/预约者用户名',
          key: 'borrower'
        },
        {
          title: 'Action',
          key: 'action',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {type: 'primary', size: 'small'},
                on: {
                  click: () => {
                    this.subscribe(params.row.isbn, params.row.copyId)
                  }
                }
              }, '预约')
            ]);
          }
        }
      ],
      duplicatesColumnsWithoutSubscribe: [
        {
          title: '副本编号',
          key: 'copyId'
        },
        {
          title: 'ISBN版号',
          key: 'isbn'
        },
        {
          title: '副本所属图书馆',
          key: 'branch'
        },
        {
          title: '借阅情况',
          key: 'status'
        },
        {
          title: '借阅/预约者用户名',
          key: 'borrower'
        }
      ],
      duplicatesData: [],
      duplicateNum: 1,
      branch: this.$store.state.branch,
      branchList: [
        {
          value: '邯郸'
        },
        {
          value: '江湾'
        },
        {
          value: '枫林'
        },
        {
          value: '张江'
        },
      ]
    }
  },
  methods: {
    getBook() {
      this.$axios.post('/apiBook/book/get/ISBN/'+this.$route.query.isbn).then((res) => {
        if (res.status === 200) {
          if (res.data.rtn === 1) {
            console.log(res.data);
            this.book = res.data.book;
          } else {
            this.$message({
              message: res.data.message,
              type: 'warning'
            })
          }
        }
      });
    },
    getDuplicates() {
      console.log("getDuplicates");
      this.$axios.get('/apiBook/book/copy/getCopyByIsbn/'+this.$route.query.isbn).then((res) => {
        if (res.status === 200) {
          console.log(res.data)
          if (res.data.rtn === 1) {
            console.log("副本信息返回数据：")
            console.log(res.data)
            this.duplicatesData = res.data.copies
          } else {
            this.$message({
              message: res.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    subscribe(ISBN, duplicate_id) {
      this.$axios.post("/apiBorrow/borrow/reserve", {
        session: this.$store.state.session,
        copyId: duplicate_id
      }).then((res) => {
        if (res.status === 200) {
          if (res.data.rtn === 1) {
            console.log("预约请求返回数据：")
            console.log(res.data)
            this.$message({
              message: res.data.message,
              type: 'success'
            })
            this.getDuplicates();
          } else {
            this.$message({
              message: res.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    addDuplicates() {
      this.$axios.post("/apiBook/book/copy/add/", {
        ISBN: this.$route.query.isbn,
        location: this.branch,
        number: this.duplicateNum,
        session: this.$store.state.session
      }).then((resp) => {
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            this.$message({
              message: resp.data.message,
              type: 'success'
            })
            this.getDuplicates();
          } else {
            this.$message({
              message: resp.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    getRate(){
      this.$axios.get("/apiComment/comment/isbn/"+this.$route.query.isbn).then((res)=>{
        if(res.status===200){
          if(res.data.rtn===1){
            console.log(res.data);
            let comments = res.data.comments;
            let amount = 0;
            this.currentRater = 0;
            for(let i =0; i<comments.length;i++){
              if(comments[i].status==='已评论'){
                amount += comments[i].rate;
                this.currentRater++;
              }
            }
            this.currentRate = amount/this.currentRater;
          }else{
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
    this.getBook();
    this.getDuplicates();
    this.getRate();
  },
}
</script>
