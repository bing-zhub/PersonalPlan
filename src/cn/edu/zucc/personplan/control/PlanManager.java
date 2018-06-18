package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.itf.*;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class PlanManager implements IPlanManager{
    @Override
    public BeanPlan addPlan(String name) throws BaseException{
        BeanPlan beanPlan = new BeanPlan();
        Connection conn = null;
        BeanUser beanUser = BeanUser.currentLoginUser;
        try{
            int n = 0;
            conn = DBUtil.getConnection();
            if(beanUser.getUserId().equals(null)){
                throw new BaseException("用户未登录");
            }
            PreparedStatement pst = conn.prepareStatement("SELECT max(plan_order) from tbl_plan WHERE user_id = ?");
            pst.setString(1,beanUser.getUserId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                n = rs.getInt(1);
                n+=1;
            }
            String sql = "INSERT INTO tbl_plan(user_id, plan_order, plan_name, create_time, step_count, start_step_count, finished_step_count) VALUES (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,beanUser.getUserId());
            pst.setInt(2,n);
            pst.setString(3,name);
            pst.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
            pst.setInt(5,0);
            pst.setInt(6,0);
            pst.setInt(7,0);
            pst.executeUpdate();
            pst = conn.prepareStatement("SELECT * FROM tbl_plan WHERE user_id =? and plan_order = ?");
            pst.setString(1,beanUser.getUserId());
            pst.setInt(2,n);
            rs = pst.executeQuery();
            if(rs.next()){
                beanPlan.setPlanId(rs.getInt(1));
                beanPlan.setUserId(rs.getString(2));
                beanPlan.setPlanOrder(rs.getInt(3));
                beanPlan.setPlanName(rs.getString(4));
                beanPlan.setCreateTime(rs.getTimestamp(5));
                beanPlan.setStepCount(rs.getInt(6));
                beanPlan.setStartStepCount(rs.getInt(7));
                beanPlan.setFinishedStepCount(rs.getInt(8));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return beanPlan;
    }

    @Override
    public void deletePlan(BeanPlan plan) throws BaseException{
        Connection conn = null;
        boolean haveStep = false;
        BeanUser beanUser = BeanUser.currentLoginUser;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbl_step WHERE plan_id=?");
            pst.setInt(1,plan.getPlanId());
            ResultSet rs = pst.executeQuery();
            int max = 0;
            if(rs.next()){
                throw new BaseException("该计划已存在步骤不可删除");
            }
            pst = conn.prepareStatement("DELETE FROM tbl_plan WHERE plan_id = ?");

            pst.setInt(1,plan.getPlanId());
            int n = 0;

            if(!haveStep){
                n = pst.executeUpdate();
            }
            System.out.println(n+"条计划被删除");

            int maxOrder = 0;
            pst = conn.prepareStatement("SELECT max(plan_order) FROM tbl_plan WHERE user_id = ?");
            pst.setString(1,beanUser.getUserId());
            rs = pst.executeQuery();
            if(rs.next()){
                maxOrder = rs.getInt(1);
            }
            if(plan.getPlanOrder()!=maxOrder){
                pst = conn.prepareStatement("UPDATE tbl_plan SET plan_order = plan_order -1 WHERE user_id = ? AND plan_order > ?");
                pst.setString(1,beanUser.getUserId());
                pst.setInt(2,plan.getPlanOrder()-1);
                rs = pst.executeQuery();
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<BeanPlan> loadAll() {
        List<BeanPlan> result = new ArrayList<BeanPlan>();
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbl_plan WHERE user_id = ?");
            pst.setString(1,BeanUser.currentLoginUser.getUserId());
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                BeanPlan beanPlan = new BeanPlan();
                beanPlan.setPlanId(rs.getInt(1));
                beanPlan.setUserId(rs.getString(2));
                beanPlan.setPlanOrder(rs.getInt(3));
                beanPlan.setPlanName(rs.getString(4));
                beanPlan.setCreateTime(rs.getTimestamp(5));
                beanPlan.setStepCount(rs.getInt(6));
                beanPlan.setStartStepCount(rs.getInt(7));
                beanPlan.setFinishedStepCount(rs.getInt(8));
                result.add(beanPlan);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        PlanManager planManager = new PlanManager();
        List<BeanPlan> list = planManager.loadAll();
        for (BeanPlan p:list) {
            System.out.println(p.getPlanId());
        }
    }
}
