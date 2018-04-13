
public class Driver
{

    public static void main(String[] args)
    {
        String[] files = { "P2-student/data/mesonet/20180102stil.mts",
                "P2-student/data/mesonet/20180102okcn.mts", 
                "P2-student/data/mesonet/20180102okce.mts"};
        
        try
        {
            DaysStatistics stats = new DaysStatistics(files);
            stats.findStatistics();
            System.out.println(stats.toString());

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

}
