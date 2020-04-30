<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
	var browserNotSupported = "<s:text name="msg.browser.not.supported"/>";
	var browserVersionNotSupported = "<s:text name="msg.browser.version.not.supported"/>";
	var cookiesDisabled = "<s:text name="msg.cookies.disabled"/>";
	var lowResolutionScreen = "<s:text name="msg.low.screen.resolution"/>";
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/combined-min-stable_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/combined-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
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
		//formattaCountDown();
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
		
		redirectOpenId = function(url) {
			if (url != '') {
				$("#openIdForm").attr("action", url);
				$("#openIdForm").attr("target", "_new");
				$("#openIdForm").submit();
				$("#openIdForm").attr("action", "${openIDLoginUrl}");
				$("#openIdForm").removeAttr("target");
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
	});
	
	
	function countDown (refDate)
	{
	   // la data attuale
	   var now = new Date();
	   
	   // il risultato
	   var result = new Array (0, // anni [0]
	      0, // mesi [1]
	      0, // giorni [2]
	      0, // ore [3]
	      0, // minuti [4]
	      0 // secondi [5]
	   );

	   // numero di giorni nei vari mesi
	   var daysInMonths = new Array (31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

	   // calcola i secondi
	   var s2 = now.getSeconds ();
	   var s1 = refDate.getSeconds ();
	   if (s1 <= s2) result[0] = s2 - s1;
	   else { result[0] = 60 + s2 - s1; result[1] = -1; }

	   // calcola i minuti
	   var m2 = now.getMinutes ();
	   var m1 = refDate.getMinutes ();
	   if (m1 <= m2) result[1] += m2 - m1;
	   else { result[1] += 60 + m2 - m1; result[2] = -1; }

	   // calcola le ore
	   var h2 = now.getHours ();
	   var h1 = refDate.getHours ();
	   if (h1 <= h2) result[2] += h2 - h1;
	   else { result[2] += 24 + h2 - h1; result[3] = -1; }

	   // calcola i giorni
	   var d2 = now.getDate();
	   var d1 = refDate.getDate();
	   
	   if (d1 <= d2){ 
			result[3] += d2 - d1 + 1;
	   }else{ 
			result[3] += daysInMonths[(12 + now.getMonth()) % 12] + (now.getMonth() == 1 && now.getFullYear () % 4 == 0 ? 1 : 0) + d2 - d1 + 1;
			result[4] = -1;
	   }
		
	   // calcola i mesi
	   var mt2 = now.getMonth()+1;
	   var mt1 = refDate.getMonth();
	  
	   if (mt1 <= mt2){ 
			
			result[4] += mt2 - mt1;
	    }else { 
			
			result[4] += 12 + mt2 - mt1; 
			result[5] = -1 
		}

	   // calcola gli anni
	   var y2 = now.getFullYear ();
	   var y1 = refDate.getFullYear ();
	   result[5] = y2 - y1;
	   return result;
	}

	function formattaCountDown()
	{
	   var result = countDown(new Date (2000, 06, 01));
	   document.getElementById("countdown").innerHTML = result[5] + ' anni ' + result[4] + ' mesi ' + result[3] + ' giorni ' ;

	}
</script>
