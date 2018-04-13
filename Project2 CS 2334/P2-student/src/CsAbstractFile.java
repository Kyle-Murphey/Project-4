import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/**
 * checks is files exist and last modified
 * @author Kyle Murphey
 * @version 2018/3/30
 *
 */
public abstract class CsAbstractFile implements  TimeComparable
{
    /** data file **/
    protected File file;
    /** format for date **/
    protected DateFormat dateFormat;
    /**
     * time format used for strings
     */
    protected static String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss z";
    /** file name **/
    protected String fileName;
    
 
    /**
     * ctor
     * @param inFileName file name
     */
    protected CsAbstractFile(String inFileName)
    {
        this.fileName = inFileName;
        file = new File(this.fileName);
    }
    
    /**
     * check if the file exists
     * @return if file exists
     */
    public boolean exists()
    {
        return file.exists();
    }
    
    /**
     * get the date last modified
     * @return date modified
     */
    public long getDateModified()
    {
        return file.lastModified();
    }
    
    /**
     * string output
     * @return info about file
     */
    @Override
    public String toString()
    {
        SimpleDateFormat df = new SimpleDateFormat(dateTimeFormat);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateOut = df.format(new Date(getDateModified()));
        return String.format("File name: %s\nExists: %b\nLast modified: %s", 
                fileName, exists(), dateOut);
    }

    /**
     * abstract to compare time
     * @param inDateTimeStr date in string form
     * @return an int based on the result
     * @throws ParseException invalid input
     */
    public abstract  int compareWithTimeString(String inDateTimeStr) throws ParseException;
}
