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

		var pdata = {
			xcode : d.xcode,
			xname : d.xname,
			xprice : d.xprice,
			xsetmenu: d.xsetmenu,
			sets : d.sets,
			variations: d.variations,
			addons : d.addons, 
			vat : d.vat,
			sd : d.sd,
			qty : 1, 
			lineamt : d.xprice,
			removed : false,   // used for maintain the cart array index to identify it is removed from cart after adding
			gift : false,  // use for maintain gift item
			takeaway : false,
		};

		var itemBox = 	'<div id="item-box-'+ i +'" class="col-md-4 col-sm-4 p-1 item-box">' +
							'<div class="item-box bg-white border border-secondary rounded-1 p-2">' + 
								'<div class="p-3">' + 
									'<img src="'+ source +'" class="img-fluid rounded" width="100%"/>' + 
								'</div>' +
								'<h6 class="p-0 m-0 text-primary text-center">' + d.xname + '</h6>' +
								'<p class="p-0 m-0 text-center">'+ kit.pos.amountWithCurrency(d.xprice) +'</p>' + 
							'</div>' +
						'</div>';
		itemContainer.append(itemBox);

		$('#item-box-' + i).off('click').on('click', function(){
			kit.pos.cart.addItem(pdata);
		});

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
	qty : 1, 
	lineamt : d.xprice * qty
	removed : false,
	gift: false, 
	takeaway : false
 */
kit.pos.cart.items = [];
kit.pos.cart.addItem = function(data){
	console.log({data});

	$('.cart-table tbody tr.no-item-row').remove()

	var dataindex = kit.pos.cart.items.length;
	//console.log(dataindex);
	kit.pos.cart.items.push(data);
	//console.log(kit.pos.cart.items);

	var row = `
		<tr id="row-`+ dataindex +`">
			<td scope="row" class="p-0">
				<div class="d-flex justify-content-start align-items-center">
					<div class="p-2">
						<i class="ph-trash text-danger" id="delete-row-`+ dataindex +`" data-index="`+ dataindex +`" style="cursor: pointer;"></i>
					</div>
					<div class="flex-fill item-desc" data-index="`+dataindex+`">
						<p class="m-0 text-primary fs-6">`+ data.xname +`</p>
						<p class="m-0 fw-light">variation details will be here</p>
					</div>
				</div>
			</td>
			<td class="text-center p-1">
				<div class="input-group w-lg-60 m-auto">
					<button id="qty-dec-`+dataindex+`" data-index="`+ dataindex +`" type="button" class="btn btn-sm btn-light btn-icon">
						<i class="ph-minus ph-sm"></i>
					</button>
					<input id="qty-row-` + dataindex + `" class="form-control form-control-sm form-control-number text-center numeric-only" type="number" name="number" value="`+ data.qty +`" min="1" step="1" disabled>
					<button id="qty-inc-`+dataindex+`" data-index="`+ dataindex +`" type="button" class="btn btn-sm btn-light btn-icon">
						<i class="ph-plus ph-sm"></i>
					</button>
				</div>
				<div class="mt-1">
					<p class="m-0">Price : `+ kit.pos.amountWithCurrency(data.xprice) +`</p>
				</div>
			</td>
			<td class="text-center p-1">
				<span id="lineamt-row-`+ dataindex +`">`+ (1 * data.xprice) +`</span>
			</td>
		</tr>
		<tr id="row-gift-`+ dataindex +`" class="d-none">
			<td class="text-center p-0" colspan="3">
				<div class="d-flex justify-content-around" style="width: 100%">
					<a href="" class="col text-dark gift-btn" data-index="`+ dataindex +`"><i class="ph-gift"></i> Gift</a>
					<a href="" class="col text-dark take-away-btn" data-index="`+ dataindex +`"><i class="ph-package"></i> Take Away</a>
				</div>
			</td>
		</tr>
	`;

	$('.cart-table tbody').append(row);

	$('.item-desc').off('click').on('click', function(){
		console.log('here');
		var indexnumber = $(this).data('index');
		$('#row-gift-' + indexnumber).toggleClass('d-none');
	})

	$('button#qty-inc-' + dataindex).off('click').on('click', function(){
		var indexnumber = $(this).data('index');
		var currentQty = kit.pos.cart.items[indexnumber].qty;
		var newQty = Number(currentQty) + 1;
		kit.pos.cart.items[indexnumber].qty = newQty;
		$('#qty-row-' + indexnumber).val(newQty);

		var price = kit.pos.cart.items[indexnumber].xprice;
		var lineAmt = price * newQty;
		kit.pos.cart.items[indexnumber].lineamt = lineAmt;
		$('#lineamt-row-' + indexnumber).html(lineAmt);
	})

	$('button#qty-dec-' + dataindex).off('click').on('click', function(){
		var indexnumber = $(this).data('index');
		var currentQty = kit.pos.cart.items[indexnumber].qty;
		var newQty = currentQty - 1;
		if(newQty < 1){
			$('tr#row-' + indexnumber).remove();
			$('#row-gift-' + indexnumber).remove();
			kit.pos.cart.items[indexnumber].removed = true;
			return;
		}
		kit.pos.cart.items[indexnumber].qty = newQty;
		$('#qty-row-' + indexnumber).val(newQty);

		var price = kit.pos.cart.items[indexnumber].xprice;
		var lineAmt = price * newQty;
		kit.pos.cart.items[indexnumber].lineamt = lineAmt;
		$('#lineamt-row-' + indexnumber).html(lineAmt);
	})

	$('#delete-row-' + dataindex).off('click').on('click', function(){
		var indexnumber = $(this).data('index');
		$('tr#row-' + indexnumber).remove();
		$('#row-gift-' + indexnumber).remove();
		kit.pos.cart.items[indexnumber].removed = true;
	})

	$('.gift-btn').off('click').on('click', function(e){
		e.preventDefault();
		var indexnumber = $(this).data('index');
		var isForGift = kit.pos.cart.items[indexnumber].gift;
		if(isForGift){
			isForGift = false;
			$(this).removeClass('bg-success text-white')
			$(this).addClass('text-dark');
		} else {
			isForGift = true;
			$(this).addClass('bg-success text-white')
			$(this).removeClass('text-dark');
		}
		kit.pos.cart.items[indexnumber].gift = isForGift;
	});

	$('.take-away-btn').off('click').on('click', function(e){
		e.preventDefault();
		var indexnumber = $(this).data('index');
		var isForGift = kit.pos.cart.items[indexnumber].gift;
		if(isForGift){
			isForGift = false;
			$(this).removeClass('bg-warning text-white')
			$(this).addClass('text-dark');
		} else {
			isForGift = true;
			$(this).addClass('bg-warning text-white')
			$(this).removeClass('text-dark');
		}
		kit.pos.cart.items[indexnumber].gift = isForGift;
	});

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