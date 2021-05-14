<#macro field id name class placeholder required prefix suffix tips note>
<div class="rb-field <#if required!""=="true">rb-field-required</#if> <#if class!"" != "">${class}<#else>rb-field-col-1</#if>" id="rb-field-${id}">
    <#if name??><label class="rb-field-name" for="${id}">${name}</label></#if>
    <div class="rb-field-container">
        <#if prefix!"" != ""><span class="rb-field-addon">${prefix}</span></#if>
        <#nested>
        <#if suffix!"" != ""><span class="rb-field-addon">${suffix}</span></#if>
        <#if note!"" != ""><span class="rb-field-note">${note}</span></#if>

    </div>
</div>
</#macro>