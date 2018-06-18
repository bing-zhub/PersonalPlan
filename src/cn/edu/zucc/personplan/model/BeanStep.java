package cn.edu.zucc.personplan.model;

import java.sql.Timestamp;

public class BeanStep {
	private int stepId;
	private int planId;
	private int stepOrder;
	private String stepName;
	private Timestamp planBeginTime;
	private Timestamp planEndTime;
	private Timestamp realBeginTime;
	private Timestamp realEndTime;
	public static final String[] tblStepTitle={"序号","名称","计划开始时间","计划完成时间","实际开始时间","实际完成时间"};

	public String getCell(int col){
		if(col==0) return Integer.toString(this.stepOrder);
		else if(col==1) return this.stepName;
		else if(col==2) return this.planBeginTime.toString();
		else if(col==3) return this.planEndTime.toString();
		else if(col==4)
			if(realBeginTime!=null)
				return this.realBeginTime.toString();
			else
				return "未设置";
		else if(col==5)
			if(realEndTime!=null)
				return this.realEndTime.toString();
			else
				return "未设置";
		else return "";
	}


	public int getStepId() {
		return stepId;
	}

	public void setStepId(int stepId) {
		this.stepId = stepId;
	}


	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}


	public int getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(int stepOrder) {
		this.stepOrder = stepOrder;
	}


	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}


	public Timestamp getPlanBeginTime() {
		return planBeginTime;
	}

	public void setPlanBeginTime(Timestamp planBeginTime) {
		this.planBeginTime = planBeginTime;
	}


	public Timestamp getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Timestamp planEndTime) {
		this.planEndTime = planEndTime;
	}


	public Timestamp getRealBeginTime() {
		return realBeginTime;
	}

	public void setRealBeginTime(Timestamp realBeginTime) {
		this.realBeginTime = realBeginTime;
	}


	public Timestamp getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(Timestamp realEndTime) {
		this.realEndTime = realEndTime;
	}
	
}
