<?php
/**
 * 
 *
 * @author fiLLLip
 */
class Sheep {

	/**
	 * Method that serves the client all the sheeps that belong 
	 * to his farm
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function getSheepList ($param) {
		if (is_array($param)) {
			if (!isset($param['farmid'])) {
				return null;
			}
			$DB = new Database();
			$DB->connect();
			$farmid = $DB->escapeStrings($param['farmid']);
			$numrows = $DB->getNumRows('SELECT un FROM sheep_user WHERE id = \'' . $this->getUserID() . '\' AND farm_id = \'' . $farmid . '\'');
			if ($numrows >= 1) {
				$returnarr = $DB->getResults('SELECT id, farm_id, name, UNIX_TIMESTAMP(born) as born, UNIX_TIMESTAMP(deceased) as deceased, comment, weight FROM sheep_sheep WHERE farm_id = \''. $farmid .'\'');
				$DB->disconnect();
				return $returnarr;
			}
			else {
				throw new Exception('User don\'t have access to this farm');
			}
		}
		else {
			return null;
		}
	}
	
	public function getUserID () {
		$DB = new Database();
		$DB->connect();
		if ($_SERVER['PHP_AUTH_USER'] != '') {
			$returnarr = $DB->getResults('SELECT id FROM sheep_user WHERE un = \'' . $_SERVER['PHP_AUTH_USER'] . '\'');
			$return = $returnarr['id'];
		}
		else {
			$return = null;
		}
		$DB->disconnect();
		$return = 1; //Debug
		return $return;
	}

	/**
	 * Method that receives a complete Sheep object to update
	 * in the database
	 *
	 * @param array $param
	 * @return mixed
	 */	 
	public function editSheep ($param) {
		if (is_array($param)) {
			if (!isset($param['id'])) {
				return null;
			}
			if (!isset($param['name'])) {
				return null;
			}
			if (!isset($param['born'])) {
				return null;
			}
			if (!isset($param['deceased'])) {
				return null;
			}
			if (!isset($param['comment'])) {
				return null;
			}
			if (!isset($param['weight'])) {
				return null;
			}
			$DB = new Database();
			$DB->connect();
			$id = $DB->escapeStrings($param['id']);
			$name = $DB->escapeStrings($param['name']);
			$born = $DB->escapeStrings($param['born']);
			$deceased = $DB->escapeStrings($param['deceased']);
			$comment = $DB->escapeStrings($param['comment']);
			$weight = $DB->escapeStrings($param['weight']);
			$result = $DB->setFields('UPDATE sheep_sheep
				SET name=\'' . $name . '\',
				born=FROM_UNIXTIME(' . $born . '),
				deceased=FROM_UNIXTIME(' . $deceased . '),
				comment=\'' . $comment . '\',
				weight=\'' . $weight . '\'
				WHERE id=\'' . $id . '\'');
			
			$return = array(
				"affected" => $result
			);
			$DB->disconnect();
			return $return;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Method that serves the client all the updates to a 
	 * specific sheep
	 *
	 * @param array $param
	 * @return mixed
	 */	
	public function getUpdates ($param) {
		if (is_array($param)) {
			if (!isset($param['sheepid'])) {
				return null;
			}
			if (!isset($param['limit'])) {
				return null;
			}
			$DB = new Database();
			$DB->connect();
			$sheepid = $DB->escapeStrings($param['sheepid']);
			$limit = $DB->escapeStrings($param['limit']);
			$returnarr = $DB->getResults('SELECT sheep_id, UNIX_TIMESTAMP(timestamp) as timestamp, pos_x, pos_y, pulse, temp, alarm FROM sheep_updates WHERE sheep_id = \''. $sheepid .'\' LIMIT ' . $limit . '');
			$DB->disconnect();
			return $returnarr;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Method that receives a "new sheep" and stores it in 
	 * a database
	 *
	 * @param array $param
	 * @return mixed
	 */	
	public function newSheep ($param) {
		if (is_array($param)) {
			if (!isset($param['farmid'])) {
				return null;
			}
			if (!isset($param['name'])) {
				return null;
			}
			if (!isset($param['born'])) {
				return null;
			}
			if (!isset($param['deceased'])) {
				return null;
			}
			if (!isset($param['comment'])) {
				return null;
			}
			if (!isset($param['weight'])) {
				return null;
			}
			$DB = new Database();
			$DB->connect();
			$farmid = $DB->escapeStrings($param['farmid']);
			$name = $DB->escapeStrings($param['name']);
			$born = $DB->escapeStrings($param['born']);
			$deceased = $DB->escapeStrings($param['deceased']);
			$comment = $DB->escapeStrings($param['comment']);
			$weight = $DB->escapeStrings($param['weight']);
			$result = $DB->setFields('INSERT INTO sheep_sheep
				(farm_id, name, born, deceased, comment, weight)
				VALUES (\'' . $farmid . '\', \'' . $name . '\', FROM_UNIXTIME(' . $born . '),
				FROM_UNIXTIME(' . $deceased . '), \'' . $comment . '\', ' . $weight . ')
			');
			$return = array(
				"affected" => $result
			);
			$DB->disconnect();
			return $return;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Method that receives an id of a sheep to delete from
	 * a database
	 *
	 * @param array $param
	 * @return mixed
	 */	
	public function removeSheep ($param) {
		if (is_array($param)) {
			if (!isset($param['sheepid'])) {
				return null;
			}
			$DB = new Database();
			$DB->connect();
			$sheepid = $DB->escapeStrings($param['sheepid']);
			$numown = $DB->getNumRows('SELECT s.id FROM sheep_sheep s, sheep_user u WHERE u.id=\'' . $this->getUserID() . '\' AND s.farm_id=u.farm_id AND s.id=\'' . $sheepid . '\'');
			if ($numown >= 1) {
				$result = $DB->setFields('DELETE FROM sheep_sheep WHERE id=\'' . $sheepid . '\'');
			}
			else {
				$result = 0;
			}
			$return = array(
					"affected" => $result
				);
			$DB->disconnect();
			return $return;
		}
		else {
			return null;
		}
	}
}
?>
