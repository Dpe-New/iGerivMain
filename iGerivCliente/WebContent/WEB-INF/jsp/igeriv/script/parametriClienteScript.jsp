<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var valParam = '<s:property value="valParametro"/>';
	$(document).ready(function() {
		$("input[id^='paramEdicola']").change(function() {
			doChange($(this));
		});
		
		$("textarea[id^='paramEdicola']").change(function() {
			doChange($(this));
		});
		
		$("select[id^='paramEdicola']").change(function() {
			var $sel = $(this);
			doChange($sel);
			showFileManualeInstallazione($sel);
		});
		
		$("input[type='checkbox'][id^='__checkbox_paramEdicola']").each(function() {
			var val = $(this).val();
		});
		
		$("textarea[id^='paramEdicola']").each(function() {
			var val = $(this).val().replace(/<br>/g,"\n");
			$(this).val(val);
		});
		
		function doChange($el) {
			var id = $el.attr("id");
			var codParametro = id.substring(id.indexOf("paramEdicola") + 12);
			var valParametro = $el.attr("type") == 'checkbox' ? $el.is(":checked") : $el.val();
			var sqlType = $el.attr("accesskey");
			if (!validateFields(valParametro, sqlType)) {
				$.alerts.dialogClass = "style_1";
				jAlert(wrongDataType, attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					$el.css({"borderColor":"red"}); 
					$el.focus();
				});
				return false;
			}
			$el.css({"borderColor":"#999"}); 
			$("#codParametro").val(codParametro);
			$("#valParametro").val(valParametro.toString().replace(/\n/g,"<br>"));
			formSubmitMultipartAjax('ParamEdicolaForm','html', paramEdicolaSuccessCallback, null, true);
		}
		
		$("#changeImg").tooltip({
			delay: 0,  
		    showURL: false
		}).click(function() {
			jConfirm('<s:text name="igeriv.email.modifiy.confirm"/>', attenzioneMsg.toUpperCase(), function(r) {
			    if (r) { 
			    	dojo.xhrGet({
						url: "${pageContext.request.contextPath}/params_deleteEmailCliente.action",	
						preventCache: true,
						handleAs: "text",				
						load: function(data,args) {
							promptForEmail('${pageContext.request.contextPath}/<s:text name="actionName"/>');
						},
						error: function(data,args) {
							$.alerts.dialogClass = "style_1";
							jAlert(msgErroreInvioRichiesta, attenzioneMsg.toUpperCase(), function() {
								$.alerts.dialogClass = null;
							});
						}
					});
			    } 
			});
		});
	});
	
	function paramEdicolaSuccessCallback(bodyContent) {
		var codParametro = $("#codParametro").val();
		if (codParametro == 7) {
			window.location.href = "${pageContext.request.contextPath}/params_showParams.action";
		} else if (codParametro == 15) {
			$("#imgProd").attr("src", "/immagini_miniature_edicola/" + $("#valParametro", $(bodyContent)).val());
		}
	}
	
	function validateFields(valParametro, sqlType) {
		switch(sqlType) {
			case 'INTEGER':
				return bINT(valParametro);
			case 'SMALLINT':
				return bINT(valParametro);
			case 'TINYINT':
				return bINT(valParametro);
			case 'BIGINT':
				return bINT(valParametro);
			case 'LONG':
				return bINT(valParametro);
			case 'FLOAT':
				return bDecimal(valParametro);
			case 'DOUBLE':
				return bDecimal(valParametro);
			case 'BIGDECIMAL':
				return bDecimal(valParametro);
			case 'NUMERIC':
				return bDecimal(valParametro);
			case 'BOOLEAN':
				return bBoolean(valParametro);
			case 'DATE':
				return checkDate(valParametro);
			case 'TIMESTAMP':
				return checkDate(valParametro);
		}
		return true;
	}
</script>