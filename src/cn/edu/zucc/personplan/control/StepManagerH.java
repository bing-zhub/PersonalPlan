package cn.edu.zucc.personplan.control;

import cn.edu.zucc.personplan.itf.*;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanStep;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class StepManagerH implements IStepManager{
    @Override
    public void add(BeanPlan plan, String name, String planstartdate, String planfinishdate) {

    }

    @Override
    public List<BeanStep> loadSteps(BeanPlan plan) {
        return null;
    }

    @Override
    public void deleteStep(BeanStep step) {

    }

    @Override
    public void startStep(BeanStep step) {

    }

    @Override
    public void finishStep(BeanStep step) {

    }

    @Override
    public void moveUp(BeanStep step) {

    }

    @Override
    public void moveDown(BeanStep step) {

    }
}
