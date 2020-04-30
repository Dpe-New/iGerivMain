<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script>
	$(document).ready(function() {	
		$("#MessaggiTab_table tbody td:not(:last-child)").click(function() {
				var popID = 'popup_name';   	     		    	  
			    var popWidth = 620;
			    var popHeight = 480;	 	
			 	var popURL = $(this).parent().attr('divparam'); 
			    var query= popURL.split('?');
			    var dim= query[1].split('&');	   	   	    	  		  
			    var pk = dim[0].split('=')[1];	  
			 	var url = "${pageContext.request.contextPath}${ap}/avviso_showMessageEdicole.action?messagePk=" + pk;
				openDiv(popID, popWidth, popHeight, url, "#fff");
			}
		);
		
		
		$('#MessaggiTab_table tbody tr').each(function() {				
			var child1 = $(this).find('td:nth-child(5)');	
			var str = '<span style="width:20px;">';								
			if (typeof($(this).attr('an')) !== 'undefined' && $(this).attr('an') != '') {				
				str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($(this).attr('an')) + '" target="new"><img src="/app_img/attachment_small.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>: '+$(this).attr('an')+'" alt="'+$(this).attr('an')+'" /></a>';	
			}   
			str += '</span>';			
			child1.html(str);	
			
			var child2 = $(this).find('td:nth-child(6)');	
			var str = '<span style="width:20px;">';								
			if (typeof($(this).attr('an1')) !== 'undefined' && $(this).attr('an1') != '') {				
				str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($(this).attr('an1')) + '" target="new"><img src="/app_img/attachment_small.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>: '+$(this).attr('an1')+'" alt="'+$(this).attr('an1')+'" /></a>';	
			}   
			str += '</span>';			
			child2.html(str);
			
			var child3 = $(this).find('td:nth-child(7)');	
			var str = '<span style="width:20px;">';								
			if (typeof($(this).attr('an2')) !== 'undefined' && $(this).attr('an2') != '') {				
				str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($(this).attr('an2')) + '" target="new"><img src="/app_img/attachment_small.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>: '+$(this).attr('an2')+'" alt="'+$(this).attr('an2')+'" /></a>';	
			}   
			str += '</span>';			
			child3.html(str);				
		});	
		
		
// 		$('#MessaggiTab_table tbody td:last-child').each(function() {	
// 			var $td = $(this);
// 			var $row = $td.parent();
// 			var child = $row.find('td:nth-child(5)');	
// 			var str = '<span style="float:left; width:20px;">';		
// 			var $an = $row.attr('an');
// 			if (typeof($an) != 'undefined' && $an != '') { 		
// 				str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + encodeURIComponent($an) + '" target="new"><img src="/app_img/attachment.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a>';
// 			} 
// 			str += '</span>';			
// 			child.html(str);					
// 		});	
		
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

