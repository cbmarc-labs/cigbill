// jcAutosize 0.02  
// jQuery auto height plugin for textareas
// (c) 2012 Jaspreet Chahal - http://jaspreetchahal.org 
// above line should always be kept 
// license: www.opensource.org/licenses/mit-license.php

jQuery.fn.jcautosize = function(settings) {
  settings = jQuery.extend({
    padding: '5px',
    lineHeight: '100%',
    height: '50px',
    width: '400px',
    border: '3px solid #CCC',
    borderRadius:'5px',
    boxShadowColor:'rgba(4, 174, 174, 0.7)',
    applyCSS:true, /*all above rules will be applied*/
    maxHeight:250, /*if this value is less that original box height then it will not be applied*/
    extraTail:5 /*in pixels. extra space to get rid of line flicker, 
                   this value should be zero if padding is good enough or this 
                   value should be increased if padding is set to zero*/
  },settings);
  var previousHeight = 0;
  var resize = function(el){
    jQuery(el).css('height','0px');
    var h = Math.max(el.originalHeight, (el.scrollHeight));    
    previousHeight = previousHeight || el.originalHeight;    
    // smooth out maxheight
    settings.maxHeight = (settings.maxHeight < el.originalHeight)?0:settings.maxHeight;
    // when should scrollbars appear? if maxHeight is set to ZERO they will never appear
    (h>settings.maxHeight && settings.maxHeight>0)?jQuery(el).css('overflow','auto'):jQuery(el).css('overflow','hidden');
    if(h > el.originalHeight && (h <= settings.maxHeight || settings.maxHeight ==0)){
        // add a bit of room to avoid line flickr
        jQuery(el).css('height',(h+settings.extraTail)+'px'); 
        previousHeight = h;
      }
      else if(h > el.originalHeight) {
        jQuery(el).css('height',settings.maxHeight+'px'); 
      }
      else {
        jQuery(el).css('height',(el.originalHeight)+'px');      
      }
    }
    return this.each(function(){
      this.originalHeight = parseInt(jQuery(this).css('height').replace('px','')); 
      jQuery(this).scrollHeight = 99999999; 
      // apply height and width
      jQuery(this).css({
        /*'height': settings.height,
        'width': settings.width    */    
      });
      var defaultStyles = {
        'outline': 'none !important',
        'padding': settings.padding,
        'border': settings.border,
        'borderRadius':settings.borderRadius,
        'boxShadow':'none'
      };
      // just so that if text area already have some value
      if(settings.applyCSS) {
        forcedHeight = parseInt(settings.height.replace('px',''));
        if(forcedHeight > 0) {
          this.originalHeight  = forcedHeight;
        }
        jQuery(this).css(defaultStyles);   
        jQuery(this).bind('focus',function(){
          jQuery(this).css({
            'boxShadow': '0px 0px 20px '+settings.boxShadowColor,
            'padding': settings.padding,
            'border': settings.border,
            'borderRadius':settings.borderRadius
          });
        })    
        jQuery(this).bind('blur',function(){jQuery(this).css(defaultStyles)});
      }
      resize(this);
      
      jQuery(this).bind('keydown',function(){resize(this)})    
    });
  };