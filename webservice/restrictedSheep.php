<?php
/**
 * This class extends the sheep class with the purpose to plug out unwanted method from the RPC mechanism.
 * Unwanted metod are simply overraided with dummy methods.
 *
 * @author fiLLLip
 */
class RestrictedSheep extends Sheep {
	/**
	 * This is a dummy method to plug out the parent unwanted method.
	 *
	 * @param string $something
	 */
	/*
	// Commentet out because we do not have a writeSomething method in the sheep class
	public function writeSomething($something) {
		throw new Exception('writeSomething method is not available for RPC');
	}
	*/
}
?>