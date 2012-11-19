<?php
include("JsonRpcClient.php");

$listOfCalls = array();

array_push($listOfCalls,new RpcRequest("newSheepUpdate",array('33','7.7', '7.7', '80', '37', '0')));
//$client = new AuthenticatedJsonRpcClient('http://localhost/jsonrpc/sample/server/');
$client = new JsonRpcClient('http://dyn.filllip.net/sheepwebservice2/');

echo '<pre>';
var_dump($client->simulateSheepUpdates(100000000));
echo '</pre>';

?>