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
	
	if (!isset($_SESSION['userid']) || !isset($_SESSION['hash']) || $adminsys->checkSession($_SESSION['hash'], $_SESSION['userid']) == null) {
		die();
	}
	
	$DB = new Database();
	
	switch ($_GET['table']) {
		case 'sheep':
			$aColumns = array( 'id', 'farm_id', 'name', 'born', 'deceased', 'comment', 'weight' );
			$sIndexColumn = "id";
			$sTable = "sheep_sheep";
			break;
		
		case 'farm':
			$aColumns = array( 'id', 'name', 'address' );
			$sIndexColumn = "id";
			$sTable = "sheep_farm";
			break;
		
		case 'user':
			$aColumns = array( 'id', 'un', 'name', 'email', 'phone', 'sysadmin' );
			$sIndexColumn = "id";
			$sTable = "sheep_user";
			break;
		
		case 'update':
			$aColumns = array( 'id', 'sheep_id', 'timestamp', 'pos_x', 'pos_y', 'pulse', 'temp', 'alarm' );
			$whereID = $_GET['id'];
			$sIndexColumn = "id";
			$sTable = "sheep_updates";
			break;
			
		default:
			die();
	}
	
	$DB->connect();
	
	function fatal_error ( $sErrorMessage = '' )
	{
		header( $_SERVER['SERVER_PROTOCOL'] .' 500 Internal Server Error' );
		die( $sErrorMessage );
	}
	
	/* 
	 * Paging
	 */
	$sLimit = "";
	if ( isset( $_GET['iDisplayStart'] ) && $_GET['iDisplayLength'] != '-1' )
	{
		$sLimit = "LIMIT ".$DB->escapeStrings( $_GET['iDisplayStart'] ).", ".
			$DB->escapeStrings($_GET['iDisplayLength']);
	}
	
	
	/*
	 * Ordering
	 */
	$sOrder = "";
	if ( isset( $_GET['iSortCol_0'] ) )
	{
		$sOrder = "ORDER BY  ";
		for ( $i=0 ; $i<intval( $_GET['iSortingCols'] ) ; $i++ )
		{
			if ( $_GET[ 'bSortable_'.intval($_GET['iSortCol_'.$i]) ] == "true" )
			{
				$sOrder .= "`".$aColumns[ intval( $_GET['iSortCol_'.$i] ) ]."` ".
				 	$DB->escapeStrings( $_GET['sSortDir_'.$i] ) .", ";
			}
		}
		
		$sOrder = substr_replace( $sOrder, "", -2 );
		if ( $sOrder == "ORDER BY" )
		{
			$sOrder = "";
		}
	}
	
	
	/* 
	 * Filtering
	 * NOTE this does not match the built-in DataTables filtering which does it
	 * word by word on any field. It's possible to do here, but concerned about efficiency
	 * on very large tables, and MySQL's regex functionality is very limited
	 */
	$sWhere = "";
	if ( (isset($_GET['sSearch']) && $_GET['sSearch'] != "") || isset($whereID))
	{
		if (isset($whereID) && isset($_GET['sSearch']) && $_GET['sSearch'] != "") {
			$sWhere = "WHERE sheep_id = '$whereID' AND (";
			for ( $i=0 ; $i<count($aColumns) ; $i++ )
			{
				$sWhere .= "`".$aColumns[$i]."` LIKE '%".$DB->escapeStrings( $_GET['sSearch'] )."%' OR ";
			}
			$sWhere = substr_replace( $sWhere, "", -3 );
			$sWhere .= ')';
		}
		elseif (isset($whereID)) {
			$sWhere = "WHERE sheep_id = '$whereID'";
		}
	}
	
	/* Individual column filtering */
	for ( $i=0 ; $i<count($aColumns) ; $i++ )
	{
		if ( isset($_GET['bSearchable_'.$i]) && $_GET['bSearchable_'.$i] == "true" && $_GET['sSearch_'.$i] != '' )
		{
			if ( $sWhere == "" )
			{
				$sWhere = "WHERE ";
			}
			else
			{
				$sWhere .= " AND ";
			}
			$sWhere .= "`".$aColumns[$i]."` LIKE '%".$DB->escapeStrings($_GET['sSearch_'.$i])."%' ";
		}
	}
	
	
	/*
	 * SQL queries
	 * Get data to display
	 */
	$sQuery = "
		SELECT `".str_replace(" , ", " ", implode("`, `", $aColumns))."`
		FROM   $sTable
		$sWhere
		";
	
	/* Data set length after filtering */
	$iFilteredTotal = $DB->getNumRows($sQuery);
	
	/* Total data set length */
	$totalQuery = "
		SELECT `".$sIndexColumn."`
		FROM   $sTable
	";
	$iTotal = $DB->getNumRows($totalQuery);
	
	$sQuery = "
		SELECT `".str_replace(" , ", " ", implode("`, `", $aColumns))."`
		FROM   $sTable
		$sWhere
		$sOrder
		$sLimit
		";
	/*
	 * Output
	 */
	$output = array(
		"sEcho" => intval($_GET['sEcho']),
		"iTotalRecords" => $iTotal,
		"iTotalDisplayRecords" => $iFilteredTotal,
		"aaData" => array()
	);
	
	foreach ($DB->getResults($sQuery) as $aRow)
	{
		$row = array();
		for ( $i=0 ; $i<count($aColumns) ; $i++ )
		{
			if ( $aColumns[$i] == "version" )
			{
				/* Special output formatting for 'version' column */
				$row[] = ($aRow[ $aColumns[$i] ]=="0") ? '-' : $aRow[ $aColumns[$i] ];
			}
			else if ( $aColumns[$i] != ' ' )
			{
				/* General output */
				$row[] = $aRow[ $aColumns[$i] ];
			}
		}
		$output['aaData'][] = $row;
	}
	
	$DB->disconnect();
	
	echo json_encode( $output );
?>