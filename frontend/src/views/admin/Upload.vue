<template>
    <div>
        <h2>上传书籍信息</h2>
        <divider />
        <BookUpload class="content-padding"/>
    </div>
</template>
<script>
    import BookUpload from "@/components/admin/BookUpload"
    export default {
        name: "Upload",
        components:{
            BookUpload,
        }
    }
</script>
