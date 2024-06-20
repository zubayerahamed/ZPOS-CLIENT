kit.pos = kit.pos || {};

// DATA FETCHER
kit.pos.amountWithCurrency = function(amount){
	var xsign = $('#shopCurrencySign').val();
	var xposition = $('#shopCurrencyPosition').val();

	if(xposition == 'LEFT'){
		return xsign + ' ' + amount;
	} else {
		return amount + ' ' + xsign;
	}
}

kit.pos.dataFetcher = function(controllerUrl, callbackFunc){
	console.log("Data fetcher called ====> " + controllerUrl);

	loadingMask2.show();
	$.ajax({
		url: getBasepath() + controllerUrl,
		type: "GET",
		success: function (data) {
			loadingMask2.hide();
			//console.log({data});
			callbackFunc(data);
		},
		error: function (jqXHR, status, errorThrown) {
			loadingMask2.hide();
			showMessage("error", jqXHR.responseJSON.message);
		},
	});
}

// CATEGORY
kit.pos.category = kit.pos.category || {};

kit.pos.category = function(){
	kit.pos.dataFetcher('/OP80/category/all', kit.pos.category.constructMenu);
}

kit.pos.category.constructMenu = function(data){
	console.log("Constracting Category Menu ====> ");
	var categoryList = data;

	var category_ul = $('.category-ul');
	category_ul.children("li.nav-item").remove();

	var categoryAll = {
		'xname' : 'ALL',
		'xcode' : 'ALL'
	}
	var dJSON = JSON.stringify(categoryAll);
	var li_el = '<li class="nav-item" onclick=\'kit.pos.category.onclickitem(' + dJSON + ')\'>' +
					'<a href="#" class="nav-link">' +
						'<i class="ph-plus-circle me-2"></i> ' + categoryAll.xname
					'</a>' + 
				'</li>';
	category_ul.append(li_el);

	$.each(categoryList, function(i, d){
		dJSON = JSON.stringify(d);

		li_el = '<li class="nav-item" onclick=\'kit.pos.category.onclickitem(' + dJSON + ')\'>' +
					'<a href="#" class="nav-link">' +
						'<i class="ph-plus-circle me-2"></i> ' + d.xname
					'</a>' + 
				'</li>';

		category_ul.append(li_el);
	});
}

kit.pos.category.onclickitem = function(data){
	console.log('Category '+ data.xname +' clicked');
	kit.pos.item(data.xcode);
}



// ITEM
kit.pos.item = function(xcode){
	console.log("Constracting Item Showcase based on "+ xcode +" category ====> ");
	kit.pos.dataFetcher('/OP80/item/' + xcode, kit.pos.item.constructItem);
}

kit.pos.item.constructItem = function(data){
	console.log("Constracting Item Box ====> ");
	var itemList = data;
	console.log({itemList});

	var itemContainer = $('.item-container');
	itemContainer.children(".item-box").remove();

	$.each(itemList, function(i, d){
		var itemBox = '<div class="col-md-4 col-sm-4 p-1 item-box">' +
							'<div class="item-box bg-white border border-secondary rounded-1 p-2">' + 
								'<div class="p-3">' + 
									'<img src="/assets/images/placeholder.png" class="img-fluid rounded"/>' + 
								'</div>' +
								'<h6 class="p-0 m-0 text-primary text-center">' + d.xname + '</h6>' +
								'<p class="p-0 m-0 text-center">'+ kit.pos.amountWithCurrency(d.xprice) +'</p>' + 
							'</div>' +
						'</div>';
		itemContainer.append(itemBox);
	});
}


// INIT
kit.pos.init = function(){
	console.log("Init pos ====> ");
	kit.pos.category();
	kit.pos.item('ALL');
}


$(document).ready(function(){
	//kit.ui.init();
	kit.pos.init();
})