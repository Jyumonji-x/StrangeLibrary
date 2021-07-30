<template>
    <div>
        <h2>评论区</h2>
        <div v-if="this.comments.length===0">
            <br /><p>暂无评论</p>
        </div>
    <List style="margin-left: 20px;" item-layout="vertical">
        <ListItem v-for="comment in curPageComments" :key="comment.id" class="content-padding">
            <div v-if="comment.hidden">
                <br />
                <p style="font-size: 17px;color: gray" >此评论已被管理员隐藏</p>
                <br />
                <div v-if="$store.state.session && ($store.state.user.permission==='管理员'||$store.state.user.permission==='超级管理员')">
                    <p style="font-size: 13px">原内容：</p>
                    <br />
                    <p style="font-size: 23px">{{ comment.title}}</p>
                    <p style="font-size: 14px;margin-left: 10px;" class="m-0">{{ comment.username}}</p>
                    <Rate v-model="comment.rate" disabled show-text>
                        <span style="color: #f5a623">{{ comment.rate}}</span>
                    </Rate>
                    <p style="font-size: 18px;margin-left: 10px;" class="m-0">{{ comment.comment}}</p>
                    <p style="font-size: 13px;color:gray" class="m-0">{{ comment.time}}</p>
                    <Button @click.native="reshowComment(comment)" style="margin-left: 10px;" type="warning" v-if="$store.state.session && ($store.state.user.permission==='管理员'||$store.state.user.permission==='超级管理员') && comment.hidden" >重显此评论</Button>
                    <br /><br />
                </div>
            </div>
            <div v-if="!comment.hidden">
                <br />
                <p style="font-size: 23px">{{ comment.title}}</p>
                <p style="font-size: 14px;margin-left: 10px;" class="m-0">{{ comment.username}}</p>
                <Rate v-model="comment.rate" disabled show-text>
                    <span style="color: #f5a623">{{ comment.rate}}</span>
                </Rate>
                <p style="font-size: 18px;margin-left: 10px;" class="m-0">{{ comment.comment}}</p>
                <p style="font-size: 13px;color:gray" class="m-0">{{ comment.time}}</p>
                <Button v-if="comment.discussions===undefined" @click.native="getCommentView(comment)">讨论区: 0</Button>
                <Button v-if="comment.discussions!==undefined" @click.native="getCommentView(comment)">讨论区: {{comment.discussions.length}}</Button>
                <Button @click.native="hideComment(comment)" style="margin-left: 10px;" type="warning" v-if="$store.state.session && ($store.state.user.permission==='管理员'||$store.state.user.permission==='超级管理员') && !comment.hidden">隐藏此评论</Button>
                <br /><br />
            </div>
        </ListItem>
        <!--        Modal为讨论板区域-->
        <Modal
                v-model="modal"
                title="讨论版"
                :mask-closable="false"
                :styles="{top: '20px'}"
                >
            <div style="margin-left: 10px;">
            <p style="font-size: 25px">{{ curComment.title}}</p>
                <p style="font-size: 13px" class="m-0">{{ curComment.username}}</p>
                <Rate v-model="curComment.rate" disabled show-text>
                    <span style="color: #f5a623">{{ curComment.rate}}</span>
                </Rate>
                <p style="font-size: 17px" class="m-0">{{ curComment.comment}}</p>
                <p style="font-size: 13px;color:gray" class="m-0">{{ curComment.time}}</p>
            </div>
            <divider />
            <h3>讨论区</h3>
            <divider />
            <div style="margin-left: 10px;">
            <div v-if="curComment.discussions===undefined||curComment.discussions.length ===0">
                <p style="font-size: 17px">暂无相关讨论</p>
                <divider />
            </div>

            <ListItem v-for="discuss in curComment.discussions" :key="discuss.id" class="content-padding">
                <p style="font-size: 13px" class="m-0">{{ discuss.username}}</p>
                <p style="font-size: 17px" class="m-0">{{ discuss.content}}</p>
                <p style="font-size: 13px;color:gray" class="m-0">{{ discuss.time}}</p>
                <Button @click.native="deleteCommentDiscussion(discuss)" v-if="$store.state.session&&$store.state.user.username===discuss.username">删除我的评论</Button>
                <divider />
            </ListItem>
            </div>
            <h3>参与讨论</h3>
            <divider />
            <div style="margin-left: 10px;">
            <br />
                <div v-if="!$store.state.session">
                    <p>登录以参与讨论</p>
                </div>
                <div v-if="$store.state.session">
                    <Input ref="textarea" v-model="discussData.textarea" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入评论"></Input>
                    <br /><br />
                    <Button type="primary" @click.native="submitDiscuss()">提交</Button>
                </div>
            </div>
            <div slot="footer"></div>
        </Modal>
    </List>
        <Page v-if="this.comments.length!==0" :total="totalPage" :page-size="pageSize" show-total class="paging m-3" @on-change="changePage"/>
    </div>
