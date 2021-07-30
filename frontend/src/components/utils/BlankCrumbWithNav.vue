<template>
  <div class="layout">
    <Layout :style="{minHeight: '100vh'}">
      <Sider>
        <Navigation></Navigation>
      </Sider>
      <Layout>
        <Header :style="{background: '#fff', boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)'}"></Header>
        <Content :style="{padding: '0 16px 16px'}">
          <MyBreadcrumb></MyBreadcrumb>
          <Card>
            <div :style="{padding:'5px'}">
              <router-view :key="1"></router-view>
            </div>
          </Card>
        </Content>
        <Footer></Footer>
      </Layout>
    </Layout>
  </div>
</template>

<script>
  import Footer from '@/components/utils/Footer'
  import Navigation from "@/components/utils/Navigation";
  import MyBreadcrumb from "@/components/utils/MyBreadcrumb";

  export default {
    components: {
      MyBreadcrumb,
      Navigation,
      Footer
    },
  }
</script>

<style scoped>
  .layout-con {
    height: 100%;
    width: 100%;
  }

  .collapsed-menu span {
    width: 0px;
    transition: width .2s ease;
  }

  .collapsed-menu i {
    transform: translateX(5px);
    transition: font-size .2s ease .2s, transform .2s ease .2s;
    vertical-align: middle;
    font-size: 22px;
  }
</style>
