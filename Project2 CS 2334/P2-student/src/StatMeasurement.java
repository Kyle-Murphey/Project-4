
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * @author rafal
 * @version 2018/3/30
 * the placeholder for a measurement aka stat measurement object
 *
 */
public class StatMeasurement extends Measurement implements TimeComparable
{
    private GregorianCalendar dateTimeOfMeasurment;
    private String paramId;
    private StatType statType;
    private String stationId;
    private static final String NADA = "nada";

    /**
     * constructor
     */
    public StatMeasurement()
    {
        super();
    }

    /**
     * ctor
     * @param inValue value of the measurement
     * @param obsDateTime date of the measurement
     * @param inStationId station Id
     * @param inParamId param Id (TAIR, TA9m, SRAD)
     * @param inStatType AVG, MIN, MAX, TOT
     */
    public StatMeasurement(double inValue, GregorianCalendar obsDateTime, String inStationId, 
            String inParamId, StatType inStatType)
    {
        super(inValue);
        SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        this.dateTimeOfMeasurment = new GregorianCalendar();
        this.dateTimeOfMeasurment.setTime(obsDateTime.getTime());
        this.dateTimeOfMeasurment.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setCalendar(dateTimeOfMeasurment);
        dateFormat.format(dateTimeOfMeasurment.getTime());
        
        value = inValue;
        this.stationId = inStationId;
        this.paramId = inParamId;
        this.statType = inStatType;
    }
    
    /**
     * get the date
     * @return date
     */
    public GregorianCalendar getDateTimeOfMeasurment()
    {
        return dateTimeOfMeasurment;
    }
    
    /**
     * set params
     * @param inParamId tair,ta9m,srad
     */
    public void setParamId(String inParamId)
    {
        this.paramId = inParamId;
    }
    
    
    /**
     * get params
     * @return params
     */
    public String getParamId()
    {
        return paramId;
    }
    
    
    /**
     * set stat type
     * @param type enum stat type
     */
    public void setStatType(StatType type)
    {
        this.statType = type;
    }
    
    
    /**
     * get stat type
     * @return stat type
     */
    public StatType getStatType()
    {
        return statType;
    }

    /**
     * Compare this Measurement with another Measurement
     * 
     * @param compareWith Measurement to compare with
     * @return true if both Measurements are valid AND this is strictly smaller than s OR 
     * if this is valid and s is not valid
     */
    public boolean isLessThan(StatMeasurement compareWith)
    {
        if (this.isValid() && compareWith.isValid())
        {
            return this.getValue() < compareWith.getValue();
        }
        else if (!compareWith.isValid())
        {
            return this.isValid();
        }
        else
        {
            return false;
        }
    }

    /**
     * Compare this Measurement with another Measurement
     * 
     * @param compareWith Measurement to compare with
     * @return true if both Measurements are valid AND this is strictly larger than s OR
     *         if this is valid and s is not valid
     */
    public boolean isGreaterThan(StatMeasurement compareWith)
    {
        if (this.isValid() && compareWith.isValid())
        {
            return this.getValue() > compareWith.getValue();
        }
        else if (!compareWith.isValid())
        {
            return this.isValid();
        }
        else
        {
            return false;
        }
    }

    /**
     * check if newer
     * @param inDateTime inputted date
     * @return whether of not the stat is newer than
     */
    public boolean newerThan(String inDateTime) throws ParseException
    {
        return compareWithTimeString(inDateTime) == 1 ? true : false;
    }

    /**
     * check if older
     * @param inDateTime inputted date
     * @return whether of not the stat is older
     */
    public boolean olderThan(String inDateTime) throws ParseException
    {
        return compareWithTimeString(inDateTime) == -1 ? true : false;
    }


    
    /**
     * compare the stats
     * @param inDateTime date in string form
     * @return int value based on comparison
     * @throws ParseException bad input
     */
    public int compareWithTimeString(String inDateTime) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(dateFormat.parse(inDateTime));
        g.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        
        if (this.dateTimeOfMeasurment.after(g))
        {
            return 1;
        }
        else if (this.dateTimeOfMeasurment.before(g))
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * get the info of the stat
     * @return output of stat
     */
    @Override
    public String toString()
    {

        if (this.isValid())
        {
            SimpleDateFormat date = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
            date.setCalendar(this.dateTimeOfMeasurment);
            String dateString = date.format(dateTimeOfMeasurment.getTime());
            
            return String.format("%.04s %.04s %.04f %.04s %s\n", paramId, statType, value, 
                    stationId, dateString);
        }
        else
        {
            return NADA;
        }
    }
    
}
