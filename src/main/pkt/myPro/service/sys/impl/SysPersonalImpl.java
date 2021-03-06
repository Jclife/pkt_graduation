package myPro.service.sys.impl;

import myPro.bean.seller.Goods;
import myPro.bean.seller.GoodsPo;
import myPro.bean.seller.Store;
import myPro.bean.sys.HisCartPo;
import myPro.bean.sys.HistoryGoods;
import myPro.dao.seller.SellerIndexDao;
import myPro.dao.sys.SysPersonalDao;
import myPro.service.sys.service.SysPersonalService;
import myPro.service.sys.service.SysUtilService;
import myPro.utils.common.DataConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liujun
 * @Title:
 * @Package
 * @Description:
 * @date 2018/4/5  22:35
 */
@Service
public class SysPersonalImpl implements SysPersonalService{

    @Autowired
    SysPersonalDao sys;

    public List<HistoryGoods> getHistoryGoods(int user_id,int isGetGoods) {
        return sys.getHistoryGoods(user_id,isGetGoods);
    }

    public boolean addComment(int user_id, int goods_id, int com_id,String text) {
        try {
            sys.addComment(user_id,goods_id,com_id,text);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean modifyPass(int user_id, String password) {
        try {
            sys.modifyUserPass(user_id,password);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    public List<HisCartPo> getGoodsList(List<HistoryGoods> goodsList, SysUtilService utilService) {
        List<HisCartPo> result = new ArrayList<HisCartPo>();
        List<Integer> goods_idList = new ArrayList<Integer>();
        for (int i = 0; i < goodsList.size() ; i++) {
            goods_idList.add(goodsList.get(i).getGoods_id());
        }
        List<Goods> getGoods = new ArrayList<Goods>();
        for (int i = 0; i <goods_idList.size() ; i++) {
            getGoods.add(utilService.getGoodsInId(goods_idList.get(i),1));
        }
        List<List<String>> jsonList = new ArrayList<List<String>>();
        List<GoodsPo> imgList = utilService.setImgJsonInIndex(jsonList,getGoods);
        for (int i = getGoods.size()-1; i >=0 ; i--) {
            HisCartPo cartPo = new HisCartPo();
            cartPo.setTime(goodsList.get(i).getShopTime());
            cartPo.setNum(goodsList.get(i).getGoods_count());
            cartPo.setImgs(imgList.get(i).getList().get(0));
            cartPo.setName(getGoods.get(i).getGoods_name());
            cartPo.setPrice(getGoods.get(i).getGoods_price_now());
            cartPo.setId(getGoods.get(i).getGoods_id());
            cartPo.setContent(goodsList.get(i).getContent());
            cartPo.setCom_id(goodsList.get(i).getCom_id());
            cartPo.setTotalPrice(goodsList.get(i).getGoods_count(),getGoods.get(i).getGoods_price_now());
            result.add(cartPo);
        }
        return result;
    }

    public boolean changeGoodsStatus(int com_id) {
        try {
            sys.changeGoodsStatus(com_id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<String> returnMapPoint(int com_id) {
        HistoryGoods goods = sys.getCommentPoint(com_id);
        List<String> result = new ArrayList<String>();
        Map<String,String> userMap = new HashMap<String, String>();
        try {
            userMap = DataConversion.jsonToMap(goods.getUserInfo());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //买家坐标
        result.add(0,userMap.get("lng"));
        result.add(1,userMap.get("lat"));

        //卖家坐标
        String storePoint = sys.getStorePoint(goods.getStore_id());
        result.add(2,storePoint.split(",")[0]);
        result.add(3,storePoint.split(",")[1]);

        return result;
    }
}
