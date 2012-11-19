<?php
if ($_SERVER['HTTPS'] != "on") { 
    $url = "https://". $_SERVER['SERVER_NAME'] . $_SERVER['REQUEST_URI']; 
    header("Location: $url"); 
    exit; 
}  
session_start();
require_once('./../database.php');
require_once('./adminsys.php');
$adminsys = new AdminSys();

?>
<!doctype html>
<html lang="us">
<head>
	<meta charset="utf-8">
	<title>Sheep finder admin panel</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" charset="utf-8" language="javascript" src="js/DT_bootstrap.js"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">
	<link rel="stylesheet" type="text/css" href="css/DT_bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/datepicker.css">
    <link href="css/style.css" rel="stylesheet" media="screen">
</head>
<body>
	<div class="container">
		<?php
		if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
			include('./inc/login.php');
		}
		else{
			?>
		<div>
			<h1>Sheep finder admin panel</h1>
		</div>
		<div>
			<ul class="nav nav-tabs">
				<li class="" id="stats">
					<a href="?">Statistics</a>
				</li>
				<li class="" id="users">
					<a href="?p=users">Users</a>
				</li>
				<li class="" id="farms">
					<a href="?p=farms">Farms</a>
				</li>
				<li class="" id="sheeps">
					<a href="?p=sheeps">Sheeps</a>
				</li>
				<li class="" id="logout" style="float: right">
					<a href="?p=logout">Log out</a>
				</li>
			</ul>
			
			<div class="row-fluid">	
			<?php
			if (isset($_GET['p']) && !preg_match('/[^A-Za-z0-9]/', $_GET['p'])) {
				
				$file = './inc/' . $_GET['p'] . '.php';
				if(file_exists($file)){
					include('./inc/' . $_GET['p'] . '.php');
					echo'
					<script>
						$(\'#' . $_GET['p'] . '\').addClass(\'active\');
					</script>
					';
				}
				else {
					include('./inc/stats.php');
					echo'
					<script>
						$(\'#stats\').addClass(\'active\');
					</script>
					';
				}
				
			}
			else {
				include('./inc/stats.php');
				echo'
				<script>
					$(\'#stats\').addClass(\'active\');
				</script>
				';
			}
			?>
			</div>
		</div>
			<?php
		}
		?>
	</div>
</body>
</html>
