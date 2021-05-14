package com.vekai.dataform.util;

import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.exception.ValidatorException;
import com.vekai.dataform.handler.DataObjectHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementValidatorMode;
import com.vekai.dataform.model.types.ElementValidatorRunAt;
import com.vekai.dataform.validator.ValidateRecord;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.validator.ValidateType;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 校验实现功能，单独放到一个类中来
 */
public class DataFormValidateUtils {
    protected static Logger logger = LoggerFactory.getLogger(DataFormValidateUtils.class);

    public static ValidateResult validateOne(DataForm dataForm, DataObjectHandler dataFormHandler, Object dataOne) {
        ValidateResult vr = new ValidateResult();

        Object entity = dataOne;
        List<DataFormElement> elements = dataForm.getElements();
        for (DataFormElement element : elements) {
            //检查必输项
            validateRequired(vr, element, entity);
            //根据规则作检查
            validateByRules(vr, dataFormHandler, element, entity);
        }

        return vr;
    }

    public static <T> List<ValidateResult> validateList(DataForm dataForm, DataObjectHandler dataFormHandler, List<T> dataList) {
        List<ValidateResult> vrList = new ArrayList<ValidateResult>(dataList.size());
        //每行列表都必需初始化一个校验结果
        for (int i = 0; i < dataList.size(); i++) {
            vrList.add(new ValidateResult());
        }

        //处理每一行校验
        for (int i = 0; i < dataList.size(); i++) {
            ValidateResult vrItem = vrList.get(i);
            List<DataFormElement> elements = dataForm.getElements();
            for (DataFormElement element : elements) {
                //检查必输项
                validateRequired(vrItem, element, dataList.get(i));
                //根据规则作检查
                validateListByRules(vrItem,dataFormHandler,element,dataList.get(i),dataList);
            }

        }

        return vrList;
    }


    /**
     * 必输项检查
     *
     * @param validateResult validateResult
     * @param element element
     * @param entity entity
     */
    private static <T> void validateRequired(ValidateResult validateResult, DataFormElement element, T entity) {
        if (element.getElementUIHint().getRequired()) {
            ValueObject vo = BeanKit.getPropertyValueObject(entity, element.getCode());
            if (vo.isBlank()) {
                String errorText = MessageHolder.getMessage("此项必填", "dataform.validate.required");

                ValidateRecord vRecord = new ValidateRecord();
                vRecord.setMandatory(true)
                        .setPassed(false)
                        .setValidateType(ValidateType.Required)
                        .setMessage(errorText);
                validateResult.addRecord(element.getCode(), vRecord);
            }
        }
    }

    /**
     * 校验列表对象
     *
     * @param validateResult validateResult
     * @param dataFormHandler dataFormHandler
     * @param element element
     * @param object object
     * @param dataList dataList
     */
    public static <T> void validateListByRules(ValidateResult validateResult, DataObjectHandler dataFormHandler
            , DataFormElement element, T object, List<T> dataList) {
        /**
         * 构建校验器，执行单个bean这一类的校验
         */
        ValidateByRuleTemplate<T> ruleTemplate = new ValidateByRuleTemplate<T>(validateResult,dataFormHandler,element){
            public void validateByHandler(String methodName, Object v, T object, List<T> objectList) {
                String methodFullName = dataFormHandler.getClass().getName() + "." + methodName;
                try {
//                    Method method = dataFormHandler.getClass().getMethod(methodName, v.getClass(), object.getClass(),objectList.getClass());
                    Method method = ClassKit.getMethod(dataFormHandler.getClass(),methodName);
                    if(method == null){
                        throw new DataFormException(methodFullName + "方法不存在!");
                    }
                    method.invoke(dataFormHandler, v, object,objectList);
                } catch (IllegalAccessException e) {
                    throw new DataFormException(methodFullName + "方法调用出错，请检查参数(第一个是值，第二个是对象,第三个是列表)");
                } catch (InvocationTargetException e) {
                    //校验方法抛出校验异常了，则截取下来，并且返回
                    parseException(validateResult,element,e);
                }
            }
        };
        ruleTemplate.exec(object,dataList);
    }

