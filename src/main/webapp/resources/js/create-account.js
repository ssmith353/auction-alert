$(window).load(function() {
	var createAccountForm = $('#createAccountForm');

	$("#emailAddress, #password").tooltipster({
		trigger: 'custom',
		onlyOne: false,
		position: 'bottom'
	});

	createAccountForm.validate({
		errorPlacement: function(error, element) {
			var lastError = $(element).data('lastError');
			var newError = $(error).text();

			$(element).data('lastError', newError);

			if (newError !== '' && newError !== lastError) {
				$(element).tooltipster('content', newError);
				$(element).tooltipster('show');
			}
		},
		success: function(label, element) {
			$(element).tooltipster('hide');
		},
		rules: {
			emailAddress: {
				minlength: 3,
				maxlength: 255,
				required: true,
				email: true
			},
			password: {
				required: true,
				minlength: 6
			}
		}
	});

	var handler = StripeCheckout.configure({
		key: $('#stripePublishableKey').val(),
		image: "resources/images/favicon.ico",
		name: "Auction Alert",
		description: "Subscription ($1.00 per month)",
		amount: "100",
		panelLabel: "Subscribe",
		allowRememberMe: false,
		zipCode: true,
		token: function(token) {
			createAccountForm.append($('<input id="stripeToken" name="stripeToken" type="hidden">').val(token.id));

			createAccountForm.submit();
		}
	});

	$('#createAccountSubmit').click(function(e) {
		var valid = createAccountForm.valid();

		if (valid) {
			handler.open({
				email: $('#emailAddress').val()
			});

			e.preventDefault();
		}
	});
});