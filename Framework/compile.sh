dossier= "D:/apache-tomcat-8.5.82/webapps/Test_Framework"
mkdir $dossier
webinf= "$dossier/WEB-INF"
mkdir $webinf
mkdir $webinf/lib
classes= "$dossier/WEB-INF/classes"
mkdir $classes

cp ../web.xml $webinf

javac --release 8 -d $classes Mapping.java
javac --release 8 -d $classes Urls.java
javac --release 8 -d $classes ParamList.java
javac --release 8 -d $classes Param.java
javac --release 8 -d $classes ModelView.java
javac --release 8 -d $classes Util.java
javac --release 8 -d $classes FrontServlet.java

cd $classes 
touch MANIFEST.MF
jar cf framework.jar MANIFEST.MF etu1748/framework/*.class etu1748/framework/annotation/*.class etu1748/framework/servlet/*.class etu1748/framework/util/*.class

cp framework.jar $webinf/lib

cd $dossier
rmdir -r $classes
mkdir $classes