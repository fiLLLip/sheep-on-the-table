<?php
/**
 * Parameter $hash and $userid should always be first wherever you are trying to authorize something 
 *
 * @author fiLLLip
 */
class Sheep extends JsonRpcService{

	private function checkSession ($hash) {
		if (!isset($hash)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$hash = $DB->escapeStrings($hash);
		$ip = $_SERVER['REMOTE_ADDR'];
		$result = $DB->getResults('SELECT id FROM sheep_user WHERE hash = \'' . $hash . '\' AND ip = \'' . $ip . '\' LIMIT 1');
		$DB->disconnect();
		$userid = $result[0]['id'];
		if ($userid >= 1) {
			return $userid;
		}
		else {
			return null;
		}
	}

	/** @JsonRpcMethod
	 * Method that says to client YES YOU ARE CONNECTED 
	 *
	 * @return
	 */
	public function sheepLogon ($user,$pass) {
		if (!isset($user)) {
			return null;
		} 
		if (!isset($pass)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$user = $DB->escapeStrings($user);
		$pass = $DB->escapeStrings($pass);
		$result = $DB->getResults('SELECT id, farm_id FROM sheep_user WHERE un = \'' . $user . '\' AND pw = \'' . $pass . '\' LIMIT 1');
		$userid = $result[0]['id'];
		if ($userid >= 1) {
			$ip = $_SERVER['REMOTE_ADDR'];
			$time = time();
			$hash = sha1(ip2long($ip) * $time);
			$DB->setFields('UPDATE sheep_user SET ip=\'' . $ip . '\', hash=\'' . $hash . '\' WHERE id=\'' . $userid . '\'');
			$DB->disconnect();
			$farmid = $result[0]['farm_id'];
			return array($hash, $userid, $farmid);
		}
		else {
			$DB->disconnect();
			return null;
		}
	}
	
	/** @JsonRpcMethod
	 * Method that serves the client all the sheeps that belong 
	 * to his farm
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getSheepList ($hash, $userid, $farmid) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if (!isset($farmid)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$numrows = $DB->getNumRows('SELECT un FROM sheep_user WHERE id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\'');
		if ($numrows >= 1) {
			$returnarr = $DB->getResults('SELECT id, farm_id, name, UNIX_TIMESTAMP(born) as born, UNIX_TIMESTAMP(deceased) as deceased, comment, weight FROM sheep_sheep WHERE farm_id = \''. $farmid .'\'');
			$DB->disconnect();
			return array($returnarr);
		}
		else {
			$DB->disconnect();
			return $farmid;
		}
	}
}
?>
