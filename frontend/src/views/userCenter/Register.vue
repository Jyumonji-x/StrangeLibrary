<template>
  <div>
    <Row>
      <Col span="7"></Col>
      <Col span="10">
        <div style="padding: 20px">
          <Card>
            <p slot="title">注册</p>
            <RegisterForm/>
          </Card>
        </div>
      </Col>
      <Col span="7"></Col>
    </Row>
  </div>
</template>
<script>
  import RegisterForm from "@/components/userCenter/RegisterForm";

  export default {
    name: 'register',
    components: {RegisterForm},
  }
</script>
