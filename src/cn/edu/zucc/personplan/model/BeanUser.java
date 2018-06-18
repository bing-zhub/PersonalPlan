package cn.edu.zucc.personplan.model;

public class BeanUser {
	public static BeanUser currentLoginUser=null;
	private String userId;
	private String userPwd;
	private java.sql.Timestamp registerTime;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}


	public java.sql.Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(java.sql.Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public BeanUser userForDebug(BeanUser b){
		b.setUserId("admin");
		b.setUserPwd("admin");
		return b;
	}
}
