<html>
<head>
<title>qifu3</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">


</style>


<script type="text/javascript">

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="edit" title="Edit" src="./images/edit.png" onclick="editPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';
	str += '<img alt="edit-Param" title="Edit parameter" src="./images/alert.png" onclick="editParamPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';	
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 			field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Id", 			field: "templateId"		},
		{ name: "title", 		field: "title"			},
		{ name: "Description", 	field: "description"	}
	];
}

function queryClear() {
	$("#id").val('');
	$("#title").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('CORE_PROG001D0004E', parent.getProgUrlForOid('CORE_PROG001D0004E', oid) );
}

function editParamPage(oid) {
	parent.addTab('CORE_PROG001D0004S01Q', parent.getProgUrlForOid('CORE_PROG001D0004S01Q', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./sysTemplateDeleteJson', 
						{ 'oid' : oid }, 
						function(data) {
							if ( _qifu_success_flag != data.success ) {
								parent.notifyWarning( data.message );
							}
							if ( _qifu_success_flag == data.success ) {
								parent.notifyInfo( data.message );
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

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0004Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG001D0004Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('CORE_PROG001D0004A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0004Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management template item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

      <div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="id" value="" id="id" label="Id" placeholder="Enter Id" maxlength="50"></@qifu.textbox>
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="title" value="" id="title" label="Title" placeholder="Enter title" maxlength="100"></@qifu.textbox>
       </div>
      </div>
      
<p style="margin-bottom: 10px"></p>
      
<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();"><i class="icon fa fa-search"></i>&nbsp;Query</button>
<button type="button" class="btn btn-primary" id="btnClear" onclick="queryClear();"><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear</button>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[templateId]'	: $('#id').val(),
		'parameter[title]'		: $('#title').val(),
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysTemplateQueryGridJson" 
	id="CORE_PROG001D0004Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<br/>
<br/>
<br/>

</body>
</html>