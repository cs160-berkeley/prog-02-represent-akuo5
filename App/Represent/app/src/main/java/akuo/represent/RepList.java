package akuo.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RepList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

        Button rep1 = (Button)findViewById(R.id.rep1);
        rep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepList.this, RepDetail.class);
                startActivity(intent);
            }
        });

        Button rep2 = (Button)findViewById(R.id.rep2);
        rep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("idk");
                Intent intent = new Intent(RepList.this, RepDeets2.class);
                startActivity(intent);
            }
        });

        Button rep3 = (Button)findViewById(R.id.rep3);
        rep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("idk");
                Intent intent = new Intent(RepList.this, RepDeets3.class);
                startActivity(intent);
            }
        });

    }
}
