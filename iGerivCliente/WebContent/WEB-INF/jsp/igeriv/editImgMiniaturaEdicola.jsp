<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:form id="EditImmagineForm" action="configurazioni_saveImgMiniaturaEdicola.action" enctype="multipart/form-data" theme="igeriv" method="POST" validate="false">
	<div id="mainDiv">
		<fieldset class="filterBolla" style="text-align:left; width:100%"><legend style="font-size:100%"><s:text name="igeriv.aggiorna.img.logomarca.edicola"/></legend>
			<div class="tableFields" style="float:left; height:100px">
				<div style="float:left; width:100px; margin-top:25px"><s:text name="plg.immagine" /></div>
				<div style="float:left; width:280px; color:black; margin-top:25px"><s:file name="attachment1" id="attachment1" label="File"/></div>
				<div style="float:left; width:100px;"><img id="nomeImmagine" width="80" height="80" title="<s:text name="plg.immagine"/>" src="/<s:text name="barraSceltaRapida.immagineDirAlias"/>/<s:text name="barraSceltaRapida.nomeImmagine"/>" border="1" /></div>			
			</div>
		</fieldset>		
		<div style="text-align:center; margin-left:auto; margin-right:auto; width:200px; margin-top:20px">
			<div style="width:200px; margin-top:0px;"><input type="button" name="igeriv.memorizza" id="mem" value="<s:text name='igeriv.memorizza'/>" align="center" class="tableFields" style="text-align:center; width:100px" onclick="javascript: if (doValidationImgMiniatura()) { ray.ajax(); return (formSubmitMultipartAjax('EditImmagineForm','html',function(data){editImmagineSuccessCallback(data);})); }"/></div>
		</div>			
	</div>		
	<s:hidden name="barraSceltaRapida.pk.codFiegDl"/>
	<s:hidden name="barraSceltaRapida.pk.codEdicola"/>
	<s:hidden name="barraSceltaRapida.pk.codicePubblicazione"/>
</s:form>
