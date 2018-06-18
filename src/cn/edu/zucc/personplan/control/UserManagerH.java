package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.itf.IUserManager;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.HBUtil;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import org.hibernate.Query;
import java.sql.*;

public class UserManagerH implements IUserManager {
    @Override
    public BeanUser reg(String userid, String pwd, String pwd2) throws BaseException{
        if(userid.length() == 0){
            throw new BaseException("用户名为空,请填入用户名");
        }

        if(pwd.length() == 0){
            throw new BaseException("密码为空,请输入密码");
        }

        if(pwd.length() <6){
            throw new BaseException("密码长度过短,请增加长度");
        }else if(pwd.length()>=20){
            throw new BaseException("密码长度过长,请减少长度");
        }

        if(!pwd.equals(pwd2)){
            throw new BaseException("两次密码输入不相同,请查证后重试");
        }



        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();

        String hql = "from BeanUser b where b.userId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id",userid);
        BeanUser t = (BeanUser) query.uniqueResult();
        if(t!=null){
            throw new BaseException("current userid is already in use");
        }


        BeanUser beanUser = new BeanUser();
        beanUser.setUserId(userid);
        beanUser.setUserPwd(pwd);
        beanUser.setRegisterTime(new Timestamp(System.currentTimeMillis()));
        session.save(beanUser);
        tx.commit();

        return beanUser;
    }

    @Override
    public BeanUser login(String userid, String pwd) throws BaseException{
        Session session = HBUtil.getSession();
        Query query = session.createQuery("from BeanUser b where b.userId = :id");
        query.setParameter("id",userid);
        BeanUser fromDb = (BeanUser) query.uniqueResult();
        if(!fromDb.getUserPwd().equals(pwd)){
            throw new BaseException("用户名密码不匹配");
        }

        return fromDb;
    }

    @Override
    public void changePwd(BeanUser user, String oldPwd, String newPwd, String newPwd2) throws BaseException{
        if(!newPwd.equals(newPwd2)){
            throw new BaseException("两次密码输入不一致,检查后重试");
        }
        if(newPwd.equals(user.getUserPwd())){
            throw new BaseException("新密码与原始密码一致");
        }

        if(user.getUserPwd()!=oldPwd){
            throw new BaseException("原始密码输入错误");
        }

        Session session = HBUtil.getSession();
        Transaction tx = session.beginTransaction();
        user.setUserPwd(newPwd);
        session.update(user);
        tx.commit();
    }

    public static void main(String[] args) {
        UserManagerH userManager = new UserManagerH();
        try{
            BeanUser b = new BeanUser();
            b.setUserId("admin1");
            b.setUserPwd("12345678");
            b.setRegisterTime(new Timestamp(System.currentTimeMillis()));
            userManager.changePwd(b,"12345678","123456789","123456789");
        }catch (BaseException e){
            e.printStackTrace();
        }
    }
}
