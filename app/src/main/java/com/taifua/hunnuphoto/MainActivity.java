package com.taifua.hunnuphoto;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
    private BottomNavigationView bottomNavigationView;
    private FacenetFragment facenetFragment = new FacenetFragment();
    private PhotoFragment photoFragment = new PhotoFragment();
    private FaceFragment albumFragment = new FaceFragment();
    private ShareFragment shareFragment = new ShareFragment();
    private ThingsFragment thingsFragment = new ThingsFragment();
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 申请存储空间读写权限和相机权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

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

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                mDrawerLayout.closeDrawers();
                viewPager.setCurrentItem(menuItem.getOrder());
                viewPager.setOffscreenPageLimit(4);
                return true;
            }
        });

        // 切换不同页面
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
                        return facenetFragment;
                    case 2:
                        return thingsFragment;
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
     * 连按两次返回退出程序
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit()
    {
        if (System.currentTimeMillis() - exitTime > 2000)
        {
            Toast.makeText(MainActivity.this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else
        {
            finish();
            System.exit(0);
        }
    }

    /*
     * 授权后回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
    }

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
            case R.id.take_photo:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
                break;
            case R.id.about:
                about();
                break;
            case R.id.feedback:
                feedback();
                break;
            case R.id.share:
                share();
                break;
            default:
                break;
        }
        return true;
    }


    /*
     * 关于对话
     */

    private void about()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("关于").setMessage("本应用为基于深度学习的图像识别的相册平台，祝您使用愉快~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                }).show();
    }

    private void feedback()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("反馈").setMessage("感谢您的使用，问题反馈请联系邮箱：taifu@taifua.com")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                }).show();
    }

    /*
     * 自定义分享
     */
    private void share()
    {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "HunnuPhoto");
        share_intent.putExtra(Intent.EXTRA_TEXT, "欢迎使用HunnuPhoto！");
        share_intent = Intent.createChooser(share_intent, "HunnuPhoto");
        startActivity(share_intent);
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
