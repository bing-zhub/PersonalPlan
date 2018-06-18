package cn.edu.zucc.personplan.util;

import java.util.List;

import cn.edu.zucc.personplan.model.BeanPlan;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class HBUtil {
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    public static Session getSession(){
        Session session = sessionFactory.openSession();
        return session;
    }
    public static void main(String[] args) {
        try {
            Session session=HBUtil.getSession();
            String hql="from BeanPlan";
            Query qry=session.createQuery(hql);
            List<BeanPlan> list = qry.list();
            for (BeanPlan b:list){
                System.out.println(b.getPlanId());
            }
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
