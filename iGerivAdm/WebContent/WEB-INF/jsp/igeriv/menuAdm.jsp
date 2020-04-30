<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
		.placeholder {
			background-color: #6699cc;
		}

		.ui-nestedSortable-error {
			background:#fbe3e4;
			color:#8a1f11;
		}

		ol {
			margin: 0;
			padding: 0;
			padding-left: 30px;
		}

		ol.sortable, ol.sortable ol {
			margin: 0 0 0 50px;
			padding: 0;
			list-style-type: none;
		}

		ol.sortable {
			margin: 4em 0;
		}

		.sortable li {
			margin: 7px 0 0 0;
			padding: 0;
		}

		.sortable li div  {
			border: 1px solid black;
			padding: 3px;
			margin: 0;
			cursor: move;
			background: #6699cc;
			width:30%;
			margin-left:auto;
			margin-right:auto;
		}

</style>
<s:if test="menusProfilo != null">
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<ol class="sortable" id="sortableList">	
	<s:set name="listIndex" value="0" />
	<s:iterator value="menusProfilo" var="list1" status="status">
		<s:set name="sizeP" value="#list1.size" />
		<s:iterator value="#list1" var="list2" status="s1">	
			<s:set name="size" value="#list2.size" />
			<s:set name="count" value="0" />
			<s:iterator value="#list2" var="list3" status="s2">	
				<s:set name="condition1" value="((#sizeP > 0) && ((#s1.index + 1) eq #sizeP))" />
				<s:set name="condition2" value="((#size > 1) && ((#s2.index + 1) eq #size))" />
				<s:if test="((#sizeP > 0) && (#s1.index > 0)) || ((#size > 1) && (#s2.index > 0))">
					<li id="list_<s:text name="#list3.id"/>">
				</s:if>	
				<s:else>
					<li id="list_<s:text name="#list3.id"/>">
				</s:else>
					<div id="<s:text name="#list3.id"/>" class="marker" livello="<s:text name="#list3.livello"/>" idMenuPadre="<s:text name="#list3.idModuloPadre"/>"><s:text name="#list3.titolo"/></div>	
				<s:if test="((#sizeP > 0) && (#s1.index eq 0)) || ((#size > 1) && (#s2.index eq 0))">
					<ol>
				</s:if>										
				<s:if test="((#sizeP > 0) && (#s1.index > 0) && ((#s2.index + 1) eq #size)) || ((#size > 1) && (#s2.index > 0))">
					</li>
				</s:if>	
				<s:if test="condition1 && !(condition2)">
					</ol>
				</s:if>
				<s:elseif test="((#s1.index + 1) eq (#sizeP)) && ((#s2.index + 1) eq (#size))">
					</ol></li>
					<s:if test="(#size > 1)">
						</ol>
					</s:if>
				</s:elseif>
				<s:elseif test="condition2">
					</ol></li> 
				</s:elseif>
			</s:iterator>	
		</s:iterator>
		</li>
	</s:iterator>	
</ol>
<s:form id="MenuAdmForm" action="menu_saveConfigurazioneMenu.action" method="POST" theme="simple" validate="false" onsubmit="return (prepareMenuResult() && ray.ajax())">
	<div class="required" style="width:100%; margin-top:10px; text-align:center">
		<div><s:submit name="igeriv.memorizza" id="memorizza" key="igeriv.memorizza" align="center" cssStyle="text-align:center; width:100px"/></div>
	</div>
	<s:hidden id="ids" name="ids"/>
	<s:hidden id="parentIds" name="parentIds"/>
	<s:hidden id="livello" name="livello"/>
	<s:hidden name="idProfilo"/>
</s:form>
<!-- Right Click Menu -->
<ul id="myMenu" class="contextMenu">
	<li class="edit"><a href="#edit"><s:text name="igeriv.modifica"/></a></li>
	<li class="insert"><a href="#insert"><s:text name="plg.inserisci"/></a></li>
    <li class="delete"><a href="#delete"><s:text name="dpe.contact.form.elimina"/></a></li>        	             
</ul>
</s:if>