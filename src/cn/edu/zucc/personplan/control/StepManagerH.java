package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.itf.*;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanStep;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.HBUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class StepManagerH implements IStepManager{
    @Override
    public void add(BeanPlan plan, String name, String planstartdate, String planfinishdate) throws BaseException{
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String hql = "select max(b.stepOrder) from BeanStep b";
        Query query = session.createQuery(hql);
        int maxOrder = (int)query.uniqueResult();
        BeanStep beanStep = new BeanStep();
        beanStep.setPlanId(plan.getPlanId());
        beanStep.setStepOrder(maxOrder+1);
        beanStep.setStepName(name);
        try{
            beanStep.setPlanBeginTime(new Timestamp(sdf.parse(planstartdate).getTime()));
            beanStep.setPlanEndTime(new Timestamp(sdf.parse(planfinishdate).getTime()));
        }catch (ParseException e){
            throw new BaseException("时间格式输入错误, 请按照yyyy-mm-dd输入");
        }
        session.save(beanStep);
        tx.commit();

    }

    @Override
    public List<BeanStep> loadSteps(BeanPlan plan) {
        Session session = HBUtil.getSession();
        List<BeanStep> result = new ArrayList<BeanStep>();
        String hql = "from BeanStep b where b.planId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id",plan.getPlanId());
        result = (List<BeanStep>)query.list();
        session.close();
        return result;
    }

    @Override
    public void deleteStep(BeanStep step) {
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        session.delete(step);
        tx.commit();
        session.close();
    }

    @Override
    public void startStep(BeanStep step) {
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        BeanStep beanStep = session.get(BeanStep.class,step.getStepId());
        beanStep.setRealBeginTime(new Timestamp(System.currentTimeMillis()));
        session.update(beanStep);
        tx.commit();
        session.close();
    }

    @Override
    public void finishStep(BeanStep step) {
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        BeanStep beanStep = session.get(BeanStep.class,step.getStepId());
        beanStep.setRealEndTime(new Timestamp(System.currentTimeMillis()));
        session.update(beanStep);
        tx.commit();
        session.close();
    }

    @Override
    public void moveUp(BeanStep step) {
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        int selectedOrder = step.getStepOrder();
        String hql = "from BeanStep b where b.stepOrder = :order and b.planId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("order",selectedOrder-1);
        query.setParameter("id",step.getPlanId());
        BeanStep swapStep = (BeanStep) query.uniqueResult();
        swapStep.setStepOrder(selectedOrder);
        step.setStepOrder(selectedOrder-1);
        session.update(step);
        tx.commit();
        session.close();
    }

    @Override
    public void moveDown(BeanStep step) {
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        int selectedOrder = step.getStepOrder();
        String hql = "from BeanStep b where b.stepOrder = :order and b.planId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("order",selectedOrder+1);
        query.setParameter("id",step.getPlanId());
        BeanStep swapStep = (BeanStep) query.uniqueResult();
        swapStep.setStepOrder(selectedOrder);
        step.setStepOrder(selectedOrder+1);
        session.update(step);
        tx.commit();
        session.close();
    }

    public static void main(String[] args) {
        BeanPlan b = new BeanPlan();
        b.setPlanId(1);
        StepManagerH stepManagerH = new StepManagerH();
        List<BeanStep> result = stepManagerH.loadSteps(b);
        BeanStep s = new BeanStep();
        for (BeanStep bs : result){
            s = bs;
        }
        System.out.println(s.getStepId());
        stepManagerH.moveUp(s);
    }
}
