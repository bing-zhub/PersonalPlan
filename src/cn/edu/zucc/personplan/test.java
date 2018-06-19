package cn.edu.zucc.personplan;
import java.awt.BorderLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartPanel;
import javax.swing.*;

public class test extends JFrame{
    //constructor
    public test(){
        //构造DataSet
        DefaultCategoryDataset DataSet = new DefaultCategoryDataset();
        DataSet.addValue(300, "number", "apple");
        DataSet.addValue(400, "number", "barara");
        DataSet.addValue(250, "number", "pear");
        DataSet.addValue(330, "number", "milk");
        DataSet.addValue(420, "number", "cheese");
        //创建柱形图
        JFreeChart chart = ChartFactory.createBarChart3D("Catogram",
                "Fruit", "Sale", DataSet, PlotOrientation.VERTICAL,
                false, false, false);
        //用来放置图表
        ChartPanel panel = new ChartPanel(chart);
        JPanel jp = new JPanel();
        jp.add(panel, BorderLayout.CENTER);
        this.add(jp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 700, 500);
        this.setVisible(true);
    }
    public static void main(String [] args){
        new test();
    }
}