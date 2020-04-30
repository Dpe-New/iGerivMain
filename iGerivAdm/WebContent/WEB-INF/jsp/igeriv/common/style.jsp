<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>

<style type="text/css">	
	body {
		position:relative;
		top:0px;
		background:#ccccff;
		font-family: Verdana, Arial, Helvetica, SunSans-Regular, Sans-Serif;						
	}			
			   
	div#page { position:relative;				   					
			   width:100%; 				   
			   height:620px;
			   top:0px;
				background:#cccccc;
			   text-align: center; 
			   margin-left: auto;    
			   margin-right: auto;	
			   margin-top: 0px;    	
			 } 	    
    div#header { position:relative;
			   text-align:left;				 
			   top:0px;		
			   width:100%; 
			   height:58px;			   					  
				background:#dfe4e8;
			 }	
	div#logo { 		   	
			   float:left;
			   text-align:left;	
			   width:120;	  				  
			 }	
	div#logout { 		   	
			   float:right;	 
			   text-align:right;
			   margin-top:5px;
			 }	
	div#rtae { 		   	
			   float:right;	 
			   text-align:center;	
			   width:0px;
			   margin-top:8px; 
			 }		
	div#user { 		   	
			   float:right;	 
			   text-align:center;	 
			   vertical-align:center;
			   width:200px;
			   font-size:90%;
			   margin-top:8px;
			 }
	div#icons { 		   	
			   float:left;	 
			   text-align:left;	
			   vertical-align:center;
			   width:220px;
			   font-size:90%;
			   margin-top:15px; 
			   margin-left:14%; 
			   white-space:nowrap;
			 }
	div#dl { 		   	
			   float:left;	 
			   text-align:center;	 
			   vertical-align:center;
			   width:120px;
			   font-size:90%;
			   margin-top:0px;
			 }
	div#menu { position:relative;	
			   float:none;				 			 
			   width:100%;					  				  
			   text-align:left;
			   top:0px;	
			   height:30px;
			   background:#60A3C3;
			 }
	div#breadCrumb { position:relative;	
			   float:none;				 			 
			   width:100%;					  				  
			   text-align:left;
			   top:0px;	
			   height:20px;							   
			 }
	div#filter { position:relative;
			   top:0px;
			   left:0;				  
			   width:100%;
			   height:170px;
			   text-align:left;				   
				background:#cccccc;
			  } 
    div#content1 { position:relative;
			   top:0px;
			   left:0;				 
			   width:100%;
			   height:450px;
			   text-align:left;	
				background:#cccccc;
			  } 
    div#footer { 
    		   position:relative;
    		   top:0px;
			   text-align:left;
			   width:100%;
			   background:#dfe4e8;
			 }
	fieldset {
		background:#dfe4e8;
	}
	
	/* Generic context menu styles */
	.contextMenu {
		position: absolute;
		width: 120px;
		z-index: 999999;
		border: solid 1px #CCC;
		background: #EEE;
		padding: 0px;
		margin: 0px;
		display: none;
	}
	
	.contextMenu LI {
		list-style: none;
		padding: 0px;
		margin: 0px;
	}
	
	.contextMenu A {
		color: #333;
		text-decoration: none;
		display: block;
		line-height: 20px;
		height: 20px;
		background-position: 6px center;
		background-repeat: no-repeat;
		outline: none;
		padding: 1px 5px;
		padding-left: 28px;
	}
	
	.contextMenu LI.hover A {
		color: #FFF;
		background-color: #3399FF;
	}
	
	.contextMenu LI.disabled A {
		color: #AAA;
		cursor: default;
	}
	
	.contextMenu LI.hover.disabled A {
		background-color: transparent;
	}
	
	.contextMenu LI.separator {
		border-top: solid 1px #CCC;
	}
	
	.contextMenu LI.delete A { 
		background-image: url(/app_img/delete.png); 
	}
	
	.contextMenu LI.edit A { 
		background-image: url(/app_img/edit.jpg); 
	}
	
	.contextMenu LI.insert A { 
		background-image: url(/app_img/insert.gif); 
	}
	
	.contextMenu LI.print A { 
		background-image: url(/app_img/print_16x16.png); 
	}
</style>	