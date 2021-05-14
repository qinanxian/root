package com.vekai.base;

import com.vekai.base.menu.model.MenuEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luyu on 2017/12/19.
 */
public class MenuTestData {
    protected static List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
    static {
        MenuEntry home = new MenuEntry();
        home.setId("00");
        home.setName("首页");
        home.setI18nCode("menu.home");
        home.setEnabled("Y");
        home.setSortCode(home.getId());
        menuEntries.add(home);

        MenuEntry bizMenu = null;
        String bizPrefix = "A";
        String bizNamePrefix = "模块";
        for (int i = 10; i < 20; i++) {
            bizMenu = new MenuEntry();
            bizMenu.setId(bizPrefix + i);
            bizMenu.setName(bizNamePrefix + (i - 9));
            bizMenu.setI18nCode("menu.biz.module." + (i - 9));
            bizMenu.setEnabled("Y");
            bizMenu.setSortCode(bizMenu.getId());
            menuEntries.add(bizMenu);
        }

        /*
         * 一级菜单-业务配置
        */
        String bizPropPrefix = "B";
        String bizPropNamePrefix = "配置";
        MenuEntry bizProp = new MenuEntry();
        bizProp.setId(bizPropPrefix + 10);
        bizProp.setName(bizPropNamePrefix);
        bizProp.setI18nCode("menu.biz.props");
        bizProp.setEnabled("Y");
        bizProp.setSortCode(bizProp.getId());
        menuEntries.add(bizProp);

        /*
         * 一级菜单-业务配置
         * 二级菜单-组织架构
        */
        MenuEntry bizOrg = new MenuEntry();
        bizOrg.setId(bizPropPrefix + 10 + "-10");
        bizOrg.setName("组织架构");
        bizOrg.setI18nCode("menu.biz.props.org");
        bizOrg.setEnabled("Y");
        bizOrg.setSortCode(bizOrg.getId());
        menuEntries.add(bizOrg);

        MenuEntry bizDept = new MenuEntry();
        bizDept.setId(bizPropPrefix + 10 + "-10" + "-10");
        bizDept.setName("机构部门");
        bizDept.setI18nCode("menu.biz.props.org.dept");
        bizDept.setEnabled("Y");
        bizDept.setSortCode(bizDept.getId());
        menuEntries.add(bizDept);

        MenuEntry bizUser = new MenuEntry();
        bizUser.setId(bizPropPrefix + 10 + "-10" + "-20");
        bizUser.setName("用户");
        bizUser.setI18nCode("menu.biz.props.org.user");
        bizUser.setEnabled("Y");
        bizUser.setSortCode(bizUser.getId());
        menuEntries.add(bizUser);

        MenuEntry bizRole = new MenuEntry();
        bizRole.setId(bizPropPrefix + 10 + "-10" + "-30");
        bizRole.setName("角色");
        bizRole.setI18nCode("menu.biz.props.org.role");
        bizRole.setEnabled("Y");
        bizRole.setSortCode(bizRole.getId());
        menuEntries.add(bizRole);

        MenuEntry bizPriv = new MenuEntry();
        bizPriv.setId(bizPropPrefix + 10 + "-10" + "-40");
        bizPriv.setName("权限");
        bizPriv.setI18nCode("menu.biz.props.org.priv");
        bizPriv.setEnabled("Y");
        bizPriv.setSortCode(bizPriv.getId());
        menuEntries.add(bizPriv);

        /*
         * 一级菜单-业务配置
         * 二级菜单-流程
        */
        MenuEntry bizFlow = new MenuEntry();
        bizFlow.setId(bizPropPrefix + 10 + "-20");
        bizFlow.setName("流程");
        bizFlow.setI18nCode("menu.biz.props.flow");
        bizFlow.setEnabled("Y");
        bizFlow.setSortCode(bizFlow.getId());
        menuEntries.add(bizFlow);

        MenuEntry bizFlowDesign = new MenuEntry();
        bizFlowDesign.setId(bizPropPrefix + 10 + "-20" + "-10");
        bizFlowDesign.setName("流程设计");
        bizFlowDesign.setI18nCode("menu.biz.props.flow.design");
        bizFlowDesign.setEnabled("Y");
        bizFlowDesign.setSortCode(bizFlowDesign.getId());
        menuEntries.add(bizFlowDesign);

        MenuEntry bizFlowResource = new MenuEntry();
        bizFlowResource.setId(bizPropPrefix + 10 + "-20" + "-20");
        bizFlowResource.setName("流程资源");
        bizFlowResource.setI18nCode("menu.biz.props.flow.resource");
        bizFlowResource.setEnabled("Y");
        bizFlowResource.setSortCode(bizFlowResource.getId());
        menuEntries.add(bizFlowResource);

        /*
         * 一级菜单-业务配置
         * 二级菜单-模版
        */
        MenuEntry bizTemplate = new MenuEntry();
        bizTemplate.setId(bizPropPrefix + 10 + "-30");
        bizTemplate.setName("模版");
        bizTemplate.setI18nCode("menu.biz.props.template");
        bizTemplate.setEnabled("Y");
        bizTemplate.setSortCode(bizTemplate.getId());
        menuEntries.add(bizTemplate);

        MenuEntry bizTemplFile = new MenuEntry();
        bizTemplFile.setId(bizPropPrefix + 10 + "-30" + "-10");
        bizTemplFile.setName("文档模版");
        bizTemplFile.setI18nCode("menu.biz.props.template.file");
        bizTemplFile.setEnabled("Y");
        bizTemplFile.setSortCode(bizTemplFile.getId());
        menuEntries.add(bizTemplFile);

        MenuEntry bizTemplReport = new MenuEntry();
        bizTemplReport.setId(bizPropPrefix + 10 + "-30" + "-20");
        bizTemplReport.setName("格式化报告");
        bizTemplReport.setI18nCode("menu.biz.props.template.report");
        bizTemplReport.setEnabled("Y");
        bizTemplReport.setSortCode(bizTemplReport.getId());
        menuEntries.add(bizTemplReport);

        MenuEntry bizTemplList = new MenuEntry();
        bizTemplList.setId(bizPropPrefix + 10 + "-30" + "-30");
        bizTemplList.setName("文档清单");
        bizTemplList.setI18nCode("menu.biz.props.template.list");
        bizTemplList.setEnabled("Y");
        bizTemplList.setSortCode(bizTemplList.getId());
        menuEntries.add(bizTemplList);

        /*
         * 一级菜单-业务配置
         * 二级菜单-风险探测器
        */
        MenuEntry bizRiskDetector = new MenuEntry();
        bizRiskDetector.setId(bizPropPrefix + 10 + "-40");
        bizRiskDetector.setName("风险探测器");
        bizRiskDetector.setI18nCode("menu.biz.props.riskdetector");
        bizRiskDetector.setEnabled("Y");
        bizRiskDetector.setSortCode(bizRiskDetector.getId());
        menuEntries.add(bizRiskDetector);

        /*
         * 一级菜单-业务监控
        */
        MenuEntry bizMonitor = new MenuEntry();
        bizMonitor.setId(bizPropPrefix + 15);
        bizMonitor.setName("业务监控");
        bizMonitor.setI18nCode("menu.monitor");
        bizMonitor.setEnabled("Y");
        bizMonitor.setSortCode(bizMonitor.getId());
        menuEntries.add(bizMonitor);

        /*
         * 一级菜单-业务监控
         * 二级菜单-流程实例
        */
        MenuEntry bizFlowInstance = new MenuEntry();
        bizFlowInstance.setId(bizPropPrefix + 15 + "-10");
        bizFlowInstance.setName("流程实例");
        bizFlowInstance.setI18nCode("menu.monitor.flowinstance");
        bizFlowInstance.setEnabled("Y");
        bizFlowInstance.setSortCode(bizFlowInstance.getId());
        menuEntries.add(bizFlowInstance);

        /*
         * 一级菜单-业务监控
         * 二级菜单-流程任务
        */
        MenuEntry bizFlowTask = new MenuEntry();
        bizFlowTask.setId(bizPropPrefix + 15 + "-20");
        bizFlowTask.setName("流程任务");
        bizFlowTask.setI18nCode("menu.monitor.flowtask");
        bizFlowTask.setEnabled("Y");
        bizFlowTask.setSortCode(bizFlowTask.getId());
        menuEntries.add(bizFlowTask);

        /*
         * 一级菜单-业务监控
         * 二级菜单-操作记录
        */
        MenuEntry bizOperateRecord = new MenuEntry();
        bizOperateRecord.setId(bizPropPrefix + 15 + "-30");
        bizOperateRecord.setName("流程任务");
        bizOperateRecord.setI18nCode("menu.monitor.operaterecord");
        bizOperateRecord.setEnabled("Y");
        bizOperateRecord.setSortCode(bizOperateRecord.getId());
        menuEntries.add(bizOperateRecord);


        String bizSysPrefix = "C";
        /*
         * 一级菜单-系统
         *
        */
        MenuEntry sys = new MenuEntry();
        sys.setId(bizSysPrefix + 10);
        sys.setName("系统");
        sys.setI18nCode("menu.sys");
        sys.setEnabled("Y");
        sys.setSortCode(sys.getId());
        menuEntries.add(sys);

        /*
         * 一级菜单-系统
         * 二级菜单-系统管理
        */
        MenuEntry sysManager = new MenuEntry();
        sysManager.setId(bizSysPrefix + 10 + "-10");
        sysManager.setName("系统管理");
        sysManager.setI18nCode("menu.sys.manager");
        sysManager.setEnabled("Y");
        sysManager.setSortCode(sysManager.getId());
        menuEntries.add(sysManager);

        MenuEntry sysManagerMenu = new MenuEntry();
        sysManagerMenu.setId(bizSysPrefix + 10 + "-10" + "-10");
        sysManagerMenu.setName("菜单");
        sysManagerMenu.setI18nCode("menu.sys.manager.menu");
        sysManagerMenu.setEnabled("Y");
        sysManagerMenu.setSortCode(sysManagerMenu.getId());
        menuEntries.add(sysManagerMenu);

        MenuEntry sysManagerShowReport = new MenuEntry();
        sysManagerShowReport.setId(bizSysPrefix + 10 + "-10" + "-20");
        sysManagerShowReport.setName("显示报告");
        sysManagerShowReport.setI18nCode("menu.sys.manager.showreport");
        sysManagerShowReport.setEnabled("Y");
        sysManagerShowReport.setSortCode(sysManagerShowReport.getId());
        menuEntries.add(sysManagerShowReport);

        MenuEntry sysManagerDictionary = new MenuEntry();
        sysManagerDictionary.setId(bizSysPrefix + 10 + "-10" + "-30");
        sysManagerDictionary.setName("参数字典");
        sysManagerDictionary.setI18nCode("menu.sys.manager.dictionary");
        sysManagerDictionary.setEnabled("Y");
        sysManagerDictionary.setSortCode(sysManagerDictionary.getId());
        menuEntries.add(sysManagerDictionary);

        MenuEntry sysManagerDataModel = new MenuEntry();
        sysManagerDataModel.setId(bizSysPrefix + 10 + "-10" + "-40");
        sysManagerDataModel.setName("数据模型");
        sysManagerDataModel.setI18nCode("menu.sys.manager.datamodel");
        sysManagerDataModel.setEnabled("Y");
        sysManagerDataModel.setSortCode(sysManagerDataModel.getId());
        menuEntries.add(sysManagerDataModel);

        /*
         * 一级菜单-系统
         * 二级菜单-监控
        */
        MenuEntry sysMonitor = new MenuEntry();
        sysMonitor.setId(bizSysPrefix + 10 + "-20");
        sysMonitor.setName("系统监控");
        sysMonitor.setI18nCode("menu.sys.monitor");
        sysMonitor.setEnabled("Y");
        sysMonitor.setSortCode(sysMonitor.getId());
        menuEntries.add(sysMonitor);

        MenuEntry sysLog = new MenuEntry();
        sysLog.setId(bizSysPrefix + 10 + "-20" + "-10");
        sysLog.setName("系统日志");
        sysLog.setI18nCode("menu.sys.monitor.log");
        sysLog.setEnabled("Y");
        sysLog.setSortCode(sysLog.getId());
        menuEntries.add(sysLog);

        MenuEntry sysRuntimeEnv = new MenuEntry();
        sysRuntimeEnv.setId(bizSysPrefix + 10 + "-20" + "-20");
        sysRuntimeEnv.setName("运行环境");
        sysRuntimeEnv.setI18nCode("menu.sys.monitor.runtimeenv");
        sysRuntimeEnv.setEnabled("Y");
        sysRuntimeEnv.setSortCode(sysRuntimeEnv.getId());
        menuEntries.add(sysRuntimeEnv);

        MenuEntry sysCache = new MenuEntry();
        sysCache.setId(bizSysPrefix + 10 + "-20" + "-30");
        sysCache.setName("缓存");
        sysCache.setI18nCode("menu.sys.monitor.cache");
        sysCache.setEnabled("Y");
        sysCache.setSortCode(sysCache.getId());
        menuEntries.add(sysCache);

        String demoPrefix = "D";
        /*
         * 一级菜单-参考案例
         *
        */
        MenuEntry demo = new MenuEntry();
        demo.setId(demoPrefix + 10);
        demo.setName("参考案例");
        demo.setI18nCode("menu.demo");
        demo.setEnabled("Y");
        demo.setSortCode(demo.getId());
        menuEntries.add(demo);

        /*
         * 一级菜单-参考案例
         * 二级菜单-列表
        */
        MenuEntry demoList = new MenuEntry();
        demoList.setId(demoPrefix + 10 + "-10");
        demoList.setName("列表");
        demoList.setI18nCode("menu.demo.list");
        demoList.setEnabled("Y");
        demoList.setSortCode(demoList.getId());
        menuEntries.add(demoList);

        MenuEntry baseList = new MenuEntry();
        baseList.setId(demoPrefix + 10 + "-10" + "-5");
        baseList.setName("基本列表");
        baseList.setI18nCode("menu.demo.list.base");
        baseList.setEnabled("Y");
        baseList.setSortCode(baseList.getId());
        menuEntries.add(baseList);

        MenuEntry editedList = new MenuEntry();
        editedList.setId(demoPrefix + 10 + "-10" + "-10");
        editedList.setName("可编辑列表");
        editedList.setI18nCode("menu.demo.list.edited");
        editedList.setEnabled("Y");
        editedList.setSortCode(editedList.getId());
        menuEntries.add(editedList);

        MenuEntry editedAndValidateList = new MenuEntry();
        editedAndValidateList.setId(demoPrefix + 10 + "-10" + "-15");
        editedAndValidateList.setName("可编辑列表有校验");
        editedAndValidateList.setI18nCode("menu.demo.list.editedandvalidate");
        editedAndValidateList.setEnabled("Y");
        editedAndValidateList.setSortCode(editedAndValidateList.getId());
        menuEntries.add(editedAndValidateList);

        MenuEntry majorFuncShowList = new MenuEntry();
        majorFuncShowList.setId(demoPrefix + 10 + "-10" + "-20");
        majorFuncShowList.setName("列表主要功能展示");
        majorFuncShowList.setI18nCode("menu.demo.list.majorfunction");
        majorFuncShowList.setEnabled("Y");
        majorFuncShowList.setSortCode(majorFuncShowList.getId());
        menuEntries.add(majorFuncShowList);

        MenuEntry customizedSearchList = new MenuEntry();
        customizedSearchList.setId(demoPrefix + 10 + "-10" + "-25");
        customizedSearchList.setName("列表搜索器定制");
        customizedSearchList.setI18nCode("menu.demo.list.customizedsearch");
        customizedSearchList.setEnabled("Y");
        customizedSearchList.setSortCode(customizedSearchList.getId());
        menuEntries.add(customizedSearchList);

        MenuEntry dataNeedSearchList = new MenuEntry();
        dataNeedSearchList.setId(demoPrefix + 10 + "-10" + "-30");
        dataNeedSearchList.setName("需搜索才列出数据");
        dataNeedSearchList.setI18nCode("menu.demo.list.dataneedsearch");
        dataNeedSearchList.setEnabled("Y");
        dataNeedSearchList.setSortCode(dataNeedSearchList.getId());
        menuEntries.add(dataNeedSearchList);

        MenuEntry dataWithinPrivList = new MenuEntry();
        dataWithinPrivList.setId(demoPrefix + 10 + "-10" + "-35");
        dataWithinPrivList.setName("当前登录用户权限内数据列表");
        dataWithinPrivList.setI18nCode("menu.demo.list.datawithinpriv");
        dataWithinPrivList.setEnabled("Y");
        dataWithinPrivList.setSortCode(dataWithinPrivList.getId());
        menuEntries.add(dataWithinPrivList);

        MenuEntry treeList = new MenuEntry();
        treeList.setId(demoPrefix + 10 + "-10" + "-40");
        treeList.setName("树图表格");
        treeList.setI18nCode("menu.demo.list.tree");
        treeList.setEnabled("Y");
        treeList.setSortCode(treeList.getId());
        menuEntries.add(treeList);

        MenuEntry openDetailList = new MenuEntry();
        openDetailList.setId(demoPrefix + 10 + "-10" + "-45");
        openDetailList.setName("列表展开后打开详情");
        openDetailList.setI18nCode("menu.demo.list.opendetail");
        openDetailList.setEnabled("Y");
        openDetailList.setSortCode(openDetailList.getId());
        menuEntries.add(openDetailList);

        MenuEntry entryList = new MenuEntry();
        entryList.setId(demoPrefix + 10 + "-10" + "-50");
        entryList.setName("条目列表");
        entryList.setI18nCode("menu.demo.list.entry");
        entryList.setEnabled("Y");
        entryList.setSortCode(entryList.getId());
        menuEntries.add(entryList);

        MenuEntry cardList = new MenuEntry();
        cardList.setId(demoPrefix + 10 + "-10" + "-55");
        cardList.setName("卡片列表");
        cardList.setI18nCode("menu.demo.list.card");
        cardList.setEnabled("Y");
        cardList.setSortCode(cardList.getId());
        menuEntries.add(cardList);

        MenuEntry separateDataAndTemplList = new MenuEntry();
        separateDataAndTemplList.setId(demoPrefix + 10 + "-10" + "-60");
        separateDataAndTemplList.setName("模板和数据分离使用");
        separateDataAndTemplList.setI18nCode("menu.demo.list.separatedataandtempl");
        separateDataAndTemplList.setEnabled("Y");
        separateDataAndTemplList.setSortCode(separateDataAndTemplList.getId());
        menuEntries.add(separateDataAndTemplList);

        /*
         * 一级菜单-参考案例
         * 二级菜单-详情
        */
        MenuEntry demoDetail = new MenuEntry();
        demoDetail.setId(demoPrefix + 10 + "-20");
        demoDetail.setName("详情");
        demoDetail.setI18nCode("menu.demo.detail");
        demoDetail.setEnabled("Y");
        demoDetail.setSortCode(demoDetail.getId());
        menuEntries.add(demoDetail);

        MenuEntry simpleDetail = new MenuEntry();
        simpleDetail.setId(demoPrefix + 10 + "-20" + "-10");
        simpleDetail.setName("简单详情");
        simpleDetail.setI18nCode("menu.demo.detail.simple");
        simpleDetail.setEnabled("Y");
        simpleDetail.setSortCode(simpleDetail.getId());
        menuEntries.add(simpleDetail);

        MenuEntry withGroupDetail = new MenuEntry();
        withGroupDetail.setId(demoPrefix + 10 + "-20" + "-20");
        withGroupDetail.setName("详情有分组");
        withGroupDetail.setI18nCode("menu.demo.detail.withgroup");
        withGroupDetail.setEnabled("Y");
        withGroupDetail.setSortCode(withGroupDetail.getId());
        menuEntries.add(withGroupDetail);

        MenuEntry twoColDetail = new MenuEntry();
        twoColDetail.setId(demoPrefix + 10 + "-20" + "-30");
        twoColDetail.setName("详情二列");
        twoColDetail.setI18nCode("menu.demo.detail.twocol");
        twoColDetail.setEnabled("Y");
        twoColDetail.setSortCode(twoColDetail.getId());
        menuEntries.add(twoColDetail);

        MenuEntry threeColDetail = new MenuEntry();
        threeColDetail.setId(demoPrefix + 10 + "-20" + "-40");
        threeColDetail.setName("详情三列");
        threeColDetail.setI18nCode("menu.demo.detail.threecol");
        threeColDetail.setEnabled("Y");
        threeColDetail.setSortCode(threeColDetail.getId());
        menuEntries.add(threeColDetail);

        MenuEntry fourColDetail = new MenuEntry();
        fourColDetail.setId(demoPrefix + 10 + "-20" + "-50");
        fourColDetail.setName("详情四列");
        fourColDetail.setI18nCode("menu.demo.detail.fourcol");
        fourColDetail.setEnabled("Y");
        fourColDetail.setSortCode(fourColDetail.getId());
        menuEntries.add(fourColDetail);

        MenuEntry majorFuncShowDetail = new MenuEntry();
        majorFuncShowDetail.setId(demoPrefix + 10 + "-20" + "-60");
        majorFuncShowDetail.setName("详情主要功能展示");
        majorFuncShowDetail.setI18nCode("menu.demo.detail.majorfunction");
        majorFuncShowDetail.setEnabled("Y");
        majorFuncShowDetail.setSortCode(majorFuncShowDetail.getId());
        menuEntries.add(majorFuncShowDetail);

        MenuEntry separateDataAndTemplDetail = new MenuEntry();
        separateDataAndTemplDetail.setId(demoPrefix + 10 + "-20" + "-70");
        separateDataAndTemplDetail.setName("模板和数据分离使用");
        separateDataAndTemplDetail.setI18nCode("menu.demo.detail.separatedataandtempl");
        separateDataAndTemplDetail.setEnabled("Y");
        separateDataAndTemplDetail.setSortCode(separateDataAndTemplDetail.getId());
        menuEntries.add(separateDataAndTemplDetail);

        /*
         * 一级菜单-参考案例
         * 二级菜单-布局及组件
        */
        MenuEntry layoutAndcomponentDetail = new MenuEntry();
        layoutAndcomponentDetail.setId(demoPrefix + 10 + "-30");
        layoutAndcomponentDetail.setName("布局及组件");
        layoutAndcomponentDetail.setI18nCode("menu.demo.layoutandcomponent");
        layoutAndcomponentDetail.setEnabled("Y");
        layoutAndcomponentDetail.setSortCode(layoutAndcomponentDetail.getId());
        menuEntries.add(layoutAndcomponentDetail);

        MenuEntry treeNavigation = new MenuEntry();
        treeNavigation.setId(demoPrefix + 10 + "-30" + "-5");
        treeNavigation.setName("树图导航");
        treeNavigation.setI18nCode("menu.demo.layoutandcomponent.treenavigation");
        treeNavigation.setEnabled("Y");
        treeNavigation.setSortCode(treeNavigation.getId());
        menuEntries.add(treeNavigation);

        MenuEntry stepBarNavigation = new MenuEntry();
        stepBarNavigation.setId(demoPrefix + 10 + "-30" + "-10");
        stepBarNavigation.setName("步骤条导航");
        stepBarNavigation.setI18nCode("menu.demo.layoutandcomponent.stepbarnavigation");
        stepBarNavigation.setEnabled("Y");
        stepBarNavigation.setSortCode(stepBarNavigation.getId());
        menuEntries.add(stepBarNavigation);

        MenuEntry labelPage = new MenuEntry();
        labelPage.setId(demoPrefix + 10 + "-30" + "-15");
        labelPage.setName("标签页");
        labelPage.setI18nCode("menu.demo.layoutandcomponent.labelpage");
        labelPage.setEnabled("Y");
        labelPage.setSortCode(labelPage.getId());
        menuEntries.add(labelPage);

        MenuEntry selector = new MenuEntry();
        selector.setId(demoPrefix + 10 + "-30" + "-20");
        selector.setName("选择器");
        selector.setI18nCode("menu.demo.layoutandcomponent.selector");
        selector.setEnabled("Y");
        selector.setSortCode(selector.getId());
        menuEntries.add(selector);

        MenuEntry notification = new MenuEntry();
        notification.setId(demoPrefix + 10 + "-30" + "-25");
        notification.setName("通知以及提示");
        notification.setI18nCode("menu.demo.layoutandcomponent.notification");
        notification.setEnabled("Y");
        notification.setSortCode(notification.getId());
        menuEntries.add(notification);

        MenuEntry dialog = new MenuEntry();
        dialog.setId(demoPrefix + 10 + "-30" + "-30");
        dialog.setName("模态框以及对话框");
        dialog.setI18nCode("menu.demo.layoutandcomponent.dialog");
        dialog.setEnabled("Y");
        dialog.setSortCode(dialog.getId());
        menuEntries.add(dialog);

        MenuEntry form = new MenuEntry();
        form.setId(demoPrefix + 10 + "-30" + "-35");
        form.setName("表单组件");
        form.setI18nCode("menu.demo.layoutandcomponent.form");
        form.setEnabled("Y");
        form.setSortCode(form.getId());
        menuEntries.add(form);

        MenuEntry icon = new MenuEntry();
        icon.setId(demoPrefix + 10 + "-30" + "-40");
        icon.setName("图标库");
        icon.setI18nCode("menu.demo.layoutandcomponent.icon");
        icon.setEnabled("Y");
        icon.setSortCode(icon.getId());
        menuEntries.add(icon);

        MenuEntry uploadAndDownload = new MenuEntry();
        uploadAndDownload.setId(demoPrefix + 10 + "-30" + "-45");
        uploadAndDownload.setName("文件及图片的上传下载");
        uploadAndDownload.setI18nCode("menu.demo.layoutandcomponent.uploadanddownload");
        uploadAndDownload.setEnabled("Y");
        uploadAndDownload.setSortCode(uploadAndDownload.getId());
        menuEntries.add(uploadAndDownload);

        MenuEntry sceneWithiframe = new MenuEntry();
        sceneWithiframe.setId(demoPrefix + 10 + "-30" + "-50");
        sceneWithiframe.setName("IFRAME的使用场景");
        sceneWithiframe.setI18nCode("menu.demo.layoutandcomponent.scenewithiframe");
        sceneWithiframe.setEnabled("Y");
        sceneWithiframe.setSortCode(sceneWithiframe.getId());
        menuEntries.add(sceneWithiframe);


        /*
         * 一级菜单-参考案例
         * 二级菜单-基础服务
        */
        MenuEntry baseService = new MenuEntry();
        baseService.setId(demoPrefix + 10 + "-40");
        baseService.setName("基础服务");
        baseService.setI18nCode("menu.demo.baseservice");
        baseService.setEnabled("Y");
        baseService.setSortCode(baseService.getId());
        menuEntries.add(baseService);

        MenuEntry restCall = new MenuEntry();
        restCall.setId(demoPrefix + 10 + "-40" + "-10");
        restCall.setName("rest调用");
        restCall.setI18nCode("menu.demo.baseservice.restcall");
        restCall.setEnabled("Y");
        restCall.setSortCode(restCall.getId());
        menuEntries.add(restCall);

        MenuEntry sceneWithRest = new MenuEntry();
        sceneWithRest.setId(demoPrefix + 10 + "-40" + "-20");
        sceneWithRest.setName("rest传参场景");
        sceneWithRest.setI18nCode("menu.demo.baseservice.scenewithrest");
        sceneWithRest.setEnabled("Y");
        sceneWithRest.setSortCode(sceneWithRest.getId());
        menuEntries.add(sceneWithRest);

        MenuEntry internationalization = new MenuEntry();
        internationalization.setId(demoPrefix + 10 + "-40" + "-30");
        internationalization.setName("国际化支持");
        internationalization.setI18nCode("menu.demo.baseservice.internationalization");
        internationalization.setEnabled("Y");
        internationalization.setSortCode(internationalization.getId());
        menuEntries.add(internationalization);



        /*
         * 一级菜单-参考案例
         * 二级菜单-OFFICE
        */
        MenuEntry office = new MenuEntry();
        office.setId(demoPrefix + 10 + "-50");
        office.setName("OFFICE");
        office.setI18nCode("menu.demo.office");
        office.setEnabled("Y");
        office.setSortCode(office.getId());
        menuEntries.add(office);

        MenuEntry editeOnline = new MenuEntry();
        editeOnline.setId(demoPrefix + 10 + "-50" + "-10");
        editeOnline.setName("在线编辑");
        editeOnline.setI18nCode("menu.demo.office.editeonline");
        editeOnline.setEnabled("Y");
        editeOnline.setSortCode(editeOnline.getId());
        menuEntries.add(editeOnline);

        MenuEntry toPDFAndShow = new MenuEntry();
        toPDFAndShow.setId(demoPrefix + 10 + "-50" + "-20");
        toPDFAndShow.setName("转为PDF并展示");
        toPDFAndShow.setI18nCode("menu.demo.office.topdfandshow");
        toPDFAndShow.setEnabled("Y");
        toPDFAndShow.setSortCode(toPDFAndShow.getId());
        menuEntries.add(toPDFAndShow);

        MenuEntry otherFormatFile = new MenuEntry();
        otherFormatFile.setId(demoPrefix + 10 + "-50" + "-30");
        otherFormatFile.setName("其他格式附件展示");
        otherFormatFile.setI18nCode("menu.demo.office.otherformatfile");
        otherFormatFile.setEnabled("Y");
        otherFormatFile.setSortCode(otherFormatFile.getId());
        menuEntries.add(otherFormatFile);

        MenuEntry expExcelOnList = new MenuEntry();
        expExcelOnList.setId(demoPrefix + 10 + "-50" + "-40");
        expExcelOnList.setName("LIST页面导出EXCEL");
        expExcelOnList.setI18nCode("menu.demo.office.exportexcelonlist");
        expExcelOnList.setEnabled("Y");
        expExcelOnList.setSortCode(expExcelOnList.getId());
        menuEntries.add(expExcelOnList);

        MenuEntry impExcelOnListWeb = new MenuEntry();
        impExcelOnListWeb.setId(demoPrefix + 10 + "-50" + "-50");
        impExcelOnListWeb.setName("LIST页面导入EXCEL（前端）");
        impExcelOnListWeb.setI18nCode("menu.demo.office.impexcelonlistfront");
        impExcelOnListWeb.setEnabled("Y");
        impExcelOnListWeb.setSortCode(impExcelOnListWeb.getId());
        menuEntries.add(impExcelOnListWeb);

        MenuEntry impExcelOnListServer = new MenuEntry();
        impExcelOnListServer.setId(demoPrefix + 10 + "-50" + "-60");
        impExcelOnListServer.setName("LIST页面导入EXCEL（后台）");
        impExcelOnListServer.setI18nCode("menu.demo.office.impexcelonlistserver");
        impExcelOnListServer.setEnabled("Y");
        impExcelOnListServer.setSortCode(impExcelOnListServer.getId());
        menuEntries.add(impExcelOnListServer);

        MenuEntry expExcelOnInfo = new MenuEntry();
        expExcelOnInfo.setId(demoPrefix + 10 + "-50" + "-70");
        expExcelOnInfo.setName("INFO页面导出");
        expExcelOnInfo.setI18nCode("menu.demo.office.expexceloninfo");
        expExcelOnInfo.setEnabled("Y");
        expExcelOnInfo.setSortCode(expExcelOnInfo.getId());
        menuEntries.add(expExcelOnInfo);

        MenuEntry impExcelOnInfoWeb = new MenuEntry();
        impExcelOnInfoWeb.setId(demoPrefix + 10 + "-50" + "-80");
        impExcelOnInfoWeb.setName("INFO页面导入(前端）");
        impExcelOnInfoWeb.setI18nCode("menu.demo.office.impexceloninfoweb");
        impExcelOnInfoWeb.setEnabled("Y");
        impExcelOnInfoWeb.setSortCode(impExcelOnInfoWeb.getId());
        menuEntries.add(impExcelOnInfoWeb);

        /*
         * 一级菜单-参考案例
         * 二级菜单-常见组合
        */
        MenuEntry commonCombination = new MenuEntry();
        commonCombination.setId(demoPrefix + 10 + "-60");
        commonCombination.setName("常见组合");
        commonCombination.setI18nCode("menu.demo.commoncombination");
        commonCombination.setEnabled("Y");
        commonCombination.setSortCode(commonCombination.getId());
        menuEntries.add(commonCombination);

        MenuEntry listRelatedDetail = new MenuEntry();
        listRelatedDetail.setId(demoPrefix + 10 + "-60" + "-10");
        listRelatedDetail.setName("列表详情联动");
        listRelatedDetail.setI18nCode("menu.demo.commoncombination.listrelateddetail");
        listRelatedDetail.setEnabled("Y");
        listRelatedDetail.setSortCode(listRelatedDetail.getId());
        menuEntries.add(listRelatedDetail);

        MenuEntry listIntegrationDetail = new MenuEntry();
        listIntegrationDetail.setId(demoPrefix + 10 + "-60" + "-20");
        listIntegrationDetail.setName("详情列表整合");
        listIntegrationDetail.setI18nCode("menu.demo.commoncombination.listintegrationdetail");
        listIntegrationDetail.setEnabled("Y");
        listIntegrationDetail.setSortCode(listIntegrationDetail.getId());
        menuEntries.add(listIntegrationDetail);

        MenuEntry customize = new MenuEntry();
        customize.setId(demoPrefix + 10 + "-60" + "-30");
        customize.setName("动态分组并整合列表详情以及自定义");
        customize.setI18nCode("menu.demo.commoncombination.customize");
        customize.setEnabled("Y");
        customize.setSortCode(customize.getId());
        menuEntries.add(customize);

        /*
         * 一级菜单-参考案例
         * 二级菜单-典型业务场景
        */
        MenuEntry typicalBizScene = new MenuEntry();
        typicalBizScene.setId(demoPrefix + 10 + "-70");
        typicalBizScene.setName("典型业务场景");
        typicalBizScene.setI18nCode("menu.demo.typicalbizscene");
        typicalBizScene.setEnabled("Y");
        typicalBizScene.setSortCode(typicalBizScene.getId());
        menuEntries.add(typicalBizScene);

    }
}
