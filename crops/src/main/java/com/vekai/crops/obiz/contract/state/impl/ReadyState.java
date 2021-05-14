package com.vekai.crops.obiz.contract.state.impl;

import com.vekai.crops.obiz.contract.state.ContractState;
import com.vekai.crops.obiz.contract.state.ContractStateContext;

public class ReadyState implements ContractState {
    private ContractStateContext stateContext;

    public ReadyState(ContractStateContext stateContext) {
        this.stateContext = stateContext;
    }

    public void makeReady() {

    }

    public void makeEnable() {

    }

    public void makeDisabled() {

    }

    public void makeInChange() {

    }

    public String getStateName() {
        return "READY";
    }
}
