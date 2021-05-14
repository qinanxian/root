define(['jquery','Vue'],function($,Vue){
    var detailInfo = {};

    var api = {};
    function toggleGroup(groupDom){
        var legendTitle = $(".rb-legend-title",groupDom);
        var titleIcon = $(".fa",legendTitle);
        if(titleIcon.hasClass('fa-minus')){//表示展开状态，需要收起
            collapseGroup(groupDom);
        }else{
            expandGroup(groupDom);
        }
    };
    function expandGroup(groupDom){
        var legendTitle = $(".rb-legend-title",groupDom);
        var titleIcon = $(".fa",legendTitle);
        if(titleIcon.hasClass('fa-plus')){//表示展开状态，需要收起
            $(".rb-fieldset-body",groupDom).show();
            $(".rb-fieldset-collapse-hint",groupDom).hide();
            titleIcon.addClass("fa-minus").removeClass("fa-plus");
        }
    };
    function collapseGroup(groupDom){
        var legendTitle = $(".rb-legend-title",groupDom);
        var titleIcon = $(".fa",legendTitle);
        if(titleIcon.hasClass('fa-minus')){//表示展开状态，需要收起
            $(".rb-fieldset-body",groupDom).hide();
            $(".rb-fieldset-collapse-hint",groupDom).show();
            titleIcon.addClass("fa-plus").removeClass("fa-minus");
        }
    };

    function create(el,dataModel){
        //如果是jQuery的对象，这里需要做脱壳处理
        if(el instanceof jQuery){
            el = el[0];
        }
        //绑定表单分组的事件
        $(".rb-fieldset",el).delegate(".rb-legend-title,.rb-fieldset-collapse-hint","click", function(){
            toggleGroup($(this).parents(".rb-fieldset"));
        });
       var vm = new Vue({
           el: el,
           data: dataModel
       });

       function setValue(name,value){
           vm.$data[name] = value;
       };

       return {
           setValue:setValue
       };

    }

    //把公开的API放这里来
    detailInfo.create = create;
    return detailInfo;

    //所有标题节点作事件绑定
});