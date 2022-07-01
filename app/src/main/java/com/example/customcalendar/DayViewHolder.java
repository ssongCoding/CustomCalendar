package com.example.customcalendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 1일을 끼울 홀더
 */
public class DayViewHolder extends RecyclerView.ViewHolder {

    // day.xml에 있는 1day Layout
    TextView tv_day, tv_todo;

    public DayViewHolder(@NonNull View itemView, CustomCalendarAdapter.OnItemClickListener listener) {
        super(itemView);

        // View 연결
        this.tv_day = itemView.findViewById(R.id.tv_day);
        this.tv_todo = itemView.findViewById(R.id.tv_todo);

        /*** Item Click Listener 시작 ***/
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    // MainActivity에서 item 값 읽어서 쓸 수 있게 해주려고.
                    // listener에 position 담아서 adapter로 올려줌.
                    if (listener != null){
                        listener.onItemClick(view, position);
                    }
                }
            }
        });
        /*** Item Click Listener 끝 ***/
    }
}
