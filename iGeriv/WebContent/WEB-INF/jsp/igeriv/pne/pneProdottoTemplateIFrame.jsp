<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 0px;
}

div#content1 {
	height: 520px;
}

.treeview,.treeview ul {
	padding: 3;
	margin: 0;
	list-style: none;
}

.treeview ul {
	margin-top: 4px;
}

.treeview .hitarea {
	background: url(/app_img/treeview-default.gif) -64px -25px no-repeat;
	height: 16px;
	width: 16px;
	margin-left: -16px;
	float: left;
	cursor: hand;
}

/* fix for IE6 */
* html .hitarea {
	display: inline;
	float: none;
}

.treeview li {
	margin: 0;
	padding: 3px 0pt 3px 16px;
}

.treeview a.selected {
	background-color: #eee;
}

#treecontrol {
	margin: 1em 0;
	display: none;
}

.treeview .hover {
	color: red;
	cursor: pointer;
}

.treeview li {
	background: url(/app_img/treeview-default-line.gif) 0 0 no-repeat;
}

.treeview li.collapsable,.treeview li.expandable {
	background-position: 0 -176px;
}

.treeview .expandable-hitarea {
	background-position: -80px -3px;
}

.treeview li.last {
	background-position: 0 -1766px
}

.treeview li.lastCollapsable,.treeview li.lastExpandable {
	background-image: url(/app_img/treeview-default.gif);
}

.treeview li.lastCollapsable {
	background-position: 0 -111px
}

.treeview li.lastExpandable {
	background-position: -32px -67px
}

.treeview div.lastCollapsable-hitarea,.treeview div.lastExpandable-hitarea
	{
	background-position: 0;
}

.treeview-red li {
	background-image: url(/app_img/treeview-red-line.gif);
}

.treeview-red .hitarea,.treeview-red li.lastCollapsable,.treeview-red li.lastExpandable
	{
	background-image: url(/app_img/treeview-red.gif);
}

.treeview-black li {
	background-image: url(/app_img/treeview-black-line.gif);
}

.treeview-black .hitarea,.treeview-black li.lastCollapsable,.treeview-black li.lastExpandable
	{
	background-image: url(/app_img/treeview-black.gif);
}

.treeview-gray li {
	background-image: url(/app_img/treeview-gray-line.gif);
}

.treeview-gray .hitarea,.treeview-gray li.lastCollapsable,.treeview-gray li.lastExpandable
	{
	background-image: url(/app_img/treeview-gray.gif);
}

.treeview-famfamfam li {
	background-image: url(/app_img/treeview-famfamfam-line.gif);
}

.treeview-famfamfam .hitarea,.treeview-famfamfam li.lastCollapsable,.treeview-famfamfam li.lastExpandable
	{
	background-image: url(/app_img/treeview-famfamfam.gif);
}

.treeview .placeholder {
	background: url(/app_img/ajax-loader.gif) 0 0 no-repeat;
	height: 16px;
	width: 16px;
	display: block;
}

.filetree li {
	padding: 3px 0 2px 16px;
}

.filetree span.folder,.filetree span.file {
	padding: 1px 0 1px 16px;
	display: block;
}

.filetree span.folder {
	background: url(/app_img/folder.gif) 0 0 no-repeat;
}

.filetree li.expandable span.folder {
	background: url(/app_img/folder-closed.gif) 0 0 no-repeat;
}

.filetree span.file {
	background: url(/app_img/product_small.gif) 0 0 no-repeat;
}

.filetree span.fileEdicola {
	padding: 1px 0 1px 16px;
	display: block;
}

.filetree span.fileEdicola {
	background: url(/app_img/product_small.gif) 0 0 no-repeat;
}

.productBoxes {
	float: left;
	border: 1px solid black;
	margin-left: 5px;
	width: 200px;
	height: 30px;
	background: #ccccff;
}

