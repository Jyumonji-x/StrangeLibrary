<template>
    <div>
        <h3>我的借阅和预约情况</h3>
        <br/>
        <Table :columns="personBorrowColumns" :data="personBorrowData"></Table>
    </div>
</template>
<script>
    export default {
        data(){
            return{
                personBorrowColumns: [
                    {
                        title: '书籍名称',
                        key: 'title'
                    },
                    {
                        title: '副本编号',
                        key: 'copyId'
                    },
                    {
                        title: '副本状态',
                        key: 'status'
                    },
                    {
                        title: '借阅/预约时间',
                        key: 'time'
                    },
                    {
                        title: '借阅/预约时限',
                        key: 'validTime'
                    }
                ],
                personBorrowData: [],
            }
        },
        methods:{
            getBorrow() {
                this.$axios.get('/apiBorrow/borrow/get/'+this.$store.state.user.username).then((res) => {
                    if (res.status === 200) {
                        if (res.data.rtn === 1) {
                            console.log(res.data);
                            // bookData为当前所借书信息
                            this.personBorrowData = res.data.borrows
                            console.log(this.personBorrowData)
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
            },
        },
        mounted() {
            this.getBorrow();
        }
    }
</script>
