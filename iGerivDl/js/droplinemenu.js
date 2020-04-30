/*********************
//* jQuery Drop Line Menu- By Dynamic Drive: http://www.dynamicdrive.com/
//* Last updated: June 27th, 09'
//* Menu avaiable at DD CSS Library: http://www.dynamicdrive.com/style/
*********************/

var subMenuSelected = false;

var droplinemenu = {

	arrowimage: {classname: 'downarrowclass', src: '/app_img/down.gif', leftpadding: 5}, //customize down arrow image
	animateduration: {clickin: 300, clickout: 100}, //duration of slide in/ out animation, in milliseconds
	
	buildmenu:function(menuid) {	
		$(document).ready(function($){
			var $mainmenu = $("#"+menuid+">ul");	
			var $headers = $mainmenu.children("li");	
			var count = 0;									
			$headers.each(function(i) {			
				var $curobj = $(this);
				$curobj.attr('count', count);
				var $subul=$(this).find('ul:eq(0)');
				var subWidth = $subul.width();			
				this._dimensions={h:$curobj.find('a:eq(0)').outerHeight()};
				this.istopheader=$curobj.parents("ul").length==1? true : false;
				if (!this.istopheader) {				
					$subul.css({left:0, top:this._dimensions.h});
				}
				var $innerheader=$curobj.find('a').eq(0);			
				$innerheader=($innerheader.children().eq(0).is('span'))? $innerheader.children().eq(0) : $innerheader;						
				$innerheader.append(
					'<img src="'+ droplinemenu.arrowimage.src
					+'" class="' + droplinemenu.arrowimage.classname
					+ '" style="border:0; padding-left: '+droplinemenu.arrowimage.leftpadding+'px" />'
				);
				$curobj.click(	
					function(e) {
						if (!subMenuSelected) {
							var isReturn = false;
							var $targetul = $(this).parent().children("li");
							var $targetul1 = $(this).children("ul:eq(0)");
							$targetul.each(function() {
								var $targetli = $(this).children("ul:eq(0)");
								if ($targetli.attr('down') == 'true') {	 
									$targetli.slideUp(droplinemenu.animateduration.clickout);
									$targetli.removeAttr('down');
									if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
										$("#breadcrumbDiv").show();	
									}
									isReturn = true;
									return false;
								}
							});
							if (!isReturn) {
								if ($targetul1.queue().length <= 1)	{					
									if (this.istopheader) {							
										$targetul1.css({left:0, top: this._dimensions.h});
										$subul.width(subWidth);																						
									}
									if (document.all && !window.XMLHttpRequest)
										$mainmenu.find('ul').css({overflow: (this.istopheader)? 'hidden' : 'visible'});
									$targetul1.slideDown(droplinemenu.animateduration.clickin);
									if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
										$("#breadcrumbDiv").hide();	
										$(this).show();							
									}
									$targetul1.attr('down','true');
								}
							}
						}
						subMenuSelected = false;
					}				
				);
				count++;
			});
		
		$headers = $mainmenu.find("ul[livello=1]").parent();	
			var count = 0;									
			$headers.each(function(i) {			
				var $curobj=$(this);
				$curobj.attr('count', count);
				var $subul=$(this).find('ul:eq(0)');
				var subWidth = $subul.width();			
				this._dimensions1={h:$curobj.find('a:eq(0)').outerHeight()};
				this.istopheader=$curobj.parents("ul").length==1? true : false;
				if (!this.istopheader) {				
					$subul.css({left:0, top:this._dimensions1.h});
				}
				var $innerheader=$curobj.find('a').eq(0);			
				$innerheader=($innerheader.children().eq(0).is('span'))? $innerheader.children().eq(0) : $innerheader;						
				$innerheader.append(
					'<img src="'+ droplinemenu.arrowimage.src
					+'" class="' + droplinemenu.arrowimage.classname
					+ '" style="border:0; padding-left: '+droplinemenu.arrowimage.leftpadding+'px" />'
				);
				$curobj.click(	
					function(e) {
						subMenuSelected = true;
						var isReturn = false;
						$targetul = $(this);
						var $targetul1 = $targetul.children("ul:eq(0)");
						// Chiudi tutti i menu gia' aperti allo stesso livello tranne quello corrente
						var $sameLevelLiList = $targetul.closest("ul").children("li");
						$sameLevelLiList.each(function() {
							var $ul1 = $(this).children("ul:eq(0)");
							if ($ul1.length > 0 && $ul1.html() != $targetul1.html()) {
								if ($ul1.attr('down') == 'true') {	
									$ul1.slideUp(droplinemenu.animateduration.clickout);
									$ul1.removeAttr('down');
								}
							}
						});
						// Apri o chiudi il menu corrente
						$targetul.each(function() {
							var $targetli = $(this).children("ul:eq(0)");
							if ($targetli.attr('down') == 'true') {	
								$targetli.slideUp(droplinemenu.animateduration.clickout);
								$targetli.removeAttr('down');
								if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
									$("#breadcrumbDiv").show();	
								}
								isReturn = true;
								return false;
							}
						});
						if (!isReturn) {
							if ($targetul1.queue().length <= 1)	{					
								if (this.istopheader) {							
									$targetul1.css({left:0, top: this._dimensions1.h});
									$subul.width(subWidth);																						
								}
								if (document.all && !window.XMLHttpRequest)
									$mainmenu.find('ul').css({overflow: (this.istopheader)? 'hidden' : 'visible'});
								$targetul1.slideDown(droplinemenu.animateduration.clickin);
								if (getBrowser().indexOf("MSIE") != -1 && getBrowserVersion() < 8) {
									$("#breadcrumbDiv").hide();	
									$(this).show();							
								}
								$targetul1.attr('down','true');
							}
						}
					}				
				);
				count++;
			});
			$mainmenu.find("ul").css({display:'none', visibility:'visible', width:$mainmenu.width()});
		});
	}
};