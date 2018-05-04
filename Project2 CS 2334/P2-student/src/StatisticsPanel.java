import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/***
 * 
 * @author Kyle Murphey
 * @version 2018-5-3
 * 
 * creates stats panel
 *
 */
public class StatisticsPanel extends JPanel
{
    /**
     * version ID
     */
    private static final long serialVersionUID = -5778130703074619169L;
    /** max **/
    public final static String MAX_BUTTON = "MAXIMUM";
    /** min **/
    public final static String MIN_BUTTON = "MINIMUM";
    /** max button **/
    private JRadioButton maxButton;
    /** min button **/
    private JRadioButton minButton;
    /** button group for max and min **/
    private ButtonGroup bg;
    
    /**
     * create stat panel
     */
    public  StatisticsPanel()
    {
        this.setLayout(new GridLayout(2, 0));
        setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        maxButton = new JRadioButton(MAX_BUTTON);
        minButton = new JRadioButton(MIN_BUTTON);
        
        maxButton.setBackground(new Color(153, 204, 210));
        minButton.setBackground(new Color(153, 204, 210));
        
        
        bg = new ButtonGroup();
        bg.add(maxButton);
        bg.add(minButton);
        
        add(maxButton);
        add(minButton);
        setBackground(new Color(153, 204, 210));
        Dimension preferredSize = this.getPreferredSize();
        this.setMinimumSize(preferredSize);
        this.setMaximumSize(preferredSize);
        
        
    }
    
    
    /**
     * get the stat type
     * @return max or min string
     */
    public String getStatisticsType()
    {
        if (maxButton.isSelected())
        {
            return MAX_BUTTON;
        }
        else if (minButton.isSelected())
        {
            return MIN_BUTTON;
        }
        else
        {
            return "none";
        }
    }
}
