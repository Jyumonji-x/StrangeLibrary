<template>
  <div id="browse" class="opacity">
    <h2>浏览书库</h2>
    <divider />
    <BrowseForm class="content-padding"></BrowseForm>
  </div>
</template>

<script>
import BrowseForm from "@/components/bookCenter/BrowseForm"

export default {
  name: "Browse",
  components: {
    BrowseForm
  }
}
</script>

<style>

</style>
