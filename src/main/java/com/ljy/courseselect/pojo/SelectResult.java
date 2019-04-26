package com.ljy.courseselect.pojo;

import java.util.ArrayList;
import java.util.List;

public class SelectResult {
    private int conflict;
    private boolean tooManyPlans,tooManyConflicts;
    private List<SelectPlan>plans;

    public SelectResult(){
        conflict=0;
        tooManyPlans=false;
        tooManyConflicts=false;
        plans=new ArrayList<>();
    }
    public int getConflict() {
        return conflict;
    }

    public void setConflict(int conflict) {
        this.conflict = conflict;
    }

    public boolean isTooManyPlans() {
        return tooManyPlans;
    }

    public void setTooManyPlans(boolean tooManyPlans) {
        this.tooManyPlans = tooManyPlans;
    }

    public boolean isTooManyConflicts() {
        return tooManyConflicts;
    }

    public void setTooManyConflicts(boolean tooManyConflicts) {
        this.tooManyConflicts = tooManyConflicts;
    }

    public List<SelectPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<SelectPlan> plans) {
        this.plans = plans;
    }
    public void addNewPlan(SelectPlan newPlan){
        this.plans.add(newPlan);
    }
}
