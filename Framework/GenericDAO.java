package etu1748.framework;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import etu1748.framework.annotation.*;

import java.lang.reflect.*;

public class GenericDAO {
    static Connect connect = new Connect();

    // donne le nom de la table
    public static String getTable(Object o) {
        if (o.getClass().isAnnotationPresent(Table.class)) {
            return o.getClass().getAnnotation(Table.class).value();
        }
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
        List<Object> result = executeQuery(o, request, null);
        return result;
    }

    // donne l'objet identifié par un string
    public static List<Object> findById(Object o, Object id) throws Exception {
        String request = "";
        Field idField = getIdField(o);
        System.out.println(id);
        if (idField.getType() == String.class) {
            request = requestFindById(o, String.valueOf(id));
        } else if (idField.getType() == Integer.class || idField.getType() == int.class) {
            request = requestFindById(o, Integer.valueOf(String.valueOf(id)));
        }
        System.out.println(request);
        List<Object> result = executeQuery(o, request, null);
        if (result.size() != 0)
            return result;
        return null;
    }

    // donne l'objet identifié par un string
    public static List<Object> findById(Object o, String id) throws Exception {
        String request = requestFindById(o, id);
        List<Object> result = executeQuery(o, request, null);
        return result;
    }

    // les requêtes pour les fonctions findById
    public static String requestFindById(Object o, int id) throws Exception {
        String idColumn = getIdColumnName(o);
        String request = "SELECT * FROM " + getTable(o) + " WHERE " + idColumn + " = " + id;
        return request;
    }

    public static String requestFindById(Object o, String id) throws Exception {
        String idColumn = getIdColumnName(o);
        String request = "SELECT * FROM " + getTable(o) + " WHERE " + idColumn + " = " + id;
        return request;
    }

    // donne tous les éléments de la table
    public static List<Object> findAll(Object o, Connection c) throws Exception {
        String request = requestFindAll(o);
        List<Object> result = executeQuery(o, request, c);
        return result;
    }

    public static List<Object> findAll(Object o) throws Exception {
        String request = requestFindAll(o);
        List<Object> result = executeQuery(o, request, null);
        return result;
    }

    // donne tous les éléments de la vue représenté par un objet o et pas une table
    public static List<Object> findAll(Object o, String viewName) throws Exception {
        String request = requestFindAll(viewName);
        List<Object> result = executeQuery(o, request, null);
        return result;
    }

    // findAll avec pagination
    public static List<Object> findAll(Object o, Pagination pagination) throws Exception {
        final String request = requestFindAll(o, pagination);
        List<Object> result = executeQuery(o, request, null);
        return result;
    }

    // les requêtes pour les fonctions findAll
    public static String requestFindAll(Object o) {
        return "SELECT * FROM " + getTable(o);
    }

    public static String requestFindAll(Object o, Pagination pagination) throws Exception {
        // si base de données postgresql
        if (pagination != null)
            return "SELECT * FROM " + getTable(o) + " limit " + pagination.getLimit() + " offset "
                    + pagination.getStart();
        else
            throw new Exception("Erreur: pagination nulle");
    }

    public static String requestFindAll(String viewName) {
        return "SELECT * FROM " + viewName;
    }

    // donne tous les éléments d'une table avec une condition particulière
    public static List<Object> findAllSpec(Object o, String condition) throws Exception {
        String request = requestFindAllSpec(o, condition);
        List<Object> result = executeQuery(o, request, null);
        return result;
    }

    // donne tous les éléments d'une vue avec une condition particulière
    public static List<Object> findAllSpec(Object o, String viewName, String condition) throws Exception {
        String request = requestFindAllSpec(o, condition);
        List<Object> result = executeQuery(o, request, null);
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

    // insérer dans la table spécifidque à l'objet
    public static List<Object> insert(Object o) throws Exception {
        String request = requestToInsert(o);
        executeUpdate(request, null);
        return null;
    }

    // les requêtes pour insert
    public static String requestToInsert(Object o) throws Exception {
        String request = "INSERT INTO " + getTable(o) + "(";
        String values = " VALUES(";
        Field[] fields = getFields(o);
        int i = 0;
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GenerationAUTO.class)) {
                if (i != 0) {
                    request += ",";
                    values += ",";
                }
                if (field.isAnnotationPresent(Column.class))
                    request += field.getAnnotation(Column.class).value();
                else if (field.isAnnotationPresent(ForeignKey.class))
                    request += field.getAnnotation(ForeignKey.class).value();
                else
                    request += field.getName();
                Class<?> type = field.getType();
                field.setAccessible(true);
                if (type == String.class || type == Date.class) {
                    values += "'" + field.get(o) + "'";
                } else if (type == FileUpload.class) {
                    FileUpload file = (FileUpload) field.get(o);
                    if (file != null) {
                        values += "'" + file.getName() + "'";
                    } else {
                        values += "''";
                    }
                } else {
                    if (field.isAnnotationPresent(ForeignKey.class)) {
                        Object fk = field.get(o);
                        Field id = getIdField(fk);
                        id.setAccessible(true);
                        if (id.getType() == int.class || id.getType() == Integer.class) {
                            System.out.println(id.get(fk));
                            values += id.get(fk);
                        } else if (id.getType() == String.class) {
                            values += "'" + id.get(fk) + "'";
                        } else {
                            values += id.get(fk);
                        }
                        id.setAccessible(false);
                    } else {
                        values += field.get(o);
                    }
                }
                field.setAccessible(false);
                i++;
            }
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
    public static List<Object> update(Object o, int id) throws Exception {
        String request = requestToUpdate(o, id);
        executeUpdate(request, null);
        List<Object> value = new ArrayList<>();
        value.add(o);
        return value;
    }

