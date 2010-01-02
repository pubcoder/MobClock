/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Weather;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Canvas;
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
    private Hashtable moonPhases = new Hashtable();
    private Canvas owner;

    public Weather(Canvas owner){
        busy = false;
        this.owner = owner;
    }

    public void reload(){
        new Thread(worker).start();
    };

    public void setLocation(String loc){ location = loc; }

    Date today;
    String pressure, temperature, realfeel, humidity, weathertext, weathericon, windSpeed, windGusts, windDir;
    Vector forecasts = new Vector();
    Forecast forecast = null;

    public int getMaxId(){
        return forecasts.size()*2;
    }
    public String getMoonPhase(int id) {
        if (id == 0) return (String)moonPhases.get(today.toString());
        id--;
        if (id/2>=forecasts.size()) id = forecasts.size()*2-1;
        return (String)moonPhases.get(((Forecast)forecasts.elementAt(id/2)).date.toString());
    }

    public Date getDate(int id){
        if (id==0) return today;
        id--;
        if (id/2>=forecasts.size()) id = forecasts.size()*2-1;
        return ((Forecast)forecasts.elementAt(id/2)).date;
    }

    public String getText(int id){
        if (id==0) return weathertext;
        id--;
        if (id/2>=forecasts.size()) id = forecasts.size()*2-1;
        return ((Forecast)forecasts.elementAt(id/2)).parts[id%2].text;
    }
    public Image getIcon(int id){
        String w = weathericon;
        if (id>0) {
            id--;
            if (id/2>=forecasts.size()) id = forecasts.size()*2-1;
            w = ((Forecast)forecasts.elementAt(id/2)).parts[id%2].icon;
        }
        Image img = null;
        try {
            img = Image.createImage("/Weather/icons/" + w + ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return img;
    }
    //public String getCurrentPressure(){ return pressure; }
    public String getTemp(int id) {
        if (id==0) return getCurrentTemperature()+" ("+getCurrentRealFeel()+")";
        id--;
        if (id/2>=forecasts.size()) id = forecasts.size()*2-1;
        return ((Forecast)forecasts.elementAt(id/2)).parts[id%2].getTemperature();
    }
    public String getCurrentTemperature(){
        if (temperature.charAt(0)!='-' && temperature.charAt(0)!='0')
            return "+"+temperature+"\u00B0C";
        else return temperature+"\u00B0C";
    }
    public String getCurrentRealFeel(){
        if (realfeel.charAt(0)!='-' && realfeel.charAt(0)!='0')
            return "+"+realfeel+"\u00B0C";
        else return realfeel+"\u00B0C";
    }
    public String getWind(int id){
        if (id==0) {
            if (windSpeed!=null && !windSpeed.equals(windGusts))
                return windDir+": "+windSpeed+"-"+windGusts+"m/s";
            else
                return windDir+": "+windSpeed+"m/s";
        } else {
            id--;
            if (id/2>=forecasts.size()) id = forecasts.size()*2-1;
            return ((Forecast)forecasts.elementAt(id/2)).parts[id%2].getWind();
        }
    }
    //public String getCurrentWindSpeed(){ return windSpeed; }
    //public String getCurrentWindDirection(){ return windDir; }
    //public String getCurrentHumidity(){ return humidity; }
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
            String url = server+urlEncode(location)+postfix;
            try {
                HttpConnection con = (HttpConnection) Connector.open(url);
                con.setRequestMethod(HttpConnection.GET);
                InputStream is = con.openInputStream();
                int res = con.getResponseCode();
                if (res == HttpConnection.HTTP_OK) {
                    forecasts.removeAllElements();
                    today = null;
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
            owner.repaint();
        }
    }
    protected DefaultHandler handler = new DefaultHandler() {
        Stack path = new Stack();
        int currentDepth = 0;
        int moonDepth = 0;
        int foreDepth = 0;
        String phaseDate = null;
        boolean newMoon = false;
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
                else if ("windgusts".equals(s)) windGusts = new String(ch, start, length);
                else if ("winddirection".equals(s)) windDir = new String(ch, start, length);
            } else if (moonDepth==3 && phaseDate!=null) {
                Date d = Forecast.parse(phaseDate);
                if (today == null) today = d;
                else if (d.getTime() < today.getTime()) today = d;
                if (newMoon) moonPhases.put(d.toString(), "0");
                else {
                    String s = new String(ch, start, length);
                    moonPhases.put(d.toString(), s);
                }
            } else if (foreDepth==3) {
                String s = (String)path.peek();
                if ("obsdate".equals(s)) {
                    forecast.setDate(new String(ch, start, length));
                    if (today == null) today = forecast.date;
                    else if (forecast.date.getTime() < today.getTime())
                        today = forecast.date;
                }
            } else if (foreDepth==4 || foreDepth==5) {
                String s = (String)path.peek();
                int p = foreDepth-4;
                if ("txtshort".equals(s)) forecast.parts[p].text = new String(ch, start, length);
                else if ("weathericon".equals(s)) forecast.parts[p].icon = new String(ch, start, length);
                else if ("hightemperature".equals(s)) forecast.parts[p].hightemp = new String(ch, start, length);
                else if ("lowtemperature".equals(s)) forecast.parts[p].lowtemp = new String(ch, start, length);
                else if ("realfeelhigh".equals(s)) forecast.parts[p].feelhigh = new String(ch, start, length);
                else if ("realfeellow".equals(s)) forecast.parts[p].feellow = new String(ch, start, length);
                else if ("windspeed".equals(s)) forecast.parts[p].windSpeed = new String(ch, start, length);
                else if ("windgust".equals(s)) forecast.parts[p].windGusts = new String(ch, start, length);
                else if ("winddirection".equals(s)) forecast.parts[p].windDir = new String(ch, start, length);
                else if ("rainamount".equals(s)) forecast.parts[p].rain = new String(ch, start, length);
                else if ("snowamount".equals(s)) forecast.parts[p].snow = new String(ch, start, length);
                else if ("precipamount".equals(s)) forecast.parts[p].precip = new String(ch, start, length);
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
            switch (moonDepth){
                case 0:
                    if ("adc_database".equals(qName)) moonDepth++;
                    break;
                case 1:
                    if ("moon".equals(qName)) moonDepth++;
                    break;
                case 2:
                    if ("phase".equals(qName)) {
                        phaseDate = attrs.getValue("date");
                        if ("New".equals(attrs.getValue("text"))) newMoon = true;
                        moonDepth++;
                    }
                    break;
            }
            switch (foreDepth) {
                case 0:
                    if ("adc_database".equals(qName)) foreDepth++;
                    break;
                case 1:
                    if ("forecast".equals(qName)) foreDepth++;
                    break;
                case 2:
                    if ("day".equals(qName)) {
                        foreDepth++;
                        forecast = new Forecast();
                    }
                    break;
                case 3:
                    if ("daytime".equals(qName)) foreDepth++;
                    else if ("nighttime".equals(qName)) foreDepth+=2;
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
            switch (moonDepth){
                case 1:
                    if ("adc_database".equals(qName)) moonDepth--;
                    break;
                case 2:
                    if ("moon".equals(qName)) moonDepth--;
                    break;
                case 3:
                    if ("phase".equals(qName)) {
                        moonDepth--;
                        phaseDate=null;
                        newMoon = false;
                    }
                    break;
            }
            switch (foreDepth){
                case 1:
                    if ("adc_database".equals(qName)) foreDepth--;
                    break;
                case 2:
                    if ("forecast".equals(qName)) foreDepth--;
                    break;
                case 3:
                    if ("day".equals(qName)) {
                        foreDepth--;
                        forecasts.addElement(forecast);
                        forecast = null;
                    }
                    break;
                case 4:
                    if ("daytime".equals(qName)) foreDepth--;
                    break;
                case 5:
                    if ("nighttime".equals(qName)) foreDepth -= 2;
                    break;
            }
        }
    };
    private String urlEncode(String s) {
        try {
            if (s == null) return (s);
            StringBuffer sb = new StringBuffer(100);
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') ||
                        (c >= 'a' && c <= 'z') || (c=='|')) {
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
            System.err.println("Exception, URLencode string is " + s);
            return (null);
        }
    }
}
