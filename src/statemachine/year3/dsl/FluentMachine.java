package statemachine.year3.dsl;

import java.util.ArrayList;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public abstract class FluentMachine extends Machine {

    private enum Effect { SET, CHANGE }
    private enum Condition { EQUAL, GREATER }
    
    private List<State> allStates = new ArrayList<State>();
    private State currentState;
    private String pendingEvent, targetTransition;
    private Effect effectMaybe;
    private int effectArgument;
    private IntegerState effectVariable;
    
    public FluentMachine() {
        build();
        flushTransition(null,null,0);
        allStates.add(currentState);
    }
    
    @Override
    protected List<State> getAllStates() {
        return allStates;
    }

    protected abstract void build();
    
    public FluentMachine state(String name) {
        if(currentState!=null) {
            flushTransition(null,null,0);
            allStates.add(currentState);
        }
        currentState = new State(this,name);
        return this;
    }
    
    public FluentMachine transition(String event) {
        if(targetTransition!=null || effectMaybe!=null) flushTransition(null,null,0);
        pendingEvent = event;
        return this;
    }
    
    public FluentMachine to(String state) {
        targetTransition = state;
        return this;
        
    }
    
    public FluentMachine setState(IntegerState variable, int value) {
        effectMaybe = Effect.SET;
        effectVariable = variable;
        effectArgument = value;
        return this;
    }
    
    public FluentMachine changeState(IntegerState variable, int value) {
        effectMaybe = Effect.CHANGE;
        effectVariable = variable;
        effectArgument = value;
        return this;
    }
    
    public FluentMachine whenStateEquals(IntegerState variable, int value) {
        flushTransition(Condition.EQUAL,variable,value);
        return this;
    }
    
    public FluentMachine whenStateGreaterThan(IntegerState variable, int value) {
        flushTransition(Condition.GREATER,variable,value);
        return this;
    }
    
    public FluentMachine otherwise() {
        flushTransition(null,null,0);
        return this;
    }

    private void flushTransition(Condition cond, IntegerState condVariableMaybe, int value) {
        if(pendingEvent==null) return;
        if(targetTransition==null && effectMaybe==null) return; // empty transition
        Transition transition = new GenericTransition(targetTransition,
                effectMaybe,effectVariable,effectArgument,
                cond, condVariableMaybe,value);
        currentState.addTransition(pendingEvent, transition);
        System.out.println(currentState.getName()+" # "+pendingEvent+" : "+transition);
        effectMaybe = null;
        effectVariable = null;
        targetTransition = null;
    }

    private static class GenericTransition extends Transition {

        private Effect effectMaybe;
        private IntegerState condVariableMaybe;
        private IntegerState effectVariable;
        private int effectArgument;
        private int condValue;
        private Condition conditionMaybe;

        public GenericTransition(String target, 
                Effect effectMaybe, IntegerState effectVariable, int effectArgument, 
                Condition cond, IntegerState condVariableMaybe, int value) {
            super(target);
            this.effectMaybe = effectMaybe; this.effectVariable = effectVariable; this.effectArgument = effectArgument; 
            this.conditionMaybe = cond; this.condVariableMaybe = condVariableMaybe; this.condValue = value;
            if(effectMaybe!=null && effectVariable==null) throw new Error("Inconistent effect description");
        }
        
        @Override public boolean isApplicable() {
            if(conditionMaybe==null) return true; // no condition
            if(conditionMaybe==Condition.EQUAL) {
                return condVariableMaybe.value()==condValue;
            } else if(conditionMaybe==Condition.GREATER) {
                return condVariableMaybe.value()>condValue;
            } else 
                throw new Error("Illegal condition kind");
        }
        
        @Override public void effect() {
            if(effectMaybe==null) return; // no effect
            if(effectMaybe==Effect.SET)
                effectVariable.set(effectArgument);
            else if(effectMaybe==Effect.CHANGE)
                effectVariable.set(effectVariable.value()+effectArgument);
            else
                throw new Error("Uknown effect");
        }
        
        public String toString() {
            return "T("+super.getTarget()+"): "+effectMaybe + "@" + effectVariable + "," + effectArgument;
        }
    }
}
