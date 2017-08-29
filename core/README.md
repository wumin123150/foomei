1.本系统使用lombok生成JavaBean的构造器、getter、setter、equals、hashcode、toString方法
a)将lombok.jar复制到myeclipse.ini/eclipse.ini所在的文件夹目录下
b)重启eclipse/myeclipse

2.foomei-common-2.1.0-GA.jar添加到Maven的本地仓库
mvn install:install-file -Dfile=foomei-common-2.1.0-GA.jar -DgroupId=com.foomei -DartifactId=foomei-common -Dversion=2.1.0-GA -Dpackaging=jar

3.API文档访问路径/swagger-ui.html

4.druid监控访问路径/druid，用户名/密码：druid/druid

5.代码生成，执行com.foomei.core.utils.JpaCodeUtil
