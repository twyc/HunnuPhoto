package com.taifua.material;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 在这里把所有碎片和控件建好
 * 然后再在fragment类里面写不同的页面
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{

    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private NavigationView navView;
    private SearchView searchView;
    private BottomNavigationView bottomNavigationView;
    private PhotoFragment photoFragment = new PhotoFragment();
    private AlbumFragment albumFragment = new AlbumFragment();
    private AssistantFragment assistantFragment = new AssistantFragment();
    private ShareFragment shareFragment = new ShareFragment();

    /*
     * 菜单键
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /*
     * 菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.more:
                Toast.makeText(this, "You clicked Setting", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int i)
            {
                switch (i)
                {
                    case 0:
                        return photoFragment;
                    case 1:
                        return albumFragment;
                    case 2:
                        return assistantFragment;
                    case 3:
                        return shareFragment;
                }
                return null;
            }

            @Override
            public int getCount()
            {
                return 4;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                viewPager.setCurrentItem(menuItem.getOrder());
                viewPager.setOffscreenPageLimit(4);
                return true;
            }
        });
    }


    /*
     * 页面切换控制
     */

    @Override
    public void onPageScrolled(int i, float v, int i1)
    {

    }

    @Override
    public void onPageSelected(int i)
    {
        bottomNavigationView.getMenu().getItem(i).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i)
    {

    }
}
