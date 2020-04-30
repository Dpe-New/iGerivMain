<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {	
		$("#MessaggiTab_table tbody td").click(function() {
			 	var popURL = $(this).parent().attr('divparam'); 
			    var query= popURL.split('?');
			    var dim= query[1].split('&');	   	   	    	  		  
			    var codPromemoria = dim[0].split('=')[1];	  
			 	var url = "promemoria_showPromemoria.action?codPromemoria=" + codPromemoria;
				openDiv('popup_name', 650, 480, url);
			}
		);
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDaStr').datepicker();
		$('#dataAStr').datepicker();
		
// 		$('#dataDaStr').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataAStr').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		$('#nuovo').click(function() {
			openDiv('popup_name', 650, 480, "promemoria_newPromemoria.action");
		});
		
		addFadeLayerEvents();
	});
	
	function doSubmit() { 
		var dtDa = $("#dataDaStr").val();
		var dtA = $("#dataAStr").val();
		if (dtDa.trim() != '' && !checkDate(dtDa)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#dataDaStr').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		if (dtA.trim() != '' && !checkDate(dtA)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#dataAStr').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		$("#filterForm").submit();
	};
</script>

