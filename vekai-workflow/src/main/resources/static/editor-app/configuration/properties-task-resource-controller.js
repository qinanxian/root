/**
 * @author 涂汉江(hjtu@amarsoft.com)
 * @date 2017/01/17
 * 流程资源配置控制器
 **/


//引入的Array merge方法，Array.js与editor存在冲突
Array.prototype.merge = function(){
    for(var i = 0; i < arguments.length; i++){
        var array = arguments[i];
        for(var j = 0; j < array.length; j++){
            if(this.indexOf(array[j]) === -1) {
                this.push(array[j]);
            }
        }
    }
    return this;
};

var app = angular.module("activitiModeler");
var _myRootScope;
app.controller("WorkflowTaskResource", function($scope,$http,$filter,$timeout,$injector,$compile, $http){
    var vm = $scope;
    _myRootScope = vm;
    vm.button = {};
    vm.button.actions = [];
    vm.button.links = [];
    vm.subregions = [];
    vm.allSubregions = {};

    //表头
    vm.columnDefs = [{title:'显示',field:'readable'},{title:'只读',field:'writable'},{title:'展开',field:'required'}];


    // 取流程编号
    var key = vm.$parent.$parent.oryxProcessKey;
    if(!key) {
        console.error("获取流程定义key失败");
        return;
    }
    console.log('loading definitionKey:',key);

    //获取流程资源
    var resourcesUrl = KISBPM.URL.getWorkflowResources(key);

    $http({method: 'GET', url: resourcesUrl}).
    success(function (data, status, headers, config) {
        if(!data||data.length==0) {
            alert('请先到资源配置表中,配置资源全集');
            return;
        }
        var dataList = data;
        var formProperties = vm.$parent.formProperties;

        //融合数据
        if(formProperties && formProperties.length){
            formProperties.forEach(function(property){
                for(var i=0;i<dataList.length;i++){
                    if(property.id == dataList[i].id){
                        dataList[i] = jQuery.extend(property,dataList[i]);
                        break;
                    }
                }
            })
        }

        //控制只读与可写的切换
        dataList.forEach(function(item){
            if(item.writable === undefined)
                item = true;
            item.readonly = !item.writable;
        });

        //控制展开与折叠的切换
        dataList.forEach(function(item){
            if(item.required === undefined)
                item = true;
            //item.readonly = !item.required;
        });

        vm.$parent.formPropertiesCopy = dataList;

        vm.button.actions.merge($filter("jsonListFilter")(dataList, 'type', '=="Button"'));//过滤筛选出按钮部分
        vm.button.links.merge($filter("jsonListFilter")(dataList, 'type', '=="Link"')); //过滤筛选出链接部分
        vm.subregions.merge($filter("jsonListFilter")(dataList, 'type', '=="Fieldset"'));//过滤筛选出页面subregion

        console.log(vm.button.actions);
        console.log(vm.button.links);
        console.log(vm.subregions);

        //组装数据结构
        vm.allSubregions =[{
            hidden:'writable',
            title:'按钮',
            expand:"required",
            data:vm.button.actions,
        },{
            expand:"required",
            title:'快速链接',
            data:vm.button.links,
        },{
            title:'信息块',
            data:vm.subregions,
        }];

    }).
    error(function (data, status, headers, config) {
        console.error("获取流程资源出错，请检查");
    });

})