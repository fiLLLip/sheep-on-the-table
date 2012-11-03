<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
	
	if (isset($_GET['id'])) {
		
		if (isset($_POST['submit'])) {

			//$_POST['inputBorn'] = strtotime($_POST['inputBorn']);
			$arr = preg_split('/-/', $_POST['inputBorn']);
			$_POST['inputBorn'] = mktime(0, 0, 0, $arr[1], $arr[0], $arr[2]);
			$arr = preg_split('/-/', $_POST['inputDeceased']);
			$_POST['inputDeceased'] = mktime(0, 0, 0, $arr[1], $arr[0], $arr[2]);
			
			/*echo '<pre>';
			print_r($_POST);
			echo '</pre>';*/
			
			if ($adminsys->editSheep($_GET['id'], $_POST['inputName'], $_POST['inputBorn'], $_POST['inputDeceased'], $_POST['inputComment'], $_POST['inputWeight']) >= 1) {
				echo '<div class="alert alert-success"><b>Success!</b> Sheep updated successfully</div>';
			}
			else {
				echo '<div class="alert alert-warning"><b>Warning!</b> Sheep not affected. Maybe no changes?</div>';
			}
		}
		
		$sheep = $adminsys->getSheepDetails($_GET['id']);
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
	<div class="span6">
		<form action="" class="form-horizontal" method="post">
			<div class="control-group">
				<label class="control-label" for="inputName">Name</label>
				<div class="controls">
					<input type="text" id="inputName" name="inputName" placeholder="Name" value="<?php echo $name; ?>">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputFarmID">Farm ID</label>
				<div class="controls">
					<input type="text" id="inputFarmID" name="inputFarmID" placeholder="Farm ID" value="<?php echo $farmid; ?>">
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
			<div class="control-group">
				<label class="control-label" for="inputComment">Comment</label>
				<div class="controls">
					<input type="text" id="inputComment" name="inputComment" placeholder="Comment" value="<?php echo $comment; ?>">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputWeight">Weight</label>
				<div class="controls">
					<input type="text" id="inputWeight" name="inputWeight" placeholder="Weight" value="<?php echo $weight; ?>">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" name="submit" class="btn">Save</button>
				</div>
			</div>
		</form>
	</div>
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
		
	} );
</script>
<?php
	}
?>