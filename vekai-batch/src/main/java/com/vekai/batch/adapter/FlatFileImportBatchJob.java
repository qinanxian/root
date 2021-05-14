package com.vekai.batch.adapter;

import com.vekai.batch.listener.BatchLoggerMonitorListener;
import com.vekai.batch.core.JobWrapper;
import cn.fisok.raw.beans.propertyeditors.DoublePropertyEditor;
import cn.fisok.raw.beans.propertyeditors.IntegerPropertyEditor;
import cn.fisok.raw.beans.propertyeditors.LongPropertyEditor;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 把文件的导入作简化处理
 * @param <T>
 */
public class FlatFileImportBatchJob<T> {
    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;
    private PlatformTransactionManager transactionManager;
    private BatchLoggerMonitorListener monitorListener;

    private String jobName;
    private String jobTitle;

    private String[] fields;
    private String delimiter;
    private String dateFormat;
    private Class<? extends T> classType;
    private Resource readResource;
    private int linesToSkip = 0;
    private ItemProcessor<T,T> processor;
    private int chunkSize = 10;
    private ItemWriteListener<T> itemWriteListener;
    private ItemWriter<T> writer = null;

    private boolean deleteBeforeInsert = false;

    private BeanCruder beanCruder;

    public FlatFileImportBatchJob(){
        processor = new ItemProcessor<T, T>() {
            public T process(T in) throws Exception {
                return in;
            }
        };

        writer = items -> {
            beanCruder.save(items);
        };
    }

    public FlatFileImportBatchJob(StepBuilderFactory stepBuilderFactory, JobBuilderFactory jobBuilderFactory, PlatformTransactionManager transactionManager, BatchLoggerMonitorListener monitorListener) {
        this();
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.transactionManager = transactionManager;
        this.monitorListener = monitorListener;
    }

    public JobWrapper buildJobWrapper(){
        //-----------------------
        //1. 创建Reader
        //-----------------------
        FlatFileItemReader<T> reader = new FlatFileItemReader<T>();
        reader.setLineMapper(new DefaultLineMapper<T>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(fields);
                setDelimiter(delimiter);  //竖线分割
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<T>(){{

                Map<Class<?>,PropertyEditorSupport> typeEditors = MapKit.newHashMap();
                //设置日期，数字类型的转换器，否则日期或数字无法作转换
                typeEditors.put(Date.class,new CustomDateEditor(new SimpleDateFormat(dateFormat),true));
                typeEditors.put(Double.class,new DoublePropertyEditor());
                typeEditors.put(Integer.class,new IntegerPropertyEditor());
                typeEditors.put(Long.class,new LongPropertyEditor());

                setCustomEditors(typeEditors);
                setTargetType(classType);
            }});
        }});
//        reader.setResource(new FileSystemResource("src/test/resources/data/reduct-flow.txt"));
        reader.setResource(readResource);
        reader.setLinesToSkip(linesToSkip);       //跳过表头



        //-----------------------
        //4. 创建Step
        //-----------------------
        SimpleStepBuilder<T, T> builder = stepBuilderFactory.get(jobName+"Step")
                .transactionManager(transactionManager)
                .<T, T>chunk(chunkSize) // 批处理每次提交
                .reader(reader) // 给step绑定reader
                .processor(processor) // 给step绑定processor
                .writer(writer) // 给step绑定writer
                ;
        //插入前删除
        if(deleteBeforeInsert){
            ItemWriteListener<T> deleteWriteListener = new ItemListenerSupport<T,T>(){
                public void beforeWrite(List<? extends T> items) {
                    beanCruder.delete(items);
                }
            };
            builder.listener(deleteWriteListener);
        }

        if(itemWriteListener != null){
            builder.listener(itemWriteListener);
        }

        //-----------------------
        //5. 创建JOB
        //-----------------------
        builder.listener((StepExecutionListener) monitorListener);
        builder.listener((ChunkListener) monitorListener);
        builder.listener((ItemProcessListener<? super T, ? super T>) monitorListener);
        builder.listener((ItemReadListener<? super T>) monitorListener);
        builder.listener((ItemWriteListener<? super T>) monitorListener);

        Job job = jobBuilderFactory.get(jobName)
                .listener(monitorListener)
                .flow(builder.build()) // 为Job指定Step
                .end()
                .build();

        //-----------------------
        //6. 返回JOB包装对象
        //-----------------------
        return new JobWrapper(job)
                .setJobTitle(jobTitle)
                ;
    }

    public FlatFileImportBatchJob<T> classType(Class<? extends T> classType){
        this.classType = classType;
        return this;
    }

    public FlatFileImportBatchJob<T> beanCruder(BeanCruder beanCruder){
        this.beanCruder = beanCruder;
        return this;
    }

    public FlatFileImportBatchJob<T> jobName(String jobName){
        this.jobName = jobName;
        return this;
    }

    public FlatFileImportBatchJob<T> jobTitle(String jobTitle){
        this.jobTitle = jobTitle;
        return this;
    }

    public FlatFileImportBatchJob<T> fields(String[] fields){
        this.fields = fields;
        return this;
    }

    public FlatFileImportBatchJob<T> delimiter(String delimiter){
        this.delimiter = delimiter;
        return this;
    }


    public FlatFileImportBatchJob<T> dateFormat(String dateFormat){
        this.dateFormat = dateFormat;
        return this;
    }

    public FlatFileImportBatchJob<T> readResource(Resource readResource){
        this.readResource = readResource;
        return this;
    }

    public FlatFileImportBatchJob<T> linesToSkip(int linesToSkip){
        this.linesToSkip = linesToSkip;
        return this;
    }

    public FlatFileImportBatchJob<T> processor(ItemProcessor<T,T> processor){
        this.processor = processor;
        return this;
    }

    public FlatFileImportBatchJob<T> chunkSize(int chunkSize){
        this.chunkSize = chunkSize;
        return this;
    }

    public FlatFileImportBatchJob<T> itemWriteListener(ItemWriteListener<T> itemWriteListener){
        this.itemWriteListener = itemWriteListener;
        return this;
    }

    public FlatFileImportBatchJob<T> itemWriteListener(ItemWriter<T> writer){
        this.writer = writer;
        return this;
    }

    public FlatFileImportBatchJob<T> deleteBeforeInsert(boolean deleteBeforeInsert){
        this.deleteBeforeInsert = deleteBeforeInsert;
        return this;
    }




}
