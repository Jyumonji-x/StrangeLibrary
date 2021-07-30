package fudan.util;

import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleTest {
    @Test
    void test() {
        double d = 1.111122313412;
        BigDecimal bigDecimal = new BigDecimal(d);
        d = bigDecimal.setScale(2, RoundingMode.CEILING).doubleValue();
        System.out.println(d);
    }
}
