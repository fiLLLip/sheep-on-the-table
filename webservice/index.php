<?php
if($_SERVER['REQUEST_METHOD'] == 'POST') {
	require_once("JsonRpcServer.php");
	require_once("database.php");
	require_once("sheep.php");
    session_start();
	
	$server = new JsonRpcServer(file_get_contents("php://input"));

	$server->addService(new Sheep());
	$server->processingRequests();
}
?>