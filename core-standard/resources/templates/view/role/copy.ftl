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

var msgFields = new Object();
msgFields['role'] 	= 'role';

function saveSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearSave();
}

function clearSave() {
	clearWarningMessageField(msgFields);
	window.location=parent.getProgUrlForOid('CORE_PROG002D0001S02Q', '${role.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG002D0001S02Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG002D0001S02Q', '${role.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.hideModal('CORE_PROG002D0001S02Q');">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			The original Role&nbsp;:&nbsp;${role.role}
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="role" value="" id="role" label="New role" requiredFlag="Y" maxlength="50" placeholder="Enter new role" ></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./roleCopySaveJson"
			xhrParameter="
			{
				'fromRoleOid'	:	'${role.oid}',
				'role'			:	$('#role').val(),
				'description'	:	$('#description').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();"
			selfPleaseWaitShow="Y">
		</@qifu.button>
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

</body>
</html>