package com.vekai.crops.obiz.contract.state.impl;

import com.vekai.crops.obiz.contract.state.ContractState;

public class DisabledState implements ContractState {
    public void makeReady() {

    }

    public void makeEnable() {

    }

    public void makeDisabled() {

    }

    public void makeInChange() {

    }

    public String getStateName() {
        return "DISABLED";
    }
}
