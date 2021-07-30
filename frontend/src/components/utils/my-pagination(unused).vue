<template>
    <ul class="pagination m-auto p-2">
        <li class="page-item" v-if="!isFirstPage()">
            <router-link class="page-link" @click.native="toFirstPage()" to="">Previous</router-link>
        </li>
        <div v-for="(page) in pages" :key="page">
            <li class="page-item active" v-if="isCurrentPage(page)">
                <router-link class="page-link" @click.native="setPage(page)" to="">{{ page }}</router-link>
            </li>
            <li class="page-item" v-if="!isCurrentPage(page)">
                <router-link class="page-link" @click.native="setPage(page)" to="">{{ page }}</router-link>
            </li>
        </div>
        <li class="page-item" v-if="!isLastPage()">
            <router-link class="page-link" @click.native="toLastPage()" to="">Next</router-link>
        </li>
    </ul>
</template>

<script>
    export default {
        props:['currentPage','totalPage'],
        propsData:{
            currentPage: 1,
            totalPage: 1
        },
        data:{

        },
        name: "pageNav",
        methods:{
            isCurrentPage(i) {
                return i === this.currentPage
            },
            toLastPage() {
                this.currentPage = this.totalPage;
                this.getBook();
            },
            toFirstPage() {
                this.currentPage = 1;
                this.getBook();
            },
            setPage(i) {
                this.currentPage = i;
                this.getBook();
            },
            isFirstPage() {
                return this.currentPage === 1;

            },
            isLastPage() {
                return this.currentPage === this.totalPage;

            },
        }
    }

</script>

<style scoped>

</style>
