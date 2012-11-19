<?php
	if(isset($_POST['loginSubmit'])){
		$loginArr = $adminsys->logon($_POST['username'], $_POST['password']);
		if ($loginArr != null) {
			?>
	<script>
		window.location.reload();
	</script>
			<?php
		}
		else {
			$error = true;
		}
	}
?>

<style rel="stylesheet">
	body {
		padding-top: 40px;
		padding-bottom: 40px;
		background-color: #f5f5f5;
	}

	.form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
	}
	.form-signin .form-signin-heading,
	.form-signin .checkbox {
        margin-bottom: 10px;
    }
	.form-signin input[type="text"],
	.form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
    }
</style>
<h1 style="text-align:center;">
	Sheep finder admin panel
</h1><br />

<form class="form-signin" method="post" action="">
	<h2 class="form-signin-heading">Please sign in</h2>
	<?php
		if ($error) {
			echo '<div class="alert alert-error"><b>Error!</b> Wrong username or password</div>';
		}
	?>
	<input name="username" type="text" class="input-block-level" placeholder="Username">
	<input name="password" type="password" class="input-block-level" placeholder="Password">
	<!--<label class="checkbox">
		<input type="checkbox" value="remember-me"> Remember me
	</label>-->
	<button name="loginSubmit" class="btn btn-large btn-primary" type="submit">Sign in</button>
</form>