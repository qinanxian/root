package cn.fisok.web.exhandle;

import cn.fisok.raw.lang.FisokException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandleAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandleAdvice.class);

    @ExceptionHandler(FisokException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ExceptionModel handleBizEx(FisokException ex) {
        LOGGER.error("BizException", ex);
        ExceptionModel em = new ExceptionModel();
        em.setMessage(ex.getMessage());
        em.setCode(ex.getCode());
        em.setType(ExceptionModel.EXCEPTION_TYPE_BIZ);
        return em;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionModel handleEx(RuntimeException ex) {
        LOGGER.error("RuntimeException", ex);
        ExceptionModel em = new ExceptionModel();
        em.setMessage(ex.getMessage());
        if (ex instanceof FisokException) {
            FisokException vekaiException = (FisokException)ex;
            em.setCode(vekaiException.getCode());
        }
        if (null == em.getType())
            em.setType(ExceptionModel.EXCEPTION_TYPE_SYS);
        return em;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionModel handleCheckedEx(Exception ex) {
        LOGGER.error("Exception", ex);
        ExceptionModel em = new ExceptionModel();
        em.setMessage(ex.getMessage());
        em.setType(ExceptionModel.EXCEPTION_TYPE_SYS);
        return em;
    }
}
