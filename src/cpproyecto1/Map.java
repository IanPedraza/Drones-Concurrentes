package cpproyecto1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Map extends JPanel {

    private final BufferedImage background;
    private final ArrayList<Drone> drones;

    public Map(BufferedImage background, ArrayList<Drone> drones) {
        this.background = background;
        this.drones = drones;
    }

    public int lenght() {
        return drones.size();
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
                xStep = getWidth() / size;
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
                g2.fill(new Rectangle2D.Double(getWidth() / 2, 0, 2, getHeight()));

                x = 0;
                yStep = getHeight() / (size / 2);
                y = yStep;

                width = getWidth();
                height = 2;

                for (i = 0; i < (size / 2) - 1; i++) {
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
            g2.drawString(String.valueOf(drone.getDroneNumber()), drone.getX() + 9, drone.getY() + 14);
        }

    }

}
