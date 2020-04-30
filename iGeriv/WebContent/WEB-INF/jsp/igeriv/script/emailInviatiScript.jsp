<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataMessaggioDa').datepicker();
		$('#strDataMessaggioA').datepicker();
		
// 		$('#strDataMessaggioDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#strDataMessaggioA').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		$("#MessaggiTab_table tbody tr td:not(:nth-child(5))").click(function() {
			var popWidth = 800;
		 	var popHeight = 480;
		    var popURL = $(this).parent().attr('divparam'); 
		    var query= popURL.split('?');
		    var dim= query[1].split('&');	   	   	    	  		  
		    var pk = dim[0].split('=')[1];
		    var url = "${pageContext.request.contextPath}/emailInviati_showMessageInviatoDaRivendita.action?idEmailRivendita=" + pk;
			openDiv('popup_name', popWidth, popHeight, url);
		});
		
		$('#MessaggiTab_table tbody tr').each(function() {				
			var child1 = $(this).find('td:nth-child(5)');	
			var str = '<span style="float:left; width:20px;">';								
			if (typeof($(this).attr('an')) !== 'undefined' && $(this).attr('an') != '') {				
				str += '<a href="${pageContext.request.contextPath}/attachment.action?codRivendita=<s:text name="%{#request.codRivendita}"/>&fileName=' + encodeURIComponent($(this).attr('an')) + '" target="new"><img src="/app_img/attachment.png" border="0px" style="border-style: none" title="<s:text name="igeriv.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a>';	
			}   
			str += '</span>';			
			child1.html(str);	
		});	
	});
</script>