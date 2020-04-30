<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<!-- <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/> -->
<style>
		div#filter { height:20px;} 
		div#content1 { height:550px;} 
</style> 
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="MessageForm" action="messages_saveMessage.action" method="POST" enctype="multipart/form-data" theme="igeriv" validate="true" onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="text-align:left; width:750px"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div style="width:750px;">	
				<div class="required" style="width:750px;">
					<div style="float:left; width:30%; vertical-align:center;"><s:text name="igeriv.data.ora.messaggio" /></div>
					<div>
						<div style="float:left; margin-top:0px;">
							<s:textfield name="strDataMessaggio" id="strDataMessaggio" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>
						</div>
						<div style="float:left; margin-top:0px;">
							<s:select label=""
							    name="oraMessaggio"
							    id="oraMessaggio" 
							    listKey="key" 
							    listValue="value"
							    list="#{'00':'00', '01':'01', '02':'02', '03':'03', '04':'04', '05':'05', '06':'06', '07':'07', '08':'08', '09':'09', '10':'10', '11':'11', '12':'12', '13':'13', '14':'14', '15':'15', '16':'16', '17':'17', '18':'18', '19':'19', '20':'20', '21':'21', '22':'22', '23':'23'}"
							    emptyOption="false" 
							    cssStyle="width: 50px"
						    />
						</div>
						<div style="float:left; margin-top:0px;">
							<s:select label=""
							    name="minutoMessaggio"
							    id="minutoMessaggio" 
							    listKey="key" 
							    listValue="value"
							    list="#{'00':'00', '01':'01', '02':'02', '03':'03', '04':'04', '05':'05', '06':'06', '07':'07', '08':'08', '09':'09', '10':'10', '11':'11', '12':'12', '13':'13', '14':'14', '15':'15', '16':'16', '17':'17', '18':'18', '19':'19', '20':'20', '21':'21', '22':'22', '23':'23', '24':'24', '25':'25', '26':'26', '27':'27', '28':'28', '29':'29', '30':'30', '31':'31', '32':'32', '33':'33', '34':'34', '35':'35', '36':'36', '37':'37', '38':'38', '39':'39', '40':'40', '41':'41', '42':'42', '43':'43', '44':'44', '45':'45', '46':'46', '47':'47', '48':'48', '49':'49', '50':'50', '51':'51', '52':'52', '53':'53', '54':'54', '55':'55', '56':'56', '57':'57', '58':'58', '59':'59'}"
							    emptyOption="false" 
							    cssStyle="width: 50px"
						    />
						</div>
						<div style="white-space:nowrap;">
							&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="sysdate" id="sysdate" value='true'>&nbsp;&nbsp;<s:text name="igeriv.sysdate"/>&nbsp;
						</div>
					</div>
				</div>
				<div style="width:750px; height:30px;">
					<div style="float:left; width:30%;"><s:text name="igeriv.destinatario" /></div>
					<div style="float:left; font-size:11px; white-space:nowrap;">
						<input type="radio" name="destinatario" id="destinatario1" value='0' <s:if test="0 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.tutte.rivendite"/>&nbsp;												
						<input type="radio" name="destinatario" id="destinatario2" value='1' <s:if test="1 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.rivendita.singola"/>&nbsp;
						<input type="radio" name="destinatario" id="destinatario4" value='4' <s:if test="4 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.rivendite.multiple"/>&nbsp;
						<input type="radio" name="destinatario" id="destinatario3" value='2' <s:if test="2 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.giri"/>&nbsp;
						<input type="radio" name="destinatario" id="destinatario4" value='3' <s:if test="3 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.zone"/>
					</div>				
				</div>		 
				<div id="rivenditeDiv" style="width:700px;">
					<div style="float:left; width:30%;"><s:text name="igeriv.cod.rivendita.dl.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
					<div style="float:right; height:50px;"><s:textfield id="edicolaLabel" name="messaggio.edicolaLabel" cssStyle="width: 350px" cssClass="tableFields" /></div>				
				</div>
				<div id="giriDiv" class="required" style="width:750px;">
					<div style="float:left; width:30%;">
						<div style="float:left; width:80px;"><s:text name="igeriv.giro.tipo" /></div>
						<div style="float:right; width:120px;"><s:textfield id="giroTipo" name="giroTipo" cssStyle="width:100px" cssClass="tableFields" /></div>
					</div>
					<div id="giriList" style="float:left; margin-left:30px; height:100px; overflow-y:scroll; text-align:center">
						
					</div>
				</div>
				<div id="zoneDiv" class="required" style="width:750px;">
					<div style="float:left; width:30%; height:100px;">
						<div style="float:left; width:80px;"><s:text name="igeriv.zona.tipo" /></div>
						<div style="float:right; width:120px;"><s:textfield id="zonaTipo" name="zonaTipo" cssStyle="width:100px" cssClass="tableFields" /></div>
					</div>
					<div id="zoneList" style="float:left; margin-left:30px; height:100px; overflow-y:scroll; text-align:center">
						
					</div>
				</div>	
				<div id="rivenditeMultipleDiv" class="required" style="width:750px;">
					<div style="float:left; width:60%; height:100px;">
						<div style="float:left; width:40%;"><s:text name="igeriv.cod.rivendita.dl.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
						<div style="float:right;"><s:textfield id="riv" name="riv" cssStyle="width:250px; font-size:10px;" cssClass="tableFields" /></div>
					</div>
					<div id="rivenditeList" style="float:left; margin-left:20px; height:100px; text-align:center">
						<s:select id="rivenditeSel" name="rivenditeSel" list="#{}" multiple="true" cssStyle="width:240px; height:100px; font-size:10px;"></s:select>
					</div>
				</div>
				<div class="required" style="width:750px;">
					<div style="float:left; width:30%; margin-top:10px;"><s:text name="igeriv.tipo.messaggio" /></div>	
					<div style="float:left;">
						<s:select label=""
						    name="messaggio.tipoMessaggio"
						    id="messageType" 
						    listKey="key" 
						    listValue="value"
						    list="%{#session['statiTipoMessaggi']}"
						    emptyOption="false" 
						    cssStyle="width: 320px"
					    />
					</div>				
				</div>
				<div style="float:left; width:750px; height:10px; text-align:center;"></div>	
				<div style="width:750px; heigth:300px; text-align:center;">
					<div id="innerDiv" style="750px; text-align:center;"></div>											
				</div>	
				<div style="width:750px;">
