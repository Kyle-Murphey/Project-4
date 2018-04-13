/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * wrong time zone exception
 *
 */
public class WrongTimeZoneException extends Exception
{
    /**
    * serial verion uid
     */
    private static final long serialVersionUID = 1L;

    /**
     * ctor
     */
    public WrongTimeZoneException()
    {
        super("Invalid time zone detected, should be UTC");
        // default implementation ignored
    }
}
