<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	
	$(document).ready(function() {	
		
		var dett_coddl = '<s:property value="codDl"/>'; 
		
		addFadeLayerEvents();	
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dett_dataInserimento').datepicker();
		$('#dett_dataSospensione').datepicker();
		
		$('#dett_data_da').datepicker();
		$('#dett_data_a').datepicker();

		
	
		
			$('#button_aggiorna_date').click(function() {
				
				
				
// 				var search_codDl = $('#search_codDl').val();
// 				var search_idProfiliDL = $('#search_idProfiliDL').val();
// 				var search_codEdicolaWebStr = $('#search_codEdicolaWebStr').val();
// 				var search_ragioneSociale = $('#search_ragioneSociale').val();
			
				var codEdicolaWeb 	= $('#codEdicolaWeb_hid').val();
				var codFiegDl 		= $('#codFiegDl_hid').val();
				var dataInserimento = $('#dett_dataInserimento').val();
				var dataSospensione = $('#dett_dataSospensione').val();
				
				jConfirm('MEMORIZZA DATE INSERITE ?', attenzioneMsg.toUpperCase(), function(r) {
							 if (r) { 
								 	ray.ajax();
							    	dojo.xhrGet({
							    		
							    		url: "${pageContext.request.contextPath}/pubblicazioniRpc_memorizzaDataInserimentoDataSospensione.action?codEdicolaWeb="+codEdicolaWeb+"&codFiegDl="+codFiegDl+"&dataInserimento="+dataInserimento+"&dataSospensione="+dataSospensione,	
										preventCache: true,
										handleAs: "json",				
										headers: { "Content-Type": "application/json; charset=utf-8"}, 	
										handle: function(data,args) {
											
											
											 var message = "";
										      switch(args.xhr.status){
										         case 200:
										        	 	//Chiudo il popup
													 	if ($('#popup_name_dettaglio_profilo').is(':visible')) {
													 		$("#close").trigger('click');
													 		//Ricarico i dati del popup di dettaglio
													 		var url = appContext + "/profilazione_showDettaglioProfilo.action?codEdicolaWeb=" + codEdicolaWeb+"&codDl="+codFiegDl ;
															openDiv('popup_name_dettaglio_profilo', 1200, 700, url);
													 		//Messaggio di avvenuto inserimento
															$.alerts.dialogClass = "style_1";
															jAlert('Inserimento effettuato con seccesso...!!!', msgAvviso.toUpperCase(), function() {
																$.alerts.dialogClass = null;
																return false;
															});
													 	} 
										           break;
										         case 404:
										           message = "The requested page was not found";
										           break;
										         case 500:
										           message = "The server reported an error.";
										           break;
										         case 407:
										           message = "You need to authenticate with a proxy.";
										           break;
										         default:
										           message = "Unknown error.";
										      }
										      
										      if(message != ""){
										    	  	jAlert(message, attenzioneMsg.toUpperCase(), function() {
														$.alerts.dialogClass = null;
														return false;
													});
										      }
											
											
											
											
													
										}
									});
							    }
							 unBlockUI();
						});
				}); //END button_aggiorna_date
				
				
				$('#button_aggiungi_profilazione').click(function() {
				
					var codEdicolaWeb 	= $('#codEdicolaWeb_hid').val();
					var codFiegDl 		= $('#codFiegDl_hid').val();
					var popup_dett_select_profili 	= $('#dett_select_profili').val();
					var popup_dett_data_da 			= $('#dett_data_da').val();
					var popup_dett_data_a 			= $('#dett_data_a').val();
					
					var popup_dett_flag_test	= $('#dett_flag_test').is(":checked");
					var dett_flag_promo 		= $('#dett_flag_promo').is(":checked");
					var dett_flag_plus 			= $('#dett_flag_plus').is(":checked");
					
					if($('#dett_select_profili').val()=="" || $('#dett_data_da').val()=="" ){
					    jAlert('Attenzione - Dati non sufficienti per l\'inserimento di un nuovo piano!!!:-)', msgAvviso.toUpperCase(), function() {
							return false;
						});
					    
					}else{
							if(popup_dett_data_da == 'undefined') popup_dett_data_da = "";
							if(popup_dett_data_a == 'undefined') popup_dett_data_a = "";
							if(popup_dett_flag_test == 'undefined') popup_dett_flag_test = "false";
							if(dett_flag_promo == 'undefined') dett_flag_promo = "false";
							if(dett_flag_plus == 'undefined') dett_flag_plus = "false";
						
							jConfirm('Incrementa il piano del punto vendita?', attenzioneMsg.toUpperCase(), function(r) {
								 if (r) {
								 		ray.ajax();
								    	dojo.xhrGet({
								    		url: "${pageContext.request.contextPath}/pubblicazioniRpc_memorizzaPianoProfilazione.action?codEdicolaWeb="+codEdicolaWeb
								    															+"&codFiegDl="+codFiegDl
								    															+"&popup_dett_select_profili="+popup_dett_select_profili
								    															+"&popup_dett_data_da="+popup_dett_data_da
								    															+"&popup_dett_data_a="+popup_dett_data_a
								    															+"&popup_dett_flag_test="+popup_dett_flag_test
								    															+"&dett_flag_promo="+dett_flag_promo
								    															+"&dett_flag_plus="+dett_flag_plus,	
											preventCache: true,
											handleAs: "json",				
											headers: { "Content-Type": "application/json; charset=utf-8"}, 	
											handle: function(data,args) {
												
										 			  var message = "";
												      switch(args.xhr.status){
												         case 200:
												        		//Chiudo il popup
															 	if ($('#popup_name_dettaglio_profilo').is(':visible')) {
															 		$("#close").trigger('click');
															 		//Ricarico i dati del popup di dettaglio
															 		var url = appContext + "/profilazione_showDettaglioProfilo.action?codEdicolaWeb=" + codEdicolaWeb+"&codDl="+codFiegDl ;
																	openDiv('popup_name_dettaglio_profilo', 1200, 700, url);
															 		//Messaggio di avvenuto inserimento
																	$.alerts.dialogClass = "style_1";
																	jAlert('Inserimento effettuato con seccesso...!!!', msgAvviso.toUpperCase(), function() {
																		$.alerts.dialogClass = null;
																		return false;
																	});
															 	} 
												           break;
												         case 404:
												           message = "The requested page was not found";
												           break;
												         case 500:
												           message = "The server reported an error.";
												           break;
												         case 407:
												           message = "You need to authenticate with a proxy.";
												           break;
												         default:
												           message = "Unknown error.";
												      }
												      
												      if(message != ""){
												    	  	jAlert(message, attenzioneMsg.toUpperCase(), function() {
																$.alerts.dialogClass = null;
																return false;
															});
												      }
												    	  
														 	
											}
										});
									 
									 	
								    }
								 unBlockUI();
							});
					
						}
					}); //END button_aggiungi_profilazione
				
		
			getProfiliCoddl(dett_coddl);	
				
	});	
	
	
	
	
	function openMsgJAlert(msg){
		$.alerts.dialogClass = "style_1";
		jAlert(msg, msgAvviso.toUpperCase(), function() {
			$.alerts.dialogClass = null;
			return false;
		});
		
	}
	
	function getProfiliCoddl(coddl){
		var select_coddl = coddl;  
		ray.ajax();
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}/pubblicazioniRpc_getProfiliDL.action?select_coddl="+select_coddl+"",			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			preventCache: true,
			handle: function(data,args) {
				unBlockUI();
				if (args.xhr.status == 200) {
					$("select#dett_select_profili").find('option').remove();
					for (i = 0; i < data.length; i++) {
						$("select#dett_select_profili").append('<option value="'+data[i].key+'">' +data[i].value + '</option>');
					}
					
				} else {
					alert('not ok');
				} 
			}
			
			
	    });
	}
	
	
	
	
</script>