package API;

import java.lang.reflect.Field;

public class GeneralOperations {

    public static String GetTableName(String schema, String tablename){
        return "\"" + schema + "\".\"" + tablename + "\"";
    }


    public static <T> void Update( T Model ){
        String sql = "INSERT INTO \"OFFICE\".\"" + Model.getClass().getName() + "\"(";

        Field[] fields = Model.getClass().getDeclaredFields();

        for(Field field : fields){
            sql += field.getName() + ",";
        }

        sql = sql.substring(0,sql.length() - 1);

        sql += ") VALUES(";




    }

}