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
		$result = $DB->getResults('SELECT id FROM sheep_user WHERE un = \'' . $user . '\' AND pw = \'' . $pass . '\' LIMIT 1');
		$userid = $result[0]['id'];
		if ($userid >= 1) {
			$ip = $_SERVER['REMOTE_ADDR'];
			$time = time();
			$hash = sha1(ip2long($ip) * $time);
			$DB->setFields('UPDATE sheep_user SET ip=\'' . $ip . '\', hash=\'' . $hash . '\' WHERE id=\'' . $userid . '\'');
			$farms = $DB->getResults('SELECT p.farm_id as id, f.name as name, f.address as address FROM sheep_permissions p, sheep_farm f WHERE p.user_id = \'' . $userid . '\' AND p.farm_id=f.id');
			$DB->disconnect();
			return array($hash, $userid, $farms);
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
		$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\'');
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
	 * Method that receives a userid and farmid and returns permission level
	 *
	 * @return mixed
	 */	 
	public function getUserPermission ($hash, $userid, $farmid, $checkid) {
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
		if (!isset($checkid)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$checkid = $DB->escapeStrings($checkid);
		$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\'');
		if ($numrows >= 1) {
			$returnarr = $DB->getResults('SELECT level FROM sheep_permissions WHERE user_id = \'' . $checkid . '\' AND farm_id = \''. $farmid .'\' LIMIT 1');
			$value = $returnarr[0]['level'];
			$DB->disconnect();
			return $value;
		}
		else {
			$DB->disconnect();
			return null;
		}
	}

	/** @JsonRpcMethod
	 * Method that receives permission level value for a user to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserPermission ($hash, $userid, $farmid, $setid, $level) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($farmid)) {
			return null;
		}
		if (!isset($setid)) {
			return null;
		}
		if (!isset($level)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$setid = $DB->escapeStrings($setid);
		$level = $DB->escapeStrings($level);
		$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\' AND level=\'2\'');
		if ($numrows >= 1) {
			$result = $DB->setFields('UPDATE sheep_permissions
				SET level=\'' . $level . '\'
				WHERE user_id=\'' . $setid . '\'
				AND farm_id=\'' . $farmid . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}

	/** @JsonRpcMethod
	 * Method that receives a farmid and returns all farms
	 *
	 * @return mixed
	 */	 
	public function getUsersForFarm ($hash, $userid, $farmid) {
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
		$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\'');
		if ($numrows >= 1) {
			$returnarr = $DB->getResults('SELECT p.user_id, s.level, u.un, u.name, u.phone FROM sheep_permissions p, sheep_user u WHERE u.id=p.user_id AND p.farm_id = \''. $farmid .'\'');
			$DB->disconnect();
			return $returnarr;
		}
		else {
			$DB->disconnect();
			return null;
		}
	}

	/** @JsonRpcMethod
	 * Method that receives a userid and farmid and returns all settings for user
	 *
	 * @return mixed
	 */	 
	public function getUserSettings ($hash, $userid, $farmid, $checkid) {
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
		if (!isset($checkid)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$checkid = $DB->escapeStrings($checkid);
		if ($userid == $checkid) {
			$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\'');
		}
		else {
			$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\' AND level=\'2\'');
		}
		if ($numrows >= 1) {
			$returnarr = $DB->getResults('SELECT SMSAlarmAttack, SMSAlarmTemperature, SMSAlarmStationary, EmailAlarmAttack, EmailAlarmTemperature, 
				EmailAlarmStationary FROM sheep_permissions WHERE user_id = \''. $checkid .'\' AND farm_id = \''. $farmid .'\'');
			$DB->disconnect();
			return $returnarr;
		}
		else {
			$DB->disconnect();
			return null;
		}
	}

	/** @JsonRpcMethod
	 * Method that receives all settings values of a user to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserSettings ($hash, $userid, $farmid, $setid, $smssattack, $smstemp, $smsstat, $emailattack, $emailtemp, $emailstat) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($farmid)) {
			return null;
		}
		if (!isset($setid)) {
			return null;
		}
		if (!isset($smssattack)) {
			return null;
		}
		if (!isset($smstemp)) {
			return null;
		}
		if (!isset($smsstat)) {
			return null;
		}
		if (!isset($emailattack)) {
			return null;
		}
		if (!isset($emailtemp)) {
			return null;
		}
		if (!isset($emailstat)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$setid = $DB->escapeStrings($setid);
		$smssattack = $DB->escapeStrings($smssattack);
		$smstemp = $DB->escapeStrings($smstemp);
		$smsstat = $DB->escapeStrings($smsstat);
		$emailattack = $DB->escapeStrings($emailattack);
		$emailtemp = $DB->escapeStrings($emailtemp);
		$emailstat = $DB->escapeStrings($emailstat);
		if ($userid == $setid) {
			$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\'');
		}
		else {
			$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\' AND level=\'2\'');
		}
		if ($numrows >= 1) {
			$result = $DB->setFields('UPDATE sheep_permissions
				SET SMSAlarmAttacl=\'' . $smssattack . '\',
				SMSAlarmTemperature=\'' . $smstemp . '\',
				SMSAlarmStationary=\'' . $smsstat . '\',
				EmailAlarmAttack=\'' . $emailattack . '\',
				EmailAlarmTemperature=\'' . $emailtemp . '\',
				EmailAlarmStationary=\'' . $emailstat . '\'
				WHERE user_id=\'' . $setid . '\'
				AND farm_id=\'' . $farmid . '\'');
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
	 * Method that receives a username to add permission to a farm. Checks if userid that requests is owner.
	 *
	 * @return mixed
	 */	
	public function addUserToFarm ($hash, $userid, $farmid, $username) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($farmid)) {
			return null;
		}
		if (!isset($username)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$username = $DB->escapeStrings($username);
		$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\' AND level=\'2\'');
		if ($numrows >= 1) {
			$numrows = $DB->getNumRows('SELECT id FROM sheep_user WHERE un = \'' . $username . '\'');
			if ($numrows >= 1) {
				$userarr = $DB->getResults('SELECT id FROM sheep_user WHERE un = \''. $username .'\' LIMIT 1');
				$result = $DB->setFields('INSERT INTO sheep_permissions
					(user_id, farm_id)
					VALUES (\'' . $userarr[0]['id'] . '\', \'' . $farmid . '\')
				');
				$return = $result;
			}
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}
	
	/** @JsonRpcMethod
	 * Method that receives a userid and deletes user. Checks if userid that requests is owner.
	 *
	 * @return mixed
	 */	
	public function removeUserFromFarm ($hash, $userid, $farmid, $delid) {
		if (!isset($hash)) {
			return null;
		}
		if ($this->checkSession($hash) == null) {
			return null;
		}
		if (!isset($farmid)) {
			return null;
		}
		if (!isset($delid)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$delid = $DB->escapeStrings($delid);
		$numrows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userid . '\' AND farm_id = \'' . $farmid . '\' AND level=\'2\'');
		if ($numrows >= 1) {
			$result = $DB->setFields('DELETE FROM sheep_permissions
				WHERE user_id=\'' . $delid . '\'
				AND farm_id=\'' . $farm_id . '\'
			');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}
	
	/** @JsonRpcMethod
	 * Method that receives var for a "new update" and stores it in 
	 * a database
	 *
	 * @return mixed
	 */	
	public function updateSheep ($sheepid, $posx, $posy, $pulse, $temp, $alarm) {
		if (!isset($sheepid)) {
			return null;
		}
		if (!isset($posx)) {
			return null;
		}
		if (!isset($posy)) {
			return null;
		}
		if (!isset($pulse)) {
			return null;
		}
		if (!isset($temp)) {
			return null;
		}
		if (!isset($alarm)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$sheepid = $DB->escapeStrings($sheepid);
		$posx = $DB->escapeStrings($posx);
		$posy = $DB->escapeStrings($posy);
		$pulse = $DB->escapeStrings($pulse);
		$temp = $DB->escapeStrings($temp);
		$alarm = $DB->escapeStrings($alarm);
		$result = $DB->setFields('
			INSERT INTO sheep_updates
			(sheep_id, timestamp, pos_x, pos_y, pulse, temp, alarm)
			VALUES (\'' . $alarm . '\', FROM_UNIXTIMESTAMP(' . time() . '), \'' . $posx . '\', 
			\'' . $posy . '\', \'' . $pulse . '\', \'' . $temp . '\', \'' . $alarm . '\')
		');
		$return = $result;
		$DB->disconnect();
		if ($alarm == '1') {
			// TODO: Implement here
		}
		return 1;
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
