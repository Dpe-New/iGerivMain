<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<META charset="ISO-8859-1">
	<title><tiles:insertAttribute name="title" /></title>
	<tiles:insertAttribute name="header" />
	<style type="text/css" media="screen"> 
		body {
				position:relative;
				top:0px;		
				background:#e0e0e0;
				/*background:#c69f5b;*/	
				font-family: Verdana, Arial, Helvetica, SunSans-Regular, Sans-Serif;
			}
		
		div#page { position:relative;
				   top:0%;
				   left:0%;				   
				   width:100%; 
				   height:280px;
				   margin-left: auto;    
				   margin-right: auto;		
				 } 
		div#logoContent { position:relative;
			   text-align:center;
			   width:450px;	
			   margin-top:150px;
			   height:40px;
			   margin-left: auto;    
			   margin-right: auto;}
		 div#content { position:relative;
			   top:10%; 
			   margin-left: auto;    
			   margin-right: auto;	
			   align:center;			 
			   width:450px;
			   height:200px;
			   text-align:center } 
	</style>
</head> 
<body onload="javascript: onLoadFunction();">
	<div id="fb-root"></div>
    <div id="page"> 
    	<div id="logoContent"><tiles:insertAttribute name="logoContent" /></div>
        <div id="content"><tiles:insertAttribute name="content" /></div> 
    </div>               
</body> 
<tiles:insertAttribute name="script" />
<tiles:insertAttribute name="customScript" /> 
<head>
	<!--[if IE ]>
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Expires" CONTENT="-1">
	<![endif]--> 
</head>
</html>