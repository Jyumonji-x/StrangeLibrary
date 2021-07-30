<template>
  <div>
    <div class="content-padding">
      <el-form :inline="true" :model="searchForm" name="search" status-icon class="p-0">
        <el-form-item class="m-3">
          <el-input placeholder="搜索关键词" v-model="searchForm.searchMsg" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item class="my-3">
          <el-button type="primary" @click="getBook">搜索</el-button>
        </el-form-item>
        <Button @click.native="getAll">显示全部</Button>
      </el-form>
    </div>
    <h3 class="text-center" v-if="books.length===0">没有图书或没有搜索结果</h3>
    <List item-layout="vertical">
      <ListItem v-for="(book,index) in curPageBooks" :key="index" class="content-padding">
        <h5 style="font-size: 24px" class="m-0">{{ book.title }}</h5>
        <p style="font-size: 15px" class="m-0"><span>{{ book.author }}</span>|<span>{{ book.time_publish }}</span>
        </p>
        <p style="font-size: 15px" class="m-0" >馆藏副本：共<span class="text-muted">{{book.copy_number}}</span>本，<span
                class="text-muted">{{ book.available_number }}</span>本可借</p>
        <p style="font-size: 15px">{{ book.intro }}</p>
        <Button type="primary" @click.native="toBookDetailsPage(book.isbn)">详情</Button>
        <template slot="extra">
          <img :src="'http://121.37.153.119:9091/img/'+book.cover" class="p-2" style="width: 280px"
               alt="cover">
        </template>
        <divider />
      </ListItem>
    </List>
    <Page :current.sync="currentPage" :total="totalPage" :page-size="pageSize" show-total class="paging m-3" @on-change="changePage"/>
  </div>
</template>
<script>
  export default {
    data: function () {
      return {
        searchForm: {
          searchMsg: ''
        },
        searchingMsg: '',
        count: 1,
        books: [{}],
        currentPage:1,
        totalPage:0,
        pageSize:2,
        curPageBooks:[{}]
    }
  },
  methods: {
    toBookDetailsPage(isbn) {
      this.$router.push({path: '/bookDetails', query: {isbn: isbn}});
    },
    search() {
      this.searchingMsg = this.searchForm.searchMsg;
      this.changePage(1);
    },
    getBook() {
      if(this.searchForm.searchMsg===""){
        this.$message({
          message: "请输入关键词！",
          type: 'warning'
        })
        return;
      }
      this.$axios.post("/apiBook/book/get/title/"+this.searchForm.searchMsg)
          .then(resp => {
            this.books = resp.data.books;
        //    this.count = resp.data.books.length;
            console.log(resp)
          })
    },
    changePage(index){
      let _start = ( index - 1 ) * this.pageSize;
      let _end = index * this.pageSize;
      this.curPageBooks = this.books.slice(_start,_end);
    },
    getAll() {
      this.$axios.post('/apiBook/book/get/browse').then((resp) => {
        this.books = resp.data.books
        //this.count = resp.data.books.length
        //this.totalPage = Math.ceil(resp.data.count / this.pageSize)
        this.totalPage = this.books.length;
        console.log(this.books);
      })
    },
  },
  mounted() {

  this.getAll();
  this.changePage(1);
  },
    watch:{
      books:{
        handler(){
          this.changePage(1);
          this.totalPage = this.books.length;
        }
      }
    }
}
</script>

<style scoped>
html, body {
  height: auto;
}
</style>
