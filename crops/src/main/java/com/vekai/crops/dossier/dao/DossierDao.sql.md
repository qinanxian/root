queryDossier
===
* 查询资料清单对象
```sql
SELECT * 
FROM CMON_DOSSIER 
WHERE DOSSIER_ID=:dossierId
```

queryDossierItems
===
* 查询资料清单明细项
```sql
SELECT * 
FROM CMON_DOSSIER_ITEM 
WHERE DOSSIER_ID=:dossierId 
ORDER BY SORT_CODE ASC,UPDATED_TIME ASC

```

queryFileEntities
===
* 查询资料清单下的所有文件
```sql
SELECT * FROM CMON_FILE A
WHERE A.OBJECT_TYPE='DOSSER_ITEM' 
AND EXISTS(
	SELECT 1 FROM CMON_DOSSIER_ITEM B 
    WHERE A.OBJECT_ID=B.ITEM_ID 
    AND B.DOSSIER_ID=:dossierId
    )
 ORDER BY A.OBJECT_ID ASC
```

queryDossierByObjectType
===
* 根据对象类型对象号，查文档清单对象
```sql
SELECT * FROM CMON_DOSSIER WHERE OBJECT_TYPE=:objectType AND OBJECT_ID=:objectId
```
