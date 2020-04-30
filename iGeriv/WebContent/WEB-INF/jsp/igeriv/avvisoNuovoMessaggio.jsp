<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="textDiv">
	<s:if test="priorita eq 2">
		<s:text name="igeriv.new.message.text" />
		<br>
		<br>
		<span style="font-weight: bold; color: #CC0033"><s:text
				name="igeriv.clicca.per.aprirlo" /></span>
	</s:if>
	<s:elseif test="priorita eq 3">
		<s:text name="igeriv.new.message.text.priorita.massima" />
		<br>
		<br>
		<span style="font-weight: bold; color: #CC0033"><s:text
				name="igeriv.clicca.per.aprirlo" /></span>
	</s:elseif>
</div>
<s:if test="priorita eq 2">
	<div id="divClose"
		style="margin-top: 20px; text-align: right; color: #336699; font-weight: normal; font-size: 80%; cursor: hand;">
		<a href="#" style="text-decoration: none"><s:text
				name='igeriv.chiudi' /></a>
	</div>
</s:if>