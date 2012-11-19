<?php
include("JsonRpcClient.php");

$listOfCalls = array();

array_push($listOfCalls,new RpcRequest("sheepLogon",array('33','77')));
array_push($listOfCalls,new RpcRequest("getNonAuthSheepList", array('3')));
//$client = new AuthenticatedJsonRpcClient('http://localhost/jsonrpc/sample/server/');
$client = new JsonRpcClient('http://dyn.filllip.net/sheepwebservice2/');
$user = $client->getNonAuthSheepList(3);
echo '<pre>';

//var_dump($client->simulateSheepUpdates(100000000));
print_r($user);
//$client = new JsonRpcClient('http://dyn.filllip.net/sheepwebservice2/');
//var_dump($user);
//var_dump($client->getSheepList($user[1][0], $user[1][1], '3'));
echo '</pre>';

?>