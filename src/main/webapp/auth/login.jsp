<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zxx" class="no-js">

<head>
	<!-- Mobile Specific Meta -->
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<!-- Favicon-->
	<link rel="icon" href="../../assets/img/electro-logo.png" />
	<!-- Author Meta -->
	<meta name="author" content="CodePixar">
	<!-- Meta Description -->
	<meta name="description" content="">
	<!-- Meta Keyword -->
	<meta name="keywords" content="">
	<!-- meta character set -->
	<meta charset="UTF-8">
	<!-- Site Title -->
	<title>Electro</title>

	<!--
		CSS
		============================================= -->
	<link rel="stylesheet" href="../../assets/css/linearicons.css">
	<link rel="stylesheet" href="../../assets/css/owl.carousel.css">
	<link rel="stylesheet" href="../../assets/css/themify-icons.css">
	<link rel="stylesheet" href="../../assets/css/font-awesome.min.css">
	<link rel="stylesheet" href="../../assets/css/nice-select.css">
	<link rel="stylesheet" href="../../assets/css/nouislider.min.css">
	<link rel="stylesheet" href="../../assets/css/bootstrap.css">
	<link rel="stylesheet" href="../../assets/css/main.css">
	<link rel="stylesheet" href="../../assets/css/all.css">
	<link rel="stylesheet" href="../../assets/css/style.css">


</head>

<body>

	<!-- Start Header Area -->
	<header class="header_area sticky-header">
		<div class="main_menu">
			<nav class="navbar navbar-expand-lg navbar-light main_box">
				<div class="container">
					<!-- Brand and toggle get grouped for better mobile display -->

					<div class="logo-container">
						<a class="navbar-brand logo_h" href="/"
						><img src="../../assets/img/electro-logo.png" alt="" class="logo-image"
						/></a>
					</div>

					<button
							class="navbar-toggler"
							type="button"
							data-toggle="collapse"
							data-target="#navbarSupportedContent"
							aria-controls="navbarSupportedContent"
							aria-expanded="false"
							aria-label="Toggle navigation"
					>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<!-- Collect the nav links, forms, and other content for toggling -->
					<div
							class="collapse navbar-collapse offset"
							id="navbarSupportedContent"
					>
						<ul class="nav navbar-nav menu_nav ml-auto">
							<li class="nav-item active">
								<a class="nav-link" href="/">Home</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" href="/shop">Shop</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" href="#">Login</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" href="../contact.jsp">Contact</a>
							</li>
						</ul>
						<ul class="nav navbar-nav navbar-right">
							<br>
							<li class="nav-item">
								<form action="/cart" method="get" style="display: inline;">
									<button type="submit" class="search">
										<span class="ti-bag"></span>
									</button>
								</form>
							</li>
						</ul>
					</div>
				</div>
			</nav>
		</div>
	</header>
	<!-- End Header Area -->

	<!-- Start Banner Area -->
	<section class="banner-area organic-breadcrumb">
		<div class="container">
			<div class="breadcrumb-banner d-flex flex-wrap align-items-center justify-content-end">
				<div class="col-first">
					<h1>Login / Register</h1>
				</div>
			</div>
		</div>
	</section>
	<!-- End Banner Area -->

	<!--================Login Box Area =================-->
	<section class="login_box_area section_gap">
		<div class="container">
			<div class="row">
				<div class="col-lg-6">
					<div class="login_box_img">
						<img class="img-fluid" src="../../assets/img/login.jpg" alt="">
						<div class="hover">
							<h4>New to our website?</h4>
							<p>There are advances being made in science and technology everyday, and a good example of this is the</p>
							<a class="primary-btn" href="registration.jsp">Create an Account</a>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="login_form_inner">
						<h3>Log in to enter</h3>
						<form class="row login_form" action="/auth/login" method="post" id="contactForm">
							<div class="col-md-12 form-group">
								<input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
							</div>
							<div class="col-md-12 form-group">
								<input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
							</div>
							<div class="col-md-12 form-group">
								<div class="creat_account">
									<input type="checkbox" id="f-option2" name="rememberMe">
									<label for="f-option2">Keep me logged in</label>
								</div>
							</div>
							<div class="col-md-12 form-group">
								<button type="submit" value="submit" class="primary-btn">Log In</button>
							</div>

							<!-- Centering Google sign-in -->
							<div class="col-md-12 form-group text-center">
								<a id="google-signin" href="https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=http://localhost:8080/auth/grantcode&response_type=code&client_id=820523978692-81smkoloveoe7jqav826cp63sgd2201a.apps.googleusercontent.com&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline">
									<img src="https://developers.google.com/identity/images/g-logo.png" alt="Google Logo" style="height: 20px; margin-right: 10px;">
									Sign in with Google
								</a>
							</div>

							<div class="col-md-12 form-group">
								<c:if test="${not empty loginErrorResponse}">
									<div id="loginError" class="text-danger">${loginErrorResponse}</div>
								</c:if>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!--================End Login Box Area =================-->

	<!-- start footer Area -->
	<c:import url="../footer.jsp" />
	<!-- End footer Area -->


	<script src="../../assets/js/vendor/jquery-2.2.4.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
	 crossorigin="anonymous"></script>
	<script src="../../assets/js/vendor/bootstrap.min.js"></script>
	<script src="../../assets/js/jquery.ajaxchimp.min.js"></script>
	<script src="../../assets/js/jquery.nice-select.min.js"></script>
	<script src="../../assets/js/jquery.sticky.js"></script>
	<script src="../../assets/js/nouislider.min.js"></script>
	<script src="../../assets/js/jquery.magnific-popup.min.js"></script>
	<script src="../../assets/js/owl.carousel.min.js"></script>
	<!--gmaps Js-->
	<script src="https://apis.google.com/js/platform.js" async defer></script>
	<meta name="google-signin-client_id" content="820523978692-81smkoloveoe7jqav826cp63sgd2201a.apps.googleusercontent.com.apps.googleusercontent.com">

	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCjCGmQ0Uq4exrzdcL6rvxywDDOvfAu6eE"></script>
	<script src="../../assets/js/gmaps.min.js"></script>
	<script src="../../assets/js/main.js"></script>
</body>

</html>