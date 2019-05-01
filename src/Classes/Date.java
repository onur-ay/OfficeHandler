package Classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date extends java.util.Date {

    @Override
    public String toString()
    {
        java.util.Date thisDate = new java.util.Date(super.getTime());
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(thisDate);
    }

    public Date(String dateFormat){
        try{
            java.util.Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormat);
            long time = date.getTime();
            super.setTime(time);
        }catch(ParseException e){
            e.printStackTrace();
        }
    }

    public Date(java.util.Date date){
        super.setTime(date.getTime());
    }
}
