<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNotePubblicazione = "<s:text name='igeriv.inserisci.note.pubblicazione'/>";
	$(document).ready(function() {
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataDa').datepicker();
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataA').datepicker();
		
		
// 		$('#strDataDa').click(function() { 
// 	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#strDataA').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	

		$("#strDataDa, #strDataA, #titolo").bind('keydown', function(event) { 
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
			var dataDa = $("#strDataDa").val();
			var dataA = $("#strDataA").val();
			if (dataDa.trim() == '' || dataA.trim() == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}','<s:text name="igeriv.data.uscita"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#strDataDa").focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false; 
			} 
			if (dataDa.trim() != '' && !checkDate(dataDa)) {
				$.alerts.dialogClass = "style_1";
				jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#strDataDa').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false; 
			} 
			if (dataA.trim() != '' && !checkDate(dataA)) {
				$.alerts.dialogClass = "style_1";
				jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$('#strDataA').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false; 
			} 
	        var day = parseInt(dataDa.substring(0, dataDa.indexOf("/")), 10);
	        var mo = parseInt(dataDa.substring(dataDa.indexOf("/") + 1, dataDa.lastIndexOf("/")), 10);
	        var yr = parseInt(dataDa.substring(dataDa.lastIndexOf("/") + 1), 10);
	        var dtDa = new Date(yr, mo - 1, day);
	        day = parseInt(dataA.substring(0, dataA.indexOf("/")), 10);
	        mo = parseInt(dataA.substring(dataA.indexOf("/") + 1, dataA.lastIndexOf("/")), 10);
	        yr = parseInt(dataA.substring(dataA.lastIndexOf("/") + 1), 10);
	        var dtA = new Date(yr, mo - 1, day);
			var difference_ms = Math.abs(dtDa - dtA);
			var ONE_DAY = 1000 * 60 * 60 * 24;
			var days = Math.round(difference_ms / ONE_DAY);
			var maxDays = 30;
			var titolo = $("#titolo").val().trim();
			var maxDaysLimit = titolo == '' ? 60 : 180;
			if (titolo == '' && days > maxDays && days <= maxDaysLimit) {
				jConfirm('<s:text name="igeriv.intervallo.intervallo.tempo.superiore.x.giorni"/>'.replace('{0}', maxDays), attenzioneMsg, function(r) {
					if (r) {
				    	$("#PubblicazioniInveduteFilterForm").submit();
					} else {
						setTimeout(function() {
							unBlockUI();					
						}, 100);
						return false;
					}
				}, true, false);
			} else if (days > maxDaysLimit) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.intervallo.date.eccede.limite"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					setTimeout(function() {
						$('#strDataDa').focus();					
					}, 100);
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			} else {
				$("#PubblicazioniInveduteFilterForm").submit();
			}
		});
		$("#escludiQuotidiani").attr("checked", true);
		$("#strDataDa").focus();
	});
</script>		    