<%-- 					<s:if test="messaggio.attachmentName1 == null || messaggio.attachmentName1 == ''"> --%>
						<div id="attachment1_inputfile">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato1" /></div>
								<div style="float:left; width:220px;"><s:file name="attachment1" label="File" /></div>			
								<div style="float:right; width:430px; text-align:right; vertical-align:middle;"><font color="red"><span id="err_attachment1"><s:fielderror><s:param>attachment1</s:param></s:fielderror></span></font></div>		
							</div>
						</div>	
<%-- 					</s:if>	 --%>
<%-- 					<s:else> --%>
						<div id="attachment1_viewfile">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato1" /></div>
								<div style="float:left; width:320px;"><a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName1" />" target="new" class="textLinks"><s:text name="messaggio.attachmentName1" /></a></div>
								<div id="delete1" style="float:right; width:20px; margin-right:200px; text-align:left; cursor: pointer"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>					
							</div>
						</div>	
<%-- 					</s:else> --%>
					
<%-- 					<s:if test="messaggio.attachmentName2 == null || messaggio.attachmentName2 == ''"> --%>
						
						<div id="attachment2_inputfile">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato2" /></div>
								<div style="float:left; width:320px;"><s:file name="attachment2" label="File1" /></div>	
								<div style="float:right; width:220px;"><font color="red"><span id="err_attachment2"><s:fielderror><s:param>attachment2</s:param></s:fielderror></span></font></div>		
							</div>	
						</div>
