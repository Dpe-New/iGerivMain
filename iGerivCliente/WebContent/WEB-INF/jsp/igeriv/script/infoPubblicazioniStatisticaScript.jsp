<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script>
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNotePubblicazione = "<s:text name='igeriv.inserisci.note.pubblicazione'/>";
	$(document).ready(function() {
		loadFunctions();
		setContentDivHeight(30);
		<s:if test="actionName != null && actionName.equals('statisticaPubblicazioni_showPubblicazioniStatistica.action')">
			clearPubblicazioniFilterFields();
			$("table[id*='_table'] tbody tr").find("td:nth-child(10)").each(function() {
				var $cell = $(this);
				var $tr = $cell.closest("tr");
				var $idtn = $tr.attr("idtn");
				var $tds = $tr.find("td");
				var $titoloTd = $tds.first();
				var titolo = $titoloTd.text(); 
				var cpu = $titoloTd.next().next().text();
				var hasNoteCpu = $("#noteRivenditaCpu" + cpu).val().trim().length > 0;
       		    var imgName = "note_rivendita" + (($("#noteRivendita" + $idtn).val().trim().length > 0 || hasNoteCpu) ? "_red" : "") + ".gif";
       		 	var titleMsg = getNoteRivendita($("#noteRivendita" + $idtn).val(), $("#noteRivenditaCpu" + cpu).val());
				$cell.html('<img name="noteRiv" id="noteRiv' + $idtn + '" src="/app_img/' + imgName + '" border="0px" style="border-style: none" onclick="javascript: addNote(\'' + titolo.replace(new RegExp('\'', 'g'),'\\\'') + '\',\'' + $idtn + '\',\'' + cpu + '\')" title="' + titleMsg + '" alt="' + msgNoteRivendita + '" />&nbsp;');
			});
		</s:if>
		$("table[id*='_table'] tbody").find("td:not(:nth-child(10)):not(:nth-child(11))").click(function() {
			var popID = 'popup_name';	   
		    var popURL = $(this).parent().attr('divParam');
		 	var popWidth = 900;
		 	var popHeight = 400;
		    var query= popURL.split('?'); 
		    var dim= query[1].split('&');	   	    	    	  		  
		    var cpu = dim[0].split('=')[1];	  
		    var periodicita = '';
		    if (dim.length > 1) {
		    	periodicita = dim[1].split('=')[1];
		    }
		 	var url = "${pageContext.request.contextPath}/statisticaPubblicazioni_showNumeriPubblicazioniStatistica.action?codPubblicazione=" + cpu;
		 	if (periodicita != '') {
		 		url += "&periodicita=" + periodicita;
		 	}
			openDiv(popID, popWidth, popHeight, url);
		});
		if ($("#titolo").length > 0) {
			$("#titolo").focus();
		}
	});
	
	function loadFunctions() {
	}		
			
</script>