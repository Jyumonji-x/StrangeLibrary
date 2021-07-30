<template>
    <div>
        <h2>现场取预约书</h2>
        <divider />
        <div class="content-padding">
            <searchUserForm></searchUserForm>
        </div>
    </div>
</template>

<script>
    import SearchUserForm from "@/components/admin/SearchUserForm";
    export default {
        name: "SearchUser",
        components: {
            SearchUserForm
        }
    }
</script>
