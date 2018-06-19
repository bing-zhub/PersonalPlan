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
        DataSet.setValue("��ʼ��δ����", chartManager.getNotFinishedCount(curPlan));
        DataSet.setValue("�ѽ���", chartManager.getFinishedStepCount(curPlan));
        DataSet.setValue("δ��ʼ",chartManager.getBeginNotSetCount(curPlan));


        //������ͼ
        JFreeChart chart = ChartFactory.createPieChart3D(curPlan.getPlanName()+"�ƻ�ͳ��",
                DataSet, true, true, false);
        //��������ͼ��
        ChartPanel panel = new ChartPanel(chart);
        JPanel jp = new JPanel();
        jp.add(panel, BorderLayout.CENTER);
        this.add(jp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 700, 500);
        this.setVisible(true);
    }

}