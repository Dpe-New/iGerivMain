<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var erroreInvioRichiesta = "<s:text name='msg.errore.invio.richiesta'/>";
	var msgCopieFuoriCompetenza = "<s:text name='msg.copie.fuori.competenza'/>";
	var titleCopieFuoriCompetenza = "<s:text name='msg.copie.fuori.competenza.title'/>";
	var msgConfirmInserimentoCopieFuoriCompetenza = "<s:text name='msg.confirm.inserimento.copie.fuori.competenza'/>";
	var msgRigaInseritaBolla = "<s:text name='msg.creata.nuova.riga.bolla'/>";
	
	setDataTipoBolla("<s:property value='dataTipoBolla'/>", "dataTipoBolla");
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
	<s:if test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">
		$(document).ready(function() {	
			$("#SonoInoltreUsciteTab_table").thfloat();
			addFadeLayerEvents();
			setContentDivHeight(20);
			$("img[name='fuori_competenza']").tooltip({ 
				delay: 0,  
			    showURL: false
			});
		});
		
		getQuantitaFuoriCompetenzaInserita = function(idtn, pk) {
			var qta = 0;
			dojo.xhrGet({
				url: "${pageContext.request.contextPath}/sonoInoltreUsciteJ_getQuantitaFuoriCompetenzaInserita.action?idtn=" + idtn + "&pk=" + pk,	
				preventCache: true,
				handleAs: "json",	
				sync: true,
				headers: { "Content-Type": "application/json; charset=utf-8"},
				handle: function(data,args) {
					if (args.xhr.status == 200) {
						qta = Number(data.qta);
					}
				}	
			});
			return qta;
		}
		
		promptSaveCopieFuoriCompetenza = function(pk, idtn, titolo, numero) {
			var dt = $("#dataTipoBolla").val().split("|")[0];
			var tp = $("#dataTipoBolla").val().split("|")[1].replace("Tipo","").trim();
			var qta = getQuantitaFuoriCompetenzaInserita(idtn, pk);
			jPrompt($.validator.format(msgCopieFuoriCompetenza, [titolo, numero]), qta, titleCopieFuoriCompetenza, function(res) {
				if (res != null && !isNaN(res) && Number(res) > 0) {
					jConfirm($.validator.format(msgConfirmInserimentoCopieFuoriCompetenza, [titolo, numero, dt, tp]), '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function(r) {
						if (r) {
							$("#pkCopieFuoriCompetenza").val(pk);
							$("#idtnCopieFuoriCompetenza").val(idtn);
							$("#quantitaCopieFuoriCompetenza").val(res);
							dojo.xhrPost({
								form: "CopieFuoriCompetenzaForm",			
								handleAs: "text",		
								handle: function(data,args) {
									if (args.xhr.status == 200) {
										jAlert($.validator.format(msgRigaInseritaBolla, [titolo, numero, dt, tp]));
									} else {
										jAlert(erroreInvioRichiesta);
									}
								}		
							});
						}
					}, true, true);
				}
			});
			setTimeout(function() {
				$("#popup_prompt").numeric({ decimal : false , negative : false });
			}, 200);
		}
		
		$("#SonoInoltreUsciteTab_table tbody tr td").not(":nth-child(8)").not(":last-child").click(function() { 
				var popID = 'popup_name_rifornimenti';	   
			    var popURL = $(this).parent().attr('divParam'); 
			    var popWidth = 900;
			 	var popHeight = 500;
			    var query= popURL.split('?'); 
			    var dim= query[1].split('&');	   	    	    	  		  
			    var idtn = dim[0].split('=')[1];
			    var periodicita = '';
			    var coddl = '';
			    if (dim.length > 1) {
			    	periodicita = dim[1].split('=')[1];
			    	coddl = dim[2].split('=')[1];
			    }
			 	var url = "${pageContext.request.contextPath}/sonoInoltreUscite_showRichiesteRifornimenti.action?idtn=" + idtn + "&coddl=" + coddl;
			 	if (periodicita != '') {
			 		url += "&periodicita=" + periodicita;
			 	}
			 	//ticket 0000374
		        openDivRifornimenti(popID, popWidth, popHeight, url);
				//openDiv(popID, popWidth, popHeight, url);
			}
		);
		
		$("table").find("td:last-child").click(function(){	
			var link = document.getElementById($(this).find('a').attr('id'));				
			thumbnailviewer.stopanimation();
			thumbnailviewer.loadimage(link);
			return false;
		});					
	</s:if>
</script>
