# lab5进度文档

## 许同樵 2021.6.3

已完成前端书籍详细页面的评论区讨论区前端
书籍详细页面需获得的评论信息：

```js
[{
  hidden: false,
  index: 1,
  title: 'Hello Title!1234',
  commentContent: 'hello',
  user: 'xtq1',
  time: '2020-10-20 13:32',
  list:[{
    index: 1,
    user: 'xtq2',
    time: '2020-10-21 14:36',
    content: 'not hello!'
  },{...},...]
},{...},...]
```

需完成的功能：获取上述列表，已登录用户进行评论，管理员设置隐藏评论，管理员设置显示评论
已完成前端个人管理页面的列表
个人评论管理页面需获得信息：

```js
[{
    index:124,
    isbn:"1234567891",
    bookName:"书名2",
    commented:false,
    status: "未评论",
    time: "2020-10-30 13:42",
    comment:"hello comment"
},{...},...]
```

需完成的功能：获取上述列表，修改某个评论信息

### 许同樵 2021.6.4
基本完成评价体系，已分页，已使用console简化debug过程。