<template>
    <div>
        <h2>我的评论管理</h2>
        <divider />
        <br/>
        <Table :columns="personCommentColumns" :data="curPageCommentData"></Table>
        <Page :current.sync="currentPage" :total="totalPage" :page-size="pageSize" show-total class="paging m-3" @on-change="changePage"/>
        <!--        Modal为修改评论区域-->
        <Modal
                v-model="modal"
                title="我的评论"
                :mask-closable="false"
                :styles="{top: '20px'}"
        >
            <p></p>
            <h3>添加/修改评论</h3>
            <divider />
            <p style="font-size: 17px">书名：{{this.curCommentBook.bookName}} </p>
            <br />
            <Input v-model="curCommentBook.title" placeholder="请输入评论标题"></Input>
            <br /><br />
            <Rate v-model="curCommentBook.rate"></Rate>
            <Input v-model="curCommentBook.comment" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入评论"></Input>
            <Button @click.native="submitComment()">提交评论</Button>
            <div slot="footer"></div>
        </Modal>
    </div>
</template>
<script>
    export default {
        data(){
            return{
                currentPage:1,
                totalPage:0,
                pageSize:2,
                curCommentBook:{},
                modal: false,
                personCommentColumns: [
                    {
                        title: 'id',
                        key: 'id'
                    },
                    {
                        title: 'ISBN',
                        key: 'isbn'
                    },
                    {
                        title: '书名',
                        key: 'bookName'
                    },
                    {
                        title: '状态',
                        key: 'status'
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
                                            this.modifyComment(params.row)
                                        }
                                    }
                                }, params.row.status==='已评论' ?'评论修改':'添加评论')
                            ]);
                        }
                    },
                    {
                        title: 'Action',
                        key: 'action',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {type: 'warning', size: 'small'},
                                    on: {
                                        click: () => {
                                            this.deleteComment(params.row)
                                        }
                                    }
                                }, params.row.status==='已评论' ?'删除评论':'放弃评论')
                            ]);
                        }
                    }
                ],
                curPageCommentData:[{}],
                personCommentData: [{}],
            }
        },
        methods:{
            modifyComment(book){
                this.curCommentBook = book;
                console.log("修改评论，获取当前欲修改的书籍评论")
                console.log(this.curCommentBook);
                this.modal = true;
            },
            getComments(){
                // this.personCommentData= [
                //     {
                //         index:123,
                //         isbn:"1234567890",
                //         rate:5,
                //         bookName:"书名1",
                //         commented:true,
                //         status: "已评论",
                //         time: "2020-10-30 13:42",
                //         title: "hello",
                //         comment:"hello comment"
                //     },
                //     {
                //         index:124,
                //         rate:4,
                //         isbn:"1234567891",
                //         bookName:"书名25",
                //         commented:false,
                //         status: "未评论",
                //         time: "2020-10-30 13:42",
                //         title: "hello",
                //         comment:""
                //     },
                //     {
                //         index:126,
                //         rate:3,
                //         isbn:"1234567591",
                //         bookName:"书名62",
                //         commented:false,
                //         status: "未评论",
                //         time: "2020-10-30 13:42",
                //         title: "hello",
                //         comment:""
                //     },
                //     {
                //         index:128,
                //         rate:4,
                //         isbn:"1234563891",
                //         bookName:"书名3452",
                //         commented:false,
                //         status: "未评论",
                //         time: "2020-10-30 13:42",
                //         title: "hello",
                //         comment:""
                //     },
                //     {
                //         index:135,
                //         rate:5,
                //         isbn:"1234567791",
                //         bookName:"书名2",
                //         commented:false,
                //         status: "未评论",
                //         time: "2020-10-30 13:42",
                //         title: "hello",
                //         comment:""
                //     }
                // ];
                this.$axios.get("/apiComment/comment/getBySession/"+this.$store.state.session).then((res)=>{
                    if (res.status === 200) {
                        if(res.data.rtn===1){
                            this.personCommentData = res.data.comments;
                            console.log(this.personCommentData);
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
                this.totalPage = this.personCommentData.length;
                console.log("获取所有评论，前端生成了静态评论信息，用户名："+this.$store.state.user.username);
                console.log(this.personCommentData);
            },
            submitComment(){
                if(this.curCommentBook.title===''){
                    this.$message({
                        message: "请输入评论标题！",
                        type: 'warning'
                    })
                    return;
                }
                if(this.curCommentBook.comment===''){
                    this.$message({
                        message: "请输入评论内容！",
                        type: 'warning'
                    })
                    return;
                }
                console.log(this.curCommentBook);
                console.log("提交评论");
                console.log("username:" +this.$store.state.user.username+"  isbn:"+this.curCommentBook.isbn+"  title:"+this.curCommentBook.title+"  comment:"+this.curCommentBook.comment);
                this.$axios.post("/apiComment/comment/make/",{
                    id: this.curCommentBook.id,
                    rate: this.curCommentBook.rate,
                    title: this.curCommentBook.title,
                    comment: this.curCommentBook.comment
                }).then((res)=>{
                    if(res.status===200){
                        if(res.data.rtn===1){
                            this.$message({
                                message: res.data.message,
                                type: 'success'
                            })
                          this.getComments();
                          this.changePage(1);
                          this.currentPage = 1;
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
                this.modal = false;
                this.getComments();
            },
            deleteComment(book){
                console.log("删除评论");
                console.log("username: "+this.$store.state.user.username+"  isbn:"+book.isbn);
                this.$axios.post("/apiComment/comment/deleteComment/",{
                    session: this.$store.state.session,
                    id: book.id,
                }).then((res)=>{
                    if(res.status===200){
                        if(res.data.rtn===1){
                            this.$message({
                                message: res.data.message,
                                type: 'success'
                            })
                          this.getComments();
                          this.changePage(1);
                          this.currentPage = 1;
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })

            },
            changePage(index){
                let _start = ( index - 1 ) * this.pageSize;
                let _end = index * this.pageSize;
                this.curPageCommentData = this.personCommentData.slice(_start,_end);
            },
    },
            mounted() {
                this.getComments();
                this.changePage(1);
            },
            watch:{
                personCommentData:{
                    handler(){
                        this.changePage(1);
                    }
                }
            }
        }
</script>

