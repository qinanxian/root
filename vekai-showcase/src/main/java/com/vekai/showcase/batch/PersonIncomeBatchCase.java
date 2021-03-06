package com.vekai.showcase.batch;

import cn.fisok.raw.beans.propertyeditors.DoublePropertyEditor;
import cn.fisok.raw.beans.propertyeditors.IntegerPropertyEditor;
import cn.fisok.raw.beans.propertyeditors.LongPropertyEditor;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.core.rowmapper.JpaBeanPropertyRowMapper;
import com.vekai.batch.annotation.BatchJob;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.listener.BatchLoggerMonitorListener;
import com.vekai.showcase.entity.DemoPerson;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PersonIncomeBatchCase {

    @Autowired
    SqloyProperties sqloyProperties;
    @Autowired
    DataSource dataSource;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
//    @Qualifier("batchTransactionManager")
    PlatformTransactionManager transactionManager;
    @Autowired
    BatchLoggerMonitorListener monitorListener;

    private String[] getFields(){
        return new String[]{
                "id",
                "code",
                "name",
                "chnName",
                "engName",
                "avatar",
                "gender",
                "birth",
                "height",
                "weight",
                "nation",
                "political",
                "marital",
                "educationLevel",
                "graduatedFrom",
                "domicilePlaceProvince",
                "domicilePlaceCity",
                "domicilePlaceAddress",
                "presentAddress",
                "mobilePhone",
                "email",
                "workAs",
                "jobTitle",
                "companyIndustry",
                "companyAddress",
                "companyPostcode",
                "entryDate",
                "monthIncome",
                "familyMonthIncome",
                "familyYearIncome",
                "familyMonthCost",
                "hobby",
                "remark",
                "status",
                "revision",
                "createdBy",
                "createdTime",
                "updatedBy",
                "updatedTime",
        };
    }

    @BatchJob
    public JobWrapper demoPersonImportJob(){
        //-----------------------
        //1. ??????Reader
        //-----------------------
        FlatFileItemReader<DemoPerson> reader = new FlatFileItemReader<>();
        reader.setLineMapper(new DefaultLineMapper<DemoPerson>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(getFields());
                setDelimiter("|");  //????????????
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<DemoPerson>(){{

                Map<Class<?>,PropertyEditorSupport> typeEditors = MapKit.newHashMap();
                //??????????????????????????????????????????????????????????????????????????????
                typeEditors.put(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy/MM/dd"),true));
                typeEditors.put(Double.class,new DoublePropertyEditor());
                typeEditors.put(Integer.class,new IntegerPropertyEditor());
                typeEditors.put(Long.class,new LongPropertyEditor());

                setCustomEditors(typeEditors);
                setTargetType(DemoPerson.class);

            }});
        }});
