<template>
  <div>
    <Row>
      <Col span="7"></Col>
      <Col span="10">
        <div style="padding: 20px">
          <Card>
            <p slot="title">登录</p>
            <LoginForm/>
          </Card>
        </div>
      </Col>
      <Col span="7"></Col>
    </Row>
  </div>
</template>
<script>
  import LoginForm from "@/components/userCenter/LoginForm";

  export default {
  components: {LoginForm},
}
</script>
