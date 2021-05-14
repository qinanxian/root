package com.vekai.crops.obiz.contract.state;

public class ContractStateContext {
    private ContractState state;

    public ContractState getState() {
        return state;
    }

    public void setState(ContractState state) {
        this.state = state;
    }

    public void makeReady() {
        state.makeReady();
    }

    public void makeEnable() {
        state.makeEnable();
    }

    public void makeDisabled() {
        state.makeDisabled();
    }

    public void makeInChange() {
        state.makeInChange();
    }
}
