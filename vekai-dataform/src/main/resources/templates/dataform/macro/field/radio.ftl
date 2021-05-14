<#macro radio id name class placeholder required prefix suffix tips note>
    <@field id="${id}" name="${name}" class="${class}" placeholder="${placeholder}" required="${required}" prefix="${prefix}" suffix="${suffix}" tips="${tips}" note="${note}">
        <#nested>
    </@field>
</#macro>