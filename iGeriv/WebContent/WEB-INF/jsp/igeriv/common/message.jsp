<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if test="%{message != null}">
	<fieldset style="width: 60%">
		<table width="100%" cellpadding="5" class="tableFields">
			<tr>
				<td align="center"><FONT color=red><s:property
							value="message" /></FONT></td>
			</tr>
		</table>
	</fieldset>
</s:if>