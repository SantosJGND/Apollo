<%@ page import="org.codehaus.groovy.grails.web.json.JSONArray" %>
<nav class="navbar navbar-default">
    %{--<div class="apollo-header row">--}%
        <div class="apollo-header row">
        <g:link uri="/"><asset:image src="ApolloLogo_100x36.png"/></g:link>
        <perms:admin>
            <div class="btn btn-group">
                <button class="btn dropdown-toggle glyphicon glyphicon-list-alt " data-toggle="dropdown">
                    Reports
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu apollo-dropdown">
                    <g:each in="${grailsApplication.config.apollo.administrativePanel}" var="report">
                        <g:if test="${report.type == 'report'}">
                            <li><g:link uri="${report.link}">${report.label}</g:link></li>
                        </g:if>
                    </g:each>
                </ul>
            </div>
        </perms:admin>
    </div>

    %{--<div class="container-fluid">--}%
        <!-- Brand and toggle get grouped for better mobile display -->
        %{--<div class="navbar-header">--}%
            %{--<div class="input-prepend">--}%
                %{--<a class="navbar-brand glyphicon glyphicon-home" href="${createLink(uri: '/')}">Home</a>--}%

            %{--</div>--}%

        %{--</div>--}%
    %{--</div>--}%
</nav>