    /**
     * 根据正则表达式，handler上配置的方法进行校验（校验单个对象）
     *
     * @param validateResult validateResult
     * @param dataFormHandler dataFormHandler
     * @param element element
     * @param object object
     */
    private static void validateByRules(ValidateResult validateResult, DataObjectHandler dataFormHandler, DataFormElement element, Object object) {
        /**
         * 构建校验器，执行单个bean这一类的校验
         */
        ValidateByRuleTemplate<Object> ruleTemplate = new ValidateByRuleTemplate<Object>(validateResult,dataFormHandler,element){
            public void validateByHandler(String methodName, Object v, Object object, List<Object> objectList) {
                String methodFullName = dataFormHandler.getClass().getName() + "." + methodName;
                try {
//                    Method method = dataFormHandler.getClass().getMethod(methodName, v.getClass(), object.getClass());
                    Method method = ClassKit.getMethod(dataFormHandler.getClass(),methodName);
                    if(method == null){
                        throw new DataFormException(methodFullName + "方法不存在!");
                    }
                    method.invoke(dataFormHandler, v, object);
                } catch (IllegalAccessException e) {
                    throw new DataFormException(methodFullName + "方法调用出错，请检查参数(第一个是值，第二个是对象)");
                } catch (InvocationTargetException e) {
                    //校验方法抛出校验异常了，则截取下来，并且返回
                    parseException(validateResult,element,e);
                }
            }
        };
        ruleTemplate.exec(object);
    }

    private static void parseException(ValidateResult validateResult,DataFormElement element,InvocationTargetException e){
        //校验方法抛出校验异常了，则截取下来，并且返回
        Throwable targetException = e.getTargetException();
        if (targetException instanceof ValidatorException) {
            String message = ((ValidatorException) targetException).getMessage();
            ValidateRecord record = new ValidateRecord();
            record.setPassed(false);
            record.setMandatory(true);
            record.setMessage(message);
            validateResult.addRecord(element.getCode(), record);
        } else {
            throw new DataFormException(e);
        }
    }

    /**
     * 把根据规则校验的，使用一个模板类来处理，因为校验处理大部分都相同，只是List和Info在调用handler方法校验时，参数会不同
     * 把都有的校验部分剥离，没有的部分校验自己独立实现就可以了
     *
     * @param <T> T
     */
    private static abstract class ValidateByRuleTemplate<T>{
        protected ValidateResult validateResult;
        protected DataObjectHandler dataFormHandler;
        protected DataFormElement element;

        public ValidateByRuleTemplate(ValidateResult validateResult, DataObjectHandler dataFormHandler, DataFormElement element) {
            this.validateResult = validateResult;
            this.dataFormHandler = dataFormHandler;
            this.element = element;
        }
        public abstract void validateByHandler(String methodName,Object v,T object,List<T> objectList);

        public void exec(T object){
            exec(object,null);
        }
        public void exec(T object,List<T> objectList){
            List<DataFormElement.FormElementValidator> validators = element.getValidatorList();
            //处理每一个校验项
            validators.forEach(validator -> {
                ElementValidatorRunAt runAt = validator.getRunAt();
                ElementValidatorMode mode = validator.getMode();
                if (runAt == ElementValidatorRunAt.Server || runAt == ElementValidatorRunAt.Both) {
                    String expr = validator.getExpr();
                    if (StringKit.isBlank(expr)) return;

                    String defaultMessage = validator.getDefaultMessage();
                    String defaultMessageI18n = validator.getDefaultMessageI18nCode();
                    String message = defaultMessage;
                    if (StringKit.isNotBlank(defaultMessageI18n)) {
                        message = MessageHolder.getMessage(defaultMessage, defaultMessageI18n);
                    }

                    Object v = null;
                    if (object instanceof Map) {
                        v = DataFormUtils.getMapValue(element, MapObject.build((Map) object));
                    } else {
                        v = BeanKit.getPropertyValue(object, element.getCode());
                    }
                    if (v == null) return;

                    //正则表达式
                    if (mode == ElementValidatorMode.RegExp) {
                        String strVal = ValueObject.valueOf(v).strValue();
                        if (StringKit.isBlank(strVal)) return;    //空字串不校验

                        Pattern pattern = Pattern.compile(expr);
                        if (!pattern.matcher(strVal).find()) {
                            ValidateRecord record = new ValidateRecord();
                            record.setPassed(false);
                            record.setMandatory(true);
                            record.setMessage(message);
                            validateResult.addRecord(element.getCode(), record);
                        }
                    }
                    //调用handler上的方法进行校验
                    if (mode == ElementValidatorMode.HandlerMethod) {
                        String methodName = expr;
                        validateByHandler(methodName,v,object,objectList);
                    }

                }
            });

        }
    }

}
