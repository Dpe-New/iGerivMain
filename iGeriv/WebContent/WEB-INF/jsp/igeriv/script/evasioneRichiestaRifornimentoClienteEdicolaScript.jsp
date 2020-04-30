<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>	
	var tabCount = 0;
	$(document).ready(function() {

		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDa').datepicker();
		$('#dataA').datepicker();
		$('#dataCompEC').datepicker();
		
// 		$('#dataDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataA').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$("#dataCompEC").click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		setContentDivHeight(50);
		
		$('#EvasioneClienteEdicolaForm input:text[name^=messLibero]').each(function() {
			$(this).val("");
		});
		
		<s:if test="%{#request.prenotazioneEvasioneQuantitaEvasaEmpty == false}">
			$('#EvasioneClienteEdicolaForm input:text[name^=evaso]').each(function() {
				var qta = $(this).parent("td").prev().text();
				$(this).val(qta);
			});
		</s:if>
		<s:else>
			$('#EvasioneClienteEdicolaForm input:text[name^=evaso]').each(function() {
				$(this).val('');
			});
		</s:else>
		
		<s:if test="%{#request.emailValido == false}">
			unBlockUI();
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.msg.email.non.registrata"/>', '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
				$.alerts.dialogClass = null; 
				promptForEmail('${pageContext.request.contextPath}${ap}/<s:text name="actionName"/>');
			});
		</s:if>
		
		$("#memorizza").removeAttr("disabled");
		$('#EvasioneClienteEdicolaForm input:text[name^=evaso]').first().focus();
	});
	
	function validateExtraFields(showMsg) {
		var ret = true;
		var hasValues = false;
		$('#EvasioneClienteEdicolaForm input:text[name^=evaso]').each(function() {
			var sel = $(this).closest('tr').find('select[name^=ultimaRisposta]').first();
			var qtaDaEvadere = $(this).parent().prev().text().trim();
			var $currField = $(this);
			var $qtaEvasa = $currField.val().trim();
			if ($qtaEvasa.length > 0) {
				hasValues = true;
			}
			if ($qtaEvasa.length > 0 && $qtaEvasa > 0 && $qtaEvasa.trim().length > 0 && Number($qtaEvasa.trim()) > Number(qtaDaEvadere)) {
				if (showMsg) {
					$.alerts.dialogClass = "style_1";
					jAlert('<s:text name="msg.quantita.evasa.superiore.quantita.evadere"/>', attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						setTimeout('unBlockUI();', 100);
					});
				}
				ret = false;
			} else if ($qtaEvasa.length > 0 && $qtaEvasa < qtaDaEvadere && sel.val() == '') {
				if (showMsg) {
					$.alerts.dialogClass = "style_1";
					jAlert('<s:text name="msg.necessario.messaggio.codificato"/>', attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
						setTimeout('unBlockUI();', 100);
						$currField.focus();
					});
				}
				ret = false;
			}
		});	
		if (!hasValues) {
			if (showMsg) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.msg.nessuna.quantita.evadere"/>', '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
				});
			}
			ret = false;
		}
		return ret;
	}
	
	function creaContoCliente() {
		$("#idConto1").val($("#idConto").val());
		$("#contoCliente").val('${nomeCliente}');
		$("#ContoClienteForm").submit();
	}
	
	function setFieldsToSave(isSubmit) {	
		$("#EvasioneClienteEdicolaForm input:hidden[name='evaso']").each(function() {
			$(this).attr('disabled', true);
		});
		$("#EvasioneClienteEdicolaForm input:hidden[name='qtaDaEvadere']").not(":last").each(function() {
			$(this).remove();
		});
		$("#EvasioneClienteEdicolaForm input:hidden[name='pk']").each(function() {
			if ($(this).attr("id").indexOf("pkevaso") == -1) {
				$(this).remove();
			}
		});
		$("#EvasioneClienteEdicolaForm input:hidden[name='ultimaRisposta'],#EvasioneClienteEdicolaForm input:hidden[name='messLibero']").each(function() {
			$(this).remove();
		});
		$("#EvasioneClienteEdicolaForm input:text[name='evaso']").each(function() {
			var $evaso = $(this);
			var $tr = $evaso.closest("tr");
			var $pk = $tr.find("input:hidden[name='pk']").first();
			var $msgCod = $tr.find("select").first();
			var $msgLibero = $tr.find("input:text[name='messLibero']").first();
			var disabled;
			if ($evaso.val().trim().length == 0) {
				disabled = true;
			} else {
				disabled = false;
			}
			$evaso.attr('disabled', disabled);
			$pk.attr('disabled', disabled);
			$msgCod.attr('disabled', disabled);
			$msgLibero.attr('disabled', disabled);
		});
		var qtaEvadere = [];
		var colIndex = <s:if test="spunte != null && !spunte.isEmpty()">6;</s:if><s:else>5;</s:else>
		$("#RichiesteRifornimentiClienteEdicola_table>tbody>tr>td:nth-child(" + colIndex + ")").each(function() {
			qtaEvadere.push($(this).text());
		});
		$("#qtaDaEvadere").val(qtaEvadere.toString());
		return true;
	}
	
	function submitForm(formid){
		dojo.xhrPost({
			form: formid,			
			handleAs: "text",		
			handle: function(data,args) {
				unBlockUI();
				var $doc = null;
				if (getBrowser().indexOf("MSIE") != -1) {
					$doc = document.createElement('div');
					$doc.innerHTML = data;
				} else {
					$doc = $(data);
				}
				var errMsg = $('igerivException', $doc).length > 0 ? $('igerivException', $doc).html() : '';
				if (errMsg != '') {
					$.alerts.dialogClass = "style_1";
					jAlert(errMsg, attenzioneMsg.toUpperCase(), function() {
						$.alerts.dialogClass = null;
					});
				} else {
					setContentDivHeight(50);
					if ($('#RichiesteRifornimentiClienteEdicola_table', $doc).length > 0) {
						dojo.byId('contentDiv').innerHTML = $('#contentDiv', $doc).html();	
						addTableLastRow("RichiesteRifornimentiClienteEdicola_table");	
						$("#print").tooltip({ 
							delay: 0,  
						    showURL: false
						});
					} else {
						dojo.byId('contentDiv').innerHTML = '<div style="width:100%; text-align:center; color: #444444; font-weight: bold; font-size: 15px; vertical-align: middle;"><s:text name="igeriv.evasione.conclusa"/></div>';
					}
					if ($('#breadcrumbDiv', $doc).html().length > 0) {
						dojo.byId('breadcrumbDiv').innerHTML = $('#breadcrumbDiv', $doc).html();
					}
				}
			} 
		});
	}
	
	function printRicevuta() {
		var $table = $('#RichiesteRifornimentiClienteEdicola_table');
		var $rows = $('tbody > tr', $table);
		var arrRows = new Array();
		var n = '';
		var arrClientRows = new Array();
		arrRows.push(arrClientRows);
		$.each($rows, function(index, row){
			var name = $(row).find("td:nth-child(1)").text().trim();
			if (n != '' && n != name) {
				arrClientRows = new Array();
				arrRows.push(arrClientRows);
			}
			if ($(row).hasClass("odd") || $(row).hasClass("even")) {
				arrClientRows.push(row);
			}
			n = name;
		});
		
		$.each(arrRows, function(index, arr) {
			$("#ReportVenditeForm").empty();
			var nome = '';
			var idCliente = 0;
			$.each(arr, function(i, clientArr) {
				var $row = $(clientArr);
				var prog = i + 1;
				nome = $row.find("td:nth-child(1)").text().trim();
				var query= $row.attr("divparam").split('?');
			    var dim= query[1].split('&');	   	   	    	  		  
			    var idtnPub = dim[0].split('=')[1];
			    idCliente = dim[1].split('=')[1];
			    var prezzoStr = $row.find("td:nth-child(8)").text().trim();
				var prezzoCopertina = parseLocalNum(prezzoStr);	
				var tit = $row.find("td:nth-child(3)").text();
				var sottotit = $row.find("td:nth-child(4)").text();
				var numcop = $row.find("td:nth-child(5)").text();
				var quantita = $row.find("td:nth-child(6)").text();
				var dtUscita = $row.find("td:nth-child(7)").text();
				var progElem = '<input type="hidden" name="vendite[' + i + '].progressivo" value="' + prog + '"/>';
				var idtnElem = '<input type="hidden" name="vendite[' + i + '].idtn" value="' + idtnPub + '"/>';
				var importoElem = '<input type="hidden" name="vendite[' + i + '].importo" value="' + prezzoCopertina + '"/>';
				var qtaElem = '<input type="hidden" name="vendite[' + i + '].copie" value="' + quantita + '"/>';
				var titoloElem = '<input type="hidden" name="vendite[' + i + '].titolo" id="tit' + i + '" value="' + tit.replace(/\&#8364;/g,'Euro').replace(/\u20ac/g,'Euro') + '"/>';
				var sottoTitoloElem = '<input type="hidden" name="vendite[' + i + '].sottoTitolo" value="' + sottotit.replace(/\&#8364;/g,'Euro').replace(/\u20ac/g,'Euro') + '"/>';
				var numeroCopertinaElem = '<input type="hidden" name="vendite[' + i + '].numeroCopertina" value="' + numcop + '"/>';
				var dtUscitaElem = '<input type="hidden" name="vendite[' + i + '].dataUscita" value="' + dtUscita + '"/>';
				$("#ReportVenditeForm").append(progElem);
				$("#ReportVenditeForm").append(idtnElem);
				$("#ReportVenditeForm").append(importoElem);
				$("#ReportVenditeForm").append(qtaElem);
				$("#ReportVenditeForm").append(titoloElem);
				$("#ReportVenditeForm").append(sottoTitoloElem);
				$("#ReportVenditeForm").append(numeroCopertinaElem);
				$("#ReportVenditeForm").append(dtUscitaElem);
				$("#ReportVenditeForm").append('<input type="hidden" name="vendite[' + i + '].codCliente" value="' + idCliente + '"/>');
			});
			var ragSocInput = '<input type="hidden" name="ragSocCliente" value="' + nome.toUpperCase() + '"/>';
			$("#ReportVenditeForm").append(ragSocInput);
			$("#ReportVenditeForm").attr("target", "tab" + tabCount);
			window.open('','tab' + tabCount);
			$("#ReportVenditeForm").submit();
			tabCount++;
		});
	}
	
</script>
