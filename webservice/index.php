<?php
require_once 'jsonRPCServer.php';
require_once 'database.php';
require 'sheep.php';
require 'restrictedSheep.php';

$mySheep = new RestrictedSheep();
	
jsonRPCServer::handle($mySheep)
	or print 'no request';
?>