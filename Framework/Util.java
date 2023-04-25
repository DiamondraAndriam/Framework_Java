package etu1748.framework.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                if (baseName.equalsIgnoreCase(""))
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
                    if (baseName.equalsIgnoreCase(""))
                        base = file.getName();
                    else
                        base = baseName + "." + file.getName();
                    classes.addAll(getClasses(file, base));
                }
            }
        }
        return classes;
    }

    public static String getSetter(Field field) {
        String name = field.getName();
        char debut = name.charAt(0);
        String mot = "" + debut;
        name = name.replaceFirst(mot, mot.toUpperCase());
        System.out.println(name);
        return "set" + name;
    }

    public static Object parseType(String input, Class type) {
        Object answer = null;
        if (type == int.class || type == Integer.class) {
            answer = Integer.parseInt(input);
        } else if (type == double.class || type == Double.class) {
            answer = Double.parseDouble(input);
        } else if (type == float.class || type == Float.class) {
            answer = Float.parseFloat(input);
        } else if (type == java.sql.Date.class) {
            answer = java.sql.Date.valueOf(input);
        } else if (type == LocalDate.class) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            answer = LocalDate.parse(input, formatter);
        } else if (type == java.sql.Time.class) {
            answer = java.sql.Time.valueOf(input);
        } else if (type == boolean.class || type == Boolean.class) {
            answer = Boolean.parseBoolean(input);
        } else {
            answer = input;
        }
        return answer;
    }
}
