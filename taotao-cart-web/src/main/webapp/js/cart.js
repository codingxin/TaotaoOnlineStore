var TTCart = {
	load : function(){ // 加载购物车数据
		
	},
	itemNumChange : function(){
		$(".increment").click(function(){//＋
			//$(this)是把"+"这个<a>标签转换成jquery对象，因为只有转成jquery对象才能使用它的方法
			//$(this).siblings("input");的意思是找到兄弟节点中是<input>的标签
			// siblings（) 获得匹配集合中每个元素的同胞
			var _thisInput = $(this).siblings("input");
			//_thisInput.val(eval(_thisInput.val()) + 1);这句代码的意思是找到input标签后，将数字加1
			_thisInput.val(eval(_thisInput.val()) + 1);
			/**
			 * 这句代码的意思是，点击"+"会向服务端发送post请求，请求的url的形式是：
			 * ./cart/update/num/{itemId}/{num}.action，这里需要注意的是，
			 * url的结尾不能是.html，因为我们从服务端返回的结果是个Map，
			 * 如果请求是以".html"结尾的话，浏览器会认为返回的结果是个静态页面，浏览器去尝试将Map转成html，发现根本没法转换，因此便会报406的错误
			 */
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() 
					+ ".action",function(data){
				TTCart.refreshTotalPrice();
			});
		});
		$(".decrement").click(function(){//-
			var _thisInput = $(this).siblings("input");
			if(eval(_thisInput.val()) == 1){
				return ;
			}
			_thisInput.val(eval(_thisInput.val()) - 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				TTCart.refreshTotalPrice();
			});
		});
		$(".quantity-form .quantity-text").rnumber(1);//限制只能输入数字
		$(".quantity-form .quantity-text").change(function(){
			var _thisInput = $(this);
			$.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
				TTCart.refreshTotalPrice();
			});
		});
	},
	refreshTotalPrice : function(){ //重新计算总价
		var total = 0;
		$(".quantity-form .quantity-text").each(function(i,e){
			var _this = $(e);
			total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
		});
		$(".totalSkuPrice").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
			 prefix: '￥',
			 thousandsSeparator: ',',
			 centsLimit: 2
		});
	}
};

$(function(){
	TTCart.load();
	TTCart.itemNumChange();
});