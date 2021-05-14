package com.vekai.appframe.cmon.doclist.service;

import com.vekai.appframe.conf.doclist.service.ConfDocListService;
import com.vekai.appframe.cmon.doclist.model.DoclistObject;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DocListService {

    @Autowired
    private BeanCruder accessor;
    @Autowired
    ConfDocListService confDocListService;

    /**
     * 创建文档实例对象
     * @param doclistCode
     * @param objectType
     * @param objectId
     * @return
     */
    public DoclistObject createDoclist(String doclistCode, String objectType, String objectId){
        DoclistObject doclistObject = null;

        //1.从配置表中查出文档清单配置

        //2.根据配置，生成文档实例对象

        //3.在文档实例表中，保存业务对象和文档实例的关联关系

        return doclistObject;
    }

    public DoclistObject getDoclistObject(String doclistId){
        return null;
    }

    /**
     * 建立文档清单明细项和文件的关联关系
     * @param listItemId
     * @param fileId
     * @return 文件查看码
     */
    public String buildRelation(String listItemId,String fileId){
        return null;
    }

//    public String getDocListIdAndCreateIfNotExist(String doclistCode, String objectId,
//        String objectType) {
//        DocListPO docListPO = initSingleDocList(doclistCode, objectId, objectType);
//        String doclistId = docListPO.getDoclistId();
//        return doclistId;
//    }
//
////    /**
////     * 根据文档清单模板代码,初始化一个具体的业务对象实例的清单
////     *
////     * @param doclistCode
////     * @param objectId
////     * @param objectType
////     * @deprecated
////     * @return
////     */
////    public int initDocList(String doclistCode, String objectId, String objectType) {
////        Integer result = 0;
////        DocListPO docListPO = initSingleDocList(doclistCode, objectId, objectType);
////        String doclistId = docListPO.getDoclistId();
////
////        List<DocListGroupPO> groupPOList = new ArrayList<>();
////        List<ConfDocListGroupPO> confGroupList = confDocListService.getDocListGroups(doclistCode);
////        List<DocListGroupPO> docListGroupList = getDocListGroupPOList(doclistId);
////        if (isListNull(docListGroupList)) {
////            for (ConfDocListGroupPO group : confGroupList) {
////                DocListGroupPO docListGroupPO = new DocListGroupPO();
////                BeanKit.copyProperties(group, docListGroupPO);
////                docListGroupPO.setDoclistId(doclistId);
////                result = accessor.save(docListGroupPO);
////                groupPOList.add(docListGroupPO);
////            }
////        }
////
////        List<DocListItemPO> docListItemPOList = getDocListItemPOList(doclistId);
////        List<ConfDocListItemPO> confItemList = confDocListService.getDocListItems(doclistCode);
////        for (ConfDocListItemPO item : confItemList) {
////            if (!isListNull(docListItemPOList)) {
////                break;
////            }
////            DocListItemPO docListItemPO = new DocListItemPO();
////            BeanKit.copyProperties(item, docListItemPO);
////            docListItemPO.setDoclistId(doclistId);
////            for (DocListGroupPO groupPO : groupPOList) {
//////                if (groupPO.getGroupCode().equals(item.getGroupCode())) {
////                if (groupPO.getGroupCode().equals(item.getItemGroup())) {
////                    docListItemPO.setGroupId(groupPO.getGroupId());
////                }
////            }
////            result += accessor.save(docListItemPO);
////        }
////        return result;
////    }
//
////    /**
////     * 根据文档清单模板代码和分组,初始化一个具体的业务对象实例的清单
////     *
////     * @param doclistCode
////     * @param groupCode
////     * @param objectId
////     * @param objectType
////     * @return
////     */
////    public int initDocList(String doclistCode, String groupCode, String objectId,
////        String objectType) {
////        return initDocListAndFillFileId(doclistCode, groupCode, objectId, objectType, null);
////    }
//
//    /**
//     * 根据文档清单模板代码和分组,初始化一个具体的业务对象实例的清单,如果有预设好的真是文件，则关联起来
//     *
//     * @param doclistCode
//     * @param groupCode
//     * @param objectId
//     * @param objectType
//     * @param fileIdMap
//     * @return
//     */
//    public int initDocListAndFillFileId(String doclistCode, String groupCode, String objectId,
//        String objectType, Map<String, String> fileIdMap) {
//        Integer result = 0;
//        DocListPO docListPO = initSingleDocList(doclistCode, objectId, objectType);
//        String doclistId = docListPO.getDoclistId();
//
//        ConfDocListGroupPO confDocListGroupPO =
//            confDocListService.getDocListGroup(doclistCode, groupCode);
//        DocListGroupPO docListGroupPO = getDocListGroupPO(doclistId, groupCode);
//        if (null == docListGroupPO) {
//            docListGroupPO = new DocListGroupPO();
//            BeanKit.copyProperties(confDocListGroupPO, docListGroupPO);
//            docListGroupPO.setDoclistId(doclistId);
//            result += accessor.save(docListGroupPO);
//        }
//
//        List<DocListItemPO> docListItemPOList =
//            getDocListItemPOList(doclistId, docListGroupPO.getGroupId());
//        if (isListNull(docListItemPOList)) {
//            List<ConfDocListItemPO> confItemList = confDocListService.getDocListItems(doclistCode);
//            for (ConfDocListItemPO item : confItemList) {
////                if (item.getGroupCode().equals(groupCode)) {
//                if (item.getItemGroup().equals(groupCode)) {
//                    DocListItemPO docListItemPO = new DocListItemPO();
//                    BeanKit.copyProperties(item, docListItemPO);
//                    docListItemPO.setDoclistId(doclistId);
//                    docListItemPO.setGroupId(docListGroupPO.getGroupId());
//                    if (null != fileIdMap) {
//                        docListItemPO.setFileId(fileIdMap.get(item.getUniqueCode()));
//                    }
//                    result += accessor.save(docListItemPO);
//                }
//            }
//        }
//        return result;
//    }
//
//    public int initDocList(String doclistCode, List<String> groupCodeList, String objectId,
//        String objectType) {
//        Integer result = 0;
//        for (String groupCode : groupCodeList) {
//            result = initDocList(doclistCode, groupCode, objectId, objectType);
//        }
//        return result;
//    }
//
//    /**
//     * 根据文档清单模板代码和明细,初始化一个具体的业务对象实例的清单
//     *
//     * @param doclistCode
//     * @param itemCode
//     * @param objectId
//     * @param objectType
//     * @return
//     */
//    public DocListPO initSingleDocListItem(String doclistCode, String itemCode, String objectId,
//        String objectType) {
//        Integer result = 0;
//        DocListPO docListPO = initSingleDocList(doclistCode, objectId, objectType);
//        String docListId = docListPO.getDoclistId();
//
//        List<DocListGroupPO> groupPOList = new ArrayList<>();
//        List<ConfDocListGroupPO> confGroupList = confDocListService.getDocListGroups(doclistCode);
//        List<DocListGroupPO> docListGroupList = getDocListGroupPOList(docListId);
//        if (isListNull(docListGroupList)) {
//            for (ConfDocListGroupPO group : confGroupList) {
//                DocListGroupPO docListGroupPO = new DocListGroupPO();
//                BeanKit.copyProperties(group, docListGroupPO);
//                docListGroupPO.setDoclistId(docListId);
//                result += accessor.save(docListGroupPO);
//                groupPOList.add(docListGroupPO);
//            }
//        }
//
//        ConfDocListItemPO confDocListItem =
//            confDocListService.getDocListItem(doclistCode, itemCode);
//
//        if (null != confDocListItem) {
//            DocListGroupPO docListGroupPO = initSingleDocListGroup(confDocListItem.getDoclistCode(),
//                confDocListItem.getItemGroup());
////                confDocListItem.getGroupCode());
//            DocListItemPO docListItemPO = new DocListItemPO();
//            BeanKit.copyProperties(confDocListItem, docListItemPO);
//            docListItemPO.setDoclistId(docListId);
//            if (docListGroupPO != null) {
//                docListGroupPO.setDoclistId(docListId);
//                docListItemPO.setGroupId(docListGroupPO.getGroupId());
//                accessor.save(docListGroupPO);
//            }
//
//            accessor.save(docListItemPO);
//        }
//        return docListPO;
//    }
//
//    /**
//     * 初始化一条doclist，不含group和item）
//     *
//     * @param doclistCode
//     * @param objectId
//     * @param objectType
//     * @return
//     */
//    public DocListPO initSingleDocList(String doclistCode, String objectId, String objectType) {
//        ConfDocListPO confDoclistPO = confDocListService.getDocListByCode(doclistCode);
//        DocListPO docListPO = getDocListPO(objectId, objectType);
//        if (null == docListPO) {
//            docListPO = new DocListPO();
//            docListPO.setObjectId(objectId);
//            docListPO.setObjectType(objectType);
//            BeanKit.copyProperties(confDoclistPO, docListPO);
//            accessor.save(docListPO);
//        }
//        return docListPO;
//    }
//
//    /**
//     * 初始化一条group，不含doclist和item
//     *
//     * @param doclistCode
//     * @param groupCode
//     * @return
//     */
//    public DocListGroupPO initSingleDocListGroup(String doclistCode, String groupCode) {
//        ConfDocListGroupPO confDocListGroupPO =
//            confDocListService.getDocListGroup(doclistCode, groupCode);
//        if (confDocListGroupPO == null)
//            return null;
//        DocListGroupPO docListGroupPO = new DocListGroupPO();
//        BeanKit.copyProperties(confDocListGroupPO, docListGroupPO);
//        accessor.save(docListGroupPO);
//        return docListGroupPO;
//    }
//
//    /**
//     * 初始化一条item，不含doclist和group
//     *
//     * @param doclistCode
//     * @param itemCode
//     * @return
//     */
//    public DocListItemPO initSingleDocListItem(String doclistCode, String itemCode) {
//        ConfDocListItemPO confDocListItem =
//            confDocListService.getDocListItem(doclistCode, itemCode);
//        DocListItemPO docListItemPO = new DocListItemPO();
//        BeanKit.copyProperties(confDocListItem, docListItemPO);
//        accessor.save(docListItemPO);
//        return docListItemPO;
//    }
//
//    /**
//     * 根据文档清单模板代码,分组和流程节点标识,初始化一个具体的业务对象实例的清单
//     *
//     * @param doclistCode
//     * @param groupCode
//     * @param workFlowId
//     * @param objectId
//     * @param objectType
//     * @return
//     */
//    public int initDocList(String doclistCode, String groupCode, String workFlowId, String objectId,
//        String
//            objectType) {
//        return 0;
//    }

//    public List<DocListItemPO> getDocListItemPO(String doclistId) {
//        List<DocListItemPO> docListItemPOList = accessor.selectList(DocListItemPO.class,
//            "SELECT * FROM CMON_DOCLIST_ITEM WHERE DOCLIST_ID =:doclistId",
//            MapKit.mapOf("doclistId", doclistId));
//        return docListItemPOList;
//    }
//
//    public DocListPO getDocListPO(String objectId, String objectType) {
//        DocListPO docListPO =
//            accessor.selectOne(DocListPO.class, "SELECT * FROM CMON_DOCLIST WHERE " +
//                    "OBJECT_ID=:objectId and OBJECT_TYPE=:objectType",
//                MapKit.mapOf("objectId", objectId, "objectType", objectType));
//        return docListPO;
//    }
//
//    public DocListPO getDocListPO(String doclistId) {
//        DocListPO docListPO = accessor.selectOne(DocListPO.class,
//                "SELECT * FROM CMON_DOCLIST WHERE " + "DOCLIST_ID=:doclistId",
//                        MapKit.mapOf("doclistId", doclistId));
//        return docListPO;
//    }
//
//    public DocListGroupPO getDocListGroupPO(String doclistId, String groupCode) {
//        DocListGroupPO docListGroupPO = accessor.selectOne(DocListGroupPO.class, "SELECT * FROM " +
//                "CMON_DOCLIST_GROUP WHERE " +
//                "DOCLIST_ID=:doclistId and GROUP_CODE=:groupCode",
//            MapKit.mapOf("doclistId", doclistId, "groupCode", groupCode));
//        return docListGroupPO;
//    }
//
//    public List<DocListItemPO> getDocListItemPoList(String doclistId, String groupId) {
//        return accessor.selectList(DocListItemPO.class,
//            "SELECT * FROM CMON_DOCLIST_ITEM WHERE DOCLIST_ID=:doclistId and GROUP_ID=:groupId",
//            MapKit.mapOf("doclistId", doclistId, "groupId", groupId));
//    }
//
//    public List<DocListItemPO> getDocListItemPOList(String doclistId) {
//        List<DocListItemPO> itemList = accessor.selectList(DocListItemPO.class, "SELECT * FROM " +
//                "CMON_DOCLIST_ITEM WHERE " +
//                "DOCLIST_ID=:doclistId",
//            MapKit.mapOf("doclistId", doclistId));
//        return itemList;
//    }
//
//
//
//    public List<DocListGroupPO> getDocListGroupPOList(String doclistId) {
//        List<DocListGroupPO> groupList =
//            accessor.selectList(DocListGroupPO.class, "SELECT * FROM " +
//                    "CMON_DOCLIST_GROUP WHERE " +
//                    "DOCLIST_ID=:doclistId",
//                MapKit.mapOf("doclistId", doclistId));
//        return groupList;
//    }
//
//    private List<DocListItemPO> getDocListItemPOList(String doclistId, String groupId) {
//        String sql =
//            "SELECT * FROM CMON_DOCLIST_ITEM WHERE DOCLIST_ID=:doclistId and GROUP_ID=:groupId";
//        List<DocListItemPO> itemList = accessor.selectList(DocListItemPO.class, sql,
//            MapKit.mapOf("doclistId", doclistId, "groupId", groupId));
//        return itemList;
//    }
//
//    private boolean isListNull(List<?> list) {
//        return (null == list || list.size() == 0);
//    }
//
//    public DocListItemPO createDoclistItem(String objectId, String objectType, String groupCode) {
//        DocListPO docListPO = getDocListPO(objectId, objectType);
//        if (docListPO == null) {
//            docListPO = new DocListPO();
//            docListPO.setObjectType(objectType);
//            docListPO.setObjectId(objectId);
//            accessor.save(docListPO);
//        }
//        String doclistId = docListPO.getDoclistId();
//
//        DocListItemPO docListItemPO = new DocListItemPO();
//        docListItemPO.setDoclistId(doclistId);
//        if (!StringKit.isBlank(groupCode)) {
//            DocListGroupPO docListGroupPO = this.getDocListGroupPO(doclistId, groupCode);
//            if (docListGroupPO == null) {
//                docListGroupPO = new DocListGroupPO();
//                docListGroupPO.setGroupCode(groupCode);
//                docListGroupPO.setDoclistId(doclistId);
//                accessor.save(docListGroupPO);
//            }
//            docListItemPO.setGroupId(docListGroupPO.getGroupId());
//        }
//        accessor.save(docListItemPO);
//        return docListItemPO;
//    }
//
//    public void deleteDocListItemFromGroup(String objectId, String objectType, String groupCode) {
//        DocListPO docListPO = getDocListPO(objectId, objectType);
//        if (docListPO == null)
//            return;
//        String doclistId = docListPO.getDoclistId();
//        DocListGroupPO docListGroupPO = this.getDocListGroupPO(doclistId, groupCode);
//        if (docListGroupPO == null)
//            return;
//
//        List<DocListItemPO> docListItems =
//            getDocListItemPoList(doclistId, docListGroupPO.getGroupId());
//        if (!docListItems.isEmpty() && docListItems.size() != 0) {
//            accessor.delete(docListItems);
//        }
//    }
//
//    public DocListGroupPO getDocListGroupPO(String groupId) {
//        String sql = "SELECT * FROM CMON_DOCLIST_GROUP where group_id =:groupId";
//        DocListGroupPO docListGroupPO =
//            accessor.selectOne(DocListGroupPO.class, sql, "groupId", groupId);
//        return docListGroupPO;
//    }
//
//    public DocListItemPO getDocListItemPOByItemId(String itemId) {
//        String sql = "SELECT * FROM CMON_DOCLIST_ITEM where item_id =:itemId";
//        DocListItemPO docListItemPO =
//            accessor.selectOne(DocListItemPO.class, sql, "itemId", itemId);
//        Optional.ofNullable(docListItemPO).orElseThrow(
//            () -> new BizException(MessageHolder.getMessage("", "cmon.docList.item.not.exist")));
//        return docListItemPO;
//    }
//
//    public List<DocListItemPO> getDocListItemPOByLoanIdAndObjectType(String loanId,
//        String objectType) {
//        String sql =
//            "SELECT * FROM CMON_DOCLIST_ITEM DI LEFT JOIN CMON_DOCLIST DL ON DL.DOCLIST_ID=DI"
//                + ".DOCLIST_ID LEFT JOIN CMON_DOCLIST_GROUP DG ON DI.GROUP_ID=DG.GROUP_ID LEFT "
//                + "JOIN CMON_FILE FI ON DI.FILE_ID=FI.FILE_ID where DL.OBJECT_ID=:loanId AND DL"
//                + ".OBJECT_TYPE=:objectType";
//        return accessor.selectList(DocListItemPO.class, sql,
//            MapKit.mapOf("loanId", loanId, "objectType", objectType));
//    }
}