</template>
<script>
    export default {
        data(){
            return {
                totalPage:0,
                pageSize:4,
                discussData: {
                    textarea: '',
                    value : 0
                },
                modal: false,
                curComment: {},
                curPageComments:[{}],
                comments:[{}]
            }
        },
        methods:{
          deleteCommentDiscussion(discuss){
            this.$axios.post("/apiComment/discussion/delete/",{
              session:this.$store.state.session,
              id:discuss.id
            }).then((res)=>{
              if(res.status===200){
                if(res.data.rtn===1){
                  this.$message({
                    message: res.data.message,
                    type: 'success'
                  })
                  this.getComments();
                  this.modal=false;
                }else{
                  this.$message({
                    message: res.data.message,
                    type: 'warning'
                  })
                }
              }
            })
          },
            reshowComment(comment){
                console.log("管理员设置不再隐藏评论：")
                console.log("index: "+comment.id+"  isbn:"+this.$route.query.isbn);
                this.$axios.post("/apiComment/comment/reshowComment/",{
                    session:this.$store.state.session,
                    id:comment.id
                }).then((res)=>{
                    if(res.status===200){
                        if(res.data.rtn===1){
                            comment.hidden = false;
                            this.$message({
                                message: res.data.message,
                                type: 'success'
                            })
                          this.getComments();
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
            },
            hideComment(comment){
                console.log("管理员设置隐藏评论：")
                console.log("index: "+comment.id+"  isbn:"+this.$route.query.isbn);
                this.$axios.post("/apiComment/comment/hideComment/",{
                    session:this.$store.state.session,
                    id:comment.id
                }).then((res)=>{
                    if(res.status===200){
                        if(res.data.rtn===1){
                            comment.hidden = true;
                            this.$message({
                                message: res.data.message,
                                type: 'success'
                            })
                          this.getComments();
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
            },
            getCommentView(comment){
                this.modal = true;
                this.curComment = comment;
                console.log("显示当前评论的相关讨论：");
                console.log(this.curComment);
            },
            submitDiscuss(){
                if(this.discussData.textarea===''){
                    this.$message({
                        message: '请输入讨论内容！',
                        type: 'warning'
                    })
                    return;
                }
                this.$axios.post("/apiComment/discussion/release/",{
                    session:this.$store.state.session,
                    commentId:this.curComment.id,
                    content:this.discussData.textarea
                }).then((res)=>{
                    if(res.status===200){
                        if(res.data.rtn===1){
                            this.$message({
                                message: res.data.message,
                                type: 'success'
                            })
                            this.getComments();
                            this.modal = false;
                            this.discussData.textarea ='';
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
                console.log("提交讨论内容")
                console.log("ISBN:"+this.$route.query.isbn +"    curCommentIndex:"+this.curComment.id+"   user："+this.$store.state.user.username+"    discuss："+ this.discussData.textarea);

            },
            getComments(){
                this.$axios.get("/apiComment/comment/isbn/"+this.$route.query.isbn).then((res)=>{
                    if(res.status===200){
                        if(res.data.rtn===1){
                            console.log(res.data);
                            if(res.data.comments.length===0) this.comments = [];
                            else this.comments = res.data.comments;
                            this.changePage(1);
                        }else{
                            this.$message({
                                message: res.data.message,
                                type: 'warning'
                            })
                        }
                    }
                })
                console.log("获取当前书籍的评论，前端生成了静态内容，isbn:"+this.$route.query.isbn);
                console.log(this.comments);
            },
            changePage(index){
                let _start = ( index - 1 ) * this.pageSize;
                let _end = index * this.pageSize;
                this.curPageComments = this.comments.slice(_start,_end);
            },
        },
        watch:{
            modal:{
                handler(){
                    if(!this.modal){
                        this.discussData.textarea =''
                    }
                }
            },
            comments:{
                    handler(){
                        this.totalPage = this.comments.length;
                        this.changePage(1);
                    }
            }
        },
        mounted() {
            this.getComments();
        }
    }
</script>
