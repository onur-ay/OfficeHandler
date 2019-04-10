package API;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/OfficeHandlerDB";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Sher76796775ed.";

    public static <T> void Insert(T Model){
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\"(\"");
        Field[] fields = Model.getClass().getDeclaredFields();

        for(Field field : fields)
            if(!field.getName().substring(field.getName().lastIndexOf('.')+1).equals("ID"))
                sqlQuery.append(field.getName()).append("\",\"");

        sqlQuery = new StringBuilder(sqlQuery.substring(0,sqlQuery.length()-2));
        sqlQuery.append(") VALUES(");

        for(Field field : fields){
            if(!field.getName().substring(field.getName().lastIndexOf('.')+1).equals("ID")){
                StringBuilder type = new StringBuilder(field.getType().getName());
                boolean isQuoted = type.toString().contains("int") || type.toString().contains("double") || type.toString().contains("boolean");
                if(!isQuoted)
                    sqlQuery.append("'{");
                sqlQuery.append(getDeclaredFieldValue(field, Model).toString());
                if(!isQuoted)
                    sqlQuery.append("}'");
                sqlQuery.append(",");
            }
        }
        sqlQuery.deleteCharAt(sqlQuery.length()-1);
        sqlQuery.append(");");

        executeUpdate(sqlQuery);


    }

    public static <T> void Update(T Model, String [][] updatedFields, String[][] conditions){
        StringBuilder sqlQuery = new StringBuilder("UPDATE \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\" SET ");
        for(String[] field : updatedFields){
            sqlQuery.append("\"").append(field[1]).append("\"=");
            if(field[0].equals("string"))
                sqlQuery.append("'{").append(field[2]).append("}', ");
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
                    sqlQuery.append("'{").append(condition[2]).append("}' AND ");
                else
                    sqlQuery.append(condition[2]).append(" AND ");
            }
            sqlQuery = new StringBuilder(sqlQuery.substring(0, sqlQuery.length()-5)).append(";");
        }

        executeUpdate(sqlQuery);
    }

    public static <T> ResultSet Select(T Model, @Nullable String[][] conditions){
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM \"OFFICE\".\"" + Model.getClass().getName().substring(Model.getClass().getName().lastIndexOf('.')+1) + "\"");

        if(conditions == null){
            sqlQuery.append(";");
        }else{
            sqlQuery.append(" WHERE ");
            for(String[] condition : conditions){
                sqlQuery.append("\"").append(condition[1]).append("\"");
                if(condition[3].equals("1"))
                    sqlQuery.append(" = ");
                else
                    sqlQuery.append(" <> ");
                if(condition[0].equals("string"))
                    sqlQuery.append("'{").append(condition[2]).append("}' AND ");
                else
                    sqlQuery.append(condition[2]).append(" AND ");
            }
            sqlQuery = new StringBuilder(sqlQuery.substring(0, sqlQuery.length()-5)).append(";");
        }

        return executeSelect(sqlQuery);
    }

    private static <T> Object getDeclaredFieldValue(Field field, T Model){
        StringBuilder className = new StringBuilder(Model.getClass().getName());
        StringBuilder fieldName = new StringBuilder(field.getName().replace(className.toString(),""));
        StringBuilder methodName;

        for (Method method : Model.getClass().getMethods()){
            methodName = new StringBuilder(method.getName().replace(className.toString(),""));

            if (methodName.toString().startsWith("get") && (methodName.length() == fieldName.length()+3)) {
                if (methodName.toString().endsWith(fieldName.toString())) {
                    try {
                        return method.invoke(Model);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println("Could not determine method: " + method.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
        return "null";
    }

    private static void executeUpdate(StringBuilder query){
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}