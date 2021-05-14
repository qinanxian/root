package com.vekai.crops.customer.controller;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cust/doclist")
public class CustDocListController {

	@Autowired
    MapObjectCruder beanCruder;

	/**
	 * 自然人客户资料清单
	 * @param custId
	 * @return
	 */
	@GetMapping("/indCustDocList/{custId}")
	public List<DocListItem> indCustDocList(@PathVariable("custId") String custId){
		String sql = "SELECT cdi.DOCLIST_ID,cdi.FILE_ID,cdi.ITEM_ID,cdi.ITEM_CODE,cdi.ITEM_NAME,cdg.GROUP_ID,cdg.GROUP_CODE,cdg.GROUP_NAME FROM cmon_doclist_item cdi left join cmon_doclist_group cdg on cdi.GROUP_ID=cdg.GROUP_ID where " +
				"EXISTS(select 1 from cmon_doclist cd where cd.DOCLIST_ID=cdi.DOCLIST_ID and " +
				"cd.OBJECT_ID=:custId) ORDER BY cdi.SORT_CODE ASC, cdi.SORT_CODE ASC";

		List<MapObject> mds = beanCruder.selectList(sql, "custId",custId);

        List<DocListItem> docListItems = new ArrayList<>();

        for (MapObject md:mds) {
            DocListItem docListItem = new DocListItem();
            docListItem.setDoclistId(md.get("doclistId").toString());
            docListItem.setFileId(md.get("fileId")==null?null:md.get("fileId").toString());
            docListItem.setItemId(md.get("itemId").toString());
            docListItem.setItemCode(md.get("itemCode").toString());
            docListItem.setItemName(md.get("itemName").toString());
            docListItem.setGroupId(md.get("groupId").toString());
            docListItem.setGroupCode(md.get("groupCode").toString());
            docListItem.setGroupName(md.get("groupName").toString());

            docListItems.add(docListItem);
        }
        return docListItems;
	}


    @PutMapping("/clearIndCustDocListItem/{itemId}")
    public boolean clearIndCustDocListItem(@PathVariable("itemId") String itemId){
        beanCruder.execute("update cmon_doclist_item set file_id=null where item_id=:itemId", MapKit.mapOf("itemId", itemId));
	    return true;
    }

	class DocListItem{
		private String doclistId;
		private String fileId;
		private String itemId;
		private String itemCode;
		private String itemName;
		private String groupId;
		private String groupCode;
		private String groupName;

        public String getDoclistId() {
            return doclistId;
        }

        public void setDoclistId(String doclistId) {
            this.doclistId = doclistId;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }
}
