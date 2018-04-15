import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;

import org.junit.Test;

/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * testing the project
 *
 */
public class DaysStatisticsTest
{
 
    /**
     * testing stats
     * @throws IOException no file
     * @throws WrongCopyrightException wrong cr
     * @throws ParseException bad value
     * @throws WrongParameterIdException wrong param
     */
    @Test
    public void dayStatTest() throws IOException, WrongCopyrightException, ParseException, WrongParameterIdException
    {
        String[] files = new String[3];
        files[0] = "P2-student/data/mesonet/20180102stil.mts";
        files[1] = "P2-student/data//mesonet//20180102okcn.mts";
        files[2] = "P2-student/data/mesonet/20180102okce.mts";
        //String[] files = {"P2-student/data/mesonet/20180102stil.mts", 
          //      "P2-student/data//mesonet//20180102okcn.mts", "P2-student/data/mesonet/20180102okce.mts"};
        DaysStatistics d = new DaysStatistics(files);
        
        d.findStatistics();
        Assert.assertEquals("TAIR MAX -6.2000 OKCE 2018-01-02T21:30:00 UTC\n",
                d.getMaximumDay("TAIR").toString());
        Assert.assertEquals("TAIR MIN -13.3000 OKCE 2018-01-02T08:05:00 UTC\n", d.getMinimumDay("TAIR").toString());
        Assert.assertEquals("TA9M MAX -6.4000 OKCE 2018-01-02T22:20:00 UTC\n", d.getMaximumDay("TA9M").toString());
        Assert.assertEquals("TA9M MIN -12.4000 OKCE 2018-01-02T08:20:00 UTC\n", d.getMinimumDay("TA9M").toString());
        Assert.assertEquals("SRAD MIN 0.0000 OKCE 2018-01-02T00:00:00 UTC\n", d.getMinimumDay("SRAD").toString());
        Assert.assertEquals("SRAD MAX 254.0000 OKCE 2018-01-02T18:55:00 UTC\n", d.getMaximumDay("SRAD").toString());
        
        Assert.assertEquals("TAIR MAX -6.2000 OKCE 2018-01-02T21:30:00 UTC\n\n"
                + "TAIR MIN -13.3000 OKCE 2018-01-02T08:05:00 UTC\n\n", d.combineMinMaxStatistics("TAIR"));

        Assert.assertEquals("   ID  STAT      VALUE  STID       DATE T TIME     TZ\n" + 
                "-----------------------------------------------------\n" + 
                "[TAIR MAX -5.3000 STIL 2018-01-02T21:40:00 UTC\n" + 
                ", TAIR MAX -6.4000 OKCN 2018-01-02T21:35:00 UTC\n" + 
                ", TAIR MAX -6.2000 OKCE 2018-01-02T21:30:00 UTC\n" + 
                "]\n" + 
                "[TAIR MIN -11.5000 STIL 2018-01-02T08:05:00 UTC\n" + 
                ", TAIR MIN -12.7000 OKCN 2018-01-02T07:10:00 UTC\n" + 
                ", TAIR MIN -13.3000 OKCE 2018-01-02T08:05:00 UTC\n" + 
                "]\n" + 
                "[TA9M MAX -5.6000 STIL 2018-01-02T21:40:00 UTC\n" + 
                ", TA9M MAX -6.6000 OKCN 2018-01-02T21:05:00 UTC\n" + 
                ", TA9M MAX -6.4000 OKCE 2018-01-02T22:20:00 UTC\n" + 
                "]\n" + 
                "[TA9M MIN -11.3000 STIL 2018-01-02T08:20:00 UTC\n" + 
                ", TA9M MIN -12.1000 OKCN 2018-01-02T06:25:00 UTC\n" + 
                ", TA9M MIN -12.4000 OKCE 2018-01-02T08:20:00 UTC\n" + 
                "]\n" + 
                "[SRAD MIN 0.0000 STIL 2018-01-02T00:00:00 UTC\n" + 
                ", SRAD MIN 0.0000 OKCN 2018-01-02T00:00:00 UTC\n" + 
                ", SRAD MIN 0.0000 OKCE 2018-01-02T00:00:00 UTC\n" + 
                "]\n" + 
                "[SRAD MAX 447.0000 STIL 2018-01-02T18:50:00 UTC\n" + 
                ", SRAD MAX 252.0000 OKCN 2018-01-02T18:45:00 UTC\n" + 
                ", SRAD MAX 254.0000 OKCE 2018-01-02T18:55:00 UTC\n" + 
                "]", d.toString());
    }
    
    /**
     * Testing the exception
     * @throws IOException bad file
     * @throws WrongCopyrightException bad cr
     * @throws ParseException bad input
     */
    @Test
    public void wrongMinParamTest() throws IOException, WrongCopyrightException, ParseException
    {
        try 
        {
            String[] files = { "P2-student/data//mesonet//20180102okcn.mts", 
                "P2-student/data//mesonet//20180103okcn.mts", "P2-student/data/mesonet/20180102stil.mts",
                "P2-student/data/mesonet/20180102okce.mts", "P2-student/data/mesonet/20180101okce.mts" };
            DaysStatistics d = new DaysStatistics(files);
            
            d.findStatistics();
            d.getMinimumDay("fail");
            Assert.fail("Wrong param");
        }
        catch (WrongParameterIdException e)
        {
            //succeed
        }
        
        
    }
    
    /**
     * Testing the exception
     * @throws IOException bad file
     * @throws WrongCopyrightException bad cr
     * @throws ParseException bad input
     */
    @Test
    public void wrongParamMaxTest() throws IOException, WrongCopyrightException, ParseException
    {
        try 
        {
            String[] files = { "P2-student/data/mesonet/20180102stil.mts",
                "P2-student/data//mesonet//20180102okcn.mts", "P2-student/data/mesonet/20180102okce.mts"};
            DaysStatistics d = new DaysStatistics(files);
            
            d.findStatistics();
            d.getMaximumDay("fail");
            Assert.fail("Wrong param");
        }
        catch (WrongParameterIdException e)
        {
            //succeed
            e = new WrongParameterIdException("fail");
        }
    }

}
