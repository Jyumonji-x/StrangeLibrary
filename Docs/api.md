# API接口约定

[toc]

## 数据库结构

### user结构 <font color=green>(somthing new in Lab3, Done)</font>

| 数据名称    | 数据类型 | 含义                                                |
| ----------- | -------- | --------------------------------------------------- |
| id          | Long     | 账号编号                                            |
| name        | String   | 十一位学号工号                                      |
| email       | String   | 电子邮箱                                            |
| password    | String   | 密码                                                |
| permission  | int      | 权限级别，0为普通用户，1为普通管理员，2为超级管理员 |
| time_create | datetime | 创建时间                                            |
| time_login  | datetime | 最后登录时间                                        |
| branch      | String   | 所属分馆                                            |

### book结构

book表为书籍信息,具体的实体书存放在book_duplicate表中

| 数据名称         | 数据类型 | 含义         |
| ---------------- | -------- | ------------ |
| id               | Long     | 书籍编号     |
| name             | String   | 书名         |
| author           | String   | 作者         |
| intro            | String   | 简介         |
| ISBN             | long     | ISBN版号     |
| time_publication | datetime | 出版时间     |
| time_create      | datetime | 上新时间     |
| cover            | String   | 图片存储路径 |

### book_duplicate结构 <font color=red>(something new in Lab3)</font>

book_duplicate中存放图书馆中的各个书籍副本，与book表中的书籍信息多对一对应

| 数据名称     | 数据类型 | 含义                                             |
| ------------ | -------- | ------------------------------------------------ |
| id           | long     | 书籍副本编号                                     |
| title        | String   | 书名                                             |
| duplicate_id | String   | 对应书籍ISBN+三位十进制编号                      |
| ISBN         | long     | ISBN版号                                         |
| branch       | String   | 当前所属分馆(在库状态下)                         |
| borrower     | String   | 借阅人(预约或借出状态下)                         |
| time_create  | datetime | 上新时间                                         |
| status       | int      | 出借状态，0为在库，1为被预约，2为被借走, 3为遗失 |

status包含在库、借出、已预约、遗失，默认状态为在库

### book_borrow结构 <font color=red>(something new in Lab3)</font>

book_borrow中存放用户借阅、归还图书的情况

| 数据名称      | 数据类型 | 含义                                      |
| ------------- | -------- | ----------------------------------------- |
| id            | long     | 书籍借阅记录编号                          |
| user_name     | String   | 借书发起人                                |
| operator_name | String   | 操作的管理员                              |
| duplicate_id  | long     | 对应副本编号                              |
| book_name     | String   | 书名                                      |
| branch_out    | String   | 被预约时所属分馆                          |
| branch_rtn    | String   | 归还时所属分馆                            |
| time_create   | datetime | 预约时间                                  |
| time_out      | datetime | 借出时间                                  |
| time_rtn      | datetime | 归还时间                                  |
| status        | int      | 出借状态，0为已归还，1为被预约，2为被借走 |

## api接口规定

>${ip}:${port}/api/

暂定使用local host:8080/
暂定所有接口使用POST方式

### 账号操作account

>${ip}:${port}/api/user/

#### 登录-查询账号信息 <font color=green>(something new in Lab3，Done)</font>

>${ip}:${port}/api/user/login
>method:POST

输入参数

| 参数      | 类型   | 含义   | 必填 | 示例          |
| --------- | ------ | ------ | ---- | ------------- |
| user_name | String | 用户名 | 是   | "19302010020" |
| password  | String | 密码   | 是   | "abcd1234"    |

输出参数

| 参数        | 类型   | 含义                       | 必填         | 示例                         |
| ----------- | ------ | -------------------------- | ------------ | ---------------------------- |
| rtn         | int    | 请求码                     | 是           | 0                            |
| message     | String | 描述信息                   | 是           | 登陆失败，用户名或密码不正确 |
| token       | int    | 身份表示符，登录成功才返回 | 是           |                              |
| userDetails | Object | 登录用户的信息(除去密码)   | 否，成功才有 |                              |

服务器需要生成token并存储，前端记录token，在必要的页面中向后端验证

#### 注册-新建账号信息 <font color=green>(something new in Lab3，Done)</font>

>${ip}:${port}/api/user/register
>method:POST

输入参数

| 参数      | 类型   | 含义   | 必填 | 示例                |
| --------- | ------ | ------ | ---- | ------------------- |
| user_name | String | 用户名 | 是   | YC_Yuan             |
| email     | String | 邮箱   | 是   | example@example.com |
| password  | String | 密码   | 是   | abcd1234            |

输出参数

| 参数        | 类型   | 含义                     | 必填        | 示例                   |
| ----------- | ------ | ------------------------ | ----------- | ---------------------- |
| rtn         | int    | 请求码                   | 是          | 1                      |
| message     | String | 描述信息                 | 是          | 注册失败，用户名已存在 |
| userDetails | Object | 登录用户的信息(除去密码) | 否,成功才有 |                        |

合法性检测（前后端检测项统一，检测顺序统一，提示内容统一。后端通过message返回如下错误信息）:

