/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Clock;

import Weather.Weather;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author marius
 */
public class Clock extends Canvas {
    private Font font = null;
    private Image dig[] = new Image[10];
    private Image sl = null;
    private Image bg = null;
    private Image fg = null;
    private Weather weather = new Weather(this);
    private Font smallFont = Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private Font largeFont = Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_BOLD, Font.SIZE_LARGE);
    private Font mediumFont = Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private Font boldFont = Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    private static String[] month = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November",
            "December"
        };
    private static String[] dow = {null, "Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday" };

    Calendar start = Calendar.getInstance();
    Calendar stop = Calendar.getInstance();
    long period = 3*60*60*1000, lastReload;
    Calendar today = Calendar.getInstance();
    String city;
    private int id = 0;
    public void setLocation(String loc){
        city = loc.substring(loc.lastIndexOf('|', loc.length()-2)+1, loc.length()-1);
        weather.setLocation(loc);
    }
    public void setUpdateStart(Date d){ start.setTime(d); }
    public void setUpdateStop(Date d){ stop.setTime(d); }
    public void setUpdatePeriod(long t){ period = t; }
    public void reload(){ 
        weather.reload();
        lastReload = System.currentTimeMillis();
        reset();
    }

    public void reset() { id = 0; repaint(); }


    public Clock(){
        start.set(Calendar.HOUR, 7);
        start.set(Calendar.MINUTE, 0);
        stop.set(Calendar.HOUR, 20);
        stop.set(Calendar.MINUTE, 0);

        new Thread(new Runnable(){
            public void run() {
                long now, x, w=0;
                Date d;
                try {
                    now = System.currentTimeMillis();
                    w = now + period;
                    reload();
                    while (true) {
                        now = System.currentTimeMillis();
                        //x = 1001 - (now % 1000);
                        if (now > w) {
                            today.setTime(new Date(now));
                            start.set(Calendar.YEAR, today.get(Calendar.YEAR));
                            start.set(Calendar.DATE, today.get(Calendar.DATE));
                            stop.set(Calendar.YEAR, today.get(Calendar.YEAR));
                            stop.set(Calendar.DATE, today.get(Calendar.DATE));
                            if (today.after(start) && today.before(stop)) {
                                w = now + period;
                                reload();
                            } else {
                                w = start.getTime().getTime();
                                if (today.after(start)) w += 24*60*60*1000;
                            }
                        }
                        x = 60001 - (now % 60000);
                        repaint();
                        Thread.sleep(x);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();        
    }

    Owner owner;
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    Font getFont() {
        if (font == null)
         font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        return font;
    }

    Image getDigit(int i){
        if (i<0 || i>=dig.length) return null;
        if (dig[i] == null) try {
            dig[i] = Image.createImage("/Clock/icons/" + i + ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dig[i];
    }
    Image getSlot() {
        if (sl == null) try {
            sl = Image.createImage("/Clock/icons/slot.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sl;
    }

    Image getBackground() {
        if (bg == null) try {
            bg = Image.createImage("/Clock/icons/bg.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bg;
    }

    Image getForeground() {
        if (fg == null) try {
            fg = Image.createImage("/Clock/icons/fg.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fg;
    }

    Image moon = null;
    String moonPhase = null;

    Image getMoon(String phase) {
        if (phase.equals(moonPhase)) return moon;
        try {
            moon = Image.createImage("/Clock/moon/"+phase+".png");
            moonPhase = phase;
        } catch (IOException ex) {
            ex.printStackTrace();
            moon = null;
            moonPhase = null;
        }
        return moon;
    }

    int drawNumber(Graphics g, int x, int y, int n) {
        Image slot = getSlot();
        Image n1 = getDigit(n/10%10);
        Image n2 = getDigit(n%10);
        int cx = x+slot.getWidth()/2;
        int cy = y+slot.getHeight()/2;
        cx += (n1.getWidth() - n2.getWidth())/2;
        g.drawImage(slot, x, y, Graphics.TOP | Graphics.LEFT);
        g.drawImage(n1, cx, cy, Graphics.VCENTER | Graphics.RIGHT);
        g.drawImage(n2, cx, cy, Graphics.VCENTER | Graphics.LEFT);
        return x+slot.getWidth();
    }

    protected void drawString(Graphics g, int color, String s, int x, int y, int pos){
        g.setColor(0, 0, 0);
        g.drawString(s, x-1, y-1, pos);
        g.drawString(s, x-1, y+1, pos);
        g.drawString(s, x+1, y+1, pos);
        g.drawString(s, x+1, y-1, pos);
        g.setColor(color);
        g.drawString(s, x, y, pos);
    }

    protected void paint(Graphics g) {
        int w = getWidth(), h = getHeight();
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, w, h);
        Image i = getBackground();
        if (i!=null) g.drawImage(i, 0, 0, Graphics.TOP | Graphics.LEFT);
        Calendar cal = Calendar.getInstance();
        Image s = getSlot();
        int posx = w/2-s.getWidth()-4, posy = h/3-s.getHeight()/2;
        posx = drawNumber(g, posx, posy, cal.get(Calendar.HOUR_OF_DAY));
        posx = drawNumber(g, posx+8, posy, cal.get(Calendar.MINUTE));
        //g.setColor(255,255,255);
        //g.drawString(""+w+"x"+h, w/2, h*2/3, Graphics.TOP | Graphics.HCENTER);
        if (weather.getStatus()!=null) {
            i = getForeground();
            if (i!=null) g.drawImage(i, 0, 0, Graphics.TOP | Graphics.LEFT);
            drawString(g, 0xFFFFFF, weather.getStatus(), 0, 48, Graphics.TOP | Graphics.LEFT);
        } else {
            int y = h/3+s.getHeight()/2;
            int hh = smallFont.getHeight()+largeFont.getHeight()+mediumFont.getHeight()+10;
            //g.setColor(0, 0, 0);  g.fillRoundRect(0, y, w, hh, 5, 5);
            g.setFont(smallFont);
            if (id>0) cal.setTime(weather.getDate(id));
            final int d = cal.get(Calendar.DAY_OF_WEEK);
            final String m = month[cal.get(Calendar.MONTH)];
            final String day = dow[d];
            posx = 0;
            if (smallFont.stringWidth(m) > posx) posx = smallFont.stringWidth(m);
            if (mediumFont.stringWidth(day) > posx) posx = mediumFont.stringWidth(day);
            drawString(g, 0xFFFFFF, m, 5+posx/2, y+5, Graphics.TOP | Graphics.HCENTER);
            int color;
            if (d==Calendar.SUNDAY) color = 0xFF00000;
            else if (d==Calendar.SATURDAY) color = 0xFFB0B0;
            else color = 0xFFFFFF;
            g.setFont(largeFont);
            drawString(g, color, ""+cal.get(Calendar.DAY_OF_MONTH), 5+posx/2, y+5+smallFont.getHeight(), Graphics.TOP | Graphics.HCENTER);
            g.setFont(mediumFont);
            drawString(g, color, day, 5+posx/2, y+5+smallFont.getHeight()+largeFont.getHeight(), Graphics.TOP | Graphics.HCENTER);
            i = weather.getIcon(id);
            if (i!=null) {
                g.drawImage(i, w/2, y+hh/2, Graphics.VCENTER | Graphics.HCENTER);
            }            
            g.setFont(smallFont);
            if (id==0) {
                color = 0xFFFFFF;
                drawString(g, color, city+" now:", w-5, y+hh/2-2*smallFont.getHeight(), Graphics.TOP | Graphics.RIGHT);
            } else {
                if (id%2==0) {
                    color = 0x00FFFF;
                    drawString(g, color, city+" night:", w-5, y+hh/2-2*smallFont.getHeight(), Graphics.TOP | Graphics.RIGHT);
                } else {
                    color = 0xFFFF00;
                    drawString(g, color, city+" day:", w-5, y+hh/2-2*smallFont.getHeight(), Graphics.TOP | Graphics.RIGHT);
                }
            }
            drawString(g, color, weather.getText(id), w-5, y+hh/2-boldFont.getHeight(), Graphics.TOP | Graphics.RIGHT);
            drawString(g, color, weather.getTemp(id), w-5, y+hh/2,                      Graphics.TOP | Graphics.RIGHT);
            drawString(g, color, weather.getWind(id), w-5, y+hh/2+boldFont.getHeight(), Graphics.TOP | Graphics.RIGHT);
            i = getMoon(weather.getMoonPhase(id));
            if (i!=null) {
                g.drawImage(i, w/2, h/3-s.getHeight()/2, Graphics.VCENTER | Graphics.HCENTER);
                //g.setColor(128,128,128);
                //g.drawArc(w/2-i.getWidth()/2, h/2-s.getHeight()/2-i.getHeight()/2, i.getWidth(), i.getHeight(), 0, 360);
            }
            /*
            if (key != 0) {
                g.setFont(smallFont);
                g.setColor(0xFFFFFF);
                g.drawString("k="+key+" n="+getKeyName(key), 0, h, Graphics.LEFT | Graphics.BOTTOM);
            }*/
            g.setColor(0xFFFFFF);
            g.drawString(""+((System.currentTimeMillis()-lastReload+30)/1000/60)+"m", w, 40, Graphics.RIGHT | Graphics.BOTTOM);
            //g.drawString(""+(weather.getMaxId()/2)+"d", 0, 48, Graphics.LEFT | Graphics.BOTTOM);
            //drawString(g, 0xFFFFFF, weather.getDate(id).toString(), 0, h, Graphics.BOTTOM | Graphics.LEFT);
        }
    }
    int key=0;
    protected void keyReleased(int keyCode){
        key = keyCode;        
        if (key == -8) { // C or Clear
            if (owner!=null) owner.returnControl();
        } else if (key == -5) { // Fire or Select
            reload();
        } else if (key == -3) { // Left
            if (id>0) id--;
            repaint();
        } else if (key == -4) { // Right
            if (id<weather.getMaxId()) id++;
            repaint();
        } else repaint();
    }
}
