$(document).ready(function()
{
		var github = "div#github";
		$(github).mouseenter(function()
		{
			bumpUp(github);
		});

		$(github).mouseleave(function()
		{
			bumpDown(github);
		});


		var playStore = "div#playStore";
		$(playStore).mouseenter(function()
		{
			bumpUp(playStore);
		});

		$(playStore).mouseleave(function()
		{
			bumpDown(playStore);
		});
});


function bumpUp(id)
{
	$(id).animate
	(
		{
			marginBottom: "2px"
		},
		128
	);
}


function bumpDown(id)
{
	$(id).animate
	(
		{
			marginBottom: "0px"
		},
		128
	);
}