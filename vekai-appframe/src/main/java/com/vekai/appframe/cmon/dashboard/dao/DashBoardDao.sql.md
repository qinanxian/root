getNumOfMeeting
---
* 查询 本月/本年 发起的项目预沟通会议数量
```sql
select * from CMON_MEETING where CREATED_TIME between :startTime  and :endTime
```


getNumOfIvstPlan
---
* 查询 本月/本年 审核通过的项目数量
```sql
SELECT * FROM WKFL_PROC WHERE PROC_DEF_KEY = :procDekKey  AND STATUS = :status AND FINISH_TIME BETWEEN :startTime  and :endTime
```

getNumOfDueDiligence
---
* 查询 本月/本年 项目下审核通过的尽调及投决流程数量
```sql
 SELECT DISTINCT PLAN_ID  FROM IVST_APPLY WHERE APPLY_ID IN (SELECT OBJECT_ID FROM WKFL_PROC WHERE PROC_DEF_KEY = :procDekKey  AND STATUS = :status AND FINISH_TIME BETWEEN :startTime  and :endTime);
```


getNumOfContractCompleted
---
* 查询 本月/本年 项目下通过审核的主合同数量
```sql

```


getNumOfIvstPlanLoan
---
* 查询 本月/本年 项目下最早通过审核的出资申请数量
```sql
 SELECT DISTINCT PLAN_ID  FROM IVST_PLAN_LOAN WHERE LOAN_ID IN (SELECT OBJECT_ID FROM WKFL_PROC WHERE PROC_DEF_KEY = :procDekKey  AND STATUS = STATUS = :status AND FINISH_TIME BETWEEN :startTime  and :endTime);
```



getNumOfIvstPlanForPlanPhase
---
* 查询本年/本月 所有已放款且未结束的项目数量
```sql
SELECT DISTINCT PLAN_ID FROM IVST_PLAN_LOAN WHERE ACTUAL_LOAN_DATE is NOT NULL AND ACTUAL_LOAN_DATE BETWEEN :startTime  and :endTime
```

getNumOfIvstPlanCompleted
---
* 查询本年/本月 所有已完成的项目
```sql

```

getSumMoneyOfIvstForPlanPhase
---
* 查询本年/本月 所有项目下出资总额
```sql
SELECT SUM(LOAN_SUM) FROM(SELECT LOAN_SUM FROM IVST_PLAN_LOAN WHERE ACTUAL_LOAN_DATE is NOT NULL AND ACTUAL_LOAN_DATE BETWEEN :startTime  and :endTime)
```

