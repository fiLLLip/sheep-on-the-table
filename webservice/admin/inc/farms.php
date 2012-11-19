<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
if (isset($_GET['id'])) {
		
		if (isset($_POST['farmSubmit'])) {
			
			if ($adminsys->editFarm($_GET['id'], $_POST['inputName'], $_POST['inputAddress']) >= 1) {
				echo '<div class="alert alert-success"><b>Success!</b> Farm updated successfully</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Farm not affected. Maybe no changes?</div>';
			}
		}
		elseif (isset($_POST['detailsSubmit'])) {
				
			if ($adminsys->setFarmUserDetails($_GET['userid'], $_GET['id'], $_POST['inputLevel'] ) >= 1) {
				
				echo '<div class="alert alert-success"><b>Success!</b> User permissions updated successfully</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> User permissions not affected. Maybe no changes?</div>';
			}
		}
		elseif (isset($_POST['deleteAccess'])) {
			if ($adminsys->deleteAccess($_GET['userid'], $_GET['id']) >= 1) {
				echo '
				<script>
					window.location.href = \'?p=farms&id=' . $_GET['id'] . '&deleteSuccess\';
				</script>
				';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> User not affected. Maybe user didn\'t have access to farm?</div>';
			}
		}
		elseif (isset($_POST['deleteFarm'])) {
			if ($adminsys->deleteFarm($_GET['id']) >= 1) {
				echo '
				<script>
					window.location.href = \'?p=farms&deleteSuccess\';
				</script>
				';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Farm not affected. Maybe farm doesn\'t exist?</div>';
			}
		}
		elseif (isset($_POST['levelSubmit'])) {
			if ($adminsys->newAccess($_POST['inputUserID'], $_GET['id'], $_POST['inputLevel']) >= 1) {
				echo '<div class="alert alert-success"><b>Success!</b> Added access successfully.</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Could not add access. Maybe it already exists?</div>';
			}
		}
		
		$farm = $adminsys->getFarmDetails($_GET['id']);
		$name = $farm[0]['name'];
		?>
			<div class="page-header">
				<h2>
					Details of #<?php echo $_GET['id'] . ' - ' . $name; ?>
				</h2>
			</div>
		<?php
		if (isset($_GET['userid'])) {
			$details = $adminsys->getUserFarmDetails($_GET['userid'], $_GET['id']);
			$level = $details[0]['level'];
			
			?>
				<form action="" class="form-horizontal" method="post">
					<div class="span12">
						<div class="control-group">
							<label class="control-label"><h4>User #<?php echo $_GET['userid']; ?></h4></label>
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
						<div class="control-group">
							<label class="control-label">&nbsp;</label>
							<div class="controls">
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
					</div>
				</form>			
			<?php
		}
		else {
			$farm = $adminsys->getFarmDetails($_GET['id']);
			$name = $farm[0]['name'];
			$address = $farm[0]['address'];
			if (isset($_GET['deleteSuccess'])) {
				echo '<div class="alert alert-success"><b>Success!</b> Removed user access to farm successfully</div>';
			}
			if (isset($_GET['newSuccess'])) {
				echo '<div class="alert alert-success"><b>Success!</b> Farm created successfully</div>';
			}
			?>
				<form action="" class="form-horizontal" method="post">
					<div class="span12">
						<div class="control-group">
							<label class="control-label" for="inputName">Name</label>
							<div class="controls">
								<input type="text" id="inputName" name="inputName" placeholder="Name" value="<?php echo $name; ?>">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputAddress">Address</label>
							<div class="controls">
								<input type="text" id="inputAddress" name="inputAddress" placeholder="Address" value="<?php echo $address; ?>">
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button type="submit" name="farmSubmit" class="btn">Save</button>
								<button class="btn btn-danger" type="button" id="deleteAccess" data-toggle="modal" data-target="#deleteModal">Delete farm</button>
							</div>
						</div>
						<div id="deleteModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
								<h3 id="myModalLabel">Are you sure???</h3>
							</div>
								<div class="modal-body">
								<p>Do you really want to delete this farm?</p>
								<p>This action deletes the farm, all related sheeps, all related updates and all related permissions</p>
							</div>
							<div class="modal-footer">
								<button type="submit" name="deleteFarm" class="btn btn-danger">Yes</button>
								<button class="btn" data-dismiss="modal" aria-hidden="true">No</button>
							</div>
						</div>					
					</div>
				</form>
			<?php
		}
			$users = $adminsys->getUserList();
			?>
			<div class="tabbable"> <!-- Only required for left/right tabs -->
				<ul class="nav nav-tabs">
					<li class="active"><a href="#accessTable" data-toggle="tab">Users overview</a></li>
					<li><a href="#newAccess" data-toggle="tab">Add new access</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="accessTable">
						<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered clickable" id="updatetable">
							<thead> 
								<tr> 
									<th>User ID</th> 
									<th>Level</th>
								</tr> 
							</thead> 
							<tbody>
								<tr>
									<td colspan="2" class="dataTables_empty">Loading data from server</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="tab-pane" id="newAccess">			
						<form action="?p=farms&id=<?php echo $_GET['id']; ?>" class="form-horizontal" method="post">
							<div class="span6">
								<div class="control-group">
									<label class="control-label" for="inputUserID">User ID</label>
									<div class="controls">
										<select id="inputUserID" name="inputUserID">
											<?php
												foreach ($users as $user) {
													echo '<option value="' . $user['id'] . '">' . $user['id'] . ' - ' . $user['name'] . '</option>';
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
						"sAjaxSource": "ajax.php?table=farmpermissions&id=<?php echo $_GET['id']; ?>",
						"bAutoWidth": true,
						"aaSorting": [[ 0, "asc" ]],
						"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
					} );
										
					$('#updatetable tbody tr').live('click',function(){
						var aData = pTable.fnGetData(this);
						//alert("Going to details for Sheep with ID:" + aData[0]);
						location.href='?p=farms&id=<?php echo $_GET['id']; ?>&userid='+aData[0];
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
					<a href="?p=farms">Overview</a>
				</li>
				<li <?php echo (isset($_GET['new'])) ? 'class="active"' : ''; ?>>
					<a href="?p=farms&new">Add new farm</a>
				</li>
			</ul>
		<?php
		if (isset($_GET['new'])) {
			if(isset($_POST['newFarmSubmit'])){
				$farmid = $adminsys->newFarm($_POST['inputName'], $_POST['inputAddress']);
				if ($farmid >= 1) {
					//SUCCESS
					echo '
					<script>
						window.location.href = \'?p=farms&id=' . $farmid . '&newSuccess\';
					</script>
					';
				}
				else {
					//FAILURE
					echo '<div class="alert alert-warning"><b>Warning!</b> Could not add new farm</div>';
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
							<label class="control-label" for="inputAddress">Address</label>
							<div class="controls">
								<input type="text" id="inputAddress" name="inputAddress" placeholder="Address" value="<?php echo $_POST['inputAddress']; ?>">
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button type="submit" name="newFarmSubmit" class="btn">Save</button>
							</div>
						</div>
					</div>
				</form>
			<?php
		}
		else {
			?>
				<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datatable">
					<thead> 
						<tr> 
							<th>ID</th> 
							<th>Name</th> 
							<th>Address</th>
						</tr> 
					</thead> 
					<tbody>
						<tr>
							<td colspan="3" class="dataTables_empty">Loading data from server</td>
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
							"sAjaxSource": "ajax.php?table=farm",
							"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
						} );
						
						$('#datatable tbody tr').live('click',function(){
							var aData = pTable.fnGetData(this);
							//alert("Going to details for Sheep with ID:" + aData[0]);
							location.href='?p=farms&id='+aData[0];
						});
					} );
				</script>
			<?php
		}
	}
?>