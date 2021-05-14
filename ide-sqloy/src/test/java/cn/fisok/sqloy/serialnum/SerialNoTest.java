package cn.fisok.sqloy.serialnum;

import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.serialnum.consts.CursorRecordType;
import cn.fisok.sqloy.serialnum.cursor.SerialNumberCursorDao;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import cn.fisok.sqloy.serialnum.model.SerialNumberCursor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

/**
 * Created by zhulifeng on 17-12-14.
 */
public class SerialNoTest extends BaseTest {

    @Autowired
    @Qualifier(CursorRecordType.DB_TABLE)
    SerialNumberCursorDao serialNoCursorDao;

    @Autowired
    SerialNumberGeneratorFinder serialNoGeneratorFinder;

    //    @Test
    //    @Rollback
    public void testInsertSerialNoCursor() {
        SerialNumberCursor serialNoCursor = new SerialNumberCursor();
        serialNoCursor.setId("demo.Person.personId");
        serialNoCursor.setCursorValue(1L);
        serialNoCursor.setTemplate("AS0000");
        serialNoCursor.setUpdatedTime(new Date());

        serialNoCursorDao.insertOne(serialNoCursor);
    }

    //    @Test
    //    @Rollback
    public void testGetSerialNoSingleThread() {
        SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find("demo.Person.personId");
        String serialNo = generator.next("demo.Person.personId");
        System.out.println(serialNo);
    }

    //    @Test
    //    @Rollback
    public void testGetSerialNoMultiThread() throws Exception {
        MyThread myThread = new MyThread();
        //        MyThread2 myThread = new MyThread2();

        for (int i = 0; i < 20; i++) {
            new Thread(myThread, String.valueOf(i)).start();
        }

        Thread.sleep(1000 * 60);
    }

    //    @Test
    //    @Rollback
    public void testGetBatchSerialNoSingleThread() {
        SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find("demo.Person.personId");
        String[] serialNo = generator.nextBatch("demo.Person.personId", 10);
        for (int i = 0; i < serialNo.length; i++) {
            System.out.println(serialNo[i]);
        }
    }

    @Test
    @Rollback(true)
    public void testGetSerialNoWithDateYMDSingleThread() {
        String generatorId = "demo.Person.personId";
        SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find(generatorId);
        String serialNo = generator.next(generatorId);
        System.out.println(serialNo);
    }

    @Test
    @Rollback(true)
    public void testGetBatchSerialNoWithDateYMDSingleThread() {
        String generatorId = "demo.Customer.customerId";
        SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find(generatorId);
        //        String[] serialNo = generator.nextBatch(generatorId, 10);
        String[] serialNo = generator.nextBatch(generatorId, 10);
        for (int i = 0; i < serialNo.length; i++) {
            System.out.println(serialNo[i]);
        }
    }

    class MyThread implements Runnable {

        @Override
        public void run() {
            String generatorId = "demo.Food.foodId";
            SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find(generatorId);
            String serialNo = generator.next(generatorId);
            System.out.println("===================serialNo=" + serialNo);
        }
    }


    class MyThread2 implements Runnable {

        @Override
        public void run() {
            String generatorId = "demo.Person.personId";
            int start = 0;
            while (start < 1000000) {
                SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find(generatorId);
                String serialNo = generator.next(generatorId);
                System.out.println("===================serialNo=" + serialNo);
                start++;
            }
        }
    }
}
