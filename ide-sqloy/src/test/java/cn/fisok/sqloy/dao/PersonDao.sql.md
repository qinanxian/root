getPerson
---
* 查询单个客户
```sql
select * from DEMO_PERSON where ID=:id
```

getPersonList
----
* 查询单个客户
```sql
select * from DEMO_PERSON where GENDER=:gender
```

updatePersonBirth
---
* 更新用户生日
```sql
UPDATE DEMO_PERSON SET BIRTH=:birth WHERE ID=:id
```