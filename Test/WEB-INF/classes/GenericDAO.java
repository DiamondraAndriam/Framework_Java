package dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import data.Connect;
import etu1748.framework.FileUpload;

import java.lang.reflect.*;

public class GenericDAO {
    // donne le nom de la table
    public static String getTable(Object o) {
        return o.getClass().getSimpleName();
    }

    // donne la liste de sattribut non null ou différent de 0
    public static List<Field> getFieldsNotNull(Object o) throws Exception {
        Field[] fields = getFields(o);
        List<Field> notNull = new ArrayList<>();
        for (Field field : fields) {
            if (testNull(field, o) == false) {
                notNull.add(field);
            }
        }
        if (notNull.isEmpty())
            return null;
        else
            return notNull;
    }

    public static boolean testNull(Field field, Object o) throws Exception {
        if (o == null) {
            throw new Exception("Fonction testNotNull: Objet null en entrée");
        }
        try {
            field.setAccessible(true);
            Object value = field.get(o);
            field.setAccessible(false);
            if (value == null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Ne peut pas obtenir l'attribut");
        }
    }

    // donne la liste de tous les champs de l'objet
    public static Field[] getFields(Object o) throws Exception {
        return o.getClass().getDeclaredFields();
    }

    // donne l'objet identifié par un int
    public static List<Object> findById(Object o, int id) throws Exception {
        String request = requestFindById(o, id);
        List<Object> result = executeQuery(o, request);
        return result;
    }

    // donne l'objet identifié par un string
    public static List<Object> findById(Object o, String id) throws Exception {
        String request = requestFindById(o, id);
        List<Object> result = executeQuery(o, request);
        return result;
    }

    // les requêtes pour les fonctions findById
    public static String requestFindById(Object o, int id) {
        String request = "SELECT * FROM " + getTable(o) + " WHERE id = " + id;
        return request;
    }

    public static String requestFindById(Object o, String id) {
        String request = "SELECT * FROM " + getTable(o) + " WHERE id = " + id;
        return request;
    }

    // donne tous les éléments de la table
    public static List<Object> findAll(Object o) throws Exception {
        String request = requestFindAll(o);
        List<Object> result = executeQuery(o, request);
        return result;
    }

    // donne tous les éléments de la vue représenté par un objet o et pas une table
    public static List<Object> findAll(Object o, String viewName) throws Exception {
        String request = requestFindAll(viewName);
        List<Object> result = executeQuery(o, request);
        return result;
    }

    // les requêtes pour les fonctions findAll
    public static String requestFindAll(Object o) {
        return "SELECT * FROM " + getTable(o);
    }

    public static String requestFindAll(String viewName) {
        return "SELECT * FROM " + viewName;
    }

    // donne tous les éléments d'une table avec une condition particulière
    public static List<Object> findAllSpec(Object o, String condition) throws Exception {
        String request = requestFindAllSpec(o, condition);
        List<Object> result = executeQuery(o, request);
        return result;
    }

    // donne tous les éléments d'une vue avec une condition particulière
    public static List<Object> findAllSpec(Object o, String viewName, String condition) throws Exception {
        String request = requestFindAllSpec(o, condition);
        List<Object> result = executeQuery(o, request);
        return result;
    }

    // les requêtes pour les fonctions findAllSpec
    public static String requestFindAllSpec(Object o, String condition) {
        String request = requestFindAll(o);
        return request + " WHERE " + condition;
    }

    public static String requestFindAllSpec(Object o, String viewName, String condition) {
        String request = requestFindAll(viewName);
        return request + " WHERE " + condition;
    }

    // crée une table dans la base de données
    /*public static void createTable(Object o) throws Exception {
        String request = requestToTable(o);
        executeUpdate(request);
    }

    // les requêtes pour createTable
    /*public static String requestToTable(Object o) throws Exception {
        Field[] fields = getFields(o);
        for (Field field : fields) {
            Class<?> type = field.getType();
            field.setAccessible(true);
            if (type == int.class || type == Integer.class) {

            } else if (type == float.class || type == Float.class) {

            } else if (type == double.class || type == Double.class) {

            } else if (type == boolean.class || type == Boolean.class) {

            } else if (type == Date.class) {

            } else if (type == String.class) {

            } else {

            }
            field.setAccessible(false);
        }
        return null;
    }*/

    // insérer dans la table spécifidque à l'objet
    public static void insert(Object o) throws Exception {
        String request = requestToInsert(o);
        // System.out.println(request);
        executeUpdate(request);
    }

    // les requêtes pour insert
    public static String requestToInsert(Object o) throws Exception {
        String request = "INSERT INTO " + getTable(o) + "(";
        String values = " VALUES(";
        Field[] fields = getFields(o);
        int i = 0;
        for (Field field : fields) {
            if (i != 0) {
                request += ",";
                values += ",";
            }
            request += field.getName();
            Class<?> type = field.getType();
            field.setAccessible(true);
            if (type == int.class || type == Integer.class) {
                values += field.getInt(o);
            } else if (type == float.class || type == Float.class) {
                values += field.getFloat(o);
            } else if (type == double.class || type == Double.class) {
                values += field.getDouble(o);
            } else if (type == boolean.class || type == Boolean.class) {
                values += field.getBoolean(o);
            } else if (type == String.class || type == Date.class) {
                values += "'" + field.get(o) + "'";
            } else if (type == FileUpload.class) {
                FileUpload file = (FileUpload) field.get(o);
                if (file != null) {
                    values += "'" + file.getName() + "'";
                    /*request += ",bytes";
                    values += "," + bytesToHexString(file.getFile());
                */} else {
                    values += "''";
                }
            } else { 
                values += field.get(o);
            }
            field.setAccessible(false);
            i++;
        }
        request += ")";
        values += ")";
        return request + values;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // mettre à jour la table pour l'objet (* l'identifiant n'a pas changé et connu)
    public static void update(Object o, int id) throws Exception {
        String request = requestToUpdate(o, id);
        executeUpdate(request);
    }

    // mettre à jour la table pour l'objet (* l'identifiant n'a pas changé et connu)
    public static void update(Object o, String id) throws Exception {
        String request = requestToUpdate(o, id);
        System.out.println(request);
        executeUpdate(request);
    }

    // les requêtes pour update
    public static String requestToUpdate(Object o) throws Exception {
        Field[] fields = getFields(o);
        String request = "UPDATE " + getTable(o) + " SET ";
        int i = 0;
        for (Field field : fields) {
            if (i != 0) {
                request += ",";
            }
            Class<?> type = field.getType();
            field.setAccessible(true);
            request += field.getName() + "=";
            if (type == int.class || type == Integer.class) {
                request += field.getInt(o);
            } else if (type == float.class || type == Float.class) {
                request += field.getFloat(o);
            } else if (type == double.class || type == Double.class) {
                request += field.getDouble(o);
            } else if (type == boolean.class || type == Boolean.class) {
                request += field.getBoolean(o);
            } else if (type == FileUpload.class) {
                FileUpload file = (FileUpload) field.get(o);
                request += "'" + file.getName() + "'";
            } else if (type == String.class || type == Date.class) {
                request += "'" + field.get(o) + "'";
            } else {
                request += field.get(o);
            }
            field.setAccessible(false);
            i++;
        }
        return request;
    }

    public static String requestToUpdate(Object o, int id) throws Exception {
        return requestToUpdate(o) + " WHERE id = " + id;
    }

    // les requêtes pour update
    public static String requestToUpdate(Object o, String id) throws Exception {
        return requestToUpdate(o) + " WHERE id = '" + id + "'";
    }

    // supprimer (* l'identifiant est connu)
    public static void delete(Object o, int id) throws Exception {
        String request = requestToDelete(o, id);
        executeUpdate(request);
    }

    // les requêtes pour delete
    public static String requestToDelete(Object o, int id) {
        return "DELETE FROM " + getTable(o) + " WHERE id =" + id;
    }

    // supprimer (* l'identifiant est connu)
    public static void delete(Object o, String id) throws Exception {
        String request = requestToDelete(o, id);
        executeUpdate(request);
    }

    // les requêtes pour delete
    public static String requestToDelete(Object o, String id) {
        return "DELETE FROM " + getTable(o) + " WHERE id = '" + id + "'";
    }

    // pour executer les requêtes qui retourne des objets
    public static List<Object> executeQuery(Object o, String request) throws Exception {
        List<Object> liste = new ArrayList<>();
        Connection c = null;
        Statement stat = null;
        try {
            c = Connect.postgres();
            stat = c.createStatement();
            ResultSet resultat = stat.executeQuery(request);
            Constructor<?> construct = o.getClass().getDeclaredConstructor();
            Field[] fields = getFields(o);
            while (resultat.next()) {
                Object object = construct.newInstance();
                int i = 0;
                for (Field field : fields) {
                    Class<?> type = field.getType();
                    field.setAccessible(true);
                    if (type == int.class || type == Integer.class) {
                        field.set(object, resultat.getInt(i + 1));
                    } else if (type == float.class || type == Float.class) {
                        field.set(object, resultat.getFloat(i + 1));
                    } else if (type == double.class || type == Double.class) {
                        field.set(object, resultat.getDouble(i + 1));
                    } else if (type == boolean.class || type == Boolean.class) {
                        field.set(object, resultat.getBoolean(i + 1));
                    } else if (type == Date.class) {
                        field.set(object, resultat.getDate(i + 1));
                    } else if (type == String.class) {
                        field.set(object, resultat.getString(i + 1));
                    } else {
                        field.set(object, resultat.getObject(i + 1));
                    }
                    field.setAccessible(false);
                    i++;
                }
                liste.add(object);
            }
            return liste;
        } finally {
            if (stat != null) {
                stat.close();
            }
            if (c != null) {
                c.close();
            }
        }

    }

    // pour executer des requêtes qui modifie la table dans la base de données
    public static void executeUpdate(String request) throws Exception {
        System.out.println(request);
        Connection c = null;
        PreparedStatement stat = null;
        try {
            c = Connect.postgres();
            stat = c.prepareStatement(request);
            int line = stat.executeUpdate();
            if (line < 1)
                throw new Exception("Aucune ligne modifié");
        } finally {
            if (stat != null) {
                stat.close();
            }
            if (c != null) {
                c.close();
            }
        }
    }

}
