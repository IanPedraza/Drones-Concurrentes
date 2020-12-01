package cpproyecto1;

import static cpproyecto1.SynchronizeOptions.NONE;
import static cpproyecto1.SynchronizeOptions.SEMAPHORE;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Map extends JPanel{
    
    private final int step = 50;
    
    private final BufferedImage background;
    private final ArrayList<Drone> drones;
    private int synchronizeOption;
    private boolean graphic;
    private int speed;
    private boolean pause;

    public Map(BufferedImage background, ArrayList<Drone> drones) {
        this.graphic = true;
        this.background = background;
        this.drones = drones;
        this.synchronizeOption = SEMAPHORE;
        this.speed = 300;
        this.pause = false;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
    
    
    public void increaseSpeed() {
        if (speed - step > 0) {
            this.speed -= step;
        }
    }
    
    public void decreaseSpeed() {
        this.speed += step;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public void setGraphicEnable(boolean enable) {
        this.graphic = enable;
    }
    
    public boolean isGraphicEnable() {
        return graphic;
    }
    
    public int lenght() {
        return drones.size();
    }

    public int getSynchronizeOption() {
        return synchronizeOption;
    }

    public void setSynchronizeOption(int synchronizeOption) {
        this.synchronizeOption = synchronizeOption;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        int x, y, width, height, i, xStep, yStep, size;
        size = drones.size();
        
        g2.drawImage(background, 0, 0, this);
        
        /* verticales */
        if (size > 0) {
            if (size <= 3) {
                xStep = getWidth()/size;
                x = xStep;
                y = 0;

                width = 2;
                height = getHeight();

                for (i = 0; i < size - 1; i++) {
                    g2.setColor(Color.BLACK);
                    g2.fill(new Rectangle2D.Double(x, y, width, height));
                    x += xStep;
                }
            } else if (size > 3) {
                g2.setColor(Color.BLACK);
                g2.fill(new Rectangle2D.Double(getWidth()/2, 0, 2, getHeight()));
                
                x = 0;
                yStep = getHeight()/(size/2);
                y = yStep;
                
                width = getWidth();
                height = 2;
                
                for (i = 0; i < (size/2) - 1; i++) {
                    g2.setColor(Color.BLACK);
                    g2.fill(new Rectangle2D.Double(x, y, width, height));
                    y += yStep;
                }
            }
        }
        
        
        for (Drone drone : drones) {            
            g2.setColor(Color.BLACK);
            g2.fill(new Ellipse2D.Double(drone.getX(), drone.getY(), 20, 20));

            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(drone.getDroneNumber()), drone.getX()+9, drone.getY()+14);
        }
        
    }
    
}
