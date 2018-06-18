package cn.edu.zucc.personplan.model;

public class BeanPlan {
	private int planId;
	private String userId;
	private int planOrder;
	private String planName;
	private java.sql.Timestamp createTime;
	private int stepCount;
	private int startStepCount;
	private int finishedStepCount;
	public static final String[] tableTitles={"序号","名称","步骤数","已完成数"};


	public String getCell(int col){
		if(col==0) return Integer.toString(this.planOrder);
		else if(col==1) return this.planName;
		else if(col==2) return Integer.toString(this.startStepCount);
		else if(col==3) return Integer.toString(this.finishedStepCount);
		else return "";
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public int getPlanOrder() {
		return planOrder;
	}

	public void setPlanOrder(int planOrder) {
		this.planOrder = planOrder;
	}


	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}


	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}


	public int getStepCount() {
		return stepCount;
	}

	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}


	public int getStartStepCount() {
		return startStepCount;
	}

	public void setStartStepCount(int startStepCount) {
		this.startStepCount = startStepCount;
	}


	public int getFinishedStepCount() {
		return finishedStepCount;
	}

	public void setFinishedStepCount(int finishedStepCount) {
		this.finishedStepCount = finishedStepCount;
	}
}
