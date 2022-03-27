<?php
	include("db_info.php");
	header('Content-Type: application/json; charset=utf-8');

	$amount = @$_POST["amount"] ;
	$rate = @$_POST["rate"];
	$currency = @$_POST["currency"];


	$query = $mysqli->prepare("INSERT INTO rates (amount, rate, currency) VALUES (?, ?, ?)");
	$query->bind_param("sss", $amount, $rate, $currency);
	$query->execute();

	$lastid = mysqli_insert_id($mysqli);
	echo "Last ID: ".$lastid."\n";

	$query = $mysqli->prepare("SELECT * FROM rates WHERE id = ?");
	$lastid =31;
	$query->bind_param("i", $lastid);
	$query->execute();
	$array = $query->get_result();
	$response = [];

	while($amount = $array->fetch_assoc()){
    $response[] = $amount;
	}

	$json_response = json_encode($response);
	echo $json_response;

	$myObj = json_decode($json_response, true);
	
	$amount_to_convert = $myObj[0]["amount"];
	$updated_rate = $myObj[0]["rate"];
	$currency = $myObj[0]["currency"];

	if($currency == "LBP"){
		$conversion =number_format((float)($amount_to_convert / $updated_rate), 2, '.', '');
		print_r($conversion);

	}
	else if($currency == "USD"){
		$conversion =number_format((float)($amount_to_convert * $updated_rate), 3, ',', ',');
		print_r($conversion);
	}
	else{
		echo "currency not available";
	}



	echo "\n";

	$response = [];
	$response["status"] = "Success";
	$json_response = json_encode($response);
	echo $json_response;


?>

