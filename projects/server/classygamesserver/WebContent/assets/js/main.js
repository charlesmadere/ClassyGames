$(document).ready(function()
{
	setMouseEvents("div#github");
	setMouseEvents("div#facebook");
	setMouseEvents("div#playStore");
});


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
			marginBottom: "4px"
		},
		160
	);
}


function bumpDown(element)
{
	$(element).animate
	(
		{
			marginBottom: "0px"
		},
		160
	);
}