<%-- 					</s:if>	 --%>
<%-- 					<s:else> --%>
						<div id="attachment2_viewfile">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato2" /></div>
								<div style="float:left; width:320px;"><a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName2" />" target="new" class="textLinks"><s:text name="messaggio.attachmentName2" /></a></div>
								<div id="delete2" style="float:right; width:20px; margin-right:200px; text-align:left; cursor: pointer;"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>					
							</div>	
						</div>
<%-- 					</s:else> --%>
					
<%-- 					<s:if test="messaggio.attachmentName3 == null || messaggio.attachmentName3 == ''"> --%>
						<div id="attachment3_inputfile">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato3" /></div>
								<div style="float:left; width:320px;"><s:file name="attachment3" label="File2" /></div>	
								<div style="float:right; width:220px;"><font color="red"><span id="err_attachment3"><s:fielderror><s:param>attachment3</s:param></s:fielderror></span></font></div>							
							</div>	
						</div>
<%-- 					</s:if>	 --%>
<%-- 					<s:else> --%>
						<div id="attachment3_viewfile">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato3" /></div>
								<div style="float:left; width:220px;"><a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName3" />" target="new" class="textLinks"><s:text name="messaggio.attachmentName3" /></a></div>	
								<div id="delete3" style="float:right; width:20px; margin-right:200px; text-align:left; cursor: pointer"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>				
							</div>
						</div>	
<%-- 					</s:else>		 --%>
				</div>	
			</div>
		</fieldset>	
		<div style="width:100%; text-align:center">	
			<s:if test="%{disableForm eq false}">			
				<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" class="tableFields" style="width:100px; text-align:center" onclick="javascript: setTimeout(function() {return (synchEditor() && checkFields(false) && submitForm('messages_saveMessage.action'));},10);"/>
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='igeriv.memorizza.invia'/>" name="igeriv.memorizza.invia" id="memorizzaInvia" class="tableFields" style="width:150px; text-align:center" onclick="javascript: setTimeout(function() {return (synchEditor() && checkFields(false) && submitForm('messages_saveAndSendMessage.action'));},10);"/>			
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='dpe.contact.form.reset'/>" name="dpe.contact.form.reset" id="cancella" class="tableFields" style="width:150px; text-align:center" onclick="javascript:doDelete();"/>
			</s:if>
			<s:else>
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='igeriv.mostra.conferme.lettura'/>" name="igeriv.mostra.conferme.lettura" id="confermeLettura" class="tableFields" style="width:200px; text-align:center"/>
			</s:else>
		</div>
		<div id="messageDiv" style="float:right; width:200px; height:50px; font-size:14;"></div>
	</div>		
	<s:hidden name="message" id="message"/>	
	<s:hidden name="messaggio.pk.codFiegDl"/>
	<s:hidden name="strCodEdicolaDl" id="strCodEdicolaDl"/>
	<s:hidden name="crivwSelect" id="crivwSelect"/>
	<s:hidden name="messaggio.pk.dtMessaggio" id="dtMessaggio"/>
	<s:hidden name="messaggio.pk.destinatarioA"/>
	<s:hidden name="messaggio.pk.destinatarioB"/>
	<s:hidden name="messaggio.pk.tipoDestinatario"/>
	<s:hidden name="messaggio.messaggio" id="messaggio"/>
	<s:hidden name="messaggio.attachmentName1" id="attachmentName1"/>
	<s:hidden name="messaggio.attachmentName2" id="attachmentName2"/>
	<s:hidden name="messaggio.attachmentName3" id="attachmentName3"/>
	<s:hidden name="giroTipoSelected"/>
	<s:hidden name="zonaTipoSelected"/>
	<s:hidden name="giroSelected"/>
	<s:hidden name="zonaSelected"/>
	<s:hidden name="destinatarioSelected" id="destinatarioSelected"/>
</s:form>
<!-- Right Click Menu -->
<ul id="edicoleMenu" class="contextMenu">
    <li class="delete"><a href="#delete"><s:text name="dpe.contact.form.elimina"/></a></li>        	             
</ul>