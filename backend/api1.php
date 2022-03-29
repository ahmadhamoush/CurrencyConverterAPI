<?php
	$rate_url = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP";
	$curl = curl_init();
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_HTTPHEADER, array('Accept: application/json')); //getting json obj
	curl_setopt($curl, CURLOPT_URL, $rate_url);
	$rates = curl_exec($curl);
	curl_close($curl);
	$obj_len = strlen($rates); //length of object to get last rate
	$updated_rate = substr($rates, $obj_len -8, ($obj_len -8) - ($obj_len-5)); //getting last rate
	//checking if fetched object is not empyy and the fetched rate is a number 
	//if response is unathorized, rate would not be numeric
	if ($rates !== null AND is_numeric($updated_rate)) { 
		$updated_rate = json_encode(array('rate' => $updated_rate)); // encoding the fetched rate to JSON
   		echo $updated_rate; // sending rate response
	} else {
   		print('Error fetching rates'); 
	};
	
?>

