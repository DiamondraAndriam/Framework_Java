package etu1748.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String getBaseURL(String url) {
        System.out.println(url + "\n");
        String[] baseURL = url.split("/", 5);
        return baseURL[4];
    }

    public List<Class<?>> getClasses(File directory, String baseName) {
        List<Class<?>> classes = new ArrayList<>();
        String className = "";
        String base = "";
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
            for (File file : files) {
                if(baseName.equalsIgnoreCase(""))
                    className = file.getName().substring(0, file.getName().length() - 6);
                else
                    className = baseName + "." + file.getName().substring(0, file.getName().length() - 6);
                System.out.println(className);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    // La classe n'a pas pu être chargée
                }
            }
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    System.out.println(file);
                    if(baseName.equalsIgnoreCase(""))
                        base = file.getName();
                    else
                        base = baseName + "." + file.getName();
                    classes.addAll(getClasses(file, base));
                }
            }
        }
        return classes;
    }
    
}
