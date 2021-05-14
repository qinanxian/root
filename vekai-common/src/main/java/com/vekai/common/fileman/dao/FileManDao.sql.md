queryFileEntity
===
* 查询文件实体
```sql
SELECT * FROM CMON_FILE WHERE FILE_ID=:fileId
```

queryFileEntityByShowCode
===
* 使用显示码查询文件实体
```sql
SELECT * FROM CMON_FILE WHERE SHOW_CODE=:showCode
```

queryFileEntityByObject
===
* 使用对象查询文件实体
```sql
SELECT * FROM CMON_FILE where OBJECT_TYPE=:objectType AND OBJECT_ID=:objectId
```

queryFileEntityHist
===
* 查询文件历史实体
```sql
SELECT * FROM CMON_FILE_HIST WHERE FILE_HIST_ID=:fileHistId
```

queryFileEntityHistList
===
* 查询文件历史实体列表
```sql
SELECT * FROM CMON_FILE_HIST WHERE FILE_ID=:fileId ORDER BY VERSION_CODE ASC

```