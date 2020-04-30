<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:if test="actionName != null && !actionName.contains('rifornimenti')">
	<script>	
		$('#pubMainDiv').keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);
			if (keycode == '13') {			    
				if ($("#submitRicerca").length > 0) {
					$("#PubblicazioniForm").submit();
				} else if ($('#ricerca').length > 0) {
	    			$('#ricerca').trigger('click');
				}
	    	} 
		}); 							
	</script>
</s:if>
<script>
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNotePubblicazione = "<s:text name='igeriv.inserisci.note.pubblicazione'/>";
	$(document).ready(function() {	
		<s:if test="actionName != null && actionName.equals('infoPubblicazioni_showPubblicazioni.action')">
			clearPubblicazioniFilterFields();
			$("table[id*='_table'] tbody").find("td:not(:nth-child(10)):not(:nth-child(11))").click(function() {
					var popID = 'popup_name';   
				    var popURL = $(this).parent().attr('divParam'); 
				 	var popWidth = 900;
				 	var popHeight = 400;
				    var query= popURL.split('?'); 
				    var dim= query[1].split('&');	   	    	    	  		  
				    var cpu = dim[0].split('=')[1];	  
				 	var url = "${pageContext.request.contextPath}/infoPubblicazioni_showNumeriPubblicazioni.action?codPubblicazione=" + cpu;
					openDiv(popID, popWidth, popHeight, url);
				}
			); 
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
			}
		);
		</s:if>
		<s:else>
			$("table[id*='_table'] tbody").find("td:not(:last-child)").click(function() {
					var popID = 'popup_name';   
				    var popURL = $(this).parent().attr('divParam'); 
				 	var popWidth = 900;
				 	var popHeight = 400;
				    var query= popURL.split('?'); 
				    var dim= query[1].split('&');	   	    	    	  		  
				    var cpu = dim[0].split('=')[1];	  
				 	var url = "${pageContext.request.contextPath}/infoPubblicazioni_showNumeriPubblicazioni.action?codPubblicazione=" + cpu;
					openDiv(popID, popWidth, popHeight, url);
				}
			);
		</s:else>
		loadFunctions();
		setContentDivHeight(30); 
		if ($("#titolo").length > 0) {
			$("#titolo").focus();
		}	
	});
	
	function loadFunctions() {
	}		
			
</script>