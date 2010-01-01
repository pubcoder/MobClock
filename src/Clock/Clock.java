/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Clock;

import Weather.Weather;
import java.io.IOException;
import java.util.Calendar;
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
    private Weather weather = new Weather();
    private Font smallFont = Font.getFont(Font.FONT_STATIC_TEXT, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    public Clock(){
        new Thread(new Runnable(){
            public void run() {
                long now, x, w=0;
                try {
                    now = System.currentTimeMillis();
                    w = now + 3*60*60*1000;
                    weather.reload();
                    while (true) {
                        now = System.currentTimeMillis();
                        //x = 60001 - (now % 60000);
                        x = 1001 - (now % 1000);
                        if (now > w) {
                            w = now + 3*60*60*1000;
                            weather.reload();
                        }
                        Thread.sleep(x);
                        repaint();
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();        
    }

    Font getFont() {
        if (font == null)
         font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        return font;
    }

    Image getDigit(int i){
        if (i<0 || i>=dig.length) return null;
        if (dig[i] == null) try {
            dig[i] = Image.createImage("/Clock/" + i + ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dig[i];
    }
    Image getSlot() {
        if (sl == null) try {
            sl = Image.createImage("/Clock/slot.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sl;
    }

    Image getBackground() {
        if (bg == null) try {
            bg = Image.createImage("/Clock/bg.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bg;
    }

    Image getForeground() {
        if (fg == null) try {
            fg = Image.createImage("/Clock/fg.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fg;
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

    protected void paint(Graphics g) {
        int w = getWidth(), h = getHeight();
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, w, h);
        Image i = getBackground();
        if (i!=null) g.drawImage(i, 0, 0, Graphics.TOP | Graphics.LEFT);
        Calendar cal = Calendar.getInstance();
        Image s = getSlot();
        int posx = w/2-s.getWidth()-4, posy = h/2-s.getHeight()/2;
        posx = drawNumber(g, posx, posy, cal.get(Calendar.HOUR_OF_DAY));
        posx = drawNumber(g, posx+8, posy, cal.get(Calendar.MINUTE));
        //g.setColor(255,255,255);
        //g.drawString(""+w+"x"+h, w/2, h*2/3, Graphics.TOP | Graphics.HCENTER);
        if (weather.getStatus()!=null) {
            i = getForeground();
            if (i!=null) g.drawImage(i, 0, 0, Graphics.TOP | Graphics.LEFT);
        } else {
            i = weather.getCurrentIcon();
            if (i!=null) g.drawImage(i, w/2, h/2+s.getHeight()/2, Graphics.VCENTER | Graphics.LEFT);
            g.setColor(255,255,0);
            g.setFont(smallFont);
            g.drawString(weather.getCurrentText(), 20, h/2+s.getHeight()/2, Graphics.BASELINE | Graphics.LEFT);
            g.drawString(weather.getCurrentTemp()+" ("+weather.getCurrentFeel()+")", 20, h/2+s.getHeight()/2+14, Graphics.BASELINE | Graphics.LEFT);
            g.drawString(weather.getCurrentWind(), 20, h/2+s.getHeight()/2+28, Graphics.BASELINE | Graphics.LEFT);
        }
    }

}
