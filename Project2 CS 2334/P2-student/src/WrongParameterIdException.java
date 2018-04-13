/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * wrong param exception
 *
 */
public class WrongParameterIdException extends Exception
{
    /**
     * serial verion uid
     */
    private static final long serialVersionUID = 7394973112258653626L;

    /**
     * ctor
     */
    public WrongParameterIdException()
    {
        super("Invalid parameterID detected");
    }
    
    /**
     * ctor
     * @param msg message for error
     */
    public WrongParameterIdException(String msg)
    {
        super(msg + " Invalid parameterID detected");
    }
}
