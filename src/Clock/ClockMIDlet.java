/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Clock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author marius
 */
public class ClockMIDlet extends MIDlet implements CommandListener, ItemCommandListener, Owner {

    private boolean midletPaused = false;

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command cmdLocate;
    private Command backCommand;
    private Command cmdSave;
    private Command cmdCancel;
    private Command cmdSearch;
    private Command cmdSelect;
    private Command backCommand1;
    private Clock clock;
    private Form fConfig;
    private DateField timeStop;
    private DateField timeStart;
    private DateField timePeriod;
    private StringItem textLocation;
    private Form fSearch;
    private TextField textCity;
    private StringItem strLastRes;
    private List listLocations;
    private WaitScreen waitScreen;
    private SimpleCancellableTask taskLocate;
    private Font font;
    private Ticker ticker;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The ClockMIDlet constructor.
     */
    public ClockMIDlet() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        isConfigured();//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
        switchDisplayable(null, getClock());//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == clock) {//GEN-BEGIN:|7-commandAction|1|25-preAction
            if (command == exitCommand) {//GEN-END:|7-commandAction|1|25-preAction
                // write pre-action user code here
                getDisplay().setCurrent(null);
//GEN-LINE:|7-commandAction|2|25-postAction
                getClock().reset();
            }//GEN-BEGIN:|7-commandAction|3|42-preAction
        } else if (displayable == fConfig) {
            if (command == cmdSave) {//GEN-END:|7-commandAction|3|42-preAction
                saveConfig();
                switchDisplayable(null, getClock());//GEN-LINE:|7-commandAction|4|42-postAction

            }//GEN-BEGIN:|7-commandAction|5|63-preAction
        } else if (displayable == listLocations) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|5|63-preAction
                // write pre-action user code here
                listLocationsAction();//GEN-LINE:|7-commandAction|6|63-postAction
                // write post-action user code here
            } else if (command == backCommand1) {//GEN-LINE:|7-commandAction|7|78-preAction
                // write pre-action user code here
                switchDisplayable(null, getFSearch());//GEN-LINE:|7-commandAction|8|78-postAction
                // write post-action user code here
            } else if (command == cmdSelect) {//GEN-LINE:|7-commandAction|9|67-preAction
                if (listLocations.getSelectedIndex()>=0) {
                    textLocation.setText((String)locations.elementAt(listLocations.getSelectedIndex()));
                } else return;
                switchDisplayable(null, getFConfig());//GEN-LINE:|7-commandAction|10|67-postAction

            }//GEN-BEGIN:|7-commandAction|11|73-preAction
        } else if (displayable == waitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|11|73-preAction
                // write pre-action user code here
                switchDisplayable(null, getFSearch());//GEN-LINE:|7-commandAction|12|73-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|13|72-preAction
                // write pre-action user code here
                switchDisplayable(null, getListLocations());//GEN-LINE:|7-commandAction|14|72-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|15|7-postCommandAction
        }//GEN-END:|7-commandAction|15|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|16|
    //</editor-fold>//GEN-END:|7-commandAction|16|


    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|18-getter|2|









    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: clock ">//GEN-BEGIN:|24-getter|0|24-preInit
    /**
     * Returns an initiliazed instance of clock component.
     * @return the initialized component instance
     */
    public Clock getClock() {
        if (clock == null) {//GEN-END:|24-getter|0|24-preInit
            // write pre-init user code here
            clock = new Clock();//GEN-BEGIN:|24-getter|1|24-postInit
            clock.setTitle("");
            clock.addCommand(getExitCommand());
            clock.setCommandListener(this);
            clock.setFullScreenMode(true);//GEN-END:|24-getter|1|24-postInit
            clock.setOwner(this);
        }//GEN-BEGIN:|24-getter|2|
        return clock;
    }
    //</editor-fold>//GEN-END:|24-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: isConfigured ">//GEN-BEGIN:|28-if|0|28-preIf
    /**
     * Performs an action assigned to the isConfigured if-point.
     */
    public void isConfigured() {//GEN-END:|28-if|0|28-preIf
        // enter pre-if user code here
        if (loadConfig()) {//GEN-LINE:|28-if|1|29-preAction
            // write pre-action user code here
            switchDisplayable(null, getClock());//GEN-LINE:|28-if|2|29-postAction
            // write post-action user code here
        } else {//GEN-LINE:|28-if|3|30-preAction
            // write pre-action user code here
            switchDisplayable(null, getFConfig());//GEN-LINE:|28-if|4|30-postAction
            // write post-action user code here
        }//GEN-LINE:|28-if|5|28-postIf
        // enter post-if user code here
    }//GEN-BEGIN:|28-if|6|
    //</editor-fold>//GEN-END:|28-if|6|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: fConfig ">//GEN-BEGIN:|26-getter|0|26-preInit
    /**
     * Returns an initiliazed instance of fConfig component.
     * @return the initialized component instance
     */
    public Form getFConfig() {
        if (fConfig == null) {//GEN-END:|26-getter|0|26-preInit
            // write pre-init user code here
            fConfig = new Form("Configuration", new Item[] { getTextLocation(), getTimeStart(), getTimeStop(), getTimePeriod() });//GEN-BEGIN:|26-getter|1|26-postInit
            fConfig.addCommand(getCmdSave());
            fConfig.setCommandListener(this);//GEN-END:|26-getter|1|26-postInit
            // write post-init user code here
        }//GEN-BEGIN:|26-getter|2|
        return fConfig;
    }
    //</editor-fold>//GEN-END:|26-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Items ">//GEN-BEGIN:|17-itemCommandAction|0|17-preItemCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular item.
     * @param command the Command that was invoked
     * @param displayable the Item where the command was invoked
     */
    public void commandAction(Command command, Item item) {//GEN-END:|17-itemCommandAction|0|17-preItemCommandAction
        // write pre-action user code here
        if (item == textCity) {//GEN-BEGIN:|17-itemCommandAction|1|57-preAction
            if (command == cmdSearch) {//GEN-END:|17-itemCommandAction|1|57-preAction
                // write pre-action user code here
                switchDisplayable(null, getWaitScreen());//GEN-LINE:|17-itemCommandAction|2|57-postAction
                // write post-action user code here
            }//GEN-BEGIN:|17-itemCommandAction|3|91-preAction
        } else if (item == textLocation) {
            if (command == cmdLocate) {//GEN-END:|17-itemCommandAction|3|91-preAction
                // write pre-action user code here
                switchDisplayable(null, getFSearch());//GEN-LINE:|17-itemCommandAction|4|91-postAction
                // write post-action user code here
            }//GEN-BEGIN:|17-itemCommandAction|5|17-postItemCommandAction
        }//GEN-END:|17-itemCommandAction|5|17-postItemCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|17-itemCommandAction|6|
    //</editor-fold>//GEN-END:|17-itemCommandAction|6|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdLocate ">//GEN-BEGIN:|35-getter|0|35-preInit
    /**
     * Returns an initiliazed instance of cmdLocate component.
     * @return the initialized component instance
     */
    public Command getCmdLocate() {
        if (cmdLocate == null) {//GEN-END:|35-getter|0|35-preInit
            // write pre-init user code here
            cmdLocate = new Command("Search", Command.ITEM, 0);//GEN-LINE:|35-getter|1|35-postInit
            // write post-init user code here
        }//GEN-BEGIN:|35-getter|2|
        return cmdLocate;
    }
    //</editor-fold>//GEN-END:|35-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdSave ">//GEN-BEGIN:|41-getter|0|41-preInit
    /**
     * Returns an initiliazed instance of cmdSave component.
     * @return the initialized component instance
     */
    public Command getCmdSave() {
        if (cmdSave == null) {//GEN-END:|41-getter|0|41-preInit
            // write pre-init user code here
            cmdSave = new Command("Save", Command.OK, 0);//GEN-LINE:|41-getter|1|41-postInit
            // write post-init user code here
        }//GEN-BEGIN:|41-getter|2|
        return cmdSave;
    }
    //</editor-fold>//GEN-END:|41-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|43-getter|0|43-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|43-getter|0|43-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|43-getter|1|43-postInit
            // write post-init user code here
        }//GEN-BEGIN:|43-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|43-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdCancel ">//GEN-BEGIN:|45-getter|0|45-preInit
    /**
     * Returns an initiliazed instance of cmdCancel component.
     * @return the initialized component instance
     */
    public Command getCmdCancel() {
        if (cmdCancel == null) {//GEN-END:|45-getter|0|45-preInit
            // write pre-init user code here
            cmdCancel = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|45-getter|1|45-postInit
            // write post-init user code here
        }//GEN-BEGIN:|45-getter|2|
        return cmdCancel;
    }
    //</editor-fold>//GEN-END:|45-getter|2|









    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdSearch ">//GEN-BEGIN:|56-getter|0|56-preInit
    /**
     * Returns an initiliazed instance of cmdSearch component.
     * @return the initialized component instance
     */
    public Command getCmdSearch() {
        if (cmdSearch == null) {//GEN-END:|56-getter|0|56-preInit
            // write pre-init user code here
            cmdSearch = new Command("Search", Command.ITEM, 0);//GEN-LINE:|56-getter|1|56-postInit
            // write post-init user code here
        }//GEN-BEGIN:|56-getter|2|
        return cmdSearch;
    }
    //</editor-fold>//GEN-END:|56-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdSelect ">//GEN-BEGIN:|66-getter|0|66-preInit
    /**
     * Returns an initiliazed instance of cmdSelect component.
     * @return the initialized component instance
     */
    public Command getCmdSelect() {
        if (cmdSelect == null) {//GEN-END:|66-getter|0|66-preInit
            // write pre-init user code here
            cmdSelect = new Command("Select", Command.ITEM, 0);//GEN-LINE:|66-getter|1|66-postInit
            // write post-init user code here
        }//GEN-BEGIN:|66-getter|2|
        return cmdSelect;
    }
    //</editor-fold>//GEN-END:|66-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: timeStart ">//GEN-BEGIN:|58-getter|0|58-preInit
    /**
     * Returns an initiliazed instance of timeStart component.
     * @return the initialized component instance
     */
    public DateField getTimeStart() {
        if (timeStart == null) {//GEN-END:|58-getter|0|58-preInit
            // write pre-init user code here
            timeStart = new DateField("Updates start:", DateField.TIME);//GEN-BEGIN:|58-getter|1|58-postInit
            timeStart.setLayout(ImageItem.LAYOUT_DEFAULT);
            timeStart.setDate(new java.util.Date(21600000l));//GEN-END:|58-getter|1|58-postInit
            // write post-init user code here
        }//GEN-BEGIN:|58-getter|2|
        return timeStart;
    }
    //</editor-fold>//GEN-END:|58-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: timeStop ">//GEN-BEGIN:|59-getter|0|59-preInit
    /**
     * Returns an initiliazed instance of timeStop component.
     * @return the initialized component instance
     */
    public DateField getTimeStop() {
        if (timeStop == null) {//GEN-END:|59-getter|0|59-preInit
            // write pre-init user code here
            timeStop = new DateField("Updates stop:", DateField.TIME);//GEN-BEGIN:|59-getter|1|59-postInit
            timeStop.setDate(new java.util.Date(68400000l));//GEN-END:|59-getter|1|59-postInit
            // write post-init user code here
        }//GEN-BEGIN:|59-getter|2|
        return timeStop;
    }
    //</editor-fold>//GEN-END:|59-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: timePeriod ">//GEN-BEGIN:|60-getter|0|60-preInit
    /**
     * Returns an initiliazed instance of timePeriod component.
     * @return the initialized component instance
     */
    public DateField getTimePeriod() {
        if (timePeriod == null) {//GEN-END:|60-getter|0|60-preInit
            // write pre-init user code here
            timePeriod = new DateField("Update period:", DateField.TIME);//GEN-BEGIN:|60-getter|1|60-postInit
            timePeriod.setDate(new java.util.Date(3600000l));//GEN-END:|60-getter|1|60-postInit
            // write post-init user code here
        }//GEN-BEGIN:|60-getter|2|
        return timePeriod;
    }
    //</editor-fold>//GEN-END:|60-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: fSearch ">//GEN-BEGIN:|53-getter|0|53-preInit
    /**
     * Returns an initiliazed instance of fSearch component.
     * @return the initialized component instance
     */
    public Form getFSearch() {
        if (fSearch == null) {//GEN-END:|53-getter|0|53-preInit
            // write pre-init user code here
            fSearch = new Form("Search for Location", new Item[] { getTextCity(), getStrLastRes() });//GEN-LINE:|53-getter|1|53-postInit
            // write post-init user code here
        }//GEN-BEGIN:|53-getter|2|
        return fSearch;
    }
    //</editor-fold>//GEN-END:|53-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: textCity ">//GEN-BEGIN:|55-getter|0|55-preInit
    /**
     * Returns an initiliazed instance of textCity component.
     * @return the initialized component instance
     */
    public TextField getTextCity() {
        if (textCity == null) {//GEN-END:|55-getter|0|55-preInit
            // write pre-init user code here
            textCity = new TextField("Nearest city:", "Aalborg", 32, TextField.ANY);//GEN-BEGIN:|55-getter|1|55-postInit
            textCity.addCommand(getCmdSearch());
            textCity.setItemCommandListener(this);//GEN-END:|55-getter|1|55-postInit
            // write post-init user code here
        }//GEN-BEGIN:|55-getter|2|
        return textCity;
    }
    //</editor-fold>//GEN-END:|55-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: listLocations ">//GEN-BEGIN:|62-getter|0|62-preInit
    /**
     * Returns an initiliazed instance of listLocations component.
     * @return the initialized component instance
     */
    public List getListLocations() {
        if (listLocations == null) {//GEN-END:|62-getter|0|62-preInit
            // write pre-init user code here
            listLocations = new List("Locations", Choice.EXCLUSIVE);//GEN-BEGIN:|62-getter|1|62-postInit
            listLocations.addCommand(getCmdSelect());
            listLocations.addCommand(getBackCommand1());
            listLocations.setCommandListener(this);
            listLocations.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
            listLocations.setSelectCommand(null);
            listLocations.setSelectedFlags(new boolean[] {  });//GEN-END:|62-getter|1|62-postInit
            listLocations.setSelectCommand(cmdSelect);
        }//GEN-BEGIN:|62-getter|2|
        return listLocations;
    }
    //</editor-fold>//GEN-END:|62-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: listLocationsAction ">//GEN-BEGIN:|62-action|0|62-preAction
    /**
     * Performs an action assigned to the selected list element in the listLocations component.
     */
    public void listLocationsAction() {//GEN-END:|62-action|0|62-preAction
        // enter pre-action user code here
//GEN-LINE:|62-action|1|62-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|62-action|2|
    //</editor-fold>//GEN-END:|62-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: waitScreen ">//GEN-BEGIN:|69-getter|0|69-preInit
    /**
     * Returns an initiliazed instance of waitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getWaitScreen() {
        if (waitScreen == null) {//GEN-END:|69-getter|0|69-preInit
            // write pre-init user code here
            waitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|69-getter|1|69-postInit
            waitScreen.setTitle("Searching for nearest cities...");
            waitScreen.setTicker(getTicker());
            waitScreen.setCommandListener(this);
            waitScreen.setText("Requesting list of cities...");
            waitScreen.setTextFont(getFont());
            waitScreen.setTask(getTaskLocate());//GEN-END:|69-getter|1|69-postInit
            // write post-init user code here
        }//GEN-BEGIN:|69-getter|2|
        return waitScreen;
    }
    //</editor-fold>//GEN-END:|69-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: taskLocate ">//GEN-BEGIN:|74-getter|0|74-preInit
    /**
     * Returns an initiliazed instance of taskLocate component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getTaskLocate() {
        if (taskLocate == null) {//GEN-END:|74-getter|0|74-preInit
            // write pre-init user code here
            taskLocate = new SimpleCancellableTask();//GEN-BEGIN:|74-getter|1|74-execute
            taskLocate.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|74-getter|1|74-execute
                    try {
                        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                        String url = "http://forecastfox.accuweather.com/adcbin/forecastfox/locate_city.asp?location="+
                                urlEncode(textCity.getString());

                        HttpConnection con = (HttpConnection) Connector.open(url);
                        con.setRequestMethod(HttpConnection.GET);
                        InputStream is = con.openInputStream();
                        int res = con.getResponseCode();
                        if (res == HttpConnection.HTTP_OK) {
                            getListLocations().deleteAll();
                            locations.removeAllElements();
                            parser.parse(is, handler);
                        } else {
                            System.err.println(con.getResponseMessage());
                        }
                        is.close();
                        con.close();
                        if (locations.isEmpty()) throw new Exception("No cities found");
                    } catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                        getStrLastRes().setText(ex.getMessage());
                        throw ex;
                    } catch (SAXException ex) {
                        ex.printStackTrace();
                        getStrLastRes().setText(ex.getMessage());
                        throw ex;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        getStrLastRes().setText(ex.getMessage());
                        throw ex;
                    }
                }//GEN-BEGIN:|74-getter|2|74-postInit
            });//GEN-END:|74-getter|2|74-postInit
        // write post-init user code here
        }//GEN-BEGIN:|74-getter|3|
        return taskLocate;
    }
    //</editor-fold>//GEN-END:|74-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand1 ">//GEN-BEGIN:|77-getter|0|77-preInit
    /**
     * Returns an initiliazed instance of backCommand1 component.
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {//GEN-END:|77-getter|0|77-preInit
            // write pre-init user code here
            backCommand1 = new Command("Back", Command.BACK, 0);//GEN-LINE:|77-getter|1|77-postInit
            // write post-init user code here
        }//GEN-BEGIN:|77-getter|2|
        return backCommand1;
    }
    //</editor-fold>//GEN-END:|77-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: font ">//GEN-BEGIN:|87-getter|0|87-preInit
    /**
     * Returns an initiliazed instance of font component.
     * @return the initialized component instance
     */
    public Font getFont() {
        if (font == null) {//GEN-END:|87-getter|0|87-preInit
            // write pre-init user code here
            font = Font.getDefaultFont();//GEN-LINE:|87-getter|1|87-postInit
            // write post-init user code here
        }//GEN-BEGIN:|87-getter|2|
        return font;
    }
    //</editor-fold>//GEN-END:|87-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: strLastRes ">//GEN-BEGIN:|88-getter|0|88-preInit
    /**
     * Returns an initiliazed instance of strLastRes component.
     * @return the initialized component instance
     */
    public StringItem getStrLastRes() {
        if (strLastRes == null) {//GEN-END:|88-getter|0|88-preInit
            // write pre-init user code here
            strLastRes = new StringItem("Last search result: ", null, Item.PLAIN);//GEN-LINE:|88-getter|1|88-postInit
            // write post-init user code here
        }//GEN-BEGIN:|88-getter|2|
        return strLastRes;
    }
    //</editor-fold>//GEN-END:|88-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ticker ">//GEN-BEGIN:|89-getter|0|89-preInit
    /**
     * Returns an initiliazed instance of ticker component.
     * @return the initialized component instance
     */
    public Ticker getTicker() {
        if (ticker == null) {//GEN-END:|89-getter|0|89-preInit
            // write pre-init user code here
            ticker = new Ticker("Requesting list of cities...");//GEN-LINE:|89-getter|1|89-postInit
            // write post-init user code here
        }//GEN-BEGIN:|89-getter|2|
        return ticker;
    }
    //</editor-fold>//GEN-END:|89-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: textLocation ">//GEN-BEGIN:|90-getter|0|90-preInit
    /**
     * Returns an initiliazed instance of textLocation component.
     * @return the initialized component instance
     */
    public StringItem getTextLocation() {
        if (textLocation == null) {//GEN-END:|90-getter|0|90-preInit
            // write pre-init user code here
            textLocation = new StringItem("Location: ", "EUR|DK|DA007|AALBORG|", Item.HYPERLINK);//GEN-BEGIN:|90-getter|1|90-postInit
            textLocation.addCommand(getCmdLocate());
            textLocation.setItemCommandListener(this);
            textLocation.setDefaultCommand(getCmdLocate());//GEN-END:|90-getter|1|90-postInit
            // write post-init user code here
        }//GEN-BEGIN:|90-getter|2|
        return textLocation;
    }
    //</editor-fold>//GEN-END:|90-getter|2|

    private String urlEncode(String s) {
        try {
            if (s == null) return (s);
            StringBuffer sb = new StringBuffer(100);
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') ||
                        (c >= 'a' && c <= 'z')) {
                    sb.append(c);
                    continue;
                }
                if (c > 15) { // is it a non-control char, ie. >x0F so 2 chars
                    sb.append("%" + Integer.toHexString((int) c)); // just add % and the string
                } else {
                    sb.append("%0" + Integer.toHexString((int) c)); // otherwise need to add a leading 0
                }
            }
            return (sb.toString());
        } catch(Exception ex) {
            System.out.println("Exception, URLencode string is " + s);
            return (null);
        }
    }

    Vector locations = new Vector();

    protected DefaultHandler handler = new DefaultHandler() {
        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            if ("location".equals(qName)) {
                String city = attrs.getValue("city")+", "+attrs.getValue("state");
                String location = attrs.getValue("location");
                getListLocations().append(city, null);
                locations.addElement(location);
            }
        }
    };

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;        
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    public boolean loadConfig()
    {
        try {
            RecordStore rs = RecordStore.openRecordStore("config", true);
            if (rs.getNumRecords()==0) return false;
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
            if (!re.hasNextElement()) return false;
            byte [] record = re.nextRecord();
            if (record == null) return false;
            ByteArrayInputStream bais = new ByteArrayInputStream(record);
            DataInputStream dis = new DataInputStream(bais);
            String location = dis.readUTF();
            Date start = new Date(dis.readLong());
            Date stop = new Date(dis.readLong());
            long period = dis.readLong();
            dis.close();
            rs.closeRecordStore();            
            getClock().setLocation(location); getTextLocation().setText(location);
            clock.setUpdateStart(start); getTimeStart().setDate(start);
            clock.setUpdateStop(stop); getTimeStop().setDate(stop);
            clock.setUpdatePeriod(period); getTimePeriod().setDate(new Date(period));
            clock.reload();            
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            return false;
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public void saveConfig() {
        getClock().setLocation(getTextLocation().getText());
        clock.setUpdateStart(getTimeStart().getDate());
        clock.setUpdateStop(getTimeStop().getDate());
        clock.setUpdatePeriod(getTimePeriod().getDate().getTime());
        clock.reload();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(textLocation.getText());
            dos.writeLong(timeStart.getDate().getTime());
            dos.writeLong(timeStop.getDate().getTime());
            dos.writeLong(timePeriod.getDate().getTime());
            dos.close();
            byte[] record = baos.toByteArray();
            baos.close();
            RecordStore rs = RecordStore.openRecordStore("config", true);
            if (rs.getNumRecords() > 0) {
                rs.setRecord(rs.getNextRecordID()-1, record, 0, record.length);
            } else {
                rs.addRecord(record, 0, record.length);
            }
            rs.closeRecordStore();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

    }

    public void returnControl() {
        getDisplay().setCurrent(getFConfig());
    }
}
