<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign qifu=JspTaglibs["http://www.qifu.org/controller/tag"] />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>qifu3</title>

<#import "../common-f-inc.ftl" as cfi>
<@cfi.commonFormHeadResource /> 

</head> 
  
<body bgcolor="#ffffff">

<#import "../common-f-head.ftl" as cfh>
<@cfh.commonFormHeadContent /> 

<@qifu.out escapeHtml="Y" value="qifu_jsVerBuild" />

</body>
</html>
