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

$( document ).ready(function() {
	
	
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="edit" title="Edit" src="./images/edit.png" onclick="editPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';		
	str += '<img alt="expression" title="Expression" src="./images/alert.png" onclick="editExpressionPage(\'' + value + '\');"/>';	
	str += '&nbsp;&nbsp;';		
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 			field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "System", 		field: "system"			},
		{ name: "Bean", 		field: "beanId"			},
		{ name: "Method",		field: "method"			},
		{ name: "Enable",		field: "enableFlag"		},
		{ name: "Description",	field: "description"	}
	];
}

function queryClear() {
	$("#systemOid").val( _qifu_please_select_id );
	$("#beanId").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('CORE_PROG003D0003E', parent.getProgUrlForOid('CORE_PROG003D0003E', oid) );
}

function editExpressionPage(oid) {
	parent.addTab('CORE_PROG003D0003S01Q', parent.getProgUrlForOid('CORE_PROG003D0003S01Q', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./sysBeanSupportDeleteJson', 
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
	id="CORE_PROG003D0003Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG003D0003Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('CORE_PROG003D0003A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0003Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management service bean support item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

      <div class="row">     
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.select dataSource="sysMap" name="systemOid" id="systemOid" value="" label="System"></@qifu.select>
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="beanId" value="" id="beanId" label="Bean Id" placeholder="Enter Service/Bean Id" maxlength="255"></@qifu.textbox>
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
		'parameter[systemOid]'	: $('#systemOid').val(),
		'parameter[beanId]'		: $('#beanId').val(),
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysBeanSupportQueryGridJson" 
	id="CORE_PROG003D0003Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<br/>
<br/>
<br/>

</body>
</html>