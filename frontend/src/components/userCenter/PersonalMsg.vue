<template>
    <div>
        <h3>个人信息</h3>
        <br/>
        <Table :columns="personMsgColumns" :data="personMsgData"></Table>
        <div v-if="this.$store.state.user.permission==='普通用户'">
            <h3>提交信用恢复申请</h3>
            <div>
                <Input v-model="creditReason" placeholder="请输入..." style="width: 300px"></Input>
                <Button @click.native="submitCredit()">提交申请</Button>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        data(){
            return{
                personMsgColumns: [
                    {
                        title: '信息名称',
                        key: 'infoKey'
                    },
                    {
                        title: '具体信息',
                        key: 'infoValue'
                    }
                ],
                personMsgData: [],
                credit:100,
                creditReason:'',
            }
        },
        methods:{
            submitCredit(){
                this.$axios.post("/apiUser/application/apply", {
                    session: this.$store.state.session,
                    reason: this.creditReason
                }).then(resp => {
                    if (resp.status === 200) {
                        if (resp.data.rtn === 1) {
                            this.$message({
                                message: resp.data.message,
                                type: 'success'
                            })
                            this.creditReason = ''
                        } else {
                            this.$message({
                                message: resp.data.message,
                                type: 'warning'
                            })
                        }
                    }
                });
            },
            getMsg() {
                let userDetails = this.$store.state.user;
                console.log(userDetails.identity);
                this.personMsgData.push({infoKey: '用户名', infoValue: userDetails.username});

                this.personMsgData.push({infoKey: '权限级别', infoValue: userDetails.permission});
                if(userDetails.permission==='普通用户'){
                    this.personMsgData.push({infoKey: '电子邮箱', infoValue: userDetails.username+"@fudan.edu.cn"});
                    this.personMsgData.push({infoKey: '用户身份', infoValue: userDetails.identity});
                }
                this.personMsgData.push({infoKey: '创建时间', infoValue: userDetails.time_create});
                this.personMsgData.push({infoKey: '最后登录时间', infoValue: userDetails.time_login});
            },
            getCredit(){
                this.$axios.get('/apiUser/credit/'+this.$store.state.user.username).then((res) => {
                    if (res.status === 200) {
                        this.personMsgData.push({infoKey: '信用分(0分无法借书，不足50分无法预约)', infoValue: res.data});
                    }
                })
            },
        },
        mounted() {
            this.getMsg();
            if(this.$store.state.user.permission==='普通用户'){
                this.getCredit();
            }
        }
    }
</script>
