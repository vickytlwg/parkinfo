$(".conlsr1").click(function(){
		$(".conlsr1").hide();
		$(".sdmenu").hide();
		$(".conlsr2").show();
		$(".conl").stop().animate({"width":"56px"},400);
		$(".conr").stop().animate({"paddingLeft":"79px"},400);
});
$(".conlsr2").click(function(){
		$(".conlsr1").show();
		$(".sdmenu").show();
		$(".conlsr2").hide();
		$(".conl").stop().animate({"width":"218px"},400);
		$(".conr").stop().animate({"paddingLeft":"241px"},400);
});
$(".listmoreshow").click(function(){
		$(".listmoreshow").hide();
		$(".listmorehide").show();
		$(".list0").stop().show();
});
$(".listmorehide").click(function(){
		$(".listmoreshow").show();
		$(".listmorehide").hide();
		$(".list0").stop().hide();
});
$(".ditusrshow").click(function(){
		$(".keshihual").hide();
		$(".ditusrshow").hide();
		$(".ditusrhide").show();
		$(".dituz").addClass("dituzNav");
});
$(".ditusrhide").click(function(){
		$(".keshihual").show();
		$(".ditusrshow").show();
		$(".ditusrhide").hide();
		$(".dituz").removeClass("dituzNav");
});















//返回顶部
$(".xxxxxxxxx").click(function(){
		$('html, body').animate({scrollTop:0}, 600);
});

//
var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        slidesPerView: 1,
        paginationClickable: true,
        spaceBetween: 30,
        loop: true,
        autoplay: 3000,
        autoplayDisableOnInteraction: false
    });