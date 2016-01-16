<%@ include file="../../../../includes.jspf"%>
<c:url var="create_soap_mock_response_url"  value="/web/soap/project/${soapProjectId}/port/${soapPortId}/operation/${soapOperation.id}/create/response" />
<div class="content-top">
    <h1><spring:message code="soap.createsoapmockresponse.header.createmockresponse"/></h1>
</div>
<form:form action="${create_soap_mock_response_url}" method="POST">
    <div class="content-summary">
        <table class="formTable">
            <tr>
                <td class="column1"><form:label path="name"><spring:message code="soap.createsoapmockresponse.label.name"/></form:label></td>
                <td class="column2"><form:input path="name" id="soapMockResponseNameInput"/></td>
            </tr>
            <tr>
                <td class="column1"><form:label path="httpStatusCode"><spring:message code="soap.createsoapmockresponse.label.httpstatuscode"/></form:label></td>
                <td class="column2"><form:input path="httpStatusCode" id="soapMockResponseHttpStatusCodeInput"/></td>
            </tr>
            <tr>
                <td class="column1"><form:label path="contentType"><spring:message code="soap.createsoapmockresponse.label.httpcontenttype"/></form:label></td>
                <td class="column2"><form:input path="contentType" id="soapMockResponseHttpContentTypeInput"/></td>
            </tr>
        </table>
    </div>
    <div>
        <h2 class="decorated"><span><spring:message code="soap.createsoapmockresponse.header.body"/></span></h2>
        <div class="editor">
            <form:textarea id="body" path="body"/>
        </div>
    </div>

    <button class="button-success pure-button" type="submit" name="submit"><i class="fa fa-plus"></i>  <span><spring:message code="soap.createsoapmockresponse.button.createmockresponse"/></span></button>
    <a href="<c:url value="/web/soap/project/${soapProjectId}/port/${soapPortId}/operation/${soapOperation.id}"/>" class="button-error pure-button"><i class="fa fa-times"></i> <span><spring:message code="soap.createsoapmockresponse.button.cancel"/></span></a>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>
<script>
    $("#soapMockResponseNameInput").attr('required', '');
    $("#soapMockResponseHttpStatusCodeInput").attr('required', '');
    $("#soapMockResponseHttpContentTypeInput").attr('required', '');
</script>