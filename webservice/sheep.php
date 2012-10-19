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
			$returnarr = $DB->getResults('SELECT id, farm_id, name, UNIX_TIMESTAMP(born) as born, UNIX_TIMESTAMP(deceased) as deceased, comment, weight 
				FROM sheep_sheep WHERE farm_id = \''. $farmid .'\'');
			$DB->disconnect();
			return array($returnarr);
		}
		else {
			$DB->disconnect();
			return null;
		}
	}
	
	/** @JsonRpcMethod
	 * Method that serves the client all the n updates that belong 
	 * to his sheep
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getSheepUpdates ($hash, $userid, $sheepid, $limit) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if (!isset($sheepid)) {
			return null;
		}
		if (!isset($limit)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$sheepid = $DB->escapeStrings($sheepid);
		$limit = $DB->escapeStrings($limit);
		$returnarr = $DB->getResults('SELECT id, UNIX_TIMESTAMP(timestamp) as timestamp, pos_x, pos_y, pulse, temp, alarm
			FROM sheep_updates WHERE sheep_id = \''. $sheepid .'\' LIMIT ' . $limit . '');
		$DB->disconnect();
		return array($returnarr);
	}

	/** @JsonRpcMethod
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function editSheep ($hash, $userid, $id, $name, $born, $deceased, $comment, $weight) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($id)) {
			return null;
		}
		if (!isset($name)) {
			return null;
		}
		if (!isset($born)) {
			return null;
		}
		if (!isset($deceased)) {
			return null;
		}
		if (!isset($comment)) {
			return null;
		}
		if (!isset($weight)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$id = $DB->escapeStrings($id);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		$comment = $DB->escapeStrings($comment);
		$weight = $DB->escapeStrings($weight);
		$numrows = $DB->getNumRows('SELECT un FROM sheep_user u, sheep_sheep s WHERE u.id=\'' . $userid . '\' AND u.farm_id=s.farm_id AND s.id=\'' . $id . '\'');
		if ($numrows >= 1) {
			$result = $DB->setFields('UPDATE sheep_sheep
				SET name=\'' . $name . '\',
				born=FROM_UNIXTIME(' . $born . '),
				deceased=FROM_UNIXTIME(' . $deceased . '),
				comment=\'' . $comment . '\',
				weight=\'' . $weight . '\'
				WHERE id=\'' . $id . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}
	
	/** @JsonRpcMethod
	 * Method that receives var for a "new sheep" and stores it in 
	 * a database
	 *
	 * @return mixed
	 */	
	public function newSheep ($hash, $userid, $farmid, $name, $born, $deceased, $comment, $weight) {
		if (!isset($hash)) {
			return "nohash";
		}
		if ($this->checkSession($hash) == null) {
			return "hashwat?";
		}
		if (!isset($farmid)) {
			return null;
		}
		if (!isset($name)) {
			return null;
		}
		if (!isset($born)) {
			return null;
		}
		if (!isset($deceased)) {
			return null;
		}
		if (!isset($comment)) {
			return null;
		}
		if (!isset($weight)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		$comment = $DB->escapeStrings($comment);
		$weight = $DB->escapeStrings($weight);
		$result = $DB->setFields('INSERT INTO sheep_sheep
			(farm_id, name, born, deceased, comment, weight)
			VALUES (\'' . $farmid . '\', \'' . $name . '\', FROM_UNIXTIME(' . $born . '),
			FROM_UNIXTIME(' . $deceased . '), \'' . $comment . '\', ' . $weight . ')
		');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

	/** @JsonRpcMethod
	 * Method that deletes a sheep from the database given ID
	 *
	 * @return mixed
	 */	 
	public function removeSheep ($hash, $userid, $id) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($id)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$id = $DB->escapeStrings($id);
		$numrows = $DB->getNumRows('SELECT un FROM sheep_user u, sheep_sheep s WHERE u.id=\'' . $userid . '\' AND u.farm_id=s.farm_id AND s.id=\'' . $id . '\'');
		if ($numrows >= 1) {
			$result = $DB->setFields('DELETE FROM sheep_sheep WHERE id=\'' . $id . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}
}
?>
