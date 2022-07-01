package com.example.customcalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView의 핵심!
 * list_item(아이템 레이아웃)에 데이터(는 메인에서 받을 예정)를 연결해줄 어댑터
 */
public class CustomCalendarAdapter extends RecyclerView.Adapter<DayViewHolder> {

    private List<Day> dayList;

    // 생성자
    // MainActivity에서 Adapter 생성할 때, 데이터를 넣어줄거야.
    public CustomCalendarAdapter(List<Day> dayList) {
        this.dayList = dayList;
    }

    /*** Item Click Listener 시작 ***/
    public interface OnItemClickListener { // MainActivity에서 구현해서 setOnItemClickListener에 넣어 쓸거야
        void onItemClick(View v, int positon);
    }

    private OnItemClickListener listener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    /*** Item Click Listener 끝 ***/

    /**
     * 자동 호출 메소드 - Holder 생성
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day, parent, false);
        DayViewHolder holder = new DayViewHolder(view, listener); // Holder 생성할 때 MainActivity가 만들어준 Listener도 같이 전해줌
        return holder;
    }

    /**
     * RecyclerView가 Adapter 통해서 하는 일 중 가장 중요한 일
     * Holder와 데이터를 연결해줌
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.tv_day.setText(String.valueOf(dayList.get(position).getDay()));
        holder.tv_todo.setText(dayList.get(position).getTodo());
    }

    // 아이템이 몇개인지 세서 뿌려줌.
    @Override
    public int getItemCount() {
        return (dayList != null ? dayList.size() : 0);
    }

}
