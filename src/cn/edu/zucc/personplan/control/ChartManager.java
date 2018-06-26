package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String,Float> getAllPlanRate () {
        Connection connection = null;
        Map<String,Float> result = new HashMap<String,Float>();
        try{
            connection = DBUtil.getConnection();
            String sql = "SELECT plan_name,finished_step_count/step_count FROM tbl_plan";
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                result.put(rs.getString(1),rs.getFloat(2));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public Map<String,Integer> getAllCount () {
        Connection connection = null;
        Map<String,Integer> result = new HashMap<String,Integer>();
        try{
            connection = DBUtil.getConnection();
            String sql = "SELECT plan_name,step_count FROM tbl_plan";
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                result.put(rs.getString(1),rs.getInt(2));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String,Float> result = new ChartManager().getAllPlanRate();
        for (Map.Entry<String,Float> entry:result.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
}
