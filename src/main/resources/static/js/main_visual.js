var vInterval, vDuration = 6000;
var vCurNum, vMaxNum;
$(function(){
	//
	vMaxNum = jQuery(".mainVisual>.visualBg").size()-1;
	vCurNum = 0;
	if(vMaxNum != 0) {
		vInterval = setInterval("visual()", vDuration);	
	} else {
		jQuery(".controller").css("display", "none");
		jQuery(".controller2").css("display", "none");
	}
	jQuery(".mainVisual>.controller>.btns>a").each(function(q){
		jQuery(this).hover(function(){
			clearInterval(vInterval);
			jQuery(this).find("img").attr("src", jQuery(this).find("img").attr("src").replace(".png", ".png"));
		}, function(){
			clearInterval(vInterval);
			vInterval = setInterval("visual()", vDuration);
			jQuery(this).find("img").attr("src", jQuery(this).find("img").attr("src").replace(".png", ".png"));
		}).click(function(){
			if(q) {
				jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace("_up.png", ".png"));
				jQuery(".visualBg").eq(vCurNum).stop().animate({left: "-100%"}, 500);
				vCurNum++;
				if(vCurNum > vMaxNum) vCurNum = 0;
				jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace(".png", "_up.png"));
				jQuery(".visualBg").eq(vCurNum).stop().animate({left: "100%"}, 0).stop().animate({left: "0%"}, 500);
			} else {
				jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace("_up.png", ".png"));
				jQuery(".visualBg").eq(vCurNum).stop().animate({left: "100%"}, 500);
				vCurNum--;
				if(vCurNum < 0) vCurNum = vMaxNum;
				jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace(".png", "_up.png"));
				jQuery(".visualBg").eq(vCurNum).stop().animate({left: "-100%"}, 0).stop().animate({left: "0%"}, 500);
			}
		})
	})
	
	
	//
	jQuery(".mainVisual>.controller2 a").each(function(q){
		jQuery(this).click(function(){
			if(q != vCurNum) {
				jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace("_up.png", ".png"));
				jQuery(".visualBg").eq(vCurNum).stop().animate({left: "-100%"}, 500);
				vCurNum = q;
				jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace(".png", "_up.png"));
				jQuery(".visualBg").eq(vCurNum).stop().animate({left: "100%"}, 0).stop().animate({left: "0%"}, 500);
			}
		}).hover(function(){
			clearInterval(vInterval);
		}, function(){
			clearInterval(vInterval);
			vInterval = setInterval("visual()", vDuration);
		})
	})


	
})

function visual()
{
	jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace("_up.png", ".png"));
	jQuery(".visualBg").eq(vCurNum).stop().animate({left: "-100%"}, 500);
	vCurNum++;
	if(vCurNum > vMaxNum) vCurNum = 0;
	jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src", jQuery(".mainVisual>.controller2 a").eq(vCurNum).find("img").attr("src").replace(".png", "_up.png"));
	jQuery(".visualBg").eq(vCurNum).stop().animate({left: "100%"}, 0).stop().animate({left: "0%"}, 500);
}