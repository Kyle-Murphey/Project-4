import java.text.ParseException;

/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * comparing values
 *
 */
public interface TimeComparable 
{
    /**
     * check if newer than
     * @param inDateTimeStr date as string
     * @return if newer than
     * @throws ParseException bad input
     */
    boolean newerThan(String inDateTimeStr) throws ParseException;
    /**
     * check if older than
     * @param inDateTimeStr date as string
     * @return if older than
     * @throws ParseException bad input
     */
    boolean olderThan(String inDateTimeStr) throws ParseException;
}