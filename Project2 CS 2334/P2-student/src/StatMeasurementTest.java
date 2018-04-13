import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;

import org.junit.Test;

/***
 * 
 * @author Kyle Murphey
 * @version 2018-11-4
 * 
 * Testing the StatMeasurement class
 *
 */
public class StatMeasurementTest
{

    /**
     * testing toString for StatMeasurement and Measurement
     */
    @Test
    public void testToString()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        Assert.assertEquals("TAIR AVG 4.0000 NRMN 2018-12-10T07:20:00 UTC\n", s.toString());
        
        Measurement m = new Measurement(4.0);
        Measurement n = new Measurement(-999.9);
        Assert.assertEquals("4.0000", m.toString());
        Assert.assertEquals("NaN", n.toString());
    }

    /**
     * testing StatMeasurement ctor
     */
    @Test
    public void testStatMeasurement()
    {
        StatMeasurement empty = new StatMeasurement();
        Assert.assertEquals(Double.NaN, empty.getValue(), 0.01);
        
        Assert.assertEquals("nada", empty.toString());
    }

    /**
     * testing StatMeasurement ctor with params
     */
    @Test
    public void testStatMeasurementDoubleGregorianCalendarStringStringStatType()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(), "NRMN", "TAIR", StatType.AVG);
        Assert.assertEquals(4.0, s.getValue(), 0.01);
    }

    /**
     * Testing the return dated
     */
    @Test
    public void testGetDateTimeOfMeasurment()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(new GregorianCalendar(2018, 11, 10, 1, 20).getTime());
        g.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        SimpleDateFormat date = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        date.setCalendar(g);
        date.format(g.getTime());
        
        Assert.assertEquals(g, s.getDateTimeOfMeasurment());
    }

    /**
     * Testing the ParamId
     */
    @Test
    public void testSetAndGetParamId()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        s.setParamId("TA9M");
        
        Assert.assertEquals("TA9M", s.getParamId());
    }

    /**
     * Testing the StatType
     */
    @Test
    public void testSetAndGetStatType()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        s.setStatType(StatType.TOT);
        
        Assert.assertEquals(StatType.TOT, s.getStatType());
    }

    /**
     * Testing if less than
     */
    @Test
    public void testIsLessThan()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        StatMeasurement q = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        StatMeasurement t = new StatMeasurement(5.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        StatMeasurement e = new StatMeasurement();
        
        Assert.assertEquals(true, s.isLessThan(t));
        Assert.assertEquals(false, t.isLessThan(s));
        
        Assert.assertEquals(false, e.isLessThan(q));
        Assert.assertEquals(true, s.isLessThan(e));
    }

    /**
     * Testing if greater than
     */
    @Test
    public void testIsGreaterThan()
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        StatMeasurement q = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        StatMeasurement t = new StatMeasurement(5.0, new GregorianCalendar(2018, 10, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        StatMeasurement e = new StatMeasurement(-999.9, new GregorianCalendar(2018, 10, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        Assert.assertEquals(true, t.isGreaterThan(s));
        Assert.assertEquals(false, s.isGreaterThan(t));
        
        Assert.assertEquals(false, e.isGreaterThan(q));
        Assert.assertEquals(true, s.isGreaterThan(e));
    }

    /**
     * Testing if the file is newer than the inputted date
     * @throws ParseException bad date
     */
    @Test
    public void testNewerThan() throws ParseException
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        
        Assert.assertEquals(true, s.newerThan("2017-01-01T01:00:00 UTC"));
        Assert.assertEquals(false, s.newerThan("2019-01-01T01:00:00 UTC"));
        
    }

    /**
     * Testing if file is older than the inputted date
     * @throws ParseException bad date
     */
    @Test
    public void testOlderThan() throws ParseException
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        Assert.assertEquals(true, s.olderThan("2019-01-01T01:00:00 UTC"));
        Assert.assertEquals(false, s.olderThan("2017-01-01T01:00:00 UTC"));
    }

    /**
     * Testing outputted values for the comparison
     * @throws ParseException bad date
     */
    @Test
    public void testCompareWithTimeString() throws ParseException
    {
        StatMeasurement s = new StatMeasurement(4.0, new GregorianCalendar(2018, 11, 10, 1, 20), 
                "NRMN", "TAIR", StatType.AVG);
        
        Assert.assertEquals(0, s.compareWithTimeString("2018-12-10T07:20:00 UTC"));
    }

}
