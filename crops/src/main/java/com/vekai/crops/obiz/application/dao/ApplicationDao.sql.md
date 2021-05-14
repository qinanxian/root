queryApplicationProfile
===
* 取业务的产品参数
```sql
SELECT
CUST_ID,
POLICY_ID, CUST_NAME, DATAFORM_ID,
DOSSIER_ID,
DOSSIER_DEF_KEY,
INQUIRE_DOC_ID,
LANDMARK_ID,
WORKFLOW_KEY,DOSSIER_DEF_KEY,RISK_MANAGE_MODEL_KEY
FROM OBIZ_APPLICATION 
WHERE APPLICATION_ID=:applicationId
```
queryApplication
===
* 查询申请对象
```sql
SELECT
*
FROM OBIZ_APPLICATION 
WHERE APPLICATION_ID=:applicationId
```