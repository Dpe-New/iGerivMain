<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript">			
	$(document).ready(function() {		
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataDa').datepicker();
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#strDataA').datepicker();
		
// 		$('#strDataDa').click(function() { 
// 	    	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
// 		$('#strDataA').click(function() { 
// 		    show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
		$("#MancanzeTab_table tbody tr").click(function () {
			if ($(this).find("td").last().text().trim() != '') {
			    var popURL = $(this).attr('divParam'); 
			    var query= popURL.split('?'); 
			    var dim= query[1].split('&');	   	    	    	  		  
			    var idtn = dim[0].split('=')[1];
			    var dtBolla = dim[1].split('=')[1];
			 	var url = "mancanzeBolla_showMancanzeDettaglioBolla.action?idtn=" + idtn + '&dtBolla=' + dtBolla;
				openDiv("popup_name", 360, 300, url);
			}
		});
	});
</script>		