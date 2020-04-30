<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNotePubblicazione = "<s:text name='igeriv.inserisci.note.pubblicazione'/>";
	$(document).ready(function() {
		$("#titolo").bind('keydown', function(event) { 
			var keycode = (event.keyCode ? event.keyCode : event.charCode);
			if (keycode == '13') {			
				event.preventDefault();	
				setTimeout(function() {
					$("#ricerca").trigger("click");
				}, 100);
			}
		});
		$("table[id*='_table'] tbody tr:not(:last)").find("td:nth-child(11)").each(function() {
			var $cell = $(this);
			var $tr = $cell.closest("tr");
			var $idtn = $tr.attr("idtn");
			var $tds = $tr.find("td");
			var $cpuTd = $tds.first();
			var $titoloTd = $cpuTd.next();
			var titolo = $titoloTd.text(); 
			var cpu = $cpuTd.text();
			var hasNoteCpu = $("#noteRivenditaCpu" + cpu).length > 0 ? $("#noteRivenditaCpu" + cpu).val().trim().length > 0 : false;
   		    var imgName = "note_rivendita" + (($("#noteRivendita" + $idtn).val().trim().length > 0 || hasNoteCpu) ? "_red" : "") + ".gif";
   		 	var titleMsg = getNoteRivendita($("#noteRivendita" + $idtn).val(), $("#noteRivenditaCpu" + cpu).val());
			$cell.html('<img name="noteRiv" id="noteRiv' + $idtn + '" src="/app_img/' + imgName + '" border="0px" style="border-style: none" onclick="javascript: addNote(\'' + titolo.replace(new RegExp('\'', 'g'),'\\\'') + '\',\'' + $idtn + '\',\'' + cpu + '\')" title="' + titleMsg + '" alt="' + msgNoteRivendita + '" />&nbsp;');
		});
		$("table[id*='_table'] tbody").find("td:not(:nth-child(11)):not(:nth-child(12))").click(function() {
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
		setContentDivHeight(30); 
		$("#ricerca").click(function() {
			$("#PubblicazioniInveduteFilterForm").submit();
		});
		$("#numMesiDataUscitaDaInventario").focus();
	});
</script>
