var oneNum = -1;
var twoNum = -1;
jQuery(document).ready(function(){
	//top
	//jQuery(".top ul").children("li").removeClass("on");
	jQuery(".top ul").children("li").each(function(q){
		jQuery(this).mouseover(function(){
			jQuery("#header").stop(true, true).delay(300).animate({height:162}, 480, "easeOutCubic");
			jQuery(".top ul").children("li").removeClass("on");
			jQuery(this).addClass("on");
		});

		jQuery(this).mouseout(function(){
			jQuery("#header").stop(true, true).delay(300).animate({height:32}, 480, "easeOutCubic", function(){jQuery(this).removeClass("on");});
		})
		jQuery(this).focusin(function(){
			jQuery("#header").stop(true, true).delay(300).animate({height:162}, 480, "easeOutCubic");
			jQuery(".top ul").children("li").removeClass("on");
			jQuery(this).addClass("on");
		}).focusout(function(){
			jQuery("#header").stop(true, true).delay(300).animate({height:32}, 480, "easeOutCubic", function(){jQuery(this).removeClass("on");});
		})
	})

jQuery(document).ready(function($){
	setGnb();
});

//GNB
function setGnb(){

	var headerHeight = $("#header").height();

	//링크셋팅
	$(".gsub li").each(function(){
		var link = $(this).children("a").attr("href");
		$(this).on("click",function(){
		//	location.href=link;
		});
	});
	
	//마우스 오버 이벤트
	$("#gnb > li > a").on("mouseenter", function(){
		var $sub = $(this).next(".gsub_box");
		var subHeight = $sub.innerHeight();
		$(".gsub_box").hide();
		$sub.show();

		$("#header").prev(".blind").fadeIn(150);
		$("#header").stop().animate({height:headerHeight+subHeight},150);
	});
	
	//마우스 아웃 이벤트
	$("#gnb_wrap").on("mouseleave", function(){
		$("#header").stop().animate({height:headerHeight},150,function(){
			$(".gsub_box").hide();
		});			
		$("#header").prev(".blind").fadeOut(150);
	});
}







	//gnb
	if(oneNum != -1) activeSub();
	jQuery(".gnb ul").children("li").each(function(q){
		jQuery(this).mouseenter(function(){
			if(oneNum != -1) {
				jQuery(".gnb ul").children("li").eq(oneNum).removeClass("on");
			}
			jQuery(".gnbDiv").stop().animate({height:470}, 480, "easeOutCubic");
			jQuery(this).addClass("on");
		}).focusin(function(){
			jQuery(this).mouseenter();
		})


		jQuery(this).mouseleave(function(){
			jQuery(this).removeClass("on");
			jQuery(".gnbDiv").stop().animate({height:80}, 480, "easeOutCubic");

			if(oneNum != -1) {
				activeSub()
			}
		}).focusout(function(){
			jQuery(this).mouseleave();
		})

		jQuery(this).find(".twoDep").find("a").each(function(){
			jQuery(this).mouseover(function(){
			})

			jQuery(this).mouseout(function(){
			})
		})
	})
})

function activeSub()
{
	jQuery(".gnb ul").children("li").eq(oneNum).addClass("on");
}
