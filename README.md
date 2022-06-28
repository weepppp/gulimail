# gulimall
## 谷粒商城
* BtoC：电商客户前台+管理后台
* 前端**VUE**+后端**微服务分布式集群**

#### 一、环境搭建

1. 配置linux和docker--mysql、redis

2. 在github上新建仓库，设置ignore文件

3. clone仓库到本地，用idea打开

4. 在idea上配置ignore文件设置，搭建聚合maven工程框架

5. 第一次push

6. 创建各模块数据库，导入数据库文件

   至此，基本运行环境已经配置完毕

   

#### 二、引入后台管理系统

1. 直接在主工程里导入[人人开源](https://gitee.com/renrenio)的后台管理系统源码

2. 创建后台管理系统数据库

3. 在vscode里导入前端vue文件，install

4. 进行前后联调

5. 新建common功能模块，设置共有依赖、bean、配置文件等

6. 配置[人人开源](https://gitee.com/renrenio)的逆向工程生成器逆向代码，配置mybatis+和数据源，测试基础crud

7. 第二次push

   
 
#### 三、分布式组件配置 

##### *使用spring-cloud-alibaba系列组件进行分布式配置*

以下1和2配置的使用步骤均已通过【gulimall-coupon模块的CouponController类】和【gulimall-coupon模块的MemberController类】进行了注解说明，可查看[官方文档](https://github.com/alibaba/spring-cloud-alibaba)进行完整配置

1. 使用Nacos：作为注册中心和配置中心----给每一个功能模块以及Nacos服务页进行配置

2. 配置feign：远程转发某台服务器的请求到已经注册过的其他服务器上

3. 使用gateway：提供路由服务，支持断言，底层是netty

4. 第三次push

  

#### 四、商品管理——分类

##### *本小节功能重在对前端vue的修改完善，后端逻辑比较简单*

各部分业务均采用后端-前端-后端.....的顺序进行编写调试

如部分前端或者前后端交互的问题无法解决，选择直接使用**postman**进行测试，无伤大雅

以下是注意点：

---

1. 前后端交互（网关转发等）的路由配置，浏览器跨域问题
    - 暂时不想深究了....bug一大堆......头风发作中.....

2. 增删改查业务的设计
    - 逻辑删除：按数字进行状态映射配置，调用逆向工程的删除方法即可

3. 版本依赖做不好，bug改到老




*更新中*
