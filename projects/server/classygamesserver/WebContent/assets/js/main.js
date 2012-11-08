var currentBackgroundScrollHeight = 0;


$(document).ready(function()
{
	resizeWindow();
	scrollBackground();

	setMouseEvents("div#github");
	setMouseEvents("div#facebook");
	setMouseEvents("div#playStore");
});


function resizeWindow()
{
	var windowHeight = $(window).height();
	var windowWidth = $(window).width();

	var height = 256;
	var width = 512;

	if (windowWidth >= 1536 && windowHeight >= 896)
	{
		height = 768;
		width = 1536;
	}
	else if (windowWidth >= 1280 && windowHeight >= 768)
	{
		height = 640;
		width = 1280;
	}
	else if (windowHeight >= 1024 && windowWidth >= 640)
	{
		height = 512;
		width = 1024;
	}
	else if (windowWidth >= 768 && windowHeight >= 512)
	{
		height = 384;
		width = 768;
	}

	$("div#logo").css("background-image", "url(\"assets/img/logo-" + width + ".png\")");
	$("div#logo").css("margin-left", 0 - (width / 2));
	$("div#logo").css("margin-top", 0 - (height * (4 / 7)));
	$("div#logo").css("height", height);
	$("div#logo").css("width", width);
}


$(window).resize(function()
{
	resizeWindow();
});


function scrollBackground()
{
	var height = 32;
	var beginning = "rgba(0, 0, 0, 0) ";
	var image = "url(\"assets/img/bg.png\") ";
	var middle = "repeat scroll 0px ";
	var end = "px / auto padding-box border-box";

	currentBackgroundScrollHeight = (currentBackgroundScrollHeight + 1) % height;
	$("body").css("background", beginning + image + middle + currentBackgroundScrollHeight + end);

	setTimeout("scrollBackground()", 128);
}


function setMouseEvents(element)
{
	$(element).mouseenter(function()
	{
		bumpUp(element);
	});

	$(element).mouseleave(function()
	{
		bumpDown(element);
	});
}


function bumpUp(element)
{
	$(element).animate
	(
		{
			marginBottom: "3px"
		},
		128
	);
}


function bumpDown(element)
{
	$(element).animate
	(
		{
			marginBottom: "0px"
		},
		128
	);
}