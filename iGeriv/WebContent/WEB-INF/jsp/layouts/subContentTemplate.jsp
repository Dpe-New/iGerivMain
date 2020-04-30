<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<tiles:insertAttribute name="header" />
</head>
<body onload="javascript: onLoadFunction();">
	<div>
		<tiles:insertAttribute name="filter" />
	</div>
	<div>
		<tiles:insertAttribute name="content1" />
	</div>
	<div>
		<tiles:insertAttribute name="content2" />
	</div>
	<tiles:insertAttribute name="script" />
	<tiles:insertAttribute name="customScript" />
	<script>
		var ray2 = {
				ajax : function(st) {							
					if ($("#load2").length > 0) {														
						$("#load2").remove();						
					} 
					$("body").prepend('<div id="load2" style="position:absolute;width:100%;height:150%;margin-left:auto;margin-right:auto;background-repeat: no-repeat;background-position: center;z-index:999999;left:0;background-image: url(/app_img/loading.gif);"></div>');
				},
				show : function(el) {
					this.getID(el).style.display = '';
				},
				getID : function(el) {
					return document.getElementById(el);
				}
		}
	</script>
</body>
</html>