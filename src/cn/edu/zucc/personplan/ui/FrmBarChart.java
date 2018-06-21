package cn.edu.zucc.personplan.ui;
import java.awt.BorderLayout;
import java.util.Map;

import cn.edu.zucc.personplan.control.ChartManager;
import org.jfree.chart.ChartFactory;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartPanel;
import javax.swing.*;

public class FrmBarChart extends JFrame{
    public FrmBarChart(){
        DefaultCategoryDataset DataSet = new DefaultCategoryDataset();
        Map<String,Float> result = new ChartManager().getAllPlanRate();
        for (Map.Entry<String,Float> entry: result.entrySet()){
            DataSet.addValue((double)entry.getValue(),"percentage",entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart3D("各计划完成度",
                "计划名称", "完成度", DataSet, PlotOrientation.VERTICAL,
                false, false, false);
        ChartPanel panel = new ChartPanel(chart);
        JPanel jp = new JPanel();
        jp.add(panel, BorderLayout.CENTER);
        this.add(jp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 700, 500);
        this.setVisible(true);
    }
}

