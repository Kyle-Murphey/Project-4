import javax.swing.JLabel;
import javax.swing.JPanel;

/***
 * 
 * @author Kyle Murphey
 * @version 2018-5-3
 *
 */
public class MesonetMainPanel extends JPanel
{
    /**
     * version ID
     */
    private static final long serialVersionUID = 6224309422787783370L;

    /**
     * create main panel
     */
    public MesonetMainPanel()
    {
        JLabel greetingLabel = new JLabel("Mesonet Calculator");
        
        // Add greeting to this panel
        add(greetingLabel);
    }
}
