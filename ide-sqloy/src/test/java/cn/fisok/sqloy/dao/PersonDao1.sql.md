getPerson
---
* 查询单个客户
```sql
select * from DEMO_PERSON where ID=:id
```

getPersonList
---
* 根据性别查询客户列表
```sql
select * from DEMO_PERSON where GENDER=:gender
```