/**
 * 
 * @author CS2334. Modified by: Rafal Jabrzemski
 *         <P>
 *         Date: 2018-02-01 <BR>
 *         Project 1
 *         <P>
 *         This class represents individual, real-valued samples. This class
 *         explicitly addresses the fact that some samples are bad.
 *  @version 2018-01-01
 *
 */
public class Measurement
{

    /** The observed value. */
    // use NaN for missing values
    protected double value;

    /** Indicates whether the observation is a valid one */
    protected boolean valid;

    /**
     * 
     */
    protected Measurement()
    {
        valid = false;
        value = Double.NaN;
    }

    /**
     * Sample constructor.
     * <P>
     * Values that are larger than -900 are assumed to be valid
     * 
     * @param value
     *            The value to be stored
     */
    public Measurement(double value)
    {

        if (value > -900.0)
        {
            this.value = value;
            valid = true;
        }
        else
        {
            this.value = Double.NaN;
            valid = false;
        }
    }

    /**
     * Returns the observed value
     * 
     * @return The observed value
     */
    public double getValue()
    {
        return value;
    }

    /**
     * Indicates whether the observation is valid
     * 
     * @return True if valid; false otherwise
     */
    public boolean isValid()
    {
        return valid;
    }
    

    /**
     * Describes the observation.
     * 
     * @return String that describes the observation: either the value or "NaN";
     */
    @Override
    public String toString()
    {
        if (valid)
        {
            return String.format("%.04f", value);
        }
        else
        {
            return "NaN";
        }
    }
}
