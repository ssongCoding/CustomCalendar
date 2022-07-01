package com.example.customcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_month;

    private RecyclerView recyclerView;
    private CustomCalendarAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Day> dayList;

    /*** FirebaseDatabase 연동 ***/
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View 연결
        tv_month = findViewById(R.id.tv_month);
        recyclerView = findViewById(R.id.recyclerView);

        // holder에 넣을 데이터 추가
        int thisMonth = thisMonth();    // 오늘이 몇월인지
        tv_month.setText(String.valueOf(thisMonth) + "월");
        int dates = datesOfMonth(thisMonth);   // 달력으로 뿌리고 싶은 Month의 일수
        int startDay = startDayOfMonth(thisMonth);  // 뿌리고 싶은 Month의 1일은 무슨 요일?

        dayList = new ArrayList<>();

        int lastMonthDates = datesOfMonth(thisMonth-1); // 지난달은 며칠인지
        for (int i = 1; i < startDay; i++){ // 1일의 요일이 오기까지 이전 월의 날짜로 채우기
            dayList.add(new Day(lastMonthDates-startDay+i+1));
        }
        for (int i = 1; i <= dates; i++){
            dayList.add(new Day(i));
        }

        /*** FirebaseDatabase 연동 시작 ***/
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("2022");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Firebase Database Data 받아오기
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Day day = dataSnapshot.getValue(Day.class);
                    int pos = day.getDay() + startDay - 2;
//                    Toast.makeText(MainActivity.this, day.getDay() + " " + day.getTodo() + " " + pos, Toast.LENGTH_SHORT).show();
                    dayList.set(pos, day);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("MainActivity.java", String.valueOf(error.toException())); // 에러 출력
            }
        });
        /*** FirebaseDatabase 연동 구현 끝 ***/

        // Adapter에 데이터 전달 = 그럼 Adapter가 Holder에 전해줄거야.
        adapter = new CustomCalendarAdapter(dayList);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, 7); // LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /*** Item Click Listener 시작 ***/
        adapter.setOnItemClickListener(new CustomCalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (position >= startDay-1 ) {
                    int day = dayList.get(position).getDay();
                    Toast.makeText(MainActivity.this,
                            day + "일, positon은 " + position + "입니다.",
                            Toast.LENGTH_SHORT).show();
                    databaseReference.child("test2").child("day").setValue(day);
                    databaseReference.child("test2").child("todo").setValue("Paris");
                    dayList.get(position).setTodo("Paris");
                    adapter.notifyItemChanged(position);
                }
            }
        });
        /*** Item Click Listener 끝 ***/
    } // OnCreate() 끝

    /**
     * 월이 며칠까지 있는지 반환하는 메소드
     * @param month
     * @return 1~31
     */
    private int datesOfMonth(int month){
        Calendar cal = Calendar.getInstance();
        cal.set(2022, month-1, 1); // 0이 1월이다.
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 월의 시작이 무슨 요일인지 반환하는 메소드
     * 일요일 : 1
     * 토요일 : 7
     * @param month
     * @return 1~7
     */
    private int startDayOfMonth(int month){
        Calendar cal = Calendar.getInstance();
        cal.set(2022, month-1, 1);
        return cal.get(Calendar.DAY_OF_WEEK);   // 일요일 : 1, 토요일 : 7
    }

    /**
     * 지금은 몇월인가?
     * @return 1~12
     */
    private int thisMonth() {
        Date now = new Date();  // 오늘 날짜&시간
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        return Integer.parseInt(formatter.format(now));
    }
}