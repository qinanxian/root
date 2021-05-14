<#macro money id name class placeholder required prefix suffix tips note>
    <@field id="${id}" name="${name}" class="${class}" placeholder="${placeholder}" required="${required}" prefix="${prefix}" suffix="${suffix}" tips="${tips}" note="${note}">
        <input type="number" id="${id}" name="${id}" autocomplete="off" placeholder="${placeholder}" class="rb-field-input form-control">
    </@field>
</#macro>