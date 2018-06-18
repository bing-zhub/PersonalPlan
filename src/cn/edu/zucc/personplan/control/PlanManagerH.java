package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.itf.*;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanStep;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.HBUtil;
import com.sun.xml.internal.rngom.parse.host.Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.Timestamp;
import org.hibernate.*;

public class PlanManagerH implements IPlanManager{
    @Override
    public BeanPlan addPlan(String name) throws BaseException{
        Session session = HBUtil.getSession();
        BeanPlan beanPlan = new BeanPlan();
        BeanUser currentUser = BeanUser.currentLoginUser;


//        //for dev
//        BeanUser currentUser = new BeanUser();
//        currentUser.setUserPwd("admin");
//        currentUser.setUserId("admin");

        if(currentUser.equals(null)){
            throw new BaseException("用户未登录");
        }
        Transaction tx = session.beginTransaction();
        String hql = "select max(b.planOrder) from BeanPlan b where b.userId = :userId";
        Query query = session.createQuery(hql);
        query.setString("userId",currentUser.getUserId());
        int max = (int)query.uniqueResult();

        beanPlan.setUserId(currentUser.getUserId());
        beanPlan.setPlanOrder(max+1);
        beanPlan.setPlanName(name);
        beanPlan.setCreateTime(new Timestamp(System.currentTimeMillis()));
        beanPlan.setStepCount(0);
        beanPlan.setStartStepCount(0);
        beanPlan.setFinishedStepCount(0);
        session.save(beanPlan);

        hql = "from BeanPlan b where b.userId = :id and b.planOrder = :order";
        query = session.createQuery(hql);
        query.setString("id",currentUser.getUserId());
        query.setInteger("order",max+1);
        beanPlan = (BeanPlan) query.uniqueResult();
        tx.commit();
        session.close();

        return beanPlan;
    }

    @Override
    public void deletePlan(BeanPlan plan) throws BaseException{
        Boolean haveStep = true;
//        BeanUser currentUser = BeanUser.currentLoginUser;


        //for dev
        BeanUser currentUser = new BeanUser();
        currentUser.setUserPwd("admin");
        currentUser.setUserId("admin");
        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "from BeanStep b where b.planId = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",plan.getPlanId());
        List<BeanStep> list = query.list();
        if(list.size()==0){
            haveStep = false;
        }else{
            throw new BaseException("该计划已存在步骤不可删除");
        }

        hql = "select max(b.planOrder) from BeanPlan b";
        query = session.createQuery(hql);
        int max = (int)query.uniqueResult();

        hql = "delete BeanPlan b where b.planId=:id";
        query = session.createQuery(hql);
        query.setInteger("id",plan.getPlanId());
        query.executeUpdate();

        if(plan.getPlanOrder() != max) {
            System.out.println("here");
            hql = "update BeanPlan b set b.planOrder = b.planOrder - 1 where b.planOrder <= :max and b.userId = :id and b.planOrder > :planOrder";
            query = session.createQuery(hql);
            query.setParameter("max",max);
            query.setParameter("id",currentUser.getUserId());
            query.setParameter("planOrder",plan.getPlanOrder());
            query.executeUpdate();
        }
        tx.commit();
    }

    @Override
    public List<BeanPlan> loadAll() {
        List<BeanPlan> result = new LinkedList<BeanPlan>();
        Session session = HBUtil.getSession();
        String hql = "from BeanPlan";
        Query query = session.createQuery(hql);
        result = (List<BeanPlan>) query.list();

        return result;
    }

    public static void main(String[] args) {
        PlanManagerH planManager = new PlanManagerH();
        try {
//            BeanPlan B = new BeanPlan();
//            B.setPlanId(3);
//            B.setPlanOrder(2);
//           BeanPlan B =  planManager.addPlan("plantest");
//            planManager.deletePlan(B);
//            System.out.println(B.getPlanId());
            List<BeanPlan> list = planManager.loadAll();
            for (BeanPlan b: list){
                System.out.println(b.getPlanId());
            }

            return;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}