<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
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
		
		$('#MessaggiTab_table tbody td:last-child').each(function() {	
			var $td = $(this);
			var $row = $td.parent();
			var child = $row.find('td:nth-child(5)');	
			var str = '<span style="float:left; width:20px;">';		
			var $an = $row.attr('an');
			if (typeof($an) != 'undefined' && $an != '') { 		
				str += '<a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=' + $an + '" target="new"><img src="/app_img/attachment.png" border="0px" width="30px" height="30px" style="border-style: none" title="<s:text name="igeriv.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a>';
			} 
			str += '</span>';			
			child.html(str);					
		});	
		
		$('#strDataMessaggioDa').click(function() { 
        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#strDataMessaggioA').click(function() { 
	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		addFadeLayerEvents();
	});
	
	function doSubmit() { 
		var dtDa = $("#strDataMessaggioDa").val();
		var dtA = $("#strDataMessaggioA").val();
		if (dtDa.trim() != '' && !checkDate(dtDa)) {
			$.alerts.dialogClass = "style_1";
			jAlert(dataFormatoInvalido1, attenzioneMsg, function() {
				$.alerts.dialogClass = null;
				$('#strDataMessaggioDa').focus();
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
				$('#strDataMessaggioA').focus();
			});
			setTimeout(function() {
				unBlockUI();
			}, 100);
			return false; 
		} 
		$("#filterForm").submit();
	};
</script>

