/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Weather;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author marius
 */
public class Forecast {
    public Date date;
    public class Params {
    public String text, icon, hightemp, lowtemp, feelhigh, feellow,
            windSpeed, windGusts, windDir, snow, rain, precip;
            public String getTemperature(){
                return getTemp()+" ("+getFeel()+")";
            }
            public String getTemp(){
                StringBuffer s = new StringBuffer();
                if (lowtemp.charAt(0)!='-' && lowtemp.charAt(0)!='0')
                    s.append("+");
                s.append(lowtemp).append("..");
                if (hightemp.charAt(0)!='-' && hightemp.charAt(0)!='0') s.append("+");
                s.append(hightemp).append("\u00B0C");
                return s.toString();
            }
            public String getFeel(){
                StringBuffer s = new StringBuffer();
                if (feellow.charAt(0)!='-' && feellow.charAt(0)!='0') s.append("+");
                s.append(feellow).append("..");
                if (feelhigh.charAt(0)!='-' && feelhigh.charAt(0)!='0')
                    s.append("+");
                s.append(feelhigh).append("\u00B0C");
                return s.toString();
            }
            public String getWind(){
                if (windSpeed!=null && !windSpeed.equals(windGusts))
                    return windDir+": "+windSpeed+"-"+windGusts+"m/s";
                else
                    return windDir+": "+windSpeed+"m/s";
            }
    };
    public Params day = new Params();
    public Params night = new Params();
    public Params parts[] = { day, night };

    public void setDate(String d){ date = parse(d); }

    public static Date parse(String d){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(0));
        int i = 0, j = d.indexOf('/');
        c.set(Calendar.MONTH, Integer.parseInt(d.substring(i, j))-1+Calendar.JANUARY);
        i = j+1; j = d.indexOf('/', j+1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d.substring(i, j)));
        i = j+1;
        c.set(Calendar.YEAR, Integer.parseInt(d.substring(i)));
        return c.getTime();
    }
}
