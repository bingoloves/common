package cn.cqs.baselib.skin.entity;

import android.view.View;
import java.util.ArrayList;
import java.util.List;
import cn.cqs.baselib.skin.util.ListUtils;

public class SkinItem {
	
	public View view;
	
	public List<SkinAttr> attrs;
	
	public SkinItem(){
		attrs = new ArrayList<SkinAttr>();
	}
	
	public void apply(){
		if(ListUtils.isEmpty(attrs)){
			return;
		}
		for(SkinAttr at : attrs){
			at.apply(view);
		}
	}
	
	public void clean(){
		if(ListUtils.isEmpty(attrs)){
			return;
		}
		for(SkinAttr at : attrs){
			at = null;
		}
	}

	@Override
	public String toString() {
		return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
	}
}
