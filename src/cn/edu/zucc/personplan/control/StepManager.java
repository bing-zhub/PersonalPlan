package cn.edu.zucc.personplan.control;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.personplan.itf.IStepManager;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanStep;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.hql.internal.ast.SqlASTFactory;

public class StepManager implements IStepManager {

    @Override
    public void add(BeanPlan plan, String name, String planstartdate, String planfinishdate) throws BaseException {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            int maxOrder = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbl_plan where plan_id =?");
            pst.setInt(1,plan.getPlanId());
            ResultSet rs = pst.executeQuery();

            if(!rs.next()){
                throw new BaseException("id对应plan不存在");
            }else{
                pst = conn.prepareStatement("SELECT max(step_order) FROM tbl_step WHERE plan_id = ?");
                pst.setInt(1,plan.getPlanId());
                rs = pst.executeQuery();
                if(rs.next()){
                    maxOrder = rs.getInt(1);
                }
            }

            pst= conn.prepareStatement("INSERT INTO tbl_step(plan_id, step_order, step_name, plan_begin_time, plan_end_time) VALUES (?,?,?,?,?)");
            pst.setInt(1,plan.getPlanId());
            pst.setInt(2,maxOrder+1);
            pst.setString(3,name);
            pst.setTimestamp(4,new Timestamp(sdf.parse(planfinishdate).getTime()));
            pst.setTimestamp(5,new Timestamp(sdf.parse(planfinishdate).getTime()));
            pst.executeUpdate();

//            pst = conn.prepareStatement("UPDATE tbl_plan SET step_count = step_count + 1 WHERE plan_id = ?");
//            pst.setInt(1,plan.getPlanId());
//            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
            throw new BaseException("时间输入错误, 请按照yyyy-mm-dd输入");
        }

    }

    @Override
    public List<BeanStep> loadSteps(BeanPlan plan) throws BaseException {
        List<BeanStep> result=new ArrayList<BeanStep>();
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbl_step WHERE plan_id =? ORDER BY step_order");
            pst.setInt(1,plan.getPlanId());
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                BeanStep s = new BeanStep();
                s.setStepId(rs.getInt(1));
                s.setPlanId(rs.getInt(2));
                s.setStepOrder(rs.getInt(3));
                s.setStepName(rs.getString(4));
                s.setPlanBeginTime(rs.getTimestamp(5));
                s.setPlanEndTime(rs.getTimestamp(6));
                s.setRealBeginTime(rs.getTimestamp(7));
                s.setRealEndTime(rs.getTimestamp(8));
                result.add(s);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteStep(BeanStep step) throws BaseException {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM tbl_step where step_id =?");
            pst.setInt(1,step.getStepId());
            int n = pst.executeUpdate();
            int max = 0;
            pst = conn.prepareStatement("SELECT max(step_order) FROM tbl_step WHERE plan_id = ?");
            pst.setInt(1, step.getPlanId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                max = rs.getInt(1);
            }

            if(step.getStepOrder()!=max){
                pst = conn.prepareStatement("UPDATE tbl_step SET step_order = step_order - 1 WHERE plan_id = ? AND step_order > ?");
                pst.setInt(1,step.getPlanId());
                pst.setInt(2,step.getStepOrder());
                pst.executeUpdate();
            }

            pst = conn.prepareStatement("UPDATE tbl_plan SET step_count = step_count - 1 WHERE plan_id = ?");
            pst.setInt(1,step.getPlanId());
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void startStep(BeanStep step) throws BaseException {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("UPDATE tbl_step SET real_begin_time = ? WHERE step_id = ?");
            pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            pst.setInt(2,step.getStepId());
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void finishStep(BeanStep step) throws BaseException {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("UPDATE tbl_step SET real_end_time = ? WHERE step_id = ?");
            pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            pst.setInt(2,step.getStepId());
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void moveUp(BeanStep step) throws BaseException {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst;

            if(1==step.getStepOrder()){
                throw new BaseException("已是第一个步骤不可前移");
            }

            pst = conn.prepareStatement("UPDATE tbl_step SET step_order = ? WHERE step_id = ? ");
            pst.setInt(1,step.getStepOrder()-1);
            pst.setInt(2,step.getStepId());
            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE tbl_step SET step_order = ? WHERE step_id != ? AND step_order = ?");
            pst.setInt(1,step.getStepOrder());
            pst.setInt(2,step.getStepId());
            pst.setInt(3,step.getStepOrder()-1);
            pst.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void moveDown(BeanStep step) throws BaseException {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst;

            pst = conn.prepareStatement("SELECT max(step_order) FROM tbl_step WHERE step_id = ?");
            pst.setInt(1,step.getPlanId());
            int last = pst.executeQuery().getInt(1);
            if(last==step.getStepOrder()){
                throw new BaseException("已是最后后一个步骤不可后移");
            }

            pst = conn.prepareStatement("UPDATE tbl_step SET step_order = ? WHERE step_id = ? ");
            pst.setInt(1,step.getStepOrder()+1);
            pst.setInt(2,step.getStepId());
            pst.executeUpdate();

            pst = conn.prepareStatement("UPDATE tbl_step SET step_order = ? WHERE step_id != ? AND step_order = ?");
            pst.setInt(1,step.getStepOrder());
            pst.setInt(2,step.getStepId());
            pst.setInt(3,step.getStepOrder()+1);
            pst.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        StepManager stepManager = new StepManager();
//        BeanPlan beanPlan = new BeanPlan();
//        beanPlan.setPlanId(1);
//        try{
//            List<BeanStep> list = stepManager.loadSteps(beanPlan);
//            for(BeanStep b:list){
//                System.out.println(b.getStepId());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        BeanPlan beanPlan = new BeanPlan();
        beanPlan.setPlanId(1);
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        try{
            stepManager.add(beanPlan,"step1-2","2018-3-1","2018-3-2");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
