import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author rafal
 * @version 2018/3/30
 * parses data
 */
public class MesonetTimeFile extends CsFile
{
    /** ArrayList of TimeData **/
    private ArrayList<TimeData> data;
    
    /** ArrayList of Strings of params **/
    private ArrayList<String> paramIds;
   
    /** date object **/
    private HeaderDateTime headerDateTime;

    /** num params index **/
    private static final int NUMBER_OF_PARAMETERS = 0;
    /** year index **/
    private static final int YEAR = 1;
    /** month index **/
    private static final int MONTH = 2;
    /** day index **/
    private static final int DAY = 3;
    /** hour index **/
    private static final int HOUR = 4;
    /** minute index **/
    private static final int MINUTE = 5;
    /** second index **/
    private static final int SECOND = 6;

    /** tair **/
    private static final String TAIR = "TAIR";
    /** ta9m **/
    private static final String TA9M = "TA9M";
    /** srad **/
    private static final String SRAD = "SRAD";
    /** time **/
    private static final String TIME = "TIME";
    /** station id **/
    private static final String STID = "STID";
    /** wind speed **/
    private static final String WSPD = "WSPD";
    
    /** index of tair **/
    private int tairPosition = -1;
    /** index of ta9m **/
    private int ta9mPosition = -1;
    /** index of srad **/
    private int sradPosition = -1;
    /** index of minute **/
    private int minutePosition = -1;
    /** index of station id **/
    private int stidPosition = -1;
    /** index of wspd **/
    private int wspdPosition = -1;

    /** calendar for date **/
    private GregorianCalendar dateTime;
    
    /** num of params **/
    private int params = 0;
    /** default value **/
    private int hours = 0;
    /** default value **/
    private int seconds = 0;
    
    /**
     * 
     * @author Kyle Murphey
     *
     */
    class HeaderDateTime
    {
        /** year **/
        public int year;
        /** month **/
        public int month;
        /** day **/
        public int day;
        /** minute **/
        public int minute;

        /**
         * ctor
         * @param inYear year
         * @param inMonth month
         * @param inDay day
         * @param inMinute minute
         */
        HeaderDateTime(int inYear, int inMonth, int inDay, int inMinute)
        {
            year = inYear;
            month = inMonth - 1;
            day = inDay;
            minute = inMinute;
        }
    }

    /** ctor
     * @param inFileName file name to be parsed
     * @throws IOException bad file
     * @throws WrongCopyrightException bad copyright
     */
    MesonetTimeFile(String inFileName) throws IOException, WrongCopyrightException
    {
        super(inFileName);
        
        if (file.exists())
        {
            data = new ArrayList<TimeData>();
            data = parseFile();
        }
        else
        {
            throw new IOException();
        }
    }

    /**
     * parses through the file line by line
     * @return a blank ArrayList
     * @throws IOException file closing, not authorized to read file, no disk space, not available, etc.
     * @throws WrongCopyrightException wrong copyright number
     * @throws NumberFormatException converting an invalid value
     */
    public ArrayList<TimeData> parseFile() throws IOException, WrongCopyrightException, NumberFormatException
    {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine(); // line of data
        String[] input = parseLine(line); // line of data in array form
        
        // checking if Copyright Id is correct
        copyrightIsCorrect(input[0]);
        line = br.readLine();
        input = parseLine(line);
        
        // parsing the date data
        params = Integer.parseInt(input[NUMBER_OF_PARAMETERS]);
        hours = Integer.parseInt(input[HOUR]);
        minutePosition = 2;
        seconds = Integer.parseInt(input[SECOND]);
        parseDateTimeHeader(String.format("%s %s %s %s", input[YEAR], input[MONTH], input[DAY], input[MINUTE]));
        
        // parsing the parameter Id's
        line = br.readLine();
        parseParamHeader(line);
        
        // parsing the weather data
        line = br.readLine();
        while (line != null)
        {
            parseData(line);
            line = br.readLine();
        }
        
        // closing BufferedReader
        br.close();
        
        // returning data
 
        return data;
        
        
    }
    
    /**
     * Helper method for parsing the first couple lines
     * @param line line from the file you want to turn into a String array
     * @return the line as a String array
     */
    private String[] parseLine(String line)
    {
        String[] input = line.trim().split("\\s+");
        return input;
    }

    /**
     * parses the data from the line parameter and creates new TimeData
     * @param line line of data from file
     */
    private void parseData(String line)
    {
        String[] input = line.trim().split("\\s+");

        TimeData values = new TimeData(input[stidPosition], headerDateTime.year, 
                headerDateTime.month, headerDateTime.day, Integer.parseInt(input[minutePosition]),
                new Measurement(Double.parseDouble(input[tairPosition])),
                new Measurement(Double.parseDouble(input[ta9mPosition])),
                new Measurement(Double.parseDouble(input[sradPosition])),
                new Measurement(Double.parseDouble(input[wspdPosition])));
        data.add(values);
    }

    /** parses params
     * @param inParamStr line with params
     */
    private void parseParamHeader(String inParamStr)
    {
        // adding paramIds to the ArrayList
        paramIds = new ArrayList<String>();
        paramIds.add(TAIR);
        paramIds.add(TA9M);
        paramIds.add(SRAD);
        paramIds.add(TIME);
        paramIds.add(STID);
        paramIds.add(WSPD);
        
        // parsing the parameter
        String[] input = parseLine(inParamStr);
        
        // loop through the line of data and assign proper indices
        for (int index = 0; index < params; ++index)
        {
            switch (input[index])
            {
                case TAIR:
                    tairPosition = index;
                    break;
                case TA9M:
                    ta9mPosition = index;
                    break;
                case SRAD:
                    sradPosition = index;
                    break;
                case STID:
                    stidPosition = index;
                    break;
                case TIME:
                    minutePosition = index;
                    break;
                case WSPD:
                    wspdPosition = index;
                default:
                    break;
            }
        }
    }

    /** check copyright, return exception if false
     * @param inCopyrightStr line with copyright
     * @throws WrongCopyrightException if copyright isn't 101
     */
    private void copyrightIsCorrect(String inCopyrightStr) throws WrongCopyrightException
    {
        if (!(inCopyrightStr.equals("101")))
        {
            throw new WrongCopyrightException();
        }
    }

    /** parses the header
     * @param inHeader the header line
     */
    void parseDateTimeHeader(String inHeader)
    {
        String[] input = inHeader.trim().split("\\s+");
        headerDateTime = new HeaderDateTime(Integer.parseInt(input[0]), Integer.parseInt(input[1]), 
                Integer.parseInt(input[2]), Integer.parseInt(input[3]));
    }

    /** Parses the date
     * @return date
     */
    String getStarDateTimeStringFromFile()
    {
        SimpleDateFormat date = new SimpleDateFormat(dateTimeFormat);
        dateTime = new GregorianCalendar(headerDateTime.year, headerDateTime.month - 1, 
               headerDateTime.day, hours, headerDateTime.minute);
        dateTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateTime.set(headerDateTime.year, headerDateTime.month, 
               headerDateTime.day, hours, headerDateTime.minute, seconds);
        date.setCalendar(dateTime);
        
        return date.format(dateTime.getTime()).toString();
    }

    /** formats the date
     * @return the date
     */
    String getDateTimeString()
    {
        SimpleDateFormat date = new SimpleDateFormat(dateTimeFormat);
        dateTime = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        dateTime.set(headerDateTime.year, headerDateTime.month, 
                headerDateTime.day, hours, headerDateTime.minute, seconds);
        date.setCalendar(dateTime);
        
        return date.format(dateTime.getTime());
    }

}
