<?php

include("JsonRpcClient.php");

$listOfCalls = array();

array_push($listOfCalls,new RpcRequest("newSheepUpdate",array('33','7.7', '7.7', '80', '37', '0')));
//$client = new AuthenticatedJsonRpcClient('http://localhost/jsonrpc/sample/server/');
$client = new JsonRpcClient('http://dyn.filllip.net/sheepwebservice/');

echo '<pre>';
$sheepID = 4;
			$posX = rand(10600000, 11120000) / 1000000;
			//$posX = rand(4510000, 4510090) / 1000000;
			$posY = rand(62540000, 62800000) / 1000000;
			//$posy = rand(58000000, 58000090) / 1000000;
			$pulse = rand(50, 90);
			$temp = rand(35, 40);
			$alarm = 1;
var_dump($client->newSheepUpdate($sheepID, $posX, $posY, $pulse, $temp, $alarm));
echo '</pre>';

?>