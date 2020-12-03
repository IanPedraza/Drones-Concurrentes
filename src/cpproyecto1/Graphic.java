package cpproyecto1;

import java.awt.BorderLayout;
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
    private int max, x, size, goals;
    private double last;

    private final XYSeries graphic;

    private final ChartPanel panel;
    private final XYSeriesCollection dataset;
    private final JFreeChart chart;
    private final String title;
    
    private OnRateListener listener;

    public Graphic() {
        dataset = new XYSeriesCollection();

        graphic = new XYSeries("Gráfica");

        dataset.addSeries(graphic);

        this.max = 0;
        this.goals = 0;
        this.last = 1;
        this.title = "Rendimiento de la Sincronización";

        chart = ChartFactory.createXYLineChart(
                title + "\n" + "Semáforos",
                "Iteracciones",
                "Dron en SC",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        //chart.getXYPlot().getRangeAxis().setRange(0, 10);

        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickUnit(new NumberTickUnit(1));
        xAxis.setLabel("Iteraciones");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(new NumberTickUnit(1));
        yAxis.setLabel("Dron en SC");
        yAxis.setRange(0, 10);
        
        XYPlot plot = chart.getXYPlot();
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);


        panel = new ChartPanel(chart);
        //panel.setMouseZoomable(true, true);
        panel.setMouseWheelEnabled(true);

        setLayout(new BorderLayout());
        add(panel);
        validate();
    }

    public void setOnRateListener(OnRateListener listener){
        this.listener = listener;
    }
    
    public int getItemCount() {
        return graphic.getItemCount();
    }

    public void addToGraphic(double y) {
        size = graphic.getItemCount();
        x = size + 1;
        
        if (size > 0) {
            if ((last == max && y == 1) || (last + 1 == y)) {
                //sincronizado
                goals++;
                last = y;
            } else { last = y; }
            
            if (listener != null) listener.onChange((goals*100)/size);
            
        } else { last = y; }

        graphic.add(new XYDataItem(x, y));
        chart.getXYPlot().getDomainAxis().setRange(x - range, x);

    }

    public void limpiar() {
        graphic.clear();
        //dataset.removeAllSeries();
        panel.repaint();
    }

    public void setMax(int max) {
        this.max = max;
        this.goals = 0;
        graphic.clear();
    }

    public void changeMethod(String method) {
        graphic.clear();
        goals = 0;
        chart.setTitle(title + "\n" + method);
    }

}
