(function(window) {
	var holder = {};

	function createAt(formId, ctrlId) {
		var formObject = document.getElementById(formId);
		var ctrlEmbed = document.getElementById(ctrlId);
		var lastOpenData = {};

		if (!formObject) {
			alert('表单对象[' + formId + ']在页面中不存在');
			return;
		}
		;
		if (!ctrlEmbed) {
			alert('控件对象[' + ctrlId + ']在页面中不存在');
			return;
		}
		;

		function openURL(url, mode, userName) {
			if (lastOpenData.exists)
				close();
			lastOpenData.exists = true;
			lastOpenData.url = url;
			lastOpenData.mode = mode;
			lastOpenData.userName = userName;
			ctrlEmbed.WebOpen(url, mode, userName);
		}
		;
		function close() {
			lastOpenData.exists = false;
			ctrlEmbed.Close();
		}
		;
		function refresh() {
			if (!lastOpenData.exists) {
				alert('没有打开文件，不能刷新');
				return;
			}
			close();
			openURL(lastOpenData.url, lastOpenData.mode, lastOpenData.userName);
		}
		;
		function getOfficeCtrl() {
			return ctrlEmbed;
		}
		;
		function save() {
			ctrlEmbed.WebSave();
		}
		;
		function addDataItem(name, value) {
			var field = document.createElement('input');
			field.setAttribute('type', 'hidden');
			field.setAttribute('name', name);
			field.value = value;
			formObject.appendChild(field);
		}
		;
		function showRevisions() {
			ctrlEmbed.ShowRevisions = true;
		}
		;
		function hiddenRevisions() {
			ctrlEmbed.ShowRevisions = false;
		}
		;
		function toggleRevisions() {
			ctrlEmbed.ShowRevisions = !ctrlEmbed.ShowRevisions;
		}
		;
		function handDraw() {
			ctrlEmbed.HandDraw.SetPenWidth(5);
			ctrlEmbed.HandDraw.Start();
		}
		;
		function whowHandDraw() {
			ctrlEmbed.HandDraw.ShowLayerBar();
		}
		;
		function acceptAllRevisions() {
			ctrlEmbed.AcceptAllRevisions();
		}
		;
		function insertComment() {
			ctrlEmbed.WordInsertComment();
		}
		;
		function toggleFullScreen() {
			ctrlEmbed.FullScreen = !ctrlEmbed.FullScreen;
		}
		;
		function printPreview() {
			ctrlEmbed.PrintPreview();
		}
		;
		function showDialog(dlgType) {
//			dlgType:
//			1：打开本地
//			3：另存为
//			4：打印
//			5：打印设置
//			6：文件属性
			ctrlEmbed.showDialog(dlgType);
		}
		;

		//公开可操作的接口
		return {
			'openURL' : openURL,
			'close' : close,
			'refresh' : refresh,
			'getOfficeCtrl' : getOfficeCtrl,
			'save' : save,
			'addDataItem' : addDataItem,
			'showRevisions' : showRevisions,
			'hiddenRevisions' : hiddenRevisions,
			'toggleRevisions' : toggleRevisions,
			'handDraw' : handDraw,
			'whowHandDraw' : whowHandDraw,
			'acceptAllRevisions' : acceptAllRevisions,
			'insertComment' : insertComment,
			'toggleFullScreen' : toggleFullScreen,
			'printPreview' : printPreview,
			'showDialog' : showDialog
		};
	}
	;

	holder.createAt = createAt;

	window.WebOffice = holder;
})(window);