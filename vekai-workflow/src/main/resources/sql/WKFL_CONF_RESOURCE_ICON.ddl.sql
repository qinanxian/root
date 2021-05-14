update WKFL_CONF_RESOURCE set ICON_='fa-send-o' where RESOURCE_ID='WBSubmit' or RESOURCE_ID='WBSubmitAssign';
update WKFL_CONF_RESOURCE set ICON_='fa-rotate-left' where RESOURCE_ID='WBBackOrigin' or RESOURCE_ID='WBBackOriginAssignee';
update WKFL_CONF_RESOURCE set ICON_='minuscircleo' where RESOURCE_ID='WBReject';
update WKFL_CONF_RESOURCE set ICON_='fa-backward' where RESOURCE_ID='WBBackTrack' or RESOURCE_ID='WBBackTrackAssignee';
update WKFL_CONF_RESOURCE set ICON_='fa-mail-forward' where RESOURCE_ID='WBDeliverTo';
update WKFL_CONF_RESOURCE set ICON_='fa-phone' where RESOURCE_ID='WBTakeAdvice';
update WKFL_CONF_RESOURCE set ICON_='fa-share-alt' where RESOURCE_ID='WBNotify';
update WKFL_CONF_RESOURCE set ICON_='closecircleo' where RESOURCE_ID='WBAbolished';
update WKFL_CONF_RESOURCE set ICON_='roic-workflow' where RESOURCE_ID='WBViewFlowGraph';


