<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		<s:if test="showInsertImg eq true">
			$("html, body").animate({ scrollTop: $(document).height() }, 1000);
		</s:if>
		
		$("#posizionamento").change(function() {
			manageCentroCommerciale();
		});
		
		$("#giornali").change(function() {
			setMerceologiaPrevalenteEnabled(false); 
			$("#tdMqTitle").html('<font color="#000"><b><s:text name="igeriv.aeca.mq.superficie" /></b></font>');
			$("#tdMq").html('<s:textfield name="anagraficaEdicolaAggiuntiviVo.mqSuperficieFormat" id="mqSuperficieFormat" maxlength="8" size="8"/>');
			$("#mtlineariPerGiornali").val("");
			$("#mqSuperficieFormat").numeric({ decimal : ",", negative : false });
		});
		
		$("#promiscuo").change(function() {
			setMerceologiaPrevalenteEnabled(true);
			$("#tdMqTitle").html('<font color="#000"><b><s:text name="igeriv.aeca.mt.lineari" /></b></font>');
			$("#tdMq").html('<s:textfield name="anagraficaEdicolaAggiuntiviVo.mtlineariPerGiornaliFormat" id="mtlineariPerGiornaliFormat" maxlength="8" size="8"/>');
			$("#mqSuperficie").val("");
			$("#mtlineariPerGiornaliFormat").numeric({ decimal : ",", negative : false });
		});
		
		mpNessunaClick = function() {
			if ($("#mpNessuna").is(":checked")) {
				$("input[type='checkbox'][class='merc']").attr('checked',false);
				$("input[type='checkbox'][class='merc']").attr('disabled','disabled');
				$("#mpNessuna").attr('checked',true);
			} else {
				$("input[type='checkbox'][class='merc']").removeAttr('disabled');
				$("#mpNessuna").attr('checked',false);
			}
			$("#mpNessuna").removeAttr('disabled');
		};
		
		$("#mpNessuna").click(mpNessunaClick);
		
		paNessunaClick = function() {
			if ($("#paNessuna").is(":checked")) {
				$("input[type='checkbox'][class='pattr']").attr('checked',false);
				$("input[type='checkbox'][class='pattr']").attr('disabled','disabled');
				$("#paNessuna").attr('checked',true);
			} else {
				$("input[type='checkbox'][class='pattr']").removeAttr('disabled');
				$("#paNessuna").attr('checked',false);
			}
			$("#paNessuna").removeAttr('disabled');
		};
		
		$("#paNessuna").click(paNessunaClick);
		
		manageCentroCommerciale();
		setMerceologiaPrevalenteEnabled($("#promiscuo").attr('checked'));
		mpNessunaClick();
		paNessunaClick();
		
		<s:if test="anagraficaEdicolaAggiuntiviVo.mqSuperficie != null && anagraficaEdicolaAggiuntiviVo.mqSuperficie > 0">
			$("#giornali").attr("checked", true);
			$("#giornali").trigger("change");
		</s:if>
		<s:elseif test="anagraficaEdicolaAggiuntiviVo.mtlineariPerGiornali != null && anagraficaEdicolaAggiuntiviVo.mtlineariPerGiornali > 0">
			$("#promiscuo").attr("checked", true);
			$("#promiscuo").trigger("change");
		</s:elseif>
		<s:else>
			if ($("#giornali").is(":checked")) {
				$("#giornali").trigger("change");
			} else {
				$("#promiscuo").trigger("change");
			}
		</s:else>
	});
	
	function manageCentroCommerciale() {
		var codPosizionamento = $("#posizionamento").val();
		
		if (codPosizionamento == '6') {
			// centro commerciale
			$("#attributo option[value='1']").removeAttr('disabled');
			$("#attributo option[value='2']").removeAttr('disabled');
		}
		else {
			if ($("#attributo").val() == '1' || $("#attributo").val() == '2') {
				$("#attributo").val('');
			}
			$("#attributo option[value='1']").attr('disabled', 'disabled');
			$("#attributo option[value='2']").attr('disabled', 'disabled');
		}
	}
	
	function setMerceologiaPrevalenteEnabled(enabled) {
		if (enabled) {
			$("input[type='checkbox'][class='merc']").removeAttr('disabled');
		}
		else {
			$("input[type='checkbox'][class='merc']").attr('disabled','disabled');
		}
	}
	
	function validateForm() {
		var hasMerceologia = $("input[type='checkbox'][class='merc']").filter(":checked").length > 0 ? true : false;
		var hasPoliAttr = $("input[type='checkbox'][class='pattr']").filter(":checked").length > 0 ? true : false;
		var hasSottoscrittoCondizioni = $("#condUsoAccettate").is(":checked");
		
		if ($("#promiscuo").is(":checked") && !hasMerceologia) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.error.merceologia.prevalente"/>', attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				unBlockUI();
			});
			return false; 
		}
		
		if (!hasPoliAttr) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.error.polo.attrazione"/>', attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				unBlockUI();
			});
			return false; 
		}
		
		if (!hasSottoscrittoCondizioni) {
			$.alerts.dialogClass = "style_1";
			jAlert('<s:text name="igeriv.error.condizioni.uso"/>', attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				unBlockUI();
			});
			return false; 
		}
		
		$("#mqSuperficie").val($("#mqSuperficieFormat").val());
		$("#mtlineariPerGiornali").val($("#mtlineariPerGiornaliFormat").val());
		
		return true;
	}

</script>
