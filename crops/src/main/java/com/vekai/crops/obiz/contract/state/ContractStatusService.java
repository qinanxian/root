package com.vekai.crops.obiz.contract.state;

import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.state.impl.NullState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContractStatusService {
    ContractStateContext contractStateContext = new ContractStateContext();

    public ContractState getState(ObizContract contract){
        String status = contract.getContractStatus();
        return null;
    }

    public void makeReady(String contractId) {
        contractStateContext.makeReady();
    }

    public void makeEnable(String contractId) {
        contractStateContext.makeEnable();
    }

    public void makeDisabled(String contractId) {
        contractStateContext.makeDisabled();
    }

    public void makeInChange(String contractId) {
        contractStateContext.makeInChange();
    }
}
