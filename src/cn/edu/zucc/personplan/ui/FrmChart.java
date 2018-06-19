package cn.edu.zucc.personplan.ui;
import java.awt.BorderLayout;

import java.awt.BorderLayout;

import cn.edu.zucc.personplan.control.ChartManager;
import cn.edu.zucc.personplan.model.BeanPlan;
import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import javax.swing.*;

public class FrmChart extends JFrame{
    public FrmChart(BeanPlan curPlan){
        DefaultPieDataset DataSet = new DefaultPieDataset();
        ChartManager chartManager = new ChartManager();
        DataSet.setValue("开始但未结束", chartManager.getNotFinishedCount(curPlan));
        DataSet.setValue("已结束", chartManager.getFinishedStepCount(curPlan));
        DataSet.setValue("未开始",chartManager.getBeginNotSetCount(curPlan));


        //创建饼图
        JFreeChart chart = ChartFactory.createPieChart3D(curPlan.getPlanName()+"计划统计",
                DataSet, true, true, false);
        //用来放置图表
        ChartPanel panel = new ChartPanel(chart);
        JPanel jp = new JPanel();
        jp.add(panel, BorderLayout.CENTER);
        this.add(jp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 700, 500);
        this.setVisible(true);
    }

}