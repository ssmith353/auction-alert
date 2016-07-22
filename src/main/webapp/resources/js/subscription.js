$(window).load(function() {
	var resubscribeForm = $('#resubscribeForm');
	var updateBillingForm = $('#updateBillingForm');

	var resubscribeHandler = StripeCheckout.configure({
		key: $('#stripePublishableKey').val(),
		image: "images/marketplace.png",
		name: "eBay Search",
		description: "Subscription ($9.99 per month)",
		amount: "999",
		panelLabel: "Resubscribe",
		allowRememberMe: false,
		zipCode: true,
		token: function(token) {
			resubscribeForm.append($('<input id="stripeToken" name="stripeToken" type="hidden">').val(token.id));

			resubscribeForm.submit();
		}
	});

	var updateBillingHandler = StripeCheckout.configure({
		key: $('#stripePublishableKey').val(),
		image: "images/marketplace.png",
		name: "eBay Search",
		label: "Update Card Details",
		panelLabel: "Update Card Details",
		allowRememberMe: false,
		zipCode: true,
		token: function(token) {
			updateBillingForm.append($('<input id="stripeToken" name="stripeToken" type="hidden">').val(token.id));

			updateBillingForm.submit();
		}
	});

	$('#resubscribe').click(function(e) {
		resubscribeHandler.open({
			email: $('#emailAddress').val()
		});

		e.preventDefault();
	});

	$('#updateBillingSubmit').click(function(e) {
		updateBillingHandler.open({
			email: $('#emailAddress').val()
		});

		e.preventDefault();
	});
});