/* Custom method */
function logoutEvent() {
	bootbox.confirm(
			"Logout! are you sure?", 
			function(result){ 
				if (!result) {
					return;
				}
				window.location = qifu_basePath + "logout";
			}
	);	
}
