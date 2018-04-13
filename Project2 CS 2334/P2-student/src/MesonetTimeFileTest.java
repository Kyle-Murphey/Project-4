import org.junit.Assert;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * 
 * testing project
 *
 */

public class MesonetTimeFileTest
{
    /**
     * testing the project
     * @throws IOException bad file
     * @throws WrongCopyrightException bad cr
     * @throws ParseException bad input
     */
    
    @Test
    public void datesTest() throws IOException, WrongCopyrightException, ParseException
    {
        MesonetTimeFile m = new MesonetTimeFile("P2-student/data/mesonet/20180101nrmn.mts");
        m.parseFile();
        
        GregorianCalendar g = new GregorianCalendar();
        SimpleDateFormat date = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        g.setTimeZone(TimeZone.getTimeZone("UTC"));
        date.setCalendar(g);
        
        MesonetTimeFile n = new MesonetTimeFile("P2-student/data/mesonet/20180101nrmn.mts");
        MesonetTimeFile s = new MesonetTimeFile("P2-student/data/mesonet/20180103stil.mts");
        n.parseFile();
        
        s.parseFile();
        
        Assert.assertEquals("2018-01-01T00:00:00 UTC", n.getStarDateTimeStringFromFile());
        Assert.assertEquals("2018-01-01T00:00:00 UTC", n.getDateTimeString());
        
        Assert.assertEquals("2018-01-03T00:00:00 UTC", s.getStarDateTimeStringFromFile());
        
        
        StatMeasurement stat = new StatMeasurement();
        StatMeasurement statM = new StatMeasurement(99.9, new GregorianCalendar(), "NRMN", "TAIR", StatType.AVG);
        
        Assert.assertEquals(false, stat.isValid());
        Assert.assertEquals("TAIR", statM.getParamId());
        Assert.assertEquals(StatType.AVG, statM.getStatType());
        Assert.assertEquals(true, statM.isGreaterThan(stat));
        Assert.assertEquals(true, statM.isLessThan(stat));
        Assert.assertEquals(true, statM.newerThan("2018-01-01T00:00:00 UTC"));
        Assert.assertEquals(false, statM.olderThan("2018-01-01T00:00:00 UTC"));
     
        
    }
    
    /**
     * testing toString (make sure to change date later)
     */
    @Test
    public void csAbstractTest()
    {
        CsFile c = new CsFile("P2-student/data/mesonet/20180101nrmn.mts");
        
        SimpleDateFormat df = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateOut = df.format(new Date(c.getDateModified()));
        
        Assert.assertEquals("File name: P2-student/data/mesonet/20180101nrmn.mts\n" + 
                "Exists: true\n" + 
                "Last modified: " + dateOut, c.toString());
    }
    
    /***
     * testing cs file
     * @throws ParseException invalid file or input
     */
    @Test
    public void csTest() throws ParseException
    {

        // From CsFileTest
        SimpleDateFormat converter = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        
        
        CsFile file = new CsFile("test");
        Assert.assertEquals(false, file.exists());
        Assert.assertEquals("test", file.getFileName());
        
        GregorianCalendar cal = new GregorianCalendar();
        converter.setCalendar(cal);
        converter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        CsFile norman = new CsFile("P2-student/data/mesonet/20180101nrmn.mts");
        
        CsFile okce = new CsFile("P2-student/data/mesonet/20180101okce.mts");
        
        Assert.assertEquals(true, okce.olderThan("2020-11-10T23:12:15 UTC"));
        Assert.assertEquals(true, norman.olderThan("2018-11-10T23:12:15 UTC"));
        Assert.assertEquals(false, okce.newerThan("2020-11-10T23:12:15 UTC"));
        Assert.assertEquals(true, norman.newerThan("2001-11-10T23:12:15 UTC"));
        Assert.assertEquals(false, norman.olderThan("2018-03-07T21:40:17 UTC"));
        

    }
    
    /**
     * Check if file exists
     * @throws WrongCopyrightException wrong copyright
     */
    @Test
    public void ifFileDoesNotExist() throws WrongCopyrightException
    {
        try
        {
            MesonetTimeFile m = new MesonetTimeFile("P2-student/data/mesonet/2010101nrmn.mts");
            m.parseFile();
            Assert.fail("File does not exist");
        }
        catch (IOException e)
        {
            // test succeeded
        }
    }
    
    /**
     * Testing the copyright
     * @throws IOException bad file
     */
    @Test
    public void copyrightTest() throws IOException
    {
        try {
            MesonetTimeFile m = new MesonetTimeFile("P2-student/data/20180101nrmn.csv");
            m.parseFile();
            Assert.fail("Should not find copyright");
        }
        catch (WrongCopyrightException e)
        {
            // succeed
        }
        
    }

}
