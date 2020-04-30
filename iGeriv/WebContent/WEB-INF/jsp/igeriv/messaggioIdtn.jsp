<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div class="tableFields"
	style="width: 100%; align: center; text-align: center;">
	<div style="width: 750px; text-align: center; margin-bottom: 5px;">
		<font color="#0"> <b> <s:text
					name="igeriv.messaggio.copertina" />
		</b>
		</font>
	</div>
	<div style="width: 750px; text-align: center; margin-bottom: 20px;">
		<font color="#0"> <s:property
				value="messaggioIdtnVo.storicoCopertineVo.codicePubblicazione" />&nbsp;-&nbsp;
			<s:property
				value="messaggioIdtnVo.storicoCopertineVo.anagraficaPubblicazioniVo.titolo" />&nbsp;-&nbsp;
			<s:property
				value="messaggioIdtnVo.storicoCopertineVo.numeroCopertina" />
		</font>
	</div>
	<div
		style="width: 750px; heigth: 300px; text-align: center; margin-bottom: 20px;">
		<div id="innerDiv" style="width: 750px; text-align: center;"></div>
	</div>
	<s:if test="messaggioIdtnVo.attachmentName1 != null">
		<div class="required" style="height: 25px">
			<div style="float: left; width: 100px;">
				<font color="#0"><s:text name="igeriv.allegato1" /></font>
			</div>
			<s:url id="att1" value="attachment.action" encode="true">
				<s:param name="fileName" value="%{messaggioIdtnVo.attachmentName1}" />
			</s:url>
			<div style="float: left; width: 220px;">
				<a href="<s:property value="att1"/>" target="new" class="textLinks"><s:property
						value="messaggioIdtnVo.attachmentName1" /></a>
			</div>
		</div>
	</s:if>
	<s:if test="messaggioIdtnVo.attachmentName2 != null">
		<div class="required" style="height: 25px">
			<div style="float: left; width: 100px;">
				<font color="#0"><s:text name="igeriv.allegato2" /></font>
			</div>
			<s:url id="att2" value="attachment.action" encode="true">
				<s:param name="fileName" value="%{messaggioIdtnVo.attachmentName2}" />
			</s:url>
			<div style="float: left; width: 220px;">
				<a href="<s:property value="att2"/>" target="new" class="textLinks"><s:property
						value="messaggioIdtnVo.attachmentName2" /></a>
			</div>
		</div>
	</s:if>
	<s:if test="messaggioIdtnVo.attachmentName3 != null">
		<div class="required" style="height: 25px">
			<div style="float: left; width: 100px;">
				<font color="#0"><s:text name="igeriv.allegato3" /></font>
			</div>
			<s:url id="att3" value="attachment.action" encode="true">
				<s:param name="fileName" value="%{messaggioIdtnVo.attachmentName3}" />
			</s:url>
			<div style="float: left; width: 220px;">
				<a href="<s:property value="att3"/>" target="new" class="textLinks"><s:property
						value="messaggioIdtnVo.attachmentName3" /></a>
			</div>
		</div>
	</s:if>
	<input type="hidden" id="testo"
		value="<s:property value="messaggioIdtnVo.testo" />" />
</div>