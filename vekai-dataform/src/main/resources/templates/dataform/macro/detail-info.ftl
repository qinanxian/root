<#--<#include "/dataform/macro/fieldset.ftl" />-->
<#--<#include "/dataform/macro/field/field.ftl" />-->
<#--<#include "/dataform/macro/field/text.ftl" />-->
<#--<#include "/dataform/macro/field/radio.ftl" />-->
<#--<#include "/dataform/macro/field/date.ftl" />-->
<#--<#include "/dataform/macro/field/number.ftl" />-->
<#--<#include "/dataform/macro/field/money.ftl" />-->
<#--<#include "/dataform/macro/field/picker.ftl" />-->
<#--<#include "/dataform/macro/field/select.ftl" />-->
<#--<#include "/dataform/macro/field/datemonth.ftl" />-->
<#--<#include "/dataform/macro/field/richtext.ftl" />-->
<#macro detailInfo id="detailInfo">

<div id="${id}">
<link rel="stylesheet" type="text/css" href="${ctxPath}/dataform/css/detail-info.css?${currentTimeMillis}">
    <#nested>
</div>
<#--<script src="${ctxPath}/dataform/js/detail-info.js" type="text/javascript"></script>-->
<script type="text/javascript">
//    load("dataform", "dataform/js/detail-info",["jquery"]);

//    run("dataform",function(){
////        var info = dataform.createInit("#dom1");
////        info.setValue();
//        console.log("running....");
////        console.log(dataform);
//        console.log($);
//        console.log(rober);
//        console.log(dataform);
//    });
</script>
</#macro>
