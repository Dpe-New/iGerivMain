<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript">
	function validateFieldsLogin() {
        form = document.getElementById("security_form");
        var errors = false;
        var continueValidation = true;
        if (document.getElementById("j_username")) {
            field = document.getElementById("j_username");
            var error = '<s:text name="dpe.validation.msg.required.field"/>';
            if (continueValidation && field.value != null && (field.value == "" || field.value.replace(/^\s+|\s+$/g,"").length == 0)) {
                addError(field, error);
                unBlockUI();
                errors = true;
            }
        }
        if (document.getElementById("j_password")) {        	
            field = document.getElementById("j_password");
            var error = '<s:text name="dpe.validation.msg.required.field"/>';
            if (continueValidation && field.value != null && (field.value == "" || field.value.replace(/^\s+|\s+$/g,"").length == 0)) {
                addError(field, error);
                unBlockUI();
                errors = true;
            }
        }
        return !errors;
    }
    
    function onLoadFunction() {
    	if (document.getElementById('j_username')) {
    		document.getElementById('j_username').focus();
    	}
    }
</script>