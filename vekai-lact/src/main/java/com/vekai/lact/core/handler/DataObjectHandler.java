package com.vekai.lact.core.handler;

import com.vekai.lact.model.LactLoan;

public interface DataObjectHandler<T extends LactLoan> {
    void handle(T dataObject);
}
