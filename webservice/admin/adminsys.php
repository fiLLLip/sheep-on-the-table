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
		$returnarr = $DB->getResults('SELECT id, un, name, email, phone, sysadmin FROM sheep_user ORDER BY id ASC');
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
	
	private function formatDate ($date) {
		$arr = preg_split('/-/', $date);
		if ($arr[0] == 0 && $arr[1] == 0 && $arr[2] == 0) {
			$date = 86400;
		}
		else {
			$date = mktime(0, 0, 0, $arr[1], $arr[0], $arr[2]);
			if ($date < 86400) {
				$date = 86400;
			}
		}
		return $date;
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function editSheep ($sheepid, $farmid, $name, $born, $deceased, $comment, $weight) {
		$DB = new Database();
		$DB->connect();
		$born = $this->formatDate($born);
		$deceased = $this->formatDate($deceased);
		$sheepid = $DB->escapeStrings($sheepid);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		if ($born < $deceased) {
			$deceased = 86400;
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
	 * Method that receives a farmid and returns all farms
	 *
	 * @return mixed
	 */	 
	public function getUserDetails ($userid) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$returnarr = $DB->getResults('SELECT id, un, name, email, phone, ip, sysadmin FROM sheep_user WHERE id = \''. $userid .'\' LIMIT 1');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that receives a farmid and returns all farms
	 *
	 * @return mixed
	 */	 
	public function getUserFarmDetails ($userid, $farmid) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$returnarr = $DB->getResults('SELECT level, 
			SMSAlarmAttack, EmailAlarmStationary, 
			SMSAlarmStationary, SMSAlarmTemperature, 
			EmailAlarmAttack, EmailAlarmTemperature 
			FROM sheep_permissions 
			WHERE user_id = \''. $userid .'\' 
			AND farm_id = \''. $farmid .'\'  
			LIMIT 1');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that receives a farmid and returns all farms
	 *
	 * @return mixed
	 */	 
	public function getFarmDetails ($farmid) {
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$returnarr = $DB->getResults('SELECT name, address FROM sheep_farm WHERE id = \''. $farmid .'\'  LIMIT 1');
		$DB->disconnect();
		return $returnarr;
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setUserFarmDetails ($userid, $farmid, $level, $SMSAlarmAttack, $SMSAlarmStationary, $SMSAlarmTemperature, $EmailAlarmAttack, $EmailAlarmStationary, $EmailAlarmTemperature) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$level = $DB->escapeStrings($level);
		$SMSAlarmAttack = $DB->escapeStrings($SMSAlarmAttack);
		$SMSAlarmStationary = $DB->escapeStrings($SMSAlarmStationary);
		$SMSAlarmTemperature = $DB->escapeStrings($SMSAlarmTemperature);
		$EmailAlarmAttack = $DB->escapeStrings($EmailAlarmAttack);
		$EmailAlarmStationary = $DB->escapeStrings($EmailAlarmStationary);
		$EmailAlarmTemperature = $DB->escapeStrings($EmailAlarmTemperature);
		
		$result = $DB->setFields('UPDATE sheep_permissions
			SET level=\'' . $level . '\',
			SMSAlarmAttack=\'' . $SMSAlarmAttack . '\',
			SMSAlarmStationary=\'' . $SMSAlarmStationary . '\',
			SMSAlarmTemperature=\'' . $SMSAlarmTemperature . '\',
			EmailAlarmAttack=\'' . $EmailAlarmAttack . '\',
			EmailAlarmStationary=\'' . $EmailAlarmStationary . '\',
			EmailAlarmTemperature=\'' . $EmailAlarmTemperature . '\'
			WHERE user_id=\'' . $userid . '\'
			AND farm_id=\'' . $farmid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function setFarmUserDetails ($userid, $farmid, $level) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$level = $DB->escapeStrings($level);
		
		$result = $DB->setFields('UPDATE sheep_permissions
			SET level=\'' . $level . '\'
			WHERE user_id=\'' . $userid . '\'
			AND farm_id=\'' . $farmid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function editUser ($userid, $name, $username, $email, $phone, $password, $sysadmin) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$name = $DB->escapeStrings($name);
		$username = $DB->escapeStrings($username);
		$email = $DB->escapeStrings($email);
		$phone = $DB->escapeStrings($phone);
		$password = $DB->escapeStrings($password);
		$sysadmin = $DB->escapeStrings($sysadmin);
		if (!empty($password)) {
			$result = $DB->setFields('UPDATE sheep_user
				SET name=\'' . $name . '\',
				un=\'' . $username . '\',
				pw=\'' . $password . '\',
				email=\'' . $email . '\',
				phone=\'' . $phone . '\',
				sysadmin=\'' . $sysadmin . '\'
				WHERE id=\'' . $userid . '\'');
		}
		else {
			$result = $DB->setFields('UPDATE sheep_user
				SET name=\'' . $name . '\',
				un=\'' . $username . '\',
				email=\'' . $email . '\',
				phone=\'' . $phone . '\',
				sysadmin=\'' . $sysadmin . '\'
				WHERE id=\'' . $userid . '\'');
		}
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a user id and deletes the
	 * user and all permissions related to it.
	 *
	 * @return mixed
	 */	 
	public function deleteUser ($userid) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$delPermissions = $DB->setFields('DELETE FROM sheep_permissions
			WHERE user_id=\'' . $userid . '\'');
		$result = $DB->setFields('DELETE FROM sheep_user
			WHERE id=\'' . $userid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database.
	 *
	 * @return mixed
	 */	 
	public function deleteSheep ($sheepid) {
		$DB = new Database();
		$DB->connect();
		$sheepid = $DB->escapeStrings($sheepid);
		$result = $DB->setFields('DELETE FROM sheep_sheep
			WHERE id=\'' . $sheepid . '\'');
		if ($result >= 1) {
			$delUpdates = $DB->setFields('DELETE FROM sheep_updates
				WHERE sheep_id=\'' . $sheepid . '\'');
		}
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a user id and farm id
	 * and deletes corresponding access in database.
	 *
	 * @return mixed
	 */	 
	public function deleteAccess ($userid, $farmid) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$result = $DB->setFields('DELETE FROM sheep_permissions
			WHERE user_id=\'' . $userid . '\'
			AND farm_id=\'' . $farmid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a farm id and deletes all related sheeps,
	 * permissions and updates
	 *
	 * @return mixed
	 */	 
	public function deleteFarm ($farmid) {
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$sheeps = $DB->getResults('SELECT id FROM sheep_sheep WHERE farm_id = \'' . $farmid . '\'');
		foreach ($sheeps as $sheep) {
			$delUpdates = $DB->setFields('DELETE FROM sheep_updates
				WHERE sheep_id=\'' . $sheep['id'] . '\'');
		}
		$delSheps = $DB->setFields('DELETE FROM sheep_sheep
			WHERE farm_id=\'' . $farmid . '\'');
		$delPermissions = $DB->setFields('DELETE FROM sheep_permissions
			WHERE farm_id=\'' . $farmid . '\'');
		$delFarms = $DB->setFields('DELETE FROM sheep_farm
			WHERE id=\'' . $farmid . '\'');
		$return = $delFarms;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a all values of a farm to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function editFarm ($farmid, $name, $address) {
		$DB = new Database();
		$DB->connect();
		$farmid = $DB->escapeStrings($farmid);
		$name = $DB->escapeStrings($name);
		$address = $DB->escapeStrings($address);
		$result = $DB->setFields('UPDATE sheep_farm
			SET name=\'' . $name . '\',
			address=\'' . $address . '\'
			WHERE id=\'' . $farmid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives var for a "new user" and stores it in 
	 * a database
	 *
	 * @return inserted ID or 0
	 */	
	public function newUser ($name, $username, $email, $phone, $password, $confirmpassword, $level) {
		$DB = new Database();
		$DB->connect();
		$name = $DB->escapeStrings($name);
		$username = $DB->escapeStrings($username);
		$email = $DB->escapeStrings($email);
		$phone = $DB->escapeStrings($phone);
		$password = $DB->escapeStrings($password);
		$confirmpassword = $DB->escapeStrings($confirmpassword);
		$level = $DB->escapeStrings($level);
		if ($password == $confirmpassword && $password != '') {
			$result = $DB->setFields('INSERT INTO sheep_user
				(name, un, pw, email, phone, sysadmin)
				VALUES (\'' . $name . '\', \'' . $username . '\', \'' . $password . '\', \'' . $email . '\', \'' . $phone . '\', \'' . $level . '\')
			');
			if ($result >= 1) {
				$id = $DB->getResults('SELECT last_insert_id() as id');
				$return = $id[0]['id'];
			}
			else {
				$return = 0;
			}
		}
		else {
			$return = 0;
		}
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives var for a "new user" and stores it in 
	 * a database
	 *
	 * @return inserted ID or 0
	 */	
	public function newFarm ($name, $address) {
		$DB = new Database();
		$DB->connect();
		$name = $DB->escapeStrings($name);
		$address = $DB->escapeStrings($address);
		if ($name != '' && $address != '') {
			$result = $DB->setFields('INSERT INTO sheep_farm
				(name, address)
				VALUES (\'' . $name . '\', \'' . $address . '\')
			');
			if ($result >= 1) {
				$id = $DB->getResults('SELECT last_insert_id() as id');
				$return = $id[0]['id'];
			}
			else {
				$return = 0;
			}
		}
		else {
			$return = 0;
		}
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a user id, farm id and
	 * access level and stores it in the database.
	 *
	 * @return mixed
	 */	 
	public function newAccess ($userid, $farmid, $level) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$level = $DB->escapeStrings($level);
		if ($userid != '' && $farmid != '' && $level != '') {
			$result = $DB->setFields('INSERT INTO sheep_permissions
				(user_id, farm_id, level)
				VALUES (\'' . $userid . '\', \'' . $farmid . '\', \'' . $level . '\')
			');
			if ($result >= 1) {
				$id = $DB->getResults('SELECT last_insert_id() as id');
				$return = $id[0]['id'];
			}
			else {
				$return = 0;
			}
		}
		else {
			$return = 0;
		}
		$DB->disconnect();
		return $return;
	}
	
	/**
	 * Method that receives a all values of a sheep to update
	 * in the database
	 *
	 * @return mixed
	 */	 
	public function newSheep ($farmid, $name, $born, $deceased, $weight, $comment) {
		$DB = new Database();
		$DB->connect();
		$born = $this->formatDate($born);
		$deceased = $this->formatDate($deceased);
		$farmid = $DB->escapeStrings($farmid);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		if ($born < $deceased) {
			$deceased = 86400;
		}
		$comment = $DB->escapeStrings($comment);
		$weight = $DB->escapeStrings($weight);		
		if ($name != '' && $farmid != '') {
			$result = $DB->setFields('INSERT INTO sheep_sheep
				(name, born, deceased, comment, weight, farm_id)
				VALUES (\'' . $name . '\', FROM_UNIXTIME(' . $born . '), 
					FROM_UNIXTIME(' . $deceased . '), \'' . $comment . '\',
					\'' . $weight . '\', \'' . $farmid . '\')
			');
			if ($result >= 1) {
				$id = $DB->getResults('SELECT last_insert_id() as id');
				$return = $id[0]['id'];
			}
			else {
				$return = 0;
			}
		}
		else {
			$return = 0;
		}
		$DB->disconnect();
		return $return;
	}
}
?>