//        reader.setResource(new FileSystemResource("src/test/resources/data/reduct-flow.txt"));
        reader.setResource(new ClassPathResource("com/vekai/showcase/batch/DEMO_PERSON_201807120001.txt"));
        reader.setLinesToSkip(1);       //????????????


        //-----------------------
        //2. ??????Processor
        //-----------------------
        ItemProcessor<DemoPerson, DemoPerson> processor = new ItemProcessor<DemoPerson, DemoPerson>() {
            public DemoPerson process(DemoPerson in) throws Exception {
                DemoPerson out = in;

                out.setRemark("???????????????,????????????="+DateKit.format(DateKit.now()));
                out.setUpdatedTime(DateKit.now());

                return out;
            }
        };

        StringBuffer insertSql = new StringBuffer("INSERT INTO DEMO_PERSON(");
        insertSql.append(" ID, CODE, NAME, CHN_NAME, ENG_NAME, AVATAR, GENDER, BIRTH, HEIGHT, WEIGHT, NATION, POLITICAL, MARITAL, EDUCATION_LEVEL, GRADUATED_FROM, DOMICILE_PLACE_PROVINCE, DOMICILE_PLACE_CITY, DOMICILE_PLACE_ADDRESS, PRESENT_ADDRESS, MOBILE_PHONE, EMAIL, WORK_AS, JOB_TITLE, COMPANY_INDUSTRY, COMPANY_ADDRESS, COMPANY_POSTCODE, ENTRY_DATE, MONTH_INCOME, FAMILY_MONTH_INCOME, FAMILY_YEAR_INCOME, FAMILY_MONTH_COST, HOBBY, REMARK, STATUS, REVISION, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME");
        insertSql.append(") VALUES(");
        insertSql.append(":id,:code,:name,:chnName,  :engName,:avatar,:gender,:birth,:height,:weight,:nation,:political,:marital,:educationLevel,  :graduatedFrom,  :domicilePlaceProvince,  :domicilePlaceCity,  :domicilePlaceAddress, :presentAddress,:mobilePhone, :email, :workAs, :jobTitle, :companyIndustry, :companyAddress, :companyPostcode, :entryDate, :monthIncome,  :familyMonthIncome,  :familyYearIncome, :familyMonthCost, :hobby,:remark,:status,:revision, :createdBy, :createdTime, :updatedBy, :updatedTime");
        insertSql.append(")");
        //-----------------------
        //3. ??????Writer
        //-----------------------
        JdbcBatchItemWriter<DemoPerson> writer = new JdbcBatchItemWriter<DemoPerson>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<DemoPerson>());
        writer.setSql(insertSql.toString());
        writer.setJdbcTemplate(namedParameterJdbcTemplate);
        writer.afterPropertiesSet();

        //-----------------------
        //4. ??????Step
        //-----------------------
        //?????????????????????????????????????????????
        ItemWriteListener<DemoPerson> itemWriteListener = new ItemListenerSupport<DemoPerson,DemoPerson>(){
            public void beforeWrite(List<? extends DemoPerson> items) {
                super.beforeWrite(items);
                List<String> idList = ListKit.newArrayList();
                items.forEach(item->{
                    idList.add(item.getId());
                });
                namedParameterJdbcTemplate.update("DELETE FROM DEMO_PERSON WHERE ID IN(:ids)",MapKit.mapOf("ids",idList));
            }
        };

        SimpleStepBuilder<DemoPerson, DemoPerson> builder  = stepBuilderFactory.get("DefaultStep")
                .transactionManager(transactionManager)
                .<DemoPerson, DemoPerson>chunk(10) // ?????????????????????5000?????????
                .reader(reader) // ???step??????reader
                .processor(processor) // ???step??????processor
                .writer(writer) // ???step??????writer
                .listener(itemWriteListener)
        ;

        //-----------------------
        //5. ??????JOB
        //-----------------------

        Job job = jobBuilderFactory.get("DemoPersonImportJob")
                .listener(monitorListener)
                .flow(builder.build()) // ???Job??????Step
                .end()
                .build();

        //-----------------------
        //6. ??????JOB????????????
        //-----------------------
        builder.listener((StepExecutionListener) monitorListener);
        builder.listener((ChunkListener) monitorListener);
        builder.listener((ItemProcessListener<? super DemoPerson, ? super DemoPerson>) monitorListener);
        builder.listener((ItemReadListener<? super DemoPerson>) monitorListener);
        builder.listener((ItemWriteListener<? super DemoPerson>) monitorListener);

        return new JobWrapper(job)
                .setDefaultCrontab("0 0 1/1 * * ? ")   //???10???????????????
                .setJobTitle("????????????-??????????????????");
    }

    @BatchJob
    private JobWrapper createPersonYearIncomeJob() throws Exception {
        //-----------------------
        //1. ??????Reader
        //-----------------------
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);
        queryProviderFactoryBean.setSelectClause("SELECT * ");
        queryProviderFactoryBean.setFromClause("FROM DEMO_PERSON");
        queryProviderFactoryBean.setWhereClause("WHERE ID>:id");
        queryProviderFactoryBean.setSortKey("ID");

        JdbcPagingItemReader<DemoPerson> reader = new JdbcPagingItemReader<DemoPerson>();
        reader.setQueryProvider(queryProviderFactoryBean.getObject());
        reader.setParameterValues(MapKit.mapOf("id","0"));
        reader.setPageSize(10);
        reader.setRowMapper(new JpaBeanPropertyRowMapper<DemoPerson>(DemoPerson.class));
        reader.setDataSource(dataSource);
        reader.afterPropertiesSet();

        //-----------------------
        //2. ??????Processor
        //-----------------------
        ItemProcessor<DemoPerson,DemoPerson> processor = new ItemProcessor<DemoPerson,DemoPerson>(){
            public DemoPerson process(DemoPerson in) throws Exception {
                DemoPerson out = in;

                Double familyMonthIncome = out.getFamilyMonthIncome();
                if(familyMonthIncome == null)familyMonthIncome = 0d;
                Double yearIncome = familyMonthIncome * 12;
                if(yearIncome == 0) yearIncome = Math.random();
                out.setFamilyYearIncome(yearIncome);
                out.setRemark("R:"+yearIncome*100);
                out.setUpdatedTime(DateKit.now());

                return out;
            }
        };

        //-----------------------
        //3. ??????Writer
        //-----------------------
        JdbcBatchItemWriter<DemoPerson> writer = new JdbcBatchItemWriter<DemoPerson>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<DemoPerson>());
        writer.setSql("UPDATE DEMO_PERSON set FAMILY_YEAR_INCOME=:familyYearIncome,REMARK=:remark where ID=:id");
        writer.setDataSource(dataSource);
        writer.afterPropertiesSet();


        //-----------------------
        //4. ??????Step
        //-----------------------
        SimpleStepBuilder<DemoPerson, DemoPerson> builder = stepBuilderFactory.get("DefaultStep")
                .transactionManager(transactionManager)
                .<DemoPerson, DemoPerson>chunk(10) // ?????????????????????10????????????reader??????pagesize??????
                .reader(reader) // ???step??????reader
                .processor(processor) // ???step??????processor
                .writer(writer) // ???step??????writer
            ;
        //-----------------------
        //5. ??????JOB
        //-----------------------
        builder.listener((StepExecutionListener) monitorListener);
        builder.listener((ChunkListener) monitorListener);
        builder.listener((ItemProcessListener<? super DemoPerson, ? super DemoPerson>) monitorListener);
        builder.listener((ItemReadListener<? super DemoPerson>) monitorListener);
        builder.listener((ItemWriteListener<? super DemoPerson>) monitorListener);

        Job job = jobBuilderFactory.get("PersonYearIncomeJob")
                .listener(monitorListener)
                .flow(builder.build())
                .end()
                .build();


        //-----------------------
        //6. ??????JOB????????????
        //-----------------------
        return new JobWrapper(job)
                .setJobTitle("????????????-???????????????")
                .setDefaultCrontab("0 0 1/1 * * ? ")   //???10???????????????
                ;
    }
}
