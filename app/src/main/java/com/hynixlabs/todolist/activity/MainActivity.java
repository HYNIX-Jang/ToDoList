package com.hynixlabs.todolist.activity;

/**
 * Created by HYNIX Jang on 2018.04.21
 * For : MainActivity
 * Github : https://github.com/HYNIX-Jang
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hynixlabs.todolist.vo.InfoVO;
import com.hynixlabs.todolist.R;
import com.hynixlabs.todolist.adapter.RecyclerAdapter;
import com.hynixlabs.todolist.dialog.AddDialog;
import com.hynixlabs.todolist.utils.DBHelper;
import com.hynixlabs.todolist.utils.UserPreference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*DB 설정*/
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Cursor cursor;

    /*Preference*/
    private UserPreference preference;

    /*RecyclerView*/
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private FloatingActionButton btnFAB;
    private TextView name, email;
    private ImageView imageView;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private View headerView;
    private Menu menu;

    private static Context context;


    /*유저 저장*/
    private String user[]; //이메일: 0 , 이름: 1
    private InfoVO vo;
    private AddDialog addDialog;
    private ArrayList<InfoVO> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = new UserPreference(getApplicationContext()); //UserPreference

        //Login 안해서 Preference에 정보를 가지고 있지 않을 때(기본값 Null) 로그인창으로 이동
        if (!preference.hasPreference()) {
            preference.resetPreference(); //혹시 몰라 하는 Preference 초기화
            goLogin(); //로그인 창으로 이동
        } else { //Login해서 Preference에 유저가 저장 되어 있을 때
            //유저를 확인 후 user: String[] 에 저장
            user = preference.whoUser();
            setContentView(R.layout.activity_main);

            /*FindViewByID*/
            recyclerView = findViewById(R.id.recyclerView);
            toolbar = findViewById(R.id.toolBar);
            btnFAB = findViewById(R.id.btnFAB);
            navigationView = findViewById(R.id.nav_view);
            drawer = findViewById(R.id.drawer_layout);

            /*Toolbar 추가*/
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("TODOLIST");

            /*Toolbar Menu 버튼*/
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
            headerView = navigationView.getHeaderView(0); //NavigationView 안의 Header 정보
            menu = navigationView.getMenu();

            /*로그인 된 사용자 정보 가져오기*/
            name = headerView.findViewById(R.id.name);
            email = headerView.findViewById(R.id.email);

            /*프로필 사진 Round 처리*/
            imageView = headerView.findViewById(R.id.imageView);
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);

            /*로그인 된 사용자 정보 설정*/
            email.setText(user[0]);
            name.setText(user[1]);

            data = new ArrayList<>(); //정보를 넣을 ArrayList 생성
            setRecyclerView(); //RecyclerView 설정
            btnFAB.setOnClickListener(new View.OnClickListener() { //추가버튼 클릭(플로팅버튼)
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Clicked FAB", Toast.LENGTH_LONG).show();
                    addDialog = new AddDialog(MainActivity.this);
                    addDialog.show(); //추가하는 Custom Dialog를 띄움
                }
            });
        }
    }

    //NavigationView창에서 뒤로가기 할 때 앱 종료방지
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    // RecyclerView 설정
    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true); //RecyclerView 크기변경 비활성화
        // RecyclerView Adapter설정
        adapter = new RecyclerAdapter(data, MainActivity.this);
        recyclerView.setAdapter(adapter);

        //LayoutManager -> LinearLayoutManager 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setData();
    }

    //데이터 설정 ( data: ArrayList<InfoVO>에 VO 삽입 )
    private void setData() {
        data.clear();
        getData();
        // RecyclerView 에 들어갈 데이터를 추가합니다.
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
    }


    //데이터를 가져오고 VO에 설정
    private void getData() {
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        System.out.println("User Email is =" + user);
        cursor = database.rawQuery("SELECT ID, EMAIL, INFO, LIKES FROM INFO WHERE EMAIL = ?",
                new String[]{user[0]}); //이메일로 검색
        while (cursor.moveToNext()) {
            vo = new InfoVO(); //VO 생성
            vo.setId(cursor.getInt(0)); //ID
            vo.setEmail(cursor.getString(1)); //EMAIL
            vo.setInfo(cursor.getString(2)); //INFO(내용)
            vo.setLikes(cursor.getInt(3) > 0); //getBoolean() 없음 -> TRUE, FALSE 판별
            data.add(vo); //추가
            System.out.println("ID = " + vo.getId() + " Email = " + vo.getEmail() +
                    " Info = " + vo.getInfo() + " Likes = " + vo.isLikes());
        }
        //DB 닫기
        database.close();
        dbHelper.close();
    }

    //앱을 종료할 때
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //자동로그인이 비활성화일 때
        if (!preference.isLogin()) {
            preference.resetPreference(); // 유저정보 삭제
            /*유저정보를 삭제하면 앱 실행시 hasPreference로 확인해서 저장되어있는 값이 없을 때 로그인창으로 돌아감*/
        }
    }

    //NavigationItem이 선택 시
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnLogout) {
            logout(); //로그아웃
        }
        return super.onOptionsItemSelected(item);
    }

    //로그아웃 -> 로그인 Activity로 이동 포함
    public void logout() {
        //        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        //        builder.setTitle("Logout");
        //        builder.setMessage("Are you sure you want to logout?");
        //        builder.setPositiveButton("YES",
        //                new DialogInterface.OnClickListener() {
        //                    public void onClick(DialogInterface dialog, int which) {
        //                        preference.resetPreference();
        //                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //                        startActivity(intent);
        //                        finish();
        //                    }
        //                });
        //        builder.setNegativeButton("NO", null);
        //        builder.show(); -> Change to another Custom AlertDialog
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you want to logout?")
                .setConfirmText("YES")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        preference.resetPreference(); //Preference 초기화 (로그아웃)
                        sweetAlertDialog
                                .setTitleText("Successfully did it")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        goLogin();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                }).show();
    }

    //로그인 Activity로 이동
    public void goLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}

