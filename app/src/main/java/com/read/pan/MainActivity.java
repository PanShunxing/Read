package com.read.pan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.read.pan.activity.CollectActivity;
import com.read.pan.activity.LoginActivity;
import com.read.pan.activity.SearchResultActivity;
import com.read.pan.activity.UserInfoActivity;
import com.read.pan.adapter.FragmentAdapter;
import com.read.pan.app.ReadApplication;
import com.read.pan.entity.User;
import com.read.pan.fragment.BookshelfFragment;
import com.read.pan.fragment.GenresFragment;
import com.read.pan.fragment.StoreFragment;
import com.squareup.picasso.Picasso;
import com.yamin.reader.activity.FileBrowserActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BookshelfFragment.OnFragmentInteractionListener,
        StoreFragment.OnFragmentInteractionListener,
        GenresFragment.OnFragmentInteractionListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindString(R.string.tablayoutTitle1)
    String tablayoutTitle1;
    @BindString(R.string.tablelayoutTitle2)
    String tablayoutTitle2;
    @BindString(R.string.genres)
    String tablayoutTitle3;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    ImageView avatar;
    TextView navUsername;
    List<String> tablayoutTitle;
    List<Fragment> viewPagerFragments;
    ReadApplication application;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    private SearchHistoryTable mHistoryDatabase;
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bindListener();
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化TabLayout的title
        tablayoutTitle = new ArrayList<>();
        tablayoutTitle.add(tablayoutTitle1);
        tablayoutTitle.add(tablayoutTitle2);
        tablayoutTitle.add(tablayoutTitle3);
        //初始化ViewPager的数据集
        viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new BookshelfFragment());
        viewPagerFragments.add(new StoreFragment());
        viewPagerFragments.add(new GenresFragment());
        application = (ReadApplication) getApplication();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setSupportActionBar(toolbar);
        tabLayout.addTab(tabLayout.newTab().setText(tablayoutTitle.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tablayoutTitle.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(tablayoutTitle.get(2)));
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), viewPagerFragments, tablayoutTitle);
        viewPager.setAdapter(adapter);
        //千万别忘了，关联TabLayout与ViewPager
        //同时也要覆写PagerAdapter的getPageTitle方法，否则Tab没有title
        tabLayout.setupWithViewPager(viewPager);
        setSearchView();
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                drawer.openDrawer(GravityCompat.START); // finish();
                perm(Manifest.permission.RECORD_AUDIO, 0);
            }
        });
    }
    private void perm(String permission, int permission_request) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, permission_request);
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (application.isLogin()) {
            User user = application.getLoginUser();
            if (user != null) {
                navUsername.setText(user.getUserName());
            }
            if(user.getAvatar()!=null){
                Picasso.with(getBaseContext()).load(user.getAvatar()).into(avatar);
            }
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 注册控件监听事件
     */
    private void bindListener() {
        navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View navHeadView = navView.getHeaderView(0);
        avatar = (ImageView) navHeadView.findViewById(R.id.nav_avatar);
        navUsername = (TextView) navHeadView.findViewById(R.id.nav_username);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!application.isLogin()) {
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                }else{
                    startActivity(new Intent(getBaseContext(), UserInfoActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                //主界面左上角的icon点击反应
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_imp:
                //文件夹导入书籍
                intent = new Intent(this, FileBrowserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_like:
                //喜欢的图书
                intent=new Intent(this, CollectActivity.class);
                startActivity(intent);
                break;
//            case R.id.nav_download:
//                //下载的图书
//                break;
//            case R.id.nav_setting:
//                //设置
//                break;
//            case R.id.nav_share:
//                //分享
//                break;
//            case R.id.nav_feedback:
//                //反馈
//                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
protected void setSearchView() {
    mHistoryDatabase = new SearchHistoryTable(this);

    mSearchView = (SearchView) findViewById(R.id.searchView);
    if (mSearchView != null) {
        mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
        mSearchView.setHint("Search");
        mSearchView.setTextSize(16);
        mSearchView.setHint("Search");
        mSearchView.setDivider(false);
        mSearchView.setVoice(true);
//        mSearchView.setVoiceText("Set permission on Android 6+ !");
        mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
        mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.close(false);
                getData(query, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }
        });

        List<SearchItem> suggestionsList = new ArrayList<>();
        suggestionsList.add(new SearchItem("search1"));
        suggestionsList.add(new SearchItem("search2"));
        suggestionsList.add(new SearchItem("search3"));

        SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSearchView.close(false);
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                getData(query, position);
            }
        });

        mSearchView.setAdapter(searchAdapter);
    }
}   private void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));
        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
        intent.putExtra("version", SearchView.VERSION_TOOLBAR);
        intent.putExtra("version_margins", SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        intent.putExtra("theme", SearchView.THEME_LIGHT);
        intent.putExtra("bookName", text);
        startActivity(intent);
    }

}
