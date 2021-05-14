package com.vekai.showcase.batch;

import cn.fisok.raw.beans.propertyeditors.DoublePropertyEditor;
import cn.fisok.raw.beans.propertyeditors.IntegerPropertyEditor;
import cn.fisok.raw.beans.propertyeditors.LongPropertyEditor;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.LogKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.batch.adapter.FlatFileImportBatchJob;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.listener.BatchLoggerMonitorListener;
import com.vekai.showcase.batch.entity.TenpayLoanReq;
import com.vekai.showcase.entity.DemoPerson;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TenpayBatchCase {
    @Autowired
    DataSource dataSource;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
//    @Qualifier("batchTransactionManager")
    PlatformTransactionManager transactionManager;
    @Autowired
    BatchLoggerMonitorListener monitorListener;
    @Autowired
    BeanCruder beanCruder;


//    @BatchJob
    public JobWrapper impawnProductFile(){
        //-----------------------
        //1. 创建Reader
        //-----------------------
        FlatFileItemReader<TenpayLoanReq> reader = new FlatFileItemReader<>();
        reader.setLineMapper(new DefaultLineMapper<TenpayLoanReq>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(getFields());
                setDelimiter("|");  //竖线分割
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<TenpayLoanReq>(){{

                Map<Class<?>,PropertyEditorSupport> typeEditors = MapKit.newHashMap();
                //设置日期，数字类型的转换器，否则日期或数字无法作转换
                typeEditors.put(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyyMMdd"),true));
                typeEditors.put(Double.class,new DoublePropertyEditor());
                typeEditors.put(Integer.class,new IntegerPropertyEditor());
                typeEditors.put(Long.class,new LongPropertyEditor());

                setCustomEditors(typeEditors);
                setTargetType(TenpayLoanReq.class);

            }});
        }});
//        reader.setResource(new FileSystemResource("src/test/resources/data/reduct-flow.txt"));
        reader.setResource(new ClassPathResource("com/vekai/showcase/batch/tenpay/tenpay_loan_req_0923_20180923.txt"));
        reader.setLinesToSkip(2);       //跳过表头

        //-----------------------
        //2. 创建Processor
        //-----------------------
        ItemProcessor<TenpayLoanReq, TenpayLoanReq> processor = new ItemProcessor<TenpayLoanReq, TenpayLoanReq>() {
            public TenpayLoanReq process(TenpayLoanReq in) throws Exception {
                int minute = DateKit.getMinute(DateKit.now());
                LogKit.debug("分钟="+ minute);
                if((minute%2) == 0){
                    LogKit.error("你的时间不巧，出错了！");
                    throw new Exception("随机出错，你撞上了哇，出错日志测试用的");
                }
                TenpayLoanReq out = in;
                Thread.sleep(3000);
                return out;
            }
        };

        //-----------------------
        //3. 创建Writer
        //-----------------------
        ItemWriter<TenpayLoanReq> writer = items -> {
            beanCruder.save(items);
        };
        //插入数据之前，先删除已经存在的
        ItemWriteListener<TenpayLoanReq> itemWriteListener = new ItemListenerSupport<TenpayLoanReq,TenpayLoanReq>(){
            public void beforeWrite(List<? extends TenpayLoanReq> items) {
                beanCruder.delete(items);
            }
        };

        //-----------------------
        //4. 创建Step
        //-----------------------
        SimpleStepBuilder<TenpayLoanReq, TenpayLoanReq> builder = stepBuilderFactory.get("DefaultStep")
                .transactionManager(transactionManager)
                .<TenpayLoanReq, TenpayLoanReq>chunk(3) // 批处理每次提交
                .reader(reader) // 给step绑定reader
                .processor(processor) // 给step绑定processor
                .writer(writer) // 给step绑定writer
                .listener(itemWriteListener);

        //-----------------------
        //5. 创建JOB
        //-----------------------
        builder.listener((StepExecutionListener) monitorListener);
        builder.listener((ChunkListener) monitorListener);
        builder.listener((ItemProcessListener<? super DemoPerson, ? super DemoPerson>) monitorListener);
        builder.listener((ItemReadListener<? super DemoPerson>) monitorListener);
        builder.listener((ItemWriteListener<? super DemoPerson>) monitorListener);

        Job job = jobBuilderFactory.get("impawnProductFileJob")
                .listener(monitorListener)
                .flow(builder.build()) // 为Job指定Step
                .end()
                .build();


        //-----------------------
        //6. 返回JOB包装对象
        //-----------------------
        return new JobWrapper(job)
                .setJobTitle("质押产品文件")
                .setDefaultCrontab("0 0 1/1 * * ? ")   //每10秒运行一次
                ;

    }

    private String[] getFields(){
        return new String[]{
                "serialNo",
                "fundMerchantNo",
                "fundCode",
                "productCode",
                "assetManagerName",
                "productName",
                "productIssueDate",
                "productStartInterestDate",
                "productExpiryDate",
                "productHonourDate",
                "productType",
                "impawnRatio",
                "repaymentMethod",
                "loanBasicInterestRate",
                "remarks",
        };
    }

//    @BatchJob
    public JobWrapper impawnProductFileFromBuilder(){
        FlatFileImportBatchJob batchJob = new FlatFileImportBatchJob(stepBuilderFactory,jobBuilderFactory,transactionManager,monitorListener);
        JobWrapper  jobWrapper =
                batchJob.jobTitle("质押产品文件1").jobName("impawnProductFileJobNew")
                .beanCruder(beanCruder)
                .classType(TenpayLoanReq.class)
                .processor(new ItemProcessor<TenpayLoanReq,TenpayLoanReq>() {
                    public TenpayLoanReq process(TenpayLoanReq item) throws Exception {
                        try{
                            Thread.sleep(1000); //故意延迟
                        }catch (Exception e){
                        }
                        return item;
                    }
                })
                .fields(getFields())
                .delimiter("|")
                .dateFormat("yyyyMMdd")
                .linesToSkip(2)
                .readResource(new ClassPathResource("com/vekai/showcase/batch/tenpay/tenpay_loan_req_0923_20180923.txt"))
                .deleteBeforeInsert(true)
                .chunkSize(2)
                .buildJobWrapper();

        jobWrapper.setDefaultCrontab("0 0 1/1 * * ? ");
        return jobWrapper;
    }
}
