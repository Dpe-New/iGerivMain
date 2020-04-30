<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<script> 
	$(document).ready(function() {	
		$("#butNuovo").click(function() {
			 	var url = "${pageContext.request.contextPath}${ap}/msgDpeRivendite_editMessage.action";
				openDiv("popup_name", 800, 550, url);
			}
		);
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDaStr').datepicker();
		$('#dataAStr').datepicker();
		
		
// 		$('#dataDaStr').click(function() {
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#dataAStr').click(function() {
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		var $rows = $("#MessaggiTab_table tbody tr");
		$rows.click(tableRowClick);
		
		<s:if test='abilitati eq "false"'>
			$("#abilitati").attr("checked", false);
		</s:if>
		
		initRTE("/app_img/", "${pageContext.request.contextPath}/html/", "");	
		
		$('#dataDaStr').focus();
	});
	
	function tableRowClick(obj) {
		var popID = 'popup_name';	  
	    var popURL = $(this).attr('divParam'); 
	 	var popWidth = 800; 
	 	var popHeight = 550;
	 	var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var pk = dim[0].split('=')[1];
	 	var url = "${pageContext.request.contextPath}${ap}/msgDpeRivendite_editMessage.action?pk=" + pk;
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
</script>

