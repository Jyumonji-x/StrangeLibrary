<template>
    <div>
        <h2>密码修改</h2>
        <divider />
        <div class="content-padding">
            <PassModifyForm></PassModifyForm>
        </div>
    </div>
</template>
<script>
    import PassModifyForm from "@/components/userCenter/PassModifyForm";
    export default {
        components: {PassModifyForm},
    }
</script>
