<template>
  <div id="personal" class="opacity">
    <h2>个人中心</h2>
    <divider/>
    <div class="content-padding">
      <PersonalMsg></PersonalMsg>
      <br/><br/>
      <div v-if="$store.state.session && $store.state.user.permission==='普通用户'">
        <PersonalBorrow></PersonalBorrow>
        <br/><br/>
        <PersonalSubscribeOvertime></PersonalSubscribeOvertime>
        <PersonalHistory></PersonalHistory>
        <br/><br/>
      </div>
    </div>
  </div>
</template>

<script>
  import PersonalBorrow from "../../components/userCenter/PersonalBorrow";
  import PersonalHistory from "../../components/userCenter/PersonalHistory";
  import PersonalSubscribeOvertime from "../../components/userCenter/PersonalSubscribeOvertime";
  import PersonalMsg from "../../components/userCenter/PersonalMsg";
  export default {
  components: {PersonalBorrow, PersonalHistory,PersonalSubscribeOvertime,PersonalMsg},
}
</script>
