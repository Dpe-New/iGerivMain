<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/redmond/jquery-ui-1.8.18.custom.css" />
<style>
div#content1 {
	height: 700px;
}

#gallery {
	float: left;
	width: 100%;
	min-height: 9em;
}

* html #gallery {
	height: 9em;
} /* IE6 */
.gallery.custom-state-active {
	background: #cccccc;
}

.gallery li {
	float: left;
	width: 95px;
	padding: 0.4em;
	margin: 0 0.4em 0.4em 0;
	text-align: center;
}

.gallery li h5 {
	margin: 0 0 0.4em;
	cursor: move;
}

.gallery li a {
	float: right;
}

.gallery li a.ui-icon-zoomin {
	float: left;
}

.gallery li img {
	width: 100%;
	cursor: move;
}

#mostSoldBarL {
	float: left;
	width: 100%;
	height: 400px;
	background: #eee
}

* html #mostSoldBarL {
	height: 400px;
} /* IE6 */
#mostSoldBarL h4 {
	line-height: 16px;
	margin: 0 0 0.4em;
}

#mostSoldBarL h4 .ui-icon {
	float: left;
}

#mostSoldBarL .gallery h5 {
	display: none;
}

#mostSoldBarR {
	float: left;
	width: 100%;
	height: 400px;
	background: #eee
}

* html #mostSoldBarR {
	height: 400px;
} /* IE6 */
#mostSoldBarR h4 {
	line-height: 16px;
	margin: 0 0 0.4em;
}

#mostSoldBarR h4 .ui-icon {
	float: left;
}

