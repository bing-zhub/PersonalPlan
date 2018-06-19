package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChartManager {
    public int getFinishedStepCount (BeanPlan curPlan) {
        int result = 0;
        Connection connection = null;
        try{
            connection = DBUtil.getConnection();
            String sql = "SELECT count(step_id) FROM tbl_step WHERE plan_id = ? AND real_end_time is not NULL ";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1,curPlan.getPlanId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public int getNotFinishedCount (BeanPlan curPlan) {
        int result = 0;
        Connection connection = null;
        try{
            connection = DBUtil.getConnection();
            String sql = "SELECT count(step_id) FROM tbl_step WHERE plan_id = ? AND real_begin_time is not NULL AND real_end_time is NULL ";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1,curPlan.getPlanId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public int getBeginNotSetCount (BeanPlan curPlan) {
        int result = 0;
        Connection connection = null;
        try{
            connection = DBUtil.getConnection();
            String sql = "SELECT count(step_id) FROM tbl_step WHERE plan_id = ? AND real_begin_time is NULL and real_end_time is NULL ";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1,curPlan.getPlanId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public int getAllStepCount (BeanPlan curPlan) {
        int result = 0;
        Connection connection = null;
        try{
            connection = DBUtil.getConnection();
            String sql = "SELECT count(step_id) FROM tbl_step WHERE plan_id = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1,curPlan.getPlanId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        BeanPlan b = new BeanPlan();
        b.setPlanId(1);
        int re = new ChartManager().getBeginNotSetCount(b);
        System.out.println(re);
    }
}
