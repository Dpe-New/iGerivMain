<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	$(document).ready(function() {			
		$('#RichiestaRifornimentoTab_table tbody tr td:nth-child(9), #RichiestaRifornimentoTab_table tbody tr td:nth-child(10), #RichiestaRifornimentoTab_table tbody tr td:nth-child(11)').each(function() {
			if ($(this).text().length > 0) {
				$(this).css({"color":"red","font-weight":"bold"});
			}
		}); 
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('#dataDa').datepicker();
		$('#dataA').datepicker();
// 		$('#dataDa').click(function() { 
//         	show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});	
		
// 		$('#dataA').click(function() { 
// 	        show_cal(document.getElementById($(this).attr('id')));              			            		    		
// 		});
		setContentDivHeight(100);
		$("input:hidden[name='ec_eti']").val('');
		<s:if test="hasActionErrors()">
			var msg = '';
			<s:iterator value="actionErrors">
				msg += '<s:property escape="false" />';
			</s:iterator>
			$.alerts.dialogClass = "style_1";
			jAlert(msg, attenzioneMsg.toUpperCase(), function() {
				$.alerts.dialogClass = null;
			});
		</s:if>
		//$("#dataDa").focus();
	});
</script>