<template>
  <div>
    <h2>系统记录</h2>
    <divider/>
    <h3>群发提醒</h3>
    <Button @click.native="sendMessage">群发</Button>
    <br />
    <h3>历史记录</h3>
    <div>
      <Select v-model="searchMode" style="width:200px">
        <Option v-for="i in searchModeList" :value="i.value" :key="i.value">{{ i.label }}</Option>
      </Select>
      <Input v-model="searchValue" placeholder="请输入..." style="width: 300px"></Input>
      <Button type="primary" @click.native="search()">搜索</Button>
      <Button type="primary" @click.native="getAllHistory()">显示全部</Button>
      <p v-if="this.historyData.length===0">无结果！</p>
      <Table v-if="!this.historyData.length!==0" :columns="historyColumns" :data="historyData"></Table>
    </div>
  </div>
</template>
<script>
export default {
  data(){
    return {
      searchModeList: [
        {
          value: 'byUser',
          label: '按用户'
        },
        {
          value: 'byDuplicate',
          label: '按副本序号'
        }
      ],
      searchMode: '',
      searchValue: '',
      historyColumns:[
        {
          title: 'ID',
          key: 'id'
        },
        {
          title: '书名',
          key: 'title'
        },
        {
          title: '分馆',
          key: 'branch'
        },
        {
          title: '分类',
          key: 'category'
        },
        {
          title: '副本号',
          key: 'copyId'
        },
        {
          title: '操作人',
          key: 'operator'
        },
        {
          title: '时间',
          key: 'time'
        },
        {
          title: '用户',
          key: 'username'
        },
        {
          title: '费用',
          key: 'price'
        },
        {
          title: '备注',
          key: 'note'
        },
      ],
      historyData:[]
    }
  },
  methods: {
    sendMessage() {
      this.$axios.post("/apiViolation/broadcast").then(resp => {
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
      });
    },
    search(){
      if(this.searchValue===''){
        this.$message({
          message: "请输入搜索内容",
          type: 'warning'
        })
        return;
      }
      if(this.searchMode==='byUser'){
        this.searchByUser(this.searchValue);
      }else if(this.searchMode==='byDuplicate'){
        this.searchByDuplicateId(this.searchValue);
      }else{
        this.$message({
          message: "请选择搜索方式",
          type: 'warning'
        })
      }
    },
    searchByUser(userName){
      this.$axios.get("/apiLog/logger/username/"+userName).then((resp)=>{
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            console.log(resp.data.logs);
            this.historyData = resp.data.logs;
            for(let i =0;i<this.historyData.length;i++){
              if(this.historyData[i].price===0){
                this.historyData[i].price="免费";
              }
              if(this.historyData[i].operatorName===null){
                this.historyData[i].operatorName="无";
              }
            }
          } else {
            this.$message({
              message: resp.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    searchByDuplicateId(id){
      this.$axios.get("/apiLog/logger/copyId/"+id).then((resp)=>{
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            console.log(resp.data);
            this.historyData = resp.data.logs;
            for(let i =0;i<this.historyData.length;i++){
              if(this.historyData[i].price===0){
                this.historyData[i].price="免费";
              }
              if(this.historyData[i].operatorName===null){
                this.historyData[i].operatorName="无";
              }
            }
          } else {
            this.$message({
              message: resp.data.message,
              type: 'warning'
            })
          }
        }
      })
    },
    getAllHistory(){
      this.$axios.get("/apiLog/logger/getAll/").then((resp)=>{
        if (resp.status === 200) {
          if (resp.data.rtn === 1) {
            console.log(resp.data);
            this.historyData = resp.data.logs;
            for(let i =0;i<this.historyData.length;i++){
              if(this.historyData[i].price===0){
                this.historyData[i].price="免费";
              }
              if(this.historyData[i].operatorName===null){
                this.historyData[i].operatorName="无";
              }
            }
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
    this.getAllHistory();
  },
  watch:{
    searchMode:{
      handler(){
        console.log(this.searchMode);
      }
    }
  }
}
</script>
