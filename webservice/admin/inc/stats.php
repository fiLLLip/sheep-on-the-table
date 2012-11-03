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
		<img src="img/user.png" class="staticon" /><br />
		<?php echo $stats['users']; ?>
	</div>
	<div class="span3">
		<span>Farms</span><br />
		<img src="img/farm.png" class="staticon" /><br />
		<?php echo $stats['farms']; ?>
	</div>
	<div class="span3">
		<span>Sheeps</span><br />
		<img src="img/sheep.png" class="staticon" /><br />
		<?php echo $stats['sheeps']; ?>
	</div>
	<div class="span3">
		<span>Updates</span><br />
		<img src="img/update.png" class="staticon" /><br />
		<?php echo $stats['updates']; ?>
	</div>
	<br />
</div>