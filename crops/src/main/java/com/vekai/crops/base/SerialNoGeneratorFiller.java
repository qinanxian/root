package com.vekai.crops.base;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import java.lang.reflect.Field;
import java.util.List;

@Component
public class SerialNoGeneratorFiller {
    @Autowired
    SerialNumberGeneratorFinder finder;

    public void execFill(Object object) {
        List<Field> fields = JpaKit.getGeneratedValueFields(object.getClass());
        for (Field field : fields) {
            Object fieldValue = BeanKit.getPropertyValue(object, field.getName());
            if (fieldValue == null || StringKit.isBlank(fieldValue.toString())) {
                GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
                String generatorId = StringKit.isBlank(gv.generator()) ? object.getClass().getName() + "." + field.getName() : gv.generator();
                SerialNumberGenerator<String> serialNoGenerator = finder.find(generatorId);
                String serialNo = serialNoGenerator.next(generatorId, object);
                BeanKit.setPropertyValue(object, field.getName(), serialNo);
            }
        }
    }
}
