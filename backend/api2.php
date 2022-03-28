<?php
	include("db_info.php");
	header('Content-Type: application/json; charset=utf-8');

	$response = [];
	$response[] = ""; 

	$amount = @$_POST["amount"] ; //getting post data from android app
	$rate = @$_POST["rate"];
	$currency = @$_POST["currency"];

	if(@$_POST["amount"]!== NULL AND @$_POST["rate"]!== NULL AND @$_POST["currency"]!== NULL){
		$server_response[] = "success"; // respond with success if data is posted
	}else{
		$server_response[] = "fail"; //fetching data failed
	}

	//inserting posted data to database
	$query = $mysqli->prepare("INSERT INTO rates (amount, rate, currency) VALUES (?, ?, ?)");
	$query->bind_param("sss", $amount, $rate, $currency);
	$query->execute();

	$lastid = mysqli_insert_id($mysqli); //getting last insert id inorder to access last posted object
	
	$query = $mysqli->prepare("SELECT * FROM rates WHERE id = ?");

	$query->bind_param("i", $lastid); //getting last element
	$query->execute();
	$array = $query->get_result();
	$to_be_converted_obj = [];

	while($amount = $array->fetch_assoc()){
    $to_be_converted_obj[] = $amount;
	}

	$to_be_converted_json = json_encode($to_be_converted_obj);

	$myObj = json_decode($to_be_converted_json, true); //accessing JSON properties
	$amount_to_convert = $myObj[0]["amount"];
	$updated_rate = $myObj[0]["rate"];
	$currency = $myObj[0]["currency"];

	if($currency == "LBP"){ //converting to USD 
		$conversion =number_format((float)($amount_to_convert / $updated_rate), 2, '.', '');
		$converted_currency = 'USD';

	}
	else if($currency == "USD"){ //converting to LBP
		$conversion =number_format((float)($amount_to_convert * $updated_rate), 3, ',', ',');
		$converted_currency = 'LBP';
	}
	else{
		echo "currency not available";
	}

	$converted_obj['amount'] = $conversion; //inserting new data into new object
	$converted_obj['rate'] = $updated_rate;
	$converted_obj['currency'] = $converted_currency;


	$to_post['recieved'] = $to_be_converted_obj;
	$to_post['converted'] = $converted_obj;
	$to_post['status'] = $server_response;

	$to_post_json = json_encode($to_post);
	echo $to_post_json;


?>

