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
	queryGrid();
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 			field: "oid", 		formatter: getQueryGridFormatter },
		{ name: "Permission", 	field: "permission"			},
		{ name: "Type", 		field: "permType"			},
		{ name: "Description", 	field: "description"		}
	];
}

var msgFields = new Object();
msgFields['permission'] 	= 'permission';
msgFields['permType'] 		= 'permissionType';

function saveSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearSave();
	queryGrid();
}

function clearSave() {
	clearWarningMessageField(msgFields);
	$("#permission").val( '' );
	$("#permissionType").val( _qifu_please_select_id );
	$("#description").val( '' );
	clearQueryGridTable();
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./rolePermissionDeleteJson', 
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
	id="CORE_PROG002D0001S01Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG002D0001S01Q', '${role.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG002D0001S01Q');"
	programName="${programName}"
	programId="${programId}"
	description="Settings add or delete role permission item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	Role&nbsp;:&nbsp;${role.role}
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	&nbsp;
        </div>
	</div>        
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="role" value="" id="permission" label="Permission" requiredFlag="Y" maxlength="255" placeholder="Enter permission"></@qifu.textbox>
       </div>
       <div class="col-xs-6 col-md-6 col-lg-6">
       		<@qifu.select dataSource="permTypeMap" name="permissionType" id="permissionType" value="" label="Type" requiredFlag="Y"></@qifu.select>
       </div>
	</div>
</div>
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>	
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();"><i class="icon fa fa-search"></i>&nbsp;Query</button>
		&nbsp;
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./rolePermissionSaveJson"
			xhrParameter="
			{
				'roleOid'		:	'${role.oid}',
				'permission'	:	$('#permission').val(),
				'permType'		:	$('#permissionType').val(),
				'description'	:	$('#description').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[role]'	: '${role.role}',
		'select'			: getQueryGridSelect(),
		'showRow'			: getQueryGridShowRow()	
	}
	"
	xhrUrl="./rolePermissionQueryGridJson" 
	id="CORE_PROG002D0001S01Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<br/>
<br/>
<br/>

</body>
</html>