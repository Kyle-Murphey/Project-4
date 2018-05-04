import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;

import org.junit.Test;

/**
 * 
 * @author Kyle Murphey
 * @version 2018/11/4
 *
 */
public class DayDataStatisticsTest
{

    /**
     * Testing the rest of DaydataStatistics
     * @throws WrongTimeZoneException wrong tz
     * @throws WrongParameterIdException wrong parameter
     */
    @Test
    public void notValidTest() throws WrongTimeZoneException, WrongParameterIdException
    {
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeZone(TimeZone.getTimeZone("UTC"));
        ArrayList<TimeData> d = new ArrayList<>();
        d.add(new TimeData("NRMN", g, new Measurement(-999.9), 
                new Measurement(-999.9), new Measurement(-999.9), new Measurement(-999.9)));
        DayDataStatistics data = new DayDataStatistics(d);
        
        Assert.assertEquals("NRMN", data.getStationID());
        Assert.assertEquals("TAIR MIN: NaN\n" + 
                "TAIR MAX: NaN\n" + 
                "TAIR AVG: NaN\n" + 
                "TA9M MIN: NaN\n" + 
                "TA9M MAX: NaN\n" + 
                "TA9M AVG: NaN\n" + 
                "SRAD MIN: NaN\n" + 
                "SRAD MAX: NaN\n" + 
                " SRAD AVG: NaN\n" + 
                "SRAD TOT: 0.0000", data.toString());
    }

}
