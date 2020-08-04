<?php

$host = 'localhost';
$user = 'root';
$pwd = '' ;
$db = 'store';

$conn = mysqli_connect($host, $user, $pwd, $db);

if(!$conn){
	die("ERROR IN CONNECTION: " . mysqli_connect_error());
}

$response = array();

$sql_query = "select * from cars";
$result = mysqli_query($conn , $sql_query);

if(mysqli_num_rows($result) > 0){
	$response['success'] = 1;
	$cars = array();
	while ($row = mysqli_fetch_assoc($result)) {
		# code...
		array_push($cars, $row);
	}
	$response['cars'] = $cars;
}
else{
	$response['success'] = 0;
	$response['message'] = 'No data';
}
echo json_encode($response);
mysqli_close($conn);
?>