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
     * @param $user
     * @param $pass
     * @return array|null
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
     * @return void
     */
	public function logout () {
		$_SESSION = array();
		session_destroy();
	}

    /**
     * Method that gets all the users in the database and returns it as an array
     *
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
     * @param $sheepid
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
     * @param $hash
     * @param $userid
     * @param $sheepid
     * @param $limit
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
     * @param $sheepid
     * @param $farmid
     * @param $name
     * @param $born
     * @param $deceased
     * @param $comment
     * @param $weight
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
     * Method that receives a user ID and returns all details
     *
     * @param $userid
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
     * Gets details about users alarm settings and
     * level associated with farm
     *
     * @param $userid
     * @param $farmid
     * @return mixed
     */
	public function getUserFarmDetails ($userid, $farmid) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$returnarr = $DB->getResults('SELECT level, 
			SMSAlarmAttack, EmailAlarmStationary, 
			SMSAlarmStationary, SMSAlarmHealth, 
			EmailAlarmAttack, EmailAlarmHealth 
			FROM sheep_permissions 
			WHERE user_id = \''. $userid .'\' 
			AND farm_id = \''. $farmid .'\'  
			LIMIT 1');
		$DB->disconnect();
		return $returnarr;
	}

    /**
     * Gets all details of a specific farm
     *
     * @param $farmid
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
     * Updates all alarm settings and level for a
     * user associated with a farm
     *
     * @param $userid
     * @param $farmid
     * @param $level
     * @param $SMSAlarmAttack
     * @param $SMSAlarmStationary
     * @param $SMSAlarmHealth
     * @param $EmailAlarmAttack
     * @param $EmailAlarmStationary
     * @param $EmailAlarmHealth
     * @return mixed
     */
	public function setUserFarmDetails ($userid, $farmid, $level, $SMSAlarmAttack, $SMSAlarmStationary, $SMSAlarmHealth, $EmailAlarmAttack, $EmailAlarmStationary, $EmailAlarmHealth) {
		$DB = new Database();
		$DB->connect();
		$userid = $DB->escapeStrings($userid);
		$farmid = $DB->escapeStrings($farmid);
		$level = $DB->escapeStrings($level);
		$SMSAlarmAttack = $DB->escapeStrings($SMSAlarmAttack);
		$SMSAlarmStationary = $DB->escapeStrings($SMSAlarmStationary);
		$SMSAlarmHealth = $DB->escapeStrings($SMSAlarmHealth);
		$EmailAlarmAttack = $DB->escapeStrings($EmailAlarmAttack);
		$EmailAlarmStationary = $DB->escapeStrings($EmailAlarmStationary);
		$EmailAlarmHealth = $DB->escapeStrings($EmailAlarmHealth);
		
		$result = $DB->setFields('UPDATE sheep_permissions
			SET level=\'' . $level . '\',	
			SMSAlarmAttack=\'' . $SMSAlarmAttack . '\',
			SMSAlarmStationary=\'' . $SMSAlarmStationary . '\',
			SMSAlarmHealth=\'' . $SMSAlarmHealth . '\',
			EmailAlarmAttack=\'' . $EmailAlarmAttack . '\',
			EmailAlarmStationary=\'' . $EmailAlarmStationary . '\',
			EmailAlarmHealth=\'' . $EmailAlarmHealth . '\'
			WHERE user_id=\'' . $userid . '\'
			AND farm_id=\'' . $farmid . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Sets access level for a specific user
     * associated with a farm
     *
     * @param $userID
     * @param $farmID
     * @param $level
     * @return mixed
     */
	public function setFarmUserDetails ($userID, $farmID, $level) {
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$farmID = $DB->escapeStrings($farmID);
		$level = $DB->escapeStrings($level);
		
		$result = $DB->setFields('UPDATE sheep_permissions
			SET level=\'' . $level . '\'
			WHERE user_id=\'' . $userID . '\'
			AND farm_id=\'' . $farmID . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Updates a specific user, sets password if
     * field is not empty
     *
     * @param $userID
     * @param $name
     * @param $username
     * @param $email
     * @param $phone
     * @param $password
     * @param $sysadmin
     * @return mixed
     */
	public function editUser ($userID, $name, $username, $email, $phone, $password, $sysadmin) {
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
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
				WHERE id=\'' . $userID . '\'');
		}
		else {
			$result = $DB->setFields('UPDATE sheep_user
				SET name=\'' . $name . '\',
				un=\'' . $username . '\',
				email=\'' . $email . '\',
				phone=\'' . $phone . '\',
				sysadmin=\'' . $sysadmin . '\'
				WHERE id=\'' . $userID . '\'');
		}
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Deletes a specific user and all its relations
     * to farms
     *
     * @param $userID
     * @return mixed
     */
	public function deleteUser ($userID) {
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$delPermissions = $DB->setFields('DELETE FROM sheep_permissions
			WHERE user_id=\'' . $userID . '\'');
		$result = $DB->setFields('DELETE FROM sheep_user
			WHERE id=\'' . $userID . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Deletes a specific sheep and all its related
     * updates
     *
     * @param $sheepID
     * @return mixed
     */
	public function deleteSheep ($sheepID) {
		$DB = new Database();
		$DB->connect();
		$sheepID = $DB->escapeStrings($sheepID);
		$result = $DB->setFields('DELETE FROM sheep_sheep
			WHERE id=\'' . $sheepID . '\'');
		if ($result >= 1) {
			$delUpdates = $DB->setFields('DELETE FROM sheep_updates
				WHERE sheep_id=\'' . $sheepID . '\'');
		}
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Deletes the access between a user and a farm, including
     * alarm settings for the specific farm
     *
     * @param $userID
     * @param $farmID
     * @return mixed
     */
	public function deleteAccess ($userID, $farmID) {
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$result = $DB->setFields('DELETE FROM sheep_permissions
			WHERE user_id=\'' . $userID . '\'
			AND farm_id=\'' . $farmID . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Deletes the specific farm, all access permissions, sheeps, and updates.
     *
     * @param $farmID
     * @return mixed
     */
	public function deleteFarm ($farmID) {
		$DB = new Database();
		$DB->connect();
		$farmID = $DB->escapeStrings($farmID);
		$sheeps = $DB->getResults('SELECT id FROM sheep_sheep WHERE farm_id = \'' . $farmID . '\'');
		foreach ($sheeps as $sheep) {
			$delUpdates = $DB->setFields('DELETE FROM sheep_updates
				WHERE sheep_id=\'' . $sheep['id'] . '\'');
		}
		$delSheeps = $DB->setFields('DELETE FROM sheep_sheep
			WHERE farm_id=\'' . $farmID . '\'');
		$delPermissions = $DB->setFields('DELETE FROM sheep_permissions
			WHERE farm_id=\'' . $farmID . '\'');
		$delFarms = $DB->setFields('DELETE FROM sheep_farm
			WHERE id=\'' . $farmID . '\'');
		$return = $delFarms;
		$DB->disconnect();
		return $return;
	}

    /**
     * Edits name and address of a farm.
     *
     * @param $farmID
     * @param $name
     * @param $address
     * @return mixed
     */
	public function editFarm ($farmID, $name, $address) {
		$DB = new Database();
		$DB->connect();
		$farmID = $DB->escapeStrings($farmID);
		$name = $DB->escapeStrings($name);
		$address = $DB->escapeStrings($address);
		$result = $DB->setFields('UPDATE sheep_farm
			SET name=\'' . $name . '\',
			address=\'' . $address . '\'
			WHERE id=\'' . $farmID . '\'');
		$return = $result;
		$DB->disconnect();
		return $return;
	}

    /**
     * Creates a new user
     *
     * @param $name
     * @param $username
     * @param $email
     * @param $phone
     * @param $password
     * @param $confirmPassword
     * @param $sysadmin
     * @return inserted ID or 0
     */
	public function newUser ($name, $username, $email, $phone, $password, $confirmPassword, $sysadmin) {
		$DB = new Database();
		$DB->connect();
		$name = $DB->escapeStrings($name);
		$username = $DB->escapeStrings($username);
		$email = $DB->escapeStrings($email);
		$phone = $DB->escapeStrings($phone);
		$password = $DB->escapeStrings($password);
		$confirmPassword = $DB->escapeStrings($confirmPassword);
		$sysadmin = $DB->escapeStrings($sysadmin);
		if ($password == $confirmPassword && $password != '') {
			$result = $DB->setFields('INSERT INTO sheep_user
				(name, un, pw, email, phone, sysadmin)
				VALUES (\'' . $name . '\', \'' . $username . '\', \'' . $password . '\', \'' . $email . '\', \'' . $phone . '\', \'' . $sysadmin . '\')
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
     * Creates a new farm.
     *
     * @param $name
     * @param $address
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
     * Adds a new access between a farm and user.
     *
     * @param $userID
     * @param $farmID
     * @param $level
     * @return mixed
     */
	public function newAccess ($userID, $farmID, $level) {
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$farmID = $DB->escapeStrings($farmID);
		$level = $DB->escapeStrings($level);
		if ($userID != '' && $farmID != '' && $level != '') {
			$result = $DB->setFields('INSERT INTO sheep_permissions
				(user_id, farm_id, level)
				VALUES (\'' . $userID . '\', \'' . $farmID . '\', \'' . $level . '\')
			');
			$return = $result;
		}
		else {
			$return = 0;
		}
		$DB->disconnect();
		return $return;
	}

    /**
     * Creates a new sheep
     *
     * @param $farmID
     * @param $name
     * @param $born
     * @param $deceased
     * @param $weight
     * @param $comment
     * @return mixed
     */
	public function newSheep ($farmID, $name, $born, $deceased, $weight, $comment) {
		$DB = new Database();
		$DB->connect();
		$born = $this->formatDate($born);
		$deceased = $this->formatDate($deceased);
		$farmID = $DB->escapeStrings($farmID);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		if ($born < $deceased) {
			$deceased = 86400;
		}
		$comment = $DB->escapeStrings($comment);
		$weight = $DB->escapeStrings($weight);		
		if ($name != '' && $farmID != '') {
			$result = $DB->setFields('INSERT INTO sheep_sheep
				(name, born, deceased, comment, weight, farm_id)
				VALUES (\'' . $name . '\', FROM_UNIXTIME(' . $born . '), 
					FROM_UNIXTIME(' . $deceased . '), \'' . $comment . '\',
					\'' . $weight . '\', \'' . $farmID . '\')
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
