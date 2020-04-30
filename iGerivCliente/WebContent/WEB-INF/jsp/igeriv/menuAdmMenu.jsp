<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="EditMenuForm" action="menu_saveMenu.action" namespace="/adm" method="POST" theme="igeriv" validate="false" onsubmit="return (ray.ajax())">			
	<div id="mainDiv">	
		<fieldset class="filterBolla" style="text-align:left; width:100%"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="label.print.Table.Title" /></div>
				<div style="float:left; color:black"><s:textfield label="" name="menu.titolo" id="menu.titolo" cssStyle="width: 400px" cssClass="tableFields"/></div>
				<div style="float:left;"><span id="err_menu.titolo"><s:fielderror><s:param>menu.titolo</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="igeriv.descrizione" /></div>
				<div style="float:left; color:black"><s:textfield label="" name="menu.descrizione" id="menu.descrizione" cssStyle="width: 400px" cssClass="tableFields"/></div>
				<div style="float:left;"><span id="err_menu.descrizione"><s:fielderror><s:param>menu.descrizione</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.url" /></div>
				<div style="float:left; color:black"><s:textfield label="" name="menu.url" id="menu.url" cssStyle="width: 400px" cssClass="tableFields"/></div>
				<div style="float:left;"><span id="err_menu.url"><s:fielderror><s:param>menu.url</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.menu.action" /></div>
				<div style="float:left; color:black"><s:textfield label="" name="menu.actionName" id="menu.actionName" cssStyle="width: 400px" cssClass="tableFields"/></div>
				<div style="float:left;"><span id="err_menu.actionName"><s:fielderror><s:param>menu.actionName</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.menu.modulo.padre" /></div>
				<div style="float:left; color:black">
					<s:select 
						label=""
				        name="menu.idModuloPadre"
				        id="menu.idModuloPadre" 
				        listKey="id" 
				        listValue="titolo"
				        list="listMenus"
				        emptyOption="true"
				        cssStyle="width: 400px"	
				        cssClass="tableFields"					       				       
				        />
				</div>
				<div style="float:left;"><span id="err_menu.idModuloPadre"><s:fielderror><s:param>menu.idModuloPadre</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.menu.posizione.interna" /></div>
				<div style="float:left; color:black; width:400px">
					<s:select 
						label=""
				        name="menu.posizioneItem"
				        id="menu.posizioneItem" 
				        list="#{'1':'1','2':'2','3':'3','3':'3','4':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}"
				        emptyOption="false"
				        cssStyle="width: 50px"	
				        cssClass="tableFields"					       				       
				        />
				</div>
				<div style="float:left;"><span id="err_menu.posizioneItem"><s:fielderror><s:param>menu.posizioneItem</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.menu.posizione" /></div>
				<div style="float:left; color:black; width:400px">
					<s:select 
						label=""
				        name="menu.posizioneMenu"
				        id="menu.posizioneMenu" 
				        list="#{'1':'1','2':'2','3':'3','3':'3','4':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}"
				        emptyOption="false"
				        cssStyle="width: 50px"	
				        cssClass="tableFields"					       				       
				        />
				</div>
				<div style="float:left;"><span id="err_menu.posizioneMenu"><s:fielderror><s:param>menu.posizioneMenu</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.menu.livello" /></div>
				<div style="float:left; color:black; width:400px">
					<s:select 
						label=""
				        name="menu.livello"
				        id="menu.livello" 
				        list="#{'0':'0','1':'1','2':'2'}"
				        emptyOption="false"
				        cssStyle="width: 50px"	
				        cssClass="tableFields"					       				       
				        />
				</div>
				<div style="float:left;"><span id="err_menu.livello"><s:fielderror><s:param>menu.livello</s:param></s:fielderror></span></div>
			</div>
			<div class="tableFields" style="float:left;">
				<div style="float:left; width:150px"><s:text name="dpe.menu.is.modulo.padre" /></div>
				<div style="float:left; color:black; width: 400px">
					<s:checkbox name="menu.moduloPadre" id="menu.moduloPadre" cssClass="tableFields"/>
				</div>
			</div>
		</fieldset>		
		<div style="text-align:center; margin-left:auto; margin-right:auto; width:400px; margin-top:20px">
			<div width:100px; margin-top:0px;"><input type="button" name="igeriv.memorizza" id="memorizza" value="<s:text name='igeriv.memorizza'/>" align="center" class="tableFields" style="text-align:center; width:100px" onclick="javascript: return setFormAction('EditMenuForm','menu_saveMenu.action', '', '');"/></div>						
		</div>			
	</div>		
	<s:hidden name="menu.id"/>
	<input type ="hidden" id="urlHidden" value="<s:property value="menu.url"/>"/>
	<s:hidden name="idProfilo"/>
</s:form>