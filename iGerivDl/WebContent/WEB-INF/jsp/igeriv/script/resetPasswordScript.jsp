<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript">
	function validateFieldsReset() {
        form = document.getElementById("security_form");
        var errors = false;
        var continueValidation = true;
        if (document.getElementById("codEdicola")) {
            field = document.getElementById("codEdicola");
            var error = '<s:text name="dpe.validation.msg.required.field"/>';
            if (continueValidation && field.value != null && (field.value == "" || field.value.replace(/^\s+|\s+$/g,"").length == 0)) {
                addError(field, error);
                errors = true;
                
            }
        }
        return !errors;
    }       
    
    function onLoadFunction() {
    	if (document.getElementById('codEdicola')) {
    		document.getElementById('codEdicola').focus();
    	}
    } 
</script>