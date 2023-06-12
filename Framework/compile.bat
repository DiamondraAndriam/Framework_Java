set dossier= D:\apache-tomcat-8.5.82\webapps\Test_Framework
mkdir %dossier%
set webinf= %dossier%\WEB-INF
mkdir %webinf%
mkdir %webinf%\lib
set classes=%dossier%\WEB-INF\classes
mkdir %classes%

copy ../web.xml %webinf%

javac --release 8 -d %classes% Mapping.java
javac --release 8 -d %classes% Urls.java
javac --release 8 -d %classes% ParamList.java
javac --release 8 -d %classes% Param.java
javac --release 8 -d %classes% ModelView.java
javac --release 8 -d %classes% Util.java
javac --release 8 -d %classes% FrontServlet.java

cd %classes% 
echo. > MANIFEST.MF
jar cf framework.jar MANIFEST.MF etu1748/framework/*.class etu1748/framework/annotation/*.class etu1748/framework/servlet/*.class etu1748/framework/util/*.class

copy framework.jar %webinf%\lib

cd %dossier%
rmdir /s %classes%
mkdir %classes%