var currentBackgroundScrollHeight = 0;


$(document).ready(function()
{
	scrollBackground();

	setMouseEvents("div#github");
	setMouseEvents("div#facebook");
	setMouseEvents("div#playStore");
});


function scrollBackground()
{
	var height = 256;
	var beginning = "rgba(0, 0, 0, 0) ";
	var image = "url(assets/img/bg.png) ";
	var middle = "repeat scroll 0px ";
	var end = "px / auto padding-box border-box";

	currentBackgroundScrollHeight = (currentBackgroundScrollHeight + 1) % height;
	$("body").css("background", beginning + image + middle + currentBackgroundScrollHeight + end);

	setTimeout("scrollBackground()", 64);
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