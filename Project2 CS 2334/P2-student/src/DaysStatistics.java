import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;


/**
 * @author rafal
 * @version 2018/3/30
 *
 * assigns stats
 */
public class DaysStatistics extends StatisticsAbstract
{
    /** list of files to search through **/
    private ArrayList<String> files;
    /** list of valid parameter names **/
    private HashSet<String> params;
    /** hash map that holds all the parameter extrema **/
    private HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>> paramStats;
    /** enum map that holds all tair extrema **/
    private EnumMap<StatType, ArrayList<StatMeasurement>> tairEnums;
    /** enum map that holds all ta9m extrema **/
    private EnumMap<StatType, ArrayList<StatMeasurement>> ta9mEnums;
    /** enum map that holds all srad extrema **/
    private EnumMap<StatType, ArrayList<StatMeasurement>> sradEnums;
    /** list of all tair minimums **/
    private ArrayList<StatMeasurement> tairMinList;
    /** list of all tair maximums **/
    private ArrayList<StatMeasurement> tairMaxList;
    /** list of all tair averages **/
    private ArrayList<StatMeasurement> tairAvgList;
    /** list of all ta9m minimums **/
    private ArrayList<StatMeasurement> ta9mMinList;
    /** list of all ta9m maximums **/
    private ArrayList<StatMeasurement> ta9mMaxList;
    /** list of all ta9m averages **/
    private ArrayList<StatMeasurement> ta9mAvgList;
    /** list of all srad minimums **/
    private ArrayList<StatMeasurement> sradMinList;
    /** list of all srad maximums **/
    private ArrayList<StatMeasurement> sradMaxList;
    /** list of all srad averages **/
    private ArrayList<StatMeasurement> sradAvgList;
    /** list of all srad totals **/
    private ArrayList<StatMeasurement> sradTotList;

    /**
     * ctor
     * @param files files to be used
     */
    public DaysStatistics(String[] files)
    {
        this.files = new ArrayList<String>(Arrays.asList(files));
        
        params = new HashSet<String>();
        // populating hash set for future param checking
        for (ParamType p : ParamType.values())
        {
            params.add(p.name());
        }
        
        paramStats = new HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>>();
        tairEnums = new EnumMap<StatType, ArrayList<StatMeasurement>>(StatType.class);
        ta9mEnums = new EnumMap<StatType, ArrayList<StatMeasurement>>(StatType.class);
        sradEnums = new EnumMap<StatType, ArrayList<StatMeasurement>>(StatType.class);
        
        tairMinList = new ArrayList<StatMeasurement>();
        tairMaxList = new ArrayList<StatMeasurement>();
        tairAvgList = new ArrayList<StatMeasurement>();
        ta9mMinList = new ArrayList<StatMeasurement>();
        ta9mMaxList = new ArrayList<StatMeasurement>();
        ta9mAvgList = new ArrayList<StatMeasurement>();
        sradMinList = new ArrayList<StatMeasurement>();
        sradMaxList = new ArrayList<StatMeasurement>();
        sradAvgList = new ArrayList<StatMeasurement>();
        sradTotList = new ArrayList<StatMeasurement>();
    }

    /**
     * loops through files and finds stats
     * @throws IOException no file
     * @throws WrongCopyrightException bad copyright
     * @throws ParseException can't parse 
     * @throws WrongParameterIdException wrong parameter
     */
    public void findStatistics() throws IOException, WrongCopyrightException, ParseException, WrongParameterIdException
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
     * assign stats to the StatMeasurements and populate maps
     * @param dataStats stats from the day
     * @throws ParseException invalid value
     */
    private void assignStats(DayDataStatistics dataStats) throws ParseException
    {
        addToLists(dataStats);

        tairEnums.put(StatType.MIN, tairMinList);
        tairEnums.put(StatType.MAX, tairMaxList);
        tairEnums.put(StatType.AVG, tairAvgList);
        
        ta9mEnums.put(StatType.MIN, ta9mMinList);
        ta9mEnums.put(StatType.MAX, ta9mMaxList);
        ta9mEnums.put(StatType.AVG, ta9mAvgList);
        
        sradEnums.put(StatType.MIN, sradMinList);
        sradEnums.put(StatType.MAX, sradMaxList);
        sradEnums.put(StatType.AVG, sradAvgList);
        sradEnums.put(StatType.TOT, sradTotList);
        
        paramStats.put(ParamType.TAIR.name(), tairEnums);
        paramStats.put(ParamType.TA9M.name(), ta9mEnums);
        paramStats.put(ParamType.SRAD.name(), sradEnums);
    }

    /**
     * populate lists
     * @param dataStats object of data to pull extrema from
     * @throws ParseException invalid value
     */
    private void addToLists(DayDataStatistics dataStats) throws ParseException
    {
        tairMinList.add(dataStats.getTairMin());
        tairMaxList.add(dataStats.getTairMax());
        tairAvgList.add(dataStats.getTairAverage());
        
        ta9mMinList.add(dataStats.getTa9mMin());
        ta9mMaxList.add(dataStats.getTa9mMax());
        ta9mAvgList.add(dataStats.getTa9mAverage());
        
        sradMinList.add(dataStats.getSolarRadiationMin());
        sradMaxList.add(dataStats.getSolarRadiationMax());
        sradAvgList.add(dataStats.getSolarRadiationAverage());
        sradTotList.add(dataStats.getSolarRadiationTotal());
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
        
        if (params.contains(inParamId))
        {
            for (int index = 0; index < paramStats.get(inParamId).get(StatType.MIN).size() - 1; ++index)
            {
                s = paramStats.get(inParamId).get(StatType.MIN).get(index);
                if (s.isLessThan(paramStats.get(inParamId).get(StatType.MIN).get(index + 1)))
                {
                    out = s;
                }
                else
                {
                    out = paramStats.get(inParamId).get(StatType.MIN).get(index + 1);
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
                "NRMN", inParamId, StatType.MAX);
        StatMeasurement out = new StatMeasurement(-800, new GregorianCalendar(), 
                "NRMN", inParamId, StatType.MAX);
        
        if (params.contains(inParamId))
        {
            for (int index = 0; index < paramStats.get(inParamId).get(StatType.MAX).size() - 1; ++index)
            {
                s = paramStats.get(inParamId).get(StatType.MAX).get(index);
                if (s.isGreaterThan(paramStats.get(inParamId).get(StatType.MAX).get(index + 1)))
                {
                    out = s;
                }
                else
                {
                    out = paramStats.get(inParamId).get(StatType.MAX).get(index + 1);
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
                paramStats.get(ParamType.TAIR.name()).get(StatType.MAX).toString(), 
                paramStats.get(ParamType.TAIR.name()).get(StatType.MIN).toString(), 
                paramStats.get(ParamType.TA9M.name()).get(StatType.MAX).toString(),
                paramStats.get(ParamType.TA9M.name()).get(StatType.MIN).toString(), 
                paramStats.get(ParamType.SRAD.name()).get(StatType.MIN).toString(), 
                paramStats.get(ParamType.SRAD.name()).get(StatType.MAX).toString());
    }

}
