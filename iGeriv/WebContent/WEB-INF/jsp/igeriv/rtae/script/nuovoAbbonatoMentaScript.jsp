<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<script type="text/javascript">		
	$(document).ready(function() {
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
		$("#privacy").attr("checked", false);
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#abbonato\\.dataNascita').datepicker();
		
// 		$('#abbonato\\.dataNascita').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		$("#abbonato\\.cap").numeric(false);
		$("#abbonato\\.telefono").numeric(false);
		$("#abbonato\\.cellulare").numeric(false);
		
		<s:if test="%{saved != null}">
			jAlert('<s:text name="gp.dati.memorizzati"/>');
		</s:if>
		
		if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() <= 7) {
			<s:if test="#ap eq '/dl' || #ap eq '/cs' || #ap eq '/ed'">
				$("#filter").css({"height":"180px"});
				$("#contrattoFieldset").css({"height":"120px"});
			</s:if>
		}
		
		$("#idProdotto").change(function() {
			setPdfDoc(Number($(this).val()));
		});
		
		$("#abbonato\\.localita").autocomplete({
	   		minLength: 2,			
			source: function(request, response) {	
					url: "${pageContext.request.contextPath}${ap}/automcomplete_indirizzi.action",
					preventCache: true,
					handleAs: "json",				
					headers: { "Content-Type": "application/json; charset=uft-8"}, 	
					content: { 
				    	term: request.term
				    }, 					
					handle: function(data,args) {
						response($.map(data, function(item) {											
	                           return {
	                               id: item.id,	
	                               label: item.label,
	                               value: item.label,
	                               provincia: item.provincia
	                           }
	                       }))
					}
				});
			},
			select: function (event, ui) {					
				$('#abbonato\\.codLocalita').val(ui.item.id);	
				$('#abbonato\\.codProvincia').val(ui.item.provincia);	
			},
			focus: function(event, ui) { 
				$('#abbonato\\.codLocalita').val(ui.item.id);	
				$('#abbonato\\.codProvincia').val(ui.item.provincia);	
			}
		});
		
		setPdfDoc = function(id) {
			switch(id) {
				case 7: {
					$("#regolamento").html('<a id="regolamento" href="${pageContext.request.contextPath}/pdf/Regolamento_Gazzetta_di_Reggio_sito.pdf" target="_new"><s:text name="cliente.privacy.menta"/></a>');
					break;
				}
				case 8: {
					$("#regolamento").html('<a id="regolamento" href="${pageContext.request.contextPath}/pdf/Regolamento_nuova_Gazzetta_di_Modena_sito.pdf" target="_new"><s:text name="cliente.privacy.menta"/></a>');
					break;
				}
				default: {
					$("#regolamento").html('<s:text name="cliente.privacy.menta"/>');
				}
			}
		}
		
		 $("#abbonato\\.localita").blur(function() {
			 if ($(this).val().trim().length == 0) {
				 $("#abbonato\\.codLocalita").val('');
			 }
		 });
		 
		validateFields = function() {
			var nome = $("#abbonato\\.ragioneSocialePrimaParte").val().trim();
			var cognome = $("#abbonato\\.ragioneSocialeSecondaParte").val().trim();
			var dtNasc = $("#abbonato\\.dataNascita").val().trim();
			var cf = $("#abbonato\\.codFiscale").val().trim();
			var indirizzo = $("#abbonato\\.indirizzo").val().trim();
			var localita = $("#abbonato\\.codLocalita").val().trim();
			var cap = $("#abbonato\\.cap").val().trim();
			var telefono = $("#abbonato\\.telefono").val().trim();
			var cellulare = $("#abbonato\\.cellulare").val().trim();
			var email = $("#abbonato\\.email").val().trim();
			var idProdotto = $("#idProdotto").val();
			if (nome == null || nome == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.nome"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.ragioneSocialePrimaParte").focus();
				});
				return false; 
			}
			if (cognome == null || cognome == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.cognome"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.ragioneSocialeSecondaParte").focus();
				});
				return false; 
			}
			if (dtNasc == null || dtNasc == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.data.nascita"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.dataNascita").focus();
				});
				return false; 
			}
			if (cf == null || cf == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.cf"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.codFiscale").focus();
				});
				return false; 
			}
			if (indirizzo == null || indirizzo == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.indirizzo"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.indirizzo").focus();
				});
				return false; 
			}
			if (localita == null || localita == '') {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="validation.localita.required"/>', attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.localita").focus();
				});
				return false; 
			}
			if (cap == null || cap == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.cap"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.cap").focus();
				});
				return false; 
			}
			if ((telefono == null || telefono == '') && (cellulare == null || cellulare == '')) {
				if (telefono == null || telefono == '') {
					$.alerts.dialogClass = "style_1";
					jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.telefono"/>'), attenzioneMsg, function() {
						$.alerts.dialogClass = null;
						$("#abbonato\\.telefono").focus();
					});
					return false; 
				} else if (cellulare == null || cellulare == '') {
					$.alerts.dialogClass = "style_1";
					jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.cellulare"/>'), attenzioneMsg, function() {
						$.alerts.dialogClass = null;
						$("#abbonato\\.cellulare").focus();
					});
					return false; 
				}
			}
			if (idProdotto == null || idProdotto == '') {
				$.alerts.dialogClass = "style_1";
				jAlert(campoObbligatorio.replace('{0}', '<s:text name="cliente.prodotto"/>'), attenzioneMsg, function() {
					$.alerts.dialogClass = null;
					$("#idProdotto").focus();
				});
				return false; 
			}
			if (!checkDate(dtNasc)) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.field.formato.data.dd.mm.yyyy"/>'.replace('{0}', '<s:text name="cliente.data.nascita"/>'), '<s:text name="msg.alert.attenzione"/>'.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.dataNascita").focus();
				});
				return false;
			}
			if (!validateCodiceFiscale(cf)) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="error.cf.errato"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.codFiscale").focus();
				});
				return false;
			}
			if (email != null && email != '' && !checkEmail(email)) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="dpe.validation.msg.invalid.email"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#abbonato\\.email").focus();
				});
				return false;
			}
			if ($("#privacy").attr("checked") == false) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="cliente.privacy.validation.msg"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$("#privacy").focus();
				});
				return false;
			}
			if (email == null || email == '') {
				jConfirm('<s:text name="msg.email.nulla.confirm"/>', '<s:text name="msg.alert.attenzione"/>', function(r) {
				    if (r) { 
				    	$("#clienteEditForm").submit();
				    } else {
				    	$("#abbonato\\.email").focus();
				    	return false; 
				    }
				});
				return false; 
			}
			return true;
		}
		
		doSubmit = function() {
			if (validateFields()) {
				$("#clienteEditForm").submit();
			}
		}
		
		setPdfDoc(Number($("#idProdotto").val()));
		
		$("#abbonato\\.ragioneSocialePrimaParte").focus();
	});
	
</script>
