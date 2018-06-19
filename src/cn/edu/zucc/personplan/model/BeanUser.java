package cn.edu.zucc.personplan.model;

public class BeanUser {
	public static BeanUser currentLoginUser=null;
	private String userId;
	private String userPwd;
	private java.sql.Timestamp registerTime;
	private boolean isValid = true;
	private boolean isAdmin = false;

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

	public void setValid(boolean isValid){
		this.isValid = isValid;
	}

	public boolean getValid() {
		return isValid;
	}

	public void setAdmin(boolean isAdmin){
		this.isAdmin = isAdmin;
	}

	public boolean getAdmin() {
		return isAdmin;
	}


}
