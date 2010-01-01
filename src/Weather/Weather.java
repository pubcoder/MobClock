/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Weather;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author marius
 */
public class Weather {
    private String status;
    private boolean busy;
    private static String server = "http://forecastfox.accuweather.com/adcbin/forecastfox/weather_data.asp?location=";
    private String location = "EUR|DK|DA007|AALBORG|";
    private static String postfix = "&metric=1&partner=forecastfox";
    private Worker worker = new Worker();

    public Weather(){
        busy = false;
    }

    public void reload(){
        new Thread(worker).start();
    };

    public void setLocation(String loc){
        location = loc;
    }

    String pressure, temperature, realfeel, humidity, weathertext, weathericon, windSpeed, windDir;

    public String getCurrentText(){ return weathertext; }
    public Image getCurrentIcon(){
        Image img = null;
        try {
            img = Image.createImage("/Weather/icons/" + weathericon + ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return img;
    }
    public String getCurrentPressure(){ return pressure; }
    public String getCurrentTemp(){
        if (temperature.charAt(0)!='-') return "+"+temperature+"\u00B0C";
        else return temperature+"\u00B0C";
    }
    public String getCurrentFeel(){
        if (realfeel.charAt(0)!='-') return "+"+realfeel+"\u00B0C";
        else return realfeel+"\u00B0C";
    }
    public String getCurrentWind(){ return windDir+", "+windSpeed+"m/s"; }
    public String getCurrentWindSpeed(){ return windSpeed; }
    public String getCurrentWindDirection(){ return windDir; }
    public String getCurrentHumidity(){ return humidity; }
    public String getStatus() { return status; }

    protected class Worker implements Runnable {
        protected SAXParserFactory factory;
        protected SAXParser parser;
        public Worker() {
            factory = SAXParserFactory.newInstance();
            try {
                if (parser == null) {
                    parser = factory.newSAXParser();
                }
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
                status = ex.getMessage();
            } catch (SAXException ex) {
                ex.printStackTrace();
                status = ex.getMessage();
            }
        }
        public void run() {
            if (parser==null) { status = "no parser"; return; }
            if (busy) {
                status = "updating!!!";
                return;
            } else {
                status = "updating";
                busy = true;
            }
            String error = null;
            String url = server+location+postfix;
            try {
                HttpConnection con = (HttpConnection) Connector.open(url);
                con.setRequestMethod(HttpConnection.GET);
                /*con.setRequestProperty("User-Agent", agent);
                con.setRequestProperty("Accept", accept);
                con.setRequestProperty("Accept-Language", acceptLang);
                con.setRequestProperty("Accept-Encoding", acceptEnc);
                con.setRequestProperty("Accept-Charset", acceptCharset);
                con.setRequestProperty("Keep-Alive", keepAlive);
                con.setRequestProperty("Connection", connection);*/
                //con.setRequestProperty("cookie", cookie);
                InputStream is = con.openInputStream();
                int res = con.getResponseCode();
                if (res == HttpConnection.HTTP_OK) {
                    parser.parse(is, handler);
                } else {
                    error = con.getResponseMessage();
                }
                is.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
                error = e.getMessage();
            }
            busy  = false;
            status = error;
        }
    }
    protected DefaultHandler handler = new DefaultHandler() {
        Stack path = new Stack();
        int currentDepth = 0;
        public void characters(char[] ch, int start, int length) {
            if (currentDepth==2) {
                String s = (String)path.peek();
                if ("pressure".equals(s)) pressure = new String(ch, start, length);
                else if ("temperature".equals(s)) temperature = new String(ch, start, length);
                else if ("realfeel".equals(s)) realfeel = new String(ch, start, length);
                else if ("humidity".equals(s)) humidity = new String(ch, start, length);
                else if ("weathertext".equals(s)) weathertext = new String(ch, start, length);
                else if ("weathericon".equals(s)) weathericon = new String(ch, start, length);
                else if ("windspeed".equals(s)) windSpeed = new String(ch, start, length);
                else if ("winddirection".equals(s)) windDir = new String(ch, start, length);
            }
        }

        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            path.push(qName);
            switch (currentDepth){
                case 0:
                    if ("adc_database".equals(qName)) currentDepth++;
                    break;
                case 1:
                    if ("currentconditions".equals(qName)) currentDepth++;
                    break;
            }
        }

        public void endElement(String uri, String localName, String qName) {
            path.pop();
            switch (currentDepth){
                case 1:
                    if ("adc_database".equals(qName)) currentDepth--;
                    break;
                case 2:
                    if ("currentconditions".equals(qName)) currentDepth--;
                    break;
            }
        }
    };
}