#mostSoldBarR .gallery h5 {
	display: none;
}
</style>
<br>
<div class="demo ui-widget ui-helper-clearfix">
	<ul id="gallery"
		class="gallery ui-helper-reset ui-helper-clearfix ui-droppable">
		<s:iterator value="listPubblicazioniPiuVendute">
			<s:if test="nomeImmagine != null">
				<li id="<s:property value='codicePubblicazione'/>"
					coddl="<s:property value='codFiegDl'/>"
					class="ui-widget-content ui-corner-tr">
					<h5 id="titleSpan" class="ui-widget-header"
						style="white-space: nowrap;">
						<s:property value='nomeImmagineShort' />
					</h5> <img class="imgMiniatureClass"
					height="<s:text name="venditeIconHeight"/>px"
					src="/<s:text name="immagineDirAlias"/>/<s:property value='nomeImmagineEscape'/>"
					type="<s:property value='tipoImmagine'/>"
					title="<s:property value='nomeImmagineShort'/>&nbsp;(<s:property value='quantita'/>&nbsp;<s:text name='label.print.Table.Copies.Sold'/>)"
					border="0" /> <a
					href="/immagini/<s:property value='immagineUltimaCopertina'/>"
					rel="thumbnail"
					title="<s:text name="igeriv.immagine.ultima.copertina"/>"
					class="ui-icon ui-icon-zoomin"><s:text
							name="igeriv.immagine.ultima.copertina" /></a>
				</li>
			</s:if>
		</s:iterator>
	</ul>

	<s:form id="ConfigurazioniVenditeForm"
		action="configurazioni_saveMenuSceltaRapida.action" namespace="/"
		method="POST" theme="igeriv" validate="true"
		onsubmit="return (ray.ajax())">

		<div style="width: 100%;">

			<div style="width: 48%; float: left; display: inline-block;">
				<ul id="mostSoldBarL"
					class="gallery configBarraVenditeScrollDiv ui-helper-reset ui-droppable">
					<h4 class="ui-widget-header">
						<span class="ui-icon ui-icon-trash"><s:text
								name="igeriv.barra.scelta.rapida.pubblicazioni" />&nbsp;-&nbsp;<s:text
								name="igeriv.sinistra" /></span>
						<s:text name="igeriv.barra.scelta.rapida.pubblicazioni" />
						&nbsp;-&nbsp;
						<s:text name="igeriv.sinistra" />
					</h4>
					<ul id="iternalUlL" class="gallery ui-helper-reset">
						<s:iterator value="listPubblicazioniBarraSceltaRapidaL">
							<li
								style="display: list-item; width: <s:text name="venditeIconWidth"/>px;"
								id="<s:property value='codicePubblicazione'/>"
								coddl="<s:property value='codFiegDl'/>"
								class="ui-widget-content ui-corner-tr ui-draggable">
								<h5 class="ui-widget-header" class="ellipsis">
									<s:property value='nomeImmagineShort' />
								</h5> <img style="display: inline-block; height: 60px;" alt=""
								class="imgMiniatureClass"
								src="/<s:property value='immagineDirAlias'/>/<s:property value='nomeImmagineEscape'/>"
								type="<s:property value='tipoImmagine'/>" border="0"
								height="<s:text name="venditeIconHeight"/>px"> <a
								href="/immagini/<s:property value='immagineUltimaCopertina'/>"
								rel="thumbnail"
								title="<s:text name="igeriv.immagine.ultima.copertina"/>"
								class="ui-icon ui-icon-zoomin"><s:text
										name="igeriv.immagine.ultima.copertina" /></a>
							</li>
						</s:iterator>
					</ul>
				</ul>
			</div>

			<div style="width: 48%; float: left; display: inline-block;">
				<ul id="mostSoldBarR"
					class="gallery configBarraVenditeScrollDiv ui-helper-reset ui-droppable">
					<h4 class="ui-widget-header">
						<span class="ui-icon ui-icon-trash"><s:text
								name="igeriv.barra.scelta.rapida.pubblicazioni" />&nbsp;-&nbsp;<s:text
								name="igeriv.destra" /></span>
						<s:text name="igeriv.barra.scelta.rapida.pubblicazioni" />
						&nbsp;-&nbsp;
						<s:text name="igeriv.destra" />
					</h4>
					<ul id="iternalUlR" class="gallery ui-helper-reset">
						<s:iterator value="listPubblicazioniBarraSceltaRapidaR">
							<li
								style="display: list-item; width: <s:text name="venditeIconWidth"/>px;"
								id="<s:property value='codicePubblicazione'/>"
								coddl="<s:property value='codFiegDl'/>"
								class="ui-widget-content ui-corner-tr ui-draggable">
								<h5 class="ui-widget-header" class="ellipsis">
									<s:property value='nomeImmagineShort' />
								</h5> <img style="display: inline-block; height: 60px;" alt=""
								class="imgMiniatureClass"
								src="/<s:property value='immagineDirAlias'/>/<s:property value='nomeImmagineEscape'/>"
								type="<s:property value='tipoImmagine'/>" border="0"
								height="<s:text name="venditeIconHeight"/>px"> <a
								href="/immagini/<s:property value='immagineUltimaCopertina'/>"
								rel="thumbnail"
								title="<s:text name="igeriv.immagine.ultima.copertina"/>"
								class="ui-icon ui-icon-zoomin"><s:text
										name="igeriv.immagine.ultima.copertina" /></a>
							</li>
						</s:iterator>
					</ul>
				</ul>
			</div>

		</div>

		<s:hidden id="codiciPubblicazioniL" name="codiciPubblicazioniL" />
		<s:hidden id="coddlL" name="coddlL" />
		<s:hidden id="nomeImmaginiL" name="nomeImmaginiL" />
		<s:hidden id="tipoImmaginiL" name="tipoImmaginiL" />

		<s:hidden id="codiciPubblicazioniR" name="codiciPubblicazioniR" />
		<s:hidden id="coddlR" name="coddlR" />
		<s:hidden id="nomeImmaginiR" name="nomeImmaginiR" />
		<s:hidden id="tipoImmaginiR" name="tipoImmaginiR" />
	</s:form>
</div>

<div style="width: 100%; text-align: center; margin-top: 40px;">
	<input type="button" value="<s:text name='igeriv.memorizza'/>"
		name="igeriv.memorizza" id="memorizza" class="tableFields"
		style="width: 200px; font-size: 110%; text-align: center"
		onclick="javascript: return (setFieldsToSave() && setFormAction('ConfigurazioniVenditeForm','configurazioni_saveMenuSceltaRapida.action', '', 'messageDiv'))" />
</div>

<ul id="updateImgEdicola" class="contextMenu" style="width: 200px;">
	<li class="edit"><a href="#edit"><s:text
				name="igeriv.aggiorna.img.logomarca.edicola" /></a></li>
</ul>
