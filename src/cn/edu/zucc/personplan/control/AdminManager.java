package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import com.sun.xml.internal.rngom.parse.host.Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminManager {

    public boolean isAdmin(){
        Connection conn = null;
        boolean is =false;
        try{
            conn = DBUtil.getConnection();
            String sql = "select is_admin from tbl_user where user_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,BeanUser.currentLoginUser.getUserId());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getBoolean(1)==true){
                    is = true;
                }else{
                    is = false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return is;
    }

    public String[] loadAllUsrs() {
        String[] result = new String[50];
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "SELECT * from tbl_user";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int i = 0;
            while(rs.next()){
                result[i] = rs.getString(1);
                i++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public void resetPwd(String userId) {
        Connection conn = null;
        String defaultPwd = "password";
        try{
            conn = DBUtil.getConnection();
            String sql = "UPDATE tbl_user SET user_pwd = ? WHERE user_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1,defaultPwd);
            pst.setString(2,userId);
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteUsr(String userId) throws BaseException{
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            String sql;
            PreparedStatement pst;
            ResultSet rs;

            if(userId == BeanUser.currentLoginUser.getUserId()){
                throw new BaseException("当前用户不可删除");
            }

            sql = "SELECT * FROM tbl_plan WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            rs = pst.executeQuery();
            if(rs.next()){
                throw new BaseException("该用户已经创建计划,不可删除,删除计划后继续");
            }

            sql = "SELECT * FROM tbl_user WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getBoolean(4)==false){
                    throw new BaseException("都已经被删了,你还要他怎样");
                }

                if(rs.getBoolean(5)==true){
                    throw new BaseException("目前还是管理员,不能删除");
                }
            }

            sql = "SELECT count(user_id) FROM tbl_user WHERE is_valid = TRUE ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getInt(1)==1){
                    throw new BaseException("别再删了,至少留一个用户吧");
                }
            }

            sql = "UPDATE tbl_user SET is_valid = FALSE WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void recUsr(String userId) throws BaseException{
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            String sql;
            PreparedStatement pst;
            ResultSet rs;

            sql = "SELECT * FROM tbl_user WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getBoolean(4)==true){
                    throw new BaseException("未删除,无需恢复");
                }
            }

            sql = "UPDATE tbl_user SET is_valid = TRUE WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setAdmin(String userId) throws BaseException{
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            String sql;
            PreparedStatement pst;
            ResultSet rs;

            sql = "SELECT * FROM tbl_user WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getBoolean(5)==true){
                    throw new BaseException("早就是管理员,瞎设置什么");
                }
                if(rs.getBoolean(4)==false){
                    throw new BaseException("不可用,恢复用户后重试");
                }
            }

            sql = "UPDATE tbl_user SET is_admin = TRUE WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void resetAdmin(String userId) throws BaseException{
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            String sql;
            PreparedStatement pst;
            ResultSet rs;

            sql = "SELECT * FROM tbl_user WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getBoolean(5)==false){
                    throw new BaseException("本来不是管理员,瞎设置什么");
                }
            }

            sql = "UPDATE tbl_user SET is_admin = FALSE WHERE user_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,userId);
            pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
