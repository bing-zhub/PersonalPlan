package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.itf.IUserManager;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager implements IUserManager {

    @Override
    public BeanUser reg(String userid, String pwd,String pwd2) throws BaseException {
        // TODO Auto-generated method stub
        if(userid.length() == 0){
            throw new BaseException("�û���Ϊ��,�������û���");
        }

        if(pwd.length() == 0){
            throw new BaseException("����Ϊ��,����������");
        }

        if(pwd.length() <6){
            throw new BaseException("���볤�ȹ���,�����ӳ���");
        }else if(pwd.length()>=20){
            throw new BaseException("���볤�ȹ���,����ٳ���");
        }

        if(!pwd.equals(pwd2)){
            throw new BaseException("�����������벻��ͬ,���֤������");
        }

        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO tbl_user VALUE (?,?,?)");
            pst.setString(1,userid);
            pst.setString(2,pwd);
            pst.setTimestamp(3,new java.sql.Timestamp(System.currentTimeMillis()));
            int n = pst.executeUpdate();
            System.out.println(n+"���û����ע��");

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public BeanUser login(String userid, String pwd) throws BaseException {
        BeanUser beanUser = new BeanUser();
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            // ��ѯ�û��Ƿ����
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbl_user WHERE user_id = ?");
            pst.setString(1,userid);
            ResultSet rs = pst.executeQuery();
            if(!rs.next())
                throw new BaseException("�˻�������,�봴�����ٴε�¼");

            //��ѯ���� ��֤�˻������Ƿ�ƥ��
            conn.prepareStatement("SELECT user_id,user_pwd FROM tbl_user WHERE user_id = ?");
            pst.setString(1,userid);
            rs = pst.executeQuery();

            if(rs.next()){
                String pwdC = rs.getString(2);
                if(!pwdC.equals(pwd)){
                    throw new BaseException("�˺����벻ƥ��");
                }
                boolean isValid = rs.getBoolean(4);
                if(!isValid){
                    throw new BaseException("��ǰ�˻���Ч");
                }
            }

            beanUser.setUserPwd(pwd);
            beanUser.setUserId(userid);

        }catch(SQLException e){
            e.printStackTrace();
        }
        return beanUser;
    }


    @Override
    public void changePwd(BeanUser user, String oldPwd, String newPwd, String newPwd2) throws BaseException {
        // TODO Auto-generated method stub
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            if(!newPwd.equals(newPwd2)){
                throw new BaseException("�����������벻һ��,��������");
            }

            PreparedStatement pst = conn.prepareStatement("SELECT user_pwd from tbl_user WHERE user_id = ?");
            pst.setString(1,user.getUserId());
            ResultSet rs = pst.executeQuery();
            String pwdQ = rs.next()?rs.getString(1):"";
            if(pwdQ.equals(oldPwd)){
                pst = conn.prepareStatement("UPDATE tbl_user SET user_pwd = ? WHERE user_id = ?");
                pst.setString(1,newPwd);
                pst.setString(2,user.getUserId());
                int n = pst.executeUpdate();
                System.out.println("������"+n+"����Ϣ");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        try{

//            userManager.reg("amdin","123456","123456");
            BeanUser beanUser = new BeanUser();
            beanUser = userManager.login("admin","admin1");
            System.out.println(beanUser.getUserId());
            System.out.println(beanUser.getUserPwd());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
