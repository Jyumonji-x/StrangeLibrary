<template>
    <div>
        <h2>创建新管理员</h2>
        <divider />
        <div class="content-padding">
            <AdminRegisterForm/>
        </div>
    </div>
</template>
<script>
    import AdminRegisterForm from "@/components/admin/AdminRegisterForm";

    export default {
        name: 'register',
        components: {AdminRegisterForm},
    }
</script>