1. 用户名需要由字母、数字、-、_构成
2. 用户名需要由字母或-开头
3. 用户名长度需在6-32个字符
4. 用户名已被注册，请更换
5. 密码需要由字母、数字、-、_构成
6. 密码需要包含字母、数字、(-、_)中的至少两种
7. 密码长度需在6-32个字符
8. 密码不能包含用户名
9. 邮箱格式非法(此处用雷·汤姆林森创立的标准E-mail格式)

#### 更改账号信息 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/user/modify
>method:POST

输入参数

| 参数         | 类型   | 含义       | 必填 | 示例                |
| ------------ | ------ | ---------- | ---- | ------------------- |
| user_id      | Long   | 账号编号   | 是   | 1                   |
| user_name    | String | 新用户名   | 是   | YC_Yuan             |
| email        | String | 新邮箱     | 是   | example@example.com |
| password     | String | 新密码     | 是   | abcd1234            |
| old_password | String | 账号原密码 | 是   | abcd1234            |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例             |
| ------- | ------ | -------- | ---- | ---------------- |
| rtn     | int    | 请求码   | 是   | 1                |
| message | String | 描述信息 | 是   | 账号信息修改成功 |

### 权限管理操作authority

>${ip}:${port}/api/authority/

#### 创建管理员

>${ip}:${port}/api/authority/create_admin
>method:POST

输入参数

| 参数      | 类型   | 含义            | 必填 | 示例                |
| --------- | ------ | --------------- | ---- | ------------------- |
| token     | int    | 超级管理员token | 是   |                     |
| user_name | String | 用户名          | 是   | YC_Yuan             |
| email     | String | 邮箱            | 是   | example@example.com |
| password  | String | 密码            | 是   | abcd1234            |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例           |
| ------- | ------ | -------- | ---- | -------------- |
| rtn     | int    | 请求码   | 是   | 1              |
| message | String | 描述信息 | 是   | 管理员创建成功 |

### 书本操作book

>${ip}:${port}/api/book/

#### 新增书本

>${ip}:${port}/api/book/upload
>method:POST

输入参数

| 参数             | 类型     | 含义           | 必填 | 示例          |
| ---------------- | -------- | -------------- | ---- | ------------- |
| book_name        | String   | 书籍名称       | 是   | 美丽新世界    |
| author           | String   | 作者           | 是   | 阿道斯·赫胥黎 |
| intro            | String   | 简介           | 是   |               |
| ISBN             | long     | ISBN版号       | 是   | 9787510880803 |
| publication_time | datetime | 出版时间       | 是   |               |
| cover            | String   | 书籍封面图地址 | 是   |               |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例         |
| ------- | ------ | -------- | ---- | ------------ |
| rtn     | int    | 请求码   | 是   | 1            |
| message | String | 描述信息 | 是   | 书籍上传成功 |

#### 根据ISBN获取书本 <font color=red>(something new in Lab3, Done)</font>

>${ip}:${port}/api/book/get_by_ISBN
>method:POST

输入参数

| 参数 | 类型 | 含义     | 必填 | 示例 |
| ---- | ---- | -------- | ---- | ---- |
| ISBN | Long | ISBN版号 | 是   |      |

输出参数

| 参数    | 类型              | 含义             | 必填           | 示例         |
| ------- | ----------------- | ---------------- | -------------- | ------------ |
| rtn     | int               | 请求码           | 是             | 1            |
| message | String            | 描述信息         | 是             | 书籍上传成功 |
| book    | book(json object) | 检索到的书籍信息 | 否，成功才返回 |              |

#### 根据书名模糊搜索书本 <font color=red>(something new in Lab3, Done)</font>

>${ip}:${port}/api/book/search_by_title
>method:POST

输入参数

| 参数  | 类型   | 含义     | 必填 | 示例 |
| ----- | ------ | -------- | ---- | ---- |
| title | String | ISBN版号 | 是   |      |

输出参数

| 参数    | 类型              | 含义             | 必填           | 示例         |
| ------- | ----------------- | ---------------- | -------------- | ------------ |
| rtn     | int               | 请求码           | 是             | 1            |
| message | String            | 描述信息         | 是             | 书籍上传成功 |
| books   | book(json object) | 检索到的书籍信息 | 否，成功才返回 |              |

#### 浏览书库 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/book/browse
>method:POST

输入参数

| 参数 | 类型 | 含义       | 必填 | 示例 |
| ---- | ---- | ---------- | ---- | ---- |
| page | int  | 跳过书本数 | 是   | 1    |
| size | int  | 取几本书   | 是   | 8    |

输出参数

| 参数      | 类型          | 含义                                   | 必填           | 示例         |
| --------- | ------------- | -------------------------------------- | -------------- | ------------ |
| rtn       | int           | 请求码                                 | 是             | 1            |
| message   | String        | 描述信息                               | 是             | 书籍检索成功 |
| books     | array of book | 检索到的所有书籍信息                   | 否，成功才返回 |              |
| inventory | array of int  | 每本书可用的库存 顺序与books中一一对应 | 否，成功才返回 |              |
| count     | int           | 书籍总数量                             | 是             | 100          |

