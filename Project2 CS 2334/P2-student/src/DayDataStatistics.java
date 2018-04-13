import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    /** Minimum tair across day. */
    private StatMeasurement tairMin;
    /** Maximum tair across day. */
    private StatMeasurement tairMax;
    /** Average tair across the days. */
    private StatMeasurement tairAverage;
    
    /** Minimum ta9m across day. */
    private StatMeasurement ta9mMin;
    /** Maximum ta9m across day. */
    private StatMeasurement ta9mMax;
    /** Average ta9m across day. */
    private StatMeasurement ta9mAverage;

    /** Minimum solar radiation across day. */
    private StatMeasurement solarRadiationMin;
    /** Maximum solar radiation across day. */
    private StatMeasurement solarRadiationMax;
    /** Average solar radiation. */
    private StatMeasurement solarRadiationAverage;

    /** Total solarRadiation */
    private StatMeasurement solarRadiationTotal;

    /** Station Id **/
    private String stationId = "nada";

    /**
     * ctor
     * @param inData list of time date to find stats
     */
    public DayDataStatistics(ArrayList<TimeData> inData)
    {
        data = inData;
        stationId = data.get(0).getStationID();

        calculateAirTemperatureStatistics("tair");
        calculateAirTemperatureStatistics("ta9m");
        calculateSolarRadiationStatistics();
    }

    /**
     * calc tair stats
     * @param tairName tair
     */
    private void calculateAirTemperatureStatistics(String tairName)
    {
        // These variables represent the "best so far" for min and max.
        // By setting these these to the largest and smallest possible
        // values, we ensure that the first time a valid Measurement is
        // found, it will replace these values

        // Accumulator and counter for computing average
        double sum = 0;
        int numberOfValidObservations = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        
        // min calendar placeholder
        GregorianCalendar minCalendar = new GregorianCalendar();
        minCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setCalendar(minCalendar);
        dateFormat.format(minCalendar.getTime());
        
        // max calendar placeholder
        GregorianCalendar maxCalendar = new GregorianCalendar();
        maxCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setCalendar(maxCalendar);
        dateFormat.format(maxCalendar.getTime());
        
        int numberOfMeasurements = data.size();
        double avg = 0;
        double min = 9999.9;
        double max = -9999.9;
        
        // finding tair stats
        if (tairName.equals("tair"))
        { 
            numberOfValidObservations = 0;
            sum = 0;
            int maxIndex = 0;
            int minIndex = 0;
            // looping through the array of TimeData
            for (int index = 0; index < numberOfMeasurements; ++index)
            {
                // checking validity
                if (data.get(index).getTair().isValid())
                {
                    // incrementing observations and adding values to the sum
                    ++numberOfValidObservations;
                    sum += data.get(index).getTair().getValue();
                    
                    // checking if value is less than the minimum
                    if (data.get(index).getTair().getValue() < min)
                    {
                        min = data.get(index).getTair().getValue();
                        minCalendar = data.get(index).getMeasurementDateTime();
                        minIndex = index;
                    }
                    
                    // checking if value is greater than the maximum
                    if (data.get(index).getTair().getValue() > max)
                    {
                        max = data.get(index).getTair().getValue();
                        maxCalendar = data.get(index).getMeasurementDateTime();
                        maxIndex = index;
                    }
                }
                else
                {
                    min = Double.NaN;
                    max = Double.NaN;
                }
            }
            // finding average
            avg = sum / numberOfValidObservations;
            
            maxCalendar.set(Calendar.SECOND, 0);
            minCalendar.set(Calendar.SECOND, 0);
            
            // creating tair average StatMeasurement object
            tairAverage = new StatMeasurement(avg, maxCalendar, 
                    data.get(0).getStationID(), "TAIR", StatType.AVG);
            
            // creating tair max StatMeasurement object
            tairMax = new StatMeasurement(max, maxCalendar, data.get(maxIndex).getStationID(), "TAIR", StatType.MAX);
            
            // creating  tair min StatMeasurement object
            tairMin = new StatMeasurement(min, minCalendar, data.get(minIndex).getStationID(), "TAIR", StatType.MIN);
        }
        // finding ta9m
        else
        {
            numberOfValidObservations = 0;
            sum = 0;
            int maxIndex = 0;
            int minIndex = 0;
            for (int index = 0; index < numberOfMeasurements; ++index)
            {
                // checking if valid
                if (data.get(index).getTa9m().isValid())
                {
                    ++numberOfValidObservations;
                    sum += data.get(index).getTa9m().getValue();
                    
                    // checking if value is less than the minimum
                    if (data.get(index).getTa9m().getValue() < min)
                    {
                        min = data.get(index).getTa9m().getValue();
                        minCalendar = data.get(index).getMeasurementDateTime();
                        minIndex = index;
                    }
                    
                    // checking if value is greater than the maximum
                    if (data.get(index).getTa9m().getValue() > max)
                    {
                        max = data.get(index).getTa9m().getValue();
                        maxCalendar = data.get(index).getMeasurementDateTime();
                        maxIndex = index;
                    }
                }
                else
                {
                    min = Double.NaN;
                    max = Double.NaN;
                }
            }
            // finding average
            avg = sum / numberOfValidObservations;
            
            maxCalendar.set(Calendar.SECOND, 0);
            minCalendar.set(Calendar.SECOND, 0);
            
            // creating tair average StatMeasurement object
            ta9mAverage = new StatMeasurement(avg, maxCalendar, 
                    data.get(0).getStationID(), "TA9M", StatType.AVG);
            
            // creating tair max StatMeasurement object
            ta9mMax = new StatMeasurement(max, maxCalendar, data.get(maxIndex).getStationID(), "TA9M", StatType.MAX);
            
            // creating  tair min StatMeasurement object
            ta9mMin = new StatMeasurement(min, minCalendar, data.get(minIndex).getStationID(), "TA9m", StatType.MIN);
        }
    }

    /**
     * Compute and fill in the solar radiation-related statistics
     * (solarRadiationMin, solarRadiationMax, solarRadiationAverage, and
     * solarRadiationTotal).
     * <P>
     * Notes:
     * <UL>
     * <LI>Only valid Measurements can be used in these computations
     * <LI>You may assume that every month has at least one valid Measurement
     * </UL>
     */
    private void calculateSolarRadiationStatistics()
    {
        double sum = 0;
        int numberOfValidObservations = 0;
        
        int numberOfMeasurements = data.size();
        double avg = 0;
        double min = 9999.9;
        double max = -9999.9;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        
        GregorianCalendar minCalendar = new GregorianCalendar();
        minCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setCalendar(minCalendar);
        dateFormat.format(minCalendar.getTime());
        
        GregorianCalendar maxCalendar = new GregorianCalendar();
        maxCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setCalendar(maxCalendar);
        dateFormat.format(maxCalendar.getTime());
        
        
        int maxIndex = 0;
        int minIndex = 0;
        
        for (int index = 0; index < numberOfMeasurements; ++index)
        {
            if (data.get(index).getSolarRadiation().isValid())
            {
                ++numberOfValidObservations;
                sum += data.get(index).getSolarRadiation().getValue();
                // checking if value is less than the minimum
                if (data.get(index).getSolarRadiation().getValue() < min)
                {
                    min = data.get(index).getSolarRadiation().getValue();
                    minCalendar = data.get(index).getMeasurementDateTime();
                    minIndex = index;
                }
                
                // checking if value is greater than the maximum
                if (data.get(index).getSolarRadiation().getValue() > max)
                { 
                    max = data.get(index).getSolarRadiation().getValue();
                    maxCalendar = data.get(index).getMeasurementDateTime();
                    maxIndex = index;
                }
            }
            else
            {
                min = Double.NaN;
                max = Double.NaN;
            }
            
        }
        // finding average
        avg = sum / numberOfValidObservations;
        
        maxCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.SECOND, 0);
       
        // creating srad average StatMeasurement object
        solarRadiationAverage = new StatMeasurement(avg, maxCalendar, data.get(0).getStationID(), 
                "SRAD", StatType.AVG);
        
        // creating srad max StatMeasurement object
        solarRadiationMax = new StatMeasurement(max, maxCalendar, data.get(maxIndex).getStationID(),
                "SRAD", StatType.MAX);        
        // creating  srad min StatMeasurement object
        solarRadiationMin = new StatMeasurement(min, minCalendar, data.get(minIndex).getStationID(), 
                "SRAD", StatType.MIN);
       
        // creating srad total StatMeasurement object
        solarRadiationTotal = new StatMeasurement(sum, maxCalendar, data.get(0).getStationID(), 
                "SRAD", StatType.TOT);
    }
    

    /**
     * @return average of solar radiation
     */
    public StatMeasurement getSolarRadiationAverage()
    {
        return solarRadiationAverage;
    }

    /**
     * @return maximum value of solar radiation
     */
    public StatMeasurement getSolarRadiationMax()
    {
        return solarRadiationMax;
    }

    /**
     * @return minimum value of solar radiation
     */
    public StatMeasurement getSolarRadiationMin()
    {
        return solarRadiationMin;
    }

    /**
     * @return total value of solar radiation
     */
    public StatMeasurement getSolarRadiationTotal()
    {
        return solarRadiationTotal;
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
        return ta9mAverage;
    }

    /**
     * @return maximum value of air temperature at 9m
     */
    public StatMeasurement getTa9mMax()
    {
        return ta9mMax;
    }

    /**
     * @return minimum value of air temperature at 9m
     */
    public StatMeasurement getTa9mMin()
    {
        return ta9mMin;
    }

    /**
     * @return average value of air temperature at 9m
     */
    public StatMeasurement getTairAverage()
    {
        return tairAverage;
    }

    /**
     * @return maximum value of air temperature
     */
    public StatMeasurement getTairMax()
    {
        return tairMax;
    }

    /**
     * @return minimum value of air temperature
     */
    public StatMeasurement getTairMin()
    {
        return tairMin;
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
