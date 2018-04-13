/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * copyright exception
 *
 */
public class WrongCopyrightException extends Exception
{
    /**
     ** serial verion uid
     */
    private static final long serialVersionUID = -3352808845495117276L;

    /**
     * wrong copyright
     */
	public WrongCopyrightException()
    {
        super("Invalid copyright detected");
    }

}
