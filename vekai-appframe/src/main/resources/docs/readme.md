# 剥离amix中，与具体业务无关的，属于应用基础服务的到appframe中来

base
------
* 移动前：          /amix/src/main/java/com/amarsoft/amix/base
* 移动后: /vekai-appframe/src/main/java/com/vekai/appframe/base
* dataform中需要调整的包

```
    com.amarsoft.amix.base.dataform.handler -> com.vekai.appframe.base.dataform.handler
    com.amarsoft.amix.base.dict.handler     -> com.vekai.appframe.base.dict.handler
    com.amarsoft.amix.base.param.handler    -> com.vekai.appframe.base.param.handler
```
auth
------
* 移动前：          /amix/src/main/java/com/amarsoft/amix/auth
* 移动后: /vekai-appframe/src/main/java/com/vekai/appframe/auth
* dataform中需要调整的包

```
    com.amarsoft.amix.auth.handler -> com.vekai.appframe.auth.handler
```

fnastat
------
* 移动前：          /amix/src/main/java/com/amarsoft/amix/fnastat
* 移动后: /vekai-appframe/src/main/java/com/vekai/appframe/fnastat
* dataform中需要调整的包

```
    com.amarsoft.amix.fnastat -> com.vekai.appframe.fnastat
```

workflow
------
* 移动前：          /amix/src/main/java/com/amarsoft/amix/workflow
* 移动后: /vekai-appframe/src/main/java/com/vekai/appframe/workflow
* dataform中需要调整的包

```
    com.amarsoft.amix.workflow.handler -> com.vekai.appframe.workflow.handler
```

conf
----
* 移动前：          /amix/src/main/java/com/amarsoft/amix/conf
* 移动后: /vekai-appframe/src/main/java/com/vekai/appframe/conf
* dataform中需要调整的包

```
    com.amarsoft.amix.conf -> com.vekai.appframe.conf
```

common
----
* 移动前：          /amix/src/main/java/com/amarsoft/amix/cmon
* 移动后: /vekai-appframe/src/main/java/com/vekai/appframe/cmon
* dataform中需要调整的包

```
    com.amarsoft.amix.cmon -> com.vekai.appframe.cmon
```