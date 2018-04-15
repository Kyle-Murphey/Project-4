import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Calculates statistics
 * @author Kyle Murphey
 * @version 2018/3/30
 *
 */
public class DayDataStatistics
{
    /** The set of data. */
    private ArrayList<TimeData> data;
    
    /** Map of various StatMeasurements and extrema **/
    private HashMap<ParamType, EnumMap<StatType, StatMeasurement>> paramStats;

    /** Station Id **/
    private String stationId = "nada";
    

    /**
     * ctor
     * @param inData list of time date to find stats
     * @throws WrongParameterIdException wrong parameter
     */
    public DayDataStatistics(ArrayList<TimeData> inData) throws WrongParameterIdException
    {
        data = inData;
        stationId = data.get(0).getStationID();
        
        paramStats = new HashMap<ParamType, EnumMap<StatType, StatMeasurement>>();
        
        calculateStatistics(ParamType.TAIR);
        calculateStatistics(ParamType.TA9M);
        calculateStatistics(ParamType.SRAD);
    }
    
    /**
     * calculate the statistics for the requested parameter
     * @param parameter PameterType TAIR, TA9M, SRAD
     * @throws WrongParameterIdException wrong parameter
     */
    private void calculateStatistics(ParamType parameter) throws WrongParameterIdException
    {
        // list to hold valid values and find extrema
        List<Double> list = new ArrayList<Double>();
        // EnumMap to hold type and stats
        EnumMap<StatType, StatMeasurement> enums = new EnumMap<StatType, StatMeasurement>(StatType.class);
        
        int numberOfMeasurements = data.size(); //number of values
        int numberOfValidObservations = 0; //number valid values
        int sum = 0; //sum of valid values
        int maxIndex = 0; //location of max val
        int minIndex = 0; //location of min val
        double minVal = 9999.9; //value of min
        double maxVal = -9999.9; //value of max
        
        GregorianCalendar maxCal = new GregorianCalendar(); //datetime of max
        GregorianCalendar minCal = new GregorianCalendar(); //datetime of min
        maxCal = formatCalendar(maxCal);
        minCal = formatCalendar(minCal);
        
        // loop through values in file
        for (int index = 0; index < numberOfMeasurements; ++index)
        {
            double value = data.get(index).getMeasurement(parameter).getValue();
            
            // check validity
            if (data.get(index).getMeasurement(parameter).isValid())
            {
                list.add(value);
                sum += value;
                ++numberOfValidObservations;
                
                // finding max
                if (Collections.max(list) > maxVal)
                {
                    maxIndex = index;
                    maxVal = Collections.max(list);
                    maxCal = data.get(index).getMeasurementDateTime();
                }
                
                // finding min
                if (Collections.min(list) < minVal)
                {
                    minIndex = index;
                    minVal = Collections.min(list);
                    minCal = data.get(index).getMeasurementDateTime();
                }
            }
            
            // invalid measurement
            else
            {
                value = Double.NaN;
                list.add(value);
            }
        }
        
        double max = Collections.max(list);
        double min = Collections.min(list);
        double avg;
        
        // making sure we don't divide by zero
        if (numberOfValidObservations < 1)
        {
            avg = Double.NaN;
        }
        else
        {
            avg = sum / numberOfValidObservations;
        }
        
        // reseting seconds due to runtime(?)
        maxCal.set(Calendar.SECOND, 0);
        minCal.set(Calendar.SECOND, 0);
        
        // putting the type and stat measurement into the EnumMap
        enums.put(StatType.MAX, new StatMeasurement(max, maxCal, data.get(maxIndex).getStationID(),
                parameter.name(), StatType.MAX));
        enums.put(StatType.MIN, new StatMeasurement(min, minCal, data.get(minIndex).getStationID(),
                parameter.name(), StatType.MIN));
        enums.put(StatType.AVG, new StatMeasurement(avg, maxCal, data.get(0).getStationID(),
                parameter.name(), StatType.AVG));
        enums.put(StatType.TOT, new StatMeasurement(sum, maxCal, data.get(0).getStationID(),
                parameter.name(), StatType.TOT));
        
        // putting the StatMeasurement into the HashMap
        paramStats.put(parameter, enums);
        
    }
    
    /**
     * return a formated calendar in UTC
     * @param cal calendar for formatting
     * @return formatted calendar
     */
    private GregorianCalendar formatCalendar(GregorianCalendar cal)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setCalendar(cal);
        dateFormat.format(cal.getTime());
        
        return cal;
    }

    /**
     * @return average of solar radiation
     */
    public StatMeasurement getSolarRadiationAverage()
    {
        return paramStats.get(ParamType.SRAD).get(StatType.AVG);
    }

    /**
     * @return maximum value of solar radiation
     */
    public StatMeasurement getSolarRadiationMax()
    {
        return paramStats.get(ParamType.SRAD).get(StatType.MAX);
    }

    /**
     * @return minimum value of solar radiation
     */
    public StatMeasurement getSolarRadiationMin()
    {
        return paramStats.get(ParamType.SRAD).get(StatType.MIN);
    }

    /**
     * @return total value of solar radiation
     */
    public StatMeasurement getSolarRadiationTotal()
    {
        return paramStats.get(ParamType.SRAD).get(StatType.TOT);
    }

    /**
     * @return station ID
     */
    public String getStationID()
    {
        return stationId;
    }

    /**
     * @return average value of air temperature at 9m
     */
    public StatMeasurement getTa9mAverage()
    {
        return paramStats.get(ParamType.TA9M).get(StatType.AVG);
    }

    /**
     * @return maximum value of air temperature at 9m
     */
    public StatMeasurement getTa9mMax()
    {
        return paramStats.get(ParamType.TA9M).get(StatType.MAX);
    }

    /**
     * @return minimum value of air temperature at 9m
     */
    public StatMeasurement getTa9mMin()
    {
        return paramStats.get(ParamType.TA9M).get(StatType.MIN);
    }

    /**
     * @return average value of air temperature at 9m
     */
    public StatMeasurement getTairAverage()
    {
        return paramStats.get(ParamType.TAIR).get(StatType.AVG);
    }

    /**
     * @return maximum value of air temperature
     */
    public StatMeasurement getTairMax()
    {
        return paramStats.get(ParamType.TAIR).get(StatType.MAX);
    }

    /**
     * @return minimum value of air temperature
     */
    public StatMeasurement getTairMin()
    {
        return paramStats.get(ParamType.TAIR).get(StatType.MIN);
    }



    /**
     * Describe DayStatistics
     * 
     * @return A string describing the statistics for the day
     */
    @Override
    public String toString()
    {
        return String.format("TAIR MIN: %.04f\nTAIR MAX: %.04f\nTAIR AVG: %.04f\n"
                + "TA9M MIN: %.04f\nTA9M MAX: %.04f\nTA9M AVG: %.04f\n"
                + "SRAD MIN: %.04f\nSRAD MAX: %.04f\n SRAD AVG: %.04f\nSRAD TOT: %.04f",
                getTairMin().getValue(), getTairMax().getValue(), getTairAverage().getValue(),
                getTa9mMin().getValue(), getTa9mMax().getValue(), getTa9mAverage().getValue(),
                getSolarRadiationMin().getValue(), getSolarRadiationMax().getValue(),
                getSolarRadiationAverage().getValue(), getSolarRadiationTotal().getValue());
       
    }
}
