package cn.edu.zucc.personplan.ui;

import cn.edu.zucc.personplan.control.AdminManager;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FrmUserManage extends JDialog implements ActionListener {
    private JPanel toolBar = new JPanel();
    private JPanel workPane = new JPanel();
    private JList<String> list = new JList<>();
    private AdminManager adminManager = new AdminManager();
    private Button btnRstPwd = new Button("重置密码");
    private Button btnDelUsr = new Button("删除用户");
    private Button btnRecover = new Button("恢复用户");
    private Button btnSetAdmin = new Button("设为管理员");
    private Button btnResetAdmin = new Button("取消管理员");
    private Button btnCancel = new Button("取消");

    public FrmUserManage(Frame f, String s, boolean b) {
        super(f, s, b);
        toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.getContentPane().add(workPane, BorderLayout.CENTER);
        this.setSize(150, 430);
        list.setPreferredSize(new Dimension(150, 200));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setListData(adminManager.loadAllUsrs());
        list.setSelectedIndex(1);
        workPane.add(list);
        workPane.add(btnDelUsr);
        workPane.add(btnRstPwd);
        workPane.add(btnRecover);
        workPane.add(btnSetAdmin);
        workPane.add(btnResetAdmin);
        workPane.add(btnCancel);
        this.btnRstPwd.addActionListener(this);
        this.btnCancel.addActionListener(this);
        this.btnDelUsr.addActionListener(this);
        this.btnRecover.addActionListener(this);
        this.btnSetAdmin.addActionListener(this);
        this.btnResetAdmin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] indices = list.getSelectedIndices();
        ListModel<String> listModel = list.getModel();
        if(e.getSource()==this.btnCancel)
            this.setVisible(false);
        else if (e.getSource()==this.btnRstPwd){
            String temp = "";
            for (int i: indices){
                temp = temp + listModel.getElementAt(i)+" ";
            }
            JOptionPane.showConfirmDialog(null,"是否要重置用户"+temp+"的密码?");
            for (int index : indices) {
                System.out.println(listModel.getElementAt(index));
                adminManager.resetPwd(listModel.getElementAt(index));
            }
        }else if(e.getSource()==this.btnDelUsr){
            String temp = "";
            for (int i: indices){
                temp = temp + listModel.getElementAt(i)+" ";
            }
            JOptionPane.showConfirmDialog(null,"是否要删除用户"+temp);
            try {
                for (int index: indices) {
                    adminManager.deleteUsr(listModel.getElementAt(index));
                }
            }catch (BaseException err) {
                JOptionPane.showMessageDialog(null, err.getMessage(), "错误",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }else if(e.getSource()==this.btnRecover){
            for (int i : indices){
                try{
                    adminManager.recUsr(listModel.getElementAt(i));
                }catch (BaseException err){
                    JOptionPane.showMessageDialog(null, "用户"+listModel.getElementAt(i)+err.getMessage(), "错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(e.getSource()==this.btnSetAdmin){
            String temp = "";
            for (int i: indices){
                temp = temp + listModel.getElementAt(i)+" ";
            }
            JOptionPane.showConfirmDialog(null,"是否将用户"+temp+"设置为管理员?");
            for (int i : indices){
                try{
                    adminManager.setAdmin(listModel.getElementAt(i));
                }catch (BaseException err){
                    JOptionPane.showMessageDialog(null, "用户"+listModel.getElementAt(i)+err.getMessage(), "错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(e.getSource()==this.btnResetAdmin){
            String temp = "";
            for (int i: indices){
                temp = temp + listModel.getElementAt(i)+" ";
            }
            JOptionPane.showConfirmDialog(null,"是否将用户"+temp+"取消管理员?");
            for (int i : indices){
                try{
                    adminManager.resetAdmin(listModel.getElementAt(i));
                }catch (BaseException err){
                    JOptionPane.showMessageDialog(null, "用户"+listModel.getElementAt(i)+err.getMessage(), "错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
