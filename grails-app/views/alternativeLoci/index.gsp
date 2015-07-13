
<%@ page import="org.bbop.apollo.AlternativeLoci" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'availableStatus.label', default: 'AlternativeLoci')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-availableStatus" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-availableStatus" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="value" title="${message(code: 'availableStatus.name.label', default: 'Value')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${alternativeLociInstanceList}" status="i" var="alternativeLociInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${alternativeLociInstance.id}">${fieldValue(bean: alternativeLociInstance, field: "name")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${alternativeLociInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
