## lab4个人文档

许同樵 19302010015

### 1、主要完成工作

所有前端开发工作

### 2、前端页面的重构

为了保证前端的简洁性，将bootstrap残留的部分class删除。
页面的结构布置时，将导航栏单独存在一层，而不是存到App的层中。这种情况下就可以在管理员选择分馆时不使用导航栏，以免管理员直接通过导航栏跳转而引发错误。

### 3、冷却时间实现

发送验证码邮件需要冷却时间，通过setInterval和clearInterval实现。注意内部操作函数需用箭头符号而非回调函数实现，否则无法改变外部的变量。

>        let interval = setInterval(()=>{
>        this.time= --i;
>        if(i===0) {
>         this.canSendCaptcha = true;
>         clearInterval(interval);
>       }
>    },1000);

### 4、接口

一般get接口可在后端使用restful API格式，通过在最后加参数来实现。因此前端直接动态改变API即可。使用这种方式有时可以大大简化api，例如只有一个参数需要传的时候。

### 5、关于美化和用户友好问题

#### （1）在还书列表中，在输入框后选择状态，直接添加入表格。又例如还书失败时将失败的条目留在表格中，将成功还好的书删除。
#### （2）部分内容无需显示的就隐去。在个人中心中，如果没有逾期预约的书，就不会在个人中心显示这一条。
#### （3）导航栏过长可能被挡住，小改
#### （4）表格中的数据需进行处理