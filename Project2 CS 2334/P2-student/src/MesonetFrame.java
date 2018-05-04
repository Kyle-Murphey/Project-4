import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/***
 * 
 * @author Kyle Murphey
 * @version 2018-5-3
 *
 *the main frame for the app
 */
public class MesonetFrame extends JFrame
{

    /**
     * version ID
     */
    private static final long serialVersionUID = 1L;

    /** Menu bar */
    private FileMenuBar fileMenuBar;
    /** stat panel **/
    private StatisticsPanel statistics;
    /** param panel **/
    private ParameterPanel parameters;
    /** output panel **/
    private DataPanel dataPanel;
    /** banner **/
    private MesonetMainPanel banner;
    /** button panel **/
    private JPanel buttonPanel;
    /** calc button **/
    private JButton calcButton;
    /** exit button **/
    private JButton exitButton;
    
    /** holds file paths **/
    private ArrayList<String> filePaths = new ArrayList<String>();
    

    /***
     * frame for program
     */
    public MesonetFrame()
    {
        super("Mesonet Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        
        // Menu bar
        fileMenuBar = new FileMenuBar();
        this.setJMenuBar(fileMenuBar);
        
        // Create the custom panels
        banner = new MesonetMainPanel();
        parameters = new ParameterPanel();
        statistics = new StatisticsPanel();
        dataPanel = new DataPanel();
        buttonPanel = new JPanel();

        
        this.setMinimumSize(getPreferredSize());
        
        
        this.add(banner, BorderLayout.NORTH);
        this.add(statistics, BorderLayout.WEST);
        this.add(parameters, BorderLayout.CENTER);
        this.add(dataPanel, BorderLayout.EAST);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        // building the bottom button panel
        buildButtonPanel();
        
        this.pack();
        
    }

    /***
     * build the button panel
     */
    private void buildButtonPanel()
    {
        // Create a panel for buttons.
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(new Color(102, 178, 210));
        calcButton = new JButton("Calculate");
        exitButton = new JButton("Exit");
        
        buttonPanel.add(calcButton);
        buttonPanel.add(exitButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Register the action listeners
        exitButton.addActionListener(new ExitButtonListner());
        calcButton.addActionListener(new CalcButtonListner());
        

    }

    /***
     * 
     * @author kyle
     * @version 2018
     *
     */
    private class ExitButtonListner implements ActionListener
    {

        /**
         * event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);

        }

    }

    /***
     * 
     * @author kyle
     * @version 2018-5-3
     */
    private class CalcButtonListner implements ActionListener
    {

        /**
         * event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<String> params = parameters.getParamIds();
            String stat = statistics.getStatisticsType();

            String result = "";
            
            try
            {
                result = output(params, stat);
            }
            catch (IOException | WrongCopyrightException | ParseException | WrongParameterIdException e1)
            {
                result = "error: " + e1.getMessage();
            }

//            help with debugging
//            JOptionPane.showMessageDialog(null, 
//                    "Should dipslay what is calculated for " + type + " "
//                    + paramId + " result: " + result);

            MesonetFrame.this.dataPanel.updateData(result);

        }

    }
    
    /***
     * Configure the output for the JTextArea
     * @param params WSPD, TAIR, TA9M, SRAD
     * @param stat Max or Min
     * @return the output
     * @throws WrongParameterIdException wrong param
     * @throws ParseException bad parse
     * @throws WrongCopyrightException wrong copyright
     * @throws IOException bad files
     */
    private String output(ArrayList<String> params, String stat) throws IOException, 
    WrongCopyrightException, ParseException, WrongParameterIdException
    {
        ArrayList<String> out = new ArrayList<String>();
        
        for (int i = 0; i < params.size(); ++i)
        {
            out.add(calculate(params.get(i), stat));
        }
        
        
        return out.toString();
    }
    
    
    /***
     * Calculate the maxes and mins
     * @param param parameter
     * @param stat max or min
     * @return output for that given param
     * @throws WrongParameterIdException wrong param
     * @throws ParseException couldn't parse
     * @throws WrongCopyrightException wrong copyright
     * @throws IOException bad files
     */
    private String calculate(String param, String stat) throws IOException,
    WrongCopyrightException, ParseException, WrongParameterIdException 
    {
        if (filePaths.isEmpty() || filePaths == null)
        {
            return "No File selected";
        }
        else
        {
            format();
            
            
            String[] fileArray = new String[filePaths.size()];
            fileArray = filePaths.toArray(fileArray);
            
            DaysStatistics data = new DaysStatistics(fileArray);
            data.findStatistics();
            
            
            if (stat.equals(StatisticsPanel.MAX_BUTTON))
            {
                return data.getMax(param);
            }
            else if (stat.equals(StatisticsPanel.MIN_BUTTON))
            {
                return data.getMin(param);
            }
            else
            {
                return "Select a stat type";
            }
        }
        
    }
    
    /**
     * formats file path
     */
    private void format()
    {
        for (int i = 0; i < filePaths.size(); ++i)
        {
            filePaths.set(i, filePaths.get(i).replace("\\", "/"));
            filePaths.set(i, filePaths.get(i).substring(filePaths.get(i).length() - 40, 
                    filePaths.get(i).length()));
        }
    }
    
    /**
     * 
     * @author CS2334, modified by Kyle Murphey
     * @version 2018-
     * 
     *          Menu bar that provides file loading and program exit capabilities.
     *
     */
    public class FileMenuBar extends JMenuBar
    {
        /**
         * version ID to get rid of error
         */
        private static final long serialVersionUID = 1L;

        // Menu on the menu bar
        private JMenu menu;

        // Two options for the menu
        private JMenuItem menuOpen;
        private JMenuItem menuExit;

        // Reference to a file chooser pop-up
        private JFileChooser fileChooser;

        private ArrayList<String> listOfFiles;

        /**
         * Constructor: fully assemble the menu bar and attach the necessary action
         * listeners.
         */
        public FileMenuBar()
        {
            listOfFiles = new ArrayList<>();
            filePaths = new ArrayList<String>();
            // Create the menu and add it to the menu bar
            menu = new JMenu("File");
            add(menu);

            // Create the menu items and add them to the menu
            menuOpen = new JMenuItem("Open Data File");
            menuOpen.setName("Menu Open");
            menuExit = new JMenuItem("Exit");
            menu.add(menuOpen);
            menu.add(menuExit);

            // Action listener for exit
            menuExit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.exit(0);
                }
            });

            // Create the file chooser
            fileChooser = new JFileChooser(new File("./P2-student/data/mesonet"));
            fileChooser.setMultiSelectionEnabled(true);

            // Action listener for file open
            menuOpen.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // Ask for files
                    int returnVal = fileChooser.showOpenDialog(menuOpen);
                    // Did we get one?
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        // Yes
                        File[] files = fileChooser.getSelectedFiles();
                        // System.out.println(files.length);
                        try
                        {
                            for (File file : files)
                            {
                                String fileName = file.toString();
                                System.out.println(fileName);
                                listOfFiles.add(fileName);
                                filePaths.add(fileName);
                            }
                        }
                        catch (Exception e2)
                        {
                            // Catch all other exceptions
                            JOptionPane.showMessageDialog(fileChooser, "File load error");
                            MesonetFrame.this.setCursor(null);
                        }
                    }
                    else
                    {
                        System.out.println("No files.");
                    }
                }
            });

        }

        /**
         * get file list
         * @return file list
         */
        public ArrayList<String> getFileList()
        {
            return (ArrayList<String>) listOfFiles.clone();
        }
    }

}
