dossier= "D:/apache-tomcat-8.5.82/webapps/framework"
mkdir $dossier
webinf= "$dossier/WEB-INF"
mkdir $webinf
mkdir $webinf/lib
classes= "$dossier/WEB-INF/classes"
mkdir $classes

javac --release 8 -d . JSON.java
javac --release 8 -d . Session.java
javac --release 8 -d . Scope.java
javac --release 8 -d . Auth.java
javac --release 8 -d . Urls.java
javac --release 8 -d . ParamList.java
javac --release 8 -d . Param.java

javac --release 8 -d . Mapping.java
javac --release 8 -d . FileUpload.java
javac --release 8 -d . ModelView.java
javac --release 8 -d . Util.java

javac --release 8 -d .Connect.java
javac --release 8 -d . GenericDAO.java
javac --release 8 -d . ResponseAPI.java

javac --release 8 -d . MVCController.java
javac --release 8 -d . MVCModel.java
javac --release 8 -d . ForeignKey.java
javac --release 8 -d . Column.java
javac --release 8 -d . Id.java
javac --release 8 -d . Table.java

javac --release 8 -d . FrontServlet.java

jar cf framework.jar MANIFEST.MF etu1748/framework/*.class etu1748/framework/annotation/*.class etu1748/framework/servlet/*.class etu1748/framework/util/*.class

cp framework.jar $webinf/lib
cp framework.jar D:\