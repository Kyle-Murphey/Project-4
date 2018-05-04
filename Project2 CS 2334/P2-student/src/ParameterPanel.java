import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ParameterPanel extends JPanel
{
    /**
     * version ID
     */
    private static final long serialVersionUID = 4236142307230121244L;
    /** tair **/
    public final String TAIR = "TAIR";
    /** ta9m **/
    public final String TA9M = "TA9M";
    /** srad **/
    public final String SRAD = "SRAD";
    /** wspd **/
    public final String WSPD = "WSPD";
    
    
    // Check boxes for the available parameters
    /** tair **/
    private JCheckBox airTemp;
    /** ta9m **/
    private JCheckBox ta9m;
    /** srad **/
    private JCheckBox srad;
    /** wspd **/
    private JCheckBox wspd;
    
    /**
     * creates panel
     */
    public ParameterPanel()
    {
        System.out.println("Building Parameter panel");

        // Create a GridLayout Manager
        setLayout(new GridLayout(4,1));
        setBorder(BorderFactory.createTitledBorder("Params"));
        setBackground(new Color(153, 204, 210));
        
        airTemp = new JCheckBox(TAIR);
        ta9m = new JCheckBox(TA9M);
        srad = new JCheckBox(SRAD);
        wspd = new JCheckBox(WSPD);
        
        airTemp.setBackground(new Color(153, 204, 210));
        ta9m.setBackground(new Color(153, 204, 210));
        srad.setBackground(new Color(153, 204, 210));
        wspd.setBackground(new Color(153, 204, 210));
        
        Dimension preferredSize = this.getPreferredSize();
        this.setMinimumSize(preferredSize);
        
        add(airTemp);
        add(ta9m);
        add(srad);
        add(wspd);
        
    }
    
    /**
     * getting the params selected
     * @return array list of params
     */
    public ArrayList<String> getParamIds()
    {
	// create ArrayList<String> to hold selected parameters
        ArrayList<String> params = new ArrayList<String>();
        
        if (airTemp.isSelected())
        {
            params.add(TAIR);
        }
        if (ta9m.isSelected())
        {
            params.add(TA9M);
        }
        if (srad.isSelected())
        {
            params.add(SRAD);
        }
        if (wspd.isSelected())
        {
            params.add(WSPD);
        }
        if (params.isEmpty())
        {
            params.add("empty");
        }
        return params;
    }

}
