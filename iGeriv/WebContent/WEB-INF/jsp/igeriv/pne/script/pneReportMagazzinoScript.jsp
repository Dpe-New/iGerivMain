<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataIniziale').datepicker();
		$('#dataFinale').datepicker();
		
// 		$('#dataIniziale').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataFinale').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		$("#codiceCausale").change(function() {
			if ($(this).val() == 1) {
				$("#labelForn").text('<s:text name="igeriv.pne.report.magazzino.conto" />');
				$("#codiceConto").val(-1);
				populateFornitori();
			} else if ($(this).val() == 2) {
				$("#labelForn").text('<s:text name="username.cliente" />');
				$("#codiceConto").val(-1);
				populateClienti();
			} 
		});
		
		$("input#prodottoLabel").autocomplete({
			minLength: 4,			
			source: function(request, response) {		
				dojo.xhrGet({
					url: "${pageContext.request.contextPath}${ap}/automcomplete_getProdottoNonEditorialeByCodEdicolaOrDescr.action",
					delay: 100,
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=utf-8"}, 	
					content: { 
				    	term: request.term
				    }, 					
					handle: function(data,args) {
						response($.map(data, function(item) {
											return {
			                                	id: item.codProdotto,	
			                                    label: item.descrizione,
			                                    value: item.descrizione.replace('&#8364;', '\u20AC')		                                   
			                                }
			                            }))
					}
				});
			},
			select: function (event, ui) {
				document.getElementById('codProdotto').value = ui.item.id;		
			}
		});
		
		$("#azzeraCriteri").click(function() {
			$("#codiceConto").val(-1);
			$("#codCategoria").val(-1);
			$("#codiceCausale").val(-1);
		});
		
		if ($("#codiceCausale").val() == 1) {
			$("#labelForn").text('<s:text name="igeriv.pne.report.magazzino.conto" />');
			$("#codiceConto").val(-1);
			populateFornitori();
		} else if ($("#codiceCausale").val() == 2) {
			$("#labelForn").text('<s:text name="username.cliente" />');
			$("#codiceConto").val(-1);
			populateClienti();
		} 
		
	});
	
	function populateClienti() { 
		dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_getClientiEdicola.action',			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			preventCache: true,
			handle: function(data,args) {	
				var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].codCliente == '<s:property value="codiceConto"/>') {
	            		strChecked = ' selected ';
	            	}
	            	list += '<option value="' + data[i].codCliente + '" ' + strChecked + '">' +  data[i].nome + '</option>';
	            }
	            $("#codiceConto").empty();
	            $("#codiceConto").html(list);
			}
	    });
	}
	
	function populateFornitori() { 
		dojo.xhrGet({
			url: '${pageContext.request.contextPath}/pubblicazioniRpc_getFornitoriEdicola.action',			
			handleAs: "json",				
			headers: { "Content-Type": "application/json; charset=utf-8"}, 	
			preventCache: true,
			handle: function(data,args) {	
				var list = '';		
	            for (i = 0; i < data.length; i++) {
	            	var strChecked = '';
	            	if (data[i].codFornitore == '<s:property value="codiceConto"/>') {
	            		strChecked = ' selected ';
	            	}
	            	list += '<option value="' + data[i].codFornitore + '" ' + strChecked + '">' +  data[i].nome + '</option>';
	            }
	            $("#codiceConto").empty();
	            $("#codiceConto").html(list);
			}
	    });
	}
	
	function doSubmit() { 
		if ($("#prodottoLabel").val() == '') {
			document.getElementById('codProdotto').value = '';
		} else if (!isNaN($("#prodottoLabel").val())) {
			document.getElementById('codProdotto').value = $("#prodottoLabel").val();
		}
		return true;
	};
	
	function validateFields(showAlerts) {
		if ($("#dataIniziale").val() == '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace('{0}','<s:text name="igeriv.da"/>'), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					//$("#dataIniziale").focus();
				});
			}
			return false;
		} else if (!checkDate($("#dataIniziale").val())) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="msg.formato.data.invalido"/>'.replace('{0}', $("#dataIniziale").val()), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					//$("#dataIniziale").focus();
				});
			}
			return false;
		}
		if ($("#dataFinale").val() == '') {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.campo.x.obbligatorio"/>'.replace('{0}','<s:text name="igeriv.a"/>'), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					//$("#dataFinale").focus();
				});
			}
			return false;
		} else if (!checkDate($("#dataFinale").val())) {
			if (showAlerts) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="msg.formato.data.invalido"/>'.replace('{0}', $("#dataFinale").val()), '<s:text name="msg.alert.attenzione"/>', function() {
					$.alerts.dialogClass = null;
					//$("#dataFinale").focus();
				});
			}
			return false;
		}
		return true;
	}
</script>