/**
 * 
 * @author Kyle Murphey
 * @version 2018/3/30
 * 
 * min and max day
 *
 */
public abstract class StatisticsAbstract 
{    
    /**
     * getting the min date
     * @param inParamId tair, ta9m, srad
     * @return StatMeasurement where min happened
     * @throws WrongParameterIdException wrong param
     */
    public abstract StatMeasurement getMinimumDay(String inParamId) throws WrongParameterIdException;
    /**
     * getting the max date
     * @param inParamId tair, ta9m, srad
     * @return StatMeasurement where max happened
     * @throws WrongParameterIdException wrong param
     */
    public abstract StatMeasurement getMaximumDay(String inParamId) throws WrongParameterIdException;   

}
