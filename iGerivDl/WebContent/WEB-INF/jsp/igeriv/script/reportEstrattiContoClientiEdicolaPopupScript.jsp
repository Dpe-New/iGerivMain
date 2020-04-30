<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {
		addFadeLayerEvents();
		setContentDivHeight(10);
		$("#imgPrintEC, #imgDeleteEC").tooltip({
			delay: 0,  
		    showURL: false
		}); 
		$("#strDataA").focus();
	});
	
	function changeDataEstrattoConto(obj) {
		ray.ajax();
		var strData = $(obj).val();
		dojo.xhrGet({
			url: "pubblicazioniRpc_getEstrattoContoCliente.action?dataAStr=" + strData + "&codCliente=" + $("input:hidden[name='codCliente']").val(),	
			preventCache: true,
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=uft-8"}, 	
			handle: function(data,args) {
				unBlockUI();
				var html = '';
				var count = 0;
				var deleteEnabled = false;
				for(var i = 0; i < data.length; i++) {
			        var obj = data[i];
			        trClass = (count % 2 == 0) ? "even" : "odd";
			        html += '<tr class="' +  trClass + '" style="height: 25px;" onmouseover="this.className=\'highlight\'" onmouseout="this.className=\'' + trClass + '\'">';
			        html += '<td style="text-align: left;" width="55%">' + obj.titolo + '</td>';
			        html += '<td style="text-align: right;" width="15%">' + obj.prezzo + '</td>';
			        html += '<td style="text-align: center;" width="15%">' + obj.quantita + '</td>';
			        html += '<td style="text-align: right;" width="15%">' + obj.importo + '</td>';
			        html += '</tr>';
			        if (obj.deleteEnabled.toString() == "true") {
			        	deleteEnabled = true;
			        }
			        count++;
			    }
				if (!deleteEnabled) {
					$("#deleteECSpan").hide();
				} else {
					$("#deleteECSpan").show();
				}
				$("#EstrattoContoClientiTab_table tbody").html(html);
				$("#impTotale").html(getTotaleFormatted());
				$("#EstrattoContoClientiTab_table").tablesorter();
			}
		});
	}
	
	function getTotale() {
		var totale = 0;
		$("#EstrattoContoClientiTab_table tbody tr td:nth-child(4)").each(function() {
			totale += Number(parseLocalNum($(this).text().trim()));
		});
		var num = Number(totale).toFixed(2);
		return num;
	}
	
	function getTotaleFormatted() {
		return '&#8364;&nbsp;' + displayNum(getTotale());
	}
	
	function printEC() {
		if ($("#strDataA").val() != '') {
			$("#dataEstrattoConto").val($("#strDataA").val());
			document.forms.EstrattoContoClientiForm.ec_eti.value='';
			$("#EstrattoContoPopupClientiForm").attr("action", "report_emettiEstrattoContoSingoloClienteEdicola.action"); 	
			$("#EstrattoContoPopupClientiForm").attr("target", "_blank"); 
			$("#EstrattoContoPopupClientiForm").submit();
		}
	}
	
	function deleteEC() {
		var dtComp = $("#strDataA option:selected").text();
		var cliente = '<s:text name="nomeCliente" />';
		if (dtComp != '' && cliente != '') {
			jConfirm('<s:text name="gp.cancellare.estratto.conto.cliente"/>'.replace('{0}',cliente).replace('{1}',dtComp), attenzioneMsg, function(r) {
			    if (r) { 
			    	dojo.xhrGet({
						url: "${pageContext.request.contextPath}/pubblicazioniRpc_deleteEstrattoContoCliente.action?dataAStr=" + dtComp + "&codCliente=" + $("input:hidden[name='codCliente']").val() + "&nome=" + cliente,	
						preventCache: true,
						handleAs: "json",				
						headers: { "Content-Type": "application/json; charset=uft-8"}, 	
						handle: function(data,args) {
							unBlockUI();
							if (args.xhr.status == 200) {
								var result = data.result;
								if (typeof(result) != 'undefined' && result != '') {
									jAlert(result, informazioneMsg, function() {
										$("#ricerca").trigger("click");
							    		return false;
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
			}, true, false);
		}
	}
</script>