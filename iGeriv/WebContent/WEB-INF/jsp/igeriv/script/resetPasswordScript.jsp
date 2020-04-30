<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript">

	function validateFieldsReset() {
		cleanErrFields();
	    var errCodUt = '<s:if test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}"><s:text name="dpe.validation.msg.required.field"/></s:if><s:else><s:text name="plg.inserisci.cod.utente"/></s:else>';
	    var errMailRiv = '<s:if test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}"><s:text name="dpe.validation.msg.required.field"/></s:if><s:else><s:text name="plg.inserisci.email.rivendita"/></s:else>';
	    var errCodSic = '<s:if test="%{#request.sf neq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}"><s:text name="dpe.validation.msg.required.field"/></s:if><s:else><s:text name="plg.inserisci.cod.sicurezza"/></s:else>';
	    if ($("#codEdicola").val().trim().length == 0) {
	        addError(document.getElementById("codEdicola"), errCodUt);
	        return false;
	    } else if ($("#emailEdicola").val().trim().length == 0) {
	        addError(document.getElementById("emailEdicola"), errMailRiv);
	        return false;
	    } else if ($("#jCaptchaResponse").val().trim().length == 0) {
	        addError(document.getElementById("jCaptchaResponse"), errCodSic);
	        return false;
	    } else if (!checkEmail($("#emailEdicola").val().trim())) {
	        addError(document.getElementById("emailEdicola"), '<s:text name="igeriv.email.non.valido"/>');
	        return false;
	    }
	    return true;
    }       
    
    function onLoadFunction() {
    	if ($('#codEdicola').length > 0) {
    		$('#codEdicola').focus();
    	}
    } 
    
    function cleanErrFields() {
    	$("#err_codEdicola").text("");
	    $("#err_emailEdicola").text("");
	    $("#err_jCaptchaResponse").text("");
	    $("#err_exception_msg").text("");
	    $("#err_action").text("");
    }
</script>