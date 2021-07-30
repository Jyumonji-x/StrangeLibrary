<template>
    <div>
        <h3>输入评论</h3>
        <br />

        <Form ref="commentData" :model="commentData" :rules="rules">
            <h4>您的评分</h4>
            <Rate v-model="value"></Rate>
            <Form-item>
                <Input v-model="commentData.textarea" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入评论"></Input>
            </Form-item>
            <Form-item>
                <Button type="primary" @click.native="submitComment()">提交</Button>
            </Form-item>
        </Form>
    </div>
</template>
<script>
    export default {
        data () {
            const validateText = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入评论'));
                } else {
                    callback();
                }
            };
            return {
                commentData: {
                    textarea: '',
                    value : 0
                },
                rules: {
                    textarea: [{
                        validator: validateText,
                        required: true,
                        trigger: 'blur'
                    }],
                },
            }
        },
        methods:{
            submitComment(){
                    console.log("submit")
                    this.$refs["commentData"].validate(valid =>{
                        if (valid) {
                            console.log("ISBN:"+this.$route.query.isbn +"   user："+this.$store.state.user.username+"    comment："+ this.commentData.textarea+"   rate:"+this.commentData.value);
                        }
                    })
            }
        }
    }
</script>
