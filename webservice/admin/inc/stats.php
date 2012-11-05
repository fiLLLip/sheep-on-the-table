<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
	$stats = $adminsys->getStats();
?>
<br />
<div class="row-fluid stats">
	<div class="span3">
		<span>Users</span><br />
		<a href="?p=users">
			<img src="img/user.png" rel="tooltip" title="Click to go to sheeps" class="iconTooltip staticon" /><br />
		</a>
		<?php echo $stats['users']; ?>
	</div>
	<div class="span3">
		<span>Farms</span><br />
		<a href="?p=farms">
			<img src="img/farm.png" rel="tooltip" title="Click to go to sheeps" class="iconTooltip staticon" /><br />
		</a>
		<?php echo $stats['farms']; ?>
	</div>
	<div class="span3">
		<span>Sheeps</span><br />
		<a href="?p=sheeps">
			<img src="img/sheep.png" rel="tooltip" title="Click to go to sheeps" class="iconTooltip staticon" /><br />
		</a>
		<?php echo $stats['sheeps']; ?>
	</div>
	<div class="span3">
		<span>Updates</span><br />
		<img src="img/update.png" class="staticon" /><br />
		<?php echo $stats['updates']; ?>
	</div>
	<br />
</div>
<script>
	$(document).ready(function(){
		$('.iconTooltip').tooltip();
	});
</script>