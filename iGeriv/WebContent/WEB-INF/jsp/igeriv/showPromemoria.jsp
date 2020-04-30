<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<div id="mainDiv" style="width: 620px; text-align: center">
	<s:if test="promemoria != null">
		<div class="messaggioDiv" style="text-align: center">
			<s:text name="promemoria.messaggioEscape" />
		</div>
		<s:if test="promemoria.letto eq false">
			<br>
			<br>
			<div style="text-align: left; width: 600px">
				<div
					style="float: left; width: 300px; margin-top: 20px; margin-left: 80px; height: 30px; text-align: center; font-size: 120%;">
					<s:text name="igeriv.segna.come.letto" />
					&nbsp;&nbsp;&nbsp;&nbsp;
					<s:checkbox name="chk1" id="letto" cssClass="tableFields" />
				</div>
				<div id="messageDiv1"
					style="float: right; width: 200px; height: 30px; font-size: 14"></div>
			</div>
		</s:if>
		<s:hidden name="promemoria.codPromemoria" id="codProm" />
	</s:if>
</div>

