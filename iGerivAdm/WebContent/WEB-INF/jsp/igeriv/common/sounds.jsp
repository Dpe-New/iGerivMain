<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:if test="#session['browserName'] eq constants.BROWSER_NAME_IE || #session['browserName'] eq constants.BROWSER_NAME_SAFARI">
	<embed src="sounds/beep.wav" autostart="false" width="0" height="0" id="beep" enablejavascript="true">
	<embed src="sounds/beep3.wav" autostart="false" width="0" height="0" id="beep3" enablejavascript="true">
</s:if>
<s:else>
	<audio id="beep" controls="controls" style="visibility: hidden;">
	  <source src="sounds/beep.wav" type="audio/wav" />
	</audio>
	<audio id="beep3" controls="controls" style="visibility: hidden;">
	  <source src="sounds/beep3.wav" type="audio/wav" />
	</audio>
</s:else>