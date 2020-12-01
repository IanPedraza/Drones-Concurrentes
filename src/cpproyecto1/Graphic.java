package cpproyecto1;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graphic extends JPanel {
    
    private final int range = 20;
    
    private final XYSeries graphic;
    
    private final ChartPanel panel;
    private final XYSeriesCollection dataset;
    private final JFreeChart chart;

    public Graphic() {
        dataset = new XYSeriesCollection();
        
        graphic = new XYSeries("Gráfica 1");
        
        dataset.addSeries(graphic);
        
        chart = ChartFactory.createXYLineChart(
                "Rendimiento de la Sincronización",
                "Iteracciones",
                "Dron en SC",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        
        chart.getXYPlot().getRangeAxis().setRange(0, 10);
        //chart.getXYPlot().getDomainAxis().setAutoRange(false);
        
        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickUnit(new NumberTickUnit(1));
        XYPlot plot = chart.getXYPlot();
        plot.setDomainAxis(xAxis);
        
        chart.getXYPlot().setDomainPannable(true);
        
        panel = new ChartPanel(chart);       
        panel.setMouseZoomable(true, true);
        panel.setMouseWheelEnabled(true);
           
        setLayout(new BorderLayout());
        add(panel);
        validate();
    }

    public int getItemCount() {
        return graphic.getItemCount();
    }
    
    public void actualizar(ArrayList<Double> datosX, ArrayList<Double> datosY) {
        
        for (int i = graphic.getItemCount(); i < datosX.size(); i++) {   
            graphic.add(new XYDataItem(datosX.get(i), datosY.get(i)));
        }
        
        panel.repaint();
    }
    
    public void addToGraphic(double x, double y) {
        graphic.add(new XYDataItem(x, y));
        chart.getXYPlot().getDomainAxis().setRange(x-range, x);
        panel.repaint();
    }
    
    public void addToGraphic(double y) {
        try {
            int x = graphic.getItemCount()+1;

            graphic.add(new XYDataItem(x, y));
            chart.getXYPlot().getDomainAxis().setRange(x-range, x);

            panel.repaint();
        } catch (Exception e) {
            System.out.println("Error: Dos hilos en sección crítica");
        }
    }

    public void limpiar() {
        graphic.clear();
        //dataset.removeAllSeries();
        panel.repaint();
    }
}
