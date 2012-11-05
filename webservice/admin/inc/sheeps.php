<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
	
	if (isset($_GET['id'])) {
		
		if (isset($_POST['submit'])) {
		
			if ($adminsys->editSheep($_GET['id'], $_POST['inputFarmID'], $_POST['inputName'], $_POST['inputBorn'], $_POST['inputDeceased'], $_POST['inputComment'], $_POST['inputWeight']) >= 1) {
				echo '<div class="alert alert-success"><b>Success!</b> Sheep updated successfully</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Sheep not affected. Maybe no changes?</div>';
			}
			
		}
		elseif (isset($_POST['deleteSheep'])) {
			if ($adminsys->deleteSheep($_GET['id']) >= 1) {
				echo '
				<script>
					window.location.href = \'?p=sheeps&deleteSuccess\';
				</script>
				';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Sheep not affected. Maybe sheep doesn\'t exist?</div>';
			}
		}
		
		$sheep = $adminsys->getSheepDetails($_GET['id']);
		$farms = $adminsys->getFarmlist();
		$farmid = $sheep[0]['farm_id'];
		$name = $sheep[0]['name'];
		$born = $sheep[0]['born'];
		$deceased = $sheep[0]['deceased'];
		$comment = $sheep[0]['comment'];
		$weight = $sheep[0]['weight'];
?>
	<div class="span12">
		<h2>
			Details of #<?php echo $_GET['id'] . ' - ' . $name; ?>
		</h2>
	</div>
	<form action="" class="form-horizontal" method="post">
		<div class="span6">
			<div class="control-group">
				<label class="control-label" for="inputName">Name</label>
				<div class="controls">
					<input type="text" id="inputName" name="inputName" placeholder="Name" value="<?php echo $name; ?>">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputFarmID">Farm ID</label>
				<div class="controls">
					<select id="inputFarmID" name="inputFarmID">
						<?php
							foreach ($farms as $farm) {
								echo '<option value="' . $farm['id'] . '"';
								if ($farm['id'] == $farmid) {
									echo ' selected="selected"';
								}
								echo '>' . $farm['id'] . ' - ' . $farm['name'] . '</option>';
							}
						?>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputBorn">Born</label>
				<div class="controls">
					<div class="input-append date" id="inputBorn" data-date="<?php echo $born; ?>" data-date-format="dd-mm-yyyy">
						<input class="span12" type="text" name="inputBorn" value="<?php echo $born; ?>" readonly="">
						<span class="add-on"><i class="icon-calendar"></i></span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputDeceased">Deceased</label>
				<div class="controls">
					<div class="input-append date" id="inputDeceased" data-date="<?php echo $deceased; ?>" data-date-format="dd-mm-yyyy">
						<input class="span12" type="text" name="inputDeceased" value="<?php echo $deceased; ?>" readonly="">
						<span class="add-on"><i class="icon-calendar"></i></span>
					</div>
				</div>
			</div>
		</div>
		<div class="span6">
			<div class="control-group">
				<label class="control-label" for="inputWeight">Weight</label>
				<div class="controls">
					<input type="text" id="inputWeight" name="inputWeight" placeholder="Weight" value="<?php echo $weight; ?>">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputComment">Comment</label>
				<div class="controls">
					<textarea id="inputComment" name="inputComment" placeholder="Comment" rows="3"><?php echo $comment; ?></textarea>
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" name="submit" class="btn">Save</button>
					<button class="btn btn-danger" type="button" id="deleteSheep" data-toggle="modal" data-target="#deleteModal">Delete sheep</button>
				</div>
			</div>
		</div>
		<div id="deleteModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="myModalLabel">Are you sure???</h3>
			</div>
				<div class="modal-body">
				<p>Do you really want to delete this sheep?</p>
			</div>
			<div class="modal-footer">
				<button type="submit" name="deleteSheep" class="btn btn-danger">Yes</button>
				<button class="btn" data-dismiss="modal" aria-hidden="true">No</button>
			</div>
		</div>
	</form>
	<div class="span12">
		<h3>
			Updates
		</h3>
	</div>
	<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="updatetable">
		<thead> 
			<tr> 
				<th>ID</th> 
				<th>Sheep ID</th> 
				<th>Timestamp</th> 
				<th>Longitude</th> 
				<th>Latitude</th> 
				<th>Pulse</th> 
				<th>Temp</th> 
				<th>Alarm</th> 
			</tr> 
		</thead> 
		<tbody>
			<tr>
				<td colspan="8" class="dataTables_empty">Loading data from server</td>
			</tr>
		</tbody>
	</table>
	<script>
		$(document).ready(function() {
			$.extend( $.fn.dataTableExt.oStdClasses, {
				"sWrapper": "dataTables_wrapper form-inline"
			} );
		
			var pTable = $('#updatetable').dataTable( {
				"bProcessing": true,
				"bServerSide": true,
				"sAjaxSource": "ajax.php?table=update&id=<?php echo $_GET['id']; ?>",
				"bAutoWidth": true,
				"aaSorting": [[ 0, "desc" ]],
				"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
			} );
			
			pTable.fnSetColumnVis(1, false);
			
			$('#inputBorn, #inputDeceased').datepicker()
			
			$('.alert').delay(5000).hide('slow');
		} );
	</script>
<?php
	}
	else {
		?>
			<ul class="nav nav-pills">
				<li <?php echo (!isset($_GET['new'])) ? 'class="active"' : ''; ?>>
					<a href="?p=sheeps">Overview</a>
				</li>
				<li <?php echo (isset($_GET['new'])) ? 'class="active"' : ''; ?>>
					<a href="?p=sheeps&new">Add new sheep</a>
				</li>
			</ul>
		<?php
		if (isset($_GET['new'])) {
			if(isset($_POST['newSheepSubmit'])){
				echo '<pre>';
				print_r($_POST);
				echo '</pre>';
				$sheepid = $adminsys->newSheep($_POST['inputFarmID'], $_POST['inputName'], 
					$_POST['inputBorn'], $_POST['inputDeceased'], 
					$_POST['inputWeight'], $_POST['inputComment']);
				if ($sheepid >= 1) {
					//SUCCESS
					echo '
					<script>
						window.location.href = \'?p=sheeps&id=' . $sheepid . '&newSuccess\';
					</script>
					';
				}
				else {
					//FAILURE
					echo '<div class="alert alert-warning"><b>Warning!</b> Could not add new sheep</div>';
				}
			}
			$farms = $adminsys->getFarmlist();
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
						<label class="control-label" for="inputFarmID">Farm ID</label>
						<div class="controls">
							<select id="inputFarmID" name="inputFarmID">
								<?php
									foreach ($farms as $farm) {
										echo '<option value="' . $farm['id'] . '"';
										if ($_POST['inputFarmID'] == $farm['id']) {
											echo ' selected="selected"';
										}
										echo '>' . $farm['id'] . ' - ' . $farm['name'] . '</option>';
									}
								?>
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputBorn">Born</label>
						<div class="controls">
							<div class="input-append date" id="inputBorn" data-date="02-01-1970" data-date-format="dd-mm-yyyy">
								<input class="span12" type="text" name="inputBorn" value="<?php echo (!isset($_POST['inputBorn']) ? '02-01-1970' : $_POST['inputBorn']); ?>" readonly="">
								<span class="add-on"><i class="icon-calendar"></i></span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputDeceased">Deceased</label>
						<div class="controls">
							<div class="input-append date" id="inputDeceased" data-date="02-01-1970" data-date-format="dd-mm-yyyy">
								<input class="span12" type="text" name="inputDeceased" value="<?php echo (!isset($_POST['inputDeceased']) ? '02-01-1970' : $_POST['inputDeceased']); ?>" readonly="">
								<span class="add-on"><i class="icon-calendar"></i></span>
							</div>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="inputWeight">Weight</label>
						<div class="controls">
							<input type="text" id="inputWeight" name="inputWeight" placeholder="Weight" value="<?php echo $_POST['inputWeight']; ?>">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputComment">Comment</label>
						<div class="controls">
							<textarea id="inputComment" name="inputComment" placeholder="Comment" rows="3"><?php echo $_POST['inputComment']; ?></textarea>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button type="submit" name="newSheepSubmit" class="btn">Save</button>
						</div>
					</div>
				</div>
			</form>
			<script>
				$(document).ready(function() {
					$('#inputBorn').datepicker();
				});
			</script>
			<?php
		}
		else {
			if (isset($_GET['deleteSuccess'])) {
				echo '<div class="alert alert-success"><b>Success!</b> Sheep deleted successfully</div>';
			}
			?>
				<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datatable">
					<thead> 
						<tr> 
							<th>ID</th> 
							<th>Farm ID</th> 
							<th>Name</th> 
							<th>Born</th> 
							<th>Deceased</th> 
							<th>Comment</th> 
							<th>Weight</th> 
						</tr> 
					</thead> 
					<tbody>
						<tr>
							<td colspan="7" class="dataTables_empty">Loading data from server</td>
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
							"sAjaxSource": "ajax.php?table=sheep",
							"bAutoWidth": true,
							"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
						} );
						
						$('#datatable tbody tr').live('click',function(){
							var aData = pTable.fnGetData(this);
							//alert("Going to details for Sheep with ID:" + aData[0]);
							location.href='?p=sheeps&id='+aData[0];
						});
						
						$('.alert').delay(5000).hide('slow');
						
					} );
				</script>
			<?php
		}
	}
?>