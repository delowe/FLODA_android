<?php
/**
 * Created by PhpStorm.
 * User: Bartek
 * Date: 01/09/2018
 * Time: 23:58
 */

$server ='www.serwer1727017.home.pl';
$dbname='24939152_0000008';
$username='24939152_0000008';
$password='zserTy3$2s6';
$temperature = (int)$_GET["temperature"];
$soil = (int)$_GET["soil"];
$ph = $_GET["ph"];
$humidity = (int)$_GET["humidity"];
$sun = (int)$_GET["sun"];
$nr = (int)$_GET["nr"];
$haslo = md5($_GET["haslo"]);
    $conn = new mysqli($server,$username,$password,$dbname);
    if($conn->connect_error){
        $response["success"] = false;
        die('Connection failed');
          
    }
    if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
            $ip = $_SERVER['HTTP_CLIENT_IP'];
        } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
            $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
        } else {
            $ip = $_SERVER['REMOTE_ADDR'];
}

    if($temperature!=NULL){
        $result = $conn->query("select soil,watered from floda_log where nr_floda=$nr order by date desc limit 1");
    if($result->num_rows==1){
        while($row=$result->fetch_array()){
        if($row["soil"]<$soil){
        $sql="INSERT INTO floda_log(`temperature`,`soil`,`ph`,`humidity`,`sun`,`IP`,`nr_floda`,`haslo`,`watered`) values ('$temperature','$soil','$ph','$humidity','$sun','$ip',$nr,'$haslo',current_timestamp)";
        echo("perun");
        }else{
            $foo=$row["watered"];
            $sql="INSERT INTO floda_log(`temperature`,`soil`,`ph`,`humidity`,`sun`,`IP`,`nr_floda`,`haslo`,`watered`) values ('$temperature','$soil','$ph','$humidity','$sun','$ip',$nr,'$haslo','$foo')";
            echo("klops");
        }
        }
        $conn->query($sql);
    }else{
        $sql="INSERT INTO floda_log(`temperature`,`soil`,`ph`,`humidity`,`sun`,`IP`,`nr_floda`,`haslo`,`watered`) values ('$temperature','$soil','$ph','$humidity','$sun','$ip',$nr,'$haslo',current_timestamp)";
        $conn->query($sql); 
    }
   
    
    }else{
      die('Brak danych');
    }
    $conn->close();
?>
		