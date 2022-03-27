<?php
	include("db_info.php");
	header('Content-Type: application/json; charset=utf-8');

	$response = [];
	$response["status"] = ""; 

	$amount = @$_POST["amount"] ; //getting post data from android app
	$rate = @$_POST["rate"];
	$currency = @$_POST["currency"];

	if(@$_POST["amount"]!== NULL AND @$_POST["rate"]!== NULL AND @$_POST["currency"]!== NULL){
		$server_response["status"] = "Success"; // respond with success if data is posted
	}else{
		$server_response["status"] = "Fail"; //fetching data failed
	}

	//inserting posted data to database
	$query = $mysqli->prepare("INSERT INTO rates (amount, rate, currency) VALUES (?, ?, ?)");
	$query->bind_param("sss", $amount, $rate, $currency);
	$query->execute();

	$lastid = mysqli_insert_id($mysqli); //getting last insert id inorder to access last posted object
	echo "id: ".$lastid."\n"; 

	$query = $mysqli->prepare("SELECT * FROM rates WHERE id = ?");

	$query->bind_param("i", $lastid); //getting last element
	$query->execute();
	$array = $query->get_result();
	$response = [];

	while($amount = $array->fetch_assoc()){
    $response[] = $amount;
	}

	$json_response = json_encode($response);
	echo "To be converted: ".$json_response; //returning object to be converted


	echo "\n";


	$myObj = json_decode($json_response, true); //accessing JSON properties
	$amount_to_convert = $myObj[0]["amount"];
	$updated_rate = $myObj[0]["rate"];
	$currency = $myObj[0]["currency"];

	if($currency == "LBP"){ //converting to USD 
		$conversion =number_format((float)($amount_to_convert / $updated_rate), 2, '.', '');
		$converted_currency = 'USD';

	}
	else if($currency == "USD"){ //converting to LBP
		$conversion =number_format((float)($amount_to_convert * $updated_rate), 3, ',', ',');$converted_currency = 'LBP';
	}
	else{
		echo "currency not available";
	}

	$converted_obj['amount'] = $conversion; //inserting new data into new object
	$converted_obj['rate'] = $updated_rate;
	$converted_obj['currency'] = $converted_currency;
	$converted_json = json_encode($converted_obj); 
	echo "Converted: ".$converted_json; //returning converted rate;


	echo "\n";


	$json_response = json_encode($server_response);
	echo "Server Responded with: ".$json_response; //server response


?>