.productBoxes:hover {
	background: silver;
	cursor: pointer;
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

.linethrough {
	text-decoration: line-through;
}
</style>

<s:form id="PNEForm" name="PNEForm"
	action="pne_saveProdottiTemplate.action">
	<div style="width: 100%">
		<div style="width: 100%;" id="containerDiv">
			<div id="prodottiEdicola"
				style="float: left; z-index: 1; width: 45%; margin-left: 5%; height: 400px; overflow-y: scroll;"
				class="gallery ui-helper-reset ui-helper-clearfix ui-droppable bollaScrollDiv">
				<ul id="mieiProdottiTree" class="filetree" style="z-index: 10">
					<s:iterator value="listProdottiEdicola" var="list1" status="status">
						<s:set name="map1" value="#list1" />
						<s:iterator value="#map1" var="list2" status="status1">
							<li class="level1"><span class="folder level1"
								codcategoria="<s:property value="key.substring(key.indexOf('|') + 1)"/>">&nbsp;<s:property
										value="key.substring(0, key.indexOf('|'))" escape="false" /></span> <s:set
									name="valueList" value="value" /> <s:iterator
									value="#valueList" var="list3" status="status2">
									<s:set name="map3" value="#list3" />
									<ul>
										<s:iterator value="#map3" var="list4" status="status3">
											<s:set name="innerMap" value="#list4" />
											<s:iterator value="#innerMap" var="list5" status="status4">
												<s:set name="codCat"
													value="key.substring(key.indexOf('|') + 1, key.lastIndexOf('|'))" />
												<s:set name="codScat"
													value="key.substring(key.lastIndexOf('|') + 1)" />
												<li class="level2"><span class="folder subFolder"
													id="sucategory_<s:property value="codCat"/>_<s:property value="codScat"/>"
													codcategoria="<s:property value="codCat"/>"
													codsubcategoria="<s:property value="codScat"/>">&nbsp;<s:property
															value="key.substring(0,key.indexOf('|'))" escape="false" /></span>
													<s:set name="listDto" value="value" /> <s:iterator
														value="#listDto" var="dto" status="status5">
														<ul>
															<s:iterator value="#list2" var="list3" status="status6">
																<s:if test="#dto.codProdottoInterno < 100000">
																	<s:set name="prodTemplate" value="true" />
																</s:if>
																<s:else>
																	<s:set name="prodTemplate" value="false" />
																</s:else>
																<li style="cursor: pointer; cursor: hand;"
																	class="prodotto <s:if test='#dto.escludiDalleVendite eq true'>linethrough</s:if>"><span
																	id="folder_2_<s:text name='#dto.codProdottoInterno'/>"
																	class="fileEdicola"
																	isProdottoTemplate="<s:text name='#prodTemplate'/>"
																	isProdottoDL="<s:text name='#dto.prodottoDl'/>"
																	isProdottoDigitale="<s:text name='#dto.prodottoDigitale'/>"
																	codcategoria="<s:text name='#dto.codCategoria'/>"
																	codsubcategoria="<s:text name='#dto.codSubCategoria'/>">&nbsp;<s:text
																			name='#dto.descrizione' /><span
																		style="font-size: 80%;">&nbsp;(<s:text
																				name="label.print.Table.Price" />:&nbsp;<span
																			style="font-weight: bold;"><s:text
																					name='#dto.prezzoFormat' /></span>&nbsp;<s:text
																				name="igeriv.giacienza" />:&nbsp;<span
																			style="font-weight: bold;"><s:text
																					name='#dto.giacenzaProdotto' /></span>&nbsp;<s:text
																				name="igeriv.carico" />:&nbsp;<span
																			style="font-weight: bold;"><s:text
																					name='#dto.giacenzaInizialeProdotto' /></span>)
																	</span></span></li>
															</s:iterator>
														</ul>
													</s:iterator></li>
											</s:iterator>
										</s:iterator>
									</ul>
								</s:iterator></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
			<div style="float: right; width: 40%; margin-right: 0%">
				<div id="gallery"
					class="gallery ui-helper-reset ui-helper-clearfix ui-droppable bollaScrollDiv">
					<ul id="prodottiTree" class="filetree">
						<s:iterator value="listProdotti" var="list1" status="status">
							<s:set name="map1" value="#list1" />
							<s:iterator value="#map1" var="list2" status="status1">
								<li><span id="folder_0_<s:text name="#status1.index"/>"
									class="folder">&nbsp;<s:property value="key" /></span> <s:set
										name="valueList" value="value" /> <s:iterator
										value="#valueList" var="list3" status="status2">
										<s:set name="map3" value="#list3" />
										<ul>
											<s:iterator value="#map3" var="list4" status="status3">
												<s:set name="innerMap" value="#list4" />
												<s:iterator value="#innerMap" var="list5" status="status4">
													<li><span
														id="folder_1_<s:text name="#status2.index"/>"
														class="folder">&nbsp;<s:property value="key" /></span> <s:set
															name="listDto" value="value" /> <s:iterator
															value="#listDto" var="dto" status="status5">
															<ul>
																<s:iterator value="#list2" var="list3" status="status6">
																	<li style="cursor: pointer; cursor: hand;"><span
																		id="folderTemplate_2_<s:text name='#dto.codProdottoInterno'/>"
																		class="file" isProdottoTemplate="false"
																		codcategoria="<s:text name='#dto.codCategoria'/>"
																		codsubcategoria="<s:text name='#dto.codSubCategoria'/>">&nbsp;<s:text
																				name='#dto.descrizione' /></span></li>
																</s:iterator>
															</ul>
														</s:iterator></li>
												</s:iterator>
											</s:iterator>
										</ul>
									</s:iterator></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<s:hidden name="codProdotto" id="codProdotto" />
</s:form>
<ul id="myMenu" class="contextMenu">
	<li class="edit"><a href="#edit"><s:text
				name="igeriv.modifica" /></a></li>
	<li class="delete"><a href="#delete"><s:text
				name="dpe.contact.form.elimina" /></a></li>
</ul>
<ul id="menuProdottiDl" class="contextMenu">
	<li class="edit"><a href="#edit"><s:text
				name="igeriv.modifica" /></a></li>
</ul>
<ul id="categoryInsert" class="contextMenu" style="width: 200px;">
	<li class="insert"><a href="#insertCat"><s:text
				name="plg.inserisci" />&nbsp;<s:text name="igeriv.categoria" /></a></li>
</ul>
<ul id="categoryEditDelete" class="contextMenu" style="width: 200px;">
	<li class="insert"><a href="#insertCat"><s:text
				name="plg.inserisci" />&nbsp;<s:text name="igeriv.categoria" /></a></li>
	<li class="insert"><a href="#insertScat"><s:text
				name="plg.inserisci" />&nbsp;<s:text name="igeriv.sotto.categoria" /></a></li>
	<li class="edit"><a href="#editCat"><s:text
				name="igeriv.modifica" />&nbsp;<s:text name="igeriv.categoria" /></a></li>
	<li class="delete"><a href="#deleteCat"><s:text
				name="dpe.contact.form.elimina" />&nbsp;<s:text
				name="igeriv.categoria" /></a></li>
</ul>
<ul id="subcategoryEditDelete" class="contextMenu" style="width: 200px;">
	<li class="insert"><a href="#insertScat"><s:text
				name="plg.inserisci" />&nbsp;<s:text name="igeriv.sotto.categoria" /></a></li>
	<li class="insert"><a href="#insertProd"><s:text
				name="plg.inserisci" />&nbsp;<s:text name="igeriv.prodotto" /></a></li>
	<li class="edit"><a href="#editScat"><s:text
				name="igeriv.modifica" />&nbsp;<s:text name="igeriv.sotto.categoria" /></a></li>
	<li class="delete"><a href="#deleteScat"><s:text
				name="dpe.contact.form.elimina" />&nbsp;<s:text
				name="igeriv.sotto.categoria" /></a></li>
</ul>

