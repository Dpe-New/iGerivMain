<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<style>
		div#filter { height:20px;} 
		div#content1 { height:550px;} 
</style> 
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="MessageForm" action="messagesIdtn_saveMessageIdtn.action" method="POST" enctype="multipart/form-data" theme="igeriv" validate="true" onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="text-align:left; width:750px"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div style="width:750px;">	
				<div class="required" style="width:750px; height:40px;"> 
					<div style="float:left; width:200px;"><s:text name="igeriv.cod.pubblicazione.o.titolo" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
					<div style="float:left; width:300px;"><s:textfield id="pubblicazione" name="pubblicazione" cssStyle="width: 400px" cssClass="tableFields"/></div>
				</div>
				<div class="required" style="width:750px; height:40px;"> 
					<div style="float:left; width:200px;"><s:text name="igeriv.cod.numero.copertina" /></div>
					<div style="float:left; width:300px;">
						<s:select label=""
						    name="copertina"
						    id="copertina" 
						    listKey="key" 
						    listValue="value"
						    list="#{}"
						    emptyOption="true" 
						    cssStyle="width: 200px"
					    />
					</div>
				</div>
				<div style="width:750px; height:10px;"></div>	
				<div style="width:750px; heigth:300px; text-align:center;">
					<div id="innerDiv" style="750px; text-align:center;"></div>											
				</div>	
				<div style="width:750px;">
					<div id="attachment1_input" class="required" style="height:30px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato1" /></div>
						<div style="float:left; width:220px;"><s:file name="attachment1" label="File" /></div>			
						<div style="float:right; width:430px; text-align:right; vertical-align:middle;"><font color="red"><span id="err_attachment1"><s:fielderror><s:param>attachment1</s:param></s:fielderror></span></font></div>		
					</div>	
					<div id="attachment1_show" class="required" style="height:30px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato1" /></div>
						<div style="float:left; width:320px;"><a id="attachment1_href" href="" target="new" class="textLinks">_</a></div>
						<div id="delete1" style="float:right; width:20px; margin-right:200px; text-align:left; cursor: pointer"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>					
					</div>	
					
					<div id="attachment2_input" class="required" style="height:30px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato2" /></div>
						<div style="float:left; width:220px;"><s:file name="attachment2" label="File1" /></div>	
						<div style="float:right; width:430px;"><font color="red"><span id="err_attachment2"><s:fielderror><s:param>attachment2</s:param></s:fielderror></span></font></div>		
					</div>	
					<div id="attachment2_show" class="required" style="height:30px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato2" /></div>
						<div style="float:left; width:320px;"><a id="attachment2_href" href="" target="new" class="textLinks">_</a></div>
						<div id="delete2" style="float:right; width:20px; margin-right:200px; text-align:left; cursor: pointer;"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>					
					</div>	
					
					<div id="attachment3_input" class="required" style="height:30px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato3" /></div>
						<div style="float:left; width:32220px0px;"><s:file name="attachment3" label="File2" /></div>	
						<div style="float:right; width:430px;"><font color="red"><span id="err_attachment3"><s:fielderror><s:param>attachment3</s:param></s:fielderror></span></font></div>							
					</div>	
					<div id="attachment3_show" class="required" style="height:30px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato3" /></div>
						<div style="float:left; width:220px;"><a id="attachment3_href" href="" target="new" class="textLinks">_</a></div>	
						<div id="delete3" style="float:right; width:20px; margin-right:200px; text-align:left; cursor: pointer"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>				
					</div>	
				</div>	
			</div>
		</fieldset>	
		<div style="width:100%; text-align:center">	
			<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" class="tableFields" style="width:100px; text-align:center" onclick="javascript: setTimeout(function() {return (synchEditor() && checkFields(false) && submitForm('messagesIdtn_saveMessageIdtn.action'));},10);"/>
			&nbsp;&nbsp;
			<input type="button" value="<s:text name='dpe.contact.form.reset'/>" name="dpe.contact.form.reset" id="cancella" class="tableFields" style="width:100px; text-align:center" onclick="javascript: doDelete();"/>
		</div>
		<div id="messageDiv" style="float:right; width:200px; height:50px; font-size:14;"></div>
	</div>
	<s:hidden name="idtn" id="idtn" />
	<s:hidden name="message" id="message" />
	<s:hidden name="messaggioIdtnVo.idMessaggioIdtn" id="idMessaggioIdtn" />
	<s:hidden name="messaggioIdtnVo.testo" id="testo" />
	<s:hidden name="messaggioIdtnVo.attachmentName1" id="attachmentName1" />
	<s:hidden name="messaggioIdtnVo.attachmentName2" id="attachmentName2" />
	<s:hidden name="messaggioIdtnVo.attachmentName3" id="attachmentName3" />
</s:form>