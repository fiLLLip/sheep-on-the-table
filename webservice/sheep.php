<?php
/**
 * Parameter $hash and $userID should always be first wherever you are trying to authorize something
 *
 * @author fiLLLip
 */
class Sheep extends JsonRpcService{

	private function checkSession ($hash, $userID) {
		if (!isset($hash)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$hash = $DB->escapeStrings($hash);
		$ip = $_SERVER['REMOTE_ADDR'];
		$result = $DB->getResults('SELECT id FROM sheep_user WHERE hash = \'' . $hash . '\' AND ip = \'' . $ip . '\' LIMIT 1');
		$DB->disconnect();
		$returnID = $result[0]['id'];
		if ($userID == $returnID) {
			return $userID;
		}
		else {
			return null;
		}
	}

    /** @JsonRpcMethod
     * Method that says to client YES YOU ARE CONNECTED
     *
     * @param $user
     * @param $pass
     * @return array|null
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
		$result = $DB->getResults('SELECT id, name FROM sheep_user WHERE un = \'' . $user . '\' AND pw = \'' . $pass . '\' LIMIT 1');
		$userID = $result[0]['id'];
		$name = $result[0]['name'];
		if ($userID >= 1) {
			$ip = $_SERVER['REMOTE_ADDR'];
			$time = time();
			$hash = sha1(ip2long($ip) * $time);
			$DB->setFields('UPDATE sheep_user SET ip=\'' . $ip . '\', hash=\'' . $hash . '\' WHERE id=\'' . $userID . '\'');
			$farms = $DB->getResults('SELECT p.farm_id as id, f.name as name, f.address as address, p.level as level FROM sheep_permissions p, sheep_farm f WHERE p.user_id = \'' . $userID . '\' AND p.farm_id=f.id');
			$DB->disconnect();
			return array($hash, $userID, $name, $farms);
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
     * @param $hash
     * @param $userID
     * @param $farmID
     * @internal param array $param
     * @return mixed
     */
    public function getSheepList ($hash, $userID, $farmID) {
        if (!isset($hash)) {
            return null;
        }
        if (!isset($userID)) {
            return null;
        }
        if ($this->checkSession($hash, $userID) == null) {
            return "sessionTimeout";
        }

        if (!isset($farmID)) {
            return null;
        }
        $DB = new Database();
        $DB->connect();
        $userID = $DB->escapeStrings($userID);
        $farmID = $DB->escapeStrings($farmID);
        $numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\'');
        if ($numRows >= 1) {
            $returnArr = $DB->getResults('SELECT s.id, s.farm_id, s.name, UNIX_TIMESTAMP(s.born) as born,
				UNIX_TIMESTAMP(s.deceased) as deceased, s.comment, s.weight, u.id as updateid, 
				UNIX_TIMESTAMP(u.timestamp) as updatetimestamp, u.pos_x as updateposx, 
				u.pos_y as updateposy, u.pulse as updatepulse, u.temp as updatetemp, 
				u.alarm as updatealarm
				FROM sheep_sheep s
				LEFT OUTER JOIN sheep_updates u on u.id=(SELECT MAX(id) FROM sheep_updates WHERE sheep_id=s.id)
				WHERE s.farm_id = \'' . $farmID . '\'
				');
            $DB->disconnect();
            return array($returnArr);
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
     * @param $farmID
     * @return mixed
     */
    public function getNonAuthSheepList ($farmID) {
        if (!isset($farmID)) {
            return null;
        }
        $DB = new Database();
        $DB->connect();
        $farmID = $DB->escapeStrings($farmID);
        $returnArr = $DB->getResults('SELECT s.id, s.farm_id, s.name, UNIX_TIMESTAMP(s.born) as born,
            UNIX_TIMESTAMP(s.deceased) as deceased, s.comment, s.weight, u.id as updateid,
            UNIX_TIMESTAMP(u.timestamp) as updatetimestamp, u.pos_x as updateposx,
            u.pos_y as updateposy, u.pulse as updatepulse, u.temp as updatetemp,
            u.alarm as updatealarm
            FROM sheep_sheep s
            LEFT OUTER JOIN sheep_updates u on u.id=(SELECT MAX(id) FROM sheep_updates WHERE sheep_id=s.id)
            WHERE s.farm_id = \'' . $farmID . '\'
            LIMIT 100');
        $DB->disconnect();
        return array($returnArr);
    }

    /** @JsonRpcMethod
     * Method that serves the client all the n updates that belong
     * to his sheep
     *
     * @param $hash
     * @param $userID
     * @param $sheepID
     * @param $limit
     * @internal param array $param
     * @return mixed
     */
	public function getSheepUpdates ($hash, $userID, $sheepID, $limit) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
	
		if (!isset($sheepID)) {
			return null;
		}
		if (!isset($limit)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$sheepID = $DB->escapeStrings($sheepID);
		$limit = $DB->escapeStrings($limit);
		$returnArr = $DB->getResults('SELECT id, UNIX_TIMESTAMP(timestamp) as timestamp, pos_x, pos_y, pulse, temp, alarm
			FROM sheep_updates WHERE sheep_id = \''. $sheepID .'\' ORDER BY id DESC LIMIT ' . $limit . '');
		$DB->disconnect();
		return array($returnArr);
	}

    /** @JsonRpcMethod
     * Method that receives a all values of a sheep to update
     * in the database
     *
     * @param $hash
     * @param $userID
     * @param $id
     * @param $name
     * @param $born
     * @param $deceased
     * @param $comment
     * @param $weight
     * @return mixed
     */
	public function editSheep ($hash, $userID, $id, $name, $born, $deceased, $comment, $weight) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
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
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions p, sheep_sheep s WHERE p.user_id=\'' . $userID . '\' AND p.farm_id=s.farm_id AND s.id=\'' . $id . '\' AND p.level >= 1');
		if ($numRows >= 1) {
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
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $userToCheckID
     * @return mixed
     */
	public function getUserPermission ($hash, $userID, $farmID, $userToCheckID) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
	
		if (!isset($farmID)) {
			return null;
		}
		if (!isset($userToCheckID)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$farmID = $DB->escapeStrings($farmID);
		$userToCheckID = $DB->escapeStrings($userToCheckID);
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\'');
		if ($numRows >= 1) {
			$returnArr = $DB->getResults('SELECT level FROM sheep_permissions WHERE user_id = \'' . $userToCheckID . '\' AND farm_id = \''. $farmID .'\' LIMIT 1');
			$value = $returnArr[0]['level'];
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
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $editUserID
     * @param $level
     * @return mixed
     */
	public function setUserPermission ($hash, $userID, $farmID, $editUserID, $level) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		if (!isset($farmID)) {
			return null;
		}
		if (!isset($editUserID)) {
			return null;
		}
		if (!isset($level)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmID = $DB->escapeStrings($farmID);
		$editUserID = $DB->escapeStrings($editUserID);
		$level = $DB->escapeStrings($level);
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\' AND level>=\'2\'');
        if ($numRows >= 1) {
            if ($numRows == 1 && $level <=1) {
                $return = null;
            }
            else{
                $result = $DB->setFields('UPDATE sheep_permissions
                    SET level=\'' . $level . '\'
                    WHERE user_id=\'' . $editUserID . '\'
                    AND farm_id=\'' . $farmID . '\'');
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
     * Adds a new permission to a farm based on username of specific user.
     *
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $username
     * @return mixed
     */
    public function addNewUserToFarm ($hash, $userID, $farmID, $username) {
        if (!isset($hash)) {
            return null;
        }
        if (!isset($userID)) {
            return null;
        }
        if ($this->checkSession($hash, $userID) == null) {
            return "sessionTimeout";
        }
        if (!isset($farmID)) {
            return null;
        }
        if (!isset($username)) {
            return null;
        }
        $DB = new Database();
        $DB->connect();
        $username = $DB->escapeStrings($username);
        $farmID = $DB->escapeStrings($farmID);
        $numAdmins = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\' AND level>=\'2\'');
        $userFound = $DB->getResults('SELECT id FROM sheep_user WHERE un = \'' . $username . '\'');
        if ($numAdmins >= 1 && isset($userFound[0]['id'])) {
            $userFoundID = $userFound[0]['id'];
            $result = $DB->setFields('INSERT INTO sheep_permissions
				SET user_id=\'' . $userFoundID . '\',
				farm_id=\'' . $farmID . '\'');
            $return = $result;
        }
        else {
            $return = null;
        }
        $DB->disconnect();
        return $return;
    }

    /** @JsonRpcMethod
     * Adds a new permission to a farm based on username of specific user.
     *
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $deleteUserID
     * @return mixed
     */
    public function removeUserFromFarm ($hash, $userID, $farmID, $deleteUserID) {
        if (!isset($hash)) {
            return null;
        }
        if (!isset($userID)) {
            return null;
        }
        if ($this->checkSession($hash, $userID) == null) {
            return "sessionTimeout";
        }
        if (!isset($farmID)) {
            return null;
        }
        if (!isset($deleteUserID)) {
            return null;
        }
        $DB = new Database();
        $DB->connect();
        $deleteUserID = $DB->escapeStrings($deleteUserID);
        $farmID = $DB->escapeStrings($farmID);
        $numAdmins = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\' AND level=\'2\'');
        $numUsersFound = $DB->getNumRows('SELECT id FROM sheep_user WHERE id = \'' . $deleteUserID . '\'');
        if ($numAdmins >= 1 && $numUsersFound >= 1) {
            $result = $DB->setFields('DELETE FROM sheep_permissions
				WHERE user_id=\'' . $deleteUserID . '\' AND
				farm_id=\'' . $farmID . '\'');
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
     * @param $hash
     * @param $userID
     * @param $farmID
     * @return mixed
     */
	public function getUsersForFarm ($hash, $userID, $farmID) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
	
		if (!isset($farmID)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$farmID = $DB->escapeStrings($farmID);
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\'');
		if ($numRows >= 1) {
			$returnArr = $DB->getResults('SELECT p.user_id, p.level, u.un, u.name, u.email, u.phone, p.SMSAlarmAttack, p.SMSAlarmHealth, p.SMSAlarmStationary, p.EmailAlarmAttack, p.EmailAlarmHealth,
				p.EmailAlarmStationary FROM sheep_permissions p, sheep_user u WHERE u.id=p.user_id AND p.farm_id = \''. $farmID .'\'');
			$DB->disconnect();
			return array($returnArr);
		}
		else {
			$DB->disconnect();
			return null;
		}
	}

    /** @JsonRpcMethod
     * Method that receives a userid and returns all details
     *
     * @param $hash
     * @param $userID
     * @return mixed
     */
	public function getUserDetails ($hash, $userID) {
		if (!isset($hash)) {
			return null;
		}	
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		$DB = new Database();
		$DB->connect();
		$userID = $DB->escapeStrings($userID);
		$returnArr = $DB->getResults('SELECT id, un, name, email, phone FROM sheep_user WHERE id = \''. $userID .'\' LIMIT 1');
		$DB->disconnect();
		return $returnArr[0];
	}

    /** @JsonRpcMethod
     * Method that receives all settings values of a user to update
     * in the database
     *
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $userToSetID
     * @param $smsAttack
     * @param $smsHealth
     * @param $smsStationary
     * @param $emailAttack
     * @param $emailHealth
     * @param $emailStationary
     * @return mixed
     */
	public function setUserSettings ($hash, $userID, $farmID, $userToSetID,
		$smsAttack, $smsHealth, $smsStationary,
		$emailAttack, $emailHealth, $emailStationary) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		if (!isset($farmID)) {
			return null;
		}
		if (!isset($userToSetID)) {
			return null;
		}
		if (!isset($smsAttack)) {
			return null;
		}
		if (!isset($smsHealth)) {
			return null;
		}
		if (!isset($smsStationary)) {
			return null;
		}
		if (!isset($emailAttack)) {
			return null;
		}
		if (!isset($emailHealth)) {
			return null;
		}
		if (!isset($emailStationary)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmID = $DB->escapeStrings($farmID);
		$userToSetID = $DB->escapeStrings($userToSetID);
		$smsAttack = $DB->escapeStrings($smsAttack);
		$smsHealth = $DB->escapeStrings($smsHealth);
		$smsStationary = $DB->escapeStrings($smsStationary);
		$emailAttack = $DB->escapeStrings($emailAttack);
		$emailHealth = $DB->escapeStrings($emailHealth);
		$emailStationary = $DB->escapeStrings($emailStationary);
		if ($userID == $userToSetID) {
			$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\'');
		}
		else {
			$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\' AND level=\'2\'');
		}
		if ($numRows >= 1) {
			$result = $DB->setFields('UPDATE sheep_permissions
				SET SMSAlarmAttack=\'' . $smsAttack . '\',
				SMSAlarmHealth=\'' . $smsHealth . '\',
				SMSAlarmStationary=\'' . $smsStationary . '\',
				EmailAlarmAttack=\'' . $emailAttack . '\',
				EmailAlarmHealth=\'' . $emailHealth . '\',
				EmailAlarmStationary=\'' . $emailStationary . '\'
				WHERE user_id=\'' . $userToSetID . '\'
				AND farm_id=\'' . $farmID . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}

    /** @JsonRpcMethod
     * Method that receives all detail values of logged in user to update
     * in the database
     *
     * @param $hash
     * @param $userID
     * @param $name
     * @param $email
     * @param $phone
     * @return mixed
     */
	public function setUserDetails ($hash, $userID, $name, $email, $phone) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
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
		$userID = $DB->escapeStrings($userID);
		$name = $DB->escapeStrings($name);
		$email = $DB->escapeStrings($email);
		$phone = $DB->escapeStrings($phone);
		$numRows = $DB->getNumRows('SELECT id FROM sheep_user WHERE id = \'' . $userID . '\'');
		if ($numRows >= 1) {
			$result = $DB->setFields('UPDATE sheep_user
				SET name=\'' . $name . '\',
				email=\'' . $email . '\',
				phone=\'' . $phone . '\'
				WHERE id=\'' . $userID . '\'');
			$return = $result;
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		return $return;
	}

    /** @JsonRpcMethod
     * Method that receives new password for logged in user to update
     * in the database
     *
     * @param $hash
     * @param $userID
     * @param $oldPassword
     * @param $newPassword
     * @param $newConfirmPassword
     * @return mixed
     */
	public function setUserNewPassword ($hash, $userID, $oldPassword, $newPassword, $newConfirmPassword) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		if (!isset($oldPassword)) {
			return null;
		}
		if (!isset($newPassword)) {
			return null;
		}
		if (!isset($newConfirmPassword)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$oldPassword = $DB->escapeStrings($oldPassword);
		$newPassword = $DB->escapeStrings($newPassword);
		$newConfirmPassword = $DB->escapeStrings($newConfirmPassword);
		
		$numRows = $DB->getNumRows('SELECT id FROM sheep_user WHERE id = \'' . $userID . '\' AND pw = \'' . $oldPassword . '\'');
		if ($numRows >= 1 && $newPassword == $newConfirmPassword) {
			$result = $DB->setFields('UPDATE sheep_user
				SET pw=\'' . $newPassword . '\'
				WHERE id=\'' . $userID . '\'');
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
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $name
     * @param $born
     * @param $deceased
     * @param $comment
     * @param $weight
     * @return mixed
     */
	public function newSheep ($hash, $userID, $farmID, $name, $born, $deceased, $comment, $weight) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		if (!isset($farmID)) {
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
		$farmID = $DB->escapeStrings($farmID);
		$name = $DB->escapeStrings($name);
		$born = $DB->escapeStrings($born);
		$deceased = $DB->escapeStrings($deceased);
		$comment = $DB->escapeStrings($comment);
		$weight = $DB->escapeStrings($weight);
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions p WHERE p.user_id=\'' . $userID . '\' AND p.farm_id=\'' . $farmID . '\' AND p.level >= 1');
		if ($numRows >= 1) {
			
			$result = $DB->setFields('INSERT INTO sheep_sheep
				(farm_id, name, born, deceased, comment, weight)
				VALUES (\'' . $farmID . '\', \'' . $name . '\', FROM_UNIXTIME(' . $born . '),
				FROM_UNIXTIME(' . $deceased . '), \'' . $comment . '\', \'' . $weight . '\')
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
     * Method that receives a username to add permission to a farm. Checks if userid that requests is owner.
     *
     * @param $hash
     * @param $userID
     * @param $farmID
     * @param $username
     * @return mixed
     */
	public function addUserToFarm ($hash, $userID, $farmID, $username) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		if (!isset($farmID)) {
			return null;
		}
		if (!isset($username)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$farmID = $DB->escapeStrings($farmID);
		$username = $DB->escapeStrings($username);
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions WHERE user_id = \'' . $userID . '\' AND farm_id = \'' . $farmID . '\' AND level=\'2\'');
        $return = null;
		if ($numRows >= 1) {
			$numRows = $DB->getNumRows('SELECT id FROM sheep_user WHERE un = \'' . $username . '\'');
			if ($numRows >= 1) {
				$userArr = $DB->getResults('SELECT id FROM sheep_user WHERE un = \''. $username .'\' LIMIT 1');
				$result = $DB->setFields('INSERT INTO sheep_permissions
					(user_id, farm_id)
					VALUES (\'' . $userArr[0]['id'] . '\', \'' . $farmID . '\')
				');
				$return = $result;
			}
		}
		$DB->disconnect();
		return $return;
	}

    /** @JsonRpcMethod
     * Method that simulates updates for all sheeps
     *
     * @param $possibilityOfAlarm
     * @return mixed
     */
	public function simulateSheepUpdates ($possibilityOfAlarm) {
		$DB = new Database();
		$DB->connect();
		$result = $DB->getResults('SELECT id FROM sheep_sheep ORDER BY id DESC');
		$alarms = 0;
		foreach ($result as $sheep) {
			$sheepID = $sheep['id'];
			$posX = rand(4510000, 17000000) / 1000000;
			//$posX = rand(4510000, 4510090) / 1000000;
			$posY = rand(58000000, 64000000) / 1000000;
			//$posy = rand(58000000, 58000090) / 1000000;
			$pulse = rand(50, 90);
			$temp = rand(35, 40);
			$alarm = rand(0, $possibilityOfAlarm);
			if ($alarm != 1) {
				$alarm = 0;
			}
			else {
				$alarms++;
			}
			$this->newSheepUpdate($sheepID, $posX, $posY, $pulse, $temp, $alarm);
		}
		return 'Success, alarms invoked: ' . $alarms;
		
	}

    /** @JsonRpcMethod
     * Method that receives var for a "new update" and stores it in
     * a database
     *
     * @param $sheepID
     * @param $posX
     * @param $posY
     * @param $pulse
     * @param $temp
     * @param $alarm
     * @return mixed
     */
	public function newSheepUpdate ($sheepID, $posX, $posY, $pulse, $temp, $alarm) {
		if (!isset($sheepID)) {
			return null;
		}
		if (!isset($posX)) {
			return null;
		}
		if (!isset($posY)) {
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
		$sheepID = $DB->escapeStrings($sheepID);
		$posX = $DB->escapeStrings($posX);
		$posY = $DB->escapeStrings($posY);
		$pulse = $DB->escapeStrings($pulse);
		$temp = $DB->escapeStrings($temp);
		$alarm = $DB->escapeStrings($alarm);
		$result = $DB->setFields('
			INSERT INTO sheep_updates
			(sheep_id, timestamp, pos_x, pos_y, pulse, temp, alarm)
			VALUES (\'' . $sheepID . '\', FROM_UNIXTIME(' . time() . '), \'' . $posX . '\',
			\'' . $posY . '\', \'' . $pulse . '\', \'' . $temp . '\', \'' . $alarm . '\')');
		$updatedID = $DB->getResults('SELECT last_insert_id() as id');
		$return = $result;
		//error_log('DB Result: ' . $return);
		if ($return >= 1) {
			$alarms = 0;
			if (intval($pulse) > 200 || intval($pulse) < 40 || intval($temp) > 42 || intval($temp) < 30) {
				$this->doAlarmForSheep($sheepID, 'health', $posX, $posY);
				$alarms += 2;
			}
			$result = $DB->getResults('SELECT pos_x, pos_y FROM sheep_updates WHERE sheep_id=\'' . $sheepID . '\' ORDER BY id DESC LIMIT 3');
			$maxMeters = -1;
			if (isset($result[0]) && isset($result[1]) && isset($result[2])) {
				$maxMeters = $this->maxDistanceFromThreePoints($result[0]['pos_y'], 
					$result[0]['pos_x'], $result[1]['pos_y'], 
					$result[1]['pos_x'], $result[2]['pos_y'], $result[2]['pos_x']);
			}
			if ($maxMeters != -1 && $maxMeters < 50) {
				$this->doAlarmForSheep($sheepID, 'stationary', $posX, $posY);
				$alarms += 4;
			}
			if ($alarm == '1') {
				$this->doAlarmForSheep($sheepID, 'attack', $posX, $posY);
				$alarms += 1;
			}
			$alarms = $DB->escapeStrings($alarms);
			$DB->setFields('
				UPDATE sheep_updates
				SET alarm = \'' . $alarms . '\'
				WHERE id = \'' . $updatedID . '\'');
		}
        $DB->disconnect();
		return $return;
	}
	
	private function maxDistanceFromThreePoints($lat1, $lng1, $lat2, $lng2, $lat3, $lng3){
		$pi80 = M_PI / 180;
		$lat1 *= $pi80;
		$lng1 *= $pi80;
		$lat2 *= $pi80;
		$lng2 *= $pi80;
		$lat3 *= $pi80;
		$lng3 *= $pi80;

		$r = 6372.797; // mean radius of Earth in km
		$dLat[0] = array($lat2 - $lat1, $lat2, $lat1);
		$dLat[1] = array($lat3 - $lat1, $lat3, $lat1);
		$dLat[2] = array($lat3 - $lat2, $lat3, $lat2);
		$dLng[0] = $lng2 - $lng1;
		$dLng[1] = $lng3 - $lng1;
		$dLng[2] = $lng3 - $lng2;
		$maxMeters = 0;
		for ($i = 0;$i<=2;$i++) {
			$a = sin($dLat[$i][0] / 2) * sin($dLat[$i][0] / 2) + cos($dLat[$i][2]) * cos($dLat[$i][1]) * sin($dLng[$i] / 2) * sin($dLng[$i] / 2);
			$c = 2 * atan2(sqrt($a), sqrt(1 - $a));
			$meters = $r * $c * 0.621371192 * 1000;
			if ($meters > $maxMeters) {
				$maxMeters = $meters;
			}
			//error_log('Difference in meters (' . $i . '): ' . $meters);
		}
		
		return ($maxMeters);
	}
	
	private function doAlarmForSheep ($sheepId, $method, $posX, $posY) {
		switch ($method) {
			case "attack":
				$emailColumn = 'EmailAlarmAttack';
				$smsColumn = 'SMSAlarmAttack';
				$smsMessage = 'Sheep #' . $sheepId . ' is under attack! Last position: ' . $posY . ', ' . $posX . '';
				$mailMessage = 'Sheep #' . $sheepId . ' is under attack!' . "\r\n"
				. 'Last position: ' . $posY . ', ' . $posX . '' . "\r\n"
				. 'http://dyn.filllip.net/sheepwebservice2/map.php?lon=' . $posX . '&lat=' . $posY . '';
				break;
				
			case "stationary":
				$emailColumn = 'EmailAlarmStationary';
				$smsColumn = 'SMSAlarmStationary';
				$smsMessage = 'Sheep #' . $sheepId . ' is stationary! Last position: ' . $posY . ', ' . $posX . '';
				$mailMessage = 'Sheep #' . $sheepId . ' is stationary!' . "\r\n"
				. 'Last position: ' . $posY . ', ' . $posX . '' . "\r\n"
				. 'http://dyn.filllip.net/sheepwebservice2/map.php?lon=' . $posX . '&lat=' . $posY . '';
				break;
				
			case "health":
				$emailColumn = 'EmailAlarmHealth';
				$smsColumn = 'SMSAlarmHealth';
				$smsMessage = 'Sheep #' . $sheepId . ' has abnormal health! Last position: ' . $posY . ', ' . $posX . '';
				$mailMessage = 'Sheep #' . $sheepId . ' has abnormal health!' . "\r\n"
				. 'Last position: ' . $posY . ', ' . $posX . '' . "\r\n"
				. 'http://dyn.filllip.net/sheepwebservice2/map.php?lon=' . $posX . '&lat=' . $posY . '';
				break;
			
			default:
				return;
		}
		$DB = new Database();
		$DB->connect();
		$result = $DB->getResults('SELECT p.' . $emailColumn . ', p.' . $smsColumn . ', u.email, u.phone
			FROM sheep_permissions p, sheep_user u, sheep_sheep s
			WHERE s.id=\'' . $sheepId . '\'
			AND s.farm_id=p.farm_id
			AND u.id=p.user_id
			');
		$DB->disconnect();
		foreach ($result as $user) {
			if ($user[$emailColumn] == '1') {
				$this->sendEMail($user['email'], $mailMessage);
			}
			if ($user[$smsColumn] == '1') {
				//$this->sendSMS($user['phone'], $smsmessage);
			}
		}
	}
	
	private function sendSMS ($phoneNumber, $message) {
		//set POST variables
		$url = 'http://www.vestnesconsulting.no/smsgateway/smssheep.php';
		$fields = array(
            'recipient' => urlencode($phoneNumber),
            'message' => urlencode($message)
        );
		$fields_string = '';
		foreach ($fields as $key=>$value) { 
			$fields_string .= $key.'='.$value.'&';
		}
		rtrim($fields_string, '&');

		//open connection
		$cinit = curl_init();

		//set the url, number of POST vars, POST data
		curl_setopt($cinit,CURLOPT_URL, $url);
		curl_setopt($cinit,CURLOPT_POST, count($fields));
		curl_setopt($cinit,CURLOPT_POSTFIELDS, $fields_string);
		curl_setopt($cinit,CURLOPT_RETURNTRANSFER, true);
		//execute post
		$curlresult = curl_exec($cinit);
		error_log('Tried to send SMS to ' . $phoneNumber . ' - Result: ' . $curlresult);
		//close connection
		curl_close($cinit);
	}
	
	private function sendEMail ($email, $message) {
		$message = wordwrap($message, 70);
		$headers = 'From: webmaster@sheepfinder.noexist' . "\r\n" .
			'Reply-To: webmaster@sheepfinder.noexist' . "\r\n" .
			'X-Mailer: PHP/' . phpversion();
		error_log('Sending mail: ' .mail($email, 'Sheep Finder Warning!', $message, $headers));
	}

    /** @JsonRpcMethod
     * Method that deletes a sheep from the database given ID
     *
     * @param $hash
     * @param $userID
     * @param $id
     * @return mixed
     */
	public function removeSheep ($hash, $userID, $id) {
		if (!isset($hash)) {
			return null;
		}
		if (!isset($userID)) {
			return null;
		}
		if ($this->checkSession($hash, $userID) == null) {
			return "sessionTimeout";
		}
		if (!isset($id)) {
			return null;
		}
		$DB = new Database();
		$DB->connect();
		$id = $DB->escapeStrings($id);
		$numRows = $DB->getNumRows('SELECT user_id FROM sheep_permissions p, sheep_sheep s WHERE p.user_id=\'' . $userID . '\' AND p.farm_id=s.farm_id AND s.id=\'' . $id . '\' AND p.level >= 1');
		if ($numRows >= 1) {
			$result = $DB->setFields('DELETE FROM sheep_sheep WHERE id=\'' . $id . '\'');
			if ($result >= 1) {
				$DB->setFields('DELETE FROM sheep_updates WHERE sheep_id=\'' . $id . '\'');
			}
			$return = $result;
		}
		else {
			$return = -1;
		}
		$DB->disconnect();
		return $return;
	}
}
?>
