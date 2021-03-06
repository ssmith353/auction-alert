$(window).load(function() {
	var searchQueryForm = $('#searchQueryForm');

	$("#keywords, #maxPrice, #minPrice").tooltipster({
		trigger: 'custom',
		onlyOne: false,
		position: 'bottom'
	});

	$.validator.addMethod("decimalPlaces", function(value) {
		var pattern = new RegExp("^\\d+(?:\\.\\d{2})?$");

		return pattern.test(value);
	}, "Price must have two decimal places");

	$.validator.addMethod("lessThan", function(value, element, param) {
		var $min = $(param);

		if (this.settings.onfocusout) {
			$min.off(".validate-minPrice").on("blur.validate-minPrice", function() {
				$(element).valid();
			});
		}

		if ((value == 0) && ($min.val() == 0)) {
			return true;
		}

		return parseFloat(value) < parseFloat($min.val());
	}, "Max price must be greater than min price");

	searchQueryForm.validate({
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
			keywords: {
				required: true
			},
			minPrice: {
				decimalPlaces: true,
				lessThan: '#maxPrice'
			},
			maxPrice: {
				decimalPlaces: true
			}
		}
	});

	$('#updateSearchQuerySubmit').click(function() {
		searchQueryForm.valid();
	});

});