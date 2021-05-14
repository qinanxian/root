package com.vekai.lact.core.loader;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.model.LactLoan;

public interface DataObjectLoader<T extends LactLoan> {
    void load(T dataObject, MapObject parameter);
}
