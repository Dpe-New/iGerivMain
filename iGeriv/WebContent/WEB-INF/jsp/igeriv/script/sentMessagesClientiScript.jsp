<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		
		$("#filterForm input[type='text']").keypress(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode); 
			if (keycode == 13) {
				event.preventDefault();
				$("#ricerca").trigger("click");		
			}    
		});
		
		$("#MessaggiTab_table tbody td:not(:nth-child(4)):not(:nth-child(5)):not(:nth-child(6))").click(function() {
				var popID = 'popup_name';   	     		    	  
			    var popWidth = 820;
			    var popHeight = 480;	 	
			 	var popURL = $(this).parent().attr('divparam'); 
			    var query= popURL.split('?');
			    var dim= query[1].split('&');	   	   	    	  		  
			    var codice = dim[0].split('=')[1];	  
			 	var url = "sentMessagesClienti_showMessage.action?codMessaggio=" + codice;
				openDiv(popID, popWidth, popHeight, url, "#fff");
			}
		);
		
		$('#MessaggiTab_table tbody tr').each(function() {	
			var $row = $(this);
			var child1 = $row.find('td:nth-child(4)');
			var child2 = $row.find('td:nth-child(5)');
			var child3 = $row.find('td:nth-child(6)');
			var $an = $row.attr('an');
			var $an1 = $row.attr('an1');
			var $an2 = $row.attr('an2');
			var str1 = '<span style="float:left; width:20px;">';	
			if (typeof($an) != 'undefined' && $an != '') { 		
				str1 += '<a href="attachment.action?fileName=' + encodeURIComponent($an) + '" target="new"><img src="/app_img/attachment.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a>';
			} 
			str1 += '</span>';
			var str2 = '<span style="float:left; width:20px;">';
			if (typeof($an1) != 'undefined' && $an1 != '') { 		
				str2 += '<a href="attachment.action?fileName=' + encodeURIComponent($an1) + '" target="new"><img src="/app_img/attachment.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a>';
			} 
			str2 += '</span>';
			var str3 = '<span style="float:left; width:20px;">';
			if (typeof($an2) != 'undefined' && $an2 != '') { 		
				str3 += '<a href="attachment.action?fileName=' + encodeURIComponent($an2) + '" target="new"><img src="/app_img/attachment.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a>';
			} 
			str3 += '</span>';		
			child1.html(str1);
			child2.html(str2);
			child3.html(str3);
		});	
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataMessaggioDa').datepicker();
		$('#strDataMessaggioA').datepicker();
		
// 		$('#strDataMessaggioDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#strDataMessaggioA').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		addFadeLayerEvents();
	});
	
	function doSubmit() { 
		var dtDa = $("#strDataMessaggioDa").val();
		var dtA = $("#strDataMessaggioA").val();
		if (dtDa.trim() != '' && !checkDate(dtDa)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				//$('#strDataMessaggioDa').focus();
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
				//$('#strDataMessaggioA').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		$("#filterForm").submit();
	};
</script>

