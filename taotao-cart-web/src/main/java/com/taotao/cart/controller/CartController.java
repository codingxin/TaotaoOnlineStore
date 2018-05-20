package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller  
public class CartController {  
    @Autowired  
    private ItemService itemService;  
      
    @Value("${CART_KEY}")  
    private String CART_KEY;  
    @Value("${CART_EXPIRE}")  
    private Integer CART_EXPIRE;  
      
    @RequestMapping("/cart/add/{itemId}")  
    public String addItemCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,  
            HttpServletRequest request,HttpServletResponse response){  
        //取购物车商品列表  
        List<TbItem> cartItemList = getCartItemList(request);  
        //判断商品在购物车中是否存在  
        boolean flag = false;  
        for(TbItem tbItem : cartItemList){  
            //由于tbItem的ID与参数中的itemId都是包装类型的Long，要比较是否相等不要用==，因为那样比较  
            //的是对象的地址而不是值，为了让它们比较的是值，那么可以使用.longValue来获取值  
            if(tbItem.getId() == itemId.longValue()){  
                //如果存在数量相加  
                tbItem.setNum(tbItem.getNum() + num);  
                flag = true;  
                break;  
            }  
        }  
        //如果不存在，添加一个新的商品  
        if(!flag){  
            //需要调用服务取商品信息  
            TbItem tbItem = itemService.getItemById(itemId);  
            //设置购买的商品数量  
            tbItem.setNum(num);  
            //取一张图片  
            String image = tbItem.getImage();  
            if(!StringUtils.isBlank(image)){  
                String[] images = image.split(",");  
                tbItem.setImage(images[0]);  
            }  
            //把商品添加到购物车  
            cartItemList.add(tbItem);  
        }  
        //把购物车列表写入cookie  
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList),  
                CART_EXPIRE, true);  
        //返回添加成功页面  
        return "cartSuccess";  
    }  
      
    private List<TbItem> getCartItemList(HttpServletRequest request){  
        //从cookie中取购物车商品列表  
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);//为了防止乱码，统一下编码格式  
        if(StringUtils.isBlank(json)){  
            //说明cookie中没有商品列表，那么就返回一个空的列表  
            return new ArrayList<>();  
        }  
        //这个方法的作用就是将json格式的数据转换成集合格式。
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);  
        return list;  
    }  
    
    
    @RequestMapping("/cart/cart")  
    public String showCartList(HttpServletRequest request){  
        //从cookie中取购物车列表  
        List<TbItem> cartItemList = getCartItemList(request);  
        //把购物车列表传递给jsp  
        request.setAttribute("cartList", cartItemList);  
        //返回逻辑视图  
        return "cart";  
    }  
    
    @RequestMapping("/cart/update/num/{itemId}/{num}")  
    @ResponseBody  
    public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,  
            HttpServletRequest request,HttpServletResponse response){  
        //从cookie中取购物车列表  
        List<TbItem> cartItemList = getCartItemList(request);  
        //查询得到对应的商品  
        for(TbItem tbItem : cartItemList){  
            if(tbItem.getId() == itemId.longValue()){  
                //更新商品数量  
                tbItem.setNum(num);  
                break;  
            }  
        }  
        //把购物车列表写入cookie  
        CookieUtils.setCookie(request, response, CART_KEY, JSON.toJSONString(cartItemList),  
                        CART_EXPIRE, true);  
        //返回逻辑视图  
        return TaotaoResult.ok();  
    }  
    
    @RequestMapping("/cart/delete/{itemId}")  
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,  
            HttpServletResponse response){  
        //从cookie中取购物车列表  
        List<TbItem> cartItemList = getCartItemList(request);  
        //查询得到对应的商品  
        for(TbItem tbItem : cartItemList){  
            if(tbItem.getId() == itemId.longValue()){  
                //删除商品  
                cartItemList.remove(tbItem);  
                break;  
            }  
        }  
        //把购物车列表写入cookie  
        CookieUtils.setCookie(request, response, CART_KEY, JSON.toJSONString(cartItemList),  
                        CART_EXPIRE, true);  
        //重定向到购物车列表页面  
        return "redirect:/cart/cart.html";  
    }  
    
    
    
    
    
    
    
    
    
    
}  