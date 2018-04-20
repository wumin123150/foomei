## 项目介绍

　　基于Spring+SpringMVC+Hibernate敏捷开发系统架构。

新项目使用方法
- 1.先后执行parent/install.bat、common/install.bat完成初始化
- 2.copy整个core到workspace，并修改成自己的项目名称'test'
- 3.copy整个parent到项目'test'下
- 4.copy common/target下的3个jar到项目'test'下的libs，并修改本目录下的install.bat（主要是更新版本）
- 5.修改项目'test'下的pom.xml，parent.relativePath改为'./parent/'，artifactId改为'test'、name改为'测试'
- 6.提交svn，其他人下载后，先后执行parent/install.bat、libs/install.bat完成初始化
