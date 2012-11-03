<?php
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
?>

<!-- bootstrap widget theme --> 
<link rel="stylesheet" href="css/theme.bootstrap.css"> 
<!-- tablesorter plugin --> 
<script src="js/jquery.tablesorter.min.js"></script> 
<!-- tablesorter widget file - loaded after the plugin --> 
<script src="js/jquery.tablesorter.widgets.min.js"></script>
<script src="js/jquery.tablesorter.pager.min.js"></script>
<link href="css/theme.bootstrap.css" rel="stylesheet">


<p>Sheeps</p>
<table id="sheepTable" class="tablesorter"> 
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
<?php
	$sheeps = $adminsys->getSheepList();
	foreach ($sheeps as $sheep) {
		/*
		echo '<pre>';
		print_r($sheep);
		echo '</pre>';
		*/
		echo '<tr><td>';
		echo $sheep['id'];
		echo '</td><td>';
		echo $sheep['farm_id'];
		echo '</td><td>';
		echo $sheep['name'];
		echo '</td><td>';
		echo $sheep['born'];
		echo '</td><td>';
		echo $sheep['deceased'];
		echo '</td><td>';
		echo $sheep['comment'];
		echo '</td><td>';
		echo $sheep['weight'];
		echo '</td></tr>';
	}
?>
	</tbody> 
	<tfoot>
		<tr>
			<th colspan="7" class="pager form-horizontal tablesorter-pager" data-column="0" style="">
				<button class="btn first disabled"><i class="icon-step-backward"></i></button>
				<button class="btn prev disabled"><i class="icon-arrow-left"></i></button>
				<span class="pagedisplay">1 - 10 / 50 (50)</span> <!-- this can be any element, including an input -->
				<button class="btn next"><i class="icon-arrow-right"></i></button>
				<button class="btn last"><i class="icon-step-forward"></i></button>
				<select class="pagesize input-mini" title="Select page size">
					<option selected="selected" value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
				</select>
			</th>
		</tr>
	</tfoot>
</table>
<!-- pager --> 

<script>
$(function() { 
 
  $.extend($.tablesorter.themes.bootstrap, { 
    // these classes are added to the table. To see other table classes available, 
    // look here: http://twitter.github.com/bootstrap/base-css.html#tables 
    table    : 'table table-bordered table-striped', 
    header   : 'bootstrap-header', // give the header a gradient background 
    icons    : '', // add "icon-white" to make them white; this icon class is added to the <i> in the header 
    sortNone : 'bootstrap-icon-unsorted', 
    sortAsc  : 'icon-chevron-up', 
    sortDesc : 'icon-chevron-down', 
    active   : '', // applied when column is sorted 
    hover    : '', // use custom css here - bootstrap class may not override it 
    filterRow: '', // filter row class 
    even     : '', // odd row zebra striping 
    odd      : ''  // even row zebra striping 
  }); 
 
  // call the tablesorter plugin and apply the uitheme widget 
  $("#sheepTable").tablesorter({ 
    widthFixed: true, 
 
    // widget code contained in the jquery.tablesorter.widgets.js file 
    // use the zebra stripe widget if you plan on hiding any rows (filter widget) 
    widgets : [ "uitheme", "filter", "zebra" ], 
 
    widgetOptions : { 
      // using the default zebra striping class name, so it actually isn't included in the theme variable above 
      // this is ONLY needed for bootstrap theming if you are using the filter widget, because rows are hidden 
      zebra : ["even", "odd"], 
 
      // reset filters button 
      filter_reset : ".reset", 
 
      // set the uitheme widget to use the bootstrap theme class names 
      uitheme : "bootstrap" 
 
    } 
  }) 
  .tablesorterPager({

    // target the pager markup - see the HTML block below
    container: $(".pager"),

    // use this format: "http:/mydatabase.com?page={page}&size={size}"
    // where {page} is replaced by the page number and {size} is replaced
    // by the number of records to show
    ajaxUrl: null,

    // process ajax so that the data object is returned along with
    // the total number of rows
    ajaxProcessing: function(ajax) {
        if (ajax && ajax.hasOwnProperty('data')) {
            // example ajax:
            // {
            //   "data" : [{ "ID": 1, "Name": "Foo", "Last": "Bar" }],
            //   "total_rows" : 100
            // }
            // return [ "data", "total_rows" ];
            return [ajax.data, ajax.total_rows];
        }
    },

    // output string - default is '{page}/{totalPages}';
    // possible variables:
    // {page}, {totalPages}, {startRow}, {endRow} and {totalRows}
    output: '{startRow} to {endRow} ({totalRows})',

    // apply disabled classname to the pager arrows when the rows at
    // either extreme is visible - default is true
    updateArrows: true,

    // starting page of the pager (zero based index)
    page: 0,

    // Number of visible rows - default is 10
    size: 10,

    // if true, the table will remain the same height no matter
    // how many records are displayed. The space is made up by an empty
    // table row set to a height to compensate; default is false
    fixedHeight: false,

    // remove rows from the table to speed up the sort of large tables.
    // setting this to false, only hides the non-visible rows; needed
    // if you plan to add/remove rows with the pager enabled.
    removeRows: false,

    // css class names of pager arrows
    cssNext: '.next',
    // next page arrow
    cssPrev: '.prev',
    // previous page arrow
    cssFirst: '.first',
    // go to first page arrow
    cssLast: '.last',
    // go to last page arrow
    cssPageDisplay: '.pagedisplay',
    // location of the "output"
    cssPageSize: '.pagesize',
    // dropdown that sets the "size" option
    // class added to arrows when at the extremes 
    // (i.e. prev/first arrows are "disabled" when on the first page)
    // Note there is no period "." in front of this class name
    cssDisabled: 'disabled'

}); 
 
});
</script>