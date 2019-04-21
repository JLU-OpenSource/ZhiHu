/*
 *    Copyright [2019] [chengjie.jlu@qq.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jlu.zhihu.api.service;

import com.jlu.zhihu.model.ListItemModel;
import com.jlu.zhihu.net.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.HashMap;
import java.util.List;

public interface ListService extends BaseService {

    String PAGE_SIZE = "5";

    String KEY_PAGE = "page";
    String KEY_SIZE = "size";

    interface ListCallback {

        void onInit(List<ListItemModel> list);

        void onRefresh(List<ListItemModel> list, RefreshLayout refreshLayout);

        void onLoadMore(List<ListItemModel> list, RefreshLayout refreshLayout);
    }

    void init();

    void refresh(RefreshLayout refreshLayout);

    void loadMore(RefreshLayout refreshLayout);

    void setListCallback(ListCallback callback);

    default Request<Void> listRequest(int page){
        Request<Void> request = new Request<>();
        request.args = new HashMap<>();
        request.args.put(KEY_PAGE, page + "");
        request.args.put(KEY_SIZE, PAGE_SIZE);
        return request;
    }
}
