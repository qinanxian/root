<#macro select id name class placeholder required prefix suffix tips note>
    <@field id="${id}" name="${name}" class="${class}" placeholder="${placeholder}" required="${required}" prefix="${prefix}" suffix="${suffix}" tips="${tips}" note="${note}">
        <select class="form-control" id="${id}" name="${id}">
            <#nested>
        </select>
    </@field>
</#macro>