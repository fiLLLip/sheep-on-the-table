<?php

	extract($_POST);
	
	$phonenumber = '99364430';
	$message = 'Sheep #20069 is under attack! Last position: LOLOLOLOL';
	//set POST variables
	$url = 'http://www.vestnesconsulting.no/smsgateway/smssheep.php';
	$fields = array(
		'recipient' => urlencode($phonenumber),
		'message' => urlencode($message)
	);
	$fields_string = '';
	foreach ($fields as $key=>$value) { 
		$fields_string .= $key.'='.$value.'&';
	}
	rtrim($fields_string, '&');

	//open connection
	$ch = curl_init();

	//set the url, number of POST vars, POST data
	curl_setopt($ch,CURLOPT_URL, $url);
	curl_setopt($ch,CURLOPT_POST, count($fields));
	curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

	//execute post
	$result = curl_exec($ch);
	error_log('response from smsgateway: ' . $result);
	//close connection
	curl_close($ch);
	echo '<pre>';
	var_dump($result);
	echo '</pre>';	
?>