<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<script>
	$(document).ready(function() {
		setContentDivHeight(70);
		
		$.datepicker.setDefaults($.datepicker.regional['it']);
		$('input:text[name^=dateSospensione]').datepicker();
		
		
		$('input:text[name^=dateSospensione]').each(function() {
			$(this).click(function() { 
	        	//show_cal(document.getElementById($(this).attr('id')));      
	        	$(this).attr('isChanged', 'true');					
				$(this).closest('tr').find('input:hidden[name^=pk]').attr('isChanged', 'true');
			});
		});
		
		$('input:text[name^=dateSospensione]').change(function() {
			$(this).attr('isChanged', 'true');					
			$(this).closest('tr').find('input:hidden[name^=pk]').attr('isChanged', 'true');	
		});
		
		var $rows = $("#EdicoleTab_table tbody tr td:not(:nth-child(9))");
		$rows.css("cursor","pointer").click(tableRowClick);
		$rows.tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	return "<b><s:text name='igeriv.cambia.password'/></b>";
		    } 
		}); 
		
		addFadeLayerEvents();
		
		$("#codEdicolaDl").keypress(function (e) {
		     //if the letter is not digit then display error and don't type anything
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        $("#err_codEdicolaDl").html("Solo valori numerici!").show().delay(2000).fadeOut("slow");
		               return false;
		    }
		   });
		
		
		$("#codEdicolaDl").focus();
		
	});
	
	function tableRowClick(obj) {
		var popID = 'popup_name';	  
	    var popURL = $(this).parent().attr('divParam'); 
	 	var popWidth = 550; 
	 	var popHeight = 330;
	 	var query= popURL.split('?');
	    var dim= query[1].split('&');	   	   	    	  		  
	    var codEdicolaDl = dim[0].split('=')[1];
	 	var url = "${pageContext.request.contextPath}${ap}/edicole_showPwdEdicola.action?codEdicolaWeb=" + codEdicolaDl;	 	
		openDiv(popID, popWidth, popHeight, url);
		return false;
	}
	
	function setFieldsToSave() {
		$('input:text[name^=dateSospensione],input:hidden[name^=pk]').each(function() {		
			if ($(this).attr('isChanged') != 'true') {		
				$(this).attr('disabled', true);
			}
		});
		
		return true;
	}
	
	function afterSuccessSave() {
		$('input:text[name^=dateSospensione]').each(function() {
			$(this).attr('disabled', false);
		});
	}
</script>