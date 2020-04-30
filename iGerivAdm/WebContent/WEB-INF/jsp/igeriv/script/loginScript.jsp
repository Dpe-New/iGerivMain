<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
	var browserNotSupported = "<s:text name="msg.browser.not.supported"/>";
	var browserVersionNotSupported = "<s:text name="msg.browser.version.not.supported"/>";
	var cookiesDisabled = "<s:text name="msg.cookies.disabled"/>";
	var lowResolutionScreen = "<s:text name="msg.low.screen.resolution"/>";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/combined-min-stable_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/combined-min_<s:text name="igeriv.version.timestamp"/>.js"></script>	
<script type="text/javascript">			
	var ray = {
			ajax : function(st) {							
				if ($("#load").length > 0) {														
					$("#load").remove();						
				} 
				$("body").prepend('<div id="load" style="position:absolute;width:100%;height:150%;margin-left:auto;margin-right:auto;background-repeat: no-repeat;background-position: center;z-index:999999;left:0;background-image: url(/app_img/loading.gif);"></div>');
			},
			show : function(el) {
				this.getID(el).style.display = '';
			},
			getID : function(el) {
				return document.getElementById(el);
			}
	};
	
	$(document).ready(function() {	
		makeBrowserChecks();
		$("#forum, #blog, #fb").tooltip({
			delay: 0,  
		    showURL: false
		});
		
		$(".openid").tooltip({
			delay: 0,  
		    showURL: false
		});
		
		$("#openIdImg").tooltip({
			delay: 0,  
		    showURL: false,
		    bodyHandler: function() { 
		    	var msg = "<s:text name='igeriv.openid.tooltip'/>".replace("{0}","<s:text name='appName'/>").replace("{1}","<s:text name='appName'/>").replace("{2}","<s:text name='appName'/>");
		    	return msg;
		    } 
		}); 
		
		signOpenId = function(url, providerType) {
			if (url != '') {
				$("#openId").val(url);
				$("#providerType").val(providerType)
				$("#openIdForm").submit();
			}
		}
		
		$("#pwdDim").click(function() {
			window.location = '<s:url namespace="/" action="loginResetPwd_input.action"/>';
		});
		
		doSubmit = function() {
			if ($("#j_username").val().length > 0 && $("#j_password").val().length > 0) {	
				$("#security_form").submit();
				return false;
			} 
		}
		
		$("#j_username, #j_password").keydown(function(event) {
			var keycode = (event.keyCode ? event.keyCode : event.charCode);
			if (keycode == '13') {	
				doSubmit();
				return false;
			} 
		});
	})
</script>		