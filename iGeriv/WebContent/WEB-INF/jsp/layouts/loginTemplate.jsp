<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META charset="ISO-8859-1">
	<title><tiles:insertAttribute name="title" /></title> <tiles:insertAttribute
		name="header" />
	<div id="loginStyle">
		<tiles:insertAttribute name="loginStyle" />
	</div>
</head>
<body onload="javascript: onLoadFunction();">
	<div id="fb-root"></div>
	<div id="page">
		<div id="logoContent">
			<tiles:insertAttribute name="logoContent" />
		</div>
		<div id="content">
			<tiles:insertAttribute name="content" />
		</div>
	</div>
	<tiles:insertAttribute name="script" />
	<tiles:insertAttribute name="customScript" />
</body>
</html>