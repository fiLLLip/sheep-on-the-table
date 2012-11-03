<?php
/**
 * Parameter $hash and $userid should always be first wherever you are trying to authorize something 
 *
 * @author fiLLLip
 */
class AdminSys {

	public function checkSession ($hash, $userid) {
		if (!isset($hash)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$hash = $DB->escapeStrings($hash);
		$ip = $_SERVER['REMOTE_ADDR'];
		$result = $DB->getResults('SELECT id FROM sheep_user WHERE hash = \'' . $hash . '\' AND ip = \'' . $ip . '\' LIMIT 1');
		$DB->disconnect();
		$returnid = $result[0]['id'];
		if ($userid == $returnid) {
			return $userid;
		}
		else {
			return null;
		}
	}

	/**
	 * Method that says to client YES YOU ARE CONNECTED 
	 *
	 * @return
	 */
	public function logon ($user,$pass) {
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
		$result = $DB->getResults('SELECT id FROM sheep_user WHERE un = \'' . $user . '\' AND pw = \'' . $pass . '\' AND sysadmin = \'1\' LIMIT 1');
		$userid = $result[0]['id'];
		if ($userid >= 1) {
			$ip = $_SERVER['REMOTE_ADDR'];
			$time = time();
			$hash = sha1(ip2long($ip) * $time);
			$DB->setFields('UPDATE sheep_user SET ip=\'' . $ip . '\', hash=\'' . $hash . '\' WHERE id=\'' . $userid . '\'');
			$DB->disconnect();
			$_SESSION['hash'] = $hash;
			$_SESSION['userid'] = $userid;
			return array($hash, $userid);
		}
		else {
			$DB->disconnect();
			return null;
		}
	}

	/**
	 * Method that logs out 
	 *
	 * @return
	 */
	public function logout () {
		$_SESSION = array();
		session_destroy();
	}
	
	/**
	 * Method that gets all the users in the database and returns it as an array
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getUserList () {
		$DB = new Database();
		$DB->connect();
		$returnarr = $DB->getResults('SELECT id, un, name, email, phone, sysadmin FROM sheep_user');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that gets all the users in the database and returns it as an array
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getFarmList () {
		$DB = new Database();
		$DB->connect();
		$returnarr = $DB->getResults('SELECT id, name, address FROM sheep_farm');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that gets all the users in the database and returns it as an array
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getSheepList () {
		$DB = new Database();
		$DB->connect();
		$returnarr = $DB->getResults('SELECT id, farm_id, name, UNIX_TIMESTAMP(born) as born, UNIX_TIMESTAMP(deceased) as deceased, comment, weight FROM sheep_sheep LIMIT 1');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that gets all the users in the database and returns it as an array
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getSheepDetails ($sheepid) {
		$DB = new Database();
		$DB->connect();
		$sheepid = $DB->escapeStrings($sheepid);
		$returnarr = $DB->getResults('SELECT id, farm_id, name, DATE_FORMAT(born, \'%d-%m-%Y\') as born,
			DATE_FORMAT(deceased, \'%d-%m-%Y\') as deceased, comment, weight
			FROM sheep_sheep
			WHERE id=\'' . $sheepid . '\'
			');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that gets all the users in the database and returns it as an array
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getStats () {
		$DB = new Database();
		$DB->connect();
		$numusers = $DB->getNumRows('SELECT id FROM sheep_user');
		$numfarms = $DB->getNumRows('SELECT id FROM sheep_farm');
		$numsheeps = $DB->getNumRows('SELECT id FROM sheep_sheep');
		$numupdates = $DB->getNumRows('SELECT id FROM sheep_updates');
		$returnarr = array("users"=>$numusers, "farms"=>$numfarms, "sheeps"=>$numsheeps, "updates"=>$numupdates);
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
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
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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
			FROM sheep_updates WHERE sheep_id = \''. $sheepid .'\' ORDER BY id DESC LIMIT ' . $limit . '');
		$DB->disconnect();
		return array($returnarr);
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function editSheep ($sheepid, $name, $born, $deceased, $comment, $weight) {
		$DB = new Database();
		$DB->connect();
		$sheepid = $DB->escapeStrings($sheepid);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		if ($born < $deceased) {
			$deceased = 0;
		}
		$comment = $DB->escapeStrings($comment);
		$weight = $DB->escapeStrings($weight);
		$result = $DB->setFields('UPDATE sheep_sheep
			SET name=\'' . $name . '\',
			born=FROM_UNIXTIME(' . $born . '),
			deceased=FROM_UNIXTIME(' . $deceased . '),
			comment=\'' . $comment . '\',
			weight=\'' . $weight . '\'
			WHERE id=\'' . $sheepid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

	/**
	 * Method that receives a userid and farmid and returns permission level
	 *
	 * @return mixed
	 */	 
	public function getUserPermission ($hash, $userid, $farmid, $checkid) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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

	/**
	 * Method that receives permission level value for a user to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserPermission ($hash, $userid, $farmid, $setid, $level) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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

	/**
	 * Method that receives a farmid and returns all farms
	 *
	 * @return mixed
	 */	 
	public function getUsersForFarm ($hash, $userid, $farmid) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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
			$returnarr = $DB->getResults('SELECT p.user_id, p.level, u.un, u.name, u.email, u.phone, p.SMSAlarmAttack, p.SMSAlarmTemperature, p.SMSAlarmStationary, p.EmailAlarmAttack, p.EmailAlarmTemperature, 
				p.EmailAlarmStationary FROM sheep_permissions p, sheep_user u WHERE u.id=p.user_id AND p.farm_id = \''. $farmid .'\'');
			$DB->disconnect();
			return array($returnarr);
		}
		else {
			$DB->disconnect();
			return null;
		}
	}

	/**
	 * Method that receives a farmid and returns all farms
	 *
	 * @return mixed
	 */	 
	public function getUserDetails ($hash, $userid) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
		}
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$returnarr = $DB->getResults('SELECT id, un, name, email, phone FROM sheep_user WHERE id = \''. $userid .'\' LIMIT 1');
		$DB->disconnect();
		return $returnarr[0];
	}

	/**
	 * Method that receives all settings values of a user to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserSettings ($hash, $userid, $farmid, $setid, $smssattack, $smstemp, $smsstat, $emailattack, $emailtemp, $emailstat) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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

	/**
	 * Method that receives all detail values of logged in user to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserDetails ($hash, $userid, $name, $email, $phone) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
		}
		if (!isset($name)) {
			return null;
		}
		if (!isset($email)) {
			return null;
		}
		if (!isset($phone)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$name = $DB->escapeStrings($name);
		$email = $DB->escapeStrings($email);
		$phone = $DB->escapeStrings($phone);
		$numrows = $DB->getNumRows('SELECT id FROM sheep_user WHERE id = \'' . $userid . '\'');
		if ($numrows >= 1) {
			$result = $DB->setFields('UPDATE sheep_user
				SET name=\'' . $name . '\',
				email=\'' . $email . '\',
				phone=\'' . $phone . '\'
				WHERE id=\'' . $userid . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}

	/**
	 * Method that receives new password for logged in user to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserNewPassword ($hash, $userid, $oldpw, $newpw) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
		}
		if (!isset($oldpw)) {
			return null;
		}
		if (!isset($newpw)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$oldpw = $DB->escapeStrings($oldpw);
		$newpw = $DB->escapeStrings($newpw);
		$numrows = $DB->getNumRows('SELECT id FROM sheep_user WHERE id = \'' . $userid . '\' AND pw = \'' . $oldpw . '\'');
		if ($numrows >= 1) {
			$result = $DB->setFields('UPDATE sheep_user
				SET pw=\'' . $newpw . '\'
				WHERE id=\'' . $setid . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives var for a "new sheep" and stores it in 
	 * a database
	 *
	 * @return mixed
	 */	
	public function newSheep ($hash, $userid, $farmid, $name, $born, $deceased, $comment, $weight) {
		if (!isset($hash)) {
			return "nohash";
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
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
	
	/**
	 * Method that receives a username to add permission to a farm. Checks if userid that requests is owner.
	 *
	 * @return mixed
	 */	
	public function addUserToFarm ($hash, $userid, $farmid, $username) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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
	
	/**
	 * Method that receives a userid and deletes user. Checks if userid that requests is owner.
	 *
	 * @return mixed
	 */	
	public function removeUserFromFarm ($hash, $userid, $farmid, $delid) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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
	
	/**
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
		
		if (intval($pulse) > 200 || intval($pulse) < 40) {
			// TODO: Implement alarm
		}
		if (intval($temp) > 42 || intval($temp) < 30) {
			// TODO: Implement alarm
		}
		if ($alarm == '1') {
			// TODO: Implement alarm
		}
		return 1;
	}

	/**
	 * Method that deletes a sheep from the database given ID
	 *
	 * @return mixed
	 */	 
	public function removeSheep ($hash, $userid, $id) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userid)) {
			return null;
		}
		if ($this->checkSession($hash, $userid) == null) {
			return "sessionTimeout";
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
