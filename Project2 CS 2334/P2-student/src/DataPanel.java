import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/***
 * 
 * @author Kyle Murphey
 * @version 2018-5-3
 * 
 * creates the data panel
 *
 */
public class DataPanel extends JPanel
{
    /**
     * version ID
     */
    private static final long serialVersionUID = 5777740971062336138L;

    /** output area **/
    private JTextArea resultDescription;
    /** layout **/
    private GridBagConstraints layoutConst;
    
    /**
     * creating the data panel
     */
    public DataPanel()
    {
        final int COLUMN_FIELD_WIDTH = 50;
        final int COLUMN_FIELD_HEIGHT = 6;
        
        this.setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Output"));
        
        
        resultDescription = new JTextArea(COLUMN_FIELD_HEIGHT, COLUMN_FIELD_WIDTH);
        
        layoutConst = new GridBagConstraints();
        layoutConst.gridx = 0;
        layoutConst.gridy = 3;
        layoutConst.insets = new Insets(10, 10, 10, 10);
        this.add(resultDescription, layoutConst);
        
        this.setBackground(new Color(0, 125, 210));
        
    }
    
    /**
     * update the data on the output aread
     * @param result results from calculations (extrema)
     */
    public synchronized void updateData(String result)
    {
        resultDescription.setText(result);
    }
    
    

    
}
