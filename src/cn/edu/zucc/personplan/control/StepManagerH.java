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
        return null;
    }

    @Override
    public void deleteStep(BeanStep step) {

    }

    @Override
    public void startStep(BeanStep step) {

    }

    @Override
    public void finishStep(BeanStep step) {

    }

    @Override
    public void moveUp(BeanStep step) {

    }

    @Override
    public void moveDown(BeanStep step) {

    }
}
