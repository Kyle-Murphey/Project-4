import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * compares files
 * @author Kyle Murphey
 * @version 2018/3/30
 *
 */
public class CsFile extends CsAbstractFile
{
    /**
     * ctor
     * @param inFileName file name
     */
    public CsFile(String inFileName)
    {
        super(inFileName);
    }
    
    /**
     * get the file name
     * @return file name
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * checks if newer than
     * @param inDateTime date as String
     * @return whether or not the file is newer
     */
    public boolean newerThan(String inDateTime) throws ParseException
    {
        return compareWithTimeString(inDateTime) == 1 ? true : false;
    }

    /**
     * checks if older
     * @param inDateTime date as string
     * @return whether or not the file is older
     */
    public boolean olderThan(String inDateTime) throws ParseException
    {
        return compareWithTimeString(inDateTime) == -1 ? true : false;
    }

    /**
     * compares the dates
     * @param inDateTime date as string
     * @return value based on date comparison 
     */
    @Override
    public int compareWithTimeString(String inDateTime) throws ParseException
    {
        SimpleDateFormat converter = new SimpleDateFormat(dateTimeFormat);
        converter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        Date inDate = converter.parse(inDateTime);
        Date thisDate = converter.parse(converter.format(new Date(this.getDateModified())));
        
        // if thisDate is older than the parameter date
        if (thisDate.after(inDate))
        {
            return 1; 
        }
        // if thisDate is newer than the parameter date
        else if (thisDate.before(inDate))
        {
            return -1;
        }
        // error or same date
        else
        {
            return 0;
        }
    }
}