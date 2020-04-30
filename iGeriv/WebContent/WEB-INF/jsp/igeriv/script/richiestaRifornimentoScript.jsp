<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>


<script>

	dojo.require("dojox.grid.DataGrid");
	dojo.require("dojo.data.ItemFileWriteStore");
	dojo.require("dojo.rpc.RpcService");
	dojo.require("dojo.rpc.JsonService");	
	dojo.require("dijit.Tree");
	dojo.require("dijit.tree.ForestStoreModel");
	
	var confirmInserimentoNuovaRichiestaStessoCpu 				= '<s:text name="msg.confirm.inserimento.nuovo.ordine.stesso.cpu"/>';
	var confirmInserimentoNuovaRichiestaStessoCpuDataRispQuaev 	= '<s:text name="msg.confirm.inserimento.nuovo.ordine.stesso.cpu.datarisp.quaev"/>';

	$(document).ready(function() {	
		
		
		
		addFadeLayerEvents();
		thumbnailviewer.init();
		setFirstRowColor('<s:text name="%{#request.giacenzaPressoDl}"/>');
		$("input[type='checkbox'][name='chkLivellamenti']").tooltip();
		$("#disponibilita").tooltip({
				delay: 0,  
			    showURL: false,			    
			    bodyHandler: function() {
			    	var str = '<div><s:text name="igeriv.leggenda"/>&nbsp;:</div>';
			    	str += '<div style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#009933"/><b><s:text name="igeriv.pubblicazione.disponibile"/></b>';
			    	str += '<br><div style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#ff0000"/><b><s:text name="igeriv.pubblicazione.non.disponibile"/></b>';
			    	return str;
			    }
		});
		
		var msgControllaDettaglioPerRifornimentiAncoraAttiviFirst = '<s:text name="%{#request.msgControllaDettaglioPerRifornimentiAncoraAttiviFirst}"/>';
		<s:if test="%{#request.dettaglioEsitorichiesteRifornimento != null && !#request.dettaglioEsitorichiesteRifornimento.isEmpty()}">
			if(isMenta){
				if(msgControllaDettaglioPerRifornimentiAncoraAttiviFirst== '1'){
					alert("Attenzione hai gia\' inserito la richiesta di rifornimento per tale testata numero, l\'Agenzia l\'ha gia\' processata "+
							"e puoi controllare la risposta nella schermata sotto riportata Esito Richieste di Rifornimento. Se inserisci altre copie, queste "+
							"si sommeranno a quelle precedentemente richieste.");
				}else if(msgControllaDettaglioPerRifornimentiAncoraAttiviFirst== '2'){
					alert("Attenzione hai gia\' inserito la richiesta di rifornimento per tale testata "+
							"numero, l\'Agenzia l\'ha presa in carico e ti fornita\' a breve una risposta. Se inserisci altre copie, queste "+
							"si sommeranno a quelle precedentemente richieste.");
				}else if(msgControllaDettaglioPerRifornimentiAncoraAttiviFirst== '3'){
					alert("Attenzione la pubblicazione risulta essere chiamata in resa ");
				}
			}
		</s:if>
		
		
		if(isMenta){
				$('table[id=RichiestaRifornimentoTab_table] input:text[name*=quantitaRifornimentoMap]').click(function() {
				
				var dataRichiamoResa = '';
				var msgControllaDettaglioPerRifornimentiAncoraAttivi = 0;
				var idRichiesta = $(this).parent().find("input:hidden[name=pk]").attr("value");  
				ray.ajax();
	    		dojo.xhrGet({
	    			url: "${pageContext.request.contextPath}/pubblicazioniRpc_getEsitoRifornimentoRispostaDL.action?idRichiesta="+idRichiesta+"|",			
	    			handleAs: "json",				
	    			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
	    			preventCache: true,
	    			handle: function(data,args) {
	    				unBlockUI();
						if (args.xhr.status == 200) {
							
							$("#EsitoRichiestaRifornimentoTab_table > tbody").empty();
							for (i = 0; i < data.length; i++) {
				            
								if(data[i].msgControllaDettaglioPerRifornimentiAncoraAttivi!=null && 
										data[i].msgControllaDettaglioPerRifornimentiAncoraAttivi == '1'){
									msgControllaDettaglioPerRifornimentiAncoraAttivi = 1;
								}else if(data[i].msgControllaDettaglioPerRifornimentiAncoraAttivi!=null && 
										data[i].msgControllaDettaglioPerRifornimentiAncoraAttivi == '2'){
									msgControllaDettaglioPerRifornimentiAncoraAttivi = 2;
								//Ticket 0000373
								}else if(data[i].msgControllaDettaglioPerRifornimentiAncoraAttivi!=null && 
										data[i].msgControllaDettaglioPerRifornimentiAncoraAttivi == '3'){
									msgControllaDettaglioPerRifornimentiAncoraAttivi = 3;
									dataRichiamoResa = data[i].dataRichiamoResa;
								}else{
									msgControllaDettaglioPerRifornimentiAncoraAttivi = 0;
								}
								
								if(data[i].dataordine != null){
									var classCss = (i%2==0)?"odd":"even";
									$("#EsitoRichiestaRifornimentoTab_table > tbody:last-child")
					            		.append("<tr class=\""+classCss+"\" >"+
					            			"<td class=\"extremeTableFieldsLarger\" >"+data[i].dataordine+"</td>"+      			//Data ordine
					            			"<td class=\"extremeTableFieldsLarger\" >"+data[i].dataScadenza+"</td>"+				//Data Scadenza Richiesta
					            			"<td class=\"extremeTableFieldsLarger\" >"+data[i].quantitarichiesta+"</td>"+			//Copie Ordinate 
					            			"<td class=\"extremeTableFieldsLarger\" >"+data[i].quantitaevasa+"</td>"+				//Qta Evasa
					            			"<td class=\"extremeTableFieldsLarger\" >"+data[i].datarispostadl+"</td>"+				//Data Evasione
					            			"<td class=\"extremeTableFieldsLarger\" >"+data[i].desccausalenonevadibilita+"</td>"+	//Risposta DL
					            			"</tr>");
								}
							}
							switch (true) {
						    	case (msgControllaDettaglioPerRifornimentiAncoraAttivi == 1):
		
							    	alert("Attenzione hai gia\' inserito la richiesta di rifornimento per tale testata numero, l\'Agenzia l\'ha gia\' processata "+
											"e puoi controllare la risposta nella schermata sotto riportata Esito Richieste di Rifornimento. Se inserisci altre copie, queste "+
											"si sommeranno a quelle precedentemente richieste.");
						    	
						    	/*
							    $.alerts.dialogClass = "style_1";
									jAlert("Attenzione hai gia\' inserito la richiesta di rifornimento per tale testata numero, l\'Agenzia l\'ha gia\' processata "+
										"e puoi controllare la risposta nella schermata sotto riportata Esito Richieste di Rifornimento. Se inserisci altre copie, queste "+
										"si sommeranno a quelle precedentemente richieste.",attenzioneMsg.toUpperCase(), function() {
										$.alerts.dialogClass = null;
										this.focus();
										return false;
										
									});		
								*/
						    
						        break;
							    case (msgControllaDettaglioPerRifornimentiAncoraAttivi == 2):
							    	
							    	alert("Attenzione hai gia\' inserito la richiesta di rifornimento per tale testata "+
											"numero, l\'Agenzia l\'ha presa in carico e ti fornita\' a breve una risposta. Se inserisci altre copie, queste "+
											"si sommeranno a quelle precedentemente richieste.");
							    	/*
								    $.alerts.dialogClass = "style_1";
										jAlert("Attenzione hai gia\' inserito la richiesta di rifornimento per tale testata "+
											"numero, l\'Agenzia l\'ha presa in carico e ti fornita\' a breve una risposta. Se inserisci altre copie, queste "+
											"si sommeranno a quelle precedentemente richieste.",attenzioneMsg.toUpperCase(), function() {
											$.alerts.dialogClass = null;
											
											return false;
										});	
										*/
							    
							        break;
									case (msgControllaDettaglioPerRifornimentiAncoraAttivi == 3):
							    	
										
										alert("Attenzione la pubblicazione selezionata, risulta essere chiamata in resa in data "+dataRichiamoResa);
										/*
										$.alerts.dialogClass = "style_1";
										jAlert("Attenzione la pubblicazione selezionata, risulta essere chiamata in resa in data "+dataRichiamoResa+"",
												attenzioneMsg.toUpperCase(),function() {
											$.alerts.dialogClass=null;
											$('#quantitaRifornimentoMap'+idRichiesta).focus();
												
										});
										return false;
										*/
							        break;
							    default:
							        break;
						}
							
							
						} else {
							$.alerts.dialogClass = "style_1";
							jAlert("<s:text name='msg.errore.invio.richiesta'/>", attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
								return false;
							});
						} 
	    			}
	    			
	    			
	    	    });
			});
		}
		
		
		$('input:text[name*=quantitaRifornimentoMap]:disabled').each(function (){
		    var $self = $(this), $parent = $self.closest("td"), $overlay = $("<div />");
		    var $row = $self.closest("tr");
		    var livellamentoEditable = $row.attr("divParam").split('?')[1].split('=')[1] == "true";
		    
		    
		    
		    var id = $(this).attr("id");
		    var dtOrdineRifornimento = $(this).attr("dataordinerifornimento");
		    var dataRispostaDl = $(this).attr("dataRispostaDl");
		    
		    
		    
		    if (livellamentoEditable && dtOrdineRifornimento!="" && dataRispostaDl=="" ) {
			    $overlay.css({
			        position: "absolute"
			      , top: $parent.position().top
			      , left: $parent.position().left
			      , width: $parent.outerWidth()
			      , height: $parent.outerHeight()
			      , zIndex: 10000
			      , backgroundColor: "#fff"
			      , opacity: 0
			    }).click(function (){
			    	var $nextRow = $row.next();
			    	var isPresent = false;
			    	if ($nextRow != null && $nextRow.length > 0) {
			    		var curIdtn = $row.find("input:hidden[name=pk]").first().val().split("|")[3];
			    		var nextIdtn = $nextRow.find("input:hidden[name=pk]").first().val().split("|")[3];
			    		isPresent = (nextIdtn == curIdtn);
			    	}
			    	if (!isPresent) {
			    		PlaySound('beep3');
			    		
				    	jConfirm(confirmInserimentoNuovaRichiestaStessoCpu.replace("{0}", dtOrdineRifornimento), attenzioneMsg, function(r) {
				    	    if (r) { 
				    	    	var $newRow = $row.clone();
				    	    	$newPk = $newRow.find("input:hidden[name=pk]").first();
				    	    	$newQta = $newRow.find("input:text[name*=quantitaRifornimentoMap]").first();
				    	    	$newDtaScadenza = $newRow.find("input:text[name*=giorniValiditaRichiesteRifornimentoMap]").first();
				    	    	$newNote = $newRow.find("input:text[name*=noteVenditaMap]").first();
				    	    	$newQta.removeAttr('disabled');
				    	    	$newNote.removeAttr('disabled');
				    	    	$newPk.removeAttr('disabled');
				    	    	var newId = $newQta.attr("id");
				    	    	var newIdDate = newId.split("|")[2];
				    	    	var d = new Date();
				    	    	var year = d.getFullYear();
				    			var month = pad2(d.getMonth() + 1);
				    			var day = pad2(d.getDate());
				    	    	var hours = pad2(d.getHours());
				    			var minutes = pad2(d.getMinutes());
				    			var seconds = pad2(d.getSeconds());
				    			var newDate = year + month + day + hours + minutes + seconds;
				    			$newPk.attr("name", "pkAggiunte");
				    			$newQta.attr("name", "quantitaRifornimentoAggiunte");
				    			$newDtaScadenza.attr("name", "giorniValiditaRichiesteRifornimentoAggiunte");
				    			$newNote.attr("name", "noteVenditaAggiunte");
				    			$newQta.attr("id", newId.replace(newIdDate, newDate));
				    			$newPk.val($newPk.val().replace(newIdDate, newDate));
				    	    	$newQta.val("");
				    	    	$newNote.val("");
				    	    	$newRow.insertAfter($row);
				    	    	setFocusedFieldStyle();
				    	    	$newQta.focus();
				    	    	return false;
				    	    }
			    		});
			    	}
			    });
			    $parent.append($overlay);
		    }else{
		    	
		    
		    	if(isMenta){
			    	// QUANDO LA DATA DI RISPOSTA DEL DL E' VALORIZZATA E LA QUAEV == 0 or NULL
			    	$overlay.css({
				        position: "absolute"
				      , top: $parent.position().top
				      , left: $parent.position().left
				      , width: $parent.outerWidth()
				      , height: $parent.outerHeight()
				      , zIndex: 10000
				      , backgroundColor: "#fff"
				      , opacity: 0
				    }).click(function (){
				    	var $nextRow = $row.next();
				    	var isPresent = false;
				    	if ($nextRow != null && $nextRow.length > 0) {
				    		var curIdtn = $row.find("input:hidden[name=pk]").first().val().split("|")[3];
				    		var nextIdtn = $nextRow.find("input:hidden[name=pk]").first().val().split("|")[3];
				    		isPresent = (nextIdtn == curIdtn);
				    	}
				    	if (!isPresent) {
				    		var esitoRispostaDL = "";
			    		
			    		ray.ajax();
			    		dojo.xhrGet({
				    			url: "${pageContext.request.contextPath}/pubblicazioniRpc_getEsitoRifornimentoRispostaDL.action?idRichiesta="+id+"|",			
				    			handleAs: "json",				
				    			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
				    			preventCache: true,
				    			handle: function(data,args) {
									unBlockUI();
									if (args.xhr.status == 200) {
										var result = data.result;
										if (typeof(result) != 'undefined' && result != '') {
											
											PlaySound('beep3');
								    		jConfirm(confirmInserimentoNuovaRichiestaStessoCpuDataRispQuaev.replace("{0}", dataRispostaDl).replace("{1}", result), attenzioneMsg, function(r) {
									    	    if (r) { 
									    	    	var $newRow = $row.clone();
									    	    	$newPk = $newRow.find("input:hidden[name=pk]").first();
									    	    	$newQta = $newRow.find("input:text[name*=quantitaRifornimentoMap]").first();
									    	    	$newDtaScadenza = $newRow.find("input:text[name*=giorniValiditaRichiesteRifornimentoMap]").first();
									    	    	$newNote = $newRow.find("input:text[name*=noteVenditaMap]").first();
									    	    	$newQta.removeAttr('disabled');
									    	    	$newNote.removeAttr('disabled');
									    	    	$newPk.removeAttr('disabled');
									    	    	var newId = $newQta.attr("id");
									    	    	var newIdDate = newId.split("|")[2];
									    	    	var d = new Date();
									    	    	var year = d.getFullYear();
									    			var month = pad2(d.getMonth() + 1);
									    			var day = pad2(d.getDate());
									    	    	var hours = pad2(d.getHours());
									    			var minutes = pad2(d.getMinutes());
									    			var seconds = pad2(d.getSeconds());
									    			var newDate = year + month + day + hours + minutes + seconds;
									    			$newPk.attr("name", "pkAggiunte");
									    			$newQta.attr("name", "quantitaRifornimentoAggiunte");
									    			$newDtaScadenza.attr("name", "giorniValiditaRichiesteRifornimentoAggiunte");
									    			$newNote.attr("name", "noteVenditaAggiunte");
									    			$newQta.attr("id", newId.replace(newIdDate, newDate));
									    			$newPk.val($newPk.val().replace(newIdDate, newDate));
									    	    	$newQta.val("");
									    	    	$newNote.val("");
									    	    	$newRow.insertAfter($row);
									    	    	setFocusedFieldStyle();
									    	    	$newQta.focus();
									    	    	return false;
									    	    }
								    		});
										} else {
											$.alerts.dialogClass = "style_1";
											jAlert("<s:text name='msg.errore.invio.richiesta'/>", attenzioneMsg.toUpperCase(), function() {
												$.alerts.dialogClass = null;
												return false;
											});
										}
									} else {
										$.alerts.dialogClass = "style_1";
										jAlert("<s:text name='msg.errore.invio.richiesta'/>", attenzioneMsg.toUpperCase(), function() {
											$.alerts.dialogClass = null;
											return false;
										});
									} 
								}
			    			});
			    		}
			    	});
		    	}
		    	
			    $parent.append($overlay);
		    	
		    	
		    }
		    
		    
		});
	
		
		
		var $fields = $('input:text[name*=quantitaRifornimentoMap]');
		$fields.each(function (){
			var $self = $(this);
			$self.numeric({ negative : false });
			if ($self.attr("disabled") == true) {
				$self.closest("tr").find("input:hidden[name=pk]").first().attr("disabled", true);				
			}
		});
		$fields.numeric(false);
		<s:if test="hasActionErrors()">
			var msg = '';
			<s:iterator value="actionErrors">
				msg += '<s:property escape="false" />';
			</s:iterator>
			$.alerts.dialogClass = "style_1";
			jAlert(msg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
		</s:if>
		
		$('input:text[noRifornimento=noRifornimento]').each(function (){
			$(this).attr('disabled', 'disabled');
		});
		
		$('input:text[name*=quantitaRifornimentoMap]').not(":disabled").first().focus();
	});
	
	<s:if test="%{isQuotidiano != null && isQuotidiano}">
		$("#settimana").click(function() {
			var popID = 'popup_name_det';  
			var popWidth = 850;
		 	var popHeight = 400;
			var idtn = $("input[name='idtn']").val();
		    var url = "${pageContext.request.contextPath}/sonoInoltreUscite_showVariazioniSettimana.action?idtn=" + idtn;
		    openDiv(popID, popWidth, popHeight, url);
		});
	</s:if>
	
	function pad2(number) { 
	    return (number < 10 ? '0' : '') + number
	}
	
	$('tr td:nth-child(5)').click(function() { 
	    	return false;		
	});		
	
	function afterSuccessSave() {
		selectRowByBarcode($("#pkSel").val(), 'differenze');
		$("#close").trigger('click');
	}
	 
	function setFirstRowColor(giacenzaDl) {
		var row = $("#RichiestaRifornimentoTab_table tbody tr td:nth-child(10)").first();
		var divCircle = '';
		if (giacenzaDl > 0) {
			divCircle = '<div id="disponibilita" title="Disponibile" style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#009933"/>';
		} else if (giacenzaDl < 0) {
			divCircle = '<div id="disponibilita" title="Non Disponibile" style="margin-top:0px; margin-left:auto; margin-right:auto; text-align:center; width: 1.0em; height: 1.3em; -webkit-border-radius: 1em; -moz-border-radius: 1em; background:#ff0000"/>';
		}
		row.html(divCircle);
	}
	
	
	function setSelectionRange(input, selectionStart, selectionEnd) {
		  if (input.setSelectionRange) {
		    input.focus();
		    input.setSelectionRange(selectionStart, selectionEnd);
		  } else if (input.createTextRange) {
		    var range = input.createTextRange();
		    range.collapse(true);
		    range.moveEnd('character', selectionEnd);
		    range.moveStart('character', selectionStart);
		    range.select();
		  }
	}

	function setCaretToPos(input, pos) {
	  	setSelectionRange(input, pos, pos);
	}
	
	
	
	
</script>