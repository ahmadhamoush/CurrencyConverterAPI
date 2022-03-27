<?php

	$rate_url = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP";
	$curl = curl_init();
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($curl, CURLOPT_HTTPHEADER, array('Accept: application/json'));
	curl_setopt($curl, CURLOPT_URL, $rate_url);
	$rates = curl_exec($curl);
	curl_close($curl);
	$obj_len = strlen($rates);
	$updated_rate = substr($rates, $obj_len -8, ($obj_len -8) - ($obj_len-5));
	if ($rates !== null) {
		$updated_rate = json_encode(array('rate' => $updated_rate));
   		echo $updated_rate;
	} else {
   		print('Error fetching rates');
	};

	function httpPost($url, $data)
{
    $curl = curl_init($url);
    curl_setopt($curl, CURLOPT_POST, true);
    curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    $response = curl_exec($curl);
    curl_close($curl);
    return $response;
}

	
	

?>

