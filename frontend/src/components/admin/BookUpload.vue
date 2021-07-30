<template>
  <div>
    <el-form ref="uploadData" :model="uploadData" status-icon :rules="rules" size="medium" label-width="80px">
      <el-form-item label="封面" prop="cover" required>
        <el-upload ref="cover" :file-list="coverfileList" :action="coverAction" :auto-upload="false"
                   :on-change="coverOnchange" :limit="1" :disabled="uploadDisabled"
                   :before-upload="coverBeforeUpload" list-type="picture-card" accept="image/*">
          <i class="el-icon-plus" v-if="!uploadDisabled"></i>
          <i class="el-icon-refresh" v-if="uploadDisabled" @click="resetCover"></i>
          <div slot="tip" class="el-upload__tip">请上传一张不超过 5MB 的image/*文件</div>
          <!--                    <el-button @click="resetCover">清除图片</el-button>-->
        </el-upload>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="uploadData.title" placeholder="请输入标题" clearable :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="uploadData.author" placeholder="请输入作者" clearable :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="简介" prop="intro">
        <el-input v-model="uploadData.intro" type="textarea" placeholder="请输入简介"
                  :autosize="{minRows: 1, maxRows: 6}" :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="ISBN" prop="ISBN">
        <el-input v-model="uploadData.ISBN" placeholder="请输入ISBN" clearable :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="出版时间" prop="time_publish">
        <el-date-picker v-model="uploadData.time_publish" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                        :style="{width: '100%'}" placeholder="请选择出版时间" clearable></el-date-picker>
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input v-model="uploadData.price" placeholder="请输入价格(单位：元)" clearable
                  :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="submitForm('uploadData')">提交</el-button>
        <el-button @click="resetForm('uploadData')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    let checkIsbn10 = /^(\d){10}$/ //isbn10位
    let checkIsbn13 = /^(\d){13}$/ //isbn13位
    let checkPrice = /^\d+(\.(\d){1,2})?$/ //数字
    const validateIntro = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入图书简介'));
      } else if (value.length > 250) {
        callback(new Error('图书简介请勿超过250字'));
      } else {
        callback();
      }
    }
    const validateIsbn = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入ISBN'))
      } else if (!checkIsbn10.test(value) && !checkIsbn13.test(value)) {
        callback(new Error('ISBN格式非法'))
      } else {
        callback();
      }
    };
    const validatePrice = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入价格（元）'))
      } else if (!checkPrice.test(value)) {
        callback(new Error('请输入合法价格(小数点后最多2位的数字)'))
      } else {
        callback();
      }
    };
    return {
      uploadData: {
        cover: null,
        title: undefined,
        author: undefined,
        intro: undefined,
        ISBN: undefined,
        time_publish: null,
      },
      rules: {
        title: [{
          required: true,
          message: '请输入标题',
          trigger: 'blur'
        }],
        author: [{
          required: true,
          message: '请输入作者',
          trigger: 'blur'
        }],
        intro: [{
          validator: validateIntro,
          required: true,
          trigger: 'blur'
        }],
        ISBN: [{
          validator: validateIsbn,
          required: true,
          trigger: 'blur'
        }],
        time_publish: [{
          required: true,
          message: '请选择出版时间',
          trigger: 'change'
        }],
        price: [{
          validator: validatePrice,
          required: true,
          trigger: 'blur'
        }],
      },
      coverAction: '',
      coverfileList: [],
      uploadDisabled: false,
    }
  },
  methods: {
    submitForm(formName) {
      console.log("submit")
      this.$refs[formName].validate(valid => {
        if (valid) {
          let config = {'Content-Type': 'multipart/form-data'}
          let postData = new FormData()
          postData.append("title", this.uploadData.title)
          postData.append("author", this.uploadData.author)
          postData.append("intro", this.uploadData.intro)
          postData.append("ISBN", this.uploadData.ISBN)
          postData.append("time_publish", this.uploadData.time_publish)
          postData.append("cover", this.uploadData.cover)
          postData.append("price", this.uploadData.price)
          console.log(postData)
          this.$axios.post("/apiBook/book/upload", postData, config)
              .then(resp => {
                if (resp.status === 200) {
                  if (resp.data.rtn === 1) {
                    this.$router.replace({path: '/browse'})
                  } else {
                    this.$message({
                      message: resp.data.message,
                      type: 'warning'
                    })
                    console.log(resp)
                  }
                } else {
                  this.$message({
                    message: '请求失败',
                    type: 'warning'
                  })
                }
              })
        }
      })
    },
    resetCover(e) {
      this.coverfileList = []
      this.uploadDisabled = false
      this.uploadData.cover = null
      e.stopPropagation()
    },
    resetForm(fromName) {
      this.$refs[fromName].resetFields()
      this.resetCover()
    },
    coverBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 5
      if (!isRightSize) {
        this.$message.error('文件大小超过 5MB')
      }
      let isAccept = new RegExp('image/*').test(file.type)
      if (!isAccept) {
        this.$message.error('应该选择image/*类型的文件')
      }
      return isRightSize && isAccept
      //如果返回false就不进行
    },
    coverOnchange(file, fileList) {
      this.uploadDisabled = fileList.length === 1;
      this.uploadData.cover = file.raw
    }
  }
}

</script>
