<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] />

<@sec.authorize access="isAuthenticated()">
    <br/>
    <@sec.authentication property="principal.username"/>
    
	<@sec.authorize access="hasRole('admin')">
    	<br/>hello~~ admin role
	</@sec.authorize>
	<@sec.authorize access="hasAuthority('sysSitePage')">
    	<br/>hello~~ authority
	</@sec.authorize>
</@sec.authorize>
