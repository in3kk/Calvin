// ¡÷ø‰º“Ωƒ ∞‘Ω√±€ tab
function tab_menu(num){	
	var f = jQuery('.notice_tab').find('li');
	for ( var i = 0; i < f.length; i++ ) {			
		if ( i == num) {			
			f.eq(i).addClass('active');	
			jQuery('.notice_tab0' + i ).show();
		} else {
			f.eq(i).removeClass('active');					
			jQuery('.notice_tab0' + i ).hide();
		}
	}
}

// [START] fade in & out (Author: KANG HEE CHANG / http://hckang80.waplez.com)
		function fadeGallery(obj, timer){
			var $speed = 2000;
			var $wrapper = "#" + obj;

			var $sel = 0;
			var flag = true

			$($wrapper).find(".banner_list").children("li:gt(0)").hide();
			$($wrapper).find(".control").children("li:first-child").addClass("on");
			if($($wrapper).find(".banner_list").children("li").length==1){
				return false;
			}
			function goNext(){
				if(flag == true){
					flag = false;
					$sel++;
					if($sel == $($wrapper).find(".banner_list").children("li").size()){
						$sel = 0;
					}
					$($wrapper).find(".control").children("li").eq($sel).addClass("on");
					$($wrapper).find(".control").children("li").not($($wrapper).find(".control").children("li").eq($sel)).removeClass("on");
					$($wrapper).find(".banner_list").children("li")
						.fadeOut()
						.eq($sel).fadeIn(function(){flag = true;});
				}
				return false;
			}
			$($wrapper).find("#banner_btn_right").click(goNext);

			function goPrev(){
				if(flag == true){
					flag = false;
					$sel--;
					if($sel == -1){
						$sel = $($wrapper).find(".banner_list").children("li").size() - 1;
					}
					$($wrapper).find(".control").children("li").eq($sel).addClass("on");
					$($wrapper).find(".control").children("li").not($($wrapper).find(".control").children("li").eq($sel)).removeClass("on");
					$($wrapper).find(".banner_list").children("li")
						.fadeOut()
						.eq($sel).fadeIn(function(){flag = true;});
				}
				return false;
			}
			$($wrapper).find("#banner_btn_left").click(goPrev);

			// πŸ∑Œ∞°±‚
			function goDirect(){
				if(flag == true && !$(this).parent().hasClass("on")){
					flag = false;
					$sel = $(this).parent().index();
					$(this).parent("li").addClass("on");
					$($wrapper).find(".control").children("li").not($(this).parent("li")).removeClass("on");
					$($wrapper).find(".banner_list").children("li")
						.fadeOut()
						.eq($sel).fadeIn(function(){flag = true;});
				}
				return false;
			}
			$($wrapper).find(".control a").click(goDirect);

			// ¿⁄µøΩ««‡
			var autoPlay;
			function autoChange(){
				autoPlay = setInterval(goNext, timer);
			}
			autoChange();

			// øµø™ ø¿πˆΩ√ ∏ÿ√„
			$($wrapper).find(".banner_list").hover(
				function(){
					clearInterval(autoPlay);
				},
				function(){
					autoChange();
				}
			);
		};
		// »£√‚«‘ºˆ(æ∆¿Ãµ, µÙ∑π¿Ã)
		if($("#gallery").length>0){
			fadeGallery("gallery", 4000);
		}
		if($("#gallery2").length>0){
			fadeGallery("gallery2", 4000);
		}
		// [END] fade in & out
		
//FAQ ∆Óƒß
		
if (typeof console === "undefined" || console === null) {
    console = {
      log: function() {}
    };
  }

var APP = APP || {};

APP.register = function(ns_name){
    var parts = ns_name.split('.'),
    parent = APP;    
    for(var i = 0; i < parts.length; i += 1){
        if(typeof parent[parts[i]] === "undefined"){
               parent[parts[i]] = {};
        }
        parent = parent[parts[i]];
    }
    return parent;
};


APP.isAlphaTween = true;

(function(ns, $,undefined){  
    ns.register('ui.faqAcMenu');           
    ns.ui.faqAcMenu = function(ele, otherC){
    	
		var element, btn, isOpen=false, listArr, titleTxt;
		var otherC = otherC || false;
		var i, max;
		
		element=ele;
 		listArr = $(element).find('>li>dl');
 		
 		btn = $(listArr).find('>dt>a');
 		titleTxt = String(btn.attr('title')).split(" ∫∏±‚")[0];		
 		btn.on('click', openList);
 		
        function listHandler(e) {
      		switch ( e.type ) {
                case 'mouseenter':
                case 'focusin':                  	     	
                                                               
                    break;                    
                case 'focusout':
                case 'mouseleave':
                   
                    break;
			}
      	}   
      	
       function openList(e){
	       	var parent = $(e.currentTarget).parent().parent()
	       	var viewCon = parent.find('>dd')
	       	if(parent.hasClass('on')){
	       		parent.removeClass('on');
	       		viewCon.css('display', 'none');
	       		if(titleTxt != 'undefined')$(e.currentTarget).attr('title', titleTxt+' ∫∏±‚');
	       	}else{
	       		if(otherC){
	       			listArr.removeClass('on');
	       			$(listArr).removeClass('on')
	       			$(listArr).find('>dd').css('display', 'none');
	       			if(titleTxt != 'undefined')$(btn).attr('title', titleTxt+' ∫∏±‚');
	       		}
	       		
	       		parent.addClass('on');       		
	       		viewCon.css('display', 'block');
	       		TweenLite.from(viewCon, 0.3, {css:{opacity:0}});  
	       		if(titleTxt != 'undefined')$(e.currentTarget).attr('title', titleTxt+' ¥›±‚');
	       	}
       	
       
        }
    };
    
    
    
    
}(APP || {}, jQuery));


//escÎ°ú Î†àÏù¥Ïñ¥ Îã´Í∏∞
jQuery(document).ready(function() {
	
	jQuery(document).keydown(function(){
	   if (event.keyCode == 27) 
	     close_divpop('divpop_frm', 'divpop');
	     close_divpop('divpop_frm2', 'divpop2');
	});
	
	function close_divpop(frm, div){
		jQuery("#" + div).hide();
	};
	
});

//esc∑Œ ∑π¿ÃæÓ ¥›±‚
function set_cookie(name, value, expiredays) {
	var todayDate = new Date();
	todayDate.setDate( todayDate.getDate() + expiredays );
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

function close_divpop(frm, pop) {
	var f = eval("document."+frm);
	if(f.chkbox.checked){
	 //set_cookie(pop, "Y", 1); //ø¿¥√ «œ∑Á ø≠¡ˆ æ ±‚
	 set_cookie(pop, "Y", 7); //¿œ¡÷¿œø≠¡ˆæ ±‚
	}
	document.getElementById(pop).style.display = "none";
}

cookiedata = document.cookie;
if(cookiedata.indexOf("divpop=Y") < 0) {
	document.getElementById('divpop').style.display = "block";
	document.getElementById('divpop2').style.display = "block";
} else {
document.getElementById('divpop').style.display = "none";
document.getElementById('divpop2').style.display = "none";
}


