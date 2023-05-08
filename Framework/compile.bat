javac --release 8 -d . Mapping.java
javac --release 8 -d . Urls.java
javac --release 8 -d . ModelView.java
javac --release 8 -d . Util.java
javac --release 8 -parameters -d . FrontServlet.java
jar cvfm framework.jar MANIFEST.MF etu1748/framework/*.class etu1748/framework/annotation/*.class etu1748/framework/servlet/*.class etu1748/framework/util/*.class