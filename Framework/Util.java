package etu1748.framework.util;

import java.io.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.*;

import etu1748.framework.FileUpload;

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

    public static Object parseType(String input, Class<?> type) {
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

    public static Method findMethod(String methodName, Class<?> type) {
        Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName))
                return method;
        }
        return null;
    }

    public String getFileName(Part part) {
        String contentDisposition = part.getHeader("Content-Disposition");
        System.out.println("Content-Disposition: " + contentDisposition);
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                String filename = element.substring(element.indexOf('=') + 1).trim();
                if (filename.startsWith("\"") && filename.endsWith("\"")) {
                    filename = filename.substring(1, filename.length() - 1);
                }
                System.out.println("File name : " + filename);
                return filename;
            }
        }
        return null;
    }

    public byte[] fileToBytes(Part filePart) throws IOException {
        byte[] bytes = null;
        String filename = this.getFileName(filePart);

        InputStream fileContent = filePart.getInputStream();
        ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();

        try {
            bytes = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileContent.read(bytes)) != -1) {
                fileOutput.write(bytes, 0, bytesRead);
            }

            byte[] fileBytes = fileOutput.toByteArray();
            return fileBytes;
        } finally {
            fileContent.close();
            fileOutput.close();
        }
    }

    public FileUpload getFileUpload(Part filePart) throws IOException {
        String fileName = getFileName(filePart);
        byte[] fileContent = fileToBytes(filePart);
        filePart.getInputStream().close();

        FileUpload upload = new FileUpload();
        upload.setName(fileName);
        upload.setFile(fileContent);
        return upload;
    }
}
