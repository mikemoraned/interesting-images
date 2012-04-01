<!doctype html>

<html lang="en">
<head>
<meta charset="utf-8">
<title>Chain</title>

<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/css/chain.css" rel="stylesheet">

</head>

<body>
	<div class="container">
		<div class="row">
			<div class="code span12">
				<img src="/image.300x300.png" />
			</div>
		</div>
		<div id="events"></div>
	</div>

	<script id="unsuccessful-claim-template" type="text/x-mustache-tmpl">
    <div class="alert alert-success">
		Oh dear, maybe someone got to it already?
    </div>
    </script>

	<script id="successful-claim-template" type="text/x-mustache-tmpl">
    <div class="alert alert-success">
		Yay!
    </div>
    </script>

	<script id="user-joined-template" type="text/x-mustache-tmpl">
    <div class="alert alert-success">
		Welcome!
    </div>
    </script>

	<script id="default-template" type="text/x-mustache-tmpl">
    <div class="alert">
		{{name}} happened. Reload for more detail?
    </div>
    </script>

	<script src="/js/jquery-1.7.2.min.js"></script>
	<script src="/js/mustache.js"></script>
	<script src="/js/chain.js"></script>
</body>
</html>
