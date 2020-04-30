/* jquery.imagefit 
 *
 * Version 0.2 by Oliver Boermans <http://www.ollicle.com/eg/jquery/imagefit/>
 *
 * Extends jQuery <http://jquery.com>
 *
 * Modifications done by slinouille for Resize Image To Fit Screen SMF mod
 *
 */
 (function($) {
	$.fn.imagefit = function(options) {
		var settings = {
			"elements_excluded": "",//table img, .smiley
			"elements_included": ".bbc_img"
		};
		if (options) 
			$.extend(settings, options);
			
		var fit = {
			all : function(imgs){
				imgs.each(function(){
					fit.one(this);
					})
				},
			one : function(img){
				$(img)
					.width('100%').each(function()
					{
						$(this).height(Math.round(
							$(this).attr('startheight')*($(this).width()/$(this).attr('startwidth')))
						);
					})
				}
		};
		
		this.each(function(){
				var container = this;
				
				// store list of contained images (excluding those in tables)
				//var imgs = $(settings.elements_included, container).not($(settings.elements_excluded));	=> NOT USED ACTUALLY !!
				var imgs = $(settings.elements_included, container);
				// store initial dimensions on each image 
				imgs.each(function(){
					$(this).attr('startwidth', $(this).width())
						.attr('startheight', $(this).height())
						.css('max-width', $(this).attr('startwidth')+"px");
				
					fit.one(this);
				});
				// Re-adjust when window width is changed
				$(window).bind('resize', function(){
					fit.all(imgs);
				});
			});
		return this;
	};
})(jQuery);