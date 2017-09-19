1.本系统使用lombok生成JavaBean的构造器、getter、setter、equals、hashcode、toString方法
a)将lombok.jar复制到myeclipse.ini/eclipse.ini所在的文件夹目录下
b)重启eclipse/myeclipse

2.foomei-common-2.1.0-GA.jar添加到Maven的本地仓库
mvn install:install-file -Dfile=foomei-common-2.1.0-GA.jar -DgroupId=com.foomei -DartifactId=foomei-common -Dversion=2.1.0-GA -Dpackaging=jar

3.API文档访问路径/swagger-ui.html API数据访问路径/v2/api-docs

4.druid监控访问路径/druid，用户名/密码：druid/druid

5.代码生成，执行com.foomei.core.utils.JpaCodeUtil

6.改进任务
1.timeago jquery的日期格式化
2.SearchRequest的优化
3.tree结构
4.move结构
5.加速spring启动
6.util整理
7.代码生成前端
8.com.googlecode maven-db-plugin
9.接口测试补全
10.主从数据库
11.SpringWebSocket
12.shiro多系统单点登录
13.ui http://www.layui.com/demo/tab.html  INSPINIA - Responsive Admin Theme
14 文件管理
15 通知管理
16 个人资料
17 消息管理
18 监控管理（待考虑）日志 在线用户


