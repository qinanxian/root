<#macro picker id name class placeholder required prefix suffix tips note pickername pickerclass>
    <@field id="${id}" name="${name}" class="${class}" placeholder="${placeholder}" required="${required}" prefix="${prefix}" suffix="${suffix}" tips="${tips}" note="${note}">
        <input type="text" id="${id}" name="${id}" autocomplete="off" placeholder="${placeholder}" class="rb-field-input form-control">
        <button class="btn ${pickerclass!"btn-default"} rb-field-addon" onclick="javascript:void(0)">${pickername!"请选择..."}</button>
    </@field>
</#macro>