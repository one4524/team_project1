package com.example.calendar_teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthViewActivity extends AppCompatActivity {
    int year;
    int month;
    ArrayList<String> days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////// 해당 년/월 TextView 정보 ///////////////////////

        //이전 액티비티에서 넘어온 값을 받아오는 부분
        // 액티비티가 처음 실행되면 디폴트값인 -1이 들어간다.
        Intent intent = getIntent();
        year = intent.getIntExtra("year", -1);
        month = intent.getIntExtra("month", -1);

        //처음 액티비티가 실행되면 현재 시간의 연도와 월을 가져온다.
        if (year == -1 || month == -1) {
            //Calendar 클래스 이용해서 현재시간의 연도와 월을 가져온다.
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
            month += 1; //Calendar.MONTH는 0~11로 월을 나타내기 때문에 +1을 해준다.
        }

        // id를 바탕으로 화면 레이아웃에 정의된 TextView 객체 로딩
        TextView yearMonthTV = findViewById(R.id.year_month);
        yearMonthTV.setText(year + "년 " + month + "월");

        ///////////////////// 달력의 요일을 나타내는 그리드뷰 //////////////////////

        String[] days_name = {"일", "월", "화", "수", "목", "금", "토"};   //요일 배열

        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용
        //요일를 나타내는 그리드뷰의 어댑터
        ArrayAdapter<String> adapt_day
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                days_name);

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview_day = (GridView) findViewById(R.id.days_gridview);
        // 어댑터를 GridView 객체에 연결
        gridview_day.setAdapter(adapt_day);


        /////////////////// 달력의 날짜를 나타내는 그리드뷰 /////////////////////

        //먼저 6 x 7 크기의 매트릭스에 날짜를 표시
        //각 월에 해당하는 1일의 요일과 마지막 날의 날짜를 인식
        //해당 월의 1일이 일요일이 아니면 앞에 공백이 필요
        //Calendar 클래스의 DAY_OF_WEEK와 DAY_OF_MONTH를 이용

        Calendar cal = Calendar.getInstance(); //Calendar 클래스를 이용해서 현재 월을 가져온다.

        cal.set(2021, month - 1, 1); // 해당 월의 1일로 날짜를 세팅한다.
        // 1일의 요일을 알기 위해서이다.

        int front_empty_day = cal.get(Calendar.DAY_OF_WEEK) - 1; // 해당 월의 첫날의 요일을 알 수 있다.
        // 1일 앞의 공백의 갯수를 알기위해서이다.
        // 일요일(=1)부터 토요일(=7)까지 1~7로 표현됨.

        int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  //해당 월의 마지막 날짜를 알 수 있다.

        int end_empty_day = 42 - (front_empty_day + lastday);   //6x7의 모양을 유지하기위해 필요한 공백

        days = new ArrayList<String>(); //날짜 리스트 생성


        for (int i = 0; i < front_empty_day; i++) days.add(" "); //1일 앞의 공백
        for (int i = 1; i <= lastday; i++)
            days.add(String.valueOf(i));  //해당 월의 1일부터 마지막날까지 순서대로 넣음.
        for (int i = 0; i < end_empty_day; i++) days.add(" ");   // 모양 유지 공백

        //날짜를 나타내는 그리드뷰의 어댑터
        ArrayAdapter<String> adapt_cal
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                days);

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview_cal = (GridView) findViewById(R.id.cal_girdview);
        gridview_cal.setAdapter(adapt_cal);


        ///////////////////////그리드뷰 날짜 메세지 이벤트 처리//////////////////////
        gridview_cal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position + 1) > front_empty_day) {
                    if (position < front_empty_day + lastday) {
                        Toast.makeText(getApplicationContext(),
                                String.valueOf(year) + "." + String.valueOf(month)
                                        + "." + String.valueOf((position + 1) - front_empty_day),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /////////////////////// 이전 버튼 이벤트 처리 ///////////////////////
        Button prev_btn = findViewById(R.id.previous);
        int finalMonth = month;
        int finalYear = year;
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MonthViewActivity.class);
                if (1 != finalMonth) {
                    intent.putExtra("year", finalYear);
                    intent.putExtra("month", finalMonth - 1);
                } else {
                    intent.putExtra("year", finalYear - 1);
                    intent.putExtra("month", 12);
                }

                startActivity(intent);
                finish();
            }
        });

        /////////////////////// 다음 버튼 이벤트 처리 ///////////////////////
        Button next_btn = findViewById(R.id.next);
        int finalMonth1 = month;
        int finalYear1 = year;
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MonthViewActivity.class);
                if (finalMonth1 != 12) {
                    intent.putExtra("year", finalYear1);
                    intent.putExtra("month", finalMonth1 + 1);
                } else {
                    intent.putExtra("year", finalYear1 + 1);
                    intent.putExtra("month", 1);
                }

                startActivity(intent);
                finish();
            }
        });

    }
}
