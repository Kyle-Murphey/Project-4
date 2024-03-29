import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 
 * @author CS2334. Modified by: Rafal Jabrzemski
 *         <P>
 *         Date: 2018-02-01 <BR>
 *         Project 1
 *         <P>
 *         This class represents a summary of one time's data from a single
 *         Mesonet station.
 * @version 2018-01-01
 * 
 *
 */
public class TimeData
{    
    /** date of data **/
    private GregorianCalendar measurementDateTimeUTC;
    /** station id **/
    private String stationID;
    /** tair **/
    private Measurement tair;
    /** ta9m **/
    private Measurement ta9m;
    /** srad **/
    private Measurement solarRadiation;
    /** wspd **/
    private Measurement wspd;
    /**
     * ctor
     * @param stationID station id
     * @param year year
     * @param month month
     * @param day day
     * @param minute minute
     * @param tair tair
     * @param ta9m ta9m
     * @param solarRadiation srad
     * @param wspd wind speed
     */
    public TimeData(String stationID, int year, int month, int day, int minute,
            Measurement tair, Measurement ta9m, Measurement solarRadiation, Measurement wspd)
    {
        this.stationID = stationID;
        setDateTimeComponents(year, month, day, minute);
        setMeasurements(tair, ta9m, solarRadiation, wspd);
    }
    
    /**
     * ctor
     * @param inStationID staion id
     * @param dateTime time of meas.
     * @param inTair tair
     * @param inTa9m ta9m
     * @param inSolarRadiation srad
     * @param inWspd wind speed
     * @throws WrongTimeZoneException wrong tz
     */
    public TimeData(String inStationID, GregorianCalendar dateTime, 
            Measurement inTair, Measurement inTa9m, Measurement inSolarRadiation,
            Measurement inWspd) throws WrongTimeZoneException
    {
        if (dateTime.getTimeZone().getID().equals("UTC"))
        {
            this.stationID = inStationID;
            this.measurementDateTimeUTC = new GregorianCalendar();
            this.measurementDateTimeUTC = dateTime;
            this.measurementDateTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
            dateFormat.setCalendar(this.measurementDateTimeUTC);
            dateFormat.format(this.measurementDateTimeUTC.getTime());
            setMeasurements(inTair, inTa9m, inSolarRadiation, inWspd);
        }
        else
        {
            throw new WrongTimeZoneException();
        }
    }
    
    /**
     * get the date
     * @return date
     */
    public GregorianCalendar getMeasurementDateTime()
    {
        return measurementDateTimeUTC;
    }
    
    /**
     * set the date
     * @param year year
     * @param month month
     * @param day day
     * @param minute minute
     */
    private void setDateTimeComponents(int year, int month, int day, int minute)
    {
        this.measurementDateTimeUTC = new GregorianCalendar();
        this.measurementDateTimeUTC.setTime(new GregorianCalendar(year, month, day, 0, minute).getTime());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        
        dateFormat.setCalendar(this.measurementDateTimeUTC);
        dateFormat.format(this.measurementDateTimeUTC.getTime());
        this.measurementDateTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        
    }
    
    /**
     * set the measurements
     * @param inTair tair 
     * @param inTa9m ta9m
     * @param inSolarRadiation srad
     * @param inWspd wspd
     */
    private void setMeasurements(Measurement inTair, Measurement inTa9m, Measurement inSolarRadiation, 
            Measurement inWspd)
    {
        this.tair = inTair;
        this.ta9m = inTa9m;
        this.solarRadiation = inSolarRadiation;
        this.wspd = inWspd;
    }
    /**
     * get the minute
     * @return minute
     */
    public int getMinute()
    {
        return measurementDateTimeUTC.get(Calendar.MINUTE);
    }
    
    /**
     * get the month
     * @return month
     */
    public int getMonth()
    {
        return measurementDateTimeUTC.get(Calendar.MONTH);
    }
    
    /**
     * get the day
     * @return day
     */
    public int getDay()
    {
        return measurementDateTimeUTC.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * get the year
     * @return year
     */
    public int getYear()
    {
        return measurementDateTimeUTC.get(Calendar.YEAR);
    }
    
    /**
     * get the station id
     * @return station id
     */
    public String getStationID()
    {
        return stationID;
    }
    
    /**
     * get the ta9m
     * @return ta9m
     */
    public Measurement getTa9m()
    {
        return ta9m;
    }
    
    /**
     * get the srad
     * @return srad
     */
    public Measurement getSolarRadiation()
    {
        return solarRadiation;
    }
    
    /**
     * get the tair
     * 
     * @return tair
     */
    public Measurement getTair()
    {
        return tair;
    }
    
    /**
     * get the wspd
     * @return windpseed
     */
    public Measurement getWspd()
    {
        return wspd;
    }
    
    /**
     * return the requested Measurement
     * @param param requested Measurement ("TAIR", "TA9M", "SRAD", "WSPD")
     * @throws WrongParameterIdException wrong parameter
     * @return requested Measurement
     */
    public Measurement getMeasurement(ParamType param) throws WrongParameterIdException
    {
        if (param.equals(ParamType.TAIR))
        {
            return tair;
        }
        else if (param.equals(ParamType.TA9M))
        {
            return ta9m;
        }
        else if (param.equals(ParamType.SRAD))
        {
            return solarRadiation;
        }
        else if (param.equals(ParamType.WSPD))
        {
            return wspd;
        }
        else
        {
            throw new WrongParameterIdException();
        }
    }
}
