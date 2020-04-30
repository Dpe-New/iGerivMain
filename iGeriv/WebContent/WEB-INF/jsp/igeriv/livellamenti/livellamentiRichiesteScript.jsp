<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>

<script>
	$(document).ready(function() {
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataDa').datepicker();
		$('#strDataA').datepicker();
		
// 		$('#strDataDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#strDataA').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
	});
	
	$("table[id*='_table'] tbody").find("td:not(:last-child)").click(function() {
		var popID = 'popup_name'; //Get Popup Name	   
	    var popURL = $(this).parent().attr('divParam'); 
	 	var popWidth = 900;
	 	var popHeight = 500;
// 	    var query = popURL.split('?'); 
// 	    var dim = query[1].split('&');	   	    	    	  		  
// 	    var idtn = dim[0].split('=')[1];
// 	    var periodicita = '';
// 	    var coddl = '';
// 	    if (dim.length > 1) {
// 	    	periodicita = dim[1].split('=')[1];
// 	    	coddl = dim[2].split('=')[1];
// 	    }
	    var url = '${pageContext.request.contextPath}/avvisoLivellamenti_showAccettazioneLivellamenti.action'+popURL;

// 	 	if (periodicita != '') {
// 	 		url += "&periodicita=" + periodicita;
// 	 	}
		openDiv(popID, popWidth, popHeight, url);
	}
);
	
	
	
	
	
	
</script>