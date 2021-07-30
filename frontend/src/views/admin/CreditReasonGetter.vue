<template>
    <div>
        <h3>审批恢复信用分请求</h3>
        <divider />
      <Table :columns="reasonColumns" :data="curPageReasons"></Table>
<!--        <List style="margin-left: 20px;" item-layout="vertical">-->
<!--            <ListItem v-for="reason in curPageReasons" :key="reason.username" class="content-padding">-->
<!--                        <p style="font-size: 15px">{{ reason.username}}</p>-->
<!--                        <p style="font-size: 16px;margin-left: 10px;" class="m-0">{{ reason.reason}}</p>-->
<!--                        <p style="font-size: 13px;color:gray" class="m-0">{{ reason.time}}</p>-->
<!--                    <Button type="primary" @click.native="apply(reason)">通过申请</Button>-->
<!--                <br /><br />-->
<!--            </ListItem>-->
<!--        </List>-->
        <Page v-if="this.reasons.length!==0" :total="totalPage" :page-size="pageSize" show-total class="paging m-3" @on-change="changePage"/>
    </div>
</template>
<script>
    export default {
        data(){
            return{
              reasonColumns: [
                {
                  title: '申请人',
                  key: 'username'
                },
                {
                  title: '申请理由',
                  key: 'reason'
                },
                {
                  title: '时间',
                  key: 'time'
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
                            this.apply(params.row)
                          }
                        }
                      }, '同意申请')
                    ]);
                  }
                }
              ],
                curPageReasons:[],
                totalPage:0,
                pageSize:5,
                reasons:[]
            }
        },
        methods: {
            apply(approve){
                this.$axios.post("/apiUser/application/approve",{
                    session:this.$store.state.session,
                    username:approve.username
                }).then((res) => {
                    if (res.status === 200) {
                        if (res.data.rtn === 1) {
                            this.$message({
                                message: res.data.message,
                                type: 'success'
                            })
                            this.getReasons();
                        } else {
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
            },
            getReasons() {
                this.$axios.get("/apiUser/application/").then((res) => {
                    if (res.status === 200) {
                            console.log(res.data)
                            this.reasons = res.data;
                    }
                })
            },
            changePage(index){
                let _start = ( index - 1 ) * this.pageSize;
                let _end = index * this.pageSize;
                this.curPageReasons = this.reasons.slice(_start,_end);
            },
        },
        mounted() {
            this.getReasons();
        },
        watch:{
            reasons:{
                handler(){
                    this.totalPage = this.reasons.length;
                    this.changePage(1);
                }
            }
        }
    }
</script>
