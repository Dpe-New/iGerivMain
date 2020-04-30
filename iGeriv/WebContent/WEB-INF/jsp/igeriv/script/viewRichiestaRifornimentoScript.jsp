<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {	
		$('#RichiestaRifornimentoTab_table tbody tr td:nth-child(11), #RichiestaRifornimentoTab_table tbody tr td:nth-child(12), #RichiestaRifornimentoTab_table tbody tr td:nth-child(13)').each(function() {
			if ($(this).text().length > 0) {
				$(this).css({"color":"red"});
			}
		}); 
		
		$('#RichiestaRifornimentoTab_table tbody tr').each(function() {
			var $tr = $(this);
			if ($tr.attr("rigaEvasione") == 'true') { 
				$tr.find("td:nth-child(2)").text('');
				$tr.find("td:nth-child(11)").text('<s:text name="igeriv.evaso"/>');
				$tr.contents('td').css({'border-top': '1px solid red','border-bottom': '1px solid red','border-left': 'none', 'border-right': 'none'});
				$tr.contents('td:first').css('border-left', '1px solid red');
				$tr.contents('td:last').css('border-right', '1px solid red');
			}
		});
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDa').datepicker();
		$('#dataA').datepicker();
		
// 		$('#dataDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataA').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		onCloseLayer = function() {
			$("#ricerca").trigger("click");
		}
		
		$("#RichiesteRifornimentiClienteEdicola_table tbody tr").each(function() {
			var $tr = $(this);
			var $tds = $tr.find("td"); 
			var $chk = $tds.filter(":first-child").find("input[type='checkbox']").first();
			if ($chk.length > 0) {
				$tr.css('cursor','pointer');
				var tdsFilter = $tds.filter(":not(:first-child)");
				tdsFilter.click(function() {
				    var chkName = $chk.attr("name");
				    var val = $chk.val();
				    if (chkName == 'srf') {
				    	var idCliente = val.split("|")[1];
				    	var coddl = val.split("|")[2];
				    	var cpu = val.split("|")[3];
				    	var idtn = $chk.attr("idtn");
				    	var url = "${pageContext.request.contextPath}/bollaRivendita_showPrenotazioniClienteEdicola.action?idCliente=" + idCliente + "&cpu=" + cpu + "&coddl=" + coddl + "&idtn=" + idtn;
				    } else {
				    	var idCliente = val.split("|")[1];
				    	var coddl = val.split("|")[4];
				    	var idtn = val.split("|")[5];
				    	var url = "${pageContext.request.contextPath}/bollaRivendita_showPrenotazioniClienteEdicola.action?idtn=" + idtn + "&idCliente=" + idCliente + "&coddl=" + coddl;
				    }
				    openDiv('popup_name', 900, 350, url);
				});
				tdsFilter.tooltip({
					delay: 0,  
				    showURL: false,
				    bodyHandler: function() { 
				    	return '<b><s:text name="igeriv.modifica.richieste.inserite"/></b>';
				    }
				});
			}
		});
		
		setContentDivHeight(100);
		$("input:hidden[name='ec_eti']").val('');
		//$("#dataDa").focus();
	});
	
	function deleteSelected() {
		var arrSrf = new Array();
		var arrSr = new Array();
		$('#RichiesteRifornimentiClienteEdicola_table tbody tr td:nth-child(1)').each(function() {
			var $td = $(this);
			var $chk = $td.find("input[type='checkbox']").first();
			if ($chk && $chk.attr("checked") == true) {
				if ($chk.attr("name") == 'srf') { 
					arrSrf.push($chk.val());
				} else if ($chk.attr("name") == 'sr') { 
					arrSr.push($chk.val());
				}
			}
		});
		if (arrSrf.length == 0 && arrSr.length == 0) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.nessuna.prenotazione.selezionata"/>', attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
				unBlockUI();
			});
			return false;
		}
		dojo.xhrGet({
			url: "${pageContext.request.contextPath}${ap}/viewClienteEdicola_deletePrenotazioniClientiEdicola.action?spunteRichiestaFissa=" + arrSrf.toString() + "&spunteRichiesta=" + arrSr.toString(),	
			preventCache: true,
			handleAs: "text",				
			load: function(data,args) {
				unBlockUI();
				jAlert('<s:text name="gp.dati.memorizzati.delete"/>', '<s:text name="msg.alert.info"/>', function() {
					$("#RichiesteRifornimentiClienteEdicolaForm").submit();
				});
			},
			error: function(data,args) {
				unBlockUI();
				$.alerts.dialogClass = "style_1";
				jAlert(msgErroreInvioRichiesta, '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
				});
			}
		});
	}
</script>