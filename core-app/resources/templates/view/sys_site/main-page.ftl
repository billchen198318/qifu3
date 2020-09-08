<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign qifu=JspTaglibs["http://www.qifu.org/controller/tag"] />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>qifu3</title>

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">

</style>

<script type="text/javascript">
function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="edit" title="Edit" src="./images/edit.png" onclick="editPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 	field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Id", 	field: "sysId"					},
		{ name: "Name", field: "name"					},
		{ name: "Host", field: "host"					},
		{ name: "Context path", field: "contextPath"	},
		{ name: "Local", field: "isLocal"				}
	];
}

function queryClear() {
	$("#id").val('');
	$("#name").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('CORE_PROG001D0001E', parent.getProgUrlForOid('CORE_PROG001D0001E', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./core.sysSiteDeleteJson.do', 
						{ 'oid' : oid }, 
						function(data) {
							if ( _qifu_success_flag != data.success ) {
								parent.toastrWarning( data.message );
							}
							if ( _qifu_success_flag == data.success ) {
								parent.toastrInfo( data.message );
							}
							queryGrid();
						}, 
						function() {
							
						},
						_qifu_defaultSelfPleaseWaitShow
				);
			}
	);	
}
</script>

</head> 
  
<body bgcolor="#ffffff">

<@qifu.toolBar 
	id="CORE_PROG001D0001Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG001D0001Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('CORE_PROG001D0001A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0001Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management system site module config item." />
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 



</body>
</html>
