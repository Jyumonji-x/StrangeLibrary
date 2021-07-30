<template>
    <div>
        <h3>我的历史</h3>
        <br/>
        <Table :columns="personHistoryColumns" :data="personHistoryData"></Table>
    </div>
</template>
<script>
    export default {
        data(){
            return{
                personHistoryColumns: [
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
                    title: '费用',
                    key: 'price'
                  },
                  {
                    title: '备注',
                    key: 'note'
                  },
                ],
                personHistoryData: [],
            }
        },
        methods:{
            getHistory() {
                this.$axios.get('/apiLog/logger/username/'+this.$store.state.user.username).then((res) => {
                    if (res.status === 200) {
                        if (res.data.rtn === 1) {
                            // bookData为当前所借书信息
                            this.personHistoryData = res.data.logs
                            for(let i =0;i<this.personHistoryData.length;i++){
                                if(this.personHistoryData[i].price===0){
                                    this.personHistoryData[i].price="免费";
                                }
                                if(this.personHistoryData[i].operatorName===null){
                                    this.personHistoryData[i].operatorName="无";
                                }
                                if(this.personHistoryData[i].branch===null){
                                    this.personHistoryData[i].branch="无";
                                }
                            }
                        } else {
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }

                })
            },
        },
        mounted() {
            this.getHistory();
        }
    }
</script>
