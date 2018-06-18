package cn.edu.zucc.personplan;

import cn.edu.zucc.personplan.control.PlanManager;
import cn.edu.zucc.personplan.control.StepManager;
import cn.edu.zucc.personplan.control.UserManager;
import cn.edu.zucc.personplan.itf.IPlanManager;
import cn.edu.zucc.personplan.itf.IStepManager;
import cn.edu.zucc.personplan.itf.IUserManager;

public class PersonPlanUtil {
	public static IPlanManager planManager=new PlanManager();//需要换成自行设计的实现类
	public static IStepManager stepManager=new StepManager();//需要换成自行设计的实现类
	public static IUserManager userManager=new UserManager();//需要换成自行设计的实现类
	
}