    // mettre à jour la table pour l'objet (* l'identifiant n'a pas changé et connu)
    public static List<Object> update(Object o, String id) throws Exception {
        String request = requestToUpdate(o, id);
        executeUpdate(request, null);
        List<Object> value = new ArrayList<>();
        value.add(o);
        return value;
    }

    // les requêtes pour update
    public static String requestToUpdate(Object o) throws Exception {
        Field[] fields = getFields(o);
        String request = "UPDATE " + getTable(o) + " SET ";
        int i = 0;
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Id.class)) {
                if (i != 0) {
                    request += ",";
                }
                Class<?> type = field.getType();
                field.setAccessible(true);
                String fieldName = "";
                if (field.isAnnotationPresent(Column.class))
                    fieldName = field.getAnnotation(Column.class).value();
                else if (field.isAnnotationPresent(ForeignKey.class))
                    fieldName = field.getAnnotation(ForeignKey.class).value();
                else
                    fieldName = field.getName();
                request += fieldName + "=";

                if (type == FileUpload.class) {
                    FileUpload file = (FileUpload) field.get(o);
                    request += "'" + file.getName() + "'";
                } else if (type == String.class || type == Date.class) {
                    request += "'" + field.get(o) + "'";
                } else {
                    if (field.isAnnotationPresent(ForeignKey.class)) {
                        Object fk = field.get(o);
                        Field id = getIdField(fk);
                        id.setAccessible(true);
                        if (id.getType() == int.class || id.getType() == Integer.class) {
                            System.out.println(id.get(fk));
                            request += id.get(fk);
                        } else if (id.getType() == String.class) {
                            request += "'" + id.get(fk) + "'";
                        } else {
                            request += id.get(fk);
                        }
                        id.setAccessible(false);
                    } else {
                        request += field.get(o);
                    }
                }
                field.setAccessible(false);
                i++;
            }
        }
        return request;
    }

    public static String requestToUpdate(Object o, int id) throws Exception {
        String idColumn = getIdColumnName(o);
        return requestToUpdate(o) + " WHERE " + idColumn + " = " + id;
    }

    public static Field getIdField(Object o) throws Exception {
        Field[] fields = getFields(o);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    public static String getIdColumnName(Object o) throws Exception {
        Field[] fields = getFields(o);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class).value();
            }
        }
        return "id";
    }

    // les requêtes pour update
    public static String requestToUpdate(Object o, String id) throws Exception {
        String idColumn = getIdColumnName(o);
        return requestToUpdate(o) + " WHERE " + idColumn + " = '" + id + "'";
    }

    // supprimer (* l'identifiant est connu)
    public static List<Object> delete(Object o, int id) throws Exception {
        String request = requestToDelete(o, id);
        executeUpdate(request, null);
        return null;
    }

    // les requêtes pour delete
    public static String requestToDelete(Object o, int id) throws Exception {
        String idColumn = getIdColumnName(o);
        return "DELETE FROM " + getTable(o) + " WHERE " + idColumn + " =" + id;
    }

    // supprimer (* l'identifiant est connu)
    public static List<Object> delete(Object o, String id) throws Exception {
        String request = requestToDelete(o, id);
        executeUpdate(request, null);
        return null;
    }

    // les requêtes pour delete
    public static String requestToDelete(Object o, String id) throws Exception {
        String idColumn = getIdColumnName(o);
        return "DELETE FROM " + getTable(o) + " WHERE " + idColumn + " = '" + id + "'";
    }

    public static Object setForeignKey(Field field, Object id, Connection c) throws Exception {
        Constructor<?> constructor = field.getType().getDeclaredConstructor();
        Object attribut = constructor.newInstance();
        List<Object> idObjects = findById(attribut, id);
        if (idObjects != null)
            return findById(attribut, id).get(0);
        else
            return null;
    }

    // pour executer les requêtes qui retourne des objets
    public static List<Object> executeQuery(Object o, String request, Connection c) throws Exception {
        System.out.println(request);
        List<Object> liste = new ArrayList<>();
        Statement stat = null;
        boolean newConnection = false;
        try {
            if (c == null) {
                c = Connect.connect();
                newConnection = true;
            }
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
                    String fieldName = field.getName();
                    if (field.isAnnotationPresent(Column.class) == false) {
                        if (field.isAnnotationPresent(ForeignKey.class)) {
                            String column = field.getAnnotation(ForeignKey.class).value();
                            Object fk = resultat.getObject(column);
                            field.set(object, setForeignKey(field, fk, c));
                        } else if (type == int.class || type == Integer.class) {
                            field.set(object, resultat.getInt(fieldName));
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
                    } else {
                        String column = field.getAnnotation(Column.class).value();
                        if (type == int.class || type == Integer.class) {
                            field.set(object, resultat.getInt(column));
                        } else if (type == float.class || type == Float.class) {
                            field.set(object, resultat.getFloat(column));
                        } else if (type == double.class || type == Double.class) {
                            field.set(object, resultat.getDouble(column));
                        } else if (type == boolean.class || type == Boolean.class) {
                            field.set(object, resultat.getBoolean(column));
                        } else if (type == Date.class) {
                            field.set(object, resultat.getDate(column));
                        } else if (type == String.class) {
                            field.set(object, resultat.getString(column));
                        } else {
                            field.set(object, resultat.getObject(column));
                        }
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
            if (c != null && newConnection) {
                c.close();
            }
        }
    }

    // pour executer des requêtes qui modifie la table dans la base de données
    public static void executeUpdate(String request, Connection c) throws Exception {
        System.out.println(request);
        boolean newConnection = false;
        PreparedStatement stat = null;
        try {
            if (c == null) {
                c = Connect.connect();
                newConnection = true;
            }
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
