import com.google.gson.*;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import etu1748.framework.Connect;

public class Main {

    /*public static void main(String[] args) {
        String jsonString = "{\"date\": \"2024-03-30\"}";
        
        // Création d'un objet Gson avec un adaptateur personnalisé pour les dates SQL
        Gson gson = new GsonBuilder()
        .registerTypeAdapter(Date.class, new SqlDateDeserializer())
        .create();
        
        // Conversion de l'objet JSON en objet Java
        MyObject myObject = gson.fromJson(jsonString, MyObject.class);
        
        // Utilisation de l'objet Java
        System.out.println("Date: " + myObject.getDate());
    }

    public static void main(String[] args) {
        // Création d'un objet avec une date SQL
        MyObject myObject = new MyObject();
        myObject.setDate(Date.valueOf("2024-03-30"));

        // Création d'un objet Gson avec un adaptateur personnalisé pour les dates SQL
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateSerializer())
                .create();

        // Conversion de l'objet Java en JSON
        String json = gson.toJson(myObject);
        System.out.println("JSON: " + json);
    }*/

    static class MyObject {
        private Date date;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
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

    public static void main(String[] args){
        try{
            Connection connection = Connect.connect();
            System.out.println(connection);
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}