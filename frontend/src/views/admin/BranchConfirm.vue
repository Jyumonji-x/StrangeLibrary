<template>
  <div style="margin: auto">
    <Row>
      <Col span="7"></Col>
      <Col span="10">
        <div style="padding: 20px">
          <Card>
            <p slot="title">选择分馆</p>
            <RadioGroup v-model="branch">
              <Radio label="邯郸"></Radio>
              <Radio label="枫林"></Radio>
              <Radio label="张江"></Radio>
              <Radio label="江湾"></Radio>
            </RadioGroup>
            <br /><br />
            <Button type="primary" @click.native="submit()">提交</Button>
          </Card>
        </div>
      </Col>
      <Col span="7"></Col>
    </Row>


  </div>

</template>
<script>
  export default {
    data() {
      return {
      branch: '邯郸'
    }
  },
  methods: {
    submit() {
      this.$store.commit('setBranch', this.branch);
      console.log(this.$store.state.branch);
      this.$router.replace('/user/personal');
    }
  }
}
</script>
