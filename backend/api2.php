<?php
	include("db_info.php");
	header('Content-Type: application/json; charset=utf-8');

	$amount = $_POST["amount"] ;
	$rate = $_POST["rate"];
	$currency = $_POST["currency"];

// if (isset($_POST["amount"]))
// {
//   $amount = $_POST["amount"];
//   echo json_decode($amount);
// } 
// else 
// {
//   $amount = null;
//   echo "no amount supplied\n";
// }

// if (isset($_POST["rate"]))
// {
//   $rate = $_POST["rate"];
// } 
// else 
// {
//   $rate = null;
//   echo "no rate supplied\n";
// }

// if (isset($_POST["result"]))
// {
//   $result = $_POST["result"];
// } 
// else 
// {
//   $result = null;
//   echo "no result supplied\n";
// }


	$query = $mysqli->prepare("INSERT INTO rates (amount, rate, currency) VALUES (?, ?, ?)");
	$query->bind_param("sss", $amount, $rate, $currency);
	$query->execute();
	$response = [];
	$response["status"] = "Success";
	$json_response = json_encode($response);
	echo $json_response;
?>

