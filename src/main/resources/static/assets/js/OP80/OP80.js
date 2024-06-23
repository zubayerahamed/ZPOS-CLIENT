kit.pos = kit.pos || {};
kit.pos.colors = ['#000000', '#0d6efd', '#dc3545', '#198754', '#0dcaf0'];

/** =======================================
 * =========== DATA FETCHER ===============
======================================= */ 
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

/** =======================================
 * =============== CATEGORY ===============
======================================= */ 
kit.pos.category = kit.pos.category || {};

kit.pos.category = function(){
	kit.pos.dataFetcher('/OP80/category/all', kit.pos.category.constructCategoryMenu);
}

kit.pos.category.constructCategoryMenu = function(data){
	//console.log("Constracting Category Menu ====> ");
	var categoryList = data.childs;
	if(categoryList == null || categoryList.length == 0) return;

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

	// if has parent to go back from sub menu
	if(data.parent != null && data.parent != ''){

		categoryAll = {
			'xname' : 'Back',
			'xcode' : data.parent
		}

		dJSON = JSON.stringify(categoryAll);

		li_el = '<li class="nav-item" onclick=\'kit.pos.category.onclickitem(' + dJSON + ')\'>' +
					'<a href="#" class="nav-link" style="color: '+ kit.pos.colors[data.parentIndex - 1] +'">' +
						'<i class="ph-plus-circle me-2"></i> ' + categoryAll.xname
					'</a>' + 
				'</li>';
		category_ul.append(li_el);

		if(data.parentindex == 2) textColor = 'text-primary';
	}

	$.each(categoryList, function(i, d){
		dJSON = JSON.stringify(d);

		li_el = '<li class="nav-item" onclick=\'kit.pos.category.onclickitem(' + dJSON + ')\'>' +
					'<a href="#" class="nav-link" style="color: '+ kit.pos.colors[data.parentIndex] +'">' +
						'<i class="ph-plus-circle me-2"></i> ' + d.xname
					'</a>' + 
				'</li>';

		category_ul.append(li_el);
	});
}

kit.pos.category.onclickitem = function(data){
	//console.log('Category '+ data.xname +' clicked');
	kit.pos.item(data.xcode);
	kit.pos.dataFetcher('/OP80/category/' + data.xcode, kit.pos.category.constructCategoryMenu);
}




/** =======================================
 * =============== ITEM ===================
======================================= */ 
kit.pos.item = kit.pos.item || {};

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

		var source = '/assets/images/placeholder.png';
		if(d.imageBase64 != null && d.imageBase64 != ''){
			source = "data:image/png;base64," + d.imageBase64;
		}

		var dJSON = JSON.stringify({
			xcode : d.xcode,
			xname : d.xname.replace(/'/g, ""),
			xprice : d.xprice,
			xsetmenu: d.xsetmenu,
			sets : d.sets,
			variations: d.variations,
			addons : d.addons, 
			vat : d.vat,
			sd : d.sd,
		});

		//console.log(dJSON);

		var itemBox = 	'<div class="col-md-4 col-sm-4 p-1 item-box" onclick=\'kit.pos.cart.addItem(' + dJSON + ')\'>' +
							'<div class="item-box bg-white border border-secondary rounded-1 p-2">' + 
								'<div class="p-3">' + 
									'<img src="'+ source +'" class="img-fluid rounded" width="100%"/>' + 
								'</div>' +
								'<h6 class="p-0 m-0 text-primary text-center">' + d.xname + '</h6>' +
								'<p class="p-0 m-0 text-center">'+ kit.pos.amountWithCurrency(d.xprice) +'</p>' + 
							'</div>' +
						'</div>';
		itemContainer.append(itemBox);
	});
}


/** =======================================
 * =============== CART ===================
======================================= */ 
kit.pos.cart = kit.pos.cart || {};

kit.pos.cart = function(){
	$('.cart-table tbody').empty();

	var emptyRow = `
		<tr class="no-item-row">
			<td colspan="3" class="text-center">No Item added</td>
		</tr>
	`;

	$('.cart-table tbody').append(emptyRow);
}

/** SAMPLE of Parameter
 	xcode : d.xcode,
	xname : d.xname.replace(/'/g, ""),
	xprice : d.xprice,
	xsetmenu: d.xsetmenu,
	sets : d.sets,
	variations: d.variations,
	addons : d.addons, 
	vat : d.vat,
	sd : d.sd,
 */
kit.pos.cart.addItem = function(data){
	console.log({data});

	$('.cart-table tbody tr.no-item-row').remove()

	var row = `
		<tr>
			<td scope="row" class="p-0">
				<div class="d-flex justify-content-start align-items-center">
					<div class="p-2">
						<i class="ph-trash text-danger"></i>
					</div>
					<div class="flex-fill">
						<p class="m-0 text-primary fs-6">`+ data.xname +`</p>
						<p class="m-0 fw-light">variation details will be here</p>
					</div>
				</div>
			</td>
			<td class="text-center p-1">
				<div class="input-group w-lg-60 m-auto">
					<button type="button" class="btn btn-sm btn-light btn-icon" onclick="this.parentNode.querySelector('input[type=number]').stepDown()">
						<i class="ph-minus ph-sm"></i>
					</button>
					<input class="form-control form-control-sm form-control-number text-center numeric-only" type="number" name="number" value="1" min="1" step="1">
					<button type="button" class="btn btn-sm btn-light btn-icon" onclick="this.parentNode.querySelector('input[type=number]').stepUp()">
						<i class="ph-plus ph-sm"></i>
					</button>
				</div>
				<div class="mt-1">
					<p class="m-0">Price : `+ kit.pos.amountWithCurrency(data.xprice) +`</p>
				</div>
			</td>
			<td class="text-center p-1">
				`+ (1 * data.xprice) +`
			</td>
		</tr>
	`;

	$('.cart-table tbody').append(row);
}



/** =======================================
 * =============== INIT ===================
======================================= */ 
kit.pos.init = function(){
	console.log("Init pos ====> ");
	kit.pos.category();
	kit.pos.item('ALL');
	kit.pos.cart();
}


$(document).ready(function(){
	//kit.ui.init();
	kit.pos.init();
})