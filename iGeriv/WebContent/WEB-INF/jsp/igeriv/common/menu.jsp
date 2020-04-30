<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div id="mydroplinemenu" class="droplinebar">
	<ul id="listMenu">
		<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
		<s:if test='#context["struts.actionMapping"].namespace == "/"'>
			<s:set name="ap" value="" />
		</s:if>
		<li id="menuHomeLink"><a
			href="${pageContext.request.contextPath}${ap}/home.action" id="home"
			childs="">Home</a></li>
		<s:iterator value="listModuli" var="list1" status="status">
			<s:set name="sizeP" value="#list1.size" />
			<li><s:iterator value="#list1" var="list2" status="s1">
					<s:set name="size" value="#list2.size" />
					<s:iterator value="#list2" var="list3" status="s2">
						<s:set name="condition1"
							value="((#sizeP > 0) && ((#s1.index + 1) eq #sizeP))" />
						<s:set name="condition2"
							value="((#size > 1) && ((#s2.index + 1) eq #size))" />
						<s:if
							test="((#sizeP > 0) && (#s1.index > 0)) || ((#size > 1) && (#s2.index > 0))">
							<li>
						</s:if>
						<s:if
							test="#s1.index > 0 && #list3.url != null && #list3.url != ''">
							<s:if test="#list3.attivo eq true">
								<a
									href="${pageContext.request.contextPath}${ap}/<s:text name='#list3.url'/>"
									id="<s:text name='#list3.url'/>"
									childs="<s:text name='#list3.actionName'/>">
							</s:if>
							<s:else>
								<a href="#" style="color: #999; cursor: default">
							</s:else>
						</s:if>
						<s:if
							test="((#sizeP > 0) && (#s1.index eq 0)) || ((#size > 1) && (#s2.index eq 0))">
							<span class="dir"><a href="#">
						</s:if>
						<s:if test="#list3.attivo eq true">
							<s:text name="#list3.titolo18N" />
						</s:if>
						<s:else>
							<span style="color: #999; cursor: pointer"
								<s:if test="#session['hasProfiloStarter'] eq true && ((#sizeP > 0) && (#s1.index > 0)) || ((#size > 1) && (#s2.index > 0))">onclick="showMsgDisabledMenuStarterAlert();"</s:if>><s:text
									name="#list3.titolo18N" /></span>
						</s:else>
						<s:if
							test="((#sizeP > 0) && (#s1.index eq 0)) || ((#size > 1) && (#s2.index eq 0))">
							</a>
							</span>
						</s:if>
						<s:if
							test="(#s1.index > 0 || #s2.index > 0) && #list3.url != null && #list3.url != ''">
							</a>
						</s:if>
						<s:if
							test="((#sizeP > 0) && (#s1.index eq 0)) || ((#size > 1) && (#s2.index eq 0))">
							<ul livello="<s:text name='#list3.livello'/>">
						</s:if>
						<s:if
							test="((#sizeP > 0) && (#s1.index > 0) && ((#s2.index + 1) eq #size)) || ((#size > 1) && (#s2.index > 0))"></li>
			</s:if>
			<s:if test="condition1 && !(condition2)">
	</ul>
	</s:if>
	<s:elseif
		test="((#s1.index + 1) eq (#sizeP)) && ((#s2.index + 1) eq (#size))">
		</ul>
		</li>
		<s:if test="(#size > 1)">
			</ul>
		</s:if>
	</s:elseif>
	<s:elseif test="condition2">
		</ul>
		</li>
	</s:elseif>
	</s:iterator>
	</s:iterator>
	</li>
	</s:iterator>
	</ul>
</div>

