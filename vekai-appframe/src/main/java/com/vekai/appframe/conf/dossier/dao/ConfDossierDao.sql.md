queryConfDossier
===
* 查询资料清单配置定义
```sql
SELECT * 
FROM CONF_DOSSIER 
WHERE DOSSIER_DEF_KEY=:dossierDefKey
```

queryConfDossierItems
===
* 查询资料清单配置明细
```sql
SELECT * 
FROM CONF_DOSSIER_ITEM 
WHERE DOSSIER_DEF_KEY=:dossierDefKey 
ORDER BY SORT_CODE ASC,UPDATED_TIME ASC

```

queryConfDossierItem
===
* 查询资料清单明细项对象
```sql
SELECT * 
FROM CONF_DOSSIER_ITEM 
WHERE ITEM_DEF_ID=:itemDefId
```

queryConfDossierItemByDefKey
===
* 查询资料清单明细项对象
```sql
SELECT * 
FROM CONF_DOSSIER_ITEM 
WHERE DOSSIER_DEF_KEY=:defKey AND ITEM_DEF_KEY=:itemDefKey

```

queryTplFileEntities
===
* 查询格式化文档下定义的文件对象
```sql
SELECT D.*,F.* FROM CONF_DOSSIER M,CONF_DOSSIER_ITEM D,CMON_FILE F
WHERE M.DOSSIER_DEF_KEY=D.DOSSIER_DEF_KEY
AND D.TPL_FILE_ID=F.FILE_ID
AND M.DOSSIER_DEF_KEY=:dossierDefKey
```