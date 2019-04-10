package ReqFunc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Maths {

    public static double round(double value, int numOfDigit) {
        if (numOfDigit < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(numOfDigit, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
