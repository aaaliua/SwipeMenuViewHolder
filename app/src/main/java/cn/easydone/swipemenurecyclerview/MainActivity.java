package cn.easydone.swipemenurecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UltimateRecyclerView ultimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.recyclerview);
        ultimateRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ultimateRecyclerView.setLayoutManager(layoutManager);
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("EasyDone\tEasyDone");
        }
        SwipeAdapter swipeAdapter = new SwipeAdapter(this, mList);
        ultimateRecyclerView.setAdapter(swipeAdapter);
    }
}
