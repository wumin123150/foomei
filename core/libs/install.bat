@echo off
echo [INFO] Install jar to local repository.

cd %~dp0
call mvn install:install-file -Dfile=foomei-common-2.3.3-GA.jar -DgroupId=com.foomei -DartifactId=foomei-common -Dversion=2.3.3-GA -Dpackaging=jar
call mvn install:install-file -Dfile=foomei-common-2.3.3-GA-sources.jar -DgroupId=com.foomei -DartifactId=foomei-common -Dversion=2.3.3-GA -Dpackaging=jar -Dclassifier=sources
call mvn install:install-file -Dfile=foomei-common-2.3.3-GA-tests.jar -DgroupId=com.foomei -DartifactId=foomei-common -Dversion=2.3.3-GA -Dpackaging=jar -Dclassifier=tests
pause