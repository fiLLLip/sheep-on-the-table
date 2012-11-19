<?php
/**
 * 
 *
 * @author fiLLLip
 */
class Database {

	private $host = "localhost";
	private $user = "knutela_sheep";
	private $pass = "pwonthetable";
	private $database = "knutela_sheep";
	private $mysqli;
	
	public function Database () {
		$this->mysqli = new mysqli($this->host, $this->user, $this->pass, $this->database);
	}
	
	/**
	 * Method to connect to the mysql database
	 * 
	 * @return boolean
	 */
	public function connect () {
		if ($this->mysqli->connect_errno) {
			return false;
		}
		else {
			$this->mysqli->set_charset("utf8");
			return true;
		}
	}
	
	/**
	 * Method to get results from a query formed as an array
	 * 
	 * @param $query
	 * @return array
	 */
	public function getResults ($query) {
		/* Select queries return a result set */
		if ($result = $this->mysqli->query($query)) {
			$rows = array();
			while ($row = $result->fetch_array(MYSQLI_ASSOC)) {
				array_push($rows, $row);
			}
			$result->free();
			return $rows;
		}
		else {
			return $this->mysqli->error;
		}
	}
	
	/**
	 * Method to update, delete or insert a set in the database
	 * 
	 * @param
	 * @return string
	 */
	public function setFields ($query) {
		/* Select queries return a result set */
		
		$this->mysqli->query($query);
		$affectedRows = $this->mysqli->affected_rows;
		return $affectedRows;
	}

    /**
     * Method to remove chances of mysql injection
     *
     * @param $input
     * @return string
     */
	public function escapeStrings ($input) {
		$output = $this->mysqli->real_escape_string($input);
		return $output;
	}

    /**
     * Method to get number of rows from a query
     *
     * @param $query
     * @return array
     */
	public function getNumRows ($query) {
		/* Select queries return a result set */
		if ($result = $this->mysqli->query($query)) {
			$return = $result->num_rows;
			$result->free();
			return $return;
		}
		else {
			return -1;
		}
	}
	
	/**
	 * Method to disconnect from the mysql database
	 * 
	 * @return boolean
	 */
	public function disconnect () {
		if ($this->mysqli->close()) {
			return true;
		}
		else {
			return false;
		}
	}
}
?>