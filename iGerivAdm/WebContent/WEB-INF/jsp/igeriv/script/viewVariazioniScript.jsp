<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	$(document).ready(function() {			
		$('#RichiestaRifornimentoTab_table tbody tr td:nth-child(9), #RichiestaRifornimentoTab_table tbody tr td:nth-child(10), #RichiestaRifornimentoTab_table tbody tr td:nth-child(11)').each(function() {
			if ($(this).text().length > 0) {
				$(this).css({"color":"red","font-weight":"bold"});
			}
		}); 
		$('#dataDa').click(function() { 
        	show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});	
		
		$('#dataA').click(function() { 
	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
		});
		setContentDivHeight(100);
		$("input:hidden[name='ec_eti']").val('');
		$("#dataDa").focus();
	});
</script>