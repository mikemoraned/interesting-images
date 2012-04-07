console.log("flickr content script loaded")

$("div.photo-div img").each(function (i) {
    var src = $(this).attr("src");
    console.log("img src: " + src);
    var img = new Image();
    img.src = src;
    console.log("Created Image");
    var imgElem = $(this);
    var parentDiv = $(this).parent();
    img.onload = function() {
	parentDiv.append("<canvas id='canvas' width=\"" + img.width + "\" height=\"" + img.height + "\" />");
	var canvas = document.getElementById("canvas");
	console.log("Created canvas");
	var context = canvas.getContext("2d");
	context.drawImage(img, 0, 0);
	console.log("Drew image in canvas");
	imgElem.css("display","none");
	console.log("Hid original image");
	drawRuleOfThirdsOverlay(context, img.width, img.height);
	drawDiagonalMethodOverlay(context, img.width, img.height);
    }
});

function drawRuleOfThirdsOverlay(context, width, height) {
    var grid = [[[0.0,  0.33], [1.0,  0.33]],
                [[0.0,  0.66], [1.0,  0.66]],
                [[0.33, 0.0],  [0.33, 1.0]],
                [[0.66, 0.0],  [0.66,1.0]]];
    context.beginPath();
    for (var i = 0; i < grid.length; i++) {
	var line = grid[i];
	var start = line[0];
	var end = line[1];
	context.moveTo(start[0] * width, start[1] * height);
	context.lineTo(end[0] * width, end[1] * height);
    }
    context.strokeStyle = "#fff";
    context.stroke();
}

function drawDiagonalMethodOverlay(context, width, height) {
    var grid = [[[0.0,  0.0],    [Math.min(width, height),  Math.min(width, height)]],
                [[0.0,  height], [Math.min(width, height),  height - Math.min(width, height)]],
                [[width, 0.0],   [width - Math.min(width, height), Math.min(width, height)]],
                [[width, height],[width - Math.min(width, height),  height - Math.min(width, height)]],
                ];
    context.beginPath();
    for (var i = 0; i < grid.length; i++) {
	var line = grid[i];
	var start = line[0];
	var end = line[1];
	context.moveTo(start[0], start[1]);
	context.lineTo(end[0], end[1]);
    }
    context.strokeStyle = "#f00";
    context.stroke();
}
