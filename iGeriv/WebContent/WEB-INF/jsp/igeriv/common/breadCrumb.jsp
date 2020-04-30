<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="bc" uri="/struts-arianna-tags"%>
<bc:breadcrumbs var='c' status='s'>
	<s:set name="size" value="#s.index" />
</bc:breadcrumbs>
<div
	style="position: relative; width: 100%; z-index: 0; text-align: left; margin-left: 5px; margin-top: 5px;">
	<div id="breadcrumb">
		<ul class="crumbs">
			<bc:breadcrumbs var='c' status='s'>
				<s:url id='url' action="%{action}" namespace="%{namespace}"
					encode="true" escapeAmp="false" />
				<li <s:if test="#s.index eq 0">class="first"</s:if>
					<s:elseif test="#s.index eq #size">class="last"</s:elseif>><a
					href='<s:property value="url"/><s:if test="%{params.size() > 0}">?<s:iterator value="%{params}" var="v" status="s"><s:if test="%{#s.index > 0 && key != 'saved' && key != 'loginExecuted'}">&</s:if><s:if test="%{value != null && value != '' && key != 'saved' && key != 'loginExecuted'}"><s:text name="key"/>=<s:text name="value"/></s:if></s:iterator></s:if>'><s:property
							value='name' /></a></li>
			</bc:breadcrumbs>
		</ul>
	</div>
</div>
