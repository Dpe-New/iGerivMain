<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var valParam = '<s:property value="valParametro"/>';
	$(document).ready(function() {
		
		$("#paramEdicola17").val('<s:property value="numIniFatture"/>');
		$("#paramEdicola18").val('<s:property value="numIniEC"/>');
		
		$("#paramEdicola12").val('<s:text name="%{#session.mapParamsEdicola.paramEdicola12.paramValue}"/>');
		$("#paramEdicola12").change(function() {
			if (Number($(this).val()) > 0) {
				$("#paramEdicola23").attr("disabled", false);
			} else {
				$("#paramEdicola23").attr("disabled", true);
			}
		});
		if ($("#paramEdicola12").val() > 0) {
			$("#paramEdicola23").attr("disabled", false);
		}
		$("#paramEdicola22").change(function() {
			if ($(this).is(":checked")) {
				$.alerts.dialogClass = "style_1";
				jAlert('<s:text name="igeriv.params.edicola.22.help.msg"/>', attenzioneMsg.toUpperCase(), function() {
					$.alerts.dialogClass = null;
					doChange($("#paramEdicola22"));
					$("#paramEdicola24").attr("disabled", false);
					$("#paramEdicola24").attr("checked", true);
					$("#paramEdicola24").trigger("change");
					return true;
				});
				return false;
			} else {
				doChange($(this));
				$("#paramEdicola24").attr("checked", false);
				$("#paramEdicola24").attr("disabled", true);
				$("#paramEdicola24").trigger("change");
			}
		});
		if ($("#paramEdicola22").is(":checked")) {
			$("#paramEdicola24").attr("disabled", false);
		}
		
		showFileManualeInstallazione($("#paramEdicola12"));
		
		$("input[id^='paramEdicola']").change(function() {
			if ($(this).attr("id") != 'paramEdicola22') {
				doChange($(this));
			}
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
						url: "${pageContext.request.contextPath}/params_deleteEmailEdicola.action",	
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
		
		addDeleteImg($("#imgProd"));
	});
	
	function addDeleteImg(obj) {
		obj.css({"border-style":"solid","border-width":"1px","border-color":"black"});
		obj.parent().after('<div style="float:right; width:6%; height:60px; vertical-align:middle;"><img id="eliminaImgProd" style="cursor:pointer" src="/app_img/remove.jpg" alt="<s:text name="dpe.contact.form.elimina"/>" border="0" title="<s:text name="dpe.contact.form.elimina"/>"/></div>');
        $("#eliminaImgProd").click(function() {
			$("#imgProd").attr("src", "");
			$("#paramEdicola15").val("");
			$("#paramEdicola15").trigger("change");
		});
        $("#eliminaImgProd").tooltip({
			delay: 0,  
		    showURL: false
		});
	}
	
	function paramEdicolaSuccessCallback(bodyContent) {
		var codParametro = $("#codParametro").val();
		if (codParametro == 7) {
			window.location.href = "${pageContext.request.contextPath}/params_showParams.action";
		} else if (codParametro == 15) {
			$("#imgProd").attr("src", "/immagini_miniature_edicola/" + $("#valParametro", $(bodyContent)).val());
		}
	}
	
	function showFileManualeInstallazione($sel) {
		var $opSel = $sel.find('option:selected');
		if ($sel.attr("id") == 'paramEdicola12' && $opSel.attr("filename") && $opSel.attr("filename").trim() != '') {
			$("#fileNameManualeSpan").html("<a target='_new' href='/app_img/" + $opSel.attr("filename") + "'><s:text name="igeriv.manuale.installazione.risoluzione.problemi"/></a>");
			$("#testJavaLinkSpan").html("<a target='_new' href='<s:text name="igeriv.test.java.link"/>'><s:text name="igeriv.verifica.installazione.java"/></a>");
		} else {
			$("#fileNameManualeSpan").html("");
			$("#testJavaLinkSpan").html("");
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