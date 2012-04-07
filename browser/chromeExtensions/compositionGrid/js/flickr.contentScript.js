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

function svgGrids(paper) {
    var w = paper.width;
    var h = paper.height;
    var diagonalMethod = [[[0.0, 0.0], [Math.min(w, h),     Math.min(w, h)]],
			  [[0.0, h],   [Math.min(w, h),     h - Math.min(w, h)]],
			  [[w,   0.0], [w - Math.min(w, h), Math.min(w, h)]],
			  [[w,   h],   [w - Math.min(w, h), h - Math.min(w, h)]],
			 ];

    var ruleOfThirds = [[[0.0,           h / 3.0],       [w,             h / 3.0]],
			[[0.0,           2.0 * h / 3.0], [w,             2.0 * h / 3.0]],
			[[w / 3.0,       0.0],           [w / 3.0,       h]],
			[[2.0 * w / 3.0, 0.0],           [2.0 * w / 3.0, h]]];

    svgLines(paper, diagonalMethod, "#f00", "diagonal-method");
    svgLines(paper, ruleOfThirds, "#fff", "rule-of-thirds");
}

$("div.photo-div img").each(function (i) {
    var paper = Raphael($(this).offset().left, $(this).offset().top, $(this).width(), $(this).height());
    svgGrids(paper);
});

