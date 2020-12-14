package cn.cqs.baselib.skin.listener;

import android.view.View;
import java.util.List;
import cn.cqs.baselib.skin.entity.DynamicAttr;

public interface IDynamicNewView {
	void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
