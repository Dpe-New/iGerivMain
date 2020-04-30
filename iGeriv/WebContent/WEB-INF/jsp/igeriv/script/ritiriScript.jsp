<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		<s:if test="codCliente != null">
			$("#codCliente").val('<s:property value="codCliente"/>');
		</s:if>
		if ($("#ritiriCanc").length > 0) {
			$("#ritiriCanc").tooltip({
				delay: 0,  
			    showURL: false
			}); 
		}
		<s:if test="hasRitiriCancellati eq true">
			if ($("#ritiriCanc").length > 0) {
				$("#ritiriCanc").click(function() {
					openRitiriCancellati();			
				});
			}
		</s:if>
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataDa').datepicker();
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
		setContentDivHeight(30); 
		$("#ricerca").click(function() {
			var dataDa = $("#strDataDa").val();
			var dataA = $("#strDataA").val();
			var codCliente = $("#codCliente").length > 0 ? $("#codCliente").val() : '';
			if (dataDa.trim() != '' && !checkDate(dataDa)) {
				$.alerts.dialogClass = "style_1";
				jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					//$('#strDataDa').focus();
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
					//$('#strDataA').focus();
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false; 
			} 
			<s:if test="authUser.tipoUtente == 1">
				if (codCliente.trim() == '') {
					$.alerts.dialogClass = "style_1";
					jAlert(campoObbligatorio.replace('{0}','<s:text name="username.cliente"/>'), attenzioneMsg, function() {
						$.alerts.dialogClass = null;
						$("#codCliente").focus();
					});
					setTimeout(function() {
						unBlockUI();					
					}, 100);
					return false; 
				} 
			</s:if>
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
			if (days > 360) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.intervallo.date.eccede.limite"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					setTimeout(function() {
						//$('#strDataDa').focus();					
					}, 100);
				});
				setTimeout(function() {
					unBlockUI();					
				}, 100);
				return false;
			} else {
				$("#RitiriFilterForm").submit();
			}
		});
		openRitiriCancellati = function() {
			var codCliente = $("#codCliente").length > 0 ? $("#codCliente").val() : '';
		 	var url = "${pageContext.request.contextPath}/ritiri_showRitiriCanc.action?codCliente=" + codCliente;
			openDiv('popup_name', 700, 480, url);
		};
		//$("#strDataDa").focus();
	});
	
	function deleteRitiri() {
		var pkArr = new Array();
		$("#RitiriForm input:checkbox[name='pk']").each(function() {
			if ($(this).attr("checked") == true) {   	     	  		  
				pkArr.push($(this).val());
			}
		});
		if (pkArr.length > 0) {
			jConfirm('<s:text name="gp.vuoi.cancellare.ritiri.selezionati.da.conto.cliente"/>', attenzioneMsg, function(r) {
			    if (r) { 
			    	dojo.xhrGet({
						url: "${pageContext.request.contextPath}/pubblicazioniRpc_deleteRitiriCliente.action?nome=" + $("#codCliente option:selected").text() + "&pk=" + pkArr.toString(),	
						preventCache: true,
						handleAs: "json",				
						headers: { "Content-Type": "application/json; charset=utf-8"}, 	
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
			    $("#inputText").focus();
			}, true, false);
		}
	}
</script>