### 副本操作book_duplicate

>${ip}:${port}/api/book_duplicate/

#### 新书入库-新建副本 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/book_duplicate/add
>method:POST

输入参数

| 参数     | 类型   | 含义                 | 必填 | 示例 |
| -------- | ------ | -------------------- | ---- | ---- |
| ISBN     | Long   | ISBN版号             | 是   |      |
| number   | int    | 添加数量             | 是   | 3    |
| location | String | 副本所属图书馆       | 是   | 邯郸 |
| token    | int    | 用于检验是否为管理员 | 是   |      |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例                 |
| ------- | ------ | -------- | ---- | -------------------- |
| rtn     | int    | 请求码   | 是   | 1                    |
| message | String | 描述信息 | 是   | 新书入库失败，请重试 |

#### 查看某本书的副本 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/book_duplicate/search
>method:POST

输入参数

| 参数 | 类型 | 含义     | 必填 | 示例 |
| ---- | ---- | -------- | ---- | ---- |
| ISBN | Long | ISBN版号 | 是   |      |

输出参数

| 参数           | 类型                                 | 含义     | 必填 | 示例                 |
| -------------- | ------------------------------------ | -------- | ---- | -------------------- |
| rtn            | int                                  | 请求码   | 是   | 1                    |
| message        | String                               | 描述信息 | 是   | 副本查询失败，请重试 |
| book_duplicate | array of book_duplicate(json object) | 副本数组 | 否   |                      |

### 个人借阅 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/borrow/

#### 查询个人已预约/已借阅书籍 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/borrow/borrowed_subscribed_books
>method:POST

输入参数

| 参数      | 类型   | 含义                           | 必填 | 示例 |
| --------- | ------ | ------------------------------ | ---- | ---- |
| user_name | String | 账号名                         | 是   | 1    |
| token     | int    | 表明查看者，要求是管理员或本人 | 是   | 1    |

输出参数

| 参数        | 类型   | 含义     | 必填 | 示例                 |
| ----------- | ------ | -------- | ---- | -------------------- |
| rtn         | int    | 请求码   | 是   | 1                    |
| message     | String | 描述信息 | 是   | 副本查询失败，请重试 |
| book_borrow | array  | 借书记录 | 否   |                      |

#### 预约书籍 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/borrow/subscribe
>method:POST

输入参数

| 参数         | 类型   | 含义                   | 必填 | 示例 |
| ------------ | ------ | ---------------------- | ---- | ---- |
| token        | int    | 表明预约者，要求是本人 | 是   | 1    |
| duplicate_id | String | 副本编号               | 是   |      |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例     |
| ------- | ------ | -------- | ---- | -------- |
| rtn     | int    | 请求码   | 是   | 1        |
| message | String | 描述信息 | 是   | 预约成功 |

#### 现场取预约图书 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/borrow/getSubscribe
>method:POST

输入参数

| 参数         | 类型   | 含义                         | 必填 | 示例 |
| ------------ | ------ | ---------------------------- | ---- | ---- |
| borrow_name  | String | 借阅者用户名                 | 是   | 1    |
| token        | int    | 表明操作者身份，要求是管理员 | 是   | 1    |
| duplicate_id | String | 副本唯一标识（ISBN-XXX）     | 是   |      |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例     |
| ------- | ------ | -------- | ---- | -------- |
| rtn     | int    | 请求码   | 是   | 1        |
| message | String | 描述信息 | 是   | 取书成功 |

#### 现场借书 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/borrow/onsiteBorrow
>method:POST

输入参数

| 参数         | 类型   | 含义                         | 必填 | 示例 |
| ------------ | ------ | ---------------------------- | ---- | ---- |
| borrow_name  | String | 借阅者用户名                 | 是   | 1    |
| token        | int    | 表明操作者身份，要求是管理员 | 是   | 1    |
| duplicate_id | String | 副本唯一标识（ISBN-XXX）     | 是   |      |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例     |
| ------- | ------ | -------- | ---- | -------- |
| rtn     | int    | 请求码   | 是   | 1        |
| message | String | 描述信息 | 是   | 借阅成功 |

#### 现场还书 <font color=red>(something new in Lab3)</font>

>${ip}:${port}/api/borrow/onsiteReturn
>method:POST

输入参数

| 参数         | 类型   | 含义                         | 必填 | 示例   |
| ------------ | ------ | ---------------------------- | ---- | ------ |
| token        | Int    | 表明操作者身份，要求是管理员 | 是   | 1      |
| duplicate_id | String | 副本唯一标识（ISBN-XXX）     | 是   |        |
| branch       | String | 归还的分馆                   | 是   | "邯郸" |

输出参数

| 参数    | 类型   | 含义     | 必填 | 示例     |
| ------- | ------ | -------- | ---- | -------- |
| rtn     | int    | 请求码   | 是   | 1        |
| message | String | 描述信息 | 是   | 借阅成功 |

