console.log("flickr content script loaded")

function svgLines(paper, lines, stroke, name) {
    for (var i = 0; i < lines.length; i++) {
	var line = lines[i];
	var start = line[0];
	var end = line[1];
	var p = paper.path("M" + start[0] + "," + start[1] + "L" + end[0] + "," + end[1]).attr({"stroke": stroke});
	p.node.setAttribute("class", name);
    }
}

function svgGrid(paper) {
    var width = paper.width;
    var height = paper.height;
    var diagonalMethod = [[[0.0,   0.0],    [Math.min(width, height),         Math.min(width, height)]],
			  [[0.0,   height], [Math.min(width, height),         height - Math.min(width, height)]],
			  [[width, 0.0],    [width - Math.min(width, height), Math.min(width, height)]],
			  [[width, height], [width - Math.min(width, height), height - Math.min(width, height)]],
			 ];

    var ruleOfThirds = [[[0.0,               height / 3.0],       [width,             height / 3.0]],
			[[0.0,               2.0 * height / 3.0], [width,             2.0 * height / 3.0]],
			[[width / 3.0,       0.0],                [width / 3.0,       height]],
			[[2.0 * width / 3.0, 0.0],                [2.0 * width / 3.0, height]]];

    svgLines(paper, diagonalMethod, "#f00", "diagonal-method");
    svgLines(paper, ruleOfThirds, "#fff", "rule-of-thirds");
}

$("div.photo-div img").each(function (i) {
    var paper = Raphael($(this).offset().left, $(this).offset().top, $(this).width(), $(this).height());
    svgGrid(paper);
});

