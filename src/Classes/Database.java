package Classes;

import com.sun.istack.internal.Nullable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;
import sun.misc.Regexp;

import java.nio.file.Path;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/OfficeHandlerDB";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Sher76796775ed.";

    public static <T> boolean Insert(T Model) throws IllegalAccessException {
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\"(\"");
        Field[] fields = Model.getClass().getDeclaredFields();

        for(Field field : fields)
            if(!field.getName().equals("ID") && !field.getName().equals("DeleteButton"))
                sqlQuery.append(field.getName()).append("\",\"");

        sqlQuery = new StringBuilder(sqlQuery.substring(0,sqlQuery.length()-2));
        sqlQuery.append(") VALUES(");

        for(Field field : fields){
            if(!field.getName().equals("ID") && !field.getName().equals("DeleteButton")){
                StringBuilder type = new StringBuilder(field.getType().getName());
                boolean isQuoted = !(type.toString().toLowerCase().contains("int") || type.toString().toLowerCase().contains("double") || type.toString().toLowerCase().contains("boolean"));
                if(isQuoted)
                    sqlQuery.append("'");
                field.setAccessible(true);
                sqlQuery.append(field.get(Model).toString().replace("'","''"));
                field.setAccessible(false);
                if(isQuoted)
                    sqlQuery.append("'");
                sqlQuery.append(",");
            }
        }
        sqlQuery.deleteCharAt(sqlQuery.length()-1);
        sqlQuery.append(");");

        return executeUpdate(sqlQuery);
    }

    public static <T> boolean Update(T Model, String [][] updatedFields, String[][] conditions){
        StringBuilder sqlQuery = new StringBuilder("UPDATE \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\" SET ");
        for(String[] field : updatedFields){
            sqlQuery.append("\"").append(field[1]).append("\"=");
            if(field[0].equals("string"))
                sqlQuery.append("'").append(field[2]).append("', ");
            else
                sqlQuery.append(field[2]).append(", ");
        }
        sqlQuery = new StringBuilder(sqlQuery.substring(0,sqlQuery.length()-2));

        if(conditions == null)
            sqlQuery.append(";");
        else{
            sqlQuery.append(" WHERE ");
            for(String[] condition : conditions){
                sqlQuery.append("\"").append(condition[1]).append("\"");
                if(condition[3].equals("1"))
                    sqlQuery.append("=");
                else
                    sqlQuery.append("<>");
                if(condition[0].equals("string"))
                    sqlQuery.append("'").append(condition[2]).append("' AND ");
                else
                    sqlQuery.append(condition[2]).append(" AND ");
            }
            sqlQuery = new StringBuilder(sqlQuery.substring(0, sqlQuery.length()-5)).append(";");
        }

        return executeUpdate(sqlQuery);
    }

    public static <T> boolean Update(T Model) throws IllegalAccessException {
        StringBuilder sqlQuery = new StringBuilder("UPDATE \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\" SET ");
        String className = Model.getClass().getName();
        Field[] fields = Model.getClass().getDeclaredFields();
        for(int i=1; i< (Model.getClass().getName().equals("Model.File") ? fields.length-1 : fields.length) ; i++)
            if(!fields[i].getName().equals("DeleteButton")){
                sqlQuery.append("\"").append(fields[i].getName().replace(className,"")).append("\"=");
                boolean isQuoted = !(fields[i].getType().getName().toLowerCase().contains("int") || fields[i].getType().getName().toLowerCase().contains("double") || fields[i].getType().getName().toLowerCase().contains("boolean"));
                if(isQuoted)
                    sqlQuery.append("'");
                fields[i].setAccessible(true);
                sqlQuery.append(fields[i].get(Model).toString());
                fields[i].setAccessible(false);
                if(isQuoted)
                    sqlQuery.append("'");
                sqlQuery.append(",");
            }
        fields[0].setAccessible(true);
        sqlQuery = new StringBuilder(sqlQuery.substring(0,sqlQuery.length()-1)).append(" WHERE ");
        sqlQuery.append("\"").append(fields[0].getName().substring(fields[0].getName().lastIndexOf('.')+1)).append("\"=");
        sqlQuery.append(fields[0].get(Model)).append(";");
        fields[0].setAccessible(false);

        return executeUpdate(sqlQuery);
    }

    public static <T> ObservableList<T> Select(T Model, @Nullable String[][] conditions) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ParseException {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\"");

        if(conditions == null){
            sqlQuery.append(";");
        }else{
            sqlQuery.append(" WHERE ");
            for(String[] condition : conditions){
                sqlQuery.append("\"").append(condition[1]).append("\"");
                if(condition[3].equals("0"))
                    sqlQuery.append(" <> ");
                else if(condition[3].equals("1"))
                    sqlQuery.append(" = ");
                else
                    sqlQuery.append("::text LIKE ");

                if(condition[3].equals("2"))
                    sqlQuery.append("'%").append(condition[2]).append("%' AND ");
                else if(condition[0].equals("string"))
                    sqlQuery.append("'").append(condition[2]).append("' AND ");
                else
                    sqlQuery.append(condition[2]).append(" AND ");
            }
            sqlQuery = new StringBuilder(sqlQuery.substring(0, sqlQuery.length()-5)).append(";");
        }

        ResultSet rs = executeSelect(sqlQuery);
        if (rs != null && rs.next())
            return convertToObjectArray(rs, Model);
        return null;
    }

    public static <T> boolean Delete(T Model) throws NoSuchFieldException, IllegalAccessException{
        Field id = Model.getClass().getDeclaredField("ID");
        StringBuilder sqlQuery = new StringBuilder("DELETE FROM \"OFFICE\".\"");
        sqlQuery.append(Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1));
        sqlQuery.append("\" WHERE \"ID\"=");
        id.setAccessible(true);
        sqlQuery.append(id.get(Model)).append(";");
        id.setAccessible(false);
        return executeUpdate(sqlQuery);
    }

    private static boolean executeUpdate(StringBuilder query){
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query.toString()) > 0;
        } catch (SQLException e) {
            if(!e.toString().toLowerCase().contains("unique"))
                System.out.println(e.toString());
        }
        return false;
    }

    private static ResultSet executeSelect(StringBuilder query){
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Connection connect() throws SQLException{
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static <T> ObservableList<T> convertToObjectArray(ResultSet rs, T Model) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] fields = Model.getClass().getDeclaredFields();
        String fieldName;
        String className = Model.getClass().getName();
        ObservableList<T> result = FXCollections.observableArrayList();
        Constructor<?> ctor;
        Class[] fieldTypes = Model.getClass().getName().equals("Model.File") ? new Class[fields.length-1] : new Class[fields.length];
        Object[] fieldValues = Model.getClass().getName().equals("Model.File") ? new Object[fields.length-1] : new Object[fields.length];
        do{
            int i = 0;
            for(Field field : fields)
                if(!field.getName().equals("DeleteButton")){
                    fieldName = field.getName().replace(className,"");
                    fieldTypes[i] = field.getType();
                    if(field.getType().equals(Path.class))
                        fieldValues[i] = Paths.get(rs.getString(fieldName));
                    else
                        fieldValues[i] = field.getType().getConstructor(String.class).newInstance(rs.getString(fieldName));
                    i++;
                }

            ctor = Model.getClass().getConstructor(fieldTypes);
            Object newModel = ctor.newInstance(fieldValues);
            result.add((T) newModel);
        }while(rs.next());
        return result;
    }
}