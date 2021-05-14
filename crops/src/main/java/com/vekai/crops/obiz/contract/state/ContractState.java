package com.vekai.crops.obiz.contract.state;

public interface ContractState {
    void makeReady();
    void makeEnable();
    void makeDisabled();
    void makeInChange();
    String getStateName();
}
