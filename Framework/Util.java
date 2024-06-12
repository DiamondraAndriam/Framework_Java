package etu1748.framework.util;

import java.io.*;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import etu1748.framework.FileUpload;

public class Util {
    static String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    private static HttpServletRequest request;

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

    public Object getDefault(Class<?> classe) {
        if (classe == int.class || classe == double.class || classe == float.class) {
            return 0;
        } else if (classe == boolean.class) {
            return false;
        } else {
            return null;
        }
    }

    static class SqlDateDeserializer implements JsonDeserializer<Date> {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                java.util.Date utilDate = dateFormat.parse(json.getAsString());
                return new Date(utilDate.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    static class SqlDateSerializer implements JsonSerializer<Date> {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(date));
        }
    }

    public String toJson(Object objet) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateSerializer())
                .create();
        return gson.toJson(objet);
    }

    public static Object getJSONContent(HttpServletRequest req, Class<?> classe) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateDeserializer())
                .create();
        return gson.fromJson(sb.toString(), classe);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String getToken(final HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);
        return null;
    }

    public static List<String> getConfigToken() throws IOException {
        Gson gson = new Gson();
        String tokenPath = path + "token.json";
        File tokenFile = new File(tokenPath);
        List<String> tokens = null;
        if (tokenFile.exists()) {
            // Lire le fichier JSON existant
            FileReader reader = new FileReader(tokenPath);
            Type type = new TypeToken<List<String>>() {
            }.getType();
            tokens = gson.fromJson(reader, type);
            reader.close();
        }
        return tokens;
    }

    public static void addConfig(String token) {
        try {
            // Créer un Gson instance
            Gson gson = new Gson();

            String tokenPath = path + "token.json";
            List<String> tokens = null;

            File tokenFile = new File(tokenPath);
            if (tokenFile.exists()) {
                tokens = getConfigToken();
            } else {
                tokens = new ArrayList<String>();
                tokenFile.createNewFile();
            }

            // Ajouter un nouvel élément au tableau
            tokens.add(token);

            // Écrire l'objet JSON modifié dans le fichier
            FileWriter file = new FileWriter(path + "/token.json");
            gson.toJson(tokens, file);
            file.close();

            System.out.println("Objet JSON créé/modifié et écrit avec succès");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeConfig(String token) {
        try {
            String tokenPath = path + "token.json";
            List<String> tokens = null;

            File tokenFile = new File(tokenPath);
            if (tokenFile.exists())
                tokens = getConfigToken();

            if (tokens != null) {
                for (String string : tokens) {
                    if (string.equals(token)) {
                        tokens.remove(string);
                        System.out.println("token removed");
                        break;
                    }
                }
            }
            Gson gson = new Gson();
            FileWriter file = new FileWriter(path + "/token.json");
            gson.toJson(tokens, file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
