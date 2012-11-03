<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
?>

<p>Farms</p>
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
	
		$('#datatable').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"sAjaxSource": "ajax.php?table=farm",
			"sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
		} );
	} );
</script>