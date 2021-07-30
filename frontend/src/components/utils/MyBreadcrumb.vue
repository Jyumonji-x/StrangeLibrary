<template>
    <Breadcrumb :style="{margin: '16px 0'}">
        <BreadcrumbItem>StrangeLibrary</BreadcrumbItem>
        <BreadcrumbItem v-for='(name,index) in matchedArr'
                        :key='index'
        >
            {{name}}
        </BreadcrumbItem>
    </Breadcrumb>
</template>
<script>
    export default {
        computed: {
            matchedArr() {
                let temp = [], temps = [];
                // eslint-disable-next-line no-unused-vars
                this.$route.matched.filter((item, index, self) => {
                    if (item.name) {
                        const name = item.name;
                        temp.push(name);
                    }
                });
                // eslint-disable-next-line no-unused-vars
                temp.filter((item, index, self) => {
                    if (!temps.includes(item)) {
                        temps.push(item);
                        return true;
                    }
                    return false;
                })
                return temps;
            }
        }
    }
</script>
