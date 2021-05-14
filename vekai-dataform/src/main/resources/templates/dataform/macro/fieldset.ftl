<#macro fieldset id name class>
<fieldset class="rb-fieldset" id="${id}">
    <legend class="rb-legend">
        <a class="rb-legend-title" href="javascript:void(0);">
            <i class="fa fa-minus"></i>
            <span class="rb-legend-title-label">${name}</span>
        </a>
    </legend>
    <div class="rb-fieldset-body rb-autofit ${class!'rb-container-col-2'}">
        <#nested>
    </div>
    <#--分组收起时，显示的内容-->
    <div class="rb-fieldset-collapse-hint">
        查看明细，请展开。
    </div>
</fieldset>

</#macro>
