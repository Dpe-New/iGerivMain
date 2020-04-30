<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var valParam = '<s:property value="valParametro"/>';
	$(document).ready(function() {
		
		$("#paramEdicola17").val('<s:property value="numIniFatture"/>');
		$("#paramEdicola18").val('<s:property value="numIniEC"/>');
		
		$("#paramEdicola12").val('<s:text name="%{#session.paramEdicola12.paramValue}"/>');
		showFileManualeInstallazione($("#paramEdicola12"));
		
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
			//alert(val);
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