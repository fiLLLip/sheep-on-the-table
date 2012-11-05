<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
	if (isset($_GET['id'])) {
		
		if (isset($_POST['userSubmit'])) {
			if ($adminsys->editUser($_GET['id'], $_POST['inputName'], $_POST['inputUsername'], $_POST['inputEmail'], $_POST['inputPhone'], $_POST['inputPassword'], $_POST['inputSysadmin']) >= 1) {
				echo '<div class="alert alert-success"><b>Success!</b> User updated successfully</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> User not affected. Maybe no changes?</div>';
			}
		}
		elseif (isset($_POST['detailsSubmit'])) {
			$_POST['SMSAlarmAttack'] = ((isset($_POST['SMSAlarmAttack'])) ? '1' : '0');
			$_POST['SMSAlarmStationary'] = ((isset($_POST['SMSAlarmStationary'])) ? '1' : '0');
			$_POST['SMSAlarmTemperature'] = ((isset($_POST['SMSAlarmTemperature'])) ? '1' : '0');
			$_POST['EmailAlarmAttack'] = ((isset($_POST['EmailAlarmAttack'])) ? '1' : '0');
			$_POST['EmailAlarmStationary'] = ((isset($_POST['EmailAlarmStationary'])) ? '1' : '0');
			$_POST['EmailAlarmTemperature'] = ((isset($_POST['EmailAlarmTemperature'])) ? '1' : '0');
			
			if ($adminsys->setUserFarmDetails($_GET['id'], $_GET['farmid'], $_POST['inputLevel'], 
				$_POST['SMSAlarmAttack'], $_POST['SMSAlarmStationary'], 
				$_POST['SMSAlarmTemperature'], $_POST['EmailAlarmAttack'], 
				$_POST['EmailAlarmStationary'], $_POST['EmailAlarmTemperature']) >= 1) {
				
				echo '<div class="alert alert-success"><b>Success!</b> User permissions updated successfully</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> User permissions not affected. Maybe no changes?</div>';
			}
		}
		elseif (isset($_POST['deleteUser'])) {
			if ($adminsys->deleteUser($_GET['id']) >= 1) {
				echo '
				<script>
					window.location.href = \'?p=users&deleteSuccess\';
				</script>
				';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> User not affected. Maybe user doesn\'t exist?</div>';
			}
		}
		elseif (isset($_POST['deleteAccess'])) {
			if ($adminsys->deleteAccess($_GET['id'], $_GET['farmid']) >= 1) {
				echo '
				<script>
					window.location.href = \'?p=users&id=' . $_GET['id'] . '&deleteSuccess\';
				</script>
				';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> User not affected. Maybe user didn\'t have access to farm?</div>';
			}
		}
		elseif (isset($_POST['levelSubmit'])) {
			if ($adminsys->addAccess($_GET['id'], $_POST['inputFarmID'], $_POST['inputLevel']) >= 1) {
				echo '<div class="alert alert-success"><b>Success!</b> Added access successfully.</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Could not add access. Maybe it already exists?</div>';
			}
		}
		$user = $adminsys->getUserDetails($_GET['id']);
		$username = $user[0]['un'];
		?>
			<div class="page-header">
				<h2>
					Details of #<?php echo $_GET['id'] . ' - ' . $username; ?>
				</h2>
			</div>
		<?php
		if (isset($_GET['farmid'])) {
			$details = $adminsys->getUserFarmDetails($_GET['id'], $_GET['farmid']);
			$level = $details[0]['level'];
			$SMSAlarmAttack = $details[0]['SMSAlarmAttack'];
			$SMSAlarmStationary = $details[0]['SMSAlarmStationary'];
			$SMSAlarmTemperature = $details[0]['SMSAlarmTemperature'];
			$EmailAlarmStationary = $details[0]['EmailAlarmStationary'];
			$EmailAlarmAttack = $details[0]['EmailAlarmAttack'];
			$EmailAlarmTemperature = $details[0]['EmailAlarmTemperature'];
			
			?>
			<form action="" class="form-horizontal" method="post">
				<div class="span6">
					<div class="control-group">
						<label class="control-label"><h4>Farm #<?php echo $_GET['farmid']; ?></h4></label>
						<div class="controls">&nbsp;</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputLevel">Level</label>
						<div class="controls">
							<select id="inputLevel" name="inputLevel">
								<option value="0" <?php echo ($level=='0') ? 'selected="selected"' : ''; ?>>View only</option>
								<option value="1" <?php echo ($level=='1') ? 'selected="selected"' : ''; ?>>Admin</option>
								<option value="2" <?php echo ($level=='2') ? 'selected="selected"' : ''; ?>>Owner</option>
							</select>
						</div>
					</div>
				</div>
				<div class="span2">
					<div class="control-group">
						<h4>Email</h4>
						<label class="checkbox">
							<input type="checkbox" name="EmailAlarmAttack" value="1" <?php echo ($EmailAlarmAttack == '1') ? 'checked="checked"' : ''; ?>> Attack
						</label>
						<label class="checkbox">
							<input type="checkbox" name="EmailAlarmTemperature" value="1" <?php echo ($EmailAlarmTemperature == '1') ? 'checked="checked"' : ''; ?>> Temperature
						</label>
						<label class="checkbox">
							<input type="checkbox" name="EmailAlarmStationary" value="1" <?php echo ($EmailAlarmStationary == '1') ? 'checked="checked"' : ''; ?>> Stationary
						</label>
					</div>
				</div>
				<div class="span4">
					<div class="control-group">	
						<h4>SMS</h4>
						<label class="checkbox">
							<input type="checkbox" name="SMSAlarmAttack" value="1" <?php echo ($SMSAlarmAttack == '1') ? 'checked="checked"' : ''; ?>> Attack
						</label>
						<label class="checkbox">
							<input type="checkbox" name="SMSAlarmTemperature" value="1" <?php echo ($SMSAlarmTemperature == '1') ? 'checked="checked"' : ''; ?>> Temperature
						</label>
						<label class="checkbox">
							<input type="checkbox" name="SMSAlarmStationary" value="1" <?php echo ($SMSAlarmStationary == '1') ? 'checked="checked"' : ''; ?>> Stationary
						</label>
					</div>
					<div class="control-group">
						<button type="submit" name="detailsSubmit" class="btn">Save</button>
						<button class="btn btn-danger" type="button" id="deleteAccess" data-toggle="modal" data-target="#deleteModal">Delete access</button>
					</div>
				</div>
				<div id="deleteModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="myModalLabel">Are you sure???</h3>
					</div>
						<div class="modal-body">
						<p>Do you really want to delete this access?</p>
					</div>
					<div class="modal-footer">
						<button type="submit" name="deleteAccess" class="btn btn-danger">Yes</button>
						<button class="btn" data-dismiss="modal" aria-hidden="true">No</button>
					</div>
				</div>
			</form>			
			<?php
		}
		else {
			$user = $adminsys->getUserDetails($_GET['id']);
			$name = $user[0]['name'];
			$username = $user[0]['un'];
			$name = $user[0]['name'];
			$email = $user[0]['email'];
			$phone = $user[0]['phone'];
			$lastip = $user[0]['ip'];
			$sysadmin = $user[0]['sysadmin'];
			if (isset($_GET['deleteSuccess'])) {
				echo '<div class="alert alert-success"><b>Success!</b> Removed user access to farm successfully</div>';
			}
			if (isset($_GET['newSuccess'])) {
				echo '<div class="alert alert-success"><b>Success!</b> User created successfully</div>';
			}
			?>
			
			<form action="" class="form-horizontal" method="post">
				<div class="span6">
					<input type="hidden" name="id" value="<?php echo $_GET['id']; ?>">	
					<div class="control-group">
						<label class="control-label" for="inputName">Name</label>
						<div class="controls">
							<input type="text" id="inputName" name="inputName" placeholder="Name" value="<?php echo $name; ?>">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputUsername">Username</label>
						<div class="controls">
							<input type="text" id="inputUsername" name="inputUsername" placeholder="Username" value="<?php echo $username; ?>">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputEmail">Email</label>
						<div class="controls">
							<input type="text" id="inputEmail" name="inputEmail" placeholder="Email" value="<?php echo $email; ?>">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputPhone">Phone</label>
						<div class="controls">
							<input type="text" id="inputPhone" name="inputPhone" placeholder="Phone" value="<?php echo $phone; ?>">
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="inputPassword">New password</label>
						<div class="controls">
							<input type="text" id="inputPassword" name="inputPassword" placeholder="New password" value="">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputSysadmin">Userlevel</label>
						<div class="controls">
							<select id="inputSysadmin" name="inputSysadmin">
								<option value="0" <?php echo ($sysadmin=='0') ? 'selected="selected"' : ''; ?>>Normal user</option>
								<option value="1" <?php echo ($sysadmin=='1') ? 'selected="selected"' : ''; ?>>System admin</option>
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputLastIP">Last IP</label>
						<div class="controls">
							<input type="text" id="inputLastIP" name="inputLastIP" placeholder="Last IP" value="<?php echo $lastip; ?>" readonly="">
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button type="submit" name="userSubmit" class="btn">Save</button>
							<button class="btn btn-danger" type="button" id="deleteUser" data-toggle="modal" data-target="#deleteModal">Delete user</button>
						</div>
					</div>
				</div>
				<div id="deleteModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h3 id="myModalLabel">Are you sure???</h3>
					</div>
						<div class="modal-body">
						<p>Do you really want to delete this user?</p>
						<p>This action deletes the user and all related permissions</p>
					</div>
					<div class="modal-footer">
						<button type="submit" name="deleteUser" class="btn btn-danger">Yes</button>
						<button class="btn" data-dismiss="modal" aria-hidden="true">No</button>
					</div>
				</div>
			</form>
			<?php
		}
		
		$farms = $adminsys->getFarmlist();
			?>
			<div class="tabbable"> <!-- Only required for left/right tabs -->
				<ul class="nav nav-tabs">
					<li class="active"><a href="#accessTable" data-toggle="tab">Farms overview</a></li>
					<li><a href="#newAccess" data-toggle="tab">Add new access</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="accessTable">
						<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered clickable" id="updatetable">
							<thead> 
								<tr> 
									<th colspan="2">&nbsp;</th> 
									<th colspan="3" style="text-align:center;">SMS</th> 
									<th colspan="3" style="text-align:center;">Email</th>
								</tr>
								<tr> 
									<th>Farm ID</th> 
									<th>Level</th> 
									<th>Attack</th> 
									<th>Temperature</th> 
									<th>Stationary</th> 
									<th>Attack</th> 
									<th>Temperature</th> 
									<th>Stationary</th> 
								</tr> 
							</thead> 
							<tbody>
								<tr>
									<td colspan="8" class="dataTables_empty">Loading data from server</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="tab-pane" id="newAccess">			
						<form action="" class="form-horizontal" method="post">
							<div class="span6">
								<div class="control-group">
									<label class="control-label" for="inputFarmID">Farm ID</label>
									<div class="controls">
										<select id="inputFarmID" name="inputFarmID">
											<?php
												foreach ($farms as $farm) {
													echo '<option value="' . $farm['id'] . '">' . $farm['id'] . ' - ' . $farm['name'] . '</option>';
												}
											?>
										</select>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="inputLevel">Level</label>
									<div class="controls">
										<select id="inputLevel" name="inputLevel">
											<option value="0">View only</option>
											<option value="1">Admin</option>
											<option value="2">Owner</option>
										</select>
									</div>
								</div>
								<div class="control-group">
									<div class="controls">
										<button type="submit" name="levelSubmit" class="btn">Save</button>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<script>
				$(document).ready(function() {
					$.extend( $.fn.dataTableExt.oStdClasses, {
						"sWrapper": "dataTables_wrapper form-inline"
					} );
				
					var pTable = $('#updatetable').dataTable( {
						"bProcessing": true,
						"bServerSide": true,
						"sAjaxSource": "ajax.php?table=permissions&id=<?php echo $_GET['id']; ?>",
						"bAutoWidth": true,
						"aaSorting": [[ 0, "asc" ]],
						"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
					} );
										
					$('#updatetable tbody tr').live('click',function(){
						var aData = pTable.fnGetData(this);
						//alert("Going to details for Sheep with ID:" + aData[0]);
						location.href='?p=users&id=<?php echo $_GET['id']; ?>&farmid='+aData[0];
					});
					
					$('.alert').delay(5000).hide('slow');
				} );
			</script>
		<?php
	}
	else {
		?>
			<ul class="nav nav-pills">
				<li <?php echo (!isset($_GET['new'])) ? 'class="active"' : ''; ?>>
					<a href="?p=users">Overview</a>
				</li>
				<li <?php echo (isset($_GET['new'])) ? 'class="active"' : ''; ?>>
					<a href="?p=users&new">Add new user</a>
				</li>
			</ul>
		<?php
		if (isset($_GET['new'])) {
			if(isset($_POST['newUserSubmit'])){
				$userid = $adminsys->newUser($_POST['inputName'], $_POST['inputUsername'], 
					$_POST['inputEmail'], $_POST['inputPhone'], $_POST['inputPassword'], 
					$_POST['inputConfirmPassword'], $_POST['inputSysadmin']);
				if ($userid >= 1) {
					//SUCCESS
					echo '
					<script>
						window.location.href = \'?p=users&id=' . $userid . '&newSuccess\';
					</script>
					';
				}
				else {
					//FAILURE
					echo '<div class="alert alert-warning"><b>Warning!</b> Could not add new user</div>';
				}
			}
		
			?>
				<form action="" class="form-horizontal" method="post">
					<div class="span6">
						<div class="control-group">
							<label class="control-label" for="inputName">Name</label>
							<div class="controls">
								<input type="text" id="inputName" name="inputName" placeholder="Name" value="<?php echo $_POST['inputName']; ?>">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputUsername">Username</label>
							<div class="controls">
								<input type="text" id="inputUsername" name="inputUsername" placeholder="Username" value="<?php echo $_POST['inputUsername']; ?>">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputEmail">Email</label>
							<div class="controls">
								<input type="text" id="inputEmail" name="inputEmail" placeholder="Email" value="<?php echo $_POST['inputEmail']; ?>">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputPhone">Phone</label>
							<div class="controls">
								<input type="text" id="inputPhone" name="inputPhone" placeholder="Phone" value="<?php echo $_POST['inputPhone']; ?>">
							</div>
						</div>
					</div>
					<div class="span6">
						<div class="control-group">
							<label class="control-label" for="inputPassword">Password</label>
							<div class="controls">
								<input type="text" id="inputPassword" name="inputPassword" placeholder="Password">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputConfirmPassword">Confirm password</label>
							<div class="controls">
								<input type="text" id="inputConfirmPassword" name="inputConfirmPassword" placeholder="Confirm password">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputSysadmin">Userlevel</label>
							<div class="controls">
								<select id="inputSysadmin" name="inputSysadmin">
									<option value="0" <?php echo ($_POST['inputSysadmin']=='0') ? 'selected="selected"' : ''; ?>>Normal user</option>
									<option value="1" <?php echo ($_POST['inputSysadmin']=='1') ? 'selected="selected"' : ''; ?>>System admin</option>
								</select>
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button type="submit" name="newUserSubmit" class="btn">Save</button>
							</div>
						</div>
					</div>
				</form>
			<?php
		}
		else {
			if (isset($_GET['deleteSuccess'])) {
				echo '<div class="alert alert-success"><b>Success!</b> User deleted successfully</div>';
			}
			?>
				<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datatable">
					<thead> 
						<tr> 
							<th>ID</th> 
							<th>Username</th> 
							<th>Name</th>
							<th>Email</th>
							<th>Phone</th>
							<th>Sysadmin</th>
						</tr> 
					</thead> 
					<tbody>
						<tr>
							<td colspan="6" class="dataTables_empty">Loading data from server</td>
						</tr>
					</tbody>
				</table>
				<script>
					$(document).ready(function() {
						$.extend( $.fn.dataTableExt.oStdClasses, {
							"sWrapper": "dataTables_wrapper form-inline"
						} );
					
						var pTable = $('#datatable').dataTable( {
							"bProcessing": true,
							"bServerSide": true,
							"sAjaxSource": "ajax.php?table=user",
							"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
						} );
						
						$('#datatable tbody tr').live('click',function(){
							var aData = pTable.fnGetData(this);
							//alert("Going to details for Sheep with ID:" + aData[0]);
							location.href='?p=users&id='+aData[0];
						});
						
						$('.alert').delay(5000).hide('slow');
					} );
				</script>
			<?php
		}
	}
?>