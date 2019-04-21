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

package com.jlu.zhihu.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlu.zhihu.R;
import com.jlu.zhihu.adapter.FragmentAdapter;
import com.jlu.zhihu.api.AnswerApi;
import com.jlu.zhihu.api.QuestionApi;
import com.jlu.zhihu.fragment.ListFragment;
import com.jlu.zhihu.fragment.ScrollToHeadListener;
import com.jlu.zhihu.fragment.UserFragment;
import com.jlu.zhihu.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.footer_bar)
    TabLayout footerBar;

    private Fragment currentFragment;

    private int[] active = new int[]{R.drawable.tab_home_active, R.drawable.tab_question_active,
            R.drawable.tab_article_active, R.drawable.tab_idea_active, R.drawable.tab_my_active};
    private int[] normal = new int[]{R.drawable.tab_home, R.drawable.tab_question,
            R.drawable.tab_article, R.drawable.tab_idea, R.drawable.tab_my};
    private int[] title = new int[]{R.string.tab_home, R.string.tab_question,
            R.string.tab_article, R.string.tab_idea, R.string.tab_my};

    private final List<Fragment> fragments = new ArrayList<Fragment>() {
        {
            add(ListFragment.newInstance(AnswerApi.getInstance()));
            add(ListFragment.newInstance(QuestionApi.getInstance()));
//            add(ListFragment.newInstance(QuestionApi.getInstance()));
//            add(ListFragment.newInstance(QuestionApi.getInstance()));
//            add(ListFragment.newInstance(QuestionApi.getInstance()));
            add(new UserFragment());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));
        footerBar.setupWithViewPager(viewPager);
        initFooterTab();
    }

    private void initFooterTab() {
        for (int i = 0; i < footerBar.getTabCount(); i++) {
            footerBar.getTabAt(i).setCustomView(R.layout.view_footer_tab);
            ((TextView) footerBar.getTabAt(i).getCustomView().findViewById(R.id.tab_text)).setText(title[i]);
        }

        footerBar.addOnTabSelectedListener(this);
        for (int i = 1; i <= footerBar.getTabCount(); i++) {
            viewPager.setCurrentItem(i % footerBar.getTabCount());
        }
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        currentFragment = fragments.get(position);
        Drawable drawable = getDrawable(active[position]);
        ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon)).setImageDrawable(drawable);
        int color = getColor(R.color.color_normal_button);
        ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setTextColor(color);
        LogUtil.d(TAG, "onTabSelected: %d", position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        Drawable drawable = getDrawable(normal[position]);
        ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon)).setImageDrawable(drawable);
        int color = getColor(R.color.color_black);
        ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setTextColor(color);
        LogUtil.d(TAG, "onTabUnselected: %d", position);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        ((ScrollToHeadListener) currentFragment).onScrollToHead();
        LogUtil.d(TAG, "onTabReselected: %d", position);
    }
}

