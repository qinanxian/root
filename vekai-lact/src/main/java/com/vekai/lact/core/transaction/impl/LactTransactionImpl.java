package com.vekai.lact.core.transaction.impl;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.core.handler.DataObjectHandler;
import com.vekai.lact.core.loader.DataObjectLoader;
import com.vekai.lact.core.transaction.LactTransaction;
import com.vekai.lact.model.LactLoan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class LactTransactionImpl<T extends LactLoan> extends LactTransaction<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<DataObjectLoader> loaders =  new ArrayList<DataObjectLoader>();
    protected List<DataObjectHandler> handlers =  new ArrayList<DataObjectHandler>();

    public List<DataObjectLoader> getLoaders() {
        return loaders;
    }

    public void setLoaders(List<DataObjectLoader> loaders) {
        this.loaders = loaders;
    }

    public List<DataObjectHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<DataObjectHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void reverse(T dataObject, MapObject parameter) {

    }

    @Override
    public void load(T dataObject, MapObject parameter) {

        logger.debug(MessageFormat.format("交易[{0}-{1}]初始化...", getCode(), getName()));
        for(DataObjectLoader loader : loaders){
            loader.load(dataObject, parameter);
        }
    }

    @Override
    public void handle(T dataObject) {
        logger.debug(MessageFormat.format("交易[{0}-{1}]开始执行...", getCode(), getName()));
        for(DataObjectHandler handler : handlers){
            handler.handle(dataObject);
        }

    }
}

