import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;


/**
 * @author rafal
 * @version 2018/3/30
 *
 * assigns stats
 */
public class DaysStatistics extends StatisticsAbstract
{
    private ArrayList<String> files;

    private ArrayList<StatMeasurement> tairMinStats;
    private ArrayList<StatMeasurement> tairAvgStats;
    private ArrayList<StatMeasurement> tairMaxStats;

    private ArrayList<StatMeasurement> ta9mMinStats;
    private ArrayList<StatMeasurement> ta9mAvgStats;
    private ArrayList<StatMeasurement> ta9mMaxStats;

    private ArrayList<StatMeasurement> sradMinStats;
    private ArrayList<StatMeasurement> sradAvgStats;
    private ArrayList<StatMeasurement> sradMaxStats;
    private ArrayList<StatMeasurement> sradTotalStats;

    /**
     * ctor
     * @param files files to be used
     */
    public DaysStatistics(String[] files)
    {
        this.files = new ArrayList<String>(Arrays.asList(files));
        
        tairMinStats = new ArrayList<StatMeasurement>();
        tairMaxStats = new ArrayList<StatMeasurement>();
        tairAvgStats = new ArrayList<StatMeasurement>();
        
        ta9mMinStats = new ArrayList<StatMeasurement>();
        ta9mMaxStats = new ArrayList<StatMeasurement>();
        ta9mAvgStats = new ArrayList<StatMeasurement>();
        
        sradMinStats = new ArrayList<StatMeasurement>();
        sradMaxStats = new ArrayList<StatMeasurement>();
        sradAvgStats = new ArrayList<StatMeasurement>();
        sradTotalStats = new ArrayList<StatMeasurement>();
    }

    /**
     * loops through files and finds stats
     * @throws IOException no file
     * @throws WrongCopyrightException bad copyright
     * @throws ParseException can't parse 
     */
    public void findStatistics() throws IOException, WrongCopyrightException, ParseException
    {
        for (String fileName : files)
        {
            MesonetTimeFile mtsFile = new MesonetTimeFile(fileName);
            mtsFile.parseFile();
            ArrayList<TimeData> data = mtsFile.parseFile();
            DayDataStatistics dataStats = new DayDataStatistics(data);

            assignStats(dataStats);
        }
    }

    /**
     * assign stats to the StatMeasurements
     * @param dataStats stats from the day
     * @throws ParseException invalid value
     */
    private void assignStats(DayDataStatistics dataStats) throws ParseException
    {
        
        tairMinStats.add(dataStats.getTairMin());
        tairMaxStats.add(dataStats.getTairMax());
        tairAvgStats.add(dataStats.getTairAverage());
        
        ta9mMinStats.add(dataStats.getTa9mMin());
        ta9mMaxStats.add(dataStats.getTa9mMax());
        ta9mAvgStats.add(dataStats.getTa9mAverage());
        
        sradMinStats.add(dataStats.getSolarRadiationMin());
        sradMaxStats.add(dataStats.getSolarRadiationMax());
        sradAvgStats.add(dataStats.getSolarRadiationAverage());
        sradTotalStats.add(dataStats.getSolarRadiationTotal());
    }

    /**
     * get the date that the min occurred
     * @param inParamId tair,ta9m,srad
     * @return the StatMeasurement with min
     */
    @Override
    public StatMeasurement getMinimumDay(String inParamId) throws WrongParameterIdException
    {
        StatMeasurement s = new StatMeasurement(9999, new GregorianCalendar(), 
                "NRMN", inParamId, StatType.MIN);
        StatMeasurement out = new StatMeasurement(9999, new GregorianCalendar(), 
                "NRMN", inParamId, StatType.MIN);
        
        if (inParamId.equals("TAIR"))
        {
            
            for (int i = 0; i < tairMinStats.size() - 1; ++i)
            {
                s = tairMinStats.get(i);
                
                if (s.isLessThan(tairMinStats.get(i + 1)))
                {
                    out = s;
                }
                else
                {
                    out = tairMinStats.get(i + 1);
                }
            } 
            return out;
        }
        else if (inParamId.equals("TA9M"))
        {
            for (int i = 0; i < ta9mMinStats.size() - 1; ++i)
            {
                s = ta9mMinStats.get(i);
                
                if (s.isLessThan(ta9mMinStats.get(i + 1)))
                {
                    out = s;
                }
                else
                {
                    out = ta9mMinStats.get(i + 1);
                }
            }
            return out;
        }
        else if (inParamId.equals("SRAD"))
        {
            for (int i = 0; i < sradMinStats.size() - 1; ++i)
            {
                s = sradMinStats.get(i);
                
                if (s.isLessThan(sradMinStats.get(i + 1)))
                {
                    out = s;
                }
                else
                {
                    out = sradMinStats.get(i + 1);
                }
            }
            return out;
        }
        else
        {
            throw new WrongParameterIdException();
        }
    }

    /**
     * get the day the max occurred
     * @param inParamId tair,ta9m,srad
     * @return the stat meas. in which max occurred
     */
    @Override
    public StatMeasurement getMaximumDay(String inParamId) throws WrongParameterIdException
    {
        StatMeasurement s = new StatMeasurement(-800, new GregorianCalendar(), 
                "NRMN", inParamId, StatType.MIN);
        StatMeasurement out = new StatMeasurement(-800, new GregorianCalendar(), 
                "NRMN", inParamId, StatType.MIN);
        
        if (inParamId.equals("TAIR"))
        {
            
            for (int i = 0; i < tairMaxStats.size() - 1; ++i)
            {
                s = tairMaxStats.get(i);
                
                if (s.isGreaterThan(tairMaxStats.get(i + 1)))
                {
                    out = s;
                }
                else
                {
                    out = tairMaxStats.get(i + 1);
                }
            }
            return out;
        }
        else if (inParamId.equals("TA9M"))
        {
            for (int i = 0; i < ta9mMaxStats.size() - 1; ++i)
            {
                s = ta9mMaxStats.get(i);
                
                if (s.isGreaterThan(ta9mMaxStats.get(i + 1)))
                {
                    out = s;
                }
                else
                {
                    out = ta9mMaxStats.get(i + 1);
                }
            }
            return out;
        }
        else if (inParamId.equals("SRAD"))
        {
            for (int i = 0; i < sradMaxStats.size() - 1; ++i)
            {
                s = sradMaxStats.get(i);
                
                if (s.isGreaterThan(sradMaxStats.get(i + 1)))
                {
                    out = s;
                }
                else
                {
                    out = sradMaxStats.get(i + 1);
                }
            }
            return out;
        }
        else
        {
            throw new WrongParameterIdException();
        }
    }

    /**
     * combine the dates
     * @param paramId tair,ta9m,srad
     * @return string output
     * @throws WrongParameterIdException wrong param
     */
    public String combineMinMaxStatistics(String paramId) throws WrongParameterIdException
    {       
        StatMeasurement maximumDay = getMaximumDay(paramId);
        StatMeasurement miniumuDay = getMinimumDay(paramId);
        return maximumDay.toString() + "\n" + miniumuDay.toString() + "\n";
    }
    /**
     * get the stats in output
     * @return string of stats
     */
    @Override
    public String toString()
    {
        return String.format("   ID  STAT      VALUE  STID       DATE T TIME     TZ\n"
                + "-----------------------------------------------------\n"
                + "%s\n%s\n%s\n%s\n%s\n%s",
                tairMaxStats.toString(), tairMinStats.toString(), ta9mMaxStats.toString(),
                ta9mMinStats.toString(), sradMinStats.toString(), sradMaxStats.toString());
    }

}
