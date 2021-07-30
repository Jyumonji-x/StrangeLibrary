<template>
    <div>
    <h2>支付罚款</h2>
    <divider/>
        <div v-if="priceAll===0">您没有未支付的罚款！</div>
        <div v-if="priceAll!==0">
            <Table :columns="fineColumns" :data="fineData"></Table>
            <Button @click.native="payFine()">支付以上罚款</Button>
        </div>
    </div>

</template>
<script>
    export default {
        data(){
            return{
                priceAll : 0,
                fineColumns: [
                    {
                        title: '书籍名称',
                        key: 'bookName'
                    },
                    {
                        title: '副本编号',
                        key: 'copyId'
                    },
                    {
                        title: '价格',
                        key: 'price'
                    }
                ],
                fineData:[]
            }
        },
        methods:{
            payFine(){
                this.$axios.post("/apiViolation/payment/fine/"+this.$store.state.user.username).then(resp=>{
                    if (resp.status === 200) {
                        console.log(resp);
                        if (resp.data.rtn === 1) {
                            this.$message({
                                message: resp.data.message,
                                type: 'success'
                            })
                          this.initialize();
                        }else{
                            this.$message({
                                message: resp.data.message,
                                type: 'warning'
                            })
                            return false;
                        }

                    }
                })
            },
            initialize(){
                this.$axios.get("/apiViolation/payment/fine/"+this.$store.state.user.username).then(resp=>{
                    if (resp.status === 200) {
                        console.log(resp.data);
                        if (resp.data.rtn === 1) {
                            this.priceAll = resp.data.priceAll;
                            this.fineData = resp.data.fineRecords;
                        }
                    }
                })}
        },
        mounted() {
            this.initialize();
        }
    }
</script